package ca.MazeGame.controllers;

import ca.MazeGame.SInglePlayerThreads.MazeGameThreadObj;
import ca.MazeGame.UDP.DUPListener;
import ca.MazeGame.Wrappers.ApiBoardWrapper;
import ca.MazeGame.Wrappers.ApiGameWrapper;
import ca.MazeGame.exception.ResourceNotFoundException;
import ca.MazeGame.MazeGames.MazeGame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

@RestController
@RequestMapping("/api")
@Slf4j
public class GameController {
    private List<MazeGameThreadObj> mazeGameThreadObjs = new ArrayList<>();

    private AtomicLong nextId = new AtomicLong();
    ReentrantLock computeLock = new ReentrantLock();

    DUPListener dupListener = new DUPListener();
    Thread UDPThread;


    @GetMapping("about")
    public String getHelloMessage() {
        return "Hello from Fred Wu!";
    }

    @PostMapping("/games")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiGameWrapper postNewgame() throws SocketException {
//        System.out.println("postNewgame called with gameId ");
        ReentrantLock lock = new ReentrantLock();
        lock.lock();

        MazeGame mazeGame = new MazeGame();
        if (mazeGameThreadObjs.size() != 0) {
            MazeGameThreadObj mazeGameThreadObj =  mazeGameThreadObjs.get(mazeGameThreadObjs.size() - 1);
            MazeGame game = mazeGameThreadObj.getSingleUserGame();
//            if (!game.hasUserWon() && !game.hasUserLost()) {
//                mazeGameThreadObj.getMainControl().stopThreads();
//            }
        }
        long id = nextId.incrementAndGet();
        MazeGameThreadObj mazeGameThreadObj = new MazeGameThreadObj(mazeGame, id);
        mazeGameThreadObjs.add(mazeGameThreadObj);
        ApiGameWrapper copy = ApiGameWrapper.processMaze(mazeGame, id);

        lock.unlock();
        return copy;
    }

//    @GetMapping("/games")
//    public List<ApiGameWrapper> getGames() {
//        return gameWrappers;
//    }

    @GetMapping("games/{id}")
    public ApiGameWrapper getGame(@PathVariable("id") long gameId) {
        log.debug("getGame called with gameId " + gameId);
        computeLock.lock();
        for (MazeGameThreadObj mazeGameThreadObj : mazeGameThreadObjs) {
            if (mazeGameThreadObj.gameNumber == gameId) {
                ApiGameWrapper copy = ApiGameWrapper.processMaze(mazeGameThreadObj.getSingleUserGame(), gameId);
                computeLock.unlock();
                return copy;
            }
        }
        computeLock.unlock();
        throw new ResourceNotFoundException(String.format("gane number %d does not exist", gameId));
    }

    @GetMapping("/games/{id}/board")
    public ApiBoardWrapper getBoard(@PathVariable("id") int id) {
        log.debug("getBoard called with id" + id);
        computeLock.lock();
        for (MazeGameThreadObj mazeGameThreadObj : mazeGameThreadObjs) {
            if (mazeGameThreadObj.gameNumber == id) {
//                ApiBoardWrapper copy = ApiGameWrapper.apiBoardWrapper.processMaze();
                ApiBoardWrapper copy = ApiBoardWrapper.processMaze(mazeGameThreadObj.getSingleUserGame());
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
        log.debug("makeMove called with gameId" + gameId);
        for(MazeGameThreadObj mazeGameThreadObj : mazeGameThreadObjs) {
            if(mazeGameThreadObj.gameNumber == gameId){
                mazeGameThreadObj.getControlMain().move(newMove);
                return;
            }
        }
        throw new ResourceNotFoundException(String.format("game number %d does not exist", gameId));
    }

    @PutMapping("games/{id}/increaseSpeed")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void increaseCatSpeed(@PathVariable("id") int id) {
        for(MazeGameThreadObj mazeGameThreadObj : mazeGameThreadObjs) {
            if(mazeGameThreadObj.gameNumber== id){
                mazeGameThreadObj.getControlMain().decreaseTimeInterval();
                return;
            }
        }
    }

    @PutMapping("games/{id}/decreaseSpeed")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void decreaseCatSpeed(@PathVariable("id") int id) {
        for(MazeGameThreadObj mazeGameThreadObj : mazeGameThreadObjs) {
            if(mazeGameThreadObj.gameNumber== id){
                mazeGameThreadObj.getControlMain().increaseTimeInterval();
                return;
            }
        }
    }

    @PostMapping("games/{id}/cheatstate")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void showBoard(@PathVariable("id") int id,
            @RequestBody String newMove) {
        for(MazeGameThreadObj mazeGameThreadObj : mazeGameThreadObjs) {
            if(mazeGameThreadObj.gameNumber== id){
                if (newMove.equals("SHOW_ALL")) {
                    mazeGameThreadObj.getControlMain().revealBoard();
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
