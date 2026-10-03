# CSNETWK S03
# Dela Cruz & Liwanag

from socket import *

# Target server details
serverAddress = "localhost" 
serverPort = 12000
clientName = "Client of R. Liwanag"

# Prompt user for input
raw_input = input("\nEnter an integer between 1 and 100: ")
print("> Input detected...")

# Validate that the input is a valid positive integer
if not raw_input.isdigit():
    print("Please run the program again and enter a valid integer.\n")
    exit()

clientNum = int(raw_input)

# Create TCP socket and connect to server
clientSocket = socket(AF_INET, SOCK_STREAM)
clientSocket.connect((serverAddress, serverPort))

# Send client name and integer
message = f"{clientName},{clientNum}"
clientSocket.send(message.encode())
print("> Sent message to server...")

# Receive server response
response = clientSocket.recv(1024).decode()
print("> Received response from server...")

# Check if server sent an error message
if "Error" in response:
    print(f"Message from server: {response}\n")
else:
    # Parse server details and number
    server_id = response[:response.rfind(",")]
    server_num_str = response[response.rfind(",")+1:]
    server_num = int(server_num_str)

    # Compute sum
    total = clientNum + server_num

    # Print results
    print("\n--- Results ---")
    print(f"Client Name:   {clientName}")
    print(f"Server Name:   {server_id}")
    print(f"Client Number: {clientNum}")
    print(f"Server Number: {server_num}")
    print(f"Sum:           {total}\n")

# Close client socket
clientSocket.close()
print("> Closing client...\n")