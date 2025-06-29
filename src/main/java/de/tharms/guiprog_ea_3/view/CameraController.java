package de.tharms.guiprog_ea_3.view;

import javafx.scene.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

public class CameraController {

    private final Group rootGroup = new Group();
    private final PerspectiveCamera camera = new PerspectiveCamera(true);
    private final Translate translate = new Translate(0, 0, -800);
    private final Translate cameraTranslate = new Translate(0, 0, -200);

    private final Rotate externalRotateY;
    private final Rotate externalRotateX;

    private double anchorX, anchorY;
    private double anchorAngleX, anchorAngleY;

    public CameraController(Group sceneRoot, Rotate rotateY, Rotate rotateX) {
        this.externalRotateX = rotateX;
        this.externalRotateY = rotateY;

        rootGroup.getChildren().add(sceneRoot);
        rootGroup.getTransforms().add(translate);

        camera.getTransforms().add(cameraTranslate);
        rootGroup.getChildren().add(camera);

        resetView();
    }

    public Group getRootGroup() {
        return rootGroup;
    }

    public PerspectiveCamera getCamera() {
        return camera;
    }

    public void attachMouseControl(SubScene scene) {
        scene.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> onMousePressed(e));
        scene.addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> onMouseDragged(e));
        scene.addEventHandler(ScrollEvent.SCROLL, e -> onScroll(e));
    }

    private void onMousePressed(MouseEvent e) {
        anchorX = e.getSceneX();
        anchorY = e.getSceneY();
        anchorAngleX = externalRotateX.getAngle();
        anchorAngleY = externalRotateY.getAngle();
    }

    private void onMouseDragged(MouseEvent e) {
        double deltaX = e.getSceneX() - anchorX;
        double deltaY = e.getSceneY() - anchorY;

        if (e.getButton() == MouseButton.PRIMARY && e.isShiftDown()) {
            externalRotateY.setAngle(anchorAngleY - deltaX * 0.01);
            externalRotateX.setAngle(anchorAngleX + deltaY * 0.01);
        }
    }

    private void onScroll(ScrollEvent e) {
        double zoom = cameraTranslate.getZ() + e.getDeltaY() * 0.5;
        cameraTranslate.setZ(Math.max(-10000, Math.min(-100, zoom)));
    }

    public void resetView() {
        externalRotateX.setAngle(-235);
        externalRotateY.setAngle(0);
        translate.setZ(-800);
        cameraTranslate.setZ(-400);
        camera.setNearClip(0.05);
        camera.setFarClip(10000);
    }
}