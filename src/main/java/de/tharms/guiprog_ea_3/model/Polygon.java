package de.tharms.guiprog_ea_3.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Repräsentiert ein Polygon, das aus einer Liste aus {@link Edge}-Objekten besteht.
 */
public class Polygon
{
    List<Edge> edges;

    /**
     * Konstruktor zum Erzeugen eines {@link Polygon} mit einer gegebenen Liste von Kanten.
     *
     * @param edges Die Kanten, aus denen das Polygon besteht.
     *
     * @Vorbedingung edges ist nicht null
     * @Nachbedingung Erstellt ein neues {@link Polygon} mit den angegebenen Kanten.
     */
    public Polygon(List<Edge> edges)
    {
        this.edges = edges;
    }

    public List<Edge> getEdges()
    {
        return edges;
    }

    /**
     * Berechnet die Fläche des Polygons.
     *
     * @return Die Fläche als {@code float}.
     *
     * @Vorbedingung Das Polygon ist gültig definiert. Die Fläche ist berechenbar.
     * @Nachbedingung Gibt die berechnete Fläche des Polygons zurück.
     */
    public float calculateArea()
    {
        Vector3D areaVector = new Vector3D(0, 0, 0);
        int numberOfVertices = this.getVertices().size();

        for (int i = 0; i < numberOfVertices; i++)
        {
            Vertex currentVertex = this.getVertices().get(i);
            Vertex nextVertex = this.getVertices().get(
                    (i + Constants.NUMBERS_ONE) % numberOfVertices);

            areaVector.setX(
                    areaVector.getX() + (currentVertex.getY() - nextVertex.getY()) *
                    (currentVertex.getZ() + nextVertex.getZ()));

            areaVector.setY(
                    areaVector.getY() + (currentVertex.getZ() - nextVertex.getZ()) *
                    (currentVertex.getX() + nextVertex.getX()));

            areaVector.setZ(
                    areaVector.getZ() + (currentVertex.getX() - nextVertex.getX()) *
                    (currentVertex.getY() + nextVertex.getY()));
        }

        return (float) (Constants.NUMBERS_ZERO_DOT_FIVE * areaVector.getLength());
    }

    /**
     * Liefert alle Start-Vertices der Kanten des Polygons.
     * Die zurückgegebene Liste enthält für jede Kante genau einen Start-Vertex.
     *
     * @return Eine Liste der {@link Vertex}-Objekte, die den Startpunkt jeder Kante repräsentieren.
     * @Vorbedingung Die interne Liste edges darf nicht null sein.
     * @Nachbedingung Die zurückgegebene Liste enthält alle Start-Vertices in derselben Reihenfolge wie die Kanten.
     */
    public List<Vertex> getVertices()
    {
        List<Vertex> vertices = new ArrayList<Vertex>();

        for (Edge edge : edges)
        {
            vertices.add(edge.getStart());
        }

        return vertices;
    }
}
