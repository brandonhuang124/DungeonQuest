package Project2;

import java.io.*;
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

                System.out.println("Starting ClientHandler Thread for Player #" + numPlayers);
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
        private BufferedReader bufferedReader;
        private int playerID;

        public ClientHandler(Socket socket, int id) {
            this.socket = socket;
            playerID = id;
            try {
                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
//                bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e) {
                System.out.println("IOException from ClientHandler constructor");
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
//                System.out.println("Check 1");
                dataOutputStream.writeInt(playerID);
                dataOutputStream.flush();
//                System.out.println("Check 2");
                while(true) {
//                    System.out.println("Check 3");
                    string = dataInputStream.readUTF();
//                    string = bufferedReader.readLine();
//                    System.out.println("Check 4");
                    System.out.println("Reading from Client: " + string);
                    token = string.split(";");

                    // Need to check action conditions here
                    switch (token[0]) {
                        case "WA" -> {
                            System.out.println("Writing Ack: WA");
                            dataOutputStream.writeUTF("A");
                            dataOutputStream.flush();
                        }
                        case "WD" -> {
                            System.out.println("Writing Ack: WD");
                            dataOutputStream.writeUTF("A");
                            dataOutputStream.flush();
                        }
                        case "SA" -> {
                            System.out.println("Writing Ack: SA");
                            dataOutputStream.writeUTF("A");
                            dataOutputStream.flush();
                        }
                        case "SD" -> {
                            System.out.println("Writing Ack: SD");
                            dataOutputStream.writeUTF("A");
                            dataOutputStream.flush();
                        }
                        case "W" -> {
                            System.out.println("Writing Ack: W");
                            dataOutputStream.writeUTF("A");
                            dataOutputStream.flush();
                        }
                        case "A" -> {
                            System.out.println("Writing Ack: A");
                            dataOutputStream.writeUTF("A");
                            dataOutputStream.flush();
                        }
                        case "S" -> {
                            System.out.println("Writing Ack: S");
                            dataOutputStream.writeUTF("A");
                            dataOutputStream.flush();
                        }
                        case "D" -> {
                            System.out.println("Writing Ack: D");
                            dataOutputStream.writeUTF("A");
                            dataOutputStream.flush();
                        }
                        default -> {
                            System.out.println("Writing Err");
                            dataOutputStream.writeUTF("E");
                            dataOutputStream.flush();
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