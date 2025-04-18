import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

class MC2 { 
    public static void main(String argv[]) throws Exception { 
        DatagramSocket clientSocket = new DatagramSocket();
        InetAddress serverAddress = InetAddress.getByName("localhost");
        Scanner scanner = new Scanner(System.in);

        byte[] receiveBuffer = new byte[1024];
        byte[] sendBuffer;
        DatagramPacket sendPacket;
        DatagramPacket receivePacket; 

        // Send initial request to trigger server response
        sendBuffer = "Hello Server".getBytes();
        sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, serverAddress, 6789);
        clientSocket.send(sendPacket);

        while (true) { 
            // Read menu from server
            receiveBuffer = new byte[1024]; // Clear buffer
            receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
            clientSocket.receive(receivePacket);
            String menu = new String(receivePacket.getData(), 0, receivePacket.getLength());

            System.out.println(menu);

            // Get user choice
            System.out.print("Enter choice: ");
            String choice = scanner.nextLine();
            sendBuffer = choice.getBytes();
            sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, serverAddress, 6789);
            clientSocket.send(sendPacket);

            // Exit condition
            if (choice.equals("4")) {
                System.out.println("Exiting client...");
                break;
            }

            // Read server response for operands
            receiveBuffer = new byte[1024]; // Clear buffer
            receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
            clientSocket.receive(receivePacket);
            String prompt = new String(receivePacket.getData(), 0, receivePacket.getLength());
            System.out.println(prompt);

            // Get operands
            //System.out.print("Enter operands (separated by space): ");
            String operands = scanner.nextLine();
            sendBuffer = operands.getBytes();
            sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, serverAddress, 6789);
            clientSocket.send(sendPacket);

            // Read result from server
            receiveBuffer = new byte[1024]; // Clear buffer
            receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
            clientSocket.receive(receivePacket);
            String result = new String(receivePacket.getData(), 0, receivePacket.getLength());
            System.out.println(result);
        }

        clientSocket.close();
        scanner.close();
    }
}
