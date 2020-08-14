package ca.MazeGame.Wrappers;

import ca.MazeGame.Threads.MoveCatTask;
import ca.MazeGame.exception.BadRequestException;
import ca.MazeGame.exception.InvalidMoveException;
import ca.MazeGame.model.Direction;
import ca.MazeGame.model.MazeGame;


public class ApiGameWrapper {

    public boolean isGameWon;
    public boolean isGameLost;
    public int numCheeseFound;
    public int numCheeseGoal;
    public Long gameNumber;


    public MazeGame game;
    public ApiBoardWrapper apiBoardWrapper;

    public static final String COMMAND_LEFT = "MOVE_LEFT";
    public static final String COMMAND_RIGHT = "MOVE_RIGHT";
    public static final String COMMAND_UP = "MOVE_UP";
    public static final String COMMAND_DOWN = "MOVE_DOWN";

    public ApiGameWrapper() {
    }

    public ApiGameWrapper(MazeGame game, long id) {
        apiBoardWrapper = new ApiBoardWrapper(game);
        this.game = game;
        gameNumber = id;
    }




    public MazeGame getGame() {
        return game;
    }

    public void setGame(MazeGame game) {
        this.game = game;
    }



//    public void move(String newMove) {
//        if (newMove.equals("MOVE_CATS")) {
//            game.moveCat();
//            doWonOrLost();
//            return;
//        }
//        doPlayerMove(newMove);
//    }


//    public void doPlayerMove(String arrow) {
//        Direction move = getPlayerMove(arrow);
//        if (!game.isValidPlayerMove(move)) {
//            throw new InvalidMoveException("new location on the wall");
//        } else {
//            game.recordPlayerMove(move);
//            if (!gameNotWonOrLost()) {
////                System.out.println("Cats won!");
//                doWonOrLost();
//            }
//        }
//    }

    public static Direction getPlayerMove(String newMove) {
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


    public ApiGameWrapper processMaze() {
        isGameWon = game.hasUserWon();
        isGameLost = game.hasUserLost();
        numCheeseFound = game.getNumCheeseCollected();
        numCheeseGoal = MazeGame.getNumCheeseToCollect();
        return this;
    }

}
