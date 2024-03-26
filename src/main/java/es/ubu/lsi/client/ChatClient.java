package es.ubu.lsi.client;

import es.ubu.lsi.common.ChatMessage;

public interface ChatClient {

    public void start();

    public void sendMessage(String msg);

    public void disconnect();
}
