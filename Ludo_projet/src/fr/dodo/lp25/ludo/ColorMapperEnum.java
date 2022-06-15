package fr.dodo.lp25.ludo;
import java.awt.Color;

// This classes is here only for mapping the Color from java.awt.Color to a printable version with println

public enum ColorMapperEnum {
	GREEN("Green", Color.GREEN), RED("Red", Color.RED), YELLOW("Yellow", Color.YELLOW), BLUE("Blue", Color.BLUE);

	private String textToFind;
	private Color targetColor;

	private ColorMapperEnum(String textToFind, Color targetColor) {
		this.textToFind = textToFind;
		this.targetColor = targetColor;
	}

	public String getTextToFind() {
		return textToFind;
	}

	public Color getTargetColor() {
		return targetColor;
	}
}