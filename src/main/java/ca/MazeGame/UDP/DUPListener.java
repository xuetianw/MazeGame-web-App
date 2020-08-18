package ca.MazeGame.UDP;// Java program to illustrate Server side
// Implementation using DatagramSocket
import ca.MazeGame.Wrappers.MoveUtility;
import ca.MazeGame.model.Direction;
import ca.MazeGame.MazeGames.MultiPlayerMazeGame;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class DUPListener extends MoveUtility implements Runnable
{
    public static final int PORT = 1234;
    private static final String COMMAND_LEFT = "left\n";
    private static final String COMMAND_RIGHT = "right\n";
    private static final String COMMAND_UP = "up\n";
    private static final String COMMAND_DOWN = "down\n";

    public MultiPlayerMazeGame game;

    public DUPListener() {
    }

    public MultiPlayerMazeGame getGame() {
        return game;
    }

    public void setGame(MultiPlayerMazeGame game) {
        this.game = game;
    }

    DUPListener(MultiPlayerMazeGame game) {
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

            processIncomingCommand(data(receive).toString());

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

    void processIncomingCommand(String command) {
        String instruction = null;
        switch (command) {
            case COMMAND_LEFT:
                instruction = MoveUtility.COMMAND_LEFT;
                System.out.println("user move left");
                break;
            case COMMAND_RIGHT:
                instruction = MoveUtility.COMMAND_RIGHT;
                break;
            case COMMAND_UP:
                instruction = MoveUtility.COMMAND_UP;
                System.out.println("user move up");
                break;
            case COMMAND_DOWN:
                instruction = MoveUtility.COMMAND_DOWN;
                System.out.println("user move down");
                break;
            default:
                System.out.printf("cannot find the command %s \n", command);
                break;
        }

        if (instruction != null) {
            doPlayerMove(instruction);
        }
    }

    public void doPlayerMove(String arrow) {
        Direction move = getPlayerMove(arrow);
        if (!game.isValidPlayerMove(move)) {
            System.out.println("new location on the wall");
        } else {
            game.recordSecondPlayerMove(move);
            if (!gameNotWonOrLost()) {
//                System.out.println("Cats won!");
                doWonOrLost();
            }
        }
    }

    public boolean gameNotWonOrLost() {
        return !game.hasAnyUserWon() && !game.hasAnyUserLost();
    }

    public void doWonOrLost() {
        if (game.hasAnyUserWon()) {
            revealBoard();
        } else if (game.hasAnyUserLost()) {
            revealBoard();
        } else {
            assert false;
        }
    }
    public void revealBoard() {
        game.displayBoard();
    }
}
