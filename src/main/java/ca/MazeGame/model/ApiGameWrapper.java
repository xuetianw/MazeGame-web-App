package ca.MazeGame.model;

import ca.MazeGame.exception.BadRequestException;
import ca.MazeGame.exception.InvalidMoveException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.swing.*;
import java.io.File;
import java.util.Collections;
import java.util.List;

import static javax.swing.JOptionPane.INFORMATION_MESSAGE;
import static javax.swing.JOptionPane.showMessageDialog;


public class ApiGameWrapper {

    public boolean isGameWon;
    public boolean isGameLost;
    public int numCheeseFound;
    public int numCheeseGoal;
    public Long gameNumber;
    public MazeGame game;
    public ApiBoardWrapper apiBoardWrapper;

    public ApiGameWrapper(MazeGame game, long id) {
        apiBoardWrapper = new ApiBoardWrapper(game);
        this.game = game;
        gameNumber = id;
    }

    public ApiGameWrapper(boolean isGameWon, boolean isGameLost, int numCheeseFound, int numCheeseGoal, Long gameNumber, MazeGame game) {
        this.isGameWon = isGameWon;
        this.isGameLost = isGameLost;
        this.numCheeseFound = numCheeseFound;
        this.numCheeseGoal = numCheeseGoal;
        this.gameNumber = gameNumber;
        this.game = game;
    }


    public void move(String newMove) {
        if (newMove.equals("MOVE_CATS")) {
            game.moveCat();
            doWonOrLost();
            return;
        }
        doPlayerMove(newMove);
    }

    public void doPlayerMove(String arrow) {
        Direction move = Direction.NOT_MOVING;
        move = getPlayerMove(arrow);
        if (!game.isValidPlayerMove(move)) {
            throw new InvalidMoveException("new location on the wall");
        } else {
            game.recordPlayerMove(move);
            if (!gameNotWonOrLost()) {
//                System.out.println("Cats won!");
                doWonOrLost();
            }
        }
    }

    Direction getPlayerMove(String newMove) {
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
    public boolean gameNotWonOrLost() {
        return !game.hasUserWon() && !game.hasUserLost();
    }

    private void doWonOrLost() {
        if (game.hasUserWon()) {
            revealBoard();
        } else if (game.hasUserLost()) {
			revealBoard();
        } else {
            assert false;
        }
    }
    public void revealBoard() {
        game.displayBoard();
    }

    public ApiGameWrapper processMaze() {
        isGameWon = game.hasUserWon();
        isGameLost = game.hasUserLost();
        numCheeseFound = game.getNumCheeseCollected();
        numCheeseGoal = MazeGame.getNumCheeseToCollect();
        gameNumber = gameNumber;
        game = game;
        return new ApiGameWrapper(isGameWon, isGameLost, numCheeseFound, numCheeseGoal, gameNumber, game);
    }
}
