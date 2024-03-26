package es.ubu.lsi.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServerImpl implements ChatServer {

    private Integer DEFAULT_PORT = 1500;
    private Boolean alive;

    private ServerSocket server;
    private final ArrayList<ServerThreadForClient> registers;
    private final HashSet<String> blacklist;

    public ChatServerImpl(Integer port) {
        this.setAlive(true);
        this.setDEFAULT_PORT(port);
        registers = new ArrayList<>();
        blacklist = new HashSet<>();
    }

    @Override
    public void startup() {
        /* create socket server*/
        try {
            server = new ServerSocket(this.getDEFAULT_PORT());
            ExecutorService pool = Executors.newCachedThreadPool();
            while(this.getAlive()) {
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
                pool.execute(clientThread);
            }
        } catch (IOException e) {
            shutdown();
        }
    }

    @Override
    public void shutdown() {
        try {
            this.setAlive(false);
            if(!server.isClosed()) {
                server.close();
            }
        } catch (IOException e) {
            // nothing
        }
        for(ServerThreadForClient register: registers) {
            register.shutdown();
        }
    }

    @Override
    public void broadcast(String message) {
        for(ServerThreadForClient register: registers) {
            if(register != null) {
                register.sendMsg(message);
            }
        }
    }

    @Override
    public void remove(Integer id) {
        // TODO: implement
    }

    public Integer getDEFAULT_PORT() {
        return DEFAULT_PORT;
    }

    public void setDEFAULT_PORT(Integer DEFAULT_PORT) {
        this.DEFAULT_PORT = DEFAULT_PORT;
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


        /* username for client */
        private String username;
        /* socket client */
        private final Socket client;
        /* buffer reader to get the stream from the socket */
        private BufferedReader inputStream;
        /* and print writer to write something to the client */
        private PrintWriter outputStream;

        public ServerThreadForClient(Socket client) {

            this.client =  client;
        }

        @Override
        public void run() {
            try {
                outputStream = new PrintWriter(client.getOutputStream(), true);
                inputStream = new BufferedReader(
                                new InputStreamReader(client.getInputStream()));
                /*send message to client insert nickname, id fill automatically
                * with Thread id using getId*/
                outputStream.println("Welcome to ChatApp 1.0");
                /* set id and username */
                this.setUsername(new Scanner(inputStream.readLine()).next());
                long id = Thread.currentThread().getId();
                broadcast(this.getUsername()+" joined the chat");

                /* development proposes */
                System.out.printf(
                        "[[CONNECTION]] USER: %s , " +
                                "STATUS: CONNECTED\n", this.getUsername());

                String message;
                while((message = inputStream.readLine()) != null) {
                    if(message.startsWith("ban ") || message.startsWith("unban ")) {
                        String[] words = message.split(" ",2);
                        if(words.length == 2) {
                            String otherUser = words[1];
                            if(words[0].equals("ban")) {
                                if(!blacklist.contains(otherUser)) {
                                    blacklist.add(otherUser);
                                    broadcast(otherUser+" banned by "+username);
                                    /* development proposes */
                                    System.out.printf("%s banned by %s", otherUser, username);
                                    System.out.println(" ");
                                } else {
                                    System.out.println("the user " + username + " has already been banned ");
                                }
                            } else {
                                if(blacklist.contains(otherUser)) {
                                    blacklist.remove(otherUser);
                                    broadcast(otherUser+" unbanned by "+username);
                                    /* development proposes */
                                    System.out.printf("%s unbanned by %s", otherUser, username);
                                    System.out.println(" ");
                                } else {
                                    System.out.println("the user " + username + " isn't banned ");
                                }
                            }
                        } else {
                            outputStream.println("Required a username");
                        }
                    } else if(message.equals("logout"))  {
                        broadcast(username+" left chat");
                        /* development proposes */
                        System.out.printf(
                                "[[CONNECTION]] USER: %s , STATUS: DISCONNECTED\n", this.getUsername());
                        shutdown();
                    } else {
                        if(!blacklist.contains(this.getUsername())) {
                            broadcast(this.getUsername()+": "+message);
                        } else {
                            System.out.println("You are banned");
                        }
                    }
                }

            } catch (Exception e) {
                shutdown();
            }
        }

        public void sendMsg(String message) {
            outputStream.println(message);
        }

        public void shutdown() {
            try {
                inputStream.close();
                outputStream.close();
                if(!this.client.isClosed()) {
                    this.client.close();
                }
            } catch (IOException e) {
                // nothing
            }
        }
    }

    public static void main(String[] args) {
        ChatServerImpl server = new ChatServerImpl(9999);
        server.startup();
    }
}
