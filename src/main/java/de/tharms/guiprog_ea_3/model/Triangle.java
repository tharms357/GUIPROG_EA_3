package de.tharms.guiprog_ea_3.model;

import java.util.List;

public class Triangle extends Polygon
{
    public Triangle(List<Edge> edges)
    {
        super(edges);

        if (edges.size() != 3)
        {
            throw new IllegalArgumentException(Constants.TRIANGLE_ILLEGAL_AMOUNT_OF_EDGES);
        }
    }

    public Triangle(Vertex vertexA, Vertex vertexB, Vertex vertexC)
    {
        super(List.of(
                new Edge(vertexA, vertexB),
                new Edge(vertexB, vertexC),
                new Edge(vertexC, vertexA)));
    }
}

