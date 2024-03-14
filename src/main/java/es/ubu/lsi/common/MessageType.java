package es.ubu.lsi.common;

public interface MessageType {

    enum Type {
        MESSAGE,
        SHUTDOWN,
        LOGOUT,
    }
}
