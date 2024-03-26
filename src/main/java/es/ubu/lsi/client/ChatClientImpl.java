package es.ubu.lsi.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatClientImpl implements ChatClient {

    private Socket client;
    private BufferedReader inputReader;
    private PrintWriter outputWriter;

    private String server;

    private String username;

    private Integer port;

    private boolean carryOn;


    public ChatClientImpl(String server, Integer port, String username) {
        this.setCarryOn(true);
        this.setServer(server);
        this.setPort(port);
        this.setUsername(username);
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public boolean isCarryOn() {
        return carryOn;
    }

    public void setCarryOn(boolean carryOn) {
        this.carryOn = carryOn;
    }

    @Override
    public void start() {
        try {
            client = new Socket(this.getServer(), this.getPort());
            outputWriter = new PrintWriter(client.getOutputStream(), true);
            inputReader = new BufferedReader(new InputStreamReader(client.getInputStream()));

            ChatClientListener clientListener = new ChatClientListener();
            Thread thread = new Thread(clientListener);
            thread.start();

            outputWriter.println(this.getUsername());

            String message;
            while((message = inputReader.readLine()) != null) {
                System.out.println(message);
            }
        } catch (IOException e) {
            disconnect();
        }
    }

    @Override
    public void sendMessage(String msg) {
        outputWriter.println(msg);
    }

    @Override
    public void disconnect() {
        this.setCarryOn(false);
        try {
            inputReader.close();
            outputWriter.close();
            if(!client.isClosed()) {
                client.close();
            }
        } catch (IOException e) {
            // nothing
        }
    }

    class ChatClientListener implements Runnable {

        @Override
        public void run() {
            try {
                BufferedReader inputR = new BufferedReader(new InputStreamReader(System.in));
                long id = Thread.currentThread().getId();
                while(isCarryOn()) {
                    String message = inputR.readLine();
                    if(message.equals("logout"))  {
                        sendMessage(message);
                        sendMessage(username+" left the chat");
                        inputR.close();
                        disconnect();
                    } else {
                        sendMessage(message);
                    }
                }
            } catch (IOException e) {
                disconnect();
            }

        }
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java es.ubu.lsi.client.ChatClientImpl <server_address> <nickname>");
            return;
        }
        String serverAddress = args[0];
        String username = args[1];

        ChatClientImpl client = new ChatClientImpl(serverAddress, 9999, username);
        client.start();
    }
}
