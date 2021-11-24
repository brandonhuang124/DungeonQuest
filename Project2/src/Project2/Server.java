package Project2;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private Socket socket;
    private ServerSocket server;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    // Input and Output for clients

    public int numClient = 0;

    public Server(int port) throws IOException {
        // Start server and wait for a connection(s)
        try {
            server = new ServerSocket(port);
            System.out.println("Server Status: Up");
            System.out.println("Server Port: " + port);

            // Start thread to handle client connections
            ClientHandler clientHandler = new ClientHandler(this);
            clientHandler.start();

        } catch (IOException e) {
            System.out.println("Failed to start server");
            dataInputStream.close();
            dataOutputStream.close();
            socket.close();
            server.close();
            e.printStackTrace();
        }
    }

    private static class ClientHandler extends Thread implements Runnable {
        private Server serv;
        private Socket socket;
        private PrintWriter output;

        public ClientHandler(Server server) {
            this.serv = server;
        }

        @Override
        public void run() {
            try {
                while(true) {
                    // Accept client connections
                    socket = serv.server.accept();
                    if (socket == null) {
                        System.out.println("Client failed to connect");
                    } else {
                        serv.numClient++;
                        System.out.println("Client connection established");
                    }

                    serv.dataInputStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                    serv.dataOutputStream = new DataOutputStream(socket.getOutputStream());
                }
            } catch (Exception e) {
                System.out.println("Failed to add client");
                e.printStackTrace();
            }
        }
    }

    private static class update extends Thread {
        private Server serv;

        public update(Server server) {
            this.serv = server;
        }

        @Override
        public void run() {
            while(true) {

                // Read data from client (Might make these their own functions)
                readData();

                // Write data to client
                writeData();
            }
        }

        public void readData() {
            try {
                // Read data from each client
                for(int i = 0; i < serv.numClient; i++) {
                    // Maybe send a formatted string that we can parse?
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void writeData() {

        }
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server(4999);
    }
}