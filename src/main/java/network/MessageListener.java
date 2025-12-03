package network;

import java.util.List;

public interface MessageListener 
{

    void onMessageReceive(Message message);

    void onActiveUsersUpdated(List<String> users);
}
