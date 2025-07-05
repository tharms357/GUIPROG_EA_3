package de.tharms.guiprog_ea_3.network;

import com.google.gson.Gson;
import de.tharms.guiprog_ea_3.model.Axis;
import de.tharms.guiprog_ea_3.model.Command;
import de.tharms.guiprog_ea_3.model.Constants;
import de.tharms.guiprog_ea_3.model.ServerCommands;
import de.tharms.guiprog_ea_3.view.Output;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ServerClient
{
    public static void main(String[] args)
    {
        startClient(Constants.TCP_PORT, Constants.TCP_HOSTNAME);
    }

    public static void startClient(int port, String host)
    {
        boolean running = true;
        Gson gson = new Gson();

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
                    socket.close();
                    break;
                }

                String[] inputValues = inputMessage.split(Constants.CLIENT_INPUT_MESSAGE_SPLIT_REGEX);
                if (inputValues.length != 3) {
                    Output.printInformation(Constants.CLIENT_CONSOLE_INFO);
                    continue;
                }

                Command command = new Command(
                        ServerCommands.valueOf(inputValues[Constants.INDEX_ZERO].toUpperCase()),
                        Axis.valueOf(inputValues[Constants.INDEX_ONE].toUpperCase()),
                        Float.parseFloat(inputValues[Constants.INDEX_TWO]));

                String json = gson.toJson(command);
                sendCommand(socket, output, json);

                receiveCommand(input);
            }
        }
        catch (NumberFormatException numberFormatException)
        {
            //TODO
            System.out.println(Constants.INVALID_FLOAT_VALUE + numberFormatException.getMessage());
        }
        catch (IOException ioException)
        {
            //TODO Exception fixen
            System.out.println("IOException" + ioException.getMessage());
        }

        Output.printInformation(Constants.SERVER_CLOSED);
    }

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


    private static void sendCommand(Socket socket, PrintWriter printWriter, String json)
    {
        printWriter.println(json);
    }

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
}
