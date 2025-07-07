package de.tharms.guiprog_ea_3.utility;

import de.tharms.guiprog_ea_3.model.Constants;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Diese Klasse stellt mathematische Hilfsmethoden zur Verfügung.
 */
public class Mathmatics
{
    /**
     * Prüft, ob die Euler-Charakteristik eines konvexen Polyeders erfüllt ist.
     *
     * @param numberOfVertices Anzahl der Ecken (Vertices) des Polyeders.
     * @param numberOfEdges Anzahl der Kanten (Edges) des Polyeders.
     * @param numberOfFaces Anzahl der Flächen (Faces) des Polyeders.
     * @return {@code true}, wenn die Euler-Charakteristik erfüllt ist, sonst {@code false}.
     *
     * @Vorbedingung Edges, Vertices und Faces dürfen keine identischen Objekte enthalten.
     * @Nachbedingung Gibt {@code true} zurück, wenn {@code V - E + F == 2}, andernfalls {@code false}.
     */
    //TODO euler fixen
    public static int calculateEulerCharacteristics(
            int numberOfVertices, int numberOfEdges, int numberOfFaces)
    {
        return numberOfVertices - numberOfEdges + numberOfFaces;
    }

    /**
     * Rundet einen float-Wert auf die angegebene Anzahl Nachkommastellen (HALF_UP).
     *
     * @param value  der zu rundende Wert
     * @param places Anzahl der Nachkommastellen (>= 0)
     * @return der gerundete Wert als float
     * @throws NumberFormatException wenn places kleiner als 0 ist
     */
    public static Float roundValue(Float value, int places)
    {
        if (value == null)
        {
            return null;
        }

        if (places < 0)
        {
            throw new NumberFormatException(Constants.EXCEPTION_ROUND_PLACES_LOWER_THAN_ZERO);
        }

        BigDecimal bigDecimal = new BigDecimal(value);
        bigDecimal = bigDecimal.setScale(places, RoundingMode.HALF_UP);

        return bigDecimal.floatValue();
    }
}
