package fr.dodo.lp25.ludo;
import java.util.ArrayList;
import java.util.List;

public abstract class Square {

	private int number;
	protected ArrayList<Pawn> horses = new ArrayList<Pawn>();

	Square() {
		this.number = 0;
	}

	Square(int n) {
		this.number = n;
	}

	public List<Pawn> getHorses() {
		return horses;
	}

	public int getNumber() {
		return number;
	}

	public abstract boolean CanPass(Pawn pawn);

	public abstract boolean CanStop(int endSquare, Pawn pawn, int dice);

	public void addPawn(Pawn pawn) {
		pawn.setCurrentlySquare(this);
		horses.add(pawn);
	}

	public void removePawn(Pawn pawn) {
		horses.remove(pawn);
	}

	// check if the square is a safe zone or not
	public boolean isSafezone() {
		switch (number) {
		case 1, 9, 14, 22, 27, 35, 40, 48:
			return true;
		}
		return false;
	}

	public String toString() {

		String s = new String();
		s = " number : " + this.number + " \n Currently Pawn : " + this.horses;
		return s;
	}
}
