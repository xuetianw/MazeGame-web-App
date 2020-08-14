package ca.MazeGame.controllers;

import ca.MazeGame.Threads.ThreadsMain;
import ca.MazeGame.Wrappers.ApiBoardWrapper;
import ca.MazeGame.Wrappers.ApiGameWrapper;
import ca.MazeGame.exception.BadRequestException;
import ca.MazeGame.exception.InvalidMoveException;
import ca.MazeGame.exception.ResourceNotFoundException;
import ca.MazeGame.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api")
public class GameController {
    private AtomicLong nextId = new AtomicLong();
    private List<MazeGame> games = new ArrayList<>();


    @GetMapping("about")
    public String getHelloMessage() {
        return "Hello from Fred Wu!";
    }

    //2. Games
/*
    POST /api/games
    Create a new game by POSTing to this end point. You need not post any data (no body).
    Returns one fully populated ApiGameWrapper object instance.
    Returns HTTP status code 201 (Created) when successful.
    */
    @PostMapping("/games")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiGameWrapper postNewgame() throws SocketException {
        MazeGame mazeGame = new MazeGame();
        int gameID = games.size();
        ApiGameWrapper apiGameWrapper = new ApiGameWrapper(mazeGame, gameID);
        ThreadsMain.processGame(mazeGame);
        games.add(mazeGame);

        return apiGameWrapper.processMaze();
    }

    /*
        GET /api/games
    Return a list of ApiGameWrapper objects for the games that the system knows about. Note that
    once a game is created, the server does not need to ever delete that game: it can exist until the
    server is restarted.
    Each ApiGameWrapper stores an ID to uniquely identify the game. This ID is assigned by the
    backend.
    TIP: Make the ID the game’s index into the list which you use to store the games.
     */
    @GetMapping("/games")
    public List<ApiGameWrapper> getGames() {
        List<ApiGameWrapper> apiGameWrappers = new ArrayList<>();
        for (int i = 0; i < games.size(); i++) {
            MazeGame game = games.get(i);
            apiGameWrappers.add(new ApiGameWrapper(game, i));
        }
        return apiGameWrappers;
    }

    /*
    Return the ApiGameWrapper object for game 1 (where 1 is the game ID; 1 need not be the first ID
assigned).
Error Handling:
Return 404 (File Not Found) if the requested game does not exist.
     */
    @GetMapping("games/{id}")
    public ApiGameWrapper getGames(@PathVariable("id") long gameId) {
        if (gameId < 0 || gameId >= games.size()) {
            throw new ResourceNotFoundException(String.format("gane number %d does not exist", gameId));
        }
        return new ApiGameWrapper(games.get((int)gameId), gameId).processMaze();
    }

//3. Board
    @GetMapping("/games/{id}/board")
    public ApiBoardWrapper getBoard(@PathVariable("id") long id) {
        if (id < 0 || id >= games.size()) {
            throw new ResourceNotFoundException(String.format("gane number %d does not exist", id));
        }
        return (new ApiGameWrapper(games.get((int)id), id)).apiBoardWrapper;
    }

    @PostMapping("games/{id}/moves")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void makeMove(@PathVariable("id") long gameId,
                         @RequestBody String newMove) {

        if (gameId < 0 || gameId >= games.size()) {
            throw new ResourceNotFoundException(String.format("gane number %d does not exist", gameId));
        }
        MazeGame game = games.get((int)gameId);
        if (newMove.equals("MOVE_CATS")) {
            game.moveCat();
            doWonOrLost(game);
        } else {
            doPlayerMove(newMove, game);
        }
    }

    @PutMapping("games/{id}/increaseSpeed")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void increaseCatSpeed(@PathVariable("id") int gameId) {
        if (gameId < 0 || gameId >= games.size()) {
            throw new ResourceNotFoundException(String.format("gane number %d does not exist", gameId));
        }
        MazeGame game = games.get(gameId);
        ThreadsMain.increaseCatSpeed(game);

//        for(ApiGameWrapper apiGameWrapper : apiGameWrappers) {
//            if(apiGameWrapper.gameNumber== id){
//                apiGameWrapper.decreaseTimeInterval();
//                return;
//            }
//        }
    }

    @PutMapping("games/{id}/decreaseSpeed")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void decreaseCatSpeed(@PathVariable("id") int gameId) {
//        for(ApiGameWrapper apiGameWrapper : apiGameWrappers) {
//            if(apiGameWrapper.gameNumber== id){
//                apiGameWrapper.increaseTimeInterval();
//                return;
//            }
//        }

        if (gameId < 0 || gameId >= games.size()) {
            throw new ResourceNotFoundException(String.format("gane number %d does not exist", gameId));
        }
        MazeGame game = games.get(gameId);
        ThreadsMain.decreaseCatSpeed(game);
    }

    @PostMapping("games/{id}/cheatstate")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void showBoard(@PathVariable("id") int id,
            @RequestBody String newMove) {
        if (id < 0 || id >= games.size()) {
            throw new ResourceNotFoundException(String.format("gane number %d does not exist", id));
        }
        if (newMove.equals("SHOW_ALL")) {
            MazeGame game = games.get(id);
            game.displayBoard();
        }
    }




    // Create Exception Handle
    @ResponseStatus(value = HttpStatus.BAD_REQUEST,
                reason = "Request ID not found.")
    @ExceptionHandler(IllegalArgumentException.class)
    public void badIdExceptionHandler() {
        // Nothing to do
    }

    private static Direction getPlayerMove(String newMove) {
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

    private void doPlayerMove(String arrow, MazeGame game) {
        Direction move = getPlayerMove(arrow);
        if (!game.isValidPlayerMove(move)) {
            throw new InvalidMoveException("new location on the wall");
        } else {
            game.recordPlayerMove(move);
            doWonOrLost(game);
        }
    }
    private boolean gameNotWonOrLost(MazeGame game) {
        return !game.hasUserWon() && !game.hasUserLost();
    }

    private void doWonOrLost(MazeGame game) {
//        System.out.println("called");
        if (game.hasUserWon()) {
            revealBoard(game);
        } else if (game.hasUserLost()) {
            revealBoard(game);
        } else {
            assert false;
        }
    }

    private void revealBoard(MazeGame game) {
        game.displayBoard();
    }
}
