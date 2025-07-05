package de.tharms.guiprog_ea_3.network;

import com.google.gson.Gson;
import de.tharms.guiprog_ea_3.model.Axis;
import de.tharms.guiprog_ea_3.model.Command;
import de.tharms.guiprog_ea_3.model.Constants;
import de.tharms.guiprog_ea_3.model.ServerCommands;
import de.tharms.guiprog_ea_3.view.ColorCodes;
import de.tharms.guiprog_ea_3.view.Output;
import de.tharms.guiprog_ea_3.view.ViewerController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread
{
    int port;
    boolean running;
    ServerSocket serverSocket;
    Gson gson;
    ViewerController viewerController;

    public Server(int port, ViewerController viewerController)
    {
        this.port = port;
        this.running = true;
        this.gson = new Gson();
        this.viewerController = viewerController;
    }

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

    private void executeCommand(Command command)
    {
        ServerCommands action = command.action;
        Axis axis = command.axis;
        float value = command.value;

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
                        ColorCodes.RED + Constants.SERVER_CLOSED + ColorCodes.RESET);
                close();
                break;
        }
    }

    public void close()
    {
        running = false;

        if (serverSocket != null && !serverSocket.isClosed())
        {
            try
            {
                serverSocket.close();
                Output.printInformation(Constants.SERVER_CLOSED);
            }
            catch (IOException ioException)
            {
                throw new RuntimeException(ioException);
            }
        }
    }
}
