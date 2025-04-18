import java.io.*;
import java.net.*;
import java.util.Timer;
import java.util.TimerTask;

public class GBNClient {
    private static final int WINDOW_SIZE = 4;
    private static final int TIMEOUT = 3000; // 3 seconds

    private static int base = 0, nextSeqNum = 0;
    private static Socket socket;
    private static DataOutputStream outToServer;
    private static DataInputStream inFromServer;
    private static Timer timer;

    public static void main(String[] args) {
        try {
            socket = new Socket("localhost", 6789);
            System.out.println("Connected to Server!");

            outToServer = new DataOutputStream(socket.getOutputStream());
            inFromServer = new DataInputStream(socket.getInputStream());

            // Thread to send packets indefinitely
            Thread senderThread = new Thread(() -> {
                while (true) {
                    synchronized (GBNClient.class) {
                        while (nextSeqNum < base + WINDOW_SIZE) {
                            sendPacket(nextSeqNum);
                            nextSeqNum++;
                        }
                    }
                    try {
                        Thread.sleep(500); // Simulate network delay
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });

            // Thread to receive ACKs
            Thread ackReceiverThread = new Thread(() -> {
                while (true) {
                    receiveACK();
                }
            });

            senderThread.start();
            ackReceiverThread.start();

            senderThread.join();
            ackReceiverThread.join();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void sendPacket(int seqNum) {
        try {
            System.out.println("Sending packet: " + seqNum);
            outToServer.writeInt(seqNum);
            outToServer.flush();

            if (base == seqNum) {
                startTimer();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void receiveACK() {
        try {
            int ackNum = inFromServer.readInt();
            synchronized (GBNClient.class) {
                System.out.println("Received ACK: " + ackNum);

                if (ackNum >= base) {
                    base = ackNum + 1; // Slide window
                    if (base == nextSeqNum) {
                        stopTimer();
                    } else {
                        startTimer();
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Timeout! Retransmitting from " + base);
            nextSeqNum = base;
            for (int i = base; i < base + WINDOW_SIZE; i++) {
                sendPacket(i);
            }
        }
    }

    private static void startTimer() {
        stopTimer();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("Timeout! Retransmitting from " + base);
                nextSeqNum = base;
                for (int i = base; i < base + WINDOW_SIZE; i++) {
                    sendPacket(i);
                }
            }
        }, TIMEOUT);
    }

    private static void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
    }
}
