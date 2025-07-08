package de.tharms.guiprog_ea_3.view;

import de.tharms.guiprog_ea_3.controller.PolyhedronController;
import de.tharms.guiprog_ea_3.model.Constants;
import de.tharms.guiprog_ea_3.model.Face;
import de.tharms.guiprog_ea_3.model.Polyhedron;

import java.net.SocketAddress;
import java.util.ArrayList;

/**
 * Stellt Konsolenausgabe-Methoden für Status- und Ergebnisanzeigen des STL-Viewers bereit.
 */
public class Output
{
    /**
     * Diese Methode begrüßt den Nutzer mit einem kurzen Willkommenstext und einem kleinen Kunstbild eines Flugzeuges.
     * @Vorbedingung keine
     * @Nachbedingung Die Begrüßung des Nutzers durch Ausgaben über die Konsole ist erfolgt.
     */
    public static void greetUser()
    {
        System.out.println(Constants.OUTPUT_GREET_USER);
    }

    /**
     * Gibt eine Startmeldung beim Einlesen einer Datei aus.
     *
     * @param filepath Der Pfad der einzulesenden Datei.
     * @Vorbedingung filepath darf nicht null oder leer sein und muss auf eine existierende Datei verweisen.
     * @Nachbedingung Die Konsole enthält eine Meldung zum Start des Lesevorgangs und den Dateipfad.
     */
    public static void printFileReadingStart(String filepath)
    {
        System.out.println(Constants.OUTPUT_READING_FILE_START_INFO);
        System.out.println(ColorCodes.BLUE + filepath + ColorCodes.RESET);
    }

    /**
     * Gibt eine Fehlermeldung aus, wenn eine Datei nicht gefunden wurde.
     *
     * @Vorbedingung Keine.
     * @Nachbedingung Die Konsole enthält eine farbige Fehlermeldung für Datei-NotFound.
     */
    public static void fileNotFound()
    {
        System.out.println(ColorCodes.RED + Constants.FILE_NOT_FOUND + ColorCodes.RESET);
    }

    /**
     * Gibt eine Teilliste der kleinsten und größten Flächen des Polyeders aus.
     *
     * @param faces Eine {@link ArrayList} mit {@link Face}-Objekten, sortiert nach Fläche.
     * @Vorbedingung faces darf nicht null sein.
     * @Nachbedingung Die Konsole zeigt die ersten und letzten drei Flächenwerte an.
     */
    public static void partiallyPrintSortedList(ArrayList<Face> faces)
    {
        System.out.println();
        System.out.println(Constants.OUTPUT_PARTIAL_SORTED_LIST);
        for (int i = Constants.INDEX_ZERO; i < Constants.INDEX_THREE && i < faces.size(); i++)
        {
            System.out.println(Constants.OUTPUT_AREA +
                    faces.get(i).getPolygon().calculateArea());
        }
        int start = faces.size() - Constants.INDEX_THREE;

        if (start < Constants.INDEX_ZERO)
        {
            start = Constants.INDEX_ZERO;
        }

        System.out.println(Constants.OUTPUT_SEPARATOR);

        for (int i = start; i < faces.size(); i++)
        {
            System.out.println(Constants.OUTPUT_AREA +
                    faces.get(i).getPolygon().calculateArea());
        }
    }

    /**
     * Gibt den berechneten Oberflächeninhalt in der Konsole aus.
     *
     * @param surfaceArea Der zu zeigende Flächeninhalt.
     * @Vorbedingung surfaceArea wurde berechnet und ist gültig.
     * @Nachbedingung Die Konsole informiert über den Oberflächeninhalt.
     */
    public static void printSurfaceArea(float surfaceArea)
    {
        System.out.println(Constants.OUTPUT_SURFACE_AREA + ColorCodes.BLUE + surfaceArea + ColorCodes.RESET);
    }

    /**
     * Gibt das berechnete Volumen in der Konsole aus.
     *
     * @param volume Der zu zeigende Volumenwert.
     * @Vorbedingung volume wurde berechnet und ist gültig.
     * @Nachbedingung Die Konsole informiert über das Volumen.
     */
    public static void printVolume(float volume)
    {
        System.out.println(Constants.OUTPUT_VOLUME + ColorCodes.BLUE + volume + ColorCodes.RESET);
    }

