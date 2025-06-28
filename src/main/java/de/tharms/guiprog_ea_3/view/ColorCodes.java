package de.tharms.guiprog_ea_3.view;

/**
 * Dieses Interface enthält die im Programm benutzten Farbcodes.
 * @author Thilo Harms
 */
public interface ColorCodes
{
    //ANSI-Farbcodes
    String RESET = "\u001B[0m";  //Farbe zurücksetzen
    String RED = "\033[1;31m";   //Rot
    String GREEN = "\033[1;32m"; //Grün
    String YELLOW = "\033[1;33m"; //Gelb
    String BLUE = "\033[1;34m";  //Blau
    String PURPLE = "\033[1;35m"; //Lila
}
