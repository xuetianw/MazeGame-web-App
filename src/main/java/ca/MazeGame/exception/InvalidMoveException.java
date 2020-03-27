package ca.MazeGame.exception;

/**
 * Created by wu on 2018-03-18.
 */

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidMoveException extends RuntimeException {
    public InvalidMoveException(String s){
        super(s);
    }
}
