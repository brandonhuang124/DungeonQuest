package Project2;

import org.lwjgl.Sys;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Client {
    private Socket socket;
    public DataInputStream dataInputStream;
    public DataOutputStream dataOutputStream;
    private ClientThread clientThread;
    private int playerID;

    public String inputString;

    public Client(String address, int port) {
        System.out.println("---Client---");
        try {
            socket = new Socket(address, port);
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream((socket.getOutputStream()));
            playerID = dataInputStream.readInt();
            System.out.println("Connected to server as Player #" + playerID);

            clientThread = new ClientThread(socket, dataInputStream, dataOutputStream);
            clientThread.start();
        } catch (Exception e) {
            System.out.println("Exception in Client constructor");
            e.printStackTrace();
        }
    }

    private class ClientThread extends Thread {
        private Socket socket;
        private DataInputStream dataInputStream;
        private DataOutputStream dataOutputStream;

        public ClientThread(Socket socket, DataInputStream in, DataOutputStream out) {
            this.socket = socket;
            this.dataInputStream = in;
            this.dataOutputStream = out;
        }

        @Override
        public void run() {

        }
    }

    public void readData() {

    }

    public void writeData() {

    }
}
//    public Socket socket;
//    public static String inputString = "";
//    public static String responseString = "";
//    public Thread clientThread;
//    public static int num = 0;
//
//    public Client(String address, int port) throws IOException {
//        try {
//            socket = new Socket(address, port);
//            if (socket == null) {
//                System.out.println("Failed to connect to " + address + ":" + port);
//            } else {
//                System.out.println("Connection to " + address + " established");
//            }
//
//            clientThread = new Thread(new ClientThread(socket), "Client" + num++);
//            clientThread.start();
//        } catch (IOException e) {
//            socket.close();
//            e.printStackTrace();
//        }
//    }
//
//    private static class ClientThread extends Thread {
//        public Socket socket;
//        public BufferedReader input;
//
//        public ClientThread(Socket socket) throws IOException {
//            this.socket = socket;
//            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//        }
//
//        @Override
//        public void run() {
//            // Read data from server
//            System.out.println("running....");
//            try {
//                while(true) {
//                    System.out.println("Check 1");
//                    String response = input.readLine();
//                    System.out.println("response: " + response);
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            } finally {
//                try {
//                    input.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//    public static void main(String[] args) throws IOException {
//        try (Socket socket = new Socket("localhost", 4999)) {
//            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
//            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//
////            ClientThread clientThread = new ClientThread(socket);
////            clientThread.start();
//
//            printWriter.println(inputString);
//            System.out.println("inputString: " + inputString);
//
//            System.out.println("Buffered Reader: " + bufferedReader.readLine());
////            String userInput;
////            while((userInput = bufferedReader.readLine()) != null) {
////                System.out.println("bufferedReader: " + bufferedReader.readLine());
////            }
//            printWriter.close();
//            bufferedReader.close();
//        }
//    }
//}
