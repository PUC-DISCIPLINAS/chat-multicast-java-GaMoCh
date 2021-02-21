import constants.Command;
import constants.Config;
import constants.Dialog;
import constants.Helper;
import utils.StringUtils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.stream.Collectors;

public final class Client {
    private static MulticastSocket multicastSocket;
    private static InetAddress roomAddress;
    private static String username = "";

    private Client() {
    }

    private static void showHelp(boolean isConnected) {
        final StringBuilder stringBuilder = new StringBuilder().append('\n');

        final List<Helper> helpers = Arrays.stream(Helper.values()).filter(
            helper -> helper.isWhenConnected() == isConnected
        ).collect(Collectors.toList());

        helpers.forEach(helper -> stringBuilder.append(
            String.format(Dialog.HELP, helper.getMethod(), helper.getDescription())
        ));

        if (isConnected) {
            stringBuilder.append('\n');
        }

        System.out.print(stringBuilder.toString());
    }

    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);

        boolean isConnected = false;

        try {
            final Socket socket = new Socket(InetAddress.getLoopbackAddress().getHostAddress(), Config.SERVER_PORT);
            final DataInputStream input = new DataInputStream(socket.getInputStream());
            final DataOutputStream output = new DataOutputStream(socket.getOutputStream());

            while (username.isEmpty()) {
                System.out.print(Dialog.USERNAME_INPUT);
                username = scanner.nextLine().trim().toLowerCase(Locale.ROOT);

                if (username.isEmpty()) {
                    System.out.print(Dialog.EMPTY_USERNAME);
                    continue;
                }

                output.writeUTF(username);
                username = input.readUTF();

                if (username.isEmpty()) {
                    System.out.print(Dialog.USERNAME_EXISTS);
                }
            }

            while (true) {
                while (!isConnected) {
                    System.out.print(Dialog.CLIENT_PS1);
                    final String message = scanner.nextLine().trim().toLowerCase(Locale.ROOT);

                    output.writeUTF(message);
                    final String payload = input.readUTF();

                    final String[] messageData = message.split("\\s", 2);

                    final String command = messageData[0];
                    final String argument = messageData.length > 1 ? messageData[1] : null;

                    switch (StringUtils.leftTrim(command.toLowerCase(Locale.ROOT), Config.COMMAND_PREFIX)) {
                        case Command.CREATE_ROOM:
                            if (argument == null) {
                                System.out.print(Dialog.CREATE_ROOM_ARGUMENT);
                            } else if (payload.isEmpty()) {
                                System.out.printf(Dialog.CREATE_ROOM_ERROR, argument);
                            } else {
                                System.out.printf(Dialog.CREATE_ROOM, argument);
                            }
                            break;
                        case Command.JOIN_ROOM:
                            if (argument == null) {
                                System.out.print(Dialog.JOIN_ROOM_ARGUMENT);
                            } else if (payload.isEmpty()) {
                                System.out.print(Dialog.JOIN_ROOM_ERROR);
                            } else {
                                roomAddress = InetAddress.getByName(payload);
                                multicastSocket = new MulticastSocket(Config.MULTICAST_PORT);
                                multicastSocket.joinGroup(roomAddress);

                                System.out.printf(Dialog.JOIN_ROOM, argument);
                                isConnected = true;
                            }
                            break;
                        case Command.LIST_ROOMS:
                            System.out.printf(Dialog.LIST_ROOMS, payload);
                            break;
                        case Command.HELP:
                            showHelp(false);
                            break;
                        case Command.EXIT:
                            System.out.print(Dialog.EXIT);
                            System.exit(0);
                            break;
                        default:
                            System.out.print(Dialog.INVALID_COMMAND);
                            break;
                    }
                }

                new Thread(() -> {
                    try {
                        while (true) {
                            final byte[] buffer = new byte[1024];
                            final DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length, roomAddress, Config.MULTICAST_PORT);
                            multicastSocket.receive(messageIn);

                            final String message = new String(messageIn.getData());
                            final String messageUsername = message.split(Config.MESSAGE_SEPARATOR)[0];

                            if (!messageUsername.equals(username)) {
                                System.out.print(message);
                            }
                        }
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                }).start();

                while (isConnected) {
                    final String message = scanner.nextLine().trim().toLowerCase(Locale.ROOT);
                    output.writeUTF(message);

                    final String payload = input.readUTF();
                    final String command = message.split("\\s", 2)[0];

                    if (command.startsWith(Config.COMMAND_PREFIX)) {
                        switch (StringUtils.leftTrim(command.toLowerCase(Locale.ROOT), Config.COMMAND_PREFIX)) {
                            case Command.LEAVE_ROOM:
                                isConnected = false;
                                break;
                            case Command.LIST_USERS:
                                System.out.printf(Dialog.LIST_USERS, payload);
                                break;
                            case Command.HELP:
                                showHelp(true);
                                break;
                            default:
                                System.out.println(Dialog.INVALID_COMMAND);
                                break;
                        }
                    } else {
                        final byte[] buffer = String.format(Dialog.MESSAGE, username, message).getBytes();
                        final DatagramPacket messageOut = new DatagramPacket(buffer, buffer.length, roomAddress, Config.MULTICAST_PORT);
                        multicastSocket.send(messageOut);
                    }
                }
            }
        } catch (ConnectException exception) {
            System.err.print(Dialog.CONNECTION_REFUSED);
            System.exit(1);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
