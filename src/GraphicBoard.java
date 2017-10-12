////////////////////////////////////////////////////////////////////////////////
////                                                                        ////
////  Graphic Board Class                                                   ////
////                                                                        ////
////////////////////////////////////////////////////////////////////////////////
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import javax.imageio.*;

public class GraphicBoard extends Canvas{

    final int MAX_NT = 6;  // Maximum number of tiles per side
    final int height_score = 50;
    final int height_bottom = 50;

    private int width;	
    private int height;
    private int height_total;

    private int th;	// thickness of the walls
    private int le;	// length of the walls
    private int thle;	// thickness + length
    private int marX;	// X margin
    private int marY;	// Y margin

    private Color colorHUM;	// Color Human
    private Color colorCPU;	// Color CPU
    private Color colorE;	// Color empty wall

    private Font font;
    private Font fontB;

    private Image TileHum;
    private Image TileCpu;
    private Image TileW1;
    private Image TileW2;

    private boolean warnings;
    private int level;

    private Graphics gra;
    private Image buffer;

    private Board B;

    public Movement lastMov;

    GraphicBoard(int w, int h, Board B) {
	lastMov =new Movement();	
	lastMov.i = -1;
	this.width         = w;
	this.height_total  = h;
	this.height = height_total - height_score - height_bottom;
  	this.colorCPU = new Color(0,0,255);
  	this.colorHUM = new Color(250,250,0);
	this.colorE  = new Color(100,100,100);
	this.setBounds(0,0,this.width,this.height_total);
	this.B = B;

	try {
	    InputStream is1 = getClass().getResourceAsStream(
						"res/BordersDivide.ttf");
	    InputStream is2 = getClass().getResourceAsStream(
						"res/BordersDivide.ttf");
	    font  = Font.createFont(Font.TRUETYPE_FONT,is1).deriveFont(35f);
	    fontB = Font.createFont(Font.TRUETYPE_FONT,is2).deriveFont(60f);

	    //TileW1   = ImageIO.read(new File("res/squareW1.png"));
	    //TileW2   = ImageIO.read(new File("res/squareW2.png"));
	    //TileHum  = ImageIO.read(new File("res/squareHum.png"));
	    //TileCpu  = ImageIO.read(new File("res/squareCpu.png"));
	    TileW1 = ImageIO.read(getClass().getResourceAsStream(
							"res/squareW1.png"));
	    TileW2 = ImageIO.read(getClass().getResourceAsStream(
							"res/squareW2.png"));
	    TileHum = ImageIO.read(getClass().getResourceAsStream(
							"res/squareHum.png"));
	    TileCpu = ImageIO.read(getClass().getResourceAsStream(
							"res/squareCpu.png"));
        } catch (Exception e){ }

	setSize(B.nt);
    }

    public void update() {
	Graphics g = getGraphics();
	draw(g);
    }

    public void paint(Graphics g) {
        draw(g);
    }

    public void setSize(int n) {
	if (n > MAX_NT) n = MAX_NT;
	B.setSize(n);
	int min = (width > height ? height : width);
	int a = 8;
	double th_d  = (double)min/((8+1)*B.nt+1);
	this.th   = (int) th_d;
	this.le   = 8*th;
	this.thle = th+le;
	this.marX = (width - B.nt*le - B.nt1*th)/2;
	this.marY = (height - B.nt*le - B.nt1*th)/2;
	this.marY += height_score;
    }

    public void setLevel(int l) {
	level = l;
    }

    public void setWarnings(boolean w) {
	warnings = w;
    }

    public void draw(Graphics gF) {
        buffer = createImage(width, height_total);
        gra = buffer.getGraphics();
	Color cFondo = new Color(0,0,0);
        gra.setColor(cFondo);
        gra.fillRect(0,0,width,height_total);

	// Exit
	gra.setFont(font);
        gra.setColor(Color.white);
        gra.drawString( "Exit",420,height_total-15);

	// Games
        gra.drawString("Game "+(B.NumGamesT-B.NumGames+1)+" of "+B.NumGamesT
			,20,height_total-15);

	// Level
        gra.drawString("Level: "+level,250,height_total-15);

	// Score
	gra.setFont(font);
        gra.setColor(colorHUM);
        gra.drawString( "YOU", 50,30);
        gra.drawString("("+B.games[0]+")", 140,30);
        gra.drawString(""+B.score[0], 190,30);
        gra.setColor(colorCPU);
        gra.drawString("CPU",290,30);
        gra.drawString("("+B.games[1]+")",380,30);
        gra.drawString(""+B.score[1], 430,30);

	if (B.player == 1) {
            gra.setColor(colorHUM);
	    gra.fillOval(20,10,20,20);
	} else if (B.player == 2) {
            gra.setColor(colorCPU);
	    gra.fillOval(260,10,20,20);
	}


	// Horizontal walls
        for (int i=0; i<B.nt1; i++) {
            for (int j=0; j<B.nt; j++) {
		if      (0 == B.H[i][j]) gra.setColor(colorE);
		else if (1 == B.H[i][j]) gra.setColor(colorHUM);
		else if (2 == B.H[i][j]) gra.setColor(colorCPU);
		int x = marX + j*thle+th;
		int y = marY + i*thle;
		gra.fillRect(x, y, le, th);
	    }
        }

	// Vertical walls
        for (int i=0; i<B.nt; i++) {
            for (int j=0; j<B.nt1; j++) {
		if      (0 == B.V[i][j]) gra.setColor(colorE);
		else if (1 == B.V[i][j]) gra.setColor(colorHUM);
		else if (2 == B.V[i][j]) gra.setColor(colorCPU);
		int x = marX + j*thle;
		int y = marY + i*thle+th;
		gra.fillRect(x, y, th, le);
	    }
        }

	// Tiles
        for (int i=0; i<B.nt; i++) {
            for (int j=0; j<B.nt; j++) {
		int x = marX + j*thle+th;
		int y = marY + i*thle+th;
		if (warnings) {
		    if (-2==B.T[i][j]) gra.drawImage(TileW1 ,x,y,le,le,this);
		    else if (-1==B.T[i][j]) gra.drawImage(TileW2 ,x,y,le,le,this);
		}
		if ( 1 == B.T[i][j]) gra.drawImage(TileHum,x,y,le,le,this);
		else if ( 2 == B.T[i][j]) gra.drawImage(TileCpu,x,y,le,le,this);
	    }
        }

	// Last wall:
        gra.setColor(Color.red);
	if (lastMov.i != -1) {
	    if (lastMov.wt == WallType.HOR) {
	    	int x = marX + lastMov.j*thle+th;
	    	int y = marY + lastMov.i*thle;
	    	gra.fillRect(x+3, y+3, le-6, th-6);
 	    } else if (lastMov.wt == WallType.VER){
	    	int x = marX + lastMov.j*thle;
	    	int y = marY + lastMov.i*thle+th;
	    	gra.fillRect(x+3, y+3, th-6, le-6);
	    } 
	}

        gF.drawImage(buffer,0,0,this);
    }

