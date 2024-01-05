package ca.MazeGame.model;

import ca.MazeGame.MazeGames.MazeGame;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The position and motion control of a cat.
 */

@Data
@AllArgsConstructor
@Builder
public class Cat {
	private final MazeGame game;
	private CellLocation location;
	private Direction lastMove = Direction.NOT_MOVING;
	
	public Cat(MazeGame game, CellLocation location) {
		this.game = game;
		this.location = location;
	}

	public void doMove() {
		List<Direction> possibleMoves = getPossibleMoves();
		pickOKMove(possibleMoves);		
		// Once moved, the game will ask where the cat is, as needed.
	}

	private List<Direction> getPossibleMoves() {
		List<Direction> directions = new ArrayList<>();
		directions.add(Direction.MOVE_UP);
		directions.add(Direction.MOVE_DOWN);
		directions.add(Direction.MOVE_RIGHT);
		directions.add(Direction.MOVE_LEFT);
		
		// Have the cat try not to back-track unless needed by making the 
		// backtracking move (opposite last move) be the last one to try.
		Direction oppositeLastMove = lastMove.getOppositeMove();
		directions.remove(oppositeLastMove);
		Collections.shuffle(directions);
		directions.add(oppositeLastMove);
		return directions;
	}

	private void pickOKMove(List<Direction> directions) {
		for (Direction move : directions) {
			CellLocation targetLocation = location.getTargetLocation(move);
			if (game.isCellOpen(targetLocation)) {
				location = targetLocation;
				lastMove = move;
				return;
			}
		}
	}	
}
