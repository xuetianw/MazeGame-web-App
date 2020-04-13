//
// Created by fred on 2020-04-12.
//

#include <stdlib.h>
#include <stdbool.h>
#include <stdio.h>
#include <pthread.h>
#include <time.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <sys/types.h>


#define SERVER "127.0.0.1"
#define PORT 1234


void* sendMessage(char message[]) {
    struct sockaddr_in sin;

    int s, i, slen = sizeof(sin);
    //create a UDP socket
    if ((s = socket(AF_INET, SOCK_DGRAM, IPPROTO_UDP)) == -1) {
        printf("error: could not establish connection\n");
    }
    memset((char*) &sin, 0, sizeof(sin));
    sin.sin_family = AF_INET;
    sin.sin_port = htons(PORT);

    if (inet_aton(SERVER, &sin.sin_addr) == 0) {
        fprintf(stderr, "inet_aton() failed\n");
        exit(1);
    }
    //bind socket to port
    if (bind(s, (struct sockaddr*) &sin, sizeof(sin)) == -1) {
        printf("error: could not bind socket to port\n");
    } else {
        printf("Success!\n");
    }
    if (sendto(s, message, strlen(message), 0, (struct sockaddr*) &sin, slen) == -1) {
        printf("error: could not send message\n");
    } else {
        printf("Success!\n");
    }

    close(s);
}
