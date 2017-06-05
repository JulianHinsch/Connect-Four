package connectfour;
import java.util.ArrayList;
import processing.core.*;

/**
 * Connect Four Application
 * 
 * Graphic Connect Four game made with Processing library.
 * 
 * User chooses red or black.
 * Plays against simple AI that drops tokens at strategic locations.
 * User has the option to play another game upon completion.
 * 
 * @author Julian Hinsch
 * @version 6/1/17
 */
public class ConnectFourApp extends PApplet {
	//font
	private PFont f;
	//user color
	private String usercolor;
	//comp color
	private String compcolor;
	//game stage
	private String gamestage;
	//game board
	private Board board;
	//red choice token
	private Token redtoken;
	//black choice token
	private Token blacktoken;
	
	/**
	 * Setup Method
	 * 
	 * Initializes and displays the game board, background, and text.
	 */
	public void setup() {
		size(720, 480);
		background(139,189,255);
		displayBoardText();
		noLoop();
		//make the 2 tokens
		redtoken = new Token(this,width/2-25,height/2+10,"red",false);
		blacktoken = new Token(this,width/2+25,height/2+10,"black",false);
		gamestage="colorchoice";
		redraw();
	}
	
	/**
	 * Draw Method
	 * 
	 * Contains a switch statement that displays various objects
	 * and text depending on the current state of the game.
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
	 * Mouse Clicked Method
	 * 
	 * Executes different events on a mouse click
	 * depending on the current stage of the game.
	 */
	public void mouseClicked() {
		switch(gamestage) {
			case "colorchoice":
				if (redtoken.mouseWithin()) {
					//user chose red
					usercolor="red";
					compcolor="black";
					//create board
					board = new Board(this);
					board.initializeTokenArray();
					//draw the empty board
					redraw();
					//next stage
					gamestage="userturn";
					break;
				} else if (blacktoken.mouseWithin()) {
					//user chose black
					usercolor="black";
					compcolor="red";
					//create board
					board = new Board(this);
					board.initializeTokenArray();
					//draw the empty board
					redraw();
					//next stage
					gamestage="userturn";
					break;
					
				} else {
					break;
				}
			case "userturn": 
				boolean usermoved=false;
				for (int x = 0; x < 7; x++) {
					for (int y = 0; y < 6; y++) {
						//compute which token (if any) the mouse is above
						if (board.tokenarray[x][y].mouseWithin()) {
							//make sure the column isn't full
							if (board.tokenarray[x][5].getColor().equals("blue")) {
								executeMove(x,y,usercolor);
								usermoved=true;
								redraw();
							}
						}
					}
				}
				if(usermoved==false) {
					//no move was made
					break;
				} else if (board.testWin(usercolor)) {
					//a winning move was made
					gamestage="userwin";
					redraw(); 
					break;
				} else {
					//computer gets to move
					gamestage="compturn";
					//wait for 1 second
					try {
					    Thread.sleep(1000);
					} catch(InterruptedException ex){
					    Thread.currentThread().interrupt();
					}
					//computer moves
					compMove();
					redraw();
					if (board.testWin(compcolor)) {
						//computer won
						gamestage="compwin";
						redraw();
						usermoved=false;
						break;
					} else if (board.getNumTokens()==42) {
						//computer filled the board
						gamestage="fullboard";
						redraw();
						usermoved=false;
						break;
					} else {
						//go back to the user
						gamestage="userturn";
						redraw();
						usermoved=false;
						break;	
					}
				}
			case "compturn": 
				//do nothing
					break;
			case "userwin":
				//click anywhere to play again
				gamestage="colorchoice";
				board.hide();
				redraw();
				break;
			case "compwin":
				//click anywhere to play again
				gamestage="colorchoice";
				board.hide();
				redraw();
				break;
			case "fullboard":
				//click anywhere to play again
				gamestage="colorchoice";
				board.hide();
				redraw();
				break;
			default:
				break;
		}
	}
	
//computer AI
	
