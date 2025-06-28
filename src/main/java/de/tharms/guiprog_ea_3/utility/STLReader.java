package de.tharms.guiprog_ea_3.utility;

import de.tharms.guiprog_ea_3.model.*;
import de.tharms.guiprog_ea_3.view.Output;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Die Klasse {@code STLReader} bietet Funktionen zum Einlesen und Verarbeiten von STL-Dateien
 * im ASCII- oder Binärformat zur Erzeugung von {@link Polyhedron}-Objekten.
 */
public class STLReader
{
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
        if (filepath == null || !filepath.endsWith(Constants.FILENAME_VALID_FORMAT))
        {
            throw new IllegalArgumentException(Constants.INVALID_FILE_FORMAT);
        }

        try
        {
            File file = new File(filepath);
            byte[] data = new byte[(int) file.length()];

            try (FileInputStream fileInputStream = new FileInputStream(file))
            {
                int bytesRead = fileInputStream.read(data);
                if (bytesRead != data.length)
                {
                    throw new IOException(Constants.FILE_READING_ERROR + filepath);
                }
            }

            String header = new String(data, 0, Constants.STL_BINARY_HEADER_LENGTH, StandardCharsets.US_ASCII);

            if (header.trim().startsWith(Constants.STL_ASCII_KEYWORD_SOLID))
            {
                return readASCII(prepareASCIISTL(FileReader.lineReader(filepath)), filepath);
            }
            else
            {
                return createPolyhedronFromBinarySTL(data, filepath);
            }
        } catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Fordert den Benutzer auf, einen Dateinamen über die Konsole einzugeben.
     *
     * @return Der vollständige Dateipfad basierend auf der Benutzereingabe und dem voreingestellten Verzeichnis.
     *
     * @Vorbedingung Die Benutzereingabe liefert einen gültigen Dateinamen.
     * @Nachbedingung Gibt den zusammengesetzten Dateipfad als String zurück.
     */
    public static String getFilenameFromUserInput()
    {
        Scanner userInput = new Scanner(System.in);

        Output.filenameUserInput();

        String filename = userInput.nextLine();

        return Constants.DEFAULT_FILEPATH + filename;
    }

