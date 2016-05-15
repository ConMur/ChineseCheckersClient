package io;

import java.util.Comparator;

/**
 * A message to or from the server that has a priority and can be sorted based on its priority
 * @author Connor Murphy
 */
public class Message implements Comparator<Message>, Comparable<Message>{
    private Priority priority;
    private String message;
    private ServerCommand command;

    /**
     * Creates a message with the given parameters
     * @param message the string representation of the message
     * @param command the type of message it is
     * @param priority the priority of the message
     */
    public Message(String message, ServerCommand command, Priority priority)
    {
        this.priority = priority;
        this.message = message;
        this.command = command;
    }


    /**
     * Creates a message with the given parameters
     * @param message the string representation of the message
     * @param command the type of message it is numerically
     * @param priority the priority of the message
     */
    public Message(String message, int command, Priority priority)
    {
        // Change the numerical command into the corresponding enum
        // value
        ServerCommand cmd;
        try {
            cmd = ServerCommand.values()[command];
        }
        catch(ArrayIndexOutOfBoundsException e)
        {
            throw new IllegalArgumentException(command + " is not valid command");
        }

        this.priority = priority;
        this.message = message;
        this.command = cmd;
    }

    /**
     * Returns the priority of this message
     * @return the priority of this message
     */
    public Priority getPriority() {
        return priority;
    }

    /**
     * Returns the string representation of this message
     * @return the string representation of this message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Returns the type of message this message is
     * @return the type of message this message is
     */
    public ServerCommand getCommand() {
        return command;
    }

    public int compare(Message first, Message second)
    {
        return first.getPriority().ordinal() - second.getPriority().ordinal();
    }

    public int compareTo(Message other)
    {
        return priority.ordinal() - other.getPriority().ordinal();
    }
}
