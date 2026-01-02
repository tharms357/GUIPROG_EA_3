package de.tharms.guiprog_ea_3.controller;

import de.tharms.guiprog_ea_3.model.Polyhedron;

import java.util.ArrayList;
import java.util.List;

/**
 * Verwaltet Threads zur parallelen Berechnung der Flächeninhalte von Polyhedron-Flächen.
 */
public class ThreadController
{
    /**
     * Startet mehrere Threads zur Berechnung der Flächeninhalte aller Flächen eines Polyeders.
     *
     * @param polyhedron Das {@link Polyhedron}, dessen Flächen berechnet werden sollen.
     * @param numberOfThreads Die Anzahl der Threads, die gestartet werden sollen.
     * @param threadResults Ein Array, in dem die Ergebnisse für jeden Thread gespeichert werden.
     * @return Eine Liste der gestarteten {@link Thread}-Objekte.
     * @Vorbedingung polyhedron darf nicht null sein und numberOfThreads > 0.
     * @Nachbedingung Jeder Thread in der zurückgegebenen Liste führt die Flächenberechnung durch.
     */
    public static List<Thread> startAreaCalculation
            (Polyhedron polyhedron, int numberOfThreads, float[] threadResults)
    {
        List<Thread> threads = new ArrayList<Thread>();
        int areasToCalculate = calculateThreadWorkload(polyhedron.getFaces().size(), numberOfThreads);

        for (int i = 0; i < numberOfThreads; i++)
        {
            int threadIndex = i;
            int start = i * areasToCalculate;
            int end = calculateThreadEndIndex(start, areasToCalculate, polyhedron);

            // Arbeitsbereich für jeden Thread festlegen
            Runnable task = () -> {
                float areaCumulated = 0;
                for (int j = start; j < end; j++)
                {
                    areaCumulated += polyhedron.getFaces().get(j).getPolygon().calculateArea();
                }
                threadResults[threadIndex] = areaCumulated;
            };

            Thread thread = new Thread(task);
            threads.add(thread);
            thread.start();
        }
        return threads;
    }

    /**
     * Berechnet das End-Index-Limit für die Flächenberechnung eines einzelnen Threads.
     *
     * @param start Der Startindex der Flächen, die der Thread berechnet.
     * @param areasToCalculate Die Anzahl der Flächen, die pro Thread berechnet werden sollen.
     * @param polyhedron Das {@link Polyhedron}, dessen Flächen berechnet werden sollen.
     * @return Der Endindex (exklusiv) für die Flächenberechnung dieses Threads.
     * @Vorbedingung start >= 0 und polyhedron.getFaces() ist nicht null.
     * @Nachbedingung Der zurückgegebene Index überschreitet nicht die Gesamtzahl der Flächen.
     */
    private static int calculateThreadEndIndex(int start, int areasToCalculate, Polyhedron polyhedron)
    {
        return Math.min(start + areasToCalculate, polyhedron.getFaces().size());
    }

    /**
     * Wartet, bis alle übergebene Threads beendet sind.
     *
     * @param threads Eine Liste von {@link Thread}-Objekten, auf die gewartet werden soll.
     * @Vorbedingung threads darf nicht null sein.
     * @Nachbedingung Alle Threads in der Liste sind beendet.
     * @throws RuntimeException Wenn ein Thread unterbrochen wurde.
     */
    public static void waitForAllThreads(List<Thread> threads)
    {
        for (Thread thread : threads)
        {
            try
            {
                thread.join();
            }
            catch (InterruptedException e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Berechnet, wie viele Flächen auf jeden Thread verteilt werden.
     *
     * @param size Die Gesamtzahl der Flächen.
     * @param numberOfThreads Die Anzahl der Threads.
     * @return Die Anzahl der Flächen pro Thread (aufgerundet).
     * @Vorbedingung size >= 0 und numberOfThreads > 0.
     * @Nachbedingung Das Ergebnis ist mindestens 1, sofern size > 0.
     */
    private static int calculateThreadWorkload(int size, int numberOfThreads)
    {
        return (int) Math.ceil((float) size / numberOfThreads);
    }
}
