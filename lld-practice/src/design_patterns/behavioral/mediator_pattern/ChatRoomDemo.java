package design_patterns.behavioral.mediator_pattern;

import java.util.ArrayList;
import java.util.List;

interface ChatMediator {
    void sendMessage(String message, User sender);
    void addUser(User user);
}

abstract class User {
    protected ChatMediator mediator;
    protected String name;

    public User(ChatMediator mediator, String name){
        this.mediator = mediator;
        this.name = name;
    }

    public abstract void send(String message);
    public abstract void receive(String message, String from);
    public String getName() {
        return name;
    }
}

class ChatRoom implements ChatMediator {
    private final List<User> users = new ArrayList<>();

    @Override
    public void addUser(User user) {
        users.add(user);
        System.out.println(user.getName() + " joined the chat.");
    }

    @Override
    public void sendMessage(String message, User sender) {
        for (User user: users){
            if(user != sender) {
                user.receive(message, sender.getName());
            }
        }
    }
}

class ChatUser extends User {
    public ChatUser(ChatMediator mediator, String name){
        super(mediator, name);
    }

    @Override
    public void send(String message) {
        System.out.println(name + " sends: " + message);
        mediator.sendMessage(message, this);
    }

    @Override
    public void receive(String message, String from) {
        System.out.println(name + " receives from " + from + ": " + message);
    }
}

public class ChatRoomDemo {
    public static void main(String[] args) {
        ChatRoom chatRoom = new ChatRoom();

        User alice = new ChatUser(chatRoom, "Alice");
        User bob = new ChatUser(chatRoom, "Bob");
        User charlie = new ChatUser(chatRoom, "Charlie");

        chatRoom.addUser(alice);
        chatRoom.addUser(bob);
        chatRoom.addUser(charlie);

        alice.send("Hey everyone!");
        System.out.println();
        bob.send("Hi Alice!");
    }
}
