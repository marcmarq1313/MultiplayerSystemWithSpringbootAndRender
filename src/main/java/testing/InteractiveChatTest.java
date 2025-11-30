package testing;

import java.util.Scanner;
import java.util.concurrent.ExecutionException;

import client.MyStompClient;
import model.Message;

public class InteractiveChatTest 
{

    public static void main(String[] args) throws InterruptedException, ExecutionException 
    {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter your username: ");
        String username = scanner.nextLine();

        MyStompClient client = new MyStompClient(username);

        System.out.println("Connected as " + username);
        System.out.println("Type messages (type 'exit' to quit):");

        while (true) 
        {
            String text = scanner.nextLine();
            if (text.equalsIgnoreCase("exit")) break;

            Message message = new Message(username, text);
            client.sendMessage(message);
        }

        client.disconnectUser(username);
        System.out.println("Chat session ended.");
        scanner.close();
    }
}
