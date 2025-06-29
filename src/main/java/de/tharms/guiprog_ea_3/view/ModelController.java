package de.tharms.guiprog_ea_3.view;

import javafx.scene.Group;
import javafx.scene.SubScene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

public class ModelController {

    private final Group modelGroup = new Group();
    private final Rotate rotateX = new Rotate(0, Rotate.X_AXIS);
    private final Rotate rotateZ = new Rotate(0, Rotate.Z_AXIS);
    private final Translate translate = new Translate();

    private double anchorX, anchorY;
    private double anchorAngleX, anchorAngleZ;

    public ModelController() {
        modelGroup.getTransforms().addAll(rotateZ, rotateX, translate);
    }

    public Group getModelGroup() {
        return modelGroup;
    }

    public void setMesh(MeshView meshView) {
        modelGroup.getChildren().clear();
        modelGroup.getChildren().add(meshView);
    }

    public void resetModel() {
        rotateX.setAngle(0);
        rotateZ.setAngle(0);
        translate.setX(0);
        translate.setY(0);
        translate.setZ(0);
    }

    public void attachMouseControl(SubScene scene) {
        scene.setOnMousePressed(e -> onMousePressed(e));
        scene.setOnMouseDragged(e -> onMouseDragged(e));
    }

    private void onMousePressed(MouseEvent e) {
        anchorX = e.getSceneX();
        anchorY = e.getSceneY();
        anchorAngleX = rotateX.getAngle();
        anchorAngleZ = rotateZ.getAngle();
    }

    private void onMouseDragged(MouseEvent e) {
        double deltaX = e.getSceneX() - anchorX;
        double deltaY = e.getSceneY() - anchorY;

        if (e.getButton() == MouseButton.PRIMARY && !e.isShiftDown()) {
            rotateZ.setAngle(anchorAngleZ + deltaX * 0.1);
            rotateX.setAngle(anchorAngleX + deltaY * 0.1);
        }
        else if (e.getButton() == MouseButton.SECONDARY) {
            translate.setX(translate.getX() + deltaX);
            translate.setY(translate.getY() - deltaY);
            anchorX = e.getSceneX();
            anchorY = e.getSceneY();
        }
    }
}