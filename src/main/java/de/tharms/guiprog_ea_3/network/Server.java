package de.tharms.guiprog_ea_3.network;

import com.google.gson.Gson;
import de.tharms.guiprog_ea_3.model.Axis;
import de.tharms.guiprog_ea_3.model.Command;
import de.tharms.guiprog_ea_3.model.Constants;
import de.tharms.guiprog_ea_3.model.ServerCommands;
import de.tharms.guiprog_ea_3.view.ColorCodes;
import de.tharms.guiprog_ea_3.view.Output;
import de.tharms.guiprog_ea_3.controller.ViewerController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Server-Klasse, die JSON-Befehle entgegennimmt und an den ViewerController weiterleitet.
 */
public class Server extends Thread
{
    int port;
    boolean running;
    ServerSocket serverSocket;
    Gson gson;
    ViewerController viewerController;

    /**
     * Initialisiert einen neuen Server-Thread auf dem angegebenen Port und verknüpft ihn mit dem ViewerController.
     *
     * @param port Der TCP-Port, auf dem Verbindungen angenommen werden.
     * @param viewerController Der Controller, an den empfangene Commands weitergeleitet werden.
     * @Vorbedingung ViewerController darf nicht null sein.
     * @Nachbedingung Der Server ist startbereit, running ist auf true gesetzt.
     */
    public Server(int port, ViewerController viewerController)
    {
        this.port = port;
        this.running = true;
        this.gson = new Gson();
        this.viewerController = viewerController;
    }

    /**
     * Öffnet den ServerSocket und nimmt Verbindungen an,
     * liest JSON-Commands, führt sie aus und antwortet mit einer Bestätigung.
     *
     * @Vorbedingung Dieser Thread wurde gestartet und running ist gleich true.
     * @Nachbedingung Solange running gleich true ist, akzeptiert und verarbeitet der Server eingehende Befehle.
     */
    @Override
    public void run()
    {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Output.printServerRunningInfo(port);

        while (running)
        {
            Socket socket = null;
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Output.printServerConnection(socket.getRemoteSocketAddress());

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter writer = new PrintWriter(socket.getOutputStream(), true))
            {
                String inputMessage;
                while ((inputMessage = reader.readLine()) != null)
                {
                    Command command = gson.fromJson(inputMessage, Command.class);
                    executeCommand(command);
                    writer.println(ColorCodes.GREEN + Constants.COMMAND_EXECUTED + ColorCodes.RESET);
                }
            } catch (IOException e)
            {
                System.out.println(e.toString());
            }
        }
        try {
            serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Führt einen empfangenen Command anhand des Typs aus, indem er das ModelController-Objekt aufruft.
     *
     * @param command Das Command-Objekt, enthält action, axis und value.
     * @Vorbedingung command darf nicht null sein, action und axis sind gültige Enumerationen.
     * @Nachbedingung Die entsprechende Methode im ModelController wurde aufgerufen oder eine Fehlermeldung ausgegeben.
     */
    private void executeCommand(Command command)
    {
        ServerCommands action = command.action;
        Axis axis = command.axis;
        double value = command.value;

        switch (action)
        {
            case ROTATE:
                this.viewerController.getModelController().
                        rotateObject(axis, value);
                break;

            case TRANSLATE:
                this.viewerController.getModelController().
                        translateObject(axis, value);
                break;

            case EXIT:
                Output.printServerResponse(
                        ColorCodes.RED + Constants.CONNECTION_CLOSED + ColorCodes.RESET);
                break;

            default:
                Output.printServerResponse(
                        ColorCodes.RED + Constants.INVALID_COMMAND + ColorCodes.RESET);
                break;
        }
    }

    /**
     * Beendet den Server-Loop und schließt den ServerSocket.
     *
     * @Vorbedingung Der Server läuft (running gleich true) oder wurde zuvor gestartet.
     * @Nachbedingung running ist gleich false und der ServerSocket ist geschlossen.
     */
    public void close()
    {
        running = false;

        if (serverSocket != null && !serverSocket.isClosed())
        {
            try
            {
                serverSocket.close();
                Output.printInformation(Constants.CONNECTION_CLOSED);
            }
            catch (IOException ioException)
            {
                throw new RuntimeException(ioException);
            }
        }
    }
}
