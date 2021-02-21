package constants;

public final class Dialog {
    public static final String SERVER_LISTENING = "Server listening on TCP/%d\n";
    public static final String SERVER_PORT_ERROR = "Port %d already in use\n";

    public static final String CONNECTION_REFUSED = "Connection refused\n";

    public static final String USERNAME_INPUT = "Insert your name: ";
    public static final String EMPTY_USERNAME = "\nEmpty username\n\n";
    public static final String USERNAME_EXISTS = "\nUsername already exists\n\n";

    public static final String INVALID_COMMAND = "\nInvalid command\n";

    public static final String CREATE_ROOM = "\nRoom '%s' created\n";
    public static final String CREATE_ROOM_ERROR = "\nRoom '%s' already exists\n";
    public static final String CREATE_ROOM_ARGUMENT = "\nRoom name not provided\n";

    public static final String JOIN_ROOM = String.format(
        "\nWelcome to room '%%s'\nType %s%s to help\n\n", Config.COMMAND_PREFIX, Command.HELP);
    public static final String JOIN_ROOM_ERROR = "\nRoom not exists\n";
    public static final String JOIN_ROOM_ARGUMENT = "\nRoom name not provided\n";

    public static final String LIST_ROOMS = "\nRooms: %s\n";

    public static final String LIST_USERS = "\nUsers: %s\n\n";

    public static final String EXIT = "\nDisconnecting from the server\n";

    public static final String HELP = String.format("Type %s%%s to %%s\n", Config.COMMAND_PREFIX);

    public static final String MESSAGE = String.format("%%s%s%%s\n", Config.MESSAGE_SEPARATOR);

    public static final String CLIENT_PS1 = String.format(
        "\nType %s%s to help \u276F ", Config.COMMAND_PREFIX, Command.HELP);
    public static final String SERVER_PS1 = String.format(
        "\nType %s%s to exit \u276F ", Config.COMMAND_PREFIX, Command.EXIT);

    private Dialog() {
    }
}
