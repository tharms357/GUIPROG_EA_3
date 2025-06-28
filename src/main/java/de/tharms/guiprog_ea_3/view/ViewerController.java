package de.tharms.guiprog_ea_3.view;

import de.tharms.guiprog_ea_3.model.Constants;
import de.tharms.guiprog_ea_3.utility.STLReader;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.*;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Line;
import javafx.scene.shape.MeshView;
import javafx.application.Application;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

public class ViewerController extends Application
{
    private final Rotate rotateX = new Rotate(0, Rotate.X_AXIS);
    private final Rotate rotateY = new Rotate(0, Rotate.Y_AXIS);
    private final Rotate rotateZ = new Rotate(0, Rotate.Z_AXIS);
    private final Translate distance = new Translate(0, 0, -300);

    private final Group cameraGroup = new Group();
    private final Rotate axesRotateX = new Rotate(0, Rotate.X_AXIS);
    private final Rotate axesRotateZ = new Rotate(0, Rotate.Z_AXIS);

    private final Translate translateXY = new Translate(0, 0, 0);
    private double anchorX, anchorY;
    private double anchorAngleX = 0;
    private double anchorAngleZ = 0;

    private final Group modelGroup = new Group(); // nur für das Modell
    private final Translate modelTranslate = new Translate();
    private final Rotate modelRotateX = new Rotate(0, Rotate.X_AXIS);
    private final Rotate modelRotateZ = new Rotate(0, Rotate.Z_AXIS);
    private PerspectiveCamera camera;

    private final Group sceneRoot = new Group(); // wird bei Shift rotiert
    private final Group axesGroup = new Group(); // enthält Koordinatenachsen

    private final Rotate rootRotateX = new Rotate(0, Rotate.X_AXIS);
    private final Rotate rootRotateY = new Rotate(0, Rotate.Y_AXIS);

    private final Rotate cameraRotateX = new Rotate(0, Rotate.X_AXIS);
    private final Rotate cameraRotateY = new Rotate(0, Rotate.Y_AXIS);

    private MeshView meshView;

    @Override
    public void start(Stage primaryStage) {
        meshView = PolyhedronRenderer.createMesh(
                STLReader.createPolyhedronFromSTL(Constants.DEFAULT_FILEPATH + "dragon_a.stl"));
        modelGroup.getChildren().add(meshView);
        modelGroup.getTransforms().addAll(modelRotateZ, modelRotateX, modelTranslate);
        Group axes = createCoordinateAxes();
        axes.getTransforms().addAll(axesRotateZ, axesRotateX);
        Group content = new Group(axes, modelGroup);

        // --- Szene aufbauen und Kamera setzen ---
        Group root = new Group(content, cameraGroup);
        Scene scene = new Scene(root, 800, 600, true, SceneAntialiasing.BALANCED);
        scene.setFill(Color.LIGHTGRAY);

        PerspectiveCamera camera = createPerspectiveCamera(); // Kamera erzeugen
        cameraGroup.getChildren().add(camera); // Kamera hinzufügen
        scene.setCamera(camera); // Szene bekommt die Kamera

        adjustModelControls(scene);

        primaryStage.setScene(scene);
        primaryStage.setTitle("STL Viewer: ");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }


    private Group setupLighting() {
        Group lights = new Group();

        // Grundhelligkeit (gleichmäßig von überall)
        AmbientLight ambientLight = new AmbientLight(Color.color(0.3, 0.3, 0.3));
        lights.getChildren().add(ambientLight);

        // Hauptlicht (wie Sonnenlicht von oben vorne)
        PointLight keyLight = new PointLight(Color.WHITE);
        keyLight.setTranslateX(-500);
        keyLight.setTranslateY(-300);
        keyLight.setTranslateZ(-700);

        // Gegenlicht (von hinten unten)
        PointLight fillLight = new PointLight(Color.color(0.7, 0.7, 0.7));
        fillLight.setTranslateX(400);
        fillLight.setTranslateY(300);
        fillLight.setTranslateZ(600);

        lights.getChildren().addAll(keyLight, fillLight);

        return lights;
    }


