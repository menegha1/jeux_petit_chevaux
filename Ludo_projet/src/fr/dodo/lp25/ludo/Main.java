package fr.dodo.lp25.ludo;
import java.util.ArrayList;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Main extends Application {

	private Stage stage;
	
	
	public void start(Stage primaryStage) {
		Board board = new Board(new ArrayList<Square>(), new ArrayList<StableSquare>(), new ArrayList<LadderSquare>());
		Game game = new Game(new ArrayList<Player>(), null, board);
		game.initializePlayer();
		game.initializeBoard();

	
    	Display display = new Display();
    	board.creationCircle(15);
    	GridPane gridDisplay = board.BoardDisplay(board);
    	display.setGridPane(gridDisplay);
   
    	stage = primaryStage;
        Scene scene = display.StartScene(stage,board,game);
        primaryStage.setScene(scene);
        primaryStage.show();
	}
}
