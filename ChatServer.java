import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private static int clientIdCounter = 1;
    private static final Map<Integer, ClientHandler> clients = new HashMap<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            System.out.println("Chat Server started on port 5000...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler handler = new ClientHandler(clientSocket, clientIdCounter);
                
                synchronized (clients) {
                    clients.put(clientIdCounter, handler);
                }

                new Thread(handler).start();
                System.out.println("Client " + clientIdCounter + " connected.");
                clientIdCounter++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ClientHandler implements Runnable {
        private Socket socket;
        private int clientId;
        private PrintWriter out;
        private BufferedReader in;

        public ClientHandler(Socket socket, int clientId) {
            this.socket = socket;
            this.clientId = clientId;
        }

        @Override
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                out.println("Welcome! Your Client ID: " + clientId);

                String message;
                while ((message = in.readLine()) != null) {
                    if (message.equalsIgnoreCase("GET")) {
                        sendClientList();
                    } else if (message.startsWith("SEND")) {
                        sendMessageToClient(message);
                    } else {
                        out.println("Invalid Command! Use 'GET' or 'SEND <client_id> <message>'");
                    }
                }
            } catch (IOException e) {
                System.out.println("Client " + clientId + " disconnected.");
            } finally {
                try {
                    socket.close();
                    synchronized (clients) {
                        clients.remove(clientId);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void sendClientList() {
            StringBuilder clientList = new StringBuilder("Connected Clients: ");
            synchronized (clients) {
                for (Integer id : clients.keySet()) {
                    clientList.append(id).append(" ");
                }
            }
            out.println(clientList.toString().trim()); // Remove extra spaces
        }

        private void sendMessageToClient(String message) {
            String[] parts = message.split(" ", 3);
            if (parts.length < 3) {
                out.println("Invalid SEND format. Use: SEND <client_id> <message>");
                return;
            }

            try {
                int recipientId = Integer.parseInt(parts[1]);
                String msg = parts[2];

                synchronized (clients) {
                    if (clients.containsKey(recipientId)) {
                        clients.get(recipientId).out.println("From Client " + clientId + ": " + msg);
                    } else {
                        out.println("Client " + recipientId + " not found.");
                    }
                }
            } catch (NumberFormatException e) {
                out.println("Invalid Client ID.");
            }
        }
    }
}
