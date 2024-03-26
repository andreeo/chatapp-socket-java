package es.ubu.lsi.server;

public interface ChatServer {

    /**
     * Start up the chat server
     */
    public void startup();

    /**
     * Shut down the chat server
     *
     */
    public void shutdown();

    /**
     * Sends a message to all clients connected to the server
     *
     * @param message message
     */
    public void broadcast(String message);

    /**
     * Remove client from the server
     *
     * @param id
     */
    public void remove(Integer id);
}
