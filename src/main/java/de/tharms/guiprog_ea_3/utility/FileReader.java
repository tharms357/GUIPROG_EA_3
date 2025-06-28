package de.tharms.guiprog_ea_3.utility;

import de.tharms.guiprog_ea_3.model.Constants;
import de.tharms.guiprog_ea_3.view.Output;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class FileReader
{

    /**
     * Diese Methode liest eine gegebene Datei zeilenweise ein und speichert diese Zeilen anschließend in einer ArrayList.
     * Außerdem wird über die Konsole die Zeit für den Lesevorgang ausgegeben.
     * @precondition Der angegebene Dateipfad zeigt auf eine existierende Datei; es bestehen ausreichende Lesezugriffsrechte auf diese Datei.
     * @postcondition Eine ArrayList wird zurückgegeben, in der jede Zeile der Datei als separates Element gespeichert ist;
     * die Bearbeitungszeit für den Einlesevorgang wird über die Konsole ausgegeben.
     * @param filepath Dateipfad der Datei, welche eingelesen werden soll.
     * @return Eine ArrayList mit Zeilen der gegebenen Datei als Elemente.
     */
    public static ArrayList<String> lineReader(String filepath)
    {
        Output.printFileReadingStart(filepath);

        //Erstellen einer ArrayList, in welche die gelesenen Zeilen eingefügt werden
        ArrayList<String> fileDataByLine = new ArrayList<>();

        //Einlesen der Datei
        try
        {
            Scanner scanner = new Scanner(new File(filepath));
            //so lange wie noch eine weitere Zeile in der Datei vorhanden ist
            while (scanner.hasNextLine())
            {
                //Hinzufügen der Zeile zur ArrayList
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
