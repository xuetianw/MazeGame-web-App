#include <stdio.h>
/*
sends a string message to a local NodeJs server using UDP
*/

#include "joystick.h"

int main() {
    printf("World from Beaglebone!\n");
    joystick_init();

    joystick_cleanup();
    return 0;
}
