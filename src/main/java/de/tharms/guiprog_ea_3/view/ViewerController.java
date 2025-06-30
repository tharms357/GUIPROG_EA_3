package de.tharms.guiprog_ea_3.view;

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
import javafx.scene.transform.Translate;
import javafx.stage.Stage;


public class ViewerController extends Application
{

    private Group sceneRoot = new Group();
    private final Group axesGroup = new Group();
    private final Rotate cameraRotateX = new Rotate(0, Rotate.X_AXIS);
    private final Rotate cameraRotateY = new Rotate(0, Rotate.Y_AXIS);

    private MeshView meshView;


    private CameraController cameraController;
    private ModelController modelController;
    private UIController uiController;

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
        uiController = new UIController(this);
        MenuBar menuBar = uiController.createMenuBar();
        VBox rightSidebar = uiController.createSidebar();
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


    private SubScene create3DSubScene()
    {
        sceneRoot = new Group();
        sceneRoot.getTransforms().addAll(cameraRotateY, cameraRotateX);

        modelController = new ModelController();
        cameraController = new CameraController(sceneRoot, cameraRotateY, cameraRotateX);

        //setMeshView(Constants.DEFAULT_FILEPATH + "cube_ascii.stl");

        axesGroup.getChildren().add(createCoordinateAxes());

        sceneRoot.getChildren().addAll(modelController.getModelGroup(), axesGroup, setupLighting());

        SubScene subScene = new SubScene(cameraController.getRootGroup(), 1000, 700, true, SceneAntialiasing.BALANCED);
        subScene.setFill(Color.LIGHTGRAY);
        subScene.setCamera(cameraController.getCamera());

        cameraController.attachMouseControl(subScene);
        modelController.attachMouseControl(subScene);

        return subScene;
    }

    public void setMeshView(String filepath)
    {
        meshView = PolyhedronRenderer.createMesh(
                STLReader.createPolyhedronFromSTL(filepath));

        if (meshView.getMaterial() == null)
        {
            //TODO methode für setdefaults
            meshView.setMaterial(new PhongMaterial(Color.GREEN));
        }

        modelController.setMesh(meshView);
    }


    public void updateShowAxis(boolean visible)
    {
        axesGroup.setVisible(visible);
    }

    public void updateDrawMode(boolean wireframe)
    {
        if (meshView != null)
        {
            if (wireframe)
            {
                meshView.setDrawMode(DrawMode.LINE);
            }
            else
            {
                meshView.setDrawMode(DrawMode.FILL);
            }
        }
    }
    public void resetModel()
    {
        if (modelController != null)
        {
            modelController.resetModel();
        }
    }

    public void resetCameraView()
    {
        if (cameraController != null)
        {
            cameraController.resetView();
        }
    }

    private Group setupLighting()
    {
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