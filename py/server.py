# CSNETWK S03
# Dela Cruz & Liwanag

from socket import *

serverPort = 12000

try:
    serverSocket = socket(AF_INET, SOCK_STREAM)
    serverSocket.bind(("", serverPort))
    serverSocket.listen(1)
except Exception as e:
    print(f"Error setting up server: {e}")
    exit()

serverName = "Server of R. Liwanag"
serverNum = 42

print(f"\n{serverName} is ready to receive")

while True:
    try:
        # Stores connected socket & client's IP and Port
        connectionSocket, addr = serverSocket.accept()
        print("\n> Client detected...")
        message = connectionSocket.recv(1024).decode().strip()
        print("> Received message from client...")

        # Parses client name and number
        clientName = message[:message.rfind(",")].strip()
        clientNum_str = message[message.rfind(",")+1:].strip()

        # Converts to integer safely
        try:
            clientNum = int(clientNum_str)
        except ValueError:
            clientNum = -1

        # Detects Out of Range values
        if clientNum < 1 or clientNum > 100:
            print("Error: Integer value out of range.\n")
            connectionSocket.send("Error: Integer Out of Range.".encode())
            connectionSocket.close()
            serverSocket.close()
            print("> Closing server...\n")
            break
        else:
            total = clientNum + serverNum
            print("\n--- Results ---")
            print(f"Client:          {clientName}")
            print(f"Server:          {serverName}")
            print(f"Client's Number: {clientNum}")
            print(f"Server's Number: {serverNum}")
            print(f"Sum:             {total}\n")

            # Send server name & num to client
            response = serverName + "," + str(serverNum)
            connectionSocket.send(response.encode())
            print("> Sent response to client...")

        connectionSocket.close()
        print("> Closing connection...")

    except Exception as e:
        print(f"Error occurred: {e}")
        break