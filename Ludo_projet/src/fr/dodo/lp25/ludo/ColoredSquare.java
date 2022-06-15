package fr.dodo.lp25.ludo;
public abstract class ColoredSquare extends Square {

	private ColorMapperEnum color;

	public ColoredSquare(int number, ColorMapperEnum c) {
		super(number);
		this.color = c;

	}

	public ColorMapperEnum getColor() {
		return color;
	}

	public abstract boolean CanPass(Pawn pawn);

	public abstract boolean CanStop(int endSquare, Pawn pawn, int dice);

	public String toString() {
		return color + " \n " + super.toString();
	}
}