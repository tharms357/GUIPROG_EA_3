package de.tharms.guiprog_ea_3.utility;

import de.tharms.guiprog_ea_3.model.Constants;

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
    public static boolean calculateEulerCharacteristics(
            int numberOfVertices, int numberOfEdges, int numberOfFaces)
    {
        int result = numberOfVertices - numberOfEdges + numberOfFaces;
        return result == Constants.EULER_CHARACTERISTIC_CONVEX_POLYHEDRON;
    }
}
