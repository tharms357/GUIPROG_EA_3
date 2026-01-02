package de.tharms.guiprog_ea_3.utility;

import de.tharms.guiprog_ea_3.controller.PolyhedronController;
import de.tharms.guiprog_ea_3.model.*;
import de.tharms.guiprog_ea_3.view.Output;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Diese Klasse bietet Funktionen zum Einlesen und Verarbeiten von STL-Dateien
 * im ASCII- oder Binärformat zur Erzeugung von Polyhedron-Objekten.
 */
public class STLReader
{
    /**
     * Liest eine ASCII-STL-Datei und wandelt sie in ein {@link Polyhedron}-Objekt um.
     *
     * @param filepath Der Name der Datei.
     * @return Ein {@link Polyhedron}-Objekt, das aus den ASCII-Daten erzeugt wurde.
     *
     * @Vorbedingung filepath ist nicht null.
     * @Nachbedingung Gibt ein korrekt erzeugtes {@link Polyhedron}-Objekt zurück.
     */
    public static Polyhedron createPolyhedronFromASCIISTL(String filepath)
    {
        List<Face> faces = new ArrayList<>();
        String polyhedronName = filepath;
        List<String[]> facesByLine = STLReader.prepareASCIISTL(FileReader.lineReader(filepath));

        if (Arrays.toString(facesByLine.getFirst()).contains(Constants.STL_ASCII_KEYWORD_SOLID))
        {
            polyhedronName = facesByLine.getFirst()[0].replaceFirst(
                    Constants.STL_ASCII_SPLIT_NAME_REGEX, Constants.EMPTY_STRING);
            facesByLine.removeFirst();
        }

        // Erstellen der Faces aus eingelesenen Zeilen
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

        return new Polyhedron(faces, polyhedronName);
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
                // Extrahieren der Werte aus der Zeile
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
                        vertices.get(Constants.INDEX_ZERO),
                        vertices.get(Constants.INDEX_ONE)));
        edges.add(
                new Edge(
                        vertices.get(Constants.INDEX_ONE),
                        vertices.get(Constants.INDEX_TWO)));
        edges.add(
                new Edge(
                        vertices.get(Constants.INDEX_TWO),
                        vertices.get(Constants.INDEX_ZERO)));

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
        // Extrahieren der Werte aus der Zeile
        String[] values = line.trim().replaceFirst(Constants.STL_ASCII_SPLIT_NORMAL_REGEX,
                Constants.EMPTY_STRING).split(Constants.STL_ASCII_SPLIT_REGEX);

        try
        {
            Vector3D normal = new Vector3D(
                    Float.parseFloat(values[Constants.INDEX_ZERO]),
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
        List<String[]> faces = new ArrayList<>();
        List<String> currentFace = null;

        faces.add(new String[]{fileDataByLines.getFirst().trim()});

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
                    faces.add(currentFace.toArray(new String[currentFace.size()]));
                }
            }
        }
        return faces;
    }

    /**
     * Liest eine STL-Datei im Binärformat ein und wandelt diese in ein {@link Polyhedron}-Objekt um.
     *
     * @param data Die Byte-Daten des Binärformats.
     * @param filename Der Name der Datei.
     * @return Ein {@link Polyhedron}-Objekt, das aus den Binärdaten erzeugt wurde.
     *
     * @Vorbedingung data ist ungleich null und data enthält gültige Binär-STL-Daten
     * @Nachbedingung Gibt ein vollständig aufgebautes {@link Polyhedron}-Objekt zurück.
     */
    public static Polyhedron createPolyhedronFromBinarySTL(byte[] data, String filename)
    {
        byte[] header = Arrays.copyOfRange(data, 0, Constants.STL_BINARY_HEADER_LENGTH);

        String headerName = new String(header).trim();
        String polyhedronName;

        // Setzen des Polyeder-Namens
        if (headerName.isEmpty())
        {
            polyhedronName = filename;
        }
        else
        {
            polyhedronName = headerName;
        }

        // Überspringen des Headers und Erstellen des ByteBuffers
        ByteBuffer byteBuffer = ByteBuffer
                .wrap(data)
                .order(ByteOrder.LITTLE_ENDIAN)
                .position(Constants.STL_BINARY_HEADER_LENGTH);

        int numberOfTriangles = byteBuffer.getInt();
        List<Face> faces = new ArrayList<>(numberOfTriangles);

        for (int i = 0; i < numberOfTriangles; i++)
        {
            faces.add(createFaceFromByteBuffer(byteBuffer));
        }

        return new Polyhedron(faces, polyhedronName);
    }

    /**
     * Liest aus dem aktuellen Buffer eine Dreiecks-Facette und erzeugt ein {@link Face}-Objekt.
     * Dabei werden zuerst der Normalenvektor, dann drei Vertices und zuletzt das Attribut-Short übersprungen.
     *
     * @param byteBuffer Der auf das Binär-STL-Datenarray positionierte {@link ByteBuffer}.
     * @return Ein neues {@link Face}-Objekt, bestehend aus einem {@link Triangle} und seiner Normalen.
     *
     * @Vorbedingung byteBuffer ist nicht null und auf die erste Facette positioniert.
     * @Nachbedingung Der Buffer-Position steht direkt hinter der eingelesenen Facette (nach dem Attribut-Short).
     */
    private static Face createFaceFromByteBuffer(ByteBuffer byteBuffer)
    {
        Vector3D normal = new Vector3D(
                byteBuffer.getFloat(),
                byteBuffer.getFloat(),
                byteBuffer.getFloat()
        );

        Vertex v0 = new Vertex(byteBuffer.getFloat(), byteBuffer.getFloat(), byteBuffer.getFloat());
        Vertex v1 = new Vertex(byteBuffer.getFloat(), byteBuffer.getFloat(), byteBuffer.getFloat());
        Vertex v2 = new Vertex(byteBuffer.getFloat(), byteBuffer.getFloat(), byteBuffer.getFloat());

        // Attribute überspringen
        byteBuffer.getShort();

        return new Face(new Triangle(v0, v1, v2), normal);
    }
}