    /**
     * Diese Methode gibt die benötigte Zeit für eine Operation aus.
     * @Vorbedingung  Der Parameter "time" muss die verstrichene Zeit in Millisekunden enthalten.
     * @Nachbedingung  Die benötigte Zeit wird über die Konsole ausgegeben.
     * @param time Die verstrichene Zeit in Millisekunden.
     */
    public static void timePassed(long time, String action)
    {
        System.out.println(String.format(Constants.OUTPUT_TIME_PASSED, action) + ColorCodes.YELLOW + time + ColorCodes.RESET +
                Constants.OUTPUT_UNITS_MILLISECONDS);
    }

    /**
     * Gibt das Format der eingelesenen STL-Datei (ASCII oder Binary) aus.
     *
     * @param isASCII true, wenn es sich um ASCII-STL handelt, sonst Binary.
     * @Vorbedingung Der Dateityp wurde korrekt erkannt.
     * @Nachbedingung Die Konsole zeigt das gefundene Format an.
     */
    public static void printFileFormat(boolean isASCII)
    {
        if (isASCII)
        {
            System.out.println(String.format(Constants.STL_FILE_FORMAT,
                    ColorCodes.PURPLE + Constants.ASCII_STL_FILE_FORMAT + ColorCodes.RESET));
        }
        else
        {
            System.out.println(String.format(Constants.STL_FILE_FORMAT,
                    ColorCodes.PURPLE + Constants.BINARY_STL_FILE_FORMAT + ColorCodes.RESET));
        }

        System.out.println();
    }

    /**
     * Gibt an, dass der Server läuft, und zeigt den Port an.
     *
     * @param port Der Port, auf dem der Server lauscht.
     * @Vorbedingung Keine.
     * @Nachbedingung Die Konsole zeigt die Server-Meldung an.
     */
    public static void printServerRunningInfo(int port)
    {
        System.out.println();
        System.out.println(Constants.SERVER_RUNNING_MESSAGE + ColorCodes.BLUE + port + ColorCodes.RESET);
    }

    /**
     * Gibt eine allgemeine Informationsnachricht in der Konsole aus.
     *
     * @param message Die zu zeigende Nachricht.
     * @Vorbedingung message darf nicht null sein.
     * @Nachbedingung Die Konsole gibt die Nachricht aus.
     */
    public static void printInformation(String message)
    {
        System.out.println(message);
    }

    /**
     * Gibt eine Antwortmeldung des Servers in der Konsole aus.
     *
     * @param response Der vom Server empfangene Antworttext.
     * @Vorbedingung response darf nicht null sein.
     * @Nachbedingung Die Konsole gibt die Antwort des Servers aus.
     */
    public static void printServerResponse(String response)
    {
        System.out.println(Constants.SERVER_RESPONSE_MESSAGE + response);
    }

    /**
     * Gibt eine eingehende Verbindung zum Server aus.
     *
     * @param socketAddress Die Adresse des verbundenen Clients.
     * @Vorbedingung socketAddress darf nicht null sein.
     * @Nachbedingung Die Konsole informiert über die Verbindung und die Adresse des Clients.
     */
    public static void printServerConnection(SocketAddress socketAddress)
    {
        System.out.println(Constants.SERVER_CONNECTION_FROM + socketAddress);
    }

    /**
     * Gibt die Euler-Charakteristik des Polyeders aus.
     *
     * @param polyhedron Das zu prüfende Polyhedron.
     * @param eulerCharacteristicValue Der berechnete Wert des Eulerschen Polyedersatzes.
     * @Vorbedingung polyhedron darf nicht null sein.
     * @Nachbedingung Die Konsole informiert über die Euler-Charkateristik des Polyeders.
     */
    public static void printEulerCharacteristicsInformation(Polyhedron polyhedron, int eulerCharacteristicValue)
    {
        if (polyhedron != null && eulerCharacteristicValue == Constants.EULER_CHARACTERISTIC_CLOSED_POLYHEDRON)
        {
            System.out.println(String.format(Constants.POLYEDER_EULER_INFORMATION,
                    PolyhedronController.isClosed(polyhedron), Constants.POLYEDER_EULER_CLOSED));
        }
        else if (polyhedron != null)
        {
            System.out.println(String.format(Constants.POLYEDER_EULER_INFORMATION,
                    PolyhedronController.isClosed(polyhedron), Constants.POLYEDER_EULER_OPEN));
        }
    }
}