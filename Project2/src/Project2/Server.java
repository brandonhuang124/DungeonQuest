package Project2;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private Socket socket;
    private ServerSocket server;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    public Server(int port) throws IOException {
        server = new ServerSocket(port);
        if (server == null) {
            System.out.println("Failed to start server");
        } else {
            System.out.println("Server Status: Up");
            System.out.println("Server Port: " + port);
        }

        while(true) {
            try {
                socket = server.accept();
                if (socket == null) {
                    System.out.println("Client failed to connect");
                } else {
                    System.out.println("Client connection established");
                }

                dataInputStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                dataOutputStream = new DataOutputStream(socket.getOutputStream());

                ServerThread serverThread = new ServerThread(socket);
                serverThread.start();

            } catch (IOException e) {
                dataInputStream.close();
                dataOutputStream.close();
                socket.close();
                server.close();
                e.printStackTrace();
            }
        }
    }

    private static class ServerThread extends Thread implements Runnable{
        private Socket socket;
        private PrintWriter output;

        public ServerThread(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
//            try {
//                BufferedReader input = new BufferedReader(new InputStreamReader((socket.getInputStream())));
//                output = new PrintWriter(socket.getOutputStream());
//
//                while(true) {
//                    try {
//                        String outputString = input.readLine();
//                        if(outputString.equals("exit")) {
//                            break;
//                        }
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        }
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server(4999);
    }
}