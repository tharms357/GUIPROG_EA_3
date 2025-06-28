package de.tharms.guiprog_ea_3.model;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Face {
    Polygon polygon;
    Vector3D normal;

    /**
     * Konstruktor zur Erzeugung eines {@link Face} mit einem gegebenen Polygon und Normalenvektor.
     *
     * @param polygon Das Polygon, das die Fläche definiert.
     * @param normal  Der Normalenvektor der Fläche.
     * @Vorbedingung polygon und normal sind nicht null
     * @Nachbedingung Erstellt eine neue {@link Face}-Instanz mit den übergebenen Parametern.
     */
    public Face(Polygon polygon, Vector3D normal) {
        this.polygon = polygon;
        this.normal = normal;
    }

    /**
     * Ermittelt alle einzigartigen {@link Vertex}-Objekte, die in der Fläche verwendet werden.
     * Es doppeln sich keine Objekte mit gleichen Werten.
     *
     * @return Eine Liste einzigartiger {@link Vertex}-Objekte.
     * @Vorbedingung Die Fläche enthält ein gültiges Polygon mit Edges und Vertices.
     * @Nachbedingung Gibt eine Liste mit einzigartigen {@link Vertex}-Objekten der Fläche zurück.
     */
    public List<Vertex> getUniqueVertices() {
        Set<Vertex> uniqueVertices = new LinkedHashSet<>();

        for (Edge edge : this.getPolygon().getEdges()) {
            uniqueVertices.addAll(edge.getVertices());
        }

        return new ArrayList<>(uniqueVertices);
    }

    /**
     * Ermittelt alle einzigartiger {@link Edge}-Objekte der Fläche.
     * Es doppeln sich keine Objekte mit gleichen Werten.
     *
     * @return Eine Liste einzigartiger {@link Edge}-Objekte in Einfügereihenfolge.
     * @Vorbedingung Die Fläche enthält ein gültiges Polygon mit Kanten.
     * @Nachbedingung Gibt eine Liste mit allen einzigartigen {@link Edge}-Objekten zurück.
     */
    public List<Edge> getUniqueEdges() {
        Set<Edge> uniqueEdges = new LinkedHashSet<>();

        for (Edge edge : this.getPolygon().getEdges()) {
            uniqueEdges.add(edge);
        }

        return new ArrayList<>(uniqueEdges);
    }

    public Polygon getPolygon() {
        return polygon;
    }

    public Vector3D getNormal() {
        return normal;
    }
}