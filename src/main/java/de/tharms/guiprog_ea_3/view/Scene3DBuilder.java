package de.tharms.guiprog_ea_3.view;

import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

public class Scene3DBuilder {
    public static SubScene createScene(Group sceneRoot, PerspectiveCamera camera, Rotate rotateX, Rotate rotateY, Translate zoom) {
        Group wrapper = new Group(sceneRoot);
        wrapper.getTransforms().add(zoom);

        SubScene subScene = new SubScene(wrapper, 1000, 700, true, SceneAntialiasing.BALANCED);
        subScene.setFill(Color.LIGHTGRAY);
        subScene.setCamera(camera);

        return subScene;
    }
}