	/**
	 * Computer Move Method
	 * 
	 * Find the best move to make, and
	 * drop a token of the opposite color as the user.
	 */
	private void compMove() {
		//get a smart move
		int x = findBestMove();
		//execute the move
		executeMove(x,5,compcolor);
	}
	
	/**
	 * Execute Move Method
	 * 
	 * Executes a move at lowest possible Y position.
	 * 
	 * Computer moves are already validated by findBestMove()
	 * User moves may cause IllegalArgumentException if column is full.
	 * 
	 * @param the x position of the desired move on the board.
	 * @param the color to change the space to.
	 */
	private void executeMove(int x,int maxy,String color){
		for (int y=0;y<=maxy; y++) {
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
	 * Efficiently iterates through possible computer moves.
	 * 
	 * There are 7 possible moves, stored in moves[].
	 * A score is calculated for each move based on its neighbors.
	 * 
	 * The move with the best score is returned.
	 * If there are multiple possible moves with the same score,
	 * one is chosen randomly.
	 * 
	 * @return the column in which to place a token.
	 */
	private int findBestMove() {
		//array of move scores
		int[] movescores = {0,0,0,0,0,0,0};
		//iterate through the 7 possible moves
		for (int x = 0; x < 7; x++) {
			for (int y = 0; y<6; y++) {
				//make sure its empty
				if (board.tokenarray[x][y].getColor().equals("blue")) {	
					//check if this move can be made
					//move must be on floor or above a previously placed Token
					if (y==0 || !board.tokenarray[x][y-1].getColor().equals("blue")) {
						//calculate the score
						movescores[x]=calculateMoveScore(x,y);
						//dont look at impossible moves above this y position
						break;
					}
				}
			}
		}		
		//iterate through moves[]
		//find the best score, and store all moves with this score
		int bestscore = 0;
		ArrayList<Integer> bestmoves = new ArrayList<Integer>();
		//pick the best move in the list
		for (int x = 0; x < 7; x++) {
			if (movescores[x]==bestscore) {
				//this move is equivalent to moves already in the ArrayList
				bestmoves.add(x);
			} else if (movescores[x]>bestscore) {
				//clear the ArrayList and add only this move
				bestmoves = new ArrayList<Integer>();
				bestmoves.add(x);
				//this move had the best score so far
				bestscore=movescores[x];
			}
		}
		//randomly choose a move from the ArrayList
		int randomindex=(int)(Math.random()*bestmoves.size());
		//return one of the moves
		return bestmoves.get(randomindex);
	}
	
	/**
	 * Calculate Move Score Method
	 * 
	 * Calculates a score for this move based on neighboring tokens.
	 * 
	 * @param x, the x position of the move to be considered
	 * @param y, the y position of the move to be considered
	 * @return a score, 0-4, for this move.
	 */
	private int calculateMoveScore(int x,int y) {		
		//user neighbors
		//horizontal
		int hzontaluser=min(3,(countNeighborsDirection(x,y,usercolor,1,0)+countNeighborsDirection(x,y,usercolor,-1,0)));
		//down
		int vticaluser=countNeighborsDirection(x,y,usercolor,0,-1);
		//right diagonal
		int rdiaguser=min(3,(countNeighborsDirection(x,y,usercolor,1,1)+countNeighborsDirection(x,y,usercolor,-1,-1)));
		//left diagonal
		int ldiaguser=min(3,(countNeighborsDirection(x,y,usercolor,-1,1)+countNeighborsDirection(x,y,usercolor,1,-1)));
		//maximum
		int maxuserneighbors=max(max(hzontaluser,vticaluser),max(rdiaguser,ldiaguser));
		
		//comp neighbors
		//horizontal
		int hzontalcomp=min(3,(countNeighborsDirection(x,y,compcolor,1,0)+countNeighborsDirection(x,y,compcolor,-1,0)));
		//down
		int vticalcomp=countNeighborsDirection(x,y,compcolor,0,-1);
		//right diagonal
		int rdiagcomp=min(3,(countNeighborsDirection(x,y,compcolor,1,1)+countNeighborsDirection(x,y,compcolor,-1,-1)));
		//left diagonal
		int ldiagcomp=min(3,(countNeighborsDirection(x,y,compcolor,-1,1)+countNeighborsDirection(x,y,compcolor,1,-1)));
		//maximum
		int maxcompneighbors=max(max(hzontalcomp,vticalcomp),max(rdiagcomp,ldiagcomp));
		
		if (maxcompneighbors==3) {
			//this is a winning move
			return 4;
		} else {
			return max(maxuserneighbors,maxcompneighbors);
		}
		
	}
	
	/**
	 * Count Neighbors in Direction Method
	 * 
	 * Looks for a column of user tokens near the move to be considered,
	 * in a direction specified by parameters xiter and yiter.
	 * 
	 * Returns the length of the uninterrupted row (0,1,2, or 3).
	 * 
	 * @param x, the x position of the move to be considered
	 * @param y, the y position of the move to be considered
	 * @param color, the color of the tokens to look for
	 * @param xiter, the direction to iterate the x-coordinate
	 * @param yiter, the direction to iterate the y-coordinate
	 * @return the length of the uninterrupted row (0,1,2,or 3).
	 */
	private int countNeighborsDirection(int x,int y, String color, int xiter, int yiter) {
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
	
//methods to display text
	
	/**
	 * Board Full Method
	 * 
	 * Displays text when board is full.
	 */
	private void displayBoardFullText() {
		//set font color
		fill(255);
		//align center
		textAlign(CENTER);
		//initialize font
		f = createFont("Helvetica-Bold",20,true);
		//set font
		textFont(f);
		//display text
		text("Board is full!",width/2,height/2+180);
		text("Click anywhere to play again.",width/2,height/2+200);
	}
	
	/**
	 * Display Board Text Method
	 * 
	 * Displays "Connect Four"
	 * Displays "Copyright Julian Hinsch 2017"
	 */
	private void displayBoardText(){
		//set font color
		fill(255);
		//align center
		textAlign(CENTER);
		//title text
		//initialize font
		f = createFont("Helvetica-Bold",48,true);
		//set font
		textFont(f);
		//display text
		text("Connect Four",width/2,65);
		//copyright text
		//initialize font
		f = createFont("Helvetica-Bold",15,true);
		//set font
		textFont(f);
		//display text
		text("\u00a9 2017 Julian Hinsch",width/2,470);
	}
	
	/**
	 * Display Choice Text Method
	 * 
	 * Displays"Choose black or red:"
	 */
	private void displayChoiceText() {
		//set font color
		fill(255);
		//align center
		textAlign(CENTER);
		//initialize font
		f = createFont("Helvetica-Bold",24,true);
		//set font
		textFont(f);
		text("Choose red or black:",width/2,height/2-30);
	}
	
	/**
	 * Display Computer Win Text Method
	 * 
	 * Displays text after computer wins.
	 */
	private void displayCompWinText() {
		//set font color
		fill(255);
		//align center
		textAlign(CENTER);
		//initialize font
		f = createFont("Helvetica-Bold",20,true);
		//set font
		textFont(f);
		text("You lost!",width/2,height/2+180);
		text("Click anywhere to play again.",width/2,height/2+200);
	}
	
	/**
	 * Display User Win Text Method
	 * 
	 * Displays text after user wins.
	 */
	private void displayUserWinText() {
		//set font color
		fill(255);
		//align center
		textAlign(CENTER);
		//initialize font
		f = createFont("Helvetica-Bold",20,true);
		//set font
		textFont(f);
		text("You won!",width/2,height/2+180);
		text("Click anywhere to play again.",width/2,height/2+200);
	}
}
