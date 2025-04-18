import java.io.*; 
import java.net.*; 

class MathServer { 
    public static void main(String argv[]) { 
        try (ServerSocket serverSocket = new ServerSocket(6789)) { 
            System.out.println("Math Server is running...");

            while (true) { 
                try (Socket connectionSocket = serverSocket.accept()) { 
                    System.out.println("Client connected.");

                    BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream())); 
                    DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

                    while (true) {
                        // Send menu to client
                        outToClient.writeBytes("Select 1 to add, 2 to subtract, 3 to multiply, 4 to exit.\n");
                        
                        // Read choice from client
                        String choiceStr = inFromClient.readLine();
                        int choice;
                        try {
                            choice = Integer.parseInt(choiceStr);
                        } catch (NumberFormatException e) {
                            outToClient.writeBytes("Invalid input. Please enter a number.\n");
                            continue;
                        }

                        // Exit condition
                        if (choice == 4) {
                            System.out.println("Client exited.");
                            break;
                        }

                        // Check if the choice is valid
                        if (choice < 1 || choice > 3) {
                            outToClient.writeBytes("Invalid choice. Try again.\n");
                            continue;
                        }

                        // Ask client for operands **only once**
                        outToClient.writeBytes("Enter operands (separated by space):\n");

                        while (true) {
                            String operandStr = inFromClient.readLine();
                            String[] operands = operandStr.split(" ");

                            // Validate operand count
                            if (operands.length != 2) {
                                outToClient.writeBytes("Error: Enter exactly two numbers.\n");
                                continue;
                            }

                            // Parse operands
                            double num1, num2;
                            try {
                                num1 = Double.parseDouble(operands[0]);
                                num2 = Double.parseDouble(operands[1]);
                            } catch (NumberFormatException e) {
                                outToClient.writeBytes("Error: Invalid number format.\n");
                                continue;
                            }

                            // Perform calculation
                            double result = 0;
                            switch (choice) {
                                case 1: result = num1 + num2; break;
                                case 2: result = num1 - num2; break;
                                case 3: result = num1 * num2; break;
                            }

                            // Send result to client and exit inner loop
                            outToClient.writeBytes("Result: " + result + "\n");
                            break;
                        }
                    }
                } catch (IOException e) {
                    System.err.println("Error handling client: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }
}
