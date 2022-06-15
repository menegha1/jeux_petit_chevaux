package fr.dodo.lp25.ludo;
public class PathSquare extends Square {

	public PathSquare() {
		super(0);
	}

	public PathSquare(int number) {
		super(number);
	}

	public boolean CanPass(Pawn currentlyPawn) {
		/*
		 * we check if the first pawn of the list has the same color than our
		 * "currentlyPawn" if it is true, it's impossible that there is another pawn of
		 * this color, so it can pass if it is false, we check the number of pawn (more
		 * than 1 => can't pass)
		 */
		if (!this.horses.isEmpty()) {
			if (this.horses.get(0).getColor() != currentlyPawn.getColor() && this.horses.size() > 1) {
				return false;
			}
		}

		return true;
	}

	public boolean CanStop(int endSquare, Pawn currentlyPawn, int dice) {
		if (dice == 0)
			return true;
		else
			return false;
	}

}
