import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ChatClient3 {
    private static volatile boolean writeFlag = true;
    private static volatile boolean readFlag = true;

    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 5000);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Connected to the server.");

            // Write Thread: Sends user input to the server
            Thread writeThread = new Thread(() -> {
                while (writeFlag) {
                    String message = scanner.nextLine();
                    out.println(message);
                    if (message.equalsIgnoreCase("bye")) {
                        writeFlag = false;
                        readFlag = false;  // Stop reading as well
                        try {
                            socket.close();  // Close the socket to exit properly
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;  // Exit the loop
                    }
                }
            });

            // Read Thread: Receives messages from the server
            Thread readThread = new Thread(() -> {
                try {
                    while (readFlag) {
                        String response = in.readLine();
                        if (response == null || response.equalsIgnoreCase("bye")) {
                            readFlag = false;
                            writeFlag = false;  // Stop writing as well
                            System.out.println("Server closed the connection.");
                            socket.close();  // Ensure socket is closed
                            break;
                        }
                        System.out.println("Server: " + response);
                    }
                } catch (IOException e) {
                    System.out.println("Connection closed.");
                }
            });

            writeThread.start();
            readThread.start();

            writeThread.join();
            readThread.join();

        } catch (IOException | InterruptedException e) {
            System.out.println("Disconnected from server.");
        }
    }
}
