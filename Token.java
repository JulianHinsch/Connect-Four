package connectfour;
import processing.core.PApplet;

/**
 * Token Class
 * 
 * Tokens to be displayed by ConnectFourApp.
 * These represent user/computer game tokens, or empty spaces in the board.
 * 
 * @author Julian Hinsch
 * @version 5/29/17
 */
public class Token {
	//parent
	PApplet parent;
	//diameter
	final int diameter = 40;
	//x-coordinate of circle's center
	private int x;
	//y-coordinate of circle's center
	private int y;
	//color
	public String color;
	//winner
	private boolean isWinner;

	/**
	 * Token Default Constructor
	 * 
	 * Creates a token object.
	 */
	Token() {
	}
	
	/**
	 * All-Argument Constructor
	 * 
	 * Creates a token object at specified x,y coordinates and color.
	 * 
	 * @param parent
	 * @param x, the x-coordinate of the circle's center
	 * @param y, the y-coordinate of the circle's center
	 * @param color, the color of the circle.
	 * @param isWinner, true if this circle is a winner, false otherwise.
	 */
	Token(PApplet parent,int x, int y, String color, boolean isWinner) {
		this.parent=parent;
		this.x=x;
		this.y=y;
		this.color=color;
		this.isWinner=isWinner;
	}
	
	/**
	 * Display Method
	 * 
	 * Displays this token object.
	 * Token can be blue, red, or black.
	 * If it is marked as part of a winning pattern, it will have a green outline.
	 */
	public void display() {
		parent.noStroke();
		//red
		if (this.color.equals("red")) {
			if (this.isWinner) {
				parent.stroke(0,255,0);
				parent.strokeWeight(4);
			}
			parent.fill(255,0,0);
			parent.ellipse(x,y,diameter,diameter);
			if (this.isWinner) {
				parent.noStroke();
			}
		}
		//blue
		else if (this.color.equals("blue")) {
			parent.fill(139,189,255);
			parent.ellipse(x,y,diameter,diameter);
		}
		//black
		else if (this.color.equals("black")) {
			if (this.isWinner) {
				parent.stroke(0,255,0);
				parent.strokeWeight(4);
			}
			parent.fill(0);
			parent.ellipse(x,y,diameter,diameter);
			if (this.isWinner) {
				parent.noStroke();
			}
		}
	}

	/**
	 * Mouse Within Method
	 * 
	 * Tests if the user's mouse position is within a this token.
	 * 
	 * @return true if mouse is within this token, false otherwise.
	 */
	public boolean mouseWithin() {
		//calculate radius of Token
		double r = 0.5*this.diameter;
		//calculate x^2 and y^2
		float xdif = Math.abs((float) parent.mouseX - (float) this.x); 
		float ydif = Math.abs((float) parent.mouseY - (float) this.y);
		//test if sum is less than radius squared
		if (((xdif*xdif)+(ydif*ydif)) <= (r*r)) {
			return true;	
		}
		return false;
	}
	
	/**
	 * Get Color Method
	 * 
	 * @return the color of the token.
	 */
	public String getColor() {
		return this.color;
	}
	
	/**
	 * Set Color Method
	 * 
	 * @param the color to change the token to.
	 */
	public void setColor(String color) {
		this.color = color;
	}
	
	/**
	 * Set isWinner Method
	 * 
	 * @param true if this token is to be marked a winner, false otherwise.
	 */
	public void setIsWinner(boolean isWinner) {
		this.isWinner = isWinner;
	}
	
}
