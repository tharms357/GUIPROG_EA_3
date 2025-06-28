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

    public List<Edge> getEdges() {
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
        return 0;
    }

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
