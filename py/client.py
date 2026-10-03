# CSNETWK S03
# Dela Cruz & Liwanag

from socket import *
import sys

serverAddress = "localhost" 
serverPort = 12000
clientName = "Client of R. Liwanag"

raw_input_val = input("\nEnter an integer between 1 and 100: ")
try:
    clientNum = int(raw_input_val)
except ValueError:
    print("Error: Input is not a valid integer. Exiting.\n")
    sys.exit(1)

clientSocket = None
try:
    clientSocket = socket(AF_INET, SOCK_STREAM)
    clientSocket.connect((serverAddress, serverPort))
    print("> Connected to server...")

    message = f"{clientName},{clientNum}"
    clientSocket.send(message.encode())
    print("> Sent message to server...")

    response = clientSocket.recv(1024).decode()
    print("> Received response from server...")

    if "Error" in response:
        print(f"\nMessage from server: {response}\n")
    else:
        server_id = response[:response.rfind(",")]
        server_num = int(response[response.rfind(",")+1:])
        total = clientNum + server_num

        print("\n--- Results ---")
        print(f"Client Name:   {clientName}")
        print(f"Server Name:   {server_id}")
        print(f"Client Number: {clientNum}")
        print(f"Server Number: {server_num}")
        print(f"Sum:           {total}\n")

except Exception as e:
    print(f"Network error: {e}")
finally:
    if clientSocket:
        clientSocket.close()
        print("> Closing client socket...\n")