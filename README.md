# Multicast Chat

## Author

[Gabriel Moreira Chaves](https://github.com/GaMoCh)

## Requirements

- Java Development Kit 8 (JDK 8)

## Build

```sh
javac -d bin -sourcepath src src/*.java src/**/*.java
```

## Run server

```sh
java -cp bin Server
```

## Run client

```sh
java -cp bin Client
```

## Default commands

### Outside the room

- **`:create <room name>`:** Create room
- **`:list`:** List rooms
- **`:join <room name>`:** Join room
- **`:exit`:** Exit chat

### Inside the room

- **`:leave`:** Leave room
- **`:users`:** List room users

## How to use

1. Run the server
2. Run one or more clients

## Main structures

- **Server:** Class that run in server side.
- **Client:** Class that run in client side.
- **lib.Channel:** Thread from server to comunicate whit client.
- **constants.*:** All constants of project, as commands, ports, dialogs and command helper.