    private void adjustModelControls(Scene scene) {
        scene.setOnMousePressed(event -> {
            anchorX = event.getSceneX();
            anchorY = event.getSceneY();
            anchorAngleX = modelRotateX.getAngle();
            anchorAngleZ = modelRotateZ.getAngle();
        });

        scene.setOnMouseDragged(event -> {
            double deltaX = event.getSceneX() - anchorX;
            double deltaY = event.getSceneY() - anchorY;

            if (event.getButton() == MouseButton.PRIMARY) {
                if (event.isShiftDown()) {
                    // Kamera rotiert um die Szene
                    cameraRotateY.setAngle(cameraRotateY.getAngle() + deltaX * 0.0005);
                    cameraRotateX.setAngle(cameraRotateX.getAngle() - deltaY * 0.0005);
                } else {
                    // Nur Modell innerhalb des Koordinatensystems drehen
                    modelRotateZ.setAngle(anchorAngleZ + deltaX * 0.1);
                    modelRotateX.setAngle(anchorAngleX - deltaY * 0.1);
                }
            }

            if (event.getButton() == MouseButton.SECONDARY) {
                modelTranslate.setX(modelTranslate.getX() + deltaX);
                modelTranslate.setY(modelTranslate.getY() + deltaY);
                anchorX = event.getSceneX();
                anchorY = event.getSceneY();
            }
        });

        scene.setOnScroll(scrollEvent -> {
            double zoomFactor = scrollEvent.getDeltaY() * 0.5;
            double currentZ = distance.getZ();
            double newZ = currentZ + zoomFactor;
            distance.setZ(Math.max(-10000, Math.min(-100, newZ)));
        });
    }

    public PerspectiveCamera createPerspectiveCamera() {
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setNearClip(0.1);
        camera.setFarClip(10000);
        camera.getTransforms().addAll(cameraRotateY, cameraRotateX, distance);

        return camera;
    }

    private Cylinder create3DLine(double height, Color color) {
        Cylinder line = new Cylinder(0.5, height); // dünne Linie
        line.setMaterial(new PhongMaterial(color));
        line.setTranslateY(-height / 2); // damit die Linie vom Ursprung ausgeht
        return line;
    }

    private Group createCoordinateAxes() {
        Group axes = new Group();

        double axisLength = 200;

        // X-Achse: rot
        Cylinder xAxis = new Cylinder(0.2, axisLength);
        xAxis.setMaterial(new PhongMaterial(Color.RED));
        xAxis.setRotationAxis(Rotate.Z_AXIS);
        xAxis.setRotate(90);

        // Y-Achse: grün
        Cylinder yAxis = new Cylinder(0.2, axisLength);
        yAxis.setMaterial(new PhongMaterial(Color.GREEN));
        // (keine Rotation nötig, Y ist default-Ausrichtung)

        // Z-Achse: blau
        Cylinder zAxis = new Cylinder(0.2, axisLength);
        zAxis.setMaterial(new PhongMaterial(Color.BLUE));
        zAxis.setRotationAxis(Rotate.X_AXIS);
        zAxis.setRotate(90);
        axes.getChildren().addAll(xAxis, yAxis, zAxis);
        return axes;
    }

    private Line createLine(double startX, double startY, double startZ,
                            double endX, double endY, double endZ, Color color) {
        Line line = new Line();
        line.setStartX(startX);
        line.setStartY(startY);
        line.setEndX(endX);
        line.setEndY(endY);
        line.setStroke(color);

        // JavaFX unterstützt keine 3D-Linien direkt,
        // daher kann man z.B. Cylinder oder MeshView nutzen für echte 3D-Linien,
        // oder für visuelle Hilfe eine Alternative wie 3D-Linien via Box oder Cylinder
        // Hier: einfacher 2D-Ansatz als Platzhalter – du kannst auf 3D umstellen mit z.B. Cylinder

        return line;
    }


    private void adjustMesh(Scene scene) {
        scene.setOnMousePressed(event -> {
            anchorX = event.getSceneX();
            anchorY = event.getSceneY();
            anchorAngleX = rotateX.getAngle();
            anchorAngleZ = rotateZ.getAngle();
        });

        scene.setOnMouseDragged(event -> {
            double deltaX = event.getSceneX() - anchorX;
            double deltaY = event.getSceneY() - anchorY;

            if (event.getButton() == MouseButton.PRIMARY) {
                // Rotation um Ursprung
                rotateZ.setAngle(anchorAngleZ + deltaX * 0.7);
                rotateX.setAngle(anchorAngleX - deltaY * 0.7);
            }

            if (event.getButton() == MouseButton.SECONDARY) {
                // Pan: Szene entlang X/Y verschieben
                translateXY.setX(translateXY.getX() + deltaX);
                translateXY.setY(translateXY.getY() + deltaY);

                anchorX = event.getSceneX();
                anchorY = event.getSceneY();
            }
        });

        scene.setOnScroll(scrollEvent -> {
            double zoomFactor = scrollEvent.getDeltaY() * 0.5;
            double currentZ = distance.getZ();
            double newZ = currentZ + zoomFactor;

            newZ = Math.max(-10000, Math.min(-100, newZ)); // optional: Begrenzung
            distance.setZ(newZ);
        });
    }
}
