package de.tharms.guiprog_ea_3.controller;

import de.tharms.guiprog_ea_3.model.Axis;
import de.tharms.guiprog_ea_3.model.Constants;
import javafx.scene.*;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

/**
 * Steuert die Kamera in der 3D-Ansicht und verarbeitet Benutzer-Interaktionen via Maus und Scroll.
 */
public class CameraController
{
    private final Group cameraGroup = new Group();
    private final PerspectiveCamera camera = new PerspectiveCamera(true);

    private final Translate cameraTranslate = new Translate(0, 0, Constants.CAMERA_DEFAULT_DISTANCE);
    private final Rotate cameraRotateX = new Rotate(0, Rotate.X_AXIS);
    private final Rotate cameraRotateY = new Rotate(0, Rotate.Y_AXIS);


    public CameraController()
    {
        cameraGroup.getTransforms().addAll(cameraRotateX, cameraRotateY, cameraTranslate);
        cameraGroup.getChildren().add(camera);

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
                cameraRotateY.setAngle(anchor + value);
                break;
            default:
                break;
        }
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
        cameraTranslate.setZ(Constants.CAMERA_DEFAULT_Z);
        cameraRotateX.setAngle(Constants.CAMERA_DEFAULT_X_ANGLE);
        cameraRotateY.setAngle(0);
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

    public Rotate getCameraRotateY() {
        return cameraRotateY;
    }
}