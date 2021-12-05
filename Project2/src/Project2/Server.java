package Project2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;
    private int numPlayers;
    private ClientHandler player1;
    private ClientHandler player2;

    public Server() {
        System.out.println("---Server---");
        numPlayers = 0;
        try {
            serverSocket = new ServerSocket(4999);
        } catch (IOException e) {
            System.out.println("IOException from Server() constructor");
            e.printStackTrace();
        }
    }

    public void acceptConnections() {
        try {
            System.out.println("Waiting for connections...");
            while(numPlayers < 2) {
                Socket socket = serverSocket.accept();
                numPlayers++;
                System.out.println("Player #" + numPlayers + " has connected");
                ClientHandler clientHandler = new ClientHandler(socket, numPlayers);

                if(numPlayers == 1) {
                    player1 = clientHandler;
                }
                else if(numPlayers == 2) {
                    player2 = clientHandler;
                }
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
            System.out.println("Max player connections reached");
        } catch (Exception e) {
            System.out.println("Exception from acceptConnections()");
            e.printStackTrace();
        }
    }

    private class ClientHandler implements Runnable {
        private Socket socket;
        private String string;
        private String[] token;
        private DataInputStream dataInputStream;
        private DataOutputStream dataOutputStream;
        private int playerID;

        public ClientHandler(Socket socket, int id) {
            this.socket = socket;
            playerID = id;
            try {
                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                System.out.println("IOException from ClientHandler constructor");
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                dataOutputStream.writeInt(playerID);
                dataOutputStream.flush();

                while(true) {
                    string = dataInputStream.readUTF();
                    System.out.println("Reading from Client: " + string);
                    token = string.split(";");

                    // Need to check action conditions here
                    switch (token[0]) {
                        case "WA" -> {
                            System.out.println("Writing Ack: WA");
                            dataOutputStream.writeUTF("A");
                        }
                        case "WD" -> {
                            System.out.println("Writing Ack: WD");
                            dataOutputStream.writeUTF("A");
                        }
                        case "SA" -> {
                            System.out.println("Writing Ack: SA");
                            dataOutputStream.writeUTF("A");
                        }
                        case "SD" -> {
                            System.out.println("Writing Ack: SD");
                            dataOutputStream.writeUTF("A");
                        }
                        case "W" -> {
                            System.out.println("Writing Ack: W");
                            dataOutputStream.writeUTF("A");
                        }
                        case "A" -> {
                            System.out.println("Writing Ack: A");
                            dataOutputStream.writeUTF("A");
                        }
                        case "S" -> {
                            System.out.println("Writing Ack: S");
                            dataOutputStream.writeUTF("A");
                        }
                        case "D" -> {
                            System.out.println("Writing Ack: D");
                            dataOutputStream.writeUTF("A");
                        }
                        default -> {
                            System.out.println("Writing Err");
                            dataOutputStream.writeUTF("E");
                        }
                    }
//                    if(token[0] != null) {
//                        System.out.println("Writing to Client: A");
//                        dataOutputStream.writeUTF("A");
//                    }

                }
            } catch (IOException e) {
                System.out.println("IOException from run() in ClientHandler");
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.acceptConnections();
    }
}