import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

//
// ConsumerClient
 //This client connects to the TemperatureServer and allows the user
//to request information about stored temperatures. The user can:
// - Get the temperature of a specific city
//  - List all cities and their temperatures
//  - Calculate the average temperature
//



public class ConsumerClient {

    public static void main(String[] args) {
        Scanner key = new Scanner(System.in); // Scanner used to read user input from the keyboard

        System.out.print("Server IP: ");
        String ip = key.nextLine();// Ask the user for the server IP address

        try {
            Socket socket = new Socket(ip, TemperatureServer.PORT);// Create a TCP connection with the server
            Scanner in = new Scanner(socket.getInputStream());// Stream used to receive responses from the server
            PrintStream out = new PrintStream(socket.getOutputStream());// Stream used to send commands to the server

            while (true) {// Main loop allowing the user to send multiple requests
                System.out.println();// Display available commands
                System.out.println("Choose command:");
                System.out.println("1 - GET city temperature");
                System.out.println("2 - LIST all cities");
                System.out.println("3 - AVG temperature");
                System.out.println("0 - Exit");
                System.out.print("Your choice: ");

                String choice = key.nextLine();

                // GET command: request the temperature of a specific city
                if (choice.equals("1")) {
                    System.out.print("City: ");
                    String city = key.nextLine();

                    out.println("GET " + city);
                    System.out.println("Server response: " + in.nextLine());

                } else if (choice.equals("2")) {// LIST command: request all stored cities and temperatures
                    out.println("LIST");

                    while (true) { // Read lines from the server until the END marker is received
                        String line = in.nextLine();
                        if (line.equals("END")) {
                            break;
                        }
                        System.out.println(line);
                    }

                } else if (choice.equals("3")) {// AVG command: request the average temperature of all cities
                    out.println("AVG");
                    System.out.println("Server response: " + in.nextLine());

                } else if (choice.equals("0")) {
                    out.println(".");
                    System.out.println(in.nextLine());
                    break;

                } else {
                    System.out.println("Invalid choice");
                }
            }

            in.close();
            out.close();
            socket.close();

        } catch (IOException e) {
            System.out.println("Client error: " + e.getMessage());
        }
        
        // Close keyboard scanner
        key.close();
    }
}