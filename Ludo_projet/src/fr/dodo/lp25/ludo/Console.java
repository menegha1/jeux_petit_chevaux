package fr.dodo.lp25.ludo;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Console {

	
	public Console() {
		
	}
	public void playConsole(Board board,Game game)
	{
		Scanner input = new Scanner(System.in);
		String name = new String();
		int colorID = 0, red = 0, blue = 0, green = 0, yellow = 0;

		for (int i = 1; i < 5; i++) {
			System.out.print(" Player " + i + ", choose your name : ");
			name = input.next();
			System.out.println("\n What color do you want " + name + "?");

			do {
				if (red == 0) {
					System.out.println(" Enter '1' for RED");
				}

				if (green == 0) {
					System.out.println(" Enter '2' for GREEN");
				}

				if (yellow == 0) {
					System.out.println(" Enter '3' for YELLOW");
				}

				if (blue == 0) {
					System.out.println(" Enter '4' for BLUE");
				}

				System.out.print(" Enter your choice : ");
				colorID = input.nextInt();

				while (colorID == 1 && red == 1 || colorID == 2 && green == 1 || colorID == 3 && yellow == 1
						|| colorID == 4 && blue == 1) {
					System.out.println("\n Color already pick!");
					System.out.print(" Enter your choice : ");
					colorID = input.nextInt();
				}

				System.out.println("");

			} while (colorID < 1 || colorID > 4);

			switch (colorID) {
			case 1:
				red = 1;
				System.out.println(" Your color : RED\n");
				break;
			case 2:
				green = 1;
				System.out.println(" Your color : GREEN\n");
				break;
			case 3:
				yellow = 1;
				System.out.println(" Your color : YELLOW\n");
				break;
			case 4:
				blue = 1;
				System.out.println(" Your color : BLUE\n");
				break;
			}
			game.getPlayers().get(colorID - 1).setName(name);

		}
	
		System.out.println("\n Resume of players :\n");
		for (Player i : game.getPlayers()) {
			System.out.println(i);
		}
		System.out.println("\n\n");
     
		
		// each player roll the die to know who begin
		Random rand = new Random();
		ArrayList<Integer> playerRoll = new ArrayList<Integer>();
		int dice, max = 0, index = 0;
		for (int i = 0; i < 4; i++) {
			do {
				dice = rand.nextInt(6) + 1;
			}while(playerRoll.contains(dice));
			playerRoll.add(dice);
			System.out.println(" " + game.getPlayers().get(i).getName() + " play the number " + dice);
		}
		
		for (int i = 0; i < 4; i++) {
			if(playerRoll.get(i) > max) {
				max = playerRoll.get(i);
				index = i;
		}
		}
		
		game.setCurrentPlayer(game.getPlayers().get(index));
		
		
		System.out.println("\n " + game.getPlayers().get(index).getName() + " start the game!");

		// game start
		Pawn currentPawn;
		int turn = 1;
		do {
			System.out.println("\n\n\n\n ////////////  TURN : " + turn + " ////////////");
			board.showBoard(game);
			System.out.println("\n\n Current Player : n° " + game.getCurrentPlayer().getId());
			game.setDice(rand.nextInt(6) + 1); // roll the dice
			System.out.println(" Dice : " + game.getDice());

			if (game.getDice() == 6) {
				// player can replay
				index--;
			}

			// if a pawn is choose correctly, it's move
			currentPawn = game.getCurrentPlayer().choosePawnConsole(board, game.getDice());
			if (currentPawn != null) {
				// c'est ici que je bouge le pion donc je pense aussi que c'est ici dans ce if qu'on met a jour l'affichage
				board.MovePawn(game, currentPawn, game.getCurrentPlayer(), game.getDice(),null);
				// commande pour chopper l'ID case ou le pion du joueur va atterir
				currentPawn.getCurrentlySquare().getNumber();
			}

			// change player
			index = (index + 1) % 4;
			game.setCurrentPlayer(game.getPlayers().get(index));
			turn++;
		} while (!game.isFinish()); // check if the game is finish

		System.out.println("\n\n\n GAME IS FINISH!");
		input.close();
	}
}
