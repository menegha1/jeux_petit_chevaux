package fr.dodo.lp25.ludo;
import java.util.ArrayList;
import java.util.Random;

import javafx.scene.layout.GridPane;

public class Game {

	private ArrayList<Player> players = new ArrayList<Player>();
	private Player currentPlayer;
	private Board board;
	private int dice;

	public Game() {
		players = null;
		currentPlayer = null;
		board = null;
	}

	public Game(ArrayList<Player> p, Player cP, Board b) {
		players = p;
		currentPlayer = cP;
		board = b;
	}

	public Player getCurrentPlayer() {
		return this.currentPlayer;
	}

	public void setCurrentPlayer(Player newCurrentPlayer) {
		this.currentPlayer = newCurrentPlayer;
	}

	public Board getBoard() {
		return this.board;
	}

	public void setBoard(Board newBoard) {
		this.board = newBoard;
	}

	public ArrayList<Player> getPlayers() {
		return this.players;
	}

	public int getDice() {
		return this.dice;
	}

	public void setDice(int dice) {
		this.dice = dice;
	}

	// initialize all the player of the game (default name that will change at the
	// beginning of the game)
	public void initializePlayer() {
		ColorMapperEnum[] tabColor = { ColorMapperEnum.RED, ColorMapperEnum.GREEN, ColorMapperEnum.YELLOW,
				ColorMapperEnum.BLUE };

		players.add(new Player("Player 0", tabColor[0], new ArrayList<Pawn>(), 0, 1, 51));
		players.add(new Player("Player 1", tabColor[1], new ArrayList<Pawn>(), 1, 14, 12));
		players.add(new Player("Player 2", tabColor[2], new ArrayList<Pawn>(), 2, 27, 25));
		players.add(new Player("Player 3", tabColor[3], new ArrayList<Pawn>(), 3, 40, 38));

		// initialize pawns and players and their initial places
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				players.get(i).getHorses().add(new Pawn(tabColor[i], j));
			}
		}
	}

	// initialize all the different square of the board
	public void initializeBoard() {
		ColorMapperEnum[] tabColor = { ColorMapperEnum.RED, ColorMapperEnum.GREEN, ColorMapperEnum.YELLOW,
				ColorMapperEnum.BLUE };

		for (int i = 0; i <= 3; i++) {
			board.getStableSquareList().add(new StableSquare(i, tabColor[i]));
		}

		for (int i = 1; i <= 52; i++) {
			board.getPathSquareList().add(new PathSquare(i));
		}

		for (int i = 0; i <= 3; i++) {
			for (int j = 1; j <= 6; j++) {
				board.getLadderSquareList().add(new LadderSquare(i * 10 + j, tabColor[i]));
			}
		}

		// Initialize pawns in the stable of each players
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				board.getStableSquareList().get(i).addPawn(players.get(i).getHorses().get(j));
			}
		}
	}
    public void getFirstPlayer()
    {
    	Random rand = new Random();
		ArrayList<Integer> playerRoll = new ArrayList<Integer>();
		int dice, max = 0, index = 0;
		for (int i = 0; i < 4; i++) {
			do {
				dice = rand.nextInt(6) + 1;
			}while(playerRoll.contains(dice));
			playerRoll.add(dice);
			System.out.println(" " + this.getPlayers().get(i).getName() + " play the number " + dice);
		}
		
		for (int i = 0; i < 4; i++) {
			if(playerRoll.get(i) > max) {
				max = playerRoll.get(i);
				index = i;
			}
		}
		
		this.setCurrentPlayer(this.getPlayers().get(index));
    }
	public boolean isFinish() {
		// the game is finish when 3 players on 4 have their 4 pawn in their ending
		// square
		int memory = 0;
		for (int i = 0; i < 4; i++) {
			if (board.getLadderSquareList().get(5 + 6 * i).getHorses().size() == 4) {
				memory++;
			}
		}
		return memory >= 3;
	}

	// is use to move all pawns towards their stable
	public void eatPawn(Square square, GridPane gridPane) {
		if (square.getHorses().size() != 0) {
			for (int i = 0; i <= square.getHorses().size(); i++) {
				int temp = 0;
				switch (square.getHorses().get(0).getColor()) {
				case RED:
					temp = 0;
					break;
				case GREEN:
					temp = 1;
					break;
				case YELLOW:
					temp = 2;
					break;
				case BLUE:
					temp = 3;
					break;
				}
				Pawn pawn = square.getHorses().get(0);
				board.getStableSquareList().get(temp).addPawn(pawn);
				board.updateGridScene(pawn.getCurrentlySquare(), gridPane, ((temp*4)+pawn.getId() ), board, this,pawn );
				square.getHorses().remove(0);
			}
		}
	}
	
	public Square entireTurn(Game game ,Pawn currentPawn,GridPane gridPane)
	{
		Square currentlySquare;
		
		if (currentPawn != null) {
			// c'est ici que je bouge le pion donc je pense aussi que c'est ici dans ce if qu'on met a jour l'affichage
			board.MovePawn(game, currentPawn, game.getCurrentPlayer(), game.getDice(),gridPane);
			// commande pour chopper l'ID case ou le pion du joueur va atterir
			currentlySquare = currentPawn.getCurrentlySquare();
		}else currentlySquare = null;
	 return currentlySquare; 
	}
	
}
