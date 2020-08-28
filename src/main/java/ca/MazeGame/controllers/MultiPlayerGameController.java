package ca.MazeGame.controllers;

import ca.MazeGame.MazeGames.MazeGame;
import ca.MazeGame.MazeGames.MultiPlayerMazeGame;
import ca.MazeGame.Wrappers.MultiPlayerApiBoardWrapper;
import ca.MazeGame.Wrappers.MultiPlayerApiGameWrapper;
import ca.MazeGame.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

@RestController
@RequestMapping("/api")
public class MultiPlayerGameController {
    private AtomicLong nextId = new AtomicLong();
    private List<MultiPlayerApiGameWrapper> multiPlayerGameWrappers = new ArrayList<>();
    ReentrantLock computeLock = new ReentrantLock();
    ReentrantLock moveLock = new ReentrantLock();

    @PostMapping("/multiPlayerGames")
    @ResponseStatus(HttpStatus.CREATED)
    public MultiPlayerApiGameWrapper postNewMultiPlayergame() throws SocketException {
        MultiPlayerMazeGame mazeGame = new MultiPlayerMazeGame();
        if (multiPlayerGameWrappers.size() != 0) {
            MultiPlayerApiGameWrapper apiGameWrapper =  multiPlayerGameWrappers.get(multiPlayerGameWrappers.size() - 1);
            MazeGame game = apiGameWrapper.getMultiPlayerMazeGame();
            if (!game.hasUserWon() && !game.hasUserLost()) {
                apiGameWrapper.setThreadStop(true);
            }
        }
        MultiPlayerApiGameWrapper multiPlayerApiGameWrapper = new MultiPlayerApiGameWrapper(mazeGame, nextId.incrementAndGet());
        multiPlayerGameWrappers.add(multiPlayerApiGameWrapper);
        Thread myThread = new Thread(multiPlayerApiGameWrapper);
        myThread.start();
        return multiPlayerApiGameWrapper.processMaze();
    }


    @GetMapping("multiPlayerGames/{id}")
    public MultiPlayerApiGameWrapper getMultiPlayerGame(@PathVariable("id") long gameId) {
        computeLock.lock();
        for (MultiPlayerApiGameWrapper multiPlayerApiGameWrapper : multiPlayerGameWrappers) {
            if (multiPlayerApiGameWrapper.gameNumber == gameId) {
                MultiPlayerApiGameWrapper copy = multiPlayerApiGameWrapper.processMaze();
                computeLock.unlock();
                return copy;
            }
        }
        computeLock.unlock();
        throw new ResourceNotFoundException(String.format("gane number %d does not exist", gameId));
    }


    @GetMapping("/multiPlayerGames/{id}/board")
    public MultiPlayerApiBoardWrapper getMultiPlayerGameBoard(@PathVariable("id") int id) {
        computeLock.lock();
        for (MultiPlayerApiGameWrapper multiPlayerApiGameWrapper : multiPlayerGameWrappers) {
            if (multiPlayerApiGameWrapper.gameNumber == id) {
                MultiPlayerApiBoardWrapper copy = multiPlayerApiGameWrapper.apiBoardWrapper.processMaze();
                computeLock.unlock();
                return copy;
            }
        }
        computeLock.unlock();
        throw new ResourceNotFoundException(String.format("board number %d does not exist", id));
    }


    @PostMapping("multiPlayerGames/{id}/moves")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void makeMultiPlayerMove(@PathVariable("id") int gameId,
                                    @RequestBody String newMove) {
//        moveLock.lock();
        for(MultiPlayerApiGameWrapper multiPlayerApiGameWrapper : multiPlayerGameWrappers) {
            if(multiPlayerApiGameWrapper.gameNumber== gameId){
                multiPlayerApiGameWrapper.move(newMove);
//                moveLock.unlock();
                return;
            }
        }
//        moveLock.unlock();
        throw new ResourceNotFoundException(String.format("game number %d does not exist", gameId));
    }
}
