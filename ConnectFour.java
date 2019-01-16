package connectfour;
import java.util.ArrayList;
import processing.core.*;

/**
 * Connect Four
 * 
 * User chooses red or black.
 * Plays against simple AI that drops tokens at strategic locations.
 * User has the option to play another game upon completion.
 * 
 * @author Julian Hinsch
 */
public class ConnectFour extends PApplet {

	private PFont f;
	private String usercolor;
	private String compcolor;
	private String gamestage;
	private Board board;
	private Token redtoken;
	private Token blacktoken;
	
	/**
	 * Initializes the background, text, color choice tokens, and gamestage.
	 */
	public void setup() {
		size(720, 480);
		background(139,189,255);
		displayBoardText();
		noLoop();
		redtoken = new Token(this, width/2-25, height/2+10, "red", false);
		blacktoken = new Token(this, width/2+25, height/2+10, "black", false);
		gamestage = "colorchoice";
		redraw();
	}
	
	/**
	 * Displays various objects and text depending on the current game stage.
	 */
	public void draw() {
		switch(gamestage) {
			case "colorchoice":
				displayChoiceText();
				redtoken.display();
				blacktoken.display();
				break;
			case "userturn":
				board.display();
				break;
			case "compturn":
				board.display();
				break;
			case "userwin":
				board.display();
				displayUserWinText();
				break;
			case "compwin":
				board.display();
				displayCompWinText();
				break;
			case "fullboard":
				board.display();
				displayBoardFullText();
				break;
			default:
				break;
		}
	}
	
	/**
	 * Executes different code depending on the current game stage.
	 */
	public void mouseClicked() {
		switch(gamestage) {
			case "colorchoice":
				if (redtoken.mouseWithin()) {
					//user chose red
					usercolor = "red";
					compcolor = "black";
					board = new Board(this);
					board.initializeTokenArray();
					redraw();
					gamestage = "userturn";
					break;
				} else if (blacktoken.mouseWithin()) {
					//user chose black
					usercolor = "black";
					compcolor = "red";
					board = new Board(this);
					board.initializeTokenArray();
					redraw();
					gamestage = "userturn";
					break;
				}
				break;
			case "userturn": 
				boolean usermoved = false;
				for (int x = 0; x < 7; x++) {
					for (int y = 0; y < 6; y++) {
						//determine which token (if any) the mouse is above
						if (board.tokenarray[x][y].mouseWithin()) {
							//make sure the column isn't full
							if (board.tokenarray[x][5].getColor().equals("blue")) {
								executeMove(x, y, usercolor);
								usermoved = true;
								redraw();
							}
						}
					}
				}
				if(usermoved == false) {
					break;
				} else if (board.testWin(usercolor)) {
					gamestage = "userwin";
					redraw(); 
					break;
				} else {
					gamestage = "compturn";
					//wait for 1 second - not necessary but better for UX
					try {
					    Thread.sleep(1000);
					} catch(InterruptedException ex){
					    Thread.currentThread().interrupt();
					}
					//computer moves
					compMove();
					redraw();
					if (board.testWin(compcolor)) {
						gamestage = "compwin";
						redraw();
						usermoved = false;
						break;
					} else if (board.getNumTokens() == 42) {
						//computer filled the board
						gamestage = "fullboard";
						redraw();
						usermoved = false;
						break;
					} else {
						//return control to user
						gamestage = "userturn";
						redraw();
						usermoved = false;
						break;	
					}
				}
			case "compturn": 
				break;
			case "userwin":
				gamestage = "colorchoice";
				board.hide();
				redraw();
				break;
			case "compwin":
				gamestage = "colorchoice";
				board.hide();
				redraw();
				break;
			case "fullboard":
				gamestage = "colorchoice";
				board.hide();
				redraw();
				break;
			default:
				break;
		}
	}
	
	
	/**
	 * Place a token in the most strategic column.
	 */
	private void compMove() {
		int x = findBestMove();
		executeMove(x, 5, compcolor);
	}
	
	/**
	 * Sets token color at lowest possible row.
	 * 
	 * @param x, the selected column.
	 * @param maxY, the desired row selected by user (computer always selects top row).
	 * @param color, the color to change the token to.
	 */
	private void executeMove(int x,int maxY,String color){
		for (int y = 0; y <= maxY; y++) {
			//iterate up column
			if (board.tokenarray[x][y].getColor().equals("blue")) {
				//execute move - only once
				board.tokenarray[x][y].setColor(color);
				board.setNumTokens(board.getNumTokens()+1);
				return;
			}
		}
	}
	

	/**
	 * Find Best Move Method
	 * 
	 * There are never more than 7 possible moves.
	 * A score is calculated for each move based on its neighbors.
	 * The move with the best score is returned.
	 * If there are multiple possible moves with the same score,
	 * one is chosen randomly.
	 */
	private int findBestMove() {
		int[] moveScores = {0,0,0,0,0,0,0};
		for (int x = 0; x < 7; x++) {
			for (int y = 0; y<6; y++) {
				if (board.tokenarray[x][y].getColor().equals("blue")) {	
					//check if this move can be made
					//due to 'gravity', move must be on floor or above a previously placed token
					if (y==0 || !board.tokenarray[x][y-1].getColor().equals("blue")) {
						moveScores[x]=calculateMoveScore(x,y);
						break;
					}
				}
			}
		}		
		//iterate over moveScores
		//find the best score, and store all moves with this score
		int bestScore = 0;
		ArrayList<Integer> bestMoves = new ArrayList<Integer>();
		//pick the best move in the list
		for (int x = 0; x < 7; x++) {
			if (moveScores[x]==bestScore) {
				//move is equivalent to moves already in bestMoves
				bestMoves.add(x);
			} else if (moveScores[x]>bestScore) {
				//reinitialize bestMoves with only this move
				bestMoves = new ArrayList<Integer>();
				bestMoves.add(x);
				//this move had the best score so far
				bestScore = moveScores[x];
			}
		}
		//randomly select a move from bestMoves
		int randomindex = (int)(Math.random() * bestMoves.size());
		return bestmoves.get(randomindex);
	}
	

