import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer3 {
    private static final int PORT = 5000;
    private static List<ClientHandler> clients = Collections.synchronizedList(new ArrayList<>());
    private static volatile boolean serverRunning = true;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT);
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Server started on port " + PORT);

            // Thread for handling server console input (closing server)
            Thread serverInputThread = new Thread(() -> {
                while (serverRunning) {
                    String input = scanner.nextLine();
                    if (input.equalsIgnoreCase("q")) {
                        serverRunning = false;
                        closeAllClients();
                        break;
                    }
                }
            });
            serverInputThread.start();

            // Server main loop: Accept new clients
            while (serverRunning) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket);
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clients.add(clientHandler);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            if (serverRunning) e.printStackTrace();
        }
    }

    // Close all client connections and exit
    private static synchronized void closeAllClients() {
        for (ClientHandler client : clients) {
            client.closeConnection();
        }
        System.out.println("Server shutting down...");
        System.exit(0);
    }

    static class ClientHandler implements Runnable {
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        private volatile boolean flag = true;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                Scanner serverInput = new Scanner(System.in);

                // Write Thread: Sends messages from server to client
                Thread writeThread = new Thread(() -> {
                    while (flag) {
                        String serverMessage = serverInput.nextLine();
                        out.println(serverMessage);
                        if (serverMessage.equalsIgnoreCase("bye")) {
                            flag = false;
                        }
                    }
                });

                // Read Thread: Reads messages from the client
                Thread readThread = new Thread(() -> {
                    try {
                        while (flag) {
                            String clientMessage = in.readLine();
                            if (clientMessage == null || clientMessage.equalsIgnoreCase("bye")) {
                                flag = false;
                                break;
                            }
                            System.out.println("Client: " + clientMessage);
                        }
                    } catch (IOException e) {
                        System.out.println("Client disconnected.");
                    }
                });

                writeThread.start();
                readThread.start();

                writeThread.join();
                readThread.join();

            } catch (IOException | InterruptedException e) {
                System.out.println("Error handling client.");
            } finally {
                closeConnection();
            }
        }

        public void closeConnection() {
            try {
                flag = false;
                if (in != null) in.close();
                if (out != null) out.close();
                if (socket != null) socket.close();
                System.out.println("Client disconnected.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
