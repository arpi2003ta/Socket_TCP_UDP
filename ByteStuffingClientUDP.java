import java.io.*;
import java.net.*;
import java.util.Scanner;

class ByteStuffingClientUDP {
    public static void main(String[] args) {
        try {
            DatagramSocket clientSocket = new DatagramSocket();
            InetAddress serverAddress = InetAddress.getByName("localhost");

            Scanner scanner = new Scanner(System.in);

            System.out.print("Enter a sequence of D (Data), E (Escape), F (Flag): ");
            String inputData = scanner.nextLine();

            // Perform byte stuffing
            String stuffedData = byteStuffing(inputData);
            System.out.println("Stuffed Data Sent: " + stuffedData);

            // Send stuffed data
            byte[] sendBuffer = stuffedData.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, serverAddress, 6789);
            clientSocket.send(sendPacket);


            scanner.close();
            
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String byteStuffing(String data) {
        StringBuilder stuffed = new StringBuilder();

        for (char c : data.toCharArray()) {
            if (c == 'F' || c == 'E') {
                stuffed.append("E"); // Add escape before special characters
            }
            stuffed.append(c); // Add actual character
        }

        return stuffed.toString();
    }
}