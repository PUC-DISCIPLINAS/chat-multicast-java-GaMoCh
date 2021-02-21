package constants;

// The commands must be in lower case
public final class Command {
    public static final String CREATE_ROOM = "create";
    public static final String LIST_ROOMS = "list";

    public static final String JOIN_ROOM = "join";
    public static final String LEAVE_ROOM = "leave";

    public static final String LIST_USERS = "users";

    public static final String EXIT = "exit";
    public static final String HELP = "help";

    private Command() {
    }
}
