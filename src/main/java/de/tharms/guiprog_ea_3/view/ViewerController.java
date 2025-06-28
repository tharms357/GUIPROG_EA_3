package de.tharms.guiprog_ea_3.view;

import de.tharms.guiprog_ea_3.model.Constants;
import de.tharms.guiprog_ea_3.utility.STLReader;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class ViewerController extends Application {

    private final Group modelGroup = new Group();
    private final Rotate modelRotateX = new Rotate(0, Rotate.X_AXIS);
    private final Rotate modelRotateZ = new Rotate(0, Rotate.Z_AXIS);
    private final Translate modelTranslate = new Translate();

    private final Group sceneRoot = new Group();
    private final Group axesGroup = new Group();
    private final Rotate cameraRotateX = new Rotate(0, Rotate.X_AXIS);
    private final Rotate cameraRotateY = new Rotate(0, Rotate.Y_AXIS);
    private final Translate cameraDistance = new Translate(0, 0, -800);

    private MeshView meshView;
    private double anchorX, anchorY;
    private double anchorAngleX, anchorAngleZ;

    @Override
    public void start(Stage primaryStage) {
        // Menü + Sidebar + SubScene
        MenuBar menuBar = createMenuBar();
        SubScene subScene = create3DSubScene();
        VBox rightSidebar = createSidebar();

        // Overlay-UI rechts oben (für Sidebar), im StackPane fixiert
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

        primaryStage.setScene(scene);
        primaryStage.setTitle("STL Viewer");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/stl_viewer_icon.png")));
        primaryStage.show();
    }



    private SubScene create3DSubScene()
    {
        meshView = PolyhedronRenderer.createMesh(
                STLReader.createPolyhedronFromSTL(Constants.DEFAULT_FILEPATH + "cube_ascii.stl"));
        modelGroup.getChildren().add(meshView);
        modelGroup.getTransforms().addAll(modelRotateZ, modelRotateX, modelTranslate);

        modelGroup.getChildren().clear();
        modelGroup.getChildren().add(meshView);

        axesGroup.getChildren().clear();
        axesGroup.getChildren().add(createCoordinateAxes());

        sceneRoot.getChildren().clear();
        sceneRoot.getChildren().addAll(modelGroup, axesGroup, setupLighting());

        sceneRoot.getTransforms().addAll(cameraRotateY, cameraRotateX);
        sceneRoot.getTransforms().clear();
        sceneRoot.getTransforms().addAll(cameraRotateY, cameraRotateX);

        Group rootGroup = new Group(sceneRoot);
        rootGroup.getTransforms().add(cameraDistance);

        Translate cameraTranslate = new Translate(0, 0, -800);

        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setNearClip(0.1);
        camera.setFarClip(10000);
        camera.getTransforms().add(cameraTranslate);

        rootGroup.getChildren().add(camera);

        SubScene subScene = getSubScene(rootGroup, camera, cameraTranslate);

        return subScene;
    }

    private SubScene getSubScene(Group rootGroup, PerspectiveCamera camera, Translate cameraTranslate) {
        SubScene subScene = new SubScene(rootGroup, 1000, 700, true, SceneAntialiasing.BALANCED);
        subScene.setFill(Color.LIGHTGRAY);
        subScene.setCamera(camera);

        // Maussteuerung für SubScene
        subScene.setOnMousePressed(e -> {
            anchorX = e.getSceneX();
            anchorY = e.getSceneY();
            anchorAngleX = modelRotateX.getAngle();
            anchorAngleZ = modelRotateZ.getAngle();
        });

        subScene.setOnMouseDragged(e -> {
            double deltaX = e.getSceneX() - anchorX;
            double deltaY = e.getSceneY() - anchorY;

            if (e.getButton() == MouseButton.PRIMARY) {
                if (e.isShiftDown()) {
                    cameraRotateY.setAngle(cameraRotateY.getAngle() + deltaX * 0.01);
                    cameraRotateX.setAngle(cameraRotateX.getAngle() - deltaY * 0.01);
                } else {
                    modelRotateZ.setAngle(anchorAngleZ + deltaX * 0.1);
                    modelRotateX.setAngle(anchorAngleX - deltaY * 0.1);
                }
            } else if (e.getButton() == MouseButton.SECONDARY) {
                modelTranslate.setX(modelTranslate.getX() + deltaX);
                modelTranslate.setY(modelTranslate.getY() + deltaY);
                anchorX = e.getSceneX();
                anchorY = e.getSceneY();
            }
        });

        subScene.setOnScroll(scroll -> {
            double zoom = cameraTranslate.getZ() + scroll.getDeltaY() * 0.5;
            cameraTranslate.setZ(Math.max(-10000, Math.min(-100, zoom)));
        });
        return subScene;
    }

    private MenuBar createMenuBar() {
        Menu datei = new Menu("Datei");

        MenuItem openFile = new MenuItem("STL-Datei öffnen...");
        openFile.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("STL-Datei auswählen");

            File selectedFile = fileChooser.showOpenDialog(null);
            if (selectedFile != null)
            {
                PolyhedronRenderer.createMesh(STLReader.createPolyhedronFromSTL(selectedFile.getAbsolutePath()));
                //ViewerController.setMesh(Mes)
            }
        });

        MenuItem beenden = new MenuItem("Beenden");
        beenden.setOnAction(e -> System.exit(0));
        datei.getItems().add(beenden);
        datei.getItems().add(openFile);

        // Ansicht-Menü mit Rücksetzfunktionen
        Menu ansicht = new Menu("Ansicht");
        MenuItem resetModel = new MenuItem("Modell-Position zurücksetzen");
        MenuItem resetView = new MenuItem("Koordinatensystem zurücksetzen");

        resetModel.setOnAction(e -> {
            modelTranslate.setX(0);
            modelTranslate.setY(0);
            modelTranslate.setZ(0);
            modelRotateX.setAngle(0);
            modelRotateZ.setAngle(0);
        });

        resetView.setOnAction(e -> {
            cameraRotateX.setAngle(0);
            cameraRotateY.setAngle(0);
            cameraDistance.setZ(-400);
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


    private VBox createSidebar() {
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

        CheckBox showWireframe = new CheckBox("Nur Wireframe anzeigen");
        showWireframe.setOnAction(e -> {
            if (meshView != null)
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
        });

        VBox box = new VBox(15, title, infoText, showWireframe);
        box.setStyle("-fx-background-color: rgba(255,255,255,0.95); -fx-padding: 20; -fx-min-width: 250;");
        return box;
    }

    private Group setupLighting() {
        AmbientLight ambient = new AmbientLight(Color.color(0.3, 0.3, 0.3));
        PointLight key = new PointLight(Color.WHITE);
        key.setTranslateX(-500);
        key.setTranslateY(-300);
        key.setTranslateZ(-700);
        PointLight fill = new PointLight(Color.color(0.7, 0.7, 0.7));
        fill.setTranslateX(400);
        fill.setTranslateY(300);
        fill.setTranslateZ(600);
        return new Group(ambient, key, fill);
    }

    private Group createCoordinateAxes() {
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

        Group grid = createGridPlane(100, 10);

        return new Group(x, y, z, grid);
    }

    private Group createGridPlane(int size, int step) {
        Group gridGroup = new Group();
        Color gridColor = Color.GRAY;
        double thickness = 0.05;

        for (int i = -size; i <= size; i += step) {
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

    public static void main(String[] args) {
        launch(args);
    }
}