package Project2;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Client {
    private Socket socket;
    public static String inputString;
    public Thread clientThread;
    public int num = 0;

    public Client(String address, int port) throws IOException {
        try {
            socket = new Socket(address, port);
            if (socket == null) {
                System.out.println("Failed to connect to " + address + ":" + port);
            } else {
                System.out.println("Connection to " + address + " established");
            }

            clientThread = new Thread(new ClientThread(socket), "Client" + num++);
            clientThread.start();
        } catch (IOException e) {
            socket.close();
            e.printStackTrace();
        }
    }

    private static class ClientThread implements Runnable {
        //        private Socket socket;
        public BufferedReader input;
        public PrintWriter printWriter;
        public String[] token;

        public ClientThread(Socket socket) throws IOException {
//            this.socket = socket;
            printWriter = new PrintWriter(socket.getOutputStream(), true);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }

        @Override
        public void run() {
            // Read data from server
            try {
//                Loop through formatted string, send commands to server
//                So far, only 2 arguments in string, "Movement;playerLoc"
//                Wait for an Ack/Err from server
                do {
                    if(Client.inputString == null) {
                        continue;
                    }

                    token = Client.inputString.split(";");
                    if(token[0].equals("WA")) {

                    }
                    else if(token[0].equals("WD")) {

                    }
                    else if(token[0].equals("SA")) {

                    }
                    else if(token[0].equals("SD")) {

                    }
                    else if(token[0].equals("W")) {
                        printWriter.println(Client.inputString);
                        if(input.readLine().equals("A")) {
                            System.out.println("Command W - Received Ack");
                        }
                        else if(input.readLine().equals("E")){
                            System.out.println("Command W - Received Err");
                        }
                        else {
                            System.out.println("Error");
                        }
                    }
                    else if(token[0].equals("A")) {

                    }
                    else if(token[0].equals("S")) {

                    }
                    else if(token[0].equals("D")) {

                    }
                    if(token[0].equals("exit")) {
                        break;
                    }
                } while(true);

//                while(true) {
//                    System.out.println("inputString: " + Client.inputString);
//                    printWriter.println(Client.inputString);
//
//                    String response = input.readLine();
//                    System.out.print("Server: " + response);
//                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