    public void showWinner(int win) {
        gra = buffer.getGraphics();
	
	gra.setFont(font);
	gra.setColor(Color.gray);
	gra.fillRect(180,200,140,80);
	if (1 == win) {
	     gra.setColor(colorHUM);
	     gra.drawString("YOU win!!",190,250);
	} else if (2 == win) {
	     gra.setColor(colorCPU);
	     gra.drawString("CPU win!!",190,250);
	} else {
	     gra.setColor(Color.white);
	     gra.drawString("TIED",210,250);
	}
	Graphics g = getGraphics();
        g.drawImage(buffer,0,0,this);
	try {Thread.sleep(2000);} catch(Exception es) { }
    }

    public void showEndGame() {
        buffer = createImage(width, height_total);
        gra = buffer.getGraphics();
	
	gra.setFont(fontB);
	gra.setColor(Color.black);
	gra.fillRect(50,50,400,400);
	
	gra.setColor(colorHUM);
	gra.drawString("YOU",100,150);
	gra.drawString(""+B.games[0],130,220);

	gra.setColor(colorCPU);
	gra.drawString("CPU",310,150);
	gra.drawString(""+B.games[1],340,220);

	if (B.games[0] > B.games[1]) {
	    gra.setColor(colorHUM);
	    gra.drawString("YOU win!!",150,350);
	} else if (B.games[0] < B.games[1]) {
	    gra.setColor(colorCPU);
	    gra.drawString("CPU win!!",150,350);
  	} else {
	    gra.setColor(Color.white);
	    gra.drawString("TIED",190,350);
	}
	Graphics g = getGraphics();
        g.drawImage(buffer,0,0,this);
	try {Thread.sleep(3000);} catch(Exception es) { }
    }

    // Try to set a wall. Return 0 if it succeed and no square has been completed,
    // otherwise return the player who got the square.
    // EXIT: return -2 to come back to the menu.
    public int setWall(int x, int y) {
	int ret = -1;
	// Horizontal walls
        for (int i=0; i<B.nt1; i++) {
	    if (ret != -1) break;
            for (int j=0; j<B.nt; j++) {
		int x0 = marX + j*thle+th;
		int y0 = marY + i*thle;
		int x1 = x0 + le;
		int y1 = y0 + th;
		if ((x > x0) && (x < x1) && (y > y0) && (y < y1)) {
		    if (0 == B.H[i][j]) {
			lastMov.wt = WallType.HOR;
			lastMov.i = i;
			lastMov.j = j;
			B.H[i][j] = B.player;
			// Check squares
			if (B.checkSquare(WallType.HOR, i,j)) ret = B.player;
			else ret = 0;
		    }
		}
	    }
        }

	// Vertical walls
        for (int i=0; i<B.nt; i++) {
	    if (ret != -1) break;
            for (int j=0; j<B.nt1; j++) {
		int x0 = marX + j*thle;
		int y0 = marY + i*thle+th;
		int x1 = x0 + th;
		int y1 = y0 + le;
		if ((x > x0) && (x < x1) && (y > y0) && (y < y1)) {
		    if (0 == B.V[i][j]) {
			lastMov.wt = WallType.VER;
			lastMov.i = i;
			lastMov.j = j;
			B.V[i][j] = B.player;
			// Check squares
			if (B.checkSquare(WallType.VER, i,j)) ret = B.player;
			else ret = 0;
		    }
		}
	    }
        }

	// EXIT
	if ((x > 420) && (y > height + marY-10)) {
	    return -2;
	}
	return ret;
    }
}
