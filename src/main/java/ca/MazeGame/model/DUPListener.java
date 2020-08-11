package ca.MazeGame.model;// Java program to illustrate Server side
// Implementation using DatagramSocket
import ca.MazeGame.Wrappers.ApiGameWrapper;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class DUPListener implements Runnable
{
    public static final int PORT = 1234;
    private static final String COMMAND_LEFT = "left\n";
    private static final String COMMAND_RIGHT = "right\n";
    private static final String COMMAND_UP = "up\n";
    private static final String COMMAND_DOWN = "down\n";

    public DUPListener() {
    }

    public MazeGame getGame() {
        return game;
    }

    public void setGame(MazeGame game) {
        this.game = game;
    }

    public MazeGame game;

    DUPListener(MazeGame game) {
        this.game = game;
    }

//    doPlayerMove

    // A utility method to convert the byte array
    // data into a string representation.
    public static StringBuilder data(byte[] a)
    {
        if (a == null)
            return null;
        StringBuilder ret = new StringBuilder();
        int i = 0;
        while (a[i] != 0)
        {
            ret.append((char) a[i]);
            i++;
        }
        return ret;
    }
    @Override
    public void run() {
        // Step 1 : Create a socket to listen at port 1234
        DatagramSocket ds = null;
        try {
            ds = new DatagramSocket(PORT);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        byte[] receive = new byte[65535];

        DatagramPacket DpReceive = null;
        System.out.printf("netcat -u 127.0.0.1 %d\n", PORT);
        while (true)
        {

            // Step 2 : create a DatgramPacket to receive the data.
            DpReceive = new DatagramPacket(receive, receive.length);
            // Step 3 : revieve the data in byte buffer.
            try {
                ds.receive(DpReceive);
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("Client :" + data(receive));

            processCommand(data(receive).toString());

            // Exit the server if the client sends "bye"
            if (data(receive).toString().equals("bye"))
            {
                System.out.println("Client sent bye.....EXITING");
                break;
            }

            // Clear the buffer after every message.
            receive = new byte[65535];
        }
    }

    void processCommand(String commnad) {
        String instruction = null;
        if (commnad.equals(COMMAND_LEFT)) {
            instruction = ApiGameWrapper.COMMAND_LEFT;
            System.out.println("user move left");
        } else if (commnad.equals(COMMAND_RIGHT)) {
            instruction = ApiGameWrapper.COMMAND_RIGHT;
            System.out.println("user move right");
        } else if (commnad.equals(COMMAND_UP)) {
            instruction = ApiGameWrapper.COMMAND_UP;
            System.out.println("user move up");
        } else if (commnad.equals(COMMAND_DOWN)) {
            instruction = ApiGameWrapper.COMMAND_DOWN;
            System.out.println("user move down");
        } else {
            System.out.printf("cannot find the command %s \n", commnad);
        }

        if (instruction != null) {
            doPlayerMove(instruction);
        }
    }

    public void doPlayerMove(String arrow) {
        Direction move = ApiGameWrapper.getPlayerMove(arrow);
        if (!game.isValidPlayerMove(move)) {
            System.out.println("new location on the wall");
        } else {
            game.recordPlayerMove(move);
            if (!gameNotWonOrLost()) {
//                System.out.println("Cats won!");
                doWonOrLost();
            }
        }
    }

    public boolean gameNotWonOrLost() {
        return !game.hasUserWon() && !game.hasUserLost();
    }

    public void doWonOrLost() {
        if (game.hasUserWon()) {
            revealBoard();
        } else if (game.hasUserLost()) {
            revealBoard();
        } else {
            assert false;
        }
    }
    public void revealBoard() {
        game.displayBoard();
    }
}
