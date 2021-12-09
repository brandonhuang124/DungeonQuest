package Project2;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;
    private int numPlayers, threadPlayer;
    private ClientHandler player1;
    private ClientHandler player2;

    public Server() {
        System.out.println("---Server---");
        numPlayers = threadPlayer = 0;
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
                dataOutputStream.writeInt(playerID);
                dataOutputStream.flush();
                while(true) {
                    string = dataInputStream.readUTF();
//                    string = bufferedReader.readLine();
//                    System.out.println("Check 4");
                    System.out.println("Reading from Client: " + string);
                    token = string.split(";");
                    // Here we check and assign which player this thread is handling
                    if(threadPlayer == 0) {
                      if(token[0] == "P1CLIENT")
                        threadPlayer = 1;
                      else if(token[0] == "P2CLIENT")
                        threadPlayer = 2;
                      else
                        threadPlayer = 0;
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