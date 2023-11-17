package ca.MazeGame.controllers;

import ca.MazeGame.MazeGames.MazeGame;
import ca.MazeGame.MazeGames.MultiPlayerMazeGame;
import ca.MazeGame.MultiPlayersThreads.MultiPLayerMazeGameThreadObj;
import ca.MazeGame.Wrappers.MultiPlayerApiBoardWrapper;
import ca.MazeGame.Wrappers.MultiPlayerApiGameWrapper;
import ca.MazeGame.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

@RestController
@RequestMapping("/api")
public class MultiPlayerGameController {
    private final List<MultiPLayerMazeGameThreadObj> mazeGameThreadsListObjs = new ArrayList<>();

    private AtomicLong nextId = new AtomicLong();
    ReentrantLock computeLock = new ReentrantLock();
    ReentrantLock moveLock = new ReentrantLock();

    @PostMapping("/multiPlayerGames")
    @ResponseStatus(HttpStatus.CREATED)
    public MultiPlayerApiGameWrapper postNewMultiPlayerGame() {
        ReentrantLock lock = new ReentrantLock();
        lock.lock();

        MultiPlayerMazeGame mazeGame = new MultiPlayerMazeGame();
        if (mazeGameThreadsListObjs.size() != 0) {
            MultiPLayerMazeGameThreadObj multiPLayerMazeGameThreadObj =  mazeGameThreadsListObjs.get(mazeGameThreadsListObjs.size() - 1);
//            MazeGame game = multiPLayerMazeGameThreadObj.getMultiPlayerMazeGame();
//            if (!game.hasUserWon() && !game.hasUserLost()) {
//                multiPLayerMazeGameThreadObj.getMultiPlayersMainControl().stopThreads();
//            }
        }
        long id = nextId.incrementAndGet();
        MultiPLayerMazeGameThreadObj multiPLayerMazeGameThreadObj = new MultiPLayerMazeGameThreadObj(mazeGame, id);
        mazeGameThreadsListObjs.add(multiPLayerMazeGameThreadObj);
        MultiPlayerApiGameWrapper copy = MultiPlayerApiGameWrapper.processMaze(mazeGame, id);

        lock.unlock();
        return copy;
    }


    @GetMapping("multiPlayerGames/{id}")
    public MultiPlayerApiGameWrapper getMultiPlayerGame(@PathVariable("id") long gameId) {
        computeLock.lock();
        for (MultiPLayerMazeGameThreadObj multiPLayerMazeGameThreadObj : mazeGameThreadsListObjs) {
            if (multiPLayerMazeGameThreadObj.gameNumber == gameId) {
                MultiPlayerApiGameWrapper copy =
                        MultiPlayerApiGameWrapper.processMaze(multiPLayerMazeGameThreadObj.getMultiPlayerMazeGame(), gameId);
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
        for (MultiPLayerMazeGameThreadObj multiPLayerMazeGameThreadObj : mazeGameThreadsListObjs) {
            if (multiPLayerMazeGameThreadObj.gameNumber == id) {
                MultiPlayerApiBoardWrapper copy = MultiPlayerApiBoardWrapper.processMaze(multiPLayerMazeGameThreadObj.getMultiPlayerMazeGame(), id);
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
        for(MultiPLayerMazeGameThreadObj multiPLayerMazeGameThreadObj : mazeGameThreadsListObjs) {
            if(multiPLayerMazeGameThreadObj.gameNumber== gameId){
                multiPLayerMazeGameThreadObj.getMultiPlayersControlMain().move(newMove);
                return;
            }
        }
        throw new ResourceNotFoundException(String.format("game number %d does not exist", gameId));
    }
}
