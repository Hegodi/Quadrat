////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
////                                                                        ////
////   QUADRAT                                                              ////
////                                                                        ////
////   Strategy game with the implementation of MiniMax algorithm           ////
////                                                                        ////
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
////                                                                        ////
////   Diego Gonzalez                                                       ////
////   November of 2014                                                     ////
////                                                                        ////
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.BoxLayout;
import java.awt.*;
import java.awt.event.*;

import java.io.File;
import java.lang.Object;
import java.lang.Thread;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import javax.sound.sampled.*;

// Main Class
public class Main {

    public static void main(String args[]) {    
	Window window = new Window();
    }
}

// Clase Window; Contains everithng
class Window extends JFrame {
    JButton BtnReset;
    JButton BtnStart;	
    JComboBox CoBoLevel;// Dificulty	
    JComboBox CoBoSize;	// Size of the board
    JComboBox CoBoNuGa;	// Number of games

    Board board;
    GraphicBoard graphicBoard;
    Menu  menu;
    Intro intro;

    boolean playing;
    int level;
    int size;

    public Window() {

	playing = false;

	this.setTitle("Quadrat");
	this.setBounds(0,0,500,525);
	this.setLayout(null);

	// Board
	board = new Board(3);

	// Graphic class to draw the board
        graphicBoard = new GraphicBoard(500,500,board);
	graphicBoard.setVisible(false);

	// Intro
	intro = new Intro(500,500);
	intro.setVisible(true);

	// Menu
	Image cover = intro.createCover(500,200);
	menu = new Menu(500,500, cover);
	menu.setVisible(false);

	this.add(graphicBoard);
	this.add(menu);
	this.add(intro);

	HandlerMouse handlerMouse = new HandlerMouse();
	graphicBoard.addMouseListener(handlerMouse);
	menu.addMouseListener(handlerMouse);

	this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	this.setResizable(false);
	this.setVisible(true);
	
	intro.start();
	menu.setVisible(true);
	intro.setVisible(false);
    }

    // CPU:
    private void cpu() {
	// A.I.
	while (2 == board.player) {
	    MiniMax minimax = new MiniMax(board, level);
	    int i = minimax.getBestI();
	    int j = minimax.getBestJ();
	    WallType wt = minimax.getBestWT();
	    boolean goal = false;
	    if (WallType.HOR == wt) {
		board.H[i][j] = board.player;
		graphicBoard.lastMov.wt = WallType.HOR;
		graphicBoard.lastMov.i  = i;
		graphicBoard.lastMov.j  = j;
		goal = board.checkSquare(WallType.HOR, i,j);
	    } else if (WallType.VER == wt) {
		board.V[i][j] = board.player;
		graphicBoard.lastMov.wt = WallType.VER;
		graphicBoard.lastMov.i  = i;
		graphicBoard.lastMov.j  = j;
		goal = board.checkSquare(WallType.VER, i,j);
	    }
	    if (!goal) board.switchPlayer();
	    graphicBoard.update();
	    try {Thread.sleep(200);} catch(Exception es) { }
	}
    }

    class HandlerMouse implements MouseListener {

	public void mousePressed(MouseEvent e) {
	    Object O = e.getSource();
	    if (graphicBoard == O) {
		if (!playing) return;
		int err = graphicBoard.setWall(e.getX(), e.getY());
		
		if (0 == err) {
		    board.switchPlayer();
		    graphicBoard.update();
		    cpu();
		} else if (err > 0) {
		    graphicBoard.update();
		} else if (-2 == err) {
		    // Exit:
		    playing = false;
		    menu.setVisible(true);	
		    graphicBoard.setVisible(false);
		}
		// Check is the board is full.
	 	if (board.full()) {
		    if (board.checkWinner()) { // End of the match
			graphicBoard.showEndGame();
		        menu.setVisible(true);	
		        playing = false;
		        graphicBoard.setVisible(false);
			return;
		    }
		    graphicBoard.showWinner(board.winner);
		    board.clean();
		    graphicBoard.lastMov.i = -1;
		    graphicBoard.update();
		    try {Thread.sleep(500);} catch(Exception es) { }
		    if (board.player == 2) cpu();
		}
	    } else if (menu == O) {
		if (menu.click(e.getX(), e.getY())) {
		    level = menu.Level;
		    board.setGames(menu.NumGames);
		    graphicBoard.setLevel(level);
		    graphicBoard.setSize(menu.NumTiles);
		    graphicBoard.setWarnings(menu.Warnings);
		    board.reset();
		    graphicBoard.lastMov.i = -1;
		    playing = true;
		    graphicBoard.setVisible(true);
		    menu.setVisible(false);
		} 
	    }
 	}

	public void mouseReleased(MouseEvent e) {
    	}

    	public void mouseEntered(MouseEvent e) {
    	}

    	public void mouseExited(MouseEvent e) {
    	}

    	public void mouseClicked(MouseEvent e) {
    	}
    }
}

