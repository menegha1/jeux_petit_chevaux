package fr.dodo.lp25.ludo;
import java.util.ArrayList;
import java.util.List;


import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;


public class Board {

	private ArrayList<Square> PathSquareList = new ArrayList<Square>();
	private ArrayList<StableSquare> StableSquareList = new ArrayList<StableSquare>();
	private ArrayList<LadderSquare> LadderSquareList = new ArrayList<LadderSquare>();
	private ArrayList<StackPane>  circles = new ArrayList<StackPane>();

	public Board() {
		PathSquareList = null;
		StableSquareList = null;
		LadderSquareList = null;
	}

	public Board(ArrayList<Square> p, ArrayList<StableSquare> s, ArrayList<LadderSquare> l) {
		PathSquareList = p;
		StableSquareList = s;
		LadderSquareList = l;
	}

	public ArrayList<StableSquare> getStableSquareList() {
		return this.StableSquareList;
	}

	public ArrayList<Square> getPathSquareList() {
		return this.PathSquareList;
	}

	public ArrayList<LadderSquare> getLadderSquareList() {
		return this.LadderSquareList;
	}

	// this function print in which square are all pawns of the game
	public void showBoard(Game game) {
		for (int i = 0; i < 4; i++) {
			System.out.println("\n " + game.getPlayers().get(i).getName() +"- " + game.getPlayers().get(i).getColor().getTextToFind());
			for (int j = 0; j < 4; j++) {
				Pawn pawn = game.getPlayers().get(i).getHorses().get(j);
				System.out.print(" Pawn n° " + j + " : ");
				if (StableSquareList.contains(pawn.getCurrentlySquare())) {
					System.out.print("  ");
				} else {
					if (game.getPlayers().get(i).getHorses().get(j).getCurrentlySquare().getNumber() < 10) {
						System.out.print(game.getPlayers().get(i).getHorses().get(j).getCurrentlySquare().getNumber());
						System.out.print(" ");
					} else {
						System.out.print(game.getPlayers().get(i).getHorses().get(j).getCurrentlySquare().getNumber());
					}
				}

				if (LadderSquareList.contains(pawn.getCurrentlySquare())) {
					System.out.println(" | Ladder");
				}
				if (PathSquareList.contains(pawn.getCurrentlySquare())) {
					System.out.println(" | Path");
				}
				if (StableSquareList.contains(pawn.getCurrentlySquare())) {
					System.out.println(" | Stable");
				}
			}
		}
	}

	// test if the pawn can move
	public boolean CanMovePawn(Pawn pawn, int endSquare, int dice, boolean print) {
		boolean bool = true; // indicate if the pawn can move
		boolean path = true; // pawn is in the PathSQuare
		boolean ladder = false; // pawn is in the LadderSQuare
		int player = 0;
		int currentSquare = pawn.getCurrentlySquare().getNumber() - 1;

		// check what player it is
		switch (pawn.getColor()) {
		case RED:
			player = 0;
			break;
		case GREEN:
			player = 1;
			break;
		case YELLOW:
			player = 2;
			break;
		case BLUE:
			player = 3;
			break;
		}

		// check if the pawn was already in a LadderSquare at the beginning (consider in
		// a PathSquare by default)
		if (this.LadderSquareList.contains(pawn.getCurrentlySquare())) {
			path = false;
			ladder = true;
			currentSquare = currentSquare - player * 4;
		}

		// this loop simulate a pawn's turn
		do {
			// check if the pawn goes to the LadderSquare
			if (currentSquare == endSquare - 1 && path) {
				path = false;
				ladder = true;
				currentSquare = -1 + player * 6;
			}

			currentSquare++;
			dice--;

			// check if we are in the LadderSquare
			if (ladder) {
				if (currentSquare == 24) {
					bool = false;
				} else {
					// check if the pawn can pass to the Square and if the the currentSquare is
					// the end or not
					if (this.LadderSquareList.get(currentSquare).CanPass(pawn) == false
							|| this.LadderSquareList.get(currentSquare).getNumber() / 10 != player) {
						bool = false;
					}

					if (this.LadderSquareList.get(currentSquare).CanStop(endSquare, pawn, dice) == false && dice == 0) {
						bool = false;
					}
				}

			}

			// check if we are in the PathSquare
			if (path) {
				if (currentSquare == 52) {
					currentSquare = 0;
				} else {
					// check if the pawn can pass to the Square
					if (this.PathSquareList.get(currentSquare).CanPass(pawn) == false) {
						// check if the pawn can stop to the Square
						if (this.PathSquareList.get(currentSquare).CanStop(0, pawn, dice) == false) {
							bool = false;
						}
					}
				}
			}

		} while (dice > 0 && bool == true);

		if (print) {
			if (path) {
				System.out.println(" Pawn can move in Pathsquare: " + (currentSquare + 1));
			}
			if (ladder) {
				System.out.println(" Pawn can move in Laddersquare: " + (currentSquare + player * 4 + 1));
			}
		}

		return bool;
	}

