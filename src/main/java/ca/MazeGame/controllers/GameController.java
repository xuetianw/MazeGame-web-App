package ca.MazeGame.controllers;

import ca.MazeGame.Wrappers.ApiBoardWrapper;
import ca.MazeGame.Wrappers.ApiGameWrapper;
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
    private List<ApiGameWrapper> apiGameWrappers = new ArrayList<>();


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
        if (apiGameWrappers.size() != 0) {
            ApiGameWrapper apiGameWrapper =  apiGameWrappers.get(apiGameWrappers.size() - 1);
            MazeGame game = apiGameWrapper.getGame();
            if (!game.hasUserWon() && !game.hasUserLost()) {
                apiGameWrapper.setThreadStop(true);
            }
        }
        ApiGameWrapper apiGameWrapper = new ApiGameWrapper(mazeGame, nextId.incrementAndGet());
        apiGameWrappers.add(apiGameWrapper);
        Thread myThread = new Thread(apiGameWrapper);
        myThread.start();
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
        for (ApiGameWrapper apiGameWrapper : apiGameWrappers) {
            if (apiGameWrapper.gameNumber == gameId) {
                return apiGameWrapper.processMaze();
            }
        }
        throw new ResourceNotFoundException(String.format("gane number %d does not exist", gameId));
    }

//3. Board
    @GetMapping("/games/{id}/board")
    public ApiBoardWrapper getBoard(@PathVariable("id") int id) {
        for (ApiGameWrapper apiGameWrapper : apiGameWrappers) {
            if (apiGameWrapper.gameNumber == id) {
                return apiGameWrapper.apiBoardWrapper.processMaze();
            }
        }
        throw new ResourceNotFoundException(String.format("board number %d does not exist", id));
    }

    @PostMapping("games/{id}/moves")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void makeMove(@PathVariable("id") int gameId,
                         @RequestBody String newMove) {
        for(ApiGameWrapper apiGameWrapper : apiGameWrappers) {
            if(apiGameWrapper.gameNumber== gameId){
                apiGameWrapper.move(newMove);
                return;
            }
        }
        throw new ResourceNotFoundException(String.format("game number %d does not exist", gameId));
    }

    @PutMapping("games/{id}/increaseSpeed")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void increaseCatSpeed(@PathVariable("id") int id) {
        for(ApiGameWrapper apiGameWrapper : apiGameWrappers) {
            if(apiGameWrapper.gameNumber== id){
                apiGameWrapper.decreaseTimeInterval();
                return;
            }
        }
    }

    @PutMapping("games/{id}/decreaseSpeed")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void decreaseCatSpeed(@PathVariable("id") int id) {
        for(ApiGameWrapper apiGameWrapper : apiGameWrappers) {
            if(apiGameWrapper.gameNumber== id){
                apiGameWrapper.increaseTimeInterval();
                return;
            }
        }
    }

    @PostMapping("games/{id}/cheatstate")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void showBoard(@PathVariable("id") int id,
            @RequestBody String newMove) {
        for(ApiGameWrapper apiGameWrapper : apiGameWrappers) {
            if(apiGameWrapper.gameNumber== id){
                if (newMove.equals("SHOW_ALL")) {
                    apiGameWrapper.revealBoard();
                }
                return;
            }
        }
    }




    // Create Exception Handle
    @ResponseStatus(value = HttpStatus.BAD_REQUEST,
                reason = "Request ID not found.")
    @ExceptionHandler(IllegalArgumentException.class)
    public void badIdExceptionHandler() {
        // Nothing to do
    }
}
