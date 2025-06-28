package de.tharms.guiprog_ea_3.model;

/**
 * Repräsentiert einen dreidimensionalen Vektor mit den Komponenten x, y und z.
 */
public class Vector3D
{
    float x = 0;
    float y = 0;
    float z = 0;

    /**
     * Konstruktor zur Erstellung eines {@link Vector3D}-Objekts mit gegebenem x, y und z.
     *
     * @param x Der x-Wert des Vektors.
     * @param y Der y-Wert des Vektors.
     * @param z Der z-Wert des Vektors.
     *
     * @Vorbedingung Keine
     * @Nachbedingung Ein neuer {@link Vector3D} mit den übergebenen Koordinaten wurde erzeugt.
     */
    public Vector3D(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Berechnet einen Richtungsvektor vom Punkt {@code vertex1} zum Punkt {@code vertex2}.
     *
     * @param vertex1 Der Startpunkt des Vektors.
     * @param vertex2 Der Endpunkt des Vektors.
     * @return Der resultierende Richtungsvektor von {@code vertex1} nach {@code vertex2}.
     *
     * @Vorbedingung vertex1 und vertex2 sind nicht null
     * @Nachbedingung Gibt den Vektor als neues {@link Vector3D}-Objekt zurück.
     */
    public static Vector3D getVectorFromTo(Vertex vertex1, Vertex vertex2)
    {
        return new Vector3D(vertex2.x - vertex1.x, vertex2.y - vertex1.y, vertex2.z - vertex1.z);
    }

    /**
     * Berechnet die Länge des Vektors.
     *
     * @return Die Länge des Vektors als {@code float}.
     *
     * @Vorbedingung Die Vektorkomponenten sind gültig initialisiert.
     * @Nachbedingung Gibt die Wurzel aus der Summe der quadrierten Werte zurück.
     */
    public float getLength()
    {
        return (float) Math.sqrt(
                Math.pow(this.x, Constants.INDEX_TWO) +
                        Math.pow(this.y, Constants.INDEX_TWO) +
                        Math.pow(this.z, Constants.INDEX_TWO));
    }

    /**
     * Berechnet das Kreuzprodukt dieses Vektors mit einem anderen Vektor.
     *
     * @param other Der andere Vektor.
     * @return Das Kreuzprodukt als neues {@link Vector3D}-Objekt.
     *
     * @Vorbedingung other ist nicht null
     * @Nachbedingung Gibt das Kreuzprodukt beider Vektoren als neues {@link Vector3D} zurück.
     */
    public Vector3D getCrossProduct(Vector3D other)
    {
        return new Vector3D(
                this.y * other.getZ() - this.z * other.getY(),
                this.z * other.getX() - this.x * other.getZ(),
                this.x * other.getY() - this.y * other.getX());
    }

    /**
     * Berechnet das Skalarprodukt dieses Vektors mit einem anderen Vektor.
     *
     * @param other Der andere Vektor.
     * @return Das Skalarprodukt als {@code double}.
     *
     * @Vorbedingung other != null
     * @Nachbedingung Gibt die Summe der Produkte der entsprechenden Komponenten beider Vektoren zurück.
     */
    public float calculateDotProduct(Vector3D other)
    {
        return this.getX() * other.getX() + this.getY() * other.getY() + this.getZ() * other.getZ();
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
     * Formatiert das x, y und z des Vektors zu einem lesbaren String.
     *
     * @return Der Vektor als formatierter String.
     *
     * @Vorbedingung Die Vektorkomponenten sind gültig initialisiert.
     * @Nachbedingung Gibt einen formatierten String mit den Werten zurück.
     */
    @Override
    public String toString() {
        return Constants.OUTPUT_NORMAL + Constants.OUTPUT_SEPARATOR + x + Constants.OUTPUT_SEPARATOR + y + Constants.OUTPUT_SEPARATOR + z;
    }
}