	// move the pawn after testing if the pawn can move with CanMovePawn
	public void MovePawn(Game game, Pawn pawn, Player player, int dice,GridPane gridPane) {
		int square = 0, temp = 0;
		boolean bool = false; // is use to test if the pawn is switching between Path and ladder during his
								// move

		// test if the pawn is on a LadderSquare
		if (LadderSquareList.contains(pawn.getCurrentlySquare())) {
			square = pawn.getCurrentlySquare().getNumber() - player.getId() * 4 - 1;
			LadderSquareList.get(square).removePawn(pawn);
			LadderSquareList.get(square + dice).addPawn(pawn);
		}

		// test if the pawn is on a pathSquare
		if (PathSquareList.contains(pawn.getCurrentlySquare())) {
			square = pawn.getCurrentlySquare().getNumber() - 1;
			PathSquareList.get(square).removePawn(pawn);

			for (int i = 0; i < dice; i++) {
				if (bool) {
					temp++;
				}
				if (square + i == player.getEndSquare() - 1 && i != dice) {
					bool = true;
					temp = player.getId() * 6;
				}
			}

			// check if the pawn is switching between Path and ladder during his move
			if (bool) {
				LadderSquareList.get(temp).addPawn(pawn);
			} else {
				if (!PathSquareList.get((square + dice) % 52).isSafezone()) {
					if (!PathSquareList.get((square + dice) % 52).getHorses().isEmpty() && PathSquareList
							.get((square + dice) % 52).getHorses().get(0).getColor() != pawn.getColor())
					{
						game.eatPawn(PathSquareList.get((square + dice) % 52),gridPane);
					}
				}
				PathSquareList.get((square + dice) % 52).addPawn(pawn);
			}
		}

		// test if the pawn is on a StableSquare
		if (StableSquareList.contains(pawn.getCurrentlySquare())) {
			StableSquareList.get(player.getId()).removePawn(pawn);
			PathSquareList.get(player.getStartingSquare() - 1).addPawn(pawn);
		}
	}
	
	public  void creationCircle(int radius)
	{
		Color[] tabColor = {Color.INDIANRED,Color.GREENYELLOW, Color.GOLD,Color.DEEPSKYBLUE };
		for(int i = 0; i<4 ; i++)
		{
			for(int j = 0; j<4;j++)
			{    
				Circle circle = new Circle(radius,tabColor[i]);
				Text text = new Text("" + (j+1) );
				text.setBoundsType(TextBoundsType.VISUAL);
				circle.setStroke(Color.BLACK);
				circle.setStrokeWidth(3);
				StackPane stack = new StackPane();
				stack.getChildren().addAll(circle,text);
				this.circles.add(stack);
		
			}
			
		}
	}
	
