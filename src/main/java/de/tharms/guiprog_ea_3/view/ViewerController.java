package de.tharms.guiprog_ea_3.view;

import de.tharms.guiprog_ea_3.controller.CameraController;
import de.tharms.guiprog_ea_3.controller.ModelController;
import de.tharms.guiprog_ea_3.controller.PolyhedronController;
import de.tharms.guiprog_ea_3.controller.InteractionController;
import de.tharms.guiprog_ea_3.model.Constants;
import de.tharms.guiprog_ea_3.network.Server;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.MeshView;
import javafx.stage.Stage;

/**
 * Haupt-Controller für die JavaFX-Anwendung des STL-Viewers.
 * Initialisiert Stage, Scene und SubScene sowie die Anzeige des geladenen Polyeders.
 */
public class ViewerController extends Application
{
    //private final Rotate cameraRotateX = new Rotate(0, Rotate.X_AXIS);
    //private final Rotate cameraRotateY = new Rotate(0, Rotate.Y_AXIS);

    public final Group axesGroup = new Group();

    private Stage primaryStage;

    private InteractionController interactionController;
    private CameraController cameraController;
    private ModelController modelController;
    private PolyhedronController polyhedronController;

    private Server server;

    public void startServer()
    {
        server = new Server(10002, this);
        server.start();
    }

    public void closeServer()
    {
        server.close();
    }

    /**
     * Startmethode der JavaFX-Anwendung. Setzt die primaryStage und zeigt die Hauptszene an.
     *
     * @param primaryStage Die primäre Stage.
     * @Vorbedingung primaryStage darf nicht null sein.
     * @Nachbedingung Die Stage zeigt die geladene Szene mit Menüs und 3D-Ansicht.
     */
    @Override
    public void start(Stage primaryStage)
    {
        this.primaryStage = primaryStage;

        Scene scene = buildMainScene();
        primaryStage.setScene(scene);
        setProgramTitle(Constants.EMPTY_STRING);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream(Constants.STL_VIEWER_ICON_FILEPATH)));
        primaryStage.show();

        startServer();
    }

    /**
     * Hauptmethode, die die JavaFX-Anwendung startet.
     *
     * @param args Kommandozeilenargumente.
     * @Vorbedingung Keine.
     * @Nachbedingung Die Anwendung ist gestartet.
     */
    public static void main(String[] args)
    {
        launch(args);
    }

    /**
     * Setzt den Programmtitel in der Titelleiste, optional mit Polyedername.
     *
     * @param polyhedronName Der Name des aktuell geladenen Polyeders oder ein leerer String.
     * @Vorbedingung primaryStage ist initialisiert.
     * @Nachbedingung Der Fenstertitel enthält den Standardtitel und ggf. den Polyedernamen.
     */
    private void setProgramTitle(String polyhedronName)
    {
        if (!polyhedronName.isEmpty())
        {
            primaryStage.setTitle(
                    Constants.DEFAULT_PROGRAM_TITLE + Constants.SEPERATOR + polyhedronName);
        }
       else
       {
           primaryStage.setTitle(Constants.DEFAULT_PROGRAM_TITLE);
       }
    }

    /**
     * Baut die Hauptszene mit Menüleiste, Seitenleiste und 3D-SubScene.
     *
     * @return Die konfigurierte Haupt-{@link Scene} für das Programmfenster.
     * @Vorbedingung Keine.
     * @Nachbedingung Die erstellte Szene ist vollständig konfiguriert.
     */
    private Scene buildMainScene()
    {
        interactionController = new InteractionController();

        MenuBar menuBar = interactionController.createMenuBar(this);
        VBox rightSidebar = interactionController.createSidebar(this);

        SubScene subScene = create3DSubScene();

        AnchorPane overlay = new AnchorPane(rightSidebar);
        AnchorPane.setTopAnchor(rightSidebar, 0.0);
        AnchorPane.setRightAnchor(rightSidebar, 0.0);
        overlay.setPickOnBounds(false);

        StackPane centerStack = new StackPane(subScene, overlay);

        BorderPane root = new BorderPane();
        root.setTop(menuBar);
        root.setCenter(centerStack);

        Scene scene = new Scene(root, Constants.STL_VIEWER_WINDOW_WIDTH, Constants.STL_VIEWER_WINDOW_HEIGHT);
        subScene.widthProperty().bind(centerStack.widthProperty());
        subScene.heightProperty().bind(centerStack.heightProperty());

        return scene;
    }

    /**
     * Erstellt und konfiguriert die 3D-SubScene mit Kamera, Modell und Koordinatensystem.
     *
     * @return Die erstellte 3D-{@link SubScene}.
     * @Vorbedingung Keine.
     * @Nachbedingung Die SubScene vollständig aufgestellt.
     */
    private SubScene create3DSubScene()
    {
        Group sceneRoot = new Group();
        Group cameraRoot = new Group();
        //sceneRoot.getTransforms().addAll(cameraRotateY, cameraRotateX);

        modelController = new ModelController();
        //cameraController = new CameraController(sceneRoot, cameraRotateY, cameraRotateX);
        cameraController = new CameraController();

        sceneRoot.getChildren().addAll(modelController.getModelGroup(), interactionController.createCoordinateAxes(), cameraController.getCameraGroup());

        SubScene subScene = new SubScene(sceneRoot, Constants.STL_VIEWER_SUBSCENE_WIDTH,
                Constants.STL_VIEWER_SUBSCENE_HEIGHT, true, SceneAntialiasing.BALANCED);
        subScene.setFill(Color.LIGHTGRAY);
        subScene.setCamera(cameraController.getCamera());

        interactionController.addMouseControl(subScene, modelController, cameraController);

        return subScene;
    }

    /**
     * Aktualisiert die Sichtbarkeit des Koordinatensystems basierend auf der Checkbox.
     *
     * @Vorbedingung uiController ist initialisiert.
     * @Nachbedingung axesGroup ist sichtbar oder unsichtbar gemäß Auswahl.
     */
    public void updateShowAxis()
    {
        axesGroup.setVisible(interactionController.getShowAxis().isSelected());
        if (interactionController.getShowAxis() != null)
        {
            axesGroup.setVisible(interactionController.getShowAxis().isSelected());
        }
    }



    /**
     * Lädt ein Polyeder, erstellt ein Mesh und aktualisiert die Anzeige und Details.
     *
     * @param filepath Pfad zur STL-Datei.
     * @Vorbedingung filepath verweist auf eine gültige STL-Datei.
     * @Nachbedingung Das Modell, die Darstellungsoptionen und Details werden aktualisiert.
     */
    public void setMeshView(String filepath)
    {
        modelController.createPolyhedronFromFilepath(filepath);

        MeshView mesh = PolyhedronController.createMesh(modelController.getPolyhedron());

        modelController.setMesh(mesh);
        interactionController.updateDrawMode(mesh);
        interactionController.refreshPolyhedronDetails(modelController.getPolyhedron());
        setProgramTitle(modelController.getPolyhedron().getName());
    }

    public InteractionController getUiController()
    {
        return interactionController;
    }

    public CameraController getCameraController()
    {
        return cameraController;
    }

    public ModelController getModelController()
    {
        return modelController;
    }

    public PolyhedronController getPolyhedronController()
    {
        return polyhedronController;
    }
}