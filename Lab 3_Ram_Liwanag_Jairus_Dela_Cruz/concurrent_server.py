# CSNETWK S03
# Dela Cruz & Liwanag
# Extra Credit: Concurrent Multi-Threaded Server

from socket import *
import threading
import os

serverPort = 12000
serverName = "Server of R. Liwanag & J. Dela Cruz"
serverNum = 10

# Create IPv4 TCP socket and bind to port
serverSocket = socket(AF_INET, SOCK_STREAM)
serverSocket.bind(("", serverPort))
serverSocket.listen(5)

print(f"\n{serverName} is ready to receive (Concurrent Mode)")

def handle_client(connectionSocket, addr):
    print(f"\n> Client detected from {addr}...")

    # Receive message from client
    message = connectionSocket.recv(1024).decode().strip()
    print("> Received message from client...")

    # Parse client name and integer string
    clientName = message[:message.rfind(",")].strip()
    clientNum_str = message[message.rfind(",")+1:].strip()

    # Safely verify if input is a valid positive integer string
    if clientNum_str.isdigit():
        clientNum = int(clientNum_str)
    else:
        clientNum = -1

    # Check range (1-100); terminate server if out of range
    if clientNum < 1 or clientNum > 100:
        print("Error: Integer value out of range.\n")
        connectionSocket.send("Error: Integer Out of Range.".encode())
        connectionSocket.close()
        serverSocket.close()
        print("> Closing server...\n")
        # Terminate process to cleanly stop background threads and server
        os._exit(0)
    else:
        # Calculate sum
        total = clientNum + serverNum

        # Print results on server console
        print("\n--- Results ---")
        print(f"Client:          {clientName}")
        print(f"Server:          {serverName}")
        print(f"Client's Number: {clientNum}")
        print(f"Server's Number: {serverNum}")
        print(f"Sum:             {total}\n")

        # Send server name and integer back to client
        response = serverName + "," + str(serverNum)
        connectionSocket.send(response.encode())
        print("> Sent response to client...")

    # Close connection socket for this client
    connectionSocket.close()
    print(f"> Closing connection with {addr}...")

while True:
    # Accept incoming client connection
    connectionSocket, addr = serverSocket.accept()
    
    # Spawn a separate thread for each client so multiple clients run concurrently
    client_thread = threading.Thread(target=handle_client, args=(connectionSocket, addr))
    client_thread.start()