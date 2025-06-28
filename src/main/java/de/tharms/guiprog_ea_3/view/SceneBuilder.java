package de.tharms.guiprog_ea_3.view;

import javafx.scene.*;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

import javax.swing.text.View;

public class SceneBuilder
{


    public Group buildSceneRoot(MeshView mesh)
    {
        Group root = new Group();

        root.getChildren().add(mesh);
        return root;
    }

}