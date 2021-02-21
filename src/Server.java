import constants.Command;
import constants.Config;
import constants.Dialog;
import lib.Channel;
import lib.MulticastIpGenerator;
import models.Room;
import utils.StringUtils;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.util.*;

public final class Server {
    private Server() {
    }

    public static void main(String[] args) {
        final MulticastIpGenerator multicastIpGenerator = new MulticastIpGenerator();
        final Map<String, Room> rooms = new LinkedHashMap<>();
        final Set<String> users = new LinkedHashSet<>();

        new Thread(() -> {
            final Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.print(Dialog.SERVER_PS1);
                final String message = scanner.nextLine().trim().toLowerCase(Locale.ROOT);
                final String command = message.split("\\s", 2)[0];
                final String commandValue = StringUtils.leftTrim(command.toLowerCase(Locale.ROOT), Config.COMMAND_PREFIX);

                if (commandValue.equals(Command.EXIT)) {
                    System.exit(0);
                }
            }
        }).start();

        try {
            final ServerSocket listenSocket = new ServerSocket(Config.SERVER_PORT);
            System.out.printf(Dialog.SERVER_LISTENING, Config.SERVER_PORT);

            while (true) {
                new Channel(multicastIpGenerator, rooms, users, listenSocket.accept());
            }
        } catch (BindException exception) {
            System.err.printf(Dialog.SERVER_PORT_ERROR, Config.SERVER_PORT);
            System.exit(1);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
