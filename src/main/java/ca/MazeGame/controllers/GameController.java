package ca.MazeGame.controllers;

import ca.MazeGame.UDP.DUPListener;
import ca.MazeGame.Wrappers.ApiBoardWrapper;
import ca.MazeGame.Wrappers.ApiGameWrapper;
import ca.MazeGame.exception.ResourceNotFoundException;
import ca.MazeGame.MazeGames.MazeGame;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

@RestController
@RequestMapping("/api")
public class GameController {
    private AtomicLong nextId = new AtomicLong();
    private List<ApiGameWrapper> gameWrappers = new ArrayList<>();
    ReentrantLock computeLock = new ReentrantLock();
    ReentrantLock moveLock = new ReentrantLock();

    DUPListener dupListener = new DUPListener();
    Thread UDPThread;


    @GetMapping("about")
    public String getHelloMessage() {
        return "Hello from Fred Wu!";
    }

    @PostMapping("/games")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiGameWrapper postNewgame() throws SocketException {
        MazeGame mazeGame = new MazeGame();
        if (gameWrappers.size() != 0) {
            ApiGameWrapper apiGameWrapper =  gameWrappers.get(gameWrappers.size() - 1);
            MazeGame game = apiGameWrapper.getSingleUserGame();
            if (!game.hasUserWon() && !game.hasUserLost()) {
                apiGameWrapper.setThreadStop(true);
            }
        }
        ApiGameWrapper apiGameWrapper = new ApiGameWrapper(mazeGame, nextId.incrementAndGet());
        gameWrappers.add(apiGameWrapper);
        Thread myThread = new Thread(apiGameWrapper);
        myThread.start();
        return apiGameWrapper.processMaze();
    }

    @GetMapping("/games")
    public List<ApiGameWrapper> getGames() {
        return gameWrappers;
    }

    @GetMapping("games/{id}")
    public ApiGameWrapper getGame(@PathVariable("id") long gameId) {
        computeLock.lock();
        for (ApiGameWrapper apiGameWrapper : gameWrappers) {
            if (apiGameWrapper.gameNumber == gameId) {
                ApiGameWrapper copy = apiGameWrapper.processMaze();
                computeLock.unlock();
                return copy;
            }
        }
        computeLock.unlock();
        throw new ResourceNotFoundException(String.format("gane number %d does not exist", gameId));
    }

    @GetMapping("/games/{id}/board")
    public ApiBoardWrapper getBoard(@PathVariable("id") int id) {
        computeLock.lock();
        for (ApiGameWrapper apiGameWrapper : gameWrappers) {
            if (apiGameWrapper.gameNumber == id) {
                ApiBoardWrapper copy = apiGameWrapper.apiBoardWrapper.processMaze();
                computeLock.unlock();
                return copy;
            }
        }
        computeLock.unlock();
        throw new ResourceNotFoundException(String.format("board number %d does not exist", id));
    }

    @PostMapping("games/{id}/moves")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void makeMove(@PathVariable("id") int gameId,
                         @RequestBody String newMove) {
        for(ApiGameWrapper apiGameWrapper : gameWrappers) {
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
        for(ApiGameWrapper apiGameWrapper : gameWrappers) {
            if(apiGameWrapper.gameNumber== id){
                apiGameWrapper.decreaseTimeInterval();
                return;
            }
        }
    }

    @PutMapping("games/{id}/decreaseSpeed")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void decreaseCatSpeed(@PathVariable("id") int id) {
        for(ApiGameWrapper apiGameWrapper : gameWrappers) {
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
        for(ApiGameWrapper apiGameWrapper : gameWrappers) {
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
