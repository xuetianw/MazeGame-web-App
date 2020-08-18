package ca.MazeGame.Wrappers;

import ca.MazeGame.exception.BadRequestException;
import ca.MazeGame.model.Direction;

public class MoveUtility {

    public static final String COMMAND_LEFT = "MOVE_LEFT";
    public static final String COMMAND_RIGHT = "MOVE_RIGHT";
    public static final String COMMAND_UP = "MOVE_UP";
    public static final String COMMAND_DOWN = "MOVE_DOWN";


    public Direction getPlayerMove(String newMove) {
//        System.out.println(newMove);
        switch (newMove) {
            case "MOVE_LEFT":
                return Direction.MOVE_LEFT;
            case "MOVE_UP":
                return Direction.MOVE_UP;
            case "MOVE_RIGHT":
                return Direction.MOVE_RIGHT;
            case "MOVE_DOWN":
                return Direction.MOVE_DOWN;
            default:
                throw new BadRequestException("NoSuchMove");
        }
    }
}
