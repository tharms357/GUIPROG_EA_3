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

    public CameraController(Group sceneRoot, Rotate rotateY, Rotate rotateX)
    {
        this.externalRotateX = rotateX;
        this.externalRotateY = rotateY;

        rootGroup.getChildren().add(sceneRoot);
        rootGroup.getTransforms().add(translate);

        camera.getTransforms().add(cameraTranslate);
        rootGroup.getChildren().add(camera);

        resetView();
    }

    public Group getRootGroup()
    {
        return rootGroup;
    }

    public PerspectiveCamera getCamera()
    {
        return camera;
    }

    public void attachMouseControl(SubScene scene)
    {
        scene.addEventHandler(MouseEvent.MOUSE_PRESSED, mouseEvent -> mouseIsPressed(mouseEvent));
        scene.addEventHandler(MouseEvent.MOUSE_DRAGGED, mouseEvent -> mouseIsDragged(mouseEvent));
        scene.addEventHandler(ScrollEvent.SCROLL, scrollEvent -> isScrolled(scrollEvent));
    }

    private void mouseIsPressed(MouseEvent mouseEvent)
    {
        anchorX = mouseEvent.getSceneX();
        anchorY = mouseEvent.getSceneY();
        anchorAngleX = externalRotateX.getAngle();
        anchorAngleY = externalRotateY.getAngle();
    }

    private void mouseIsDragged(MouseEvent mouseEvent)
    {
        double deltaX = mouseEvent.getSceneX() - anchorX;
        double deltaY = mouseEvent.getSceneY() - anchorY;

        if (mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.isShiftDown())
        {
            externalRotateY.setAngle(anchorAngleY - deltaX * 0.1);
            externalRotateX.setAngle(anchorAngleX + deltaY * 0.1);
        }
    }

    private void isScrolled(ScrollEvent scrollEvent) {
        double zoom = cameraTranslate.getZ() + scrollEvent.getDeltaY() * 0.5;
        cameraTranslate.setZ(Math.max(-10000, Math.min(-100, zoom)));
    }

    public void resetView()
    {
        externalRotateX.setAngle(-235);
        externalRotateY.setAngle(0);
        translate.setZ(-800);
        cameraTranslate.setZ(-400);
        camera.setNearClip(0.05);
        camera.setFarClip(10000);
    }
}