import java.io.IOException;  
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

class MS { 
    public static void main(String argv[]) throws Exception { 
        DatagramSocket serverSocket = new DatagramSocket(6789);
        System.out.println("UDP Math Server is running on port 6789...");

        byte[] receiveBuffer = new byte[1024];
        byte[] sendBuffer;    
        DatagramPacket sendPacket; 
        
            while (true) {  
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                serverSocket.receive(receivePacket);
                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();
                


                // Send menu
                sendBuffer="Select 1 to add, 2 to subtract, 3 to multiply, 4 to exit.\n".getBytes();
                sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, clientAddress, clientPort);
                serverSocket.send(sendPacket);

                System.out.println("Waiting for client choice...");
                
                receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                serverSocket.receive(receivePacket);
                String choiceStr = new String(receivePacket.getData(), 0, receivePacket.getLength());

                
                int choice;

                try {
                    choice = Integer.parseInt(choiceStr);
                } catch (NumberFormatException e) {
                    sendBuffer="Invalid input. Please enter a number.\n".getBytes();
                    sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, clientAddress, clientPort);
                    serverSocket.send(sendPacket);
                    continue;
                }

                // Exit condition
                if (choice == 4) {
                    System.out.println("Client exited.");
                    break;
                }

                // Check if choice is valid
                if (choice < 1 || choice > 3) {
                    sendBuffer="Invalid choice. Try again.\n".getBytes();
                    sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, clientAddress, clientPort);
                    serverSocket.send(sendPacket);
                    continue;
                }

                // Ask for operands
                sendBuffer="Enter operands (separated by space):\n".getBytes();
                sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, clientAddress, clientPort);
                serverSocket.send(sendPacket);
                System.out.println("Waiting for operands...");

                receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                serverSocket.receive(receivePacket);
                String operandStr = new String(receivePacket.getData(), 0, receivePacket.getLength());

                String[] operands = operandStr.split(" ");

                if (operands.length < 2) {
                    sendBuffer="Error: Enter at least two numbers.\n".getBytes();
                    sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, clientAddress, clientPort);
                    serverSocket.send(sendPacket);
                    continue;
                }

                double result;
                try {
                    result = Double.parseDouble(operands[0]); // Initialize with first operand
                    
                    // Process based on operation
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
                    sendBuffer="Error: Invalid number format.\n".getBytes();
                    sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, clientAddress, clientPort);
                    serverSocket.send(sendPacket);
                    continue;
                }

                // Send result to client
                System.out.println("Computed Result: " + result);
                String resultmessage="Result " + result;
                        
                sendBuffer=resultmessage.getBytes();
                sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, clientAddress, clientPort);
                serverSocket.send(sendPacket);
            }

            // Close client connection (server keeps running)
            
        
    }
}