	/**
	 * Calculates a score (0-4) for a possible move based on neighboring tokens.
	 * 
	 * @param x, the x position of the move to be considered
	 * @param y, the y position of the move to be considered
	 */
	private int calculateMoveScore(int x,int y) {		

		int hzontaluser=min(3,(countNeighborsDirection(x,y,usercolor,1,0)+countNeighborsDirection(x,y,usercolor,-1,0)));
		int vticaluser=countNeighborsDirection(x,y,usercolor,0,-1);
		int rdiaguser=min(3,(countNeighborsDirection(x,y,usercolor,1,1)+countNeighborsDirection(x,y,usercolor,-1,-1)));
		int ldiaguser=min(3,(countNeighborsDirection(x,y,usercolor,-1,1)+countNeighborsDirection(x,y,usercolor,1,-1)));
		int maxuserneighbors=max(max(hzontaluser,vticaluser),max(rdiaguser,ldiaguser));

		int hzontalcomp = min(3,(countNeighborsDirection(x,y,compcolor,1,0)+countNeighborsDirection(x,y,compcolor,-1,0)));
		int vticalcomp = countNeighborsDirection(x,y,compcolor,0,-1);
		int rdiagcomp = min(3,(countNeighborsDirection(x,y,compcolor,1,1)+countNeighborsDirection(x,y,compcolor,-1,-1)));
		int ldiagcomp = min(3,(countNeighborsDirection(x,y,compcolor,-1,1)+countNeighborsDirection(x,y,compcolor,1,-1)));
		int maxcompneighbors = max(max(hzontalcomp,vticalcomp),max(rdiagcomp,ldiagcomp));
		
		if (maxcompneighbors == 3) {
			//this is a winning move for the computer
			return 4;
		} else {
			return max(maxuserneighbors,maxcompneighbors);
		}
	}
	

	/**
	 * Look for user tokens near the move to be considered,
	 * in a direction specified by parameters xiter and yiter.
	 * 
	 * Returns the number of tokens in that direction (0, 1, 2, or 3).
	 * 
	 * @param x, the x position of the move to be considered
	 * @param y, the y position of the move to be considered
	 * @param color, the color of the tokens to look for
	 * @param xiter, the direction to iterate the x-coordinate
	 * @param yiter, the direction to iterate the y-coordinate
	 */
	private int countNeighborsDirection(int x, int y, String color, int xiter, int yiter) {
		int count=0;
		for (int i = 1; i <= 3; i++) {
			//x is out of bounds
			if(x+(xiter*i)>6 || x+(xiter*i)<0 ) {
				break;
			//y is out of bounds
			} else if(y+(yiter*i)>5 || y+(yiter*i)<0 ) {
				break;
			} else if (!board.tokenarray[x+(xiter*i)][y+(yiter*i)].getColor().equals(color)) {
				break;
			} else {
				count++;
			}
		}
		return count;
	}	
	
	/**
	 * Displays "Board is full!", "Click anywhere to play again."
	 */
	private void displayBoardFullText() {
		fill(255);
		textAlign(CENTER);
		f = createFont("Helvetica-Bold",20,true);
		textFont(f);
		text("Board is full!",width/2,height/2+180);
		text("Click anywhere to play again.",width/2,height/2+200);
	}
	
	/**
	 * Displays "Connect Four", "Copyright Julian Hinsch 2017"
	 */
	private void displayBoardText(){
		fill(255);
		textAlign(CENTER);
		f = createFont("Helvetica-Bold",48,true);
		textFont(f);
		text("Connect Four",width/2,65);
		f = createFont("Helvetica-Bold",15,true);
		textFont(f);
		text("\u00a9 2017 Julian Hinsch",width/2,470);
	}
	
	/**
	 * Displays "Choose black or red: "
	 */
	private void displayChoiceText() {
		fill(255);
		textAlign(CENTER);
		f = createFont("Helvetica-Bold",24,true);
		textFont(f);
		text("Choose red or black:",width/2,height/2-30);
	}
	
	/**
	 * Displays "You lost!", "Click anywhere to play again."
	 */
	private void displayCompWinText() {
		fill(255);
		textAlign(CENTER);
		f = createFont("Helvetica-Bold",20,true);
		textFont(f);
		text("You lost!",width/2,height/2+180);
		text("Click anywhere to play again.",width/2,height/2+200);
	}
	
	/**
	 * Displays "You won!", "Click anywhere to play again."
	 */
	private void displayUserWinText() {
		fill(255);
		textAlign(CENTER);
		f = createFont("Helvetica-Bold",20,true);
		textFont(f);
		text("You won!",width/2,height/2+180);
		text("Click anywhere to play again.",width/2,height/2+200);
	}
}
