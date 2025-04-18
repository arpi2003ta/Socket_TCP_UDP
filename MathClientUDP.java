import java.net.*;
import java.io.*;
import java.util.Scanner;

class MathClientUDP {
    public static void main(String[] args) throws Exception {
        DatagramSocket clientSocket = new DatagramSocket();
        InetAddress serverAddress = InetAddress.getByName("localhost");
        Scanner scanner = new Scanner(System.in);

        byte[] sendBuffer;
        byte[] receiveBuffer = new byte[1024];

        while (true) {
            // Display menu
            System.out.println("Select 1 to add, 2 to subtract, 3 to multiply, 4 to exit.");
            System.out.print("Enter choice: ");
            String choice = scanner.nextLine();

            // Send choice to server
            sendBuffer = choice.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, serverAddress, 6789);
            clientSocket.send(sendPacket);

            // Exit condition
            if (choice.equals("4")) {
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                clientSocket.receive(receivePacket);
                String goodbyeMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println("Server: " + goodbyeMessage);
                break;
            }

            // Receive prompt for operands
            DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
            clientSocket.receive(receivePacket);
            String prompt = new String(receivePacket.getData(), 0, receivePacket.getLength());
            System.out.println("Server: " + prompt);

            // Get operands from user
            System.out.print("Enter operands (separated by space): ");
            String operands = scanner.nextLine();

            // Send operands to server
            sendBuffer = operands.getBytes();
            sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, serverAddress, 6789);
            clientSocket.send(sendPacket);

            // Receive result from server
            receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
            clientSocket.receive(receivePacket);
            String result = new String(receivePacket.getData(), 0, receivePacket.getLength());
            System.out.println("Server: " + result);
        }

        clientSocket.close();
        scanner.close();
    }
}
