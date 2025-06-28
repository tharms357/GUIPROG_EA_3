package de.tharms.guiprog_ea_3.controller;

import de.tharms.guiprog_ea_3.model.*;
import de.tharms.guiprog_ea_3.utility.Mathmatics;
import de.tharms.guiprog_ea_3.utility.Stopwatch;
import de.tharms.guiprog_ea_3.view.Output;

import java.util.*;

public class PolyhedronController
{
    /**
     * Sortiert die Flächen eines gegebenen Polyeders nach deren Fläche in aufsteigender Reihenfolge.
     *
     * @param polyhedron Das {@link Polyhedron}-Objekt, dessen Flächen sortiert werden sollen.
     * @return Eine neue {@link ArrayList} von {@link Face}-Objekten, sortiert nach Flächengröße (aufsteigend).
     *
     * @Vorbedingung Das übergebene Polyeder-Objekt und seine Flächenliste dürfen nicht null sein.
     * @Nachbedingung Die zurückgegebene Liste enthält dieselben Flächen wie im Polyeder, jedoch sortiert nach Fläche.
     */
    public static ArrayList<Face> sortFacesBySize(Polyhedron polyhedron)
    {
        ArrayList<Face> faces = new ArrayList<>(polyhedron.getFaces());

        faces.sort((face1, face2) -> {
            float area1 = face1.getPolygon().calculateArea();
            float area2 = face2.getPolygon().calculateArea();

            return Float.compare(area1, area2);
        });

        return faces;
    }

    public static float calculateAreaUsingThreads(Polyhedron polyhedron)
    {
        Stopwatch.getInstance().start();

        int numberOfThreads = Runtime.getRuntime().availableProcessors();
        float[] results = new float[numberOfThreads];

        List<Thread> threads = ThreadController.startAreaCalculation(polyhedron, numberOfThreads, results);

        ThreadController.waitForAllThreads(threads);

        float polyhedronArea = 0;

        for (float area : results)
        {
            polyhedronArea += area;
        }

        Output.timePassed(Stopwatch.getInstance().stop());

        return polyhedronArea;
    }

    public static float calculateSurfaceArea(Polyhedron polyhedron)
    {
        Stopwatch.getInstance().start();

        float surfaceArea = 0;

        if (polyhedron.getFaces() == null)
        {
            return 0;
        }

        for (Face face : polyhedron.getFaces())
        {
            if (face == null || face.getPolygon() == null)
            {
                continue;
            }

            surfaceArea += face.getPolygon().calculateArea();
        }

        Output.timePassed(Stopwatch.getInstance().stop());

        return surfaceArea;
    }

    public static float calculateVolume(Polyhedron polyhedron)
    {
        float totalVolume = 0;

        for (Face face : polyhedron.getFaces())
        {
            List<Vertex> vertices = face.getUniqueVertices();

            Vector3D base = vertices.getFirst().toVector();

            for (int i = 1; i < vertices.size() - Constants.INDEX_ONE; i++)
            {
                Vector3D vector1 = vertices.get(i).toVector();
                Vector3D vector2 = vertices.get(i + Constants.INDEX_ONE).toVector();

                Vector3D crossProduct = vector1.getCrossProduct(vector2);
                float signedVolume = base.calculateDotProduct(crossProduct) / Constants.TETRAHEDRON_VOLUME_FACTOR;;

                totalVolume += signedVolume;
            }
        }

        return Math.abs(totalVolume);
    }

    public static boolean isConvex(Polyhedron polyhedron)
    {
        HashSet<Vertex> uniqueVertices = new HashSet<>();
        HashSet<Edge> uniqueEdges = new HashSet<>();

        for (Face face : polyhedron.getFaces())
        {
            Polygon currentPolygon = face.getPolygon();

            List<Edge> edges = currentPolygon.getEdges();

            for (Edge edge : edges)
            {
                uniqueVertices.add(edge.getStart());
                uniqueVertices.add(edge.getEnd());
                uniqueEdges.add(edge);
            }
        }

        return Mathmatics.calculateEulerCharacteristics(
                uniqueVertices.size(), uniqueEdges.size(), polyhedron.getFaces().size());
    }
}
