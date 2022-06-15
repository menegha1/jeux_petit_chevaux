package fr.dodo.lp25.ludo;
public class StableSquare extends ColoredSquare {

	public StableSquare(int number, ColorMapperEnum color) {
		super(number, color);
	}

	public boolean CanPass(Pawn pawn) {
		return (pawn.getColor() == getColor());
	}

	public boolean CanStop(int endSquare, Pawn pawn, int dice) {
		return (pawn.getColor() == getColor() && dice == 0);
	}

	public String toString() {
		return " StableSquare :  " + super.toString();
	}

}
