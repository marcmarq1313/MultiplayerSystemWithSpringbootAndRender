package network;

import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class MyStompClient 
{
    private static final String WS_URL = "https://multiplayersystemwithspringbootandrender.onrender.com/ws";

    private final StompSession session;
    private final String username;

    public MyStompClient(MessageListener messageListener, String username) throws ExecutionException, InterruptedException 
    {
        this.username = username;

        List<Transport> transports = List.of(new WebSocketTransport(new StandardWebSocketClient()));
        SockJsClient sockJsClient = new SockJsClient(transports);

        WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSessionHandler sessionHandler = new MyStompSessionHandler(messageListener, username);

        this.session = stompClient.connectAsync(WS_URL, sessionHandler).get();
    }

    public void sendMessage(Message message) 
    {
        try
        {
            session.send("/app/message", message);
        } 
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void disconnectUser(String username) 
    {
        try
        {
            session.send("/app/disconnect", username);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
