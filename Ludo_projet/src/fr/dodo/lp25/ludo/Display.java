package fr.dodo.lp25.ludo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Display {

	private int numberPlayer;
	ArrayList<Integer> colorValue = new ArrayList<Integer>();
	private int tour;
	private Label player = new Label();
	private GridPane gridDisplay = new GridPane();
	private Square square = new PathSquare();
	private Pawn currentPawn;

	public Display() {

	}

	public void setGridPane(GridPane gridDisplay) {
		this.gridDisplay = gridDisplay;
	}

	public GridPane getGridPane() {
		return this.gridDisplay;
	}

	//Creation of the first scene
	public Scene StartScene(Stage stage, Board board, Game game) {
		numberPlayer = 1;
		
		Button buttonDisplay = new Button(" Click to start a game with Display");
		Button buttonConsole = new Button(" Click to start a game with Console");
		VBox vbox = new VBox(50);
		Scene scene = new Scene(vbox, 800, 650);

		Label label = new Label();
		label.setFont(new Font(40));
		label.setText("WELCOME IN THE LUDO GAME ");
		label.setAlignment(Pos.CENTER);
		label.setMinSize(800, 300);

		HBox button = new HBox(20);
		
		// if we click on the buttons , we will trigger the next step
		buttonDisplay.setFont(new Font(20));
		buttonDisplay.setMinWidth(250);
		buttonDisplay.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				stage.setScene(ChosePlayer(stage, numberPlayer, board, game));
			}
		});
		buttonConsole.setFont(new Font(20));
		buttonConsole.setMinWidth(250);
		buttonConsole.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
			    stage.close();
				Console console = new Console();
				console.playConsole(board, game);
			}
		});
		button.getChildren().addAll(buttonDisplay,buttonConsole);
		button.setAlignment(Pos.CENTER);
    
		vbox.getChildren().addAll(label, button);
		vbox.setAlignment(Pos.CENTER);

		return scene;
	}

	//Creation of the scene where players will select their names and colors
	public Scene ChosePlayer(Stage stage, int number, Board board, Game game) {
		tour = -1;
		Alert alert = new Alert(AlertType.INFORMATION);
		
		alert.setHeaderText(null);
		Button button = new Button();
		VBox pane = new VBox(50);
		HBox chosePane = new HBox(10);
		Scene choseScene = new Scene(pane, 800, 650);
		//TOP SIDE
		Label name = new Label(" Player n ° " + number);
		name.setFont(new Font(20));

		// MIDDLE SIDE

		
		Label player = new Label(" Chose your name : ");
		player.setFont(new Font(17));
		TextField textArea = new TextField();

		
		Label color = new Label(" Chose your color: ");
		color.setFont(new Font(17));
		chosePane.setAlignment(Pos.CENTER_LEFT);
		// Spinner to chose the number of his color
		Spinner<Integer> spinnerColor = new Spinner<Integer>(1, 4, 1);
		spinnerColor.setMinSize(10, 10);
		// List of the color
		
		VBox choseColor = new VBox(10);
		ColorMapperEnum[] tabColorMapper = { ColorMapperEnum.RED, ColorMapperEnum.GREEN, ColorMapperEnum.YELLOW,
				ColorMapperEnum.BLUE };
		Color[] tabColor = { Color.RED, Color.GREEN, Color.YELLOW, Color.BLUE };

		for (int i = 0; i < 4; i++) {
			HBox redform = new HBox(10);
			Label color1 = new Label((i + 1) + " -> " + tabColorMapper[i].getTextToFind());
			color1.setFont(new Font(17));

			Rectangle rec = new Rectangle(20, 20, tabColor[i]);

			redform.getChildren().addAll(color1, rec);
			choseColor.getChildren().addAll(redform);
		}
		choseColor.setAlignment(Pos.CENTER);
		chosePane.getChildren().addAll(player, textArea, color, spinnerColor, choseColor);

		// BOTTOM SIDE
		button.setText(" Validate your selection");
		button.setMinSize(200, 60);
		button.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				final int finalI = (int) spinnerColor.getValue();
				final String finalS = textArea.getText();
				// alert if  two players selected the same color
				if( colorValue.contains(finalI))
				{
					alert.setContentText(" Vous avez selectionne la meme couleur ");
					alert.showAndWait();
					stage.setScene(ChosePlayer(stage, numberPlayer, board, game));
				}else {
				colorValue.add(finalI);
				if (numberPlayer < 4) {
					//we repeat it for the 4 players
					game.getPlayers().get((finalI-1)).setName(finalS);
					numberPlayer++;
					stage.setScene(ChosePlayer(stage, numberPlayer, board, game));
					
					
				} else {
					// then we show the next Scene
					game.getPlayers().get((finalI-1)).setName(finalS);
					game.getFirstPlayer();
					Random rand = new Random();
					game.setDice(rand.nextInt(6) + 1);
					game.setDice(6);
					stage.setScene(boardScene(stage, board, gridDisplay, game));
				}
			  }
			}
		});

		// FIN
		pane.getChildren().addAll(name, chosePane, button);
		pane.setAlignment(Pos.CENTER);

		return choseScene;

	}
	
	
