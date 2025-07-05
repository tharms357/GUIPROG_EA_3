package de.tharms.guiprog_ea_3.model;

import java.util.ArrayList;
import java.util.List;

public class Edge
{
    Vertex start;
    Vertex end;

    /**
     * Konstruktor zum Erzeugen einer {@link Edge} mit gegebenem Start- und Endpunkt.
     *
     * @param start Der Startpunkt der Kante.
     * @param end Der Endpunkt der Kante.
     *
     * @Vorbedingung start und end sind nicht null.
     * @Nachbedingung Eine neue {@link Edge} ist erstellt mit definierten Start- und Endpunkten.
     */
    public Edge(Vertex start, Vertex end)
        {
            this.start = start;
            this.end = end;
        }

    /**
     * Gibt die beiden Endpunkte der Kante als Liste zurück.
     *
     * @return Eine {@link List} mit zwei {@link Vertex}-Objekten: Start- und Endpunkt.
     *
     * @Vorbedingung Die Kante besitzt gültige Start- und Endpunkte.
     * @Nachbedingung Gibt eine Liste mit genau zwei Elementen zurück, dem Start- und dem Endpunkt.
     */
    public List<Vertex> getVertices()
    {
        List<Vertex> vertices = new ArrayList<>();
        vertices.add(this.start);
        vertices.add(this.end);

        return vertices;
    }

    public Vertex getStart() {
    return start;
    }

    public Vertex getEnd() {
    return end;
    }

    /**
     * Vergleicht diese Kante mit einem anderen Objekt auf Gleichheit.
     *
     * @param obj Das zu vergleichende Objekt.
     * @return {@code true}, wenn beide Kanten dieselben start und end (unabhängig von der Reihenfolge),
     * sonst {@code false}.
     *
     * @Vorbedingung object ist entweder null oder vom Typ {@link Edge}.
     * @Nachbedingung Gibt {@code true} zurück, wenn beide Kanten dieselben Vertex-Endpunkte besitzen.
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Edge other = (Edge) obj;

        return (start.equals(other.start) && end.equals(other.end)) ||
                (start.equals(other.end) && end.equals(other.start));
    }


    /**
     * Gibt einen Hashcode für die Kante zurück.
     *
     * @return Der Hashcode basierend auf Start- und Endpunkt.
     *
     * @Vorbedingung Start- und Endpunkt sind gültig initialisiert.
     * @Nachbedingung Gibt einen gültigen Hashcode zurück, der für gleichwertige Kanten identisch ist.
     */
    @Override
    public int hashCode()
    {
        return start.hashCode() + end.hashCode();
    }
}
