package es.ubu.lsi.common;

import java.io.Serializable;

public class ChatMessage implements Serializable {

    private MessageType.Type type;
    private String message;
    private int id;

    /**
     * Constructor
     *
     * @param id client id
     * @param type message type
     * @param message message
     */
    public ChatMessage(int id, MessageType.Type type, String message) {
        this.setId(id);
        this.setType(type);
        this.setMessage(message);
    }

    /**
     * Get Type
     *
     * @return type
     */
    public MessageType.Type getType() {
        return type;
    }

    /**
     * Set Type
     *
     * @param type message type
     */
    public void setType(MessageType.Type type) {
        this.type = type;
    }

    /**
     * Get Message
     *
     * @return message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Set Message
     *
     * @param message message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Get Id
     *
     * @return client id
     */
    public int getId() {
        return id;
    }

    /**
     * Set Id
     *
     * @param id client id
     */
    public void setId(int id) {
        this.id = id;
    }
}
