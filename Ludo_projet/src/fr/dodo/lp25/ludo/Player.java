package fr.dodo.lp25.ludo;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import javafx.scene.control.Alert;

public class Player {
	private String name;
	private ColorMapperEnum color;
	private List<Pawn> horses = new ArrayList<Pawn>();
	private int id, startingSquare, endSquare;

	Player() {
		name = "Player1";
		color = ColorMapperEnum.BLUE;
		horses = null;
		id = 0;
		startingSquare = 0;
		endSquare = 0;
	}

	Player(String n, ColorMapperEnum c, List<Pawn> h, int i, int s, int e) {
		name = n;
		color = c;
		horses = h;
		id = i;
		startingSquare = s;
		endSquare = e;
	}

	public int getStartingSquare() {
		return this.startingSquare;
	}

	public void setStartingSquare(int newStartingSquare) {
		this.startingSquare = newStartingSquare;
	}

	public int getEndSquare() {
		return this.endSquare;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String newName) {
		this.name = newName;
	}

	public ColorMapperEnum getColor() {
		return this.color;
	}

	public void setColor(ColorMapperEnum newColor) {
		this.color = newColor;
	}

	public List<Pawn> getHorses() {
		return this.horses;
	}

	public int getId() {
		return this.id;
	}

	public Pawn choosePawnConsole(Board board, int dice) {
		// this function ask the player what pawn he wants to play and check if this
		// pawn can be played
		Pawn pawn;
		// We begin by checking if there is at least 1 pawn that can move
		int temp = 0;
		for (int i = 0; i < 4; i++) {
			pawn = this.horses.get(i);
			if (board.getStableSquareList().get(this.id).getHorses().contains(pawn) && dice != 6
					|| board.CanMovePawn(pawn, this.endSquare, dice, false) == false) {
				temp++;
			}
		}

		if (temp == 4) {
			System.out.println(" no pawn can be played...");
			return null;
		}

		// at this point, at leats 1 pawn can move
		// we ask the player to choose 1 pawn and we check if it can be played
		Scanner scanner = new Scanner(System.in);
		int input = 0;
		do {
			do {
				do {
					System.out.print("\n\n Which pawn do you want : ");
					input = scanner.nextInt();
					if (input < 0 || input > 3) {
						System.out.print(" The ID must be in [0,3]...");
					}

				} while (input < 0 || input > 3); // is the input corresponding to 1 pawn ?

				pawn = this.horses.get(input);
				if (board.getStableSquareList().get(this.id).getHorses().contains(pawn) && dice != 6) {
					System.out.print(" Pawn in the stabblesquare, but you haven't roll a 6...");
				}
			} while (board.getStableSquareList().get(this.id).getHorses().contains(pawn) && dice != 6);

			// is the pawn in the stablesquare and the dice different of 6 ?
			if (board.CanMovePawn(pawn, this.endSquare, dice, false) == false) {
				System.out.print(" Pawn can't move");
			}
		} while (board.CanMovePawn(pawn, this.endSquare, dice, false) == false);
		if (!board.getStableSquareList().get(this.id).getHorses().contains(pawn)) {
			board.CanMovePawn(pawn, this.endSquare, dice, true);
		} else {
			System.out.print(" Pawn can move in square: " + startingSquare);
		}

		return pawn;
	}
//We create a new method to chose the pawn in according to the display
	public boolean choosePawnDisplay(Board board, int dice , int number, Game game, Alert alert) {
	
		Pawn pawn;

        List<Pawn> stablePlayer = board.getStableSquareList().get(this.id).getHorses();
		int temp = 0;
		for (int i = 0; i < 4; i++) {
			pawn = this.horses.get(i);
			if (stablePlayer.contains(pawn) && dice != 6
					|| board.CanMovePawn(pawn, this.endSquare, dice, false) == false) {
				temp++;
			}
		}
		// we check if  all the pawn are in the StableSquare

		if (temp == 4) {
			System.out.println(" no pawn can be played...");
			alert.setContentText(" no pawn can be played");
			//a revoir
			game.setCurrentPlayer(game.getPlayers().get((game.getCurrentPlayer().getId()+1)%4));
			  Random rand = new Random();
	         game.setDice(rand.nextInt(6)+1);
			
			return false;
		}

		int input = 0;
                input = number;
				pawn = this.horses.get(input);
				//We check if the paws is in his StableSquare when the value of the dice is lower than 6
				if (stablePlayer.contains(pawn) && dice != 6) {
					alert.setContentText( "  Pawn in the stabblesquare, but you haven't roll a 6...");
					alert.show();
					return false;
				}
		
				//We check if the pawn can move
	   if (board.CanMovePawn(pawn, this.endSquare, dice, false) == false) {
				alert.setContentText(" Pawn  cant move");
				alert.show();
				return false;
		}
		if (!stablePlayer.contains(pawn)) {
			board.CanMovePawn(pawn, this.endSquare, dice, true);
		} else {
			System.out.print(" Pawn can move in square: " + startingSquare);
			alert.setContentText(" Pawn can move in square : " + startingSquare);
			return true;
		}

		return true;
	}

	public String toString() {
		return " Name: " + this.name + "\n Color: " + this.color;
				
	}
}
