package constants;

public enum Helper {
    CREATE_ROOM(Command.CREATE_ROOM, false, "create room"),
    LIST_ROOMS(Command.LIST_ROOMS, false, "list rooms"),
    JOIN_ROOM(Command.JOIN_ROOM, false, "join room"),
    LEAVE_ROOM(Command.LEAVE_ROOM, true, "leave room"),
    LIST_USERS(Command.LIST_USERS, true, "list room users"),
    EXIT(Command.EXIT, false, "exit chat");

    private final String method;
    private final String description;
    private final boolean whenConnected;

    Helper(String method, boolean whenConnected, String description) {
        this.method = method;
        this.description = description;
        this.whenConnected = whenConnected;
    }

    public String getMethod() {
        return method;
    }

    public String getDescription() {
        return description;
    }

    public boolean isWhenConnected() {
        return whenConnected;
    }

    @Override
    public String toString() {
        return this.method;
    }
}