    /**
     * Liest eine ASCII-STL-Datei und wandelt sie in ein {@link Polyhedron}-Objekt um.
     *
     * @param facesByLine Die Listen der Zeileninhalte jedes Faces.
     * @param filename Der Name der Datei.
     * @return Ein {@link Polyhedron}-Objekt, das aus den ASCII-Daten erzeugt wurde.
     *
     * @Vorbedingung facesByLine ist und filename sind nicht null.
     * @Nachbedingung Gibt ein korrekt erzeugtes {@link Polyhedron}-Objekt zurück.
     */
    public static Polyhedron readASCII(List<String[]> facesByLine, String filename)
    {
        List<Face> faces = new ArrayList<>();

        if (Arrays.toString(facesByLine.getFirst()).contains(Constants.STL_ASCII_KEYWORD_SOLID))
        {
            facesByLine.removeFirst();
        }

        for (String[] face : facesByLine)
        {
            try
            {
                Face currentFace = new Face(
                        new Triangle(
                                createEdgesFromLines(face)),
                        createNormalFromLine(face[0]));

                faces.add(currentFace);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        Polyhedron polyhedron = new Polyhedron(faces, filename);

        return polyhedron;
    }

    /**
     * Erstellt drei {@link Edge}-Objekte aus einer Liste von ASCII-Zeilen.
     *
     * @param lines Die Zeilen, die ein Face beschreiben.
     * @return Eine Liste mit genau drei {@link Edge}-Objekten.
     *
     * @Vorbedingung lines != null && enthält gültige Vertex-Zeilen.
     * @Nachbedingung Gibt eine Liste mit drei gültigen {@link Edge}-Objekten zurück.
     */
    private static List<Edge> createEdgesFromLines(String[] lines)
    {
        List<Edge> edges = new ArrayList<>();
        List<Vertex> vertices = new ArrayList<>();
        String[] currentVertex;

        if (!lines[Constants.INDEX_ONE].startsWith(Constants.STL_ASCII_KEYWORD_OUTER_LOOP) &&
                !lines[Constants.INDEX_FIVE].startsWith(Constants.STL_ASCII_KEYWORD_ENDLOOP))
        {
            throw new IllegalArgumentException(Constants.INVALID_FACE_VERTEX);
        }

        for (String currentLine : lines)
        {
            if (currentLine.startsWith(Constants.STL_ASCII_KEYWORD_VERTEX))
            {
                currentVertex = currentLine.trim().replaceFirst(Constants.STL_ASCII_SPLIT_VERTICES_REGEX,
                        Constants.EMPTY_STRING).split(Constants.STL_ASCII_SPLIT_REGEX);

                vertices.add(new Vertex(
                        Float.parseFloat(currentVertex[Constants.INDEX_ZERO]),
                        Float.parseFloat(currentVertex[Constants.INDEX_ONE]),
                        Float.parseFloat(currentVertex[Constants.INDEX_TWO])));
            }
        }

        edges.add(
                new Edge(
                        vertices.get(Constants.INDEX_ONE),
                        vertices.get(Constants.INDEX_ZERO)));
        edges.add(
                new Edge(
                        vertices.get(Constants.INDEX_ZERO),
                        vertices.get(Constants.INDEX_TWO)));
        edges.add(
                new Edge(
                        vertices.get(Constants.INDEX_TWO),
                        vertices.get(Constants.INDEX_ONE)));

        return edges;
    }

    /**
     * Erstellt ein {@link Vector3D}-Objekt aus einer Zeile.
     *
     * @param line Die Zeile, die die Werte für die Normale enthält.
     * @return Ein {@link Vector3D}-Objekt mit den Werten der Zeile.
     *
     * @Vorbedingung line ist nicht null und enthält gültige Float-Werte.
     * @Nachbedingung Gibt ein korrekt initialisiertes {@link Vector3D}-Objekt zurück.
     */
    private static Vector3D createNormalFromLine(String line)
    {
        String[] values = line.trim().replaceFirst(Constants.STL_ASCII_SPLIT_NORMAL_REGEX,
                Constants.EMPTY_STRING).split(Constants.STL_ASCII_SPLIT_REGEX);

        try
        {
            Vector3D normal = new Vector3D(
                    Float.parseFloat(values[Constants.INDEX_ONE]),
                    Float.parseFloat(values[Constants.INDEX_ONE]),
                    Float.parseFloat(values[Constants.INDEX_TWO]));
            return normal;
        }
        catch (NumberFormatException e)
        {
            throw new IllegalArgumentException(Constants.INVALID_FACE_NORMAL);
        }
    }

    /**
     * Wandelt eine Liste von ASCII-Zeilen in eine Liste aus String-Arrays nach Faces um.
     *
     * @param fileDataByLines Die Zeilen der Datei als {@link ArrayList}.
     * @return Eine Liste von String-Arrays, die jeweils ein Face darstellen.
     *
     * @Vorbedingung fileDataByLines ist nicht null und enthält gültige STL-Zeilen.
     * @Nachbedingung Gibt eine Liste aus String-Arrays nach Faces zurück.
     */
    public static List<String[]> prepareASCIISTL(ArrayList<String> fileDataByLines)
    {
        List<String[]> facets = new ArrayList<>();
        List<String> currentFace = null;

        facets.add(new String[]{fileDataByLines.getFirst().trim()});

        for (String rawLine : fileDataByLines)
        {
            String line = rawLine.trim();

            if (line.startsWith(Constants.STL_ASCII_KEYWORD_FACET_NORMAL))
            {
                currentFace = new ArrayList<>();
                currentFace.add(line);
            }
            else if (line.startsWith(Constants.STL_ASCII_KEYWORD_VERTEX) |
                    line.startsWith(Constants.STL_ASCII_KEYWORD_OUTER_LOOP) |
                    line.startsWith(Constants.STL_ASCII_KEYWORD_ENDLOOP))
            {
                if (currentFace != null)
                {
                    currentFace.add(line);
                }
            }
            else if (line.startsWith(Constants.STL_ASCII_KEYWORD_ENDFACET))
            {
                if (currentFace != null)
                {
                    currentFace.add(line);
                    facets.add(currentFace.toArray(new String[currentFace.size()]));
                }
            }
        }
        return facets;
    }

    /**
     * Liest eine STL-Datei im Binärformat ein und wandelt diese in ein {@link Polyhedron}-Objekt um.
     *
     * @param data Die Byte-Daten des Binärformats.
     * @param filename Der Name der Datei.
     * @return Ein {@link Polyhedron}-Objekt, das aus den Binärdaten erzeugt wurde.
     *
     * @Vorbedingung data != null && data enthält gültige Binär-STL-Daten
     * @Nachbedingung Gibt ein vollständig aufgebautes {@link Polyhedron}-Objekt zurück.
     */
    public static Polyhedron createPolyhedronFromBinarySTL(byte[] data, String filename)
    {
        List<Face> faces = new ArrayList<>();
        ByteBuffer byteBuffer = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN);

        byteBuffer.position(Constants.STL_BINARY_HEADER_LENGTH);

        int numberOfTriangles = byteBuffer.getInt();

        for (int i = 0; i < numberOfTriangles; i++)
        {
            float[] normalValues = new float[Constants.STL_NORMAL_NUMBER_OF_DIMENSIONS];

            for (int j = 0; j < Constants.STL_NORMAL_NUMBER_OF_DIMENSIONS; j++)
            {
                normalValues[j] = byteBuffer.getFloat();
            }

            Vector3D normal = new Vector3D(
                    normalValues[Constants.INDEX_ZERO],
                    normalValues[Constants.INDEX_ONE],
                    normalValues[Constants.INDEX_TWO]);

            Vertex[] vertices = new Vertex[Constants.STL_NUMBER_OF_VERTICES];
            for (int k = 0; k < Constants.STL_NUMBER_OF_VERTICES; k++)
            {
                float[] vertexValues = new float[Constants.STL_VERTEX_NUMBER_OF_DIMENSIONS];

                for (int j = 0; j < Constants.STL_VERTEX_NUMBER_OF_DIMENSIONS; j++)
                {
                    vertexValues[j] = byteBuffer.getFloat();
                }
                vertices[k] = new Vertex(
                        vertexValues[Constants.INDEX_ZERO],
                        vertexValues[Constants.INDEX_ONE],
                        vertexValues[Constants.INDEX_TWO]);
            }

            short attributes = byteBuffer.getShort();

            faces.add(new Face
                    (new Triangle(
                            vertices[Constants.INDEX_ZERO],
                            vertices[Constants.INDEX_ONE],
                            vertices[Constants.INDEX_TWO]),
                            normal));
        }

        return new Polyhedron(faces, filename);
    }
}
