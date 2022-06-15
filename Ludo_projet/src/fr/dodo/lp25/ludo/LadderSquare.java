package fr.dodo.lp25.ludo;
public class LadderSquare extends ColoredSquare {

	public LadderSquare(int number, ColorMapperEnum color) {
		super(number, color);
	}

	public boolean CanPass(Pawn pawn) {
		// can pass if it's the good color
		return pawn.getColor() == getColor();
	}

	public boolean CanStop(int endSquare, Pawn pawn, int dice) {
		// can stop if the square is empty or is the endSquare, has the good color and the dice = 0
		return this.horses.isEmpty() && pawn.getColor() == getColor() && dice == 0
				|| pawn.getCurrentlySquare().getNumber() != endSquare && dice == 0 && pawn.getColor() == getColor();
	}

	public String toString() {
		return "LadderSquare " + super.toString();
	}
}

/*
public int getNumberSquare() {
	// en fonction de comment on gérera les nombres
	// numberbox : nombre qui sera écrit sur la case ( 1 , 2 ,3 ,4 ,5 ,6 )
	Random n = new Random();
	int numberSquare = n.nextInt(6);
	return numberSquare;
}
*/
