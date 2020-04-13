cmake_minimum_required(VERSION 3.15)
project(untitled3 C)

set(CMAKE_RUNTIME_OUTPUT_DIRECTORY "~/cmpt433/public")

#set(CMAKE_C_STANDARD 99)

add_executable(MazeGameC
        main.c
        joystick.c
        UDP.c
        )

SET(CMAKE_C_COMPILER arm-linux-gnueabihf-gcc)

target_link_libraries(MazeGameC pthread)