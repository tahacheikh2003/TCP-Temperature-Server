import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Map;
import java.util.Scanner;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;


//ClientHandler
//This class handles one client connection in a separate thread.
// It receives commands from the client, processes them, and sends
//back the appropriate response.


public class ClientHandler extends Thread {


    private Socket socket;// Socket associated with one connected client

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        Scanner in = null;
        PrintStream out = null;

        try {// Create input and output streams for communication with the client
            in = new Scanner(socket.getInputStream());
            out = new PrintStream(socket.getOutputStream());

            while (true) {// Continuously read client requests until the connection is closed
                if (!in.hasNextLine()) {
                    break;
                }

                String request = in.nextLine().trim();

                if (request.isEmpty()) {// Ignore empty requests
                    out.println("Empty request");
                    continue;
                }

                System.out.println("Request from " + socket.getInetAddress() + ": " + request);// Log the request for debugging and monitoring

                String[] parts = request.split(" ");
                String command = parts[0].toUpperCase();

                // Execute the corresponding command
                if (command.equals("UPDATE")) {
                    handleUpdate(parts, out);
                } else if (command.equals("GET")) {
                    handleGet(parts, out);
                } else if (command.equals("LIST")) {
                    handleList(out);
                } else if (command.equals("AVG")) {
                    handleAvg(out);
                } else if (command.equals(".")) {
                    out.println("Connection closed");
                    break;
                } else {
                    out.println("Unknown command");
                }
            }

        } catch (IOException e) {
            System.out.println("Client error: " + e.getMessage());
        } finally {
            try {// Close all resources when communication ends
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                System.out.println("Closing error: " + e.getMessage());
            }
        }
    }

    private void handleUpdate(String[] parts, PrintStream out) {
        if (parts.length != 3) {
            out.println("Usage: UPDATE city temperature");
            return;
        }

        // Convert city name to lowercase to avoid duplicates such as Tripoli/tripoli
        String city = parts[1].toLowerCase();
        double temp;

        // Convert temperature from String to double
        try {
            temp = Double.parseDouble(parts[2]);
        } catch (NumberFormatException e) {
            out.println("Invalid temperature format");
            return;
        }

        if (temp < -50 || temp > 60) {// Validate the temperature range
            out.println("Invalid temperature: must be between -50 and 60");
            return;
        }

        synchronized (TemperatureServer.temperatures) {
            TemperatureServer.temperatures.put(city, new CityTemperature(temp));
        }

        out.println("OK");
    }

    private void handleGet(String[] parts, PrintStream out) {
        if (parts.length != 2) {
            out.println("Usage: GET city");
            return;
        }

        String city = parts[1].toLowerCase();
        CityTemperature ct;

        synchronized (TemperatureServer.temperatures) {// Search for the city in the shared HashMap
            ct = TemperatureServer.temperatures.get(city);
        }

        if (ct == null) {
            out.println("City not found");
        } else {// Convert timestamp into a readable date and time format
            String time = Instant.ofEpochMilli(ct.getTimestamp())
            .atZone(ZoneId.systemDefault())
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            out.println("City: " + city +
            ", Temp: " + ct.getTemperature() +
            ", Time: " + time);
        }
    }

    private void handleList(PrintStream out) {
        synchronized (TemperatureServer.temperatures) {
            if (TemperatureServer.temperatures.isEmpty()) {
                out.println("No cities available");
                out.println("END");
                return;
            }

            for (Map.Entry<String, CityTemperature> entry : TemperatureServer.temperatures.entrySet()) {
                String city = entry.getKey();
                CityTemperature ct = entry.getValue();

                String time = Instant.ofEpochMilli(ct.getTimestamp())
            .atZone(ZoneId.systemDefault())
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            out.println("City: " + city +
            ", Temp: " + ct.getTemperature() +
            ", Time: " + time);
            }
        }

        out.println("END");
    }

    private void handleAvg(PrintStream out) {
        synchronized (TemperatureServer.temperatures) {
            if (TemperatureServer.temperatures.isEmpty()) {
                out.println("No data");
                return;
            }

            double sum = 0.0;
            int count = 0;

            for (CityTemperature ct : TemperatureServer.temperatures.values()) {
                sum += ct.getTemperature();
                count++;
            }

            double avg = sum / count;
            out.println("Average temperature: " + avg);
        }
    }
}