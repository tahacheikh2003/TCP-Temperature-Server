# Multithreaded TCP Temperature Server in Java



### Description

### \-----------

This project implements a multithreaded TCP server that stores and manages

temperature data for different cities. The server allows multiple clients

to connect simultaneously.



There are two types of clients:

1\) Producer Client: sends temperature updates to the server.

2\) Consumer Client: requests temperature information from the server.



The server stores the latest temperature for each city and supports

additional commands such as listing all cities and calculating the

average temperature.





### Files Description

### \-----------------



TemperatureServer.java

This is the main server program. It creates a TCP server using ServerSocket

and listens for incoming client connections. For every new client connection,

a new ClientHandler thread is created to handle communication with that client.



ClientHandler.java

This class handles the communication between the server and a connected client.

It processes the following commands:

\- UPDATE city temperature : updates the temperature of a city

\- GET city : returns the latest temperature and timestamp of a city

\- LIST : returns all stored cities and their temperatures

\- AVG : calculates the average temperature of all cities



ProducerClient.java

This client is used to send temperature updates to the server.

The user enters a city name and its temperature, and the client sends

an UPDATE command to the server.



ConsumerClient.java

This client allows users to request temperature information from the server.

The user can:

1\) Get the temperature of a specific city

2\) List all cities stored in the server

3\) Get the average temperature of all cities



CityTemperature.java

This class stores the temperature of a city and the timestamp of the

last update.





### How to Run the Program

### \----------------------



1\) Start the server



java TemperatureServer





2\) Start the Producer Client (to send temperature updates)



java ProducerClient



Example:

Server IP: 127.0.0.1

City: Tripoli

Temperature: 25





3\) Start the Consumer Client (to request information)



java ConsumerClient



Available commands:

1 - GET city temperature

2 - LIST all cities

3 - AVG temperature

0 - Exit





### Example Tests

### \-------------



1\) Use ProducerClient to add city temperatures:

Tripoli -> 24

Beirut -> 20

Paris -> 18



2\) Use ConsumerClient to test commands:



GET Tripoli

LIST

AVG



The server will return the requested information.



### 

### Additional Features Implemented

### \-------------------------------

\- Multithreaded server (multiple clients supported)

\- Temperature validation (-50 to 60)

\- Timestamp for each temperature update

\- LIST command to display all cities

\- AVG command to compute average temperature

