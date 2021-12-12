package Project2;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;
    private int numPlayers, threadPlayer;
    private volatile ClientHandler player1;
    private volatile ClientHandler player2;
    volatile boolean  hasPlayer1, hasPlayer2;

    public Server() {
        player1 = player2 = null;
        System.out.println("---Server---");
        hasPlayer1 = hasPlayer2 = false;
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
            while(!hasPlayer1 || !hasPlayer2) {
                Socket socket = serverSocket.accept();
                System.out.println("Player #" + 1 + " has connected");
                ClientHandler clientHandler = new ClientHandler(socket, 1);
                setPlayers(clientHandler,1);

                System.out.println("Starting ClientHandler Thread for Player #" + 1);
                Thread thread = new Thread(clientHandler);
                thread.start();

                Socket socket2 = serverSocket.accept();
                System.out.println("Player #" + 2 + " has connected");
                ClientHandler clientHandler2 = new ClientHandler(socket2, 2);
                setPlayers(clientHandler2,2);
                clientHandler.partner = clientHandler2;
                clientHandler2.partner = clientHandler;

                System.out.println("Starting ClientHandler Thread for Player #" + 2);
                Thread thread2 = new Thread(clientHandler2);
                thread2.start();

            }
            System.out.println("Max player connections reached");
        } catch (Exception e) {
            System.out.println("Exception from acceptConnections()");
            e.printStackTrace();
        }
        // When both players are connected, we wait
      while(true);
    }

    public void setPlayers(ClientHandler newPlayer, int id) {
      if(id == 1) {
        if(player1 == null)
          player1 = newPlayer;
        else
          player2 = newPlayer;
      }
      if(id == 2) {
        if(player2 == null)
          player2 = newPlayer;
        else
          player1 = newPlayer;
      }
    }

    public void swapPlayer() {
      ClientHandler temp = player1;
      player1 = player2;
      player2 = temp;
    }

    private class ClientHandler implements Runnable {
        private Socket socket;
        private String string;
        private String[] token;
        private DataInputStream dataInputStream;
        private DataOutputStream dataOutputStream;
        private int playerID, phase;
        private boolean gameStart;
        public volatile ClientHandler partner;

        public ClientHandler(Socket socket, int id) {
            partner = null;
            gameStart = false;
            this.socket = socket;
            playerID = id;
            phase = 1;
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
                // Phase 1, we need to get the identity of the the player conected, and check if their slot it full
                if(phase == 1) {
                  System.out.println("Phase 1, Get Identity" + playerID);
                  string = dataInputStream.readUTF();
                  System.out.println("Reading from Client: " + string);
                  token = string.split(";");
                  if(token[0].equals("Player1")) {
                    // Were the "smart" client.
                    // Check if we already have a player 1:
                    if(hasPlayer1) {
                      System.out.println("We already have a player1");
                      dataOutputStream.writeUTF("Error;1");
                      dataOutputStream.flush();
                      dataOutputStream.close();
                      dataInputStream.close();
                      socket.close();
                      return;
                    }
                    if(playerID == 2){
                      // Were assigned to the wrong player
                      Server.this.swapPlayer();
                    }
                    playerID = 1;
                    hasPlayer1 = true;
                    phase = 2;
                  }
                  else if(token[0].equals("Player2")) {
                    // Were the "dumb Client"
                    // Check if we already have a player 2:
                    if(hasPlayer2) {
                      System.out.println("We already have a player1");
                      dataOutputStream.writeUTF("Error;1");
                      dataOutputStream.flush();
                      dataOutputStream.close();
                      dataInputStream.close();
                      socket.close();
                      return;
                    }
                    if(playerID == 1){
                      // Were assigned to the wrong player
                      Server.this.swapPlayer();
                    }
                    playerID = 2;
                    hasPlayer2 = true;
                    phase = 2;
                  }
                }
                // Phase 2: We're good to go and connected, but we need to wait for the second player
                if(phase == 2) {
                  if(player1 == player2)
                    System.out.println("Players are the same uh oh");
                  System.out.println("Phase 2: Wait for other player" + playerID);
                  // Keep sending wait messages until both players are connected
                  while(!hasPlayer1 || !hasPlayer2) {
                    dataOutputStream.writeUTF("WAIT;Waiting for other player...;");
                    dataOutputStream.flush();
                    dataInputStream.readUTF();
                  }
                  // We break out once both players are connected and we move to Phase 3,
                  phase = 3;
                }
                // Phase 3: Both players are connected, and we need to wait for the signal from player 1.
                if(phase == 3) {
                  System.out.println("Phase 3: Wait for signal from P1" + playerID);
                  // If were player 1 we need to quickly tell the client we found another player and we're waiting for the signal
                  if(playerID == 1) {
                    dataOutputStream.writeUTF("FOUND;Player2Connected;");
                    dataOutputStream.flush();
                  }
                  while(!gameStart) {
                    // If were handling player 1, We need to read for the signal
                    if(playerID == 1) {
                      string = dataInputStream.readUTF();
                      System.out.println("Reading from Client: " + string);
                      token = string.split(";");
                      System.out.println(token[0]);
                      if(token[0].equals("START")) {
                        System.out.println("Read a Start");
                        partner.gameStart = true;
                        gameStart = true;
                        dataOutputStream.writeUTF("START;Acknowledged gameStart;");
                        dataOutputStream.flush();
                        phase = 4;
                      }
                    }
                    // If were handling player 2, We need to wait for the signal.
                    else if(playerID == 2) {
                      dataOutputStream.writeUTF("WAIT;Waiting for P1 to start;");
                      dataOutputStream.flush();
                      dataInputStream.readUTF();
                    }
                  }
                  // Once we break the loop when p1 gives the signal, p2 needs to tell the client we're starting
                  if(playerID == 2) {
                    dataOutputStream.writeUTF("START;Player1 Started Game;");
                    dataOutputStream.flush();
                    phase = 4;
                  }

                }

                // Phase 4, The game has started and were just exchanging data regularly now.
                if(phase == 4) {
                  System.out.println("Entered Phase 4: " + playerID);
                  while(true) {
                    string = dataInputStream.readUTF();
                    System.out.println("Reading from Client: " + string);
                    token = string.split(";");
                    if(playerID == 1) {
                      // Were the thread handling player 1
                      partner.dataOutputStream.writeUTF(string);
                      partner.dataOutputStream.flush();
                    }
                    else if(playerID == 2) {
                      // Were the thread handling player 2
                      partner.dataOutputStream.writeUTF(string);
                      partner.dataOutputStream.flush();
                    }
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