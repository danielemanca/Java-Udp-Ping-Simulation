# Java-Udp-Ping-Simulation
Simulation of Ping. I used DatagramSocket to comunicate from one process to another in the same machine. Server is basically a echo server that randomly ignores a packet to simulate packet loss in Internet. It also simulate a delay.
Clients has a socket timeout: after 2 seconds it considers that packet as lost.
