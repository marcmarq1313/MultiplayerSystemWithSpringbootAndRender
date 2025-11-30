package Session;

import java.lang.reflect.Type;

import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import model.Message;

public class MyStompSessionHandler extends StompSessionHandlerAdapter
{
	private String username;
	
	public MyStompSessionHandler(String username)
	{
		this.username = username;
	}
	
	@Override
	public void afterConnected(StompSession session, StompHeaders connectedHeaders)
	{
		System.out.println("Client Connected");
		session.send("/app/connect", username);
		
		session.subscribe("/topic/messages", new StompFrameHandler() 
		{
			@Override
			public Type getPayloadType(StompHeaders headers) 
			{
				return Message.class;
			}
			
			@Override
			public void handleFrame(StompHeaders headers, Object payload)
			{
				try {
					if(payload instanceof Message)
					{
						Message message = (Message) payload;
						System.out.println("Recieved message: " + message.getUser() + ": " + message.getMessage());	
					}
					else
					{
						System.out.println("Recieved unexpected payload type: " + payload.getClass());
					}
				}catch (Exception e)
				{
					e.printStackTrace();
				}
			}
			
		});
		System.out.println("Client subscribed to /topic/messages");

	}
	
	@Override
	public void handleTransportError(StompSession session, Throwable exception)
	{
		exception.printStackTrace();
	}
}
