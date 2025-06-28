package de.tharms.guiprog_ea_3.model;

import java.util.ArrayList;
import java.util.List;

public class Polyhedron
{
    List<Face> faces;
    String name;

    public Polyhedron(List<Face> faces, String name)
    {
        this.faces = faces;
        this.name = name;
    }

    public List<Face> getFaces()
    {
        return faces;
    }

    public List<Vertex> getVertices()
    {
        List<Vertex> vertices = new ArrayList<Vertex>();

        for (Face face : faces)
        {
            for (Edge edge : face.getPolygon().getEdges())
            {
                vertices.addAll(edge.getVertices());
            }
        }

        return vertices;
    }

    public void addFace(Face face)
    {
        faces.add(face);
    }
}
