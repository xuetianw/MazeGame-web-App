package ca.MazeGame.controllers;

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
public class PledgeController {
    private List<Pledge> pledges = new ArrayList<>();
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
        ApiGameWrapper apiGameWrapper = new ApiGameWrapper(mazeGame, nextId.incrementAndGet());
        apiGameWrappers.add(apiGameWrapper);
        Thread myThread = new Thread(mazeGame);
        myThread.start();
        DUPListener dupListener = new DUPListener();
        Thread dupThread = new Thread(dupListener);
        dupThread.start();
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


    @PostMapping("/pledges")
    @ResponseStatus(HttpStatus.CREATED)
    public Pledge createNewPledge(@RequestBody Pledge pledge) {
        // Set pledge to have next ID:
        pledge.setId(nextId.incrementAndGet());

        pledges.add(pledge);
        return pledge;
    }

    @GetMapping("/pledges")
    public List<Pledge> getAllPledges() {
        return pledges;
    }

    @GetMapping("/pledges/{id}")
    public Pledge getOnePledge(@PathVariable("id") long pledgeId) {
        for (Pledge pledge : pledges) {
            if (pledge.getId() == pledgeId) {
                return pledge;
            }
        }

        throw new IllegalArgumentException();
    }

    @PostMapping("pledges/{id}")
    public Pledge editOnePledge(
            @PathVariable("id") long pledgeId,
            @RequestBody Pledge newPledge
    ) {
        for (Pledge pledge : pledges) {
            if (pledge.getId() == pledgeId) {
                pledges.remove(pledge);
                newPledge.setId(pledgeId);
                pledges.add(newPledge);
                return pledge;
            }
        }

        throw new IllegalArgumentException();
    }


    // Create Exception Handle
    @ResponseStatus(value = HttpStatus.BAD_REQUEST,
                reason = "Request ID not found.")
    @ExceptionHandler(IllegalArgumentException.class)
    public void badIdExceptionHandler() {
        // Nothing to do
    }
}
