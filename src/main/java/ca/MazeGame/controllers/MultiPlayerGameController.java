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

@RestController
@RequestMapping("/api")
public class MultiPlayerGameController {
    private AtomicLong nextId = new AtomicLong();
    private List<MultiPlayerApiGameWrapper> multiPlayerGameWrappers = new ArrayList<>();



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
        for (MultiPlayerApiGameWrapper multiPlayerApiGameWrapper : multiPlayerGameWrappers) {
            if (multiPlayerApiGameWrapper.gameNumber == gameId) {
                return multiPlayerApiGameWrapper.processMaze();
            }
        }
        throw new ResourceNotFoundException(String.format("gane number %d does not exist", gameId));
    }


    @GetMapping("/multiPlayerGames/{id}/board")
    public MultiPlayerApiBoardWrapper getMultiPlayerGameBoard(@PathVariable("id") int id) {
        for (MultiPlayerApiGameWrapper multiPlayerApiGameWrapper : multiPlayerGameWrappers) {
            if (multiPlayerApiGameWrapper.gameNumber == id) {
                return multiPlayerApiGameWrapper.apiBoardWrapper.processMaze();
            }
        }
        throw new ResourceNotFoundException(String.format("board number %d does not exist", id));
    }


    @PostMapping("multiPlayerGames/{id}/moves")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void makeMultiPlayerMove(@PathVariable("id") int gameId,
                                    @RequestBody String newMove) {
        for(MultiPlayerApiGameWrapper multiPlayerApiGameWrapper : multiPlayerGameWrappers) {
            if(multiPlayerApiGameWrapper.gameNumber== gameId){
                multiPlayerApiGameWrapper.move(newMove);
                return;
            }
        }
        throw new ResourceNotFoundException(String.format("game number %d does not exist", gameId));
    }
}
