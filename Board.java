package connectfour;
import processing.core.PApplet;

/**
 * Board Class
 * 
 * This class represents the yellow Connect Four game board.
 * It also stores the tokens placed on it.
 * 
 * @author Julian Hinsch
 * @version 5/29/17
 */
public class Board {
	public Token[][] tokenarray;
	private int numtokens;
	PApplet parent;
	
	/**
	 * Constructor
	 */
	Board(PApplet parent) {
		this.parent=parent;
	}
	
	/**
	 * Initialize Token Array Method
	 * 
	 * Initializes the 2D token array and populates it with blue tokens.
	 */
	public void initializeTokenArray() {
		//initialize board array
		tokenarray = new Token[7][6];
		numtokens=0;
		//x positions
		for (int x = parent.width/2-150,i=0;i<7;x+=50,i++) {
			//y positions
			for (int y = parent.height/2+125,j=0;j<6;y-=50,j++) {
				//initialize token objects
				Token temp = new Token(parent,x,y,"blue",false);
				//add to board
				tokenarray[i][j]=temp;
			}
		}
	}
	
	/**
	 * Display Method
	 * 
	 * Displays the yellow board and all tokens.
	 */
	public void display() {
		//yellow rectangle
		parent.fill(255,255,0);
		parent.rect(180,85,360,310,5);
		parent.noStroke();
		//tokens
		for (int i=0;i<7;i++) {
			for (int j=0;j<6;j++) {
				tokenarray[i][j].display();
			}
		}
	}
	
	/**
	 * Hide Method
	 * 
	 * Displays a blue rectangle over the board's area.
	 */
	public void hide() {
		parent.fill(139,189,255);
		parent.rect(180,85,360,parent.height/2+125);
	}
	
	/**
	 * Get Number of Tokens Method
	 * 
	 * Gets the number of filled spaces on the board (red or black tokens).
	 * 
	 * @return the number of filled spaces on the board (red or black tokens).
	 */
	public int getNumTokens() {
		return this.numtokens;
	}
	
	/**
	 * Set Number of Tokens Method
	 * 
	 * Sets the number of filled spaces on the board (red or black tokens).
	 * Should be called whenever a token is placed.
	 * 
	 * @param the number of filled spaces on the board (red or black tokens).
	 */
	public void setNumTokens(int numtokens) {
		this.numtokens=numtokens;
	}
	
//methods to test for win
	
	/**
	 * Test Win Method
	 * 
	 * Searches the entire board for a winning pattern, given a color.
	 * 
	 * @param color, the color to search for.
	 * @return true if the color is present in a winning pattern, false otherwise.
	 */
	public boolean testWin(String color){
		//iterate through entire board
		for (int x = 0; x < 7; x++) {
			for (int y = 0; y < 6; y++) {
				//only test if the token at this position is the color to test
				if(tokenarray[x][y].getColor().equals(color)) {
					//tests if the position is part of a "four" in any direction
					if(testWinDirection(color,x,y,1,0) || 
						testWinDirection(color,x,y,0,1) || 
						testWinDirection(color,x,y,1,1) || 
						testWinDirection(color,x,y,1,-1)) {
						//position is first in a "four"
						return true;
					}
				}
			}
		}
		//no "fours" of this color were found
		return false;
	}
	
	/**
	 * Test Win in Direction Method
	 * 
	 * Tests if the position specified begins a winning "four" pattern.
	 * 
	 * Tests horizontal, vertical, left diagonal, and right diagonal.
	 * If the iteration reaches the edge of the board, or the pattern is interrupted,
	 * it returns false.
	 * 
	 * @param color, the color check.
	 * @param x, the x-coordinate of the starting position.
	 * @param y, the y-coordinate of the starting position.
	 * @param x-iterator, the direction to iterate x.
	 * @param y-iterator, the direction to iterate y.
	 * @return true if there is a winning pattern at this location, false otherwise.
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
		//winning pattern
		for (int i = 0; i < 4; i++) {
			tokenarray[x+(xiter*i)][y+(yiter*i)].setIsWinner(true);
		}
		return true;
	}
}
