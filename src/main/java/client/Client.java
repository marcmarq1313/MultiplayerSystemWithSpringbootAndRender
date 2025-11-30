package client;

import java.util.concurrent.ExecutionException;

import model.Message;

public class Client 
{
	public static void main(String[] args) throws InterruptedException, ExecutionException
	{
		MyStompClient myStompClient = new MyStompClient("Tap Tap");
		myStompClient.sendMessage(new Message("Tap Tap","Hello World!"));
		myStompClient.disconnectUser("Tap Tap");
		
	}
}
