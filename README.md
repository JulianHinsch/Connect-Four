# Connect Four

Built with Java and the Processing library.

Note: To run this game, you need to first download [Processing](https://github.com/processing/processing)

# How does the 'computer player' work?

There are no more than 7 possible moves at any time in Connect Four.
To win, the player must line up 4 tokens in any direction. 
When choosing a move, the computer player aims to either:

- Place tokens in a continuous line
- Prevent the user from placing tokens in a continuous line

So, at every chance, the computer player assigns a score to each of its possible moves.
This score is based on the neighboring tokens.  

Some examples of moves evaluated with this scoring system:

- 3 neighboring computer tokens in a line: This move is a winner - score: 4.
- 3 neighboring user tokens in a line: This move is necessary to prevent the user from winning - but not as good as the above - score: 3.

If there are multiple moves with the best possible score, one is chosen at random.