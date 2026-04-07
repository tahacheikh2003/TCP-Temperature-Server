import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

//TemperatureServer
//Cette classe implémente un serveur TCP qui stocke les températures les plus récentes
//des villes. Le serveur écoute les connexions client et crée un
// nouveau thread (ClientHandler) pour chaque client afin de permettre un accès simultané.

public class TemperatureServer {

    public static final int PORT = 2000;

    public static HashMap<String, CityTemperature> temperatures = new HashMap<>();

    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(PORT);// Create the server socket and start listening for clients
            System.out.println("Server started on port " + PORT); 

            while (true) {
                Socket socket = server.accept();// Accept a new client connection
                System.out.println("New client connected: " + socket.getInetAddress());// Log the client IP address
                new ClientHandler(socket).start();// Create a new thread to handle the client
            }

        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());// Display server error message
        }
    }
}