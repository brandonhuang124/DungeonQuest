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
//                    if(token[0].equals("W")) {
//                        System.out.println("Writing Ack");
//                        dataOutputStream.writeUTF("A");
//                    }
//                    else {
//                        System.out.println("Writing Err");
//                        dataOutputStream.writeUTF("E");
//                    }
                    if(token[0] != null) {
                        System.out.println("Writing to Client: A");
                        dataOutputStream.writeUTF("A");
                    }

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
//    private ServerSocket server;
//
//    public Server(int port) throws IOException {
//    }
//
//    // ClientHandler thread to handle all incoming client connections
//    private static class ClientHandler extends Thread {
//        private Socket socket;
//        ArrayList<ClientHandler> threadList;
//        private PrintWriter printWriter;
//        private String[] token;
//
//        public ClientHandler(Socket socket, ArrayList<ClientHandler> threads) throws IOException {
//            this.socket = socket;
//            this.threadList = threads;
//
//        }
//
//        @Override
//        public void run() {
//            try {
//                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//                printWriter = new PrintWriter(socket.getOutputStream(), true);
//
//                String line;
//                while((line = input.readLine()) != null) {
//                    // Check if action conflicts with other client
//                    System.out.println("Recieved: " + line);
//                    token = line.split(";");
//
//                    //Go through each command and check coordinates, compare to other clients coordinates
//                    if(token[0].equals("W")) {
//                        System.out.println("Token[0] = " + token[0]);
//                        printWriter.write("A");
//                    }
//                }
//                input.close();
//                printWriter.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
////    private static class ServerUpdate extends Thread {
////        private Socket socket;
////        private BufferedReader input;
////        private PrintWriter printWriter;
////
////        public ServerUpdate(Server server) throws IOException {
////            this.socket = socket;
////            printWriter = new PrintWriter(socket.getOutputStream(), true);
////            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
////        }
////
////        @Override
////        public void run() {
////            while(true) {
////                // Read data from client (Might make these their own functions)
////                readData();
////
////                // Write data to client
////                writeData();
////            }
////        }
////
////        public void readData() {
////            try {
////                // Read data from each client
//////                for(int i = 0; i < serv.numClient; i++) {
//////                    // Maybe send a formatted string that we can parse?
//////
//////                }
////
////            } catch (Exception e) {
////                e.printStackTrace();
////            }
////        }
////
////        public void writeData() {
////
////        }
////    }
//
//    public static void main(String[] args) throws IOException {
//        ArrayList<ClientHandler> threadList = new ArrayList<>();
//        try (ServerSocket serverSocket = new ServerSocket(4999)) {
//            while(true) {
//                Socket socket = serverSocket.accept();
//                System.out.println("Client connection established");
//                ClientHandler clientHandler = new ClientHandler(socket, threadList);
//
//                threadList.add(clientHandler);
//                clientHandler.start();
//                System.out.println("ClientHandler thread started");
//            }
//        } catch (Exception e) {
//            System.out.println(Arrays.toString(e.getStackTrace()));
//        }
//    }
//}