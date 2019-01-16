package connectfour;
import processing.core.PApplet;

/**
 * Displays the yellow game board and stores an array of tokens.
 * Contains methods to determine if a winning pattern is present on the board.
 */
public class Board {

	public Token[][] tokenarray;
	private int numtokens;
	PApplet parent;
	
	/**
	 * Constructor
	 *
	 * @param parent, the base class for skethes that use processing.core
	 */
	Board(PApplet parent) {
		this.parent=parent;
	}
	
	/**
	 * Initializes the 2D token array and populates it with blue tokens.
	 */
	public void initializeTokenArray() {
		tokenarray = new Token[7][6];
		numtokens=0;
		for (int x = parent.width/2-150,i=0; i<7; x+=50,i++) {
			for (int y = parent.height/2+125,j=0;j<6;y-=50,j++) {
				Token temp = new Token(parent,x,y,"blue",false);
				tokenarray[i][j]=temp;
			}
		}
	}
	
	/**
	 * Displays the yellow board and all tokens.
	 */
	public void display() {
		parent.fill(255,255,0);
		parent.rect(180,85,360,310,5);
		parent.noStroke();
		for (int i=0; i<7; i++) {
			for (int j=0;j<6;j++) {
				tokenarray[i][j].display();
			}
		}
	}
	
	/**
	 * Draws a blue rectangle over the board's area.
	 */
	public void hide() {
		parent.fill(139,189,255);
		parent.rect(180,85,360,parent.height/2+125);
	}
	
	/**
	 * Searches the entire board for a winning pattern, given a color as a parameter.
	 *
	 * @param color, the color to test.
	 */
	public boolean testWin(String color){
		for (int x = 0; x < 7; x++) {
			for (int y = 0; y < 6; y++) {
				if(tokenarray[x][y].getColor().equals(color)) {
					//tests if the position is part of a "four" in any direction
					if(
						testWinDirection(color,x,y,1,0) || 
						testWinDirection(color,x,y,0,1) || 
						testWinDirection(color,x,y,1,1) || 
						testWinDirection(color,x,y,1,-1)
					) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * Tests if the position specified begins a winning "four" pattern.
	 * 
	 * Tests horizontal, vertical, left diagonal, and right diagonal.
	 * Returns false if the iteration reaches the edge of the board or the pattern is interrupted.
	 * 
	 * @param color, the color to test.
	 * @param x, the x-coordinate of the starting position.
	 * @param y, the y-coordinate of the starting position.
	 * @param x-iterator, the direction to iterate x.
	 * @param y-iterator, the direction to iterate y.
	 */
	public boolean testWinDirection(String color,int x,int y,int xiter, int yiter) {
		for (int i = 1; i < 4; i++) {
			//x is out of bounds
			if(x+(xiter*i)>6 || x+(xiter*i)<0 ) {
				return false;
			//y is out of bounds
			} else if(y+(yiter*i)>5 || y+(yiter*i)<0 ) {
				return false;
			//the pattern is interrupted
			} else if (!tokenarray[x+(xiter*i)][y+(yiter*i)].getColor().equals(color)) {
				return false;
			}
		}
		//once it is determined that there is a win, mark each winning token.
		for (int i = 0; i < 4; i++) {
			tokenarray[x+(xiter*i)][y+(yiter*i)].setIsWinner(true);
		}
		return true;
	}

	// Getters & Setters

	public int getNumTokens() {
		return this.numtokens;
	}
	
	public void setNumTokens(int numtokens) {
		this.numtokens=numtokens;
	}
}
