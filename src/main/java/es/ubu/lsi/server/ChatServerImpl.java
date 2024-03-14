package es.ubu.lsi.server;

import es.ubu.lsi.common.ChatMessage;

import java.io.*;
import java.net.ServerSocket;
import java.text.SimpleDateFormat;
import java.net.Socket;
import java.util.ArrayList;

public class ChatServerImpl implements ChatServer {

    private Integer DEFAULT_PORT = 1500;
    private Integer clientId;
    private SimpleDateFormat sdf;
    private Boolean alive;

    private ServerSocket server;
    private ArrayList<ServerThreadForClient> registers;

    public ChatServerImpl(Integer port) {
        this.setDEFAULT_PORT(port);
    }

    public ChatServerImpl() {}

    @Override
    public void startup() {
        /* create socket server*/
        try {
            server = new ServerSocket(this.getDEFAULT_PORT());
            while(alive) {
                /* call the accept method, because its necessary
                * return a client socket*/
                Socket client = server.accept();
                /* each time, instance a new server thread for
                client*/
                ServerThreadForClient clientThread =
                        new ServerThreadForClient(client);
                /* save each thread register for push and correct
                shutdown*/
                registers.add(clientThread);
            }
        } catch (IOException e) {
            // TODO: implement
        }
    }

    @Override
    public void shutdown() {
        // TODO: implement
    }

    @Override
    public void broadcast(ChatMessage message) {
        // TODO: implement
    }

    @Override
    public void remove(String id) {
        // TODO: implement
    }

    public Integer getDEFAULT_PORT() {
        return DEFAULT_PORT;
    }

    public void setDEFAULT_PORT(Integer DEFAULT_PORT) {
        this.DEFAULT_PORT = DEFAULT_PORT;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public SimpleDateFormat getSdf() {
        return sdf;
    }

    public void setSdf(SimpleDateFormat sdf) {
        this.sdf = sdf;
    }

    public Boolean getAlive() {
        return alive;
    }

    public void setAlive(Boolean alive) {
        this.alive = alive;
    }

    class ServerThreadForClient extends Thread {

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public Socket getClient() {
            return client;
        }

        public void setClient(Socket client) {
            this.client = client;
        }

        private long id;

        /* username for client */
        private String username;
        /* socket client */
        private Socket client;
        /* buffer reader to get the stream from the socket */
        private BufferedReader inputStream;
        /* and print writer to write something to the client */
        private PrintWriter outputStream;

        public ServerThreadForClient(Socket client) {
            this.setClient(client);
        }

        @Override
        public void run() {
            try {
                inputStream =
                        new BufferedReader(
                                new InputStreamReader(client.getInputStream()));
                outputStream =
                        new PrintWriter(client.getOutputStream(), true);
                /*send message to client insert nickname, id fill automatically
                * with Thread id using getId*/
                outputStream.printf("Welcome to ChatApp 1.0");
                outputStream.printf("Please, insert your username:");
                /* set id and username */
                this.id = Thread.currentThread().getId();
                this.setUsername(inputStream.readLine());
                /* development proposes */
                System.out.printf(
                        "[[CONNECTION]] USER: %s , STATUS: CONNECTED", this.getUsername());

                String message;
                while((message = inputStream.readLine()) != null) {
                    if(message.startsWith("ban ")) {
                        // TODO: implement
                    } else if(message.startsWith("unban ")) {
                        // TODO: implement
                    } else {
                        // TODO: implement, here we can broadcast the messages
                    }
                }

            } catch (IOException e) {
                // TODO: implement
            }
        }
    }

    public static void main(String[] args) {
        ChatServerImpl server = new ChatServerImpl(9000);
        server.startup();
    }
}
