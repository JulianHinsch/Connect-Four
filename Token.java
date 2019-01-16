package connectfour;
import processing.core.PApplet;

/**
 * Represents game token or empty space on the board.
 */
public class Token {

	PApplet parent;
	final int diameter = 40;
	private int x;
	private int y;
	public String color;
	private boolean isWinner;

	/**
	 * Default Constructor
	 */
	Token() {}
	
	/**
	 * All-Arg Constructor
	 * 
	 * @param parent
	 * @param x, the x-coordinate of the circle's center
	 * @param y, the y-coordinate of the circle's center
	 * @param color, 
	 * @param isWinner,
	 */
	Token(PApplet parent,int x, int y, String color, boolean isWinner) {
		this.parent=parent;
		this.x=x;
		this.y=y;
		this.color=color;
		this.isWinner=isWinner;
	}
	
	/**
	 * Displays the token.
	 * If it is part of a winning pattern, it will have a light green outline.
	 */
	public void display() {
		parent.noStroke();
		if (this.color.equals("blue")) {
			parent.fill(139,189,255);
			parent.ellipse(x,y,diameter,diameter);
			return;
		}
		if (this.isWinner) {
			parent.stroke(0,255,0);
			parent.strokeWeight(4);
		}
		if (this.color.equals("red")) {	
			parent.fill(255,0,0);
			parent.ellipse(x,y,diameter,diameter);
		} else if (this.color.equals("black")) {
			parent.fill(0);
			parent.ellipse(x,y,diameter,diameter);
		}
		if (this.isWinner) {
			parent.noStroke();
		}
	}

	/**
	 * Tests if the user's mouse position is within the token using pythagorean theorem.
	 */
	public boolean mouseWithin() {
		double radius = 0.5 * this.diameter;
		float xdif = Math.abs((float) parent.mouseX - (float) this.x); 
		float ydif = Math.abs((float) parent.mouseY - (float) this.y);
		if (((xdif * xdif) + (ydif * ydif)) <= (radius * radius)) {
			return true;	
		}
		return false;
	}

	// Getters & Setters
	
	public String getColor() {
		return this.color;
	}
	
	public void setColor(String color) {
		this.color = color;
	}
	
	public void setIsWinner(boolean isWinner) {
		this.isWinner = isWinner;
	}
}
