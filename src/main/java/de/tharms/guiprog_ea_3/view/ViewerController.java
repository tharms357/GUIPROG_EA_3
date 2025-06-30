package de.tharms.guiprog_ea_3.view;

import de.tharms.guiprog_ea_3.controller.PolyhedronController;
import de.tharms.guiprog_ea_3.model.Constants;
import de.tharms.guiprog_ea_3.utility.STLReader;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Rotate;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class ViewerController extends Application
{

    private Group sceneRoot = new Group();
    private final Group axesGroup = new Group();
    private final Rotate cameraRotateX = new Rotate(0, Rotate.X_AXIS);
    private final Rotate cameraRotateY = new Rotate(0, Rotate.Y_AXIS);

    private MeshView meshView;

    private CheckBox showWireframe;
    private CheckBox showAxis;

    private CameraController cameraController;
    private ModelController modelController;

    @Override
    public void start(Stage primaryStage)
    {
        Scene scene = buildMainScene();
        primaryStage.setScene(scene);
        primaryStage.setTitle("STL Viewer");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/stl_viewer_icon.png")));
        primaryStage.show();
    }

    private Scene buildMainScene()
    {
        MenuBar menuBar = createMenuBar();
        VBox rightSidebar = createSidebar();
        SubScene subScene = create3DSubScene();

        AnchorPane overlay = new AnchorPane(rightSidebar);
        AnchorPane.setTopAnchor(rightSidebar, 0.0);
        AnchorPane.setRightAnchor(rightSidebar, 0.0);
        overlay.setPickOnBounds(false); // 3D-Eingabe bleibt möglich

        StackPane centerStack = new StackPane(subScene, overlay);

        BorderPane root = new BorderPane();
        root.setTop(menuBar);
        root.setCenter(centerStack);

        Scene scene = new Scene(root, 1200, 800);
        subScene.widthProperty().bind(centerStack.widthProperty());
        subScene.heightProperty().bind(centerStack.heightProperty());

        return scene;
    }

    //TODO modularisieren
    private MenuBar createMenuBar()
    {
        Menu datei = new Menu("Datei");

        MenuItem loadSampleSTL = new MenuItem("Beispiel STL-Datei laden");
        loadSampleSTL.setOnAction(e -> {
            setMeshView(Constants.SAMPLE_STL_FILEPATH);
            modelController.resetModel();
        });

        MenuItem openFile = new MenuItem("STL-Datei öffnen...");
        openFile.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("STL-Datei auswählen");

            File selectedFile = fileChooser.showOpenDialog(null);
            if (selectedFile != null)
            {
                setMeshView(selectedFile.getAbsolutePath());
            }
        });

        MenuItem beenden = new MenuItem("Beenden");
        beenden.setOnAction(e -> System.exit(0));

        datei.getItems().addAll(loadSampleSTL, openFile, beenden);

        // Ansicht-Menü mit Rücksetzfunktionen
        Menu ansicht = new Menu("Ansicht");
        MenuItem resetModel = new MenuItem("Modell-Position zurücksetzen");
        MenuItem resetView = new MenuItem("Koordinatensystem zurücksetzen");

        resetModel.setOnAction(e -> {
            modelController.resetModel();
        });

        resetView.setOnAction(e -> {
            cameraController.resetView();
            showAxis.setSelected(true);
            showAxis.setVisible(true);
            updateShowAxis();
        });

        ansicht.getItems().addAll(resetModel, resetView);

        Menu hilfe = new Menu("Hilfe");
        MenuItem über = new MenuItem("Über STL Viewer");
        über.setOnAction(e -> {
            Alert info = new Alert(Alert.AlertType.INFORMATION);
            info.setTitle("Über");
            info.setHeaderText("STL Viewer");
            info.setContentText("Ein einfacher 3D STL-Viewer\nAutor: Du :)");
            info.showAndWait();
        });
        hilfe.getItems().add(über);

        return new MenuBar(datei, ansicht, hilfe);
    }

    private SubScene create3DSubScene()
    {
        sceneRoot = new Group();
        sceneRoot.getTransforms().addAll(cameraRotateY, cameraRotateX);

        modelController = new ModelController();
        cameraController = new CameraController(sceneRoot, cameraRotateY, cameraRotateX);

        //setMeshView(Constants.DEFAULT_FILEPATH + "cube_ascii.stl");

        axesGroup.getChildren().add(createCoordinateAxes());

        sceneRoot.getChildren().addAll(modelController.getModelGroup(), axesGroup);

        SubScene subScene = new SubScene(cameraController.getRootGroup(), 1000, 700, true, SceneAntialiasing.BALANCED);
        subScene.setFill(Color.LIGHTGRAY);
        subScene.setCamera(cameraController.getCamera());

        cameraController.attachMouseControl(subScene);
        modelController.attachMouseControl(subScene);

        return subScene;
    }

    private void setMeshView(String filepath)
    {
        meshView = PolyhedronController.createMesh(
                STLReader.createPolyhedronFromSTL(filepath));

        if (meshView.getMaterial() == null)
        {
            //TODO methode für setdefaults
            meshView.setMaterial(new PhongMaterial(Color.DARKGRAY));
        }

        modelController.setMesh(meshView);
        updateDrawMode();
    }

    //TODO modularisieren
    private VBox createSidebar()
    {
        Label title = new Label("Modell-Informationen");
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

        Label infoText = new Label("""
        - Modell: Felge_a.stl
        - Eckpunkte: ca. 12.000
        - Koordinatenursprung: (0,0,0)
        - Skalierung: 1.0x
        - Material: Phong
    """);

        infoText.setWrapText(true);

        showWireframe = new CheckBox("Nur Wireframe anzeigen");
        showWireframe.setOnAction(e -> updateDrawMode());

        showAxis = new CheckBox("Koordinatensystem anzeigen");
        showAxis.setSelected(true);
        showAxis.setOnAction(e -> updateShowAxis());

        VBox sidebar = new VBox(15, title, infoText, showWireframe, showAxis);
        sidebar.setStyle("-fx-background-color: rgba(255,255,255,0.95); -fx-padding: 20; -fx-min-width: 250;");
        return sidebar;
    }

    private void updateShowAxis()
    {
        axesGroup.setVisible(showAxis.isSelected());
    }

    private void updateDrawMode()
    {
        if (meshView != null && showWireframe != null)
        {
            if (showWireframe.isSelected())
            {
                meshView.setDrawMode(DrawMode.LINE);
            }
            else
            {
                meshView.setDrawMode(DrawMode.FILL);
            }
        }
    }



    private Group createCoordinateAxes()
    {
        double axisLength = 200;
        Cylinder x = new Cylinder(0.2, axisLength);
        x.setMaterial(new PhongMaterial(Color.RED));
        x.setRotationAxis(Rotate.Z_AXIS);
        x.setRotate(90);

        Cylinder y = new Cylinder(0.2, axisLength);
        y.setMaterial(new PhongMaterial(Color.GREEN));

        Cylinder z = new Cylinder(0.2, axisLength);
        z.setMaterial(new PhongMaterial(Color.BLUE));
        z.setRotationAxis(Rotate.X_AXIS);
        z.setRotate(90);

        Group grid = createGrid(100, 10);

        return new Group(x, y, z, grid);
    }

    public Group createGrid(int size, int step)
    {
        Group gridGroup = new Group();
        Color gridColor = Color.GRAY;
        double thickness = 0.05;

        for (int i = -size; i <= size; i += step)
        {
            // Vertikale Linie (parallel zur Y-Achse)
            Box vLine = new Box(thickness, size * 2, thickness);
            vLine.setMaterial(new PhongMaterial(gridColor));
            vLine.setTranslateX(i);
            vLine.setTranslateY(0);
            vLine.setTranslateZ(0);

            // Horizontale Linie (parallel zur X-Achse)
            Box hLine = new Box(size * 2, thickness, thickness);
            hLine.setMaterial(new PhongMaterial(gridColor));
            hLine.setTranslateX(0);
            hLine.setTranslateY(i);
            hLine.setTranslateZ(0);

            gridGroup.getChildren().addAll(vLine, hLine);
        }

        return gridGroup;
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}