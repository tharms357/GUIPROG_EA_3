package de.tharms.guiprog_ea_3.network;

import com.google.gson.Gson;
import de.tharms.guiprog_ea_3.model.Axis;
import de.tharms.guiprog_ea_3.model.Command;
import de.tharms.guiprog_ea_3.model.Constants;
import de.tharms.guiprog_ea_3.model.ServerCommands;
import de.tharms.guiprog_ea_3.view.ColorCodes;
import de.tharms.guiprog_ea_3.view.Output;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Client-Klasse, die sich mit dem Server verbindet und JSON-Commands sendet.
 */
public class ServerClient
{
    /**
     * Start des Clients. Verbindet sich zum Server und startet die Command-Verarbeitung.
     *
     * @param args Kommandozeilenargumente (werden nicht verwendet).
     * @Vorbedingung Keine.
     * @Nachbedingung Der Client wurde gestartet und beendet nach Eingabe von "exit".
     */
    public static void main(String[] args)
    {
        startClient(Constants.TCP_PORT, Constants.TCP_HOSTNAME);
    }


    /**
     * Erzeugt und öffnet einen Client-Socket zum angegebenen Host und Port.
     *
     * @param host Hostname oder IP-Adresse des Servers.
     * @param port Port, auf dem der Server lauscht.
     * @return Ein verbundener {@link Socket}.
     * @Vorbedingung host darf nicht null sein, port liegt im gültigen Bereich.
     * @Nachbedingung Ein offener Socket wird zurückgegeben.
     * @throws RuntimeException mit erläuternder Fehlermeldung.
     */
    public static Socket createClient(String host, int port)
    {
        try
        {
            return new Socket(host, port);
        }
        catch (IOException ioException)
        {
            throw new RuntimeException(Constants.SOCKET_CREATION_FAILURE + ioException.getMessage());
        }
    }

    /**
     * Öffnet eine Socket-Verbindung zum Server, liest Eingaben aus der Konsole und sendet JSON-Commands.
     *
     * @param port Port, unter dem der Server erreichbar ist.
     * @param host Host-Adresse des Servers.
     * @Vorbedingung port liegt im gültigen Bereich, host darf nicht null sein.
     * @Nachbedingung Alle Benutzereingaben wurden als JSON an den Server gesendet und die Antworten angezeigt.
     */
    public static void startClient(int port, String host)
    {
        Output.printInformation(String.format(Constants.CLIENT_START_INFO, host, port));
        Output.printInformation(Constants.COMMAND_SYNTAX);

        boolean running = true;
        Gson gson = new Gson();

        // Öffnen von Socket, Scanner, Reader und Writer
        try (Socket socket = createClient(host, port);
             Scanner consoleInput = new Scanner(System.in);
             PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream())))
        {
            while (running)
            {
                Output.printInformation(Constants.CLIENT_CONSOLE_INFO);
                String inputMessage = consoleInput.nextLine().trim();

                if (inputMessage.equalsIgnoreCase(Constants.COMMAND_EXIT))
                {
                    // Command zum Beenden des Clients, Achse und Wert sind Platzhalter
                    Command command = new Command(ServerCommands.EXIT, Axis.X, 0);
                    String json = gson.toJson(command);

                    sendCommand(output, json);

                    receiveCommand(input);

                    socket.close();
                    break;
                }

                // Aufteilen der Eingabe in Command, Axis und Wert
                String[] inputValues = inputMessage.split(Constants.CLIENT_INPUT_MESSAGE_SPLIT_REGEX);
                if (inputValues.length != Constants.NUMBERS_THREE)
                {
                    Output.printInformation(ColorCodes.RED + Constants.INVALID_COMMAND + ColorCodes.RESET);
                    continue;
                }

                // Erstellen und Senden des Commands
                Command command = new Command(
                        ServerCommands.valueOf(inputValues[Constants.INDEX_ZERO].toUpperCase()),
                        Axis.valueOf(inputValues[Constants.INDEX_ONE].toUpperCase()),
                        Float.parseFloat(inputValues[Constants.INDEX_TWO]));

                String json = gson.toJson(command);
                sendCommand(output, json);

                // Antwort des Servers empfangen und ausgeben
                receiveCommand(input);
            }
        }
        catch (NumberFormatException numberFormatException)
        {
            Output.printInformation(Constants.INVALID_FLOAT_VALUE + numberFormatException.getMessage());
        }
        catch (IOException ioException)
        {
            Output.printInformation(Constants.ERROR_CLOSING_SOCKET + ioException.getMessage());
        }

        Output.printInformation(Constants.CONNECTION_CLOSED);
    }

    /**
     * Liest eine Antwortzeile vom Server und gibt sie aus.
     *
     * @param bufferedReader Reader, mit dem vom Server gelesen wird.
     * @Vorbedingung bufferedReader darf nicht null sein.
     * @Nachbedingung Eine Zeile wurde eingelesen und durch Output.printServerResponse angezeigt.
     */
    private static void receiveCommand(BufferedReader bufferedReader)
    {
        String serverResponse = Constants.EMPTY_STRING;
        try
        {
            serverResponse = bufferedReader.readLine();
        }
        catch (IOException ioException)
        {
            throw new RuntimeException(ioException);
        }
        Output.printServerResponse(serverResponse);
    }

    /**
     * Sendet einen JSON-formatierten Command-String an den Server.
     *
     * @param printWriter Writer, der auf den Server-Socket-Ausgang zeigt.
     * @param json Der als String serialisierte Command in JSON-Format.
     * @Vorbedingung printWriter und json dürfen nicht null sein.
     * @Nachbedingung Der JSON-String wurde an den Server gesendet.
     */
    private static void sendCommand(PrintWriter printWriter, String json)
    {
        printWriter.println(json);
    }
}
