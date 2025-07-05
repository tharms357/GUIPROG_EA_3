package de.tharms.guiprog_ea_3.controller;

import de.tharms.guiprog_ea_3.model.Axis;
import de.tharms.guiprog_ea_3.model.Constants;
import javafx.scene.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

/**
 * Steuert die Kamera in der 3D-Ansicht und verarbeitet Benutzer-Interaktionen via Maus und Scroll.
 */
public class CameraController
{
    private final Group cameraGroup = new Group();
    private final PerspectiveCamera camera = new PerspectiveCamera(true);

    //private final Translate rootTranslate = new Translate(0, 0, Constants.TRANSLATE_DEFAULT_VALUE);
    private final Translate cameraTranslate = new Translate(0, 0, Constants.CAMERA_DEFAULT_DISTANCE);
    private final Rotate cameraRotateX = new Rotate(0, Rotate.X_AXIS);
    private final Rotate cameraRotateZ = new Rotate(0, Rotate.Y_AXIS);

    /*
    private final Rotate externalRotateY;
    private final Rotate externalRotateX;

    private final Rotate cameraRotateX;
    private final Rotate cameraRotateY;

     */

    private double anchorX, anchorY;
    //private double anchorAngleX, anchorAngleY;
    private double anchorPanX, anchorPanZ;

    //TODO
    public CameraController()
    {
        /*
        this.cameraRotateX = cameraRotateX;
        this.cameraRotateY = cameraRotateY;
        this.externalRotateX = externalRotateX;
        this.externalRotateY = externalRotateY;
        sceneRoot.getTransforms().addAll(externalRotateY, externalRotateX);
         */

        cameraGroup.getTransforms().addAll(cameraRotateX, cameraRotateZ, cameraTranslate);
        cameraGroup.getChildren().add(camera);

        //rootGroup.getTransforms().add(rootTranslate);

        resetView();
    }

    public void rotateCamera(Axis axis, double value, double anchor)
    {
        switch (axis)
        {
            case X:
                cameraRotateX.setAngle(anchor + value);
                break;
            case Y:
                break;
            case Z:
                cameraRotateZ.setAngle(anchor + value);
                break;
            default:
                break;
        }
    }

    public void addMouseControl(SubScene subScene)
    {
        subScene.setOnMousePressed(mouseEvent -> mouseIsPressed(mouseEvent));
        subScene.setOnMouseDragged(mouseEvent -> mouseIsDragged(mouseEvent));
        subScene.setOnScroll(this::isScrolled);
    }



    private void mouseIsPressed(MouseEvent mouseEvent)
    {
        anchorX = mouseEvent.getSceneX();
        anchorY = mouseEvent.getSceneY();
        anchorPanX = cameraRotateX.getAngle();
        anchorPanZ = cameraRotateZ.getAngle();
    }



    private void mouseIsDragged(MouseEvent mouseEvent)
    {
        double deltaX = mouseEvent.getSceneX() - anchorX;
        double deltaY = mouseEvent.getSceneY() - anchorY;

        if (mouseEvent.isPrimaryButtonDown() && mouseEvent.isShiftDown())
        {
            cameraRotateZ.setAngle(anchorPanZ + deltaX * Constants.X_DEFAULT_ROTATION_FACTOR);
            cameraRotateX.setAngle(anchorPanX + deltaY * Constants.Y_DEFAULT_ROTATION_FACTOR);
        }
    }


    /**
     * Zoomt die Kamera rein oder heraus basierend auf dem Scroll-Rad der Maus.
     *
     * @param scrollEvent Das Scroll-Event, das die Delta-Werte liefert.
     * @Vorbedingung scrollEvent darf nicht null sein.
     * @Nachbedingung cameraTranslate wird im zulässigen Bereich angepasst.
     */
    private void isScrolled(ScrollEvent scrollEvent)
    {
        double zoom = cameraTranslate.getZ() + scrollEvent.getDeltaY() * Constants.DEFAULT_SCROLLING_FACTOR;
        cameraTranslate.setZ(Math.max(Constants.CAMERA_MAX_DISTANCE, Math.min(Constants.CAMERA_MIN_DISTANCE, zoom)));
    }

    /**
     * Setzt die Kamera zurück auf die Standardposition.
     *
     * @Vorbedingung Die Kamera und zugehörigen Transformationsobjekte müssen initialisiert sein.
     * @Nachbedingung Die Rotationen, Translations und Clipping-Werte der Kamera entsprechen den
     * Default-Parametern aus {@link Constants}.
     */
    public void resetView()
    {
        /*
        cameraRotateX.setAngle(Constants.CAMERA_DEFAULT_X_ANGLE);
        cameraRotateY.setAngle(Constants.CAMERA_DEFAULT_Y_ANGLE);
        rootTranslate.setZ(Constants.TRANSLATE_DEFAULT_Z);
        cameraTranslate.setZ(Constants.CAMERA_DEFAULT_Z);
         */
        cameraTranslate.setZ(Constants.CAMERA_DEFAULT_Z);
        //cameraTranslate.setY(Constants.CAMERA_DEFAULT_Y);
        cameraRotateX.setAngle(Constants.CAMERA_DEFAULT_X_ANGLE);
        cameraRotateZ.setAngle(0);
        camera.setNearClip(Constants.CAMERA_NEAR_CLIP);
        camera.setFarClip(Constants.CAMERA_FAR_CLIP);
    }

    public Group getCameraGroup()
    {
    return cameraGroup;
    }

    public PerspectiveCamera getCamera()
    {
        return camera;
    }

    public Translate getCameraTranslate() {
        return cameraTranslate;
    }

    public Rotate getCameraRotateX() {
        return cameraRotateX;
    }

    public Rotate getCameraRotateZ() {
        return cameraRotateZ;
    }
}