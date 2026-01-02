package de.tharms.guiprog_ea_3.controller;

import de.tharms.guiprog_ea_3.model.*;
import de.tharms.guiprog_ea_3.utility.Mathmatics;
import de.tharms.guiprog_ea_3.utility.STLReader;
import de.tharms.guiprog_ea_3.utility.Stopwatch;
import de.tharms.guiprog_ea_3.view.Output;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Stellt Methoden zur Analyse und Verarbeitung eines {@link Polyhedron}-Objekts bereit.
 */
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

    /**
     * Erstellt ein {@link Polyhedron}-Objekt aus einer STL-Datei (ASCII- oder Binärformat).
     *
     * @param filepath Der Pfad zur STL-Datei.
     * @return Ein {@link Polyhedron}-Objekt, das aus der Datei erzeugt wurde, oder {@code null} bei Fehlern.
     *
     * @Vorbedingung filepath ist ungleich null und endet mit dem gültigen Dateiformat
     * @Nachbedingung Gibt ein gültiges {@link Polyhedron}-Objekt zurück oder {@code null}, falls ein Fehler auftrat.
     */
    public static Polyhedron createPolyhedronFromSTL(String filepath)
    {
        Output.printFileReadingStart(filepath);

        Polyhedron polyhedron;

        if (filepath == null || !filepath.endsWith(Constants.FILENAME_VALID_FORMAT))
        {
            throw new IllegalArgumentException(Constants.INVALID_FILE_FORMAT);
        }

        // Erstellen des Polyeders
        try
        {
            File file = new File(filepath);
            byte[] data = new byte[(int) file.length()];

            try (FileInputStream fileInputStream = new FileInputStream(file))
            {
                int bytesRead = fileInputStream.read(data);
                if (bytesRead != data.length)
                {
                    throw new IOException(Constants.FILE_READING_ERROR + Constants.SEPERATOR + filepath);
                }
            }

            String header = new String(data, 0, Constants.STL_BINARY_HEADER_LENGTH, StandardCharsets.US_ASCII);

            boolean isASCII = header.trim().startsWith(Constants.STL_ASCII_KEYWORD_SOLID);
            Output.printFileFormat(isASCII);

            if (isASCII)
            {
                polyhedron = STLReader.createPolyhedronFromASCIISTL(filepath);
            }
            else
            {
                polyhedron = STLReader.createPolyhedronFromBinarySTL(data, filepath);
            }
        }
        catch (IOException e)
        {
            System.err.println(e.getMessage());
            return null;
        }

        Output.printEulerCharacteristicsInformation(polyhedron, PolyhedronController.isClosed(polyhedron));
        return polyhedron;
    }

    /**
     * Berechnet die Oberfläche des Polyeders parallel mithilfe mehrerer Threads.
     *
     * @param polyhedron Das {@link Polyhedron}-Objekt, dessen Oberfläche berechnet werden soll.
     * @return Die berechnete Gesamtoberfläche des Polyeders.
     * @Vorbedingung polyhedron darf nicht null sein.
     * @Nachbedingung Die Gesamtoberfläche wurde berechnet und zurückgegeben.
     */
    public static float calculateSurfaceAreaUsingThreads(Polyhedron polyhedron)
    {
        Stopwatch.getInstance().start();

        int numberOfThreads = Runtime.getRuntime().availableProcessors();
        float[] results = new float[numberOfThreads];
        List<Thread> threads = ThreadController.startAreaCalculation(polyhedron, numberOfThreads, results);

        ThreadController.waitForAllThreads(threads);
        float surfaceArea = 0;

        for (float area : results)
        {
            surfaceArea += area;
        }

        Output.timePassed(Stopwatch.getInstance().stop(), Constants.AREA_CALCULATION_PARALLEL);
        return surfaceArea;
    }

    /**
     * Berechnet die Oberfläche des Polyeders seriell.
     *
     * @param polyhedron Das {@link Polyhedron}-Objekt, dessen Oberfläche berechnet werden soll.
     * @return Die berechnete Gesamtoberfläche des Polyeders.
     * @Vorbedingung polyhedron darf nicht null sein.
     * @Nachbedingung Die Gesamtoberfläche wurde berechnet und zurückgegeben.
     */
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

        Output.timePassed(Stopwatch.getInstance().stop(), Constants.AREA_CALCULATION_SERIAL);

        return surfaceArea;
    }

    /**
     * Berechnet das Volumen des Polyhedrons basierend auf Tetraederung der Flächen.
     *
     * @param polyhedron Das {@link Polyhedron}-Objekt, dessen Volumen berechnet werden soll.
     * @return Der absolute Gesamtvolumenwert des Polyhedrons.
     * @Vorbedingung polyhedron darf nicht null sein.
     * @Nachbedingung Das Gesamtvolumen wurde berechnet und zurückgegeben.
     */
    public static float calculateVolume(Polyhedron polyhedron)
    {
        Stopwatch.getInstance().start();

        float totalVolume = 0;

        for (Face face : polyhedron.getFaces())
        {
            List<Vertex> vertices = new ArrayList<>(face.getPolygon().getUniqueVertices());

            Vector3D base = vertices.getFirst().toVector();

            for (int i = Constants.NUMBERS_ONE; i < vertices.size() - Constants.INDEX_ONE; i++)
            {
                Vector3D vector1 = vertices.get(i).toVector();
                Vector3D vector2 = vertices.get(i + Constants.NUMBERS_ONE).toVector();

                Vector3D crossProduct = vector1.getCrossProduct(vector2);
                float signedVolume = base.calculateDotProduct(crossProduct) / Constants.TETRAHEDRON_VOLUME_FACTOR;;

                totalVolume += signedVolume;
            }
        }
        Output.timePassed(Stopwatch.getInstance().stop(), Constants.VOLUME_CALCULATION);

        return Math.abs(totalVolume);
    }

    /**
     * Prüft, ob das Polyeder geschlossen ist, basierend auf Euler-Charakteristik.
     *
     * @param polyhedron Das {@link Polyhedron}-Objekt, dessen Konvexität geprüft werden soll.
     * @return true, wenn das Polyeder konvex ist, sonst false.
     * @Vorbedingung polyhedron darf nicht null sein.
     * @Nachbedingung Ein Wahrheitswert zur Konvexität wurde zurückgegeben.
     */
    public static int isClosed(Polyhedron polyhedron)
    {
        LinkedHashSet<Vertex> uniqueVertices = new LinkedHashSet<>();
        LinkedHashSet<Edge> uniqueEdges = new LinkedHashSet<>();

        // Einzigartige Vertices und Edges holen
        for (Face face : polyhedron.getFaces())
        {
            Polygon poly = face.getPolygon();
            uniqueVertices.addAll(poly.getUniqueVertices());
            uniqueEdges.addAll(poly.getUniqueEdges());
        }

        return Mathmatics.calculateEulerCharacteristics(
                uniqueVertices.size(), uniqueEdges.size(), polyhedron.getFaces().size());
    }

    /**
     * Erzeugt eine {@link MeshView}-Darstellung des gegebenen Polyeders.
     *
     * @param polyhedron Das {@link Polyhedron}-Objekt, aus dem das Mesh erstellt werden soll.
     * @return Eine neue {@link MeshView} mit dem 3D-Mesh.
     * @Vorbedingung polyhedron und seine Vertex- und Face-Listen dürfen nicht null sein.
     * @Nachbedingung Das MeshView-Objekt wurde erstellt und zurückgegeben.
     */
    public static MeshView createMesh(Polyhedron polyhedron)
    {
        TriangleMesh mesh = new TriangleMesh();
        mesh.getTexCoords().addAll(0, 0);
        mesh.getFaceSmoothingGroups().addAll(new int[polyhedron.getFaces().size()]);

        Map<Vertex, Integer> vertexIndexMap = convertVerticesToPoints(mesh, polyhedron.getVertices());
        int[] faceIndexes = convertFacesToIndices(polyhedron, vertexIndexMap);
        mesh.getFaces().addAll(faceIndexes);

        return new MeshView(mesh);
    }

    /**
     * Wandelt die Vertex-Liste in Punkte für das {@link TriangleMesh} um und liefert ein Mapping von {@link Vertex} zu Index.
     *
     * @param mesh Die {@link TriangleMesh}, die mit Punkten gefüllt werden soll.
     * @param vertices Die Liste der {@link Vertex}-Objekte.
     * @return Eine Map von {@link Vertex} auf deren Index in der Mesh-Punktliste.
     * @Vorbedingung mesh und vertices dürfen nicht null sein.
     * @Nachbedingung Das TriangleMesh enthält alle Vertex-Koordinaten.
     */
    private static Map<Vertex, Integer> convertVerticesToPoints
            (TriangleMesh mesh, List<Vertex> vertices)
    {
        Map<Vertex, Integer> vertexIndexMap = new HashMap<>();

        for (int i = 0; i < vertices.size(); i++)
        {
            Vertex vertex = vertices.get(i);
            vertexIndexMap.put(vertex, i);
            mesh.getPoints().addAll(vertex.getX(), vertex.getY(), vertex.getZ());
        }

        return vertexIndexMap;
    }

    /**
     * Wandelt die Face-Liste des Polyeders in ein Index-Array für das {@link TriangleMesh} um.
     *
     * @param polyhedron Das {@link Polyhedron}, dessen Faces konvertiert werden sollen.
     * @param vertexIndexMap Eine Map von {@link Vertex} zu deren Index.
     * @return Ein int-Array mit den Face-Indizes für das TriangleMesh.
     * @Vorbedingung polyhedron, vertexIndexMap und die Faces dürfen nicht null sein.
     * @Nachbedingung Das zurückgegebene Array enthält alle Face-Indizes.
     */
    private static int[] convertFacesToIndices
            (Polyhedron polyhedron, Map<Vertex, Integer> vertexIndexMap)
    {
        int[] faceIndexes = new int[polyhedron.getFaces().size() * Constants.NUMBERS_SIX];
        int index = 0;

        for (Face face : polyhedron.getFaces())
        {
            List<Vertex> faceVertices = face.getPolygon().getVertices();

            for (int i = 0; i < Constants.NUMBERS_THREE; i++)
            {
                faceIndexes[index++] = vertexIndexMap.get(faceVertices.get(i));
                faceIndexes[index++] = 0;
            }
        }
        return faceIndexes;
    }
}
