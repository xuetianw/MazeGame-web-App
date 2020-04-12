package ca.MazeGame.model;// Java program to illustrate Server side
// Implementation using DatagramSocket
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class DUPListener implements Runnable
{
    public static final int PORT = 1234;
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
}
