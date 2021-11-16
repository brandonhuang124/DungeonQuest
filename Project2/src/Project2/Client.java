package Project2;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client extends Thread{
    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    public Scanner scanner = new Scanner(System.in);
    String userInput;

    public Client(String address, int port) throws IOException {
        try {
            socket = new Socket(address, port);
            if (socket == null) {
                System.out.println("Failed to connect to " + address + ":" + port);
            } else {
                System.out.println("Connection to " + address + " established");
            }

            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());

            ClientThread clientThread = new ClientThread(socket);
            clientThread.start();

//            do {
//                if(userInput.equals("exit")) {
//                    break;
//                }
//            } while(!userInput.equals("exit"));

        } catch (IOException e) {
            dataInputStream.close();
            dataOutputStream.close();
            socket.close();
            e.printStackTrace();
        }
    }

    private static class ClientThread extends Thread implements Runnable {
        private final BufferedReader input;

        public ClientThread(Socket socket) throws IOException {
            this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }

        @Override
        public void run() {
//            try {
//                while(true) {
//                    String response = input.readLine();
//                    System.out.println(response);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            try {
//                input.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
    }

    public static void main(String[] args) throws IOException {
        Client client = new Client("localhost", 4999);
    }
}