	public GridPane BoardDisplay(Board board)
	{
		GridPane grilleContenu = new GridPane();
        List<Rectangle> rectangles = new ArrayList<Rectangle>();
         
        for (int y = 0; y < 15; y++)
        {
		   for (int x = 0; x < 15; x++) {
			Rectangle rec = new Rectangle();
			rec.setWidth(35);
			rec.setHeight(35);
			rectangles.add(rec);

			if (( y <= 5 && x <= 5) ||  (y == 7 && (x >= 1 &&  x <=6 ) )||  (x== 1 && y == 6) ) 
			{
				rec.setFill(Color.GREEN); 
			}
			

			else if ((y <= 5 && x >= 9) || (x == 7 && (y >=1 && y <= 6)) || (y== 1 &&  x == 8))
			{
				rec.setFill(Color.YELLOW);
			}
			else if ((y >= 9 && x >= 9) || (y == 7 && (x < 14 && x > 7)) || (y==8 && x == 13))
		    {
				rec.setFill(Color.BLUE);
			}
			else if ((y >= 9 && x <= 5) || (x == 7 && (y <= 13 && y > 7)) || (x == 6 && y == 13)) 
			{
				rec.setFill(Color.RED);
			}
			else if ( (( x == 6 || x == 8 ) && ( y == 6 || y == 8 ) ) || ( x == 7 && y ==7) )
			{
				rec.setFill(Color.BLACK);
			}
			else rec.setFill(Color.WHITE);
			GridPane.setRowIndex(rec, y);
			GridPane.setColumnIndex(rec, x);
			grilleContenu.getChildren().addAll(rec);
		   }
         }
        for(int i = 0; i< 4; i++)
        {
     	   for(int j = 0; j < 4 ;j++)
     	   {
     	   StackPane circle = this.getStablePos(board.getStableSquareList().get(i), this.getCircles().get(i*4+j));
     	   circle.setLayoutX(j);
     	   grilleContenu.getChildren().add(circle);
     	   }
        }
        grilleContenu.setGridLinesVisible(true);
        return grilleContenu;
	}
	
	public ArrayList<StackPane> getCircles()
	{
		return this.circles;
	}
	//We create a method to get index of the column in the GridPane  which correspond to one square
	public int getXSquare(Square Square,Board board)
		{
			// PATHSQUARE
		if(board.getPathSquareList().contains(Square))
		{
			int i = Square.getNumber();
			if((i>0 && i<6) ||( (i>= 19) && (i <= 24) ) || i == 52) return 6;
			else if ((i>= 26 && i<= 31) || (i >= 45 && i <= 50) ) return 8;
			else if (i == 25 || i == 51) return 7;
			else if ( i== 38 ) return 14;
			else if ( i == 12 ) return 0;
			else if ( i>=13 && i<=18) return(i -13);
			else if (i>= 6 && i<= 11 ) return (11 - i);
			else if ( i>= 39 && i<= 44) return (9+(44-i));
			else if ( i >= 32 && i<= 37 ) return (9+(i- 32));
		}
			
			//LADDERSQUARE		
			
		if(board.getLadderSquareList().contains(Square))
		{
			int i = Square.getNumber();
			if( i >= 1 && i <= 6) return 7;
			else if( i >= 11 && i<= 16) return (i%10);
			else if(i >= 21 && i<= 26) return 7 ;
			else if (i >= 31 && i<= 36) return (14-(i%10));
		}
			return 0;
		}
	
	//We create a method to get index of the row in the GridPane  which correspond to one square
		public int getYSquare(Square Square, Board board)
		{
			//PATHSQUARE
		if( board.getPathSquareList().contains(Square))
		{
			int i = Square.getNumber();
			if((i>=13 && i<= 18) || ( i>= 32 && i<=	37)) return 6;
			else if ((i>=6 && i<=11) || (i>= 39 && i<=44)) return 8;
			else if (i == 12 || i== 38 )return 7;
			else if ( i == 25 ) return 0;
			else if (i == 51 || i == 52) return 14;
			else if (i>= 19 && i<= 24 ) return (24-i);
			else if ( i>= 26 && i<=  31 ) return ( i - 26);
			else if ( i >= 45 && i<= 50) return ( 9 +( i - 45));
			else if ( i >= 1 && i<= 5) return (9 + ( 5 - i));
		}
		
		// LADDERSQUARE
		if(board.getLadderSquareList().contains(Square))
		{
			int i = Square.getNumber();
			if( i >= 1 && i <= 6) return (14-i);
			else if( i >= 11 && i<= 16) return 7 ;
			else if(i >= 21 && i<= 26) return (i%10);
			else if (i >= 31 && i<= 36) return 7;
		}
		return 0;
			
		}
		