// Creation of the boardScene
	public Scene boardScene(Stage stage, Board board, GridPane gridDisplay, Game game) {
		tour++;
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setHeaderText(null);
		//announce the first player
        if(tour == 0)
        {
        	 alert.setContentText(" The player who start is :" + game.getCurrentPlayer().getName());
        	 alert.showAndWait();
        }
		int index = game.getCurrentPlayer().getId();
		VBox vbox = new VBox(10);
		Scene scene = new Scene(vbox, 800, 650);

		List<Button> numbers = new ArrayList<Button>();
        HBox bottom = board.bottomScene(numbers, player, game);
        
		player.setText(" Players : " + game.getCurrentPlayer().getName() + "\n("+ game.getCurrentPlayer().getColor().getTextToFind()+") "+" \n tour " + tour);
		VBox.setMargin(gridDisplay, new Insets(0, 0, 0, 125));
		vbox.getChildren().addAll(gridDisplay, bottom);
  // we set  the events when we click on one of the button , to get the number corresponding to the button
		for (int i = 0; i < 4; i++) {
			final int finalCircle = (index * 4 + i);
			final int finalI = i;
			final int indexI = index;
			final int finalT = tour ;
			numbers.get(i).setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent e) {
					System.out.println(" valeur index " + index);
					// We reapeat this Scene while the game is not finished
					if(!game.isFinish())
					{
						//we check if the player can chose this Pawn
					  if (!game.getCurrentPlayer().choosePawnDisplay(board, game.getDice(), finalI, game, alert)) {
						
						// if not , we reapeat the Scene with the same player
						stage.setScene(boardScene(stage, board, gridDisplay, game));
					   } else {
                       // else we reapeat the scene with  the pawn which has moved
						currentPawn = game.getCurrentPlayer().getHorses().get(finalI);
						square = game.entireTurn(game, currentPawn,gridDisplay);

						if (game.getDice() == 6) {
                            // the player will play again
							game.setCurrentPlayer(game.getPlayers().get(indexI));
						} else {
							game.setCurrentPlayer(game.getPlayers().get((indexI + 1) % 4));
						}

						Random rand = new Random();
						game.setDice(rand.nextInt(6) + 1);
						
						stage.setScene(boardScene(stage, board,board.updateGridScene(square, gridDisplay, finalCircle, board,game,currentPawn), game));
					   }
					}else {
					  stage.setScene(EndScene());
					}
				}
			});
		}
		return scene;
	}
	//Creation of the last Scene
	public Scene EndScene()
	{
		
		VBox root = new VBox(40);
		
		
		Label endLabel = new Label("THE GAME IS FINISH !!!");
		endLabel.setFont(new Font(50));
		
		Button endButton = new Button(" Click here to leave");
		endButton.setFont(new Font(20));
		endButton.setOnAction(e -> {
			// leave the game when we click on it 
		               Platform.exit(); 
        	 }
      		);
		
		root.getChildren().addAll(endLabel,endButton);
		root.setAlignment(Pos.CENTER);
		
		Scene endScene = new Scene(root,800,650);
		
		
		
		return endScene;
	}
}
