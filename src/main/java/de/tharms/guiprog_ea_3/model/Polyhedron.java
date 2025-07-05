package de.tharms.guiprog_ea_3.model;

import de.tharms.guiprog_ea_3.controller.PolyhedronController;

import java.util.ArrayList;
import java.util.List;

/**
 * Repräsentiert ein Polyeder, das aus einer Liste aus {@link Face}-Objekten, einem Namen, und seinem Flächeninhalt
 * und Volumen besteht.
 */
public class Polyhedron
{
    List<Face> faces;
    float surfaceArea;
    float volume;
    String name;

    /**
     * Konstruktor für ein {@link Polyhedron}-Objekt mit den gegebenen Flächen und dem Namen.
     * Berechnet dabei automatisch Oberfläche und Volumen.
     *
     * @param faces Die Liste der {@link Face}-Objekte, aus denen das Polyeder besteht.
     * @param name Der Name des Polyeders.
     * @Vorbedingung faces und name dürfen nicht null sein.
     * @Nachbedingung Die Felder faces, surfaceArea und volume wurden gesetzt; name ist initialisiert.
     */
    public Polyhedron(List<Face> faces, String name)
    {
        this.faces = faces;
        this.surfaceArea = PolyhedronController.calculateSurfaceArea(this);
        this.volume = PolyhedronController.calculateVolume(this);
        this.name = name;
    }

    public List<Face> getFaces()
    {
        return faces;
    }

    /**
     * Ermittelt alle {@link Vertex}-Objekte aus den Kanten aller Flächen des Polyeders.
     * Fügt alle Vertices jeder Kante zur Rückgabeliste hinzu.
     *
     * @return Eine Liste aller {@link Vertex}-Objekte aus den Kanten aller Flächen.
     * @Vorbedingung Die interne Liste "faces" und deren Polygone mit Kanten dürfen nicht null sein.
     * @Nachbedingung Die zurückgegebene Liste enthält alle Vertices aller Kanten in der Reihenfolge des Durchlaufs.
     */
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

    public float getSurfaceArea()
    {
        return surfaceArea;
    }

    public String getName()
    {
        return name;
    }

    public float getVolume()
    {
        return volume;
    }
}
