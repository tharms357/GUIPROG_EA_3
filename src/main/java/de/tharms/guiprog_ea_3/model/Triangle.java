package de.tharms.guiprog_ea_3.model;

import java.util.List;

public class Triangle extends Polygon
{
    public Triangle(Edge edgeA, Edge edgeB, Edge edgeC)
    {
        super(List.of(edgeA, edgeB, edgeC));
    }

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
                new Edge(vertexC, vertexB),
                new Edge(vertexA, vertexC)));
    }

    @Override
    public float calculateArea()
    {
        List<Edge> edges = this.getEdges();

        Vertex a = edges.get(Constants.INDEX_ZERO).getEnd();
        Vertex b = edges.get(Constants.INDEX_TWO).getStart();
        Vertex c = edges.get(Constants.INDEX_TWO).getEnd();

        Vector3D vectorAB = Vector3D.getVectorFromTo(a, b);
        Vector3D vectorAC = Vector3D.getVectorFromTo(a, c);

        Vector3D crossProduct = vectorAB.getCrossProduct(vectorAC);

        return (float) (Constants.NUMBERS_ZERO_DOT_FIVE * crossProduct.getLength());
    }
}

