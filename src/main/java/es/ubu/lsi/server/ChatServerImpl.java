package es.ubu.lsi.server;

import es.ubu.lsi.common.ChatMessage;

import java.text.SimpleDateFormat;

public class ChatServerImpl implements ChatServer {

    private Integer DEFAULT_PORT = 1500;
    private Integer id;
    private SimpleDateFormat sdf;
    private Boolean alive;

    public ChatServerImpl(Integer port) {
        this.setDEFAULT_PORT(port);
    }

    public ChatServerImpl() {}

    @Override
    public void startup() {
        // TODO: implement
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    class ChatServerThreadForClient extends Thread {

        private Integer id;
        private String username;

        @Override
        public void run() {
            // TODO: implement
        }
    }

    public static void main(String[] args) {
        ChatServerImpl server = new ChatServerImpl(9000);
        server.startup();
    }
}
