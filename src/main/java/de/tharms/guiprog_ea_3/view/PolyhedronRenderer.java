package de.tharms.guiprog_ea_3.view;

import de.tharms.guiprog_ea_3.controller.PolyhedronController;
import de.tharms.guiprog_ea_3.model.Face;
import de.tharms.guiprog_ea_3.model.Polyhedron;
import de.tharms.guiprog_ea_3.model.Vertex;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PolyhedronRenderer {
    //TODO Renderer in Controller
    public static MeshView createMesh(Polyhedron polyhedron) {
        TriangleMesh mesh = new TriangleMesh();

        mesh.getTexCoords().addAll(0, 0);

        List<Vertex> vertices = polyhedron.getVertices();

        Map<Vertex, Integer> vertexIndexMap = new HashMap<>();

        // Punkte sammeln
        // TODO extra Methode unique Vertices?
        // TODO das Ding auslagern
        for (int i = 0; i < vertices.size(); i++)
        {
            Vertex vertex = vertices.get(i);
            vertexIndexMap.put(vertex, i);

            mesh.getPoints().addAll(vertex.getX(), vertex.getY(), vertex.getZ());
        }

        int index = 0;

        int[] faceIndexes = new int[polyhedron.getFaces().size() * 6];
        for (Face face : polyhedron.getFaces())
        {
            List<Vertex> faceVertices = face.getPolygon().getVertices();

            for (int i = 0; i < 3; i++)
            {
                faceIndexes[index++] = vertexIndexMap.get(faceVertices.get(i));
                faceIndexes[index++] = 0;
            }
        }

        mesh.getFaces().addAll(faceIndexes);

        MeshView meshView = new MeshView(mesh);

        /*
        meshView.setTranslateX(0);
        meshView.setTranslateY(0);
        meshView.setTranslateZ(0);

         */

        return meshView;
    }
}