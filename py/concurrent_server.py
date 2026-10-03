# CSNETWK S03
# Dela Cruz & Liwanag
# Extra Credit: Concurrent Multi-Threaded Server

from socket import *
import threading

serverPort = 12000
serverName = "Server of R. Liwanag & J. Dela Cruz"
serverNum = 10

# Event used to tell all threads that the server is shutting down
shutdown_event = threading.Event()

# Keep track of all client threads
client_threads = []

# Create IPv4 TCP socket and bind to port
serverSocket = socket(AF_INET, SOCK_STREAM)
serverSocket.settimeout(1.0)
serverSocket.bind(("", serverPort))
serverSocket.listen(5)

print(f"\n{serverName} is ready to receive (Concurrent Mode)")

def handle_client(connectionSocket, addr):
    print(f"\n> Client detected...")

    try:
        # Receive message from client
        message = connectionSocket.recv(1024).decode().strip()
        if not message:
            print("> Received empty message from client.. Returning")
            return

        print("> Received message from client...")

        clientName = message[:message.rfind(",")].strip()
        clientNum_str = message[message.rfind(",")+1:].strip()

        # Safely verify if input is a valid positive integer string
        if clientNum_str.isdigit():
            clientNum = int(clientNum_str)
        else:
            clientNum = -1

        # Check range (1-100)
        if clientNum < 1 or clientNum > 100:
            print("Error: Integer value out of range.\n")
            connectionSocket.send("Error: Integer Out of Range.".encode())

            # Exit function server on OOR
            shutdown_event.set()
            print("> Invalid client detected.")
            print("> Server shutdown requested.")
            return

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

    except ConnectionResetError:
        print(f"Error: Client disconnected unexpectedly.")

    except OSError as error:
        print(f"Error: Thread Socket error: {error}")

    finally:
        try:
            connectionSocket.shutdown(SHUT_RDWR)
        except OSError:
            pass

        try:
            connectionSocket.close()
        except OSError:
            pass

        print(f"> Closing connection with {addr}...")

try:
    while not shutdown_event.is_set():
        try:
            # Accept incoming client connection
            connectionSocket, addr = serverSocket.accept()

            # Check for shutdown seq.
            if shutdown_event.is_set():
                try:
                    connectionSocket.close()
                except OSError:
                    pass
                break

            # Spawn a separate thread for each client so multiple clients run concurrently
            client_thread = threading.Thread(target=handle_client, args=(connectionSocket, addr))
            client_threads.append(client_thread)
            client_thread.start()

        except timeout:
            continue

except KeyboardInterrupt:
    print("Error: Server interrupted.")

finally:
    print("\n> Server shutdown initiated.")

    # Close the listening socket so no new clients can connect
    try:
        serverSocket.close()
        print("No longer accepting new clients.")
    except OSError:
        print("Error: Cannot close server socket.")

    # Wait for all existing client threads to finish
    for client_thread in client_threads:
        try:
            if client_thread.is_alive() and client_thread != threading.current_thread():
                client_thread.join()
        except RuntimeError:
            print(f"> Error: Cannot join thread {client_thread.name}.")

    print("> All client threads terminated.")
    print("> Server closed safely.")
