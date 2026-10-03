# CSNETWK S03
# Dela Cruz & Liwanag

from socket import *

# Target server details
serverAddress = input("Enter the server address: ").strip()
serverPort = 12000
clientName = "Client of R. Liwanag"

clientSocket = None

# Prompt user for input
raw_input = input("\nEnter an integer between 1 and 100: [IF OOR, Terminate Server]").strip()
print("> Input detected...")

# Validate input
if not raw_input.isdigit():
    print("Please run the program again and enter a valid integer.\n")
    exit()

clientNum = int(raw_input)

try:
    # Create TCP socket
    clientSocket = socket(AF_INET, SOCK_STREAM)
    clientSocket.settimeout(10)

    # Connect to server
    clientSocket.connect((serverAddress, serverPort))
    print("> Connected to server...")

    # Send client name and integer
    message = f"{clientName},{clientNum}"
    clientSocket.send(message.encode())

    print("> Sent message to server...")

    # Receive server response
    response = clientSocket.recv(1024).decode().strip()

    if not response:
        print("Error: Server closed the connection.")
        exit()

    print("> Received response from server...")

    # Check if server sent an error message
    if response.startswith("Error"):
        print(f"Message from server: {response}\n")

    else:
        try:
            # Parse server details and number
            server_id = response[:response.rfind(",")]
            server_num_str = response[response.rfind(",") + 1:]

            server_num = int(server_num_str)

            # Compute sum
            total = clientNum + server_num

            # Print results
            print("\n--- Results ---")
            print(f"Client Name:   {clientName}")
            print(f"Server Name:   {server_id}")
            print(f"Client Number: {clientNum}")
            print(f"Server Number: {server_num}")
            print(f"Sum:           {total}")
            print("---------------\n")

        except ValueError:
            print("Error: Server sent an invalid response.")

except ConnectionRefusedError:
    print("Error: Could not connect to the server.")

except TimeoutError:
    print("Error: Connection timed out.")

except ConnectionResetError:
    print("Error: Server terminated the connection.")

except KeyboardInterrupt:
    print("Error: Client interrupted.")

except OSError as error:
    print(f"Network error: {error}")

finally:
    if clientSocket is not None:
        clientSocket.close()

    print("> Closing client...\n")

