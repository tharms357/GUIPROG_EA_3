package de.tharms.guiprog_ea_3.model;

import java.util.Objects;

/**
 * Repräsentiert einen Punkt (Vertex) im dreidimensionalen Raum.
 */
public class Vertex
{
    float x = 0;
    float y = 0;
    float z = 0;

    /**
     * Konstruktor zum Erzeugen eines Vertex mit gegebenen Koordinaten.
     *
     * @param x Der x-Wert des Vertex.
     * @param y Der y-Wert des Vertex.
     * @param z Der z-Wert des Vertex.
     *
     * @Vorbedingung Keine
     * @Nachbedingung Ein neues {@link Vertex}-Objekt mit den gegebenen Koordinaten ist erstellt.
     */
    public Vertex(float x, float y, float z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Wandelt diesen Vertex in einen {@link Vector3D} als Richtungsvektor aus dem Ursprung um.
     *
     * @return Ein {@link Vector3D}-Objekt mit denselben Koordinaten.
     *
     * @Vorbedingung Vertex ist gültig initialisiert.
     * @Nachbedingung Gibt einen Richtungsvektor aus dem Ursprung zum Vertex zurück.
     */
    public Vector3D toVector()
    {
        return new Vector3D(this.getX(), this.getY(), this.getZ());
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    /**
     * Vergleicht diesen Vertex mit einem Objekt auf Gleichheit.
     *
     * @param object Das zu vergleichende Objekt.
     * @return {@code true}, wenn das Objekt ein Vertex mit gleichen Koordinaten ist, sonst {@code false}.
     *
     * @Vorbedingung object ist nicht null und vom Typ {@code Vertex}
     * @Nachbedingung Gibt zurück, ob beide Objekte gleich sind.
     */
    @Override
    public boolean equals(Object object)
    {
        if (this == object)
        {
            return true;
        }

        Vertex other = (Vertex) object;

        return Double.compare(x, other.x) == 0 &&
                Double.compare(y, other.y) == 0 &&
                Double.compare(z, other.z) == 0;
    }

    /**
     * Gibt einen konsistenten Hashcode basierend auf den Koordinaten des Vertex zurück.
     *
     * @return Der Hashcode als {@code int}.
     *
     * @Vorbedingung Der Vertex ist gültig initialisiert.
     * @Nachbedingung Zwei gleiche Vertex-Objekte liefern den gleichen Hashcode.
     */
    @Override public int hashCode()
    {
        return Objects.hash(x, y, z);
    }
}