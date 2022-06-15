package fr.dodo.lp25.ludo;
public class Pawn {

	private ColorMapperEnum color;
	private Square currentlySquare;
	private int id;

	public Pawn(ColorMapperEnum c, int i) {
		this.color = c;
		this.id = i;
	}

	public ColorMapperEnum getColor() {
		return color;
	}

	public int getId() {
		return id;
	}
	
	public Square getCurrentlySquare() {
		return currentlySquare;
	}

	public void setCurrentlySquare(Square currentlySquare) {
		this.currentlySquare = currentlySquare;
	}

	public String toString() {

		return "Pawn " +  this.color + " n°" + this.id;
	}
}
