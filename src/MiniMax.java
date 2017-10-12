////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
////                                                                        ////
////  MiniMax Algorithm with alfa-beta pruning for Quadrat                  ////
////                                                                        ////
////  Diego Gonzalez                                                        ////
////  December of 2014                                                      ////
////                                                                        ////
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

// We assume that always the player 2 is the CPU. So it tries to
// maximize the result.
public class MiniMax {

    private double Max;
    private int Bi,Bj;	// Best movement
    private WallType Bwt;


    private int depthMax = 1;
    private int level;
    // Creator
    MiniMax(Board B, int l) {
	level = l;
	switch (level) {
	    case 1: 	
		depthMax = 1;	// Does not take into account the 3-wall squares.
		break;
	    case 2: 
		depthMax = 1;
		break;
	    case 3: 
		depthMax = 2;
		break;
	    case 4: 
		depthMax = 4;
		break;
	    case 5: 
		depthMax = 12;
		break;
	}

	int mov = B.countAvaliableWalls(); 
	int de = (int)(6.5/Math.log10(mov));
	if (de < depthMax) depthMax = de;
	double Worst = -10*B.nt*B.nt;	// Worst value
	double Best  =  10*B.nt*B.nt;	// Best value
	Max = Worst;
	Movement M = new Movement();
	do {
	    M = findNewMov(B, M.ind+1);
	    if (M.ind == -1) break;
	    Board b = new Board(B);
	    boolean goal = false;
	    if (M.wt == WallType.HOR) {
		b.H[M.i][M.j] = b.player;
		goal = b.checkSquare(WallType.HOR, M.i,M.j);	
	    } else if (M.wt == WallType.VER) {
		b.V[M.i][M.j] = b.player;
		goal = b.checkSquare(WallType.VER, M.i,M.j);	
	    }
	    if (!goal) b.switchPlayer();
	    double val = alphabeta(b, 1, Worst, Best);
	    if (val > Max) {
	    	Max = val;
		Bi  = M.i;
		Bj  = M.j;
		Bwt = M.wt;
	    }
	} while (true);
    }

    public WallType getBestWT() {
	return Bwt;
    }

    public int getBestI() {
	return Bi;
    }

    public int getBestJ() {
	return Bj;
    }

    // It explores all the solution tree until the maximum depth or
    // a terminal node is reached.
    private double alphabeta(Board B, int depth, double alpha, double beta) {
	boolean maxi;
	if (B.player == 2)  maxi = true;
	else 		    maxi = false;

	Movement M = new Movement();
 	if (B.full()) {
	    return evaluate(B);
	} else if (depth == depthMax) {
	    return evaluate(B);
	} else {
	    do {
		M = findNewMov(B, M.ind+1);
		if (M.ind == -1) break;
	    	Board bt = new Board(B);
		boolean goal = false;
		if (M.wt == WallType.HOR) {
                    bt.H[M.i][M.j] = bt.player;
                    goal = bt.checkSquare(WallType.HOR, M.i,M.j);
            	} else if (M.wt == WallType.VER) {
                    bt.V[M.i][M.j] = bt.player;
                    goal = bt.checkSquare(WallType.VER, M.i,M.j);
                }
		if (!goal) bt.switchPlayer();
		double val = alphabeta(bt, depth+1, alpha, beta);
		if (maxi) {
		    if (val > alpha) alpha = val;
		    if ( beta <= alpha) break;
		} else {
		    if (val < beta)  beta = val;
		    if (beta <= alpha) break;
		}
	    } while (true);
	    if (maxi) return alpha;
	    else      return beta;
  	}
    }


    // Find the possible moves after the movement done before.
    private Movement findNewMov(Board b0, int ind0) {

	Movement M = new Movement();

	int ntnt1 = b0.nt1*b0.nt;
  	int ntm = 2*ntnt1; 	// Total Number of possible moves:
				// (nt+1)*nt + nt*(nt+1)

 	for (int ind=ind0; ind<ntm; ind++) {
	    if (ind < ntnt1) { // Horizontal walls
		int i = ind / b0.nt;
		int j = ind % b0.nt;
		if (0 == b0.H[i][j]) { 
		    M.i = i;
		    M.j = j; 
		    M.wt = WallType.HOR; 
		    M.ind = ind;
		    return M;
		}
	    } else {	// Vertical walls
		int i = (ind-ntnt1) / b0.nt1;
		int j = (ind-ntnt1) % b0.nt1;
                if (0 == b0.V[i][j]) {
		    M.i = i;
		    M.j = j; 
		    M.wt = WallType.VER; 
		    M.ind = ind;
		    return M;
		}
	    }
	}
	return M;
    }

    // Evaluate situation: Gives a score to the current board
    // Player 2 -> MAX
    private double evaluate(Board b) {
	double res = 0;
	int nstw = 0; // Number of squares with three walls.
	for (int i=0; i<b.nt; i++) {
	    for (int j=0; j<b.nt; j++) {
		if      (b.T[i][j] ==  2) res += 10;	
		else if (b.T[i][j] ==  1) res -= 10;
		else if (b.T[i][j] == -1) nstw++;
	    }
	}
	if (level > 1){ // Only takes into account the 3 wall square for level > 1
	    if (b.player == 2) {// It's the turn of max
	    	res += nstw*5;
	    } else {
	        res -= nstw*5;
	    }
	}
	res += Math.random();  // This is to avoid making always the first 
			       // avaliable movement
	return res;
    }
}


class Movement {
    public int i, j;	 // Coordinates of the next move
    public WallType wt; // Wall type of the next move
    public int ind;

    Movement() {
	i = -1;
	j = -1;
	wt = WallType.HOR;
	ind = -1;
    }
}
