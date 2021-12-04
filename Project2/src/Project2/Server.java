package Project2;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class Server {
    private ServerSocket server;

    public Server(int port) throws IOException {
    }

    // ClientHandler thread to handle all incoming client connections
    private static class ClientHandler extends Thread {
        private Socket socket;
        ArrayList<ClientHandler> threadList;
        private PrintWriter printWriter;
        private String[] token;

        public ClientHandler(Socket socket, ArrayList<ClientHandler> threads) throws IOException {
            this.socket = socket;
            this.threadList = threads;
        }

        @Override
        public void run() {
            try {
                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                printWriter = new PrintWriter(socket.getOutputStream(), true);

                while(true) {
                    // Check if action conflicts with other client
                    String outputString = input.readLine();
                    token = outputString.split(";");
                    System.out.println(outputString);
                    System.out.println("token[0] = " + token[0]);
                    if(token[0].equals("W")) {
                        printWriter.println("A");
                        System.out.println("Writing Ack");
                    }
                }
            } catch (Exception e) {
                System.out.println("Failed to add client");
                e.printStackTrace();
            }
        }
    }

//    private static class ServerUpdate extends Thread {
//        private Socket socket;
//        private BufferedReader input;
//        private PrintWriter printWriter;
//
//        public ServerUpdate(Server server) throws IOException {
//            this.socket = socket;
//            printWriter = new PrintWriter(socket.getOutputStream(), true);
//            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//        }
//
//        @Override
//        public void run() {
//            while(true) {
//                // Read data from client (Might make these their own functions)
//                readData();
//
//                // Write data to client
//                writeData();
//            }
//        }
//
//        public void readData() {
//            try {
//                // Read data from each client
////                for(int i = 0; i < serv.numClient; i++) {
////                    // Maybe send a formatted string that we can parse?
////
////                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        public void writeData() {
//
//        }
//    }

    public static void main(String[] args) throws IOException {
        ArrayList<ClientHandler> threadList = new ArrayList<>();
        try (ServerSocket serverSocket = new ServerSocket(4999)) {
            while(true) {
                Socket socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(socket, threadList);

                threadList.add(clientHandler);
                clientHandler.start();
            }
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }

    }
}