package lib;

import constants.Command;
import constants.Config;
import models.Room;
import utils.StringUtils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class Channel extends Thread {
    private final MulticastIpGenerator multicastIpGenerator;
    private final Map<String, Room> rooms;
    private final Set<String> users;

    private DataInputStream input;
    private DataOutputStream output;
    private Socket socket;

    public Channel(MulticastIpGenerator multicastIpGenerator, Map<String, Room> rooms, Set<String> users, Socket clientSocket) {
        this.multicastIpGenerator = multicastIpGenerator;
        this.users = users;
        this.rooms = rooms;

        try {
            socket = clientSocket;
            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());
            this.start();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void run() {
        boolean userConnected = true;
        String username = null;
        Room room = null;

        try {
            while (username == null) {
                username = input.readUTF();

                if (users.contains(username)) {
                    username = null;
                    output.writeUTF("");
                } else {
                    users.add(username);
                    output.writeUTF(username);
                }
            }

            while (userConnected) {
                final String message = input.readUTF();
                final String[] messageData = message.split("\\s", 2);

                final String command = messageData[0];
                final String argument = messageData.length > 1 ? messageData[1] : null;

                final String commandValue = StringUtils.leftTrim(command.toLowerCase(Locale.ROOT), Config.COMMAND_PREFIX);

                if (room == null) {
                    switch (commandValue) {
                        case Command.CREATE_ROOM:
                            if (argument == null || rooms.get(argument) != null) {
                                output.writeUTF("");
                            } else {
                                final String address = multicastIpGenerator.generate();
                                rooms.put(argument, new Room(argument, address));
                                output.writeUTF(address);
                            }
                            break;
                        case Command.JOIN_ROOM:
                            room = rooms.get(argument);
                            if (room == null) {
                                output.writeUTF("");
                            } else {
                                room.getUsers().add(username);
                                output.writeUTF(room.getAddress());
                            }
                            break;
                        case Command.LIST_ROOMS:
                            output.writeUTF(Arrays.toString(rooms.values().toArray()));
                            break;
                        case Command.EXIT:
                            userConnected = false;
                        default:
                            output.writeUTF("");
                            break;
                    }
                } else {
                    switch (commandValue) {
                        case Command.LIST_USERS:
                            output.writeUTF(Arrays.toString(room.getUsers().toArray()));
                            break;
                        case Command.LEAVE_ROOM:
                            room.getUsers().remove(username);
                            room = null;
                            output.writeUTF("");
                            break;
                        default:
                            output.writeUTF("");
                            break;
                    }
                }
            }
        } catch (IOException exception) {
            this.interrupt();
        } finally {
            try {
                socket.close();
                users.remove(username);
                if (room != null) {
                    room.getUsers().remove(username);
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }
}
