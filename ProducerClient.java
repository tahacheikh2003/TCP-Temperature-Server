import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

// ProducerClient
// This client is responsible for sending temperature updates to the server.
//UPDATE command to the TemperatureServer using a TCP socket.


public class ProducerClient {

    public static void main(String[] args) {
        Scanner key = new Scanner(System.in); // Scanner used to read input from the keyboard

        System.out.print("Server IP: ");
        String ip = key.nextLine();

        try {
            Socket socket = new Socket(ip, TemperatureServer.PORT); // Create a TCP connection with the server
            Scanner in = new Scanner(socket.getInputStream()); // Input stream to receive responses from the server
            PrintStream out = new PrintStream(socket.getOutputStream());

            while (true) {
                System.out.print("City (or . to exit): ");
                String city = key.nextLine();

                if (city.equals(".")) { // If the user enters '.', the client closes the connection
                    out.println(".");
                    System.out.println(in.nextLine());
                    break;
                }

                System.out.print("Temperature: ");// Ask for the temperature of the city
                String temp = key.nextLine();

                out.println("UPDATE " + city + " " + temp);
                System.out.println("Server response: " + in.nextLine());// Display the server response

            }

            in.close();
            out.close();
            socket.close();

        } catch (IOException e) {
            System.out.println("Client error: " + e.getMessage());
        }

        key.close();// Close keyboard scanner
    }
}