		//we create a method to get to move a pawn in his stableSquare
		public StackPane getStablePos(Square square, StackPane stack)
		{
		 int x = 0;
		 int y =0;
		 int circleX =0;
		 int circleY = 0;
		 int j = this.getCircles().indexOf(stack);
		 int i = square.getNumber();
		if(i== 0) 
    	{   y=11;
    		x=2;
    	}
    	else if (i==1)
    	{  y = 2;
   		   x= 2;}
    	else if (i == 2)
    	{	x=11;
    		y=2;}
    	else if ( i == 3)
    	{	x=11;
    	    y=11;}
    	if((j%4)== 0)
    		{   circleX = x ;
    			circleY = y;}
    	else if((j%4)==1)
    		{   circleX = x+1;
    			circleY = y;}
    	else if((j%4)==2)
    		{   circleX = x ;
    			circleY = y+1;}
    	else if((j%4)==3)
    		{ 	circleX = x+1;
    			circleY = y+1;}
    	 GridPane.setRowIndex(stack,circleY);
	     GridPane.setColumnIndex(stack,circleX);
	     GridPane.setHalignment(stack, HPos.CENTER);
	     
	     return stack;
		}
	
	// This method allows to modificate the place of the pawn in according to the a square
	public GridPane updateGridScene(Square currentlysquare, GridPane boardDisplay,int number, Board board,Game game, Pawn currentPawn)
	{
    	
    	
		
        StackPane currentlyStack = this.getCircles().get(number);
        // we check if the square is a stablesquare
        if(board.getStableSquareList().contains(currentlysquare) == true)
    	{
        	
        	board.getStablePos(currentlysquare, currentlyStack);
        	
    	}else {
    	
     // if not we  got the index of the column and the row of the pawn in according to the square
        int circleX = this.getXSquare(currentlysquare,board);
        int circleY =  this.getYSquare(currentlysquare,board);
        GridPane.setColumnIndex(currentlyStack, circleX);
        GridPane.setRowIndex(currentlyStack, circleY);
       // if two pawns are on the same square , we modificate the size of the pawn, to see the two different pawn
        for(int i=0;i<4;i++)
        {
        	Pawn pawn = game.getCurrentPlayer().getHorses().get(i);
        	if(!board.getStableSquareList().contains(currentlysquare) && (pawn != currentPawn) && pawn.getCurrentlySquare() == currentPawn.getCurrentlySquare())
                       {
	                      StackPane stack1 = this.getCircles().get((game.getCurrentPlayer().getId()*4)+currentPawn.getId());
	                      StackPane stack2 = this.getCircles().get((game.getCurrentPlayer().getId()*4)+ pawn.getId());
	                      stack1.setMinSize(10,10);
	                      stack2.setMinSize(10, 10);
	                      GridPane.setHalignment(stack1, HPos.LEFT);
	                      GridPane.setHalignment(stack2, HPos.RIGHT);
                         }
        }      	
       }
      return  boardDisplay;
	}
	
	//we create the bottom side of the BoardScene 
	
	public HBox bottomScene(List<Button> numbers, Label player,Game game)
	{ 
	    HBox bottom = new HBox(20);
	  
	        player.setFont(new Font(20));
	        
	        // button to show the number 
	        Button diceButton = new Button("Click to Roll");
	        diceButton.setFont(new Font(15));
	        diceButton.setMinHeight(50);
	        
	       
	        Label dice = new Label("");
	        dice.setFont(new Font(30));
	        dice.setAlignment(Pos.TOP_CENTER);
	        // action if we click on it 
	        diceButton.setOnAction(e -> {
	  
	           	dice.setText("" + game.getDice() );
	        	 diceButton.setDisable(true);	
	        	 }
	      		);
	        
	        Label choiceNumber = new Label("Which pawn do you want ?");
	        choiceNumber.setFont(new Font(20));
	        
	        //creation of the gridPane which will contains 4 buttons
	        GridPane numberPawn = new GridPane();
	        numberPawn.setHgap(5);
	        numberPawn.setVgap(5);
	       
	        
	        for(int i = 0;i<2;i++)
	        {
	        	for(int j =0;j<2;j++)
	        	{
	        		Button number = new Button(" " + ((i*2)+(j+1)));
	        		number.setMinSize( 100,20);
	        		number.setFont(new Font(25));
	        		GridPane.setRowIndex(number, i);
	        		GridPane.setColumnIndex(number, j);
	        		numbers.add(number);
	        		numberPawn.getChildren().addAll(number);
	        		
	        	}
	        }
	        bottom.getChildren().addAll(player,diceButton,dice,choiceNumber,numberPawn);
		return bottom;
	}
}