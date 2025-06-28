package de.tharms.guiprog_ea_3.controller;

import de.tharms.guiprog_ea_3.model.Polyhedron;

import java.util.ArrayList;
import java.util.List;

public class ThreadController
{
    public static List<Thread> startAreaCalculation
            (Polyhedron polyhedron, int numberOfThreads, float[] threadResults)
    {
        List<Thread> threads = new ArrayList<Thread>();
        int areasToCalculate = calculateAreasForEachThread(polyhedron.getFaces().size(), numberOfThreads);

        for (int i = 0; i < numberOfThreads; i++)
        {
            final int threadIndex = i;

            int start = i * areasToCalculate;
            int end = calculateThreadEndIndex(start, areasToCalculate, polyhedron);

            Runnable task = () -> {
                float areaCumulated = 0;
                for (int j = start; j < end; j++) {
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

    private static int calculateThreadEndIndex(int start, int areasToCalculate, Polyhedron polyhedron)
    {
        return Math.min(start + areasToCalculate, polyhedron.getFaces().size());
    }

    public static void waitForAllThreads(List<Thread> threads)
    {
        for (int i = 0; i < threads.size(); i++)
        {
            try
            {
                threads.get(i).join();
            }
            catch (InterruptedException e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    private static int calculateAreasForEachThread(int size, int numberOfThreads)
    {
        return (int) Math.ceil((float) size / numberOfThreads);
    }
}
