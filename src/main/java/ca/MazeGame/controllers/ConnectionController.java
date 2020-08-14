package ca.MazeGame.controllers;

import ca.MazeGame.Threads.DUPListener;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.net.SocketException;

@RestController
public class ConnectionController {

    @PostMapping("device-connect")
    @ResponseStatus(HttpStatus.CREATED)
    public String triggerUDP() throws SocketException {

        return "";
    }
}
