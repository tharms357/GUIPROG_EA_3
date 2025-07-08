package de.tharms.guiprog_ea_3.utility;

import de.tharms.guiprog_ea_3.model.Constants;
import de.tharms.guiprog_ea_3.view.Output;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Bietet Methoden zum Einlesen von Dateien.
 */
public class FileReader
{
    /**
     * Diese Methode liest eine gegebene Datei zeilenweise ein und speichert diese Zeilen anschließend in einer ArrayList.
     * @Vorbedingung  Der angegebene Dateipfad zeigt auf eine existierende Datei; es bestehen ausreichende Lesezugriffsrechte auf diese Datei.
     * @Nachbedingung  Eine ArrayList wird zurückgegeben, in der jede Zeile der Datei als separates Element gespeichert ist.
     * @param filepath Dateipfad der Datei, welche eingelesen werden soll.
     * @return Eine ArrayList mit Zeilen der gegebenen Datei als Elemente.
     */
    public static ArrayList<String> lineReader(String filepath)
    {
        ArrayList<String> fileDataByLine = new ArrayList<>();

        // Einlesen der Datei
        try
        {
            Scanner scanner = new Scanner(new File(filepath));
            while (scanner.hasNextLine())
            {
                fileDataByLine.add(scanner.nextLine());
            }
            scanner.close();
        }

        catch (FileNotFoundException e)
        {
            Output.fileNotFound();
            System.exit(Constants.ERROR_CODE_EXIT);
        }

        return fileDataByLine;
    }
}
