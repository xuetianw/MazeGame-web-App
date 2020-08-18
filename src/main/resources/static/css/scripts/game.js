"use strict";

// Handle back-end server interactions fro game UI.
const myAppObj = new Vue({
    el: "#gameApp",
    data: {
        authorName: "(waiting for server...)",
        // playersTurn: true,
        game: null,
        board: null,
    },

    methods: {
        newGame: makeNewGame,
        cheat1Cheese: send1CheeseCheat,
        cheatShowAll: sendShowAll,
        increaseCatSpeed:  sendIncreaseCatSpeed,
        decreaseCatSpeed:  sendDecreaseCatSpeed,
        multiplayer : startMultiPlayer,

        locationMatches: function(loc, x, y) {
            return loc.x === x && loc.y === y;
        },
    }
});

// Have Axios send body as plain text (not JSON) when this config is passed to a POST request.
var plainTextConfig = {
    headers: { 'Content-Type': 'text/plain'},
    responseType: 'text'
};

// Handle arrow keys
window.addEventListener('keydown', function(e) {
    console.log("Key pressed: " + e.keyCode);
    switch(e.keyCode) {
        case 37: sendMove("MOVE_LEFT"); break;
        case 38: sendMove("MOVE_UP"); break;
        case 39: sendMove("MOVE_RIGHT"); break;
        case 40: sendMove("MOVE_DOWN"); break;
    }
});

// Refresh UI at start
$(document).ready(function() {
    loadAbout();
    window.setInterval(function () {
        loadGame();
        loadGameBoard();
    }, 200);
});

function loadAbout() {
    axios.get('/api/about', {})
        .then(function (response) {
            console.log("GET About returned:", response);
            myAppObj.authorName = response.data;

            alertOnWrongStatus("GET about", 200, response.status);
        })
        .catch(function (error) {
            console.log("GET About ERROR:", error);
        });
}

function makeNewGame() {
    axios.post('api/games', {})
        .then(function (response) {
            // console.log("POST new game returned:", response);
            myAppObj.game = response.data;

            // myAppObj.playersTurn = true;
            // loadGameBoard();
            alertOnWrongStatus("POST games", 201, response.status);
        })
        .catch(function (error) {
            console.log("POST new game ERROR:", error);
        });
}

function loadGame() {
    axios.get('/api/games/' + myAppObj.game.gameNumber, {})
        .then(function (response) {
            // console.log("Load game returned:", response);
            myAppObj.game = response.data;

            alertOnWrongStatus("GET Game", 200, response.status);
        })
        .catch(function (error) {
            console.log("Load game ERROR: ", error);
        });
}
function loadGameBoard() {
    axios.get('/api/games/' + myAppObj.game.gameNumber + "/board", {})
        .then(function (response) {
            // console.log("Load Board returned: ", response);
            myAppObj.board = response.data;

            alertOnWrongStatus("GET board", 200, response.status);
            // console.log("game.secondPlayerNumCheeseFound" + game.secondPlayerNumCheeseFound)
        })
        .catch(function (error) {
            console.log("Load Board ERROR: ", error);
        });
}
function send1CheeseCheat() {
    axios.post('/api/games/' + myAppObj.game.gameNumber + "/cheatstate", "1_CHEESE", plainTextConfig)
        .then(function (response) {
            console.log("Cheat returned: ", response);
            loadGame();
            alertOnWrongStatus("POST Cheese Cheat", 202, response.status);
        })
        .catch(function (error) {
            console.log("Cheat ERROR: ", error);
        });
}
function sendShowAll() {
    axios.post('/api/games/' + myAppObj.game.gameNumber + "/cheatstate", "SHOW_ALL", plainTextConfig)
        .then(function (response) {
            console.log("Cheat returned: ", response);
            loadGameBoard();
            alertOnWrongStatus("POST Show All Cheat", 202, response.status);
        })
        .catch(function (error) {
            console.log("Cheat ERROR: ", error);
        });
}

function sendMove(directionStr) {
    if (myAppObj.game.isGameLost || myAppObj.game.isGameWon) {
        console.log("Unable to make move after game has ended.");
        return;
    }

    axios.post('/api/games/' + myAppObj.game.gameNumber + "/moves", directionStr, plainTextConfig)
        .then(function (response) {
            console.log("Move player returned:", response);
            // loadGameBoard();
            // loadGame();

            // Cats go next, after a moment
            // myAppObj.playersTurn = false;
            // setTimeout(sendMoveCats, 100);
            alertOnWrongStatus("POST Mouse Move", 202, response.status);
        })
        .catch(function (error) {
            // Did they bump the wall?
            if (error.response.status == 400) {
                console.log("Move player: hit the wall.");
                playSound();
            } else {
                console.log("Move player ERROR:", error);
            }
        });
}

// Source: https://www.w3schools.com/graphics/game_sound.asp
var mySound = new sound("BONK.WAV");
function sound(src) {
    this.sound = document.createElement("audio");
    this.sound.src = src;
    this.sound.setAttribute("preload", "auto");
    this.sound.setAttribute("controls", "none");
    this.sound.style.display = "none";
    document.body.appendChild(this.sound);
    this.play = function(){
        this.sound.play();
    }
    this.stop = function(){
        this.sound.pause();
    }
}
function playSound() {
    mySound.play();
}


function alertOnWrongStatus(description, expectedStatus, actualStatus) {
    if (actualStatus != expectedStatus) {
        alert("ERROR: Incorrect HTTP status returned for ["
            + description
            + "]; expected " + expectedStatus
            + " but server returned " + actualStatus)
    }
}

function sendIncreaseCatSpeed() {
    axios.put('/api/games/' + myAppObj.game.gameNumber + "/increaseSpeed")
        .then(function (response) {
            // console.log("Cat move returned: ", response);
        })
        .catch(function (error) {
            console.log("speed increase ERROR: ", error);
        });
    // console.log("sendIncreaseCatSpeed\n")
}

function sendDecreaseCatSpeed() {
    axios.put('/api/games/' + myAppObj.game.gameNumber + "/decreaseSpeed")
        .then(function (response) {
            // console.log("Cat move returned: ", response);
        })
        .catch(function (error) {
            console.log("speed increase ERROR: ", error);
        });
}

function startMultiPlayer() {
    axios.put('/api/games/' + myAppObj.game.gameNumber + "/multiPlayer")
        .then(function (response) {
        })
        .catch(function (error) {
            console.log("speed increase ERROR: ", error);
        });
}
