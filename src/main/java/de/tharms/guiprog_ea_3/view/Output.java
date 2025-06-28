package de.tharms.guiprog_ea_3.view;

import de.tharms.guiprog_ea_3.model.Constants;
import de.tharms.guiprog_ea_3.model.Face;

import java.util.ArrayList;

public class Output
{
    public static void printFileReadingStart(String filepath)
    {
        System.out.println(Constants.OUTPUT_READING_FILE_START_INFO);
        System.out.println(filepath);
    }

    public static void fileNotFound()
    {
        System.out.println(ColorCodes.RED + Constants.FILE_NOT_FOUND + ColorCodes.RESET);
    }

    public static void filenameUserInput()
    {
        System.out.println(Constants.FILENAME_USER_INPUT);
    }

    public static void partiallyPrintSortedList(ArrayList<Face> faces)
    {
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

    public static void printSurfaceArea(float surfaceArea)
    {
        System.out.println(Constants.OUTPUT_SURFACE_AREA + surfaceArea);
    }

    public static void printVolume(double volume)
    {
        System.out.println();
        System.out.println(Constants.OUTPUT_VOLUME + volume);
    }

    /**
     * Diese Methode gibt die benötigte Zeit für eine Operation aus.
     * @precondition Der Parameter "time" muss die verstrichene Zeit in Millisekunden enthalten.
     * @postcondition Die benötigte Zeit wird über die Konsole ausgegeben.
     * @param time Die verstrichene Zeit in Millisekunden.
     */
    public static void timePassed(long time)
    {
        System.out.println(Constants.OUTPUT_TIME_PASSED + ColorCodes.YELLOW + time + ColorCodes.RESET +
                Constants.OUTPUT_UNITS_MILLISECONDS);
    }
}