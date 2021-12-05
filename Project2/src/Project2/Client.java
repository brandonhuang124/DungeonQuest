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