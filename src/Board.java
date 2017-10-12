////////////////////////////////////////////////////////////////////////////////
////                                                                        ////
////  Board Class                                                           ////
////                                                                        ////
////////////////////////////////////////////////////////////////////////////////

import java.awt.*;

public class Board {

    final int MAX_NT = 6;

    int     nt;		// Number of tiles
    int     nt1;	// Number of tiles + 1
    int[][] T;		// Tiles
    int[][] H;		// Horizontal walls
    int[][] V;		// Vertical wall
    int player;		// Player
    int[] score;	// Score of this game
    int[] games;	// Number of games won by each player
    int NumGames;	// Number of games 
    int NumGamesT;	// Number of total games
    int net;		// Number of empty tiles
    int naw;		// Number of avaliable walls

    int winner;	// True if the human has won the last game

    Board(int n) {
	this.nt  = n;
	this.nt1 = n + 1;
	this.T = new int[MAX_NT  ][MAX_NT  ];
	this.H = new int[MAX_NT+1][MAX_NT  ];
	this.V = new int[MAX_NT  ][MAX_NT+1];
	for (int i=0; i<nt; i++) {
	    for (int j=0; j<nt; j++) {
		this.T[i][j] = -4;
	    }
	}
	this.player = 1;
	this.score = new int[2];
	this.games = new int[2];
	net = nt*nt;
    }

    // Creator 2: Copy the Board
    Board(Board B) {
        this.nt  = B.nt;
        this.nt1 = B.nt1;
        this.T = new int[MAX_NT  ][MAX_NT  ];
        this.H = new int[MAX_NT+1][MAX_NT  ];
        this.V = new int[MAX_NT  ][MAX_NT+1];
        for (int i=0; i<nt; i++) {
            for (int j=0; j<nt; j++) {
                this.T[i][j] = B.T[i][j];
            }
        }
	for (int i=0; i<nt1; i++) {
	    for (int j=0; j<nt; j++) {
		this.H[i][j] = B.H[i][j];
		this.V[j][i] = B.V[j][i];
	    }
	}
        this.player = B.player;
        this.score = new int[2];
	this.games = new int[2];
	this.score[0] = B.score[0];
	this.score[1] = B.score[1];
	this.games[0] = B.games[0];
	this.games[1] = B.games[1];
        this.net = B.net;
        this.naw = B.naw;
	this.NumGames = B.NumGames;
	this.NumGamesT = B.NumGamesT;

    }

    // Set number of tiles of the board
    public void setSize(int n) {
	if (n > MAX_NT) n = MAX_NT;
	this.nt  = n;
	this.nt1 = n + 1;
	net = nt*nt;
	this.reset();
    }

    // Check if there are any empty square in the board.
    public boolean full() {
	for (int i=0; i<nt; i++) {
	    for (int j=0; j<nt; j++) {
		if (T[i][j] < 0) return false;
	    }
	}
	return true;
    }

    // Count the number of avaliable walls
    public int countAvaliableWalls() {
	this.naw = 0;
	for (int i=0; i<nt1; i++) {
	    for (int j=0; j<nt; j++) {
		if (this.H[i][j] == 0) this.naw++;
		if (this.V[j][i] == 0) this.naw++;
	    }
	}
	return this.naw;
    }

    public void clean() {
	for (int i=0; i<MAX_NT; i++) {
	    for (int j=0; j<MAX_NT; j++) {
		this.T[i][j] = -4;
	    }
	}
	for (int i=0; i<MAX_NT+1; i++) {
	    for (int j=0; j<nt; j++) {
		this.H[i][j] = 0;
		this.V[j][i] = 0;
	    }
	}
	int first = (games[0] + games[1]) % 2;
	player = first+1;
	score[0] = 0;
	score[1] = 0;
	net = nt*nt;
    }

    public void reset() {
	clean();
	player = 1;
	games[0] = 0;
	games[1] = 0;
    }

    public void setGames(int g) {
	NumGames = g;
	NumGamesT = g;
    }

    public void switchPlayer() {
	if (1 == player) player = 2;
	else player = 1;
    }

    // Update the number of walls of the squares involved
    // Return true if one or two squares have been completed.
    public boolean checkSquare(WallType type, int i, int j) {
	boolean goal = false;
	if (WallType.HOR == type) {
	    if (i > 0) {
		T[i-1][j]++;
		if (T[i-1][j] == 0) {
		    T[i-1][j] = player;
		    goal = true;
		    score[player-1]++;
		    net--;
		}
	    }
	    if (i < nt) {
		T[i][j]++;
		if (T[i][j] == 0) {
		    T[i][j] = player;
		    goal = true;
		    score[player-1]++;
		    net--;
		}
	    }
 	} else if (WallType.VER == type) {
	    if (j > 0) {
		T[i][j-1]++;
		if (T[i][j-1] == 0) {
		    T[i][j-1] = player;
		    goal = true;
		    score[player-1]++;
		    net--;
		}
	    }
	    if (j < nt) {
		T[i][j]++;
		if (T[i][j] == 0) {
		    T[i][j] = player;
		    goal = true;
		    score[player-1]++;
		    net--;
		}
	    }
	}
	return goal;
    }

    public int getPointsH() { return score[0];}
    public int getPointsC() { return score[1];}

    public boolean checkWinner() {
	if (score[0] > score[1]) {
	    games[0]++;
	    winner = 1;
	} else if (score[1] > score[0]) {
	    games[1]++;
	    winner = 2;
	} else {
	    winner = 0;
	}
	NumGames--;

	if (0 == NumGames) return true;
	else return false;
    }
}
