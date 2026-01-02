package de.tharms.guiprog_ea_3.model;

/**
 * Repräsentiert eine Fläche, die durch ein Polygon und einen Normalenvektor definiert ist.
 */
public class Face
{
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
    public Face(Polygon polygon, Vector3D normal)
    {
        this.polygon = polygon;
        this.normal = normal;
    }

    public Polygon getPolygon() {
        return polygon;
    }

    public Vector3D getNormal() {
        return normal;
    }
}