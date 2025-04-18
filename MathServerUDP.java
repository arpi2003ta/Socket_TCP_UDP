import java.net.*;
import java.io.*;

class MathServerUDP {
    public static void main(String[] args) throws Exception {
        DatagramSocket serverSocket = new DatagramSocket(6789);
        System.out.println("UDP Math Server is running on port 6789...");

        byte[] receiveBuffer = new byte[1024];
        byte[] sendBuffer;

        while (true) {
            DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
            serverSocket.receive(receivePacket); // Receive data from client
            InetAddress clientAddress = receivePacket.getAddress();
            int clientPort = receivePacket.getPort();

            String clientMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
            System.out.println("Received from client: " + clientMessage);

            // If the client sends "4", exit the session
            if (clientMessage.equals("4")) {
                sendBuffer = "Goodbye!".getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, clientAddress, clientPort);
                serverSocket.send(sendPacket);
                System.out.println("Client exited.");
                continue;
            }

            // Validate choice
            int choice;
            try {
                choice = Integer.parseInt(clientMessage);
                if (choice < 1 || choice > 3) {
                    sendBuffer = "Invalid choice. Try again.".getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, clientAddress, clientPort);
                    serverSocket.send(sendPacket);
                    continue;
                }
            } catch (NumberFormatException e) {
                sendBuffer = "Invalid input. Enter a number.".getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, clientAddress, clientPort);
                serverSocket.send(sendPacket);
                continue;
            }

            // Request operands from the client
            sendBuffer = "Enter operands (separated by space):".getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, clientAddress, clientPort);
            serverSocket.send(sendPacket);

            // Receive operands from client
            receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
            serverSocket.receive(receivePacket);
            String operandStr = new String(receivePacket.getData(), 0, receivePacket.getLength());
            String[] operands = operandStr.split(" ");

            if (operands.length < 2) {
                sendBuffer = "Error: Enter at least two numbers.".getBytes();
                sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, clientAddress, clientPort);
                serverSocket.send(sendPacket);
                continue;
            }

            double result;
            try {
                result = Double.parseDouble(operands[0]);

                // Perform operation
                switch (choice) {
                    case 1: // Addition
                        for (int i = 1; i < operands.length; i++) {
                            result += Double.parseDouble(operands[i]);
                        }
                        break;
                    case 2: // Subtraction
                        for (int i = 1; i < operands.length; i++) {
                            result -= Double.parseDouble(operands[i]);
                        }
                        break;
                    case 3: // Multiplication
                        for (int i = 1; i < operands.length; i++) {
                            result *= Double.parseDouble(operands[i]);
                        }
                        break;
                }
            } catch (NumberFormatException e) {
                sendBuffer = "Error: Invalid number format.".getBytes();
                sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, clientAddress, clientPort);
                serverSocket.send(sendPacket);
                continue;
            }

            // Send result to client
            String resultMessage = "Result: " + result;
            sendBuffer = resultMessage.getBytes();
            sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, clientAddress, clientPort);
            serverSocket.send(sendPacket);
            System.out.println("Sent result: " + result);
        }
    }
}
