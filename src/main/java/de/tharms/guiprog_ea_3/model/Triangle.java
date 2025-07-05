package de.tharms.guiprog_ea_3.model;

import java.util.List;

/**
 * Repräsentiert ein Dreieck als spezielle Form eines {@link Polygon}.
 *
 * @Vorbedingung Die Anzahl der Kanten muss genau drei betragen.
 * @Nachbedingung Ein Dreieck-Objekt wurde erstellt oder eine IllegalArgumentException geworfen.
 */
public class Triangle extends Polygon
{
    /**
     * Konstruktor, der ein Dreieck aus einer Liste von Kanten erstellt.
     * Überprüft, ob genau drei Kanten vorhanden sind.
     *
     * @param edges Liste der {@link Edge}-Objekte, die die Kanten des Dreiecks definieren.
     * @Vorbedingung edges darf nicht null sein und muss genau drei Elemente enthalten.
     * @Nachbedingung Ein Dreieck mit den angegebenen Kanten wurde erstellt oder eine IllegalArgumentException geworfen.
     */
    public Triangle(List<Edge> edges)
    {
        super(edges);

        if (edges.size() != Constants.NUMBERS_THREE)
        {
            throw new IllegalArgumentException(Constants.TRIANGLE_ILLEGAL_AMOUNT_OF_EDGES);
        }
    }

    /**
     * Konstruktor, der ein Dreieck aus drei {@link Vertex}-Objekten erstellt.
     * Erzeugt automatisch die entsprechenden {@link Edge}-Objekte.
     *
     * @param vertexA Der erste Punkt des Dreiecks.
     * @param vertexB Der zweite Punkt des Dreiecks.
     * @param vertexC Der dritte Punkt des Dreiecks.
     * @Vorbedingung vertexA, vertexB und vertexC dürfen nicht null sein.
     * @Nachbedingung Ein Dreieck mit Kanten zwischen den drei Scheitelpunkten wurde erstellt.
     */
    public Triangle(Vertex vertexA, Vertex vertexB, Vertex vertexC)
    {
        super(List.of(
                new Edge(vertexA, vertexB),
                new Edge(vertexB, vertexC),
                new Edge(vertexC, vertexA)));
    }
}

