////////////////////////////////////////////////////////////////////////////////
////                                                                        ////
////  Graphic Menu Class                                                    ////
////                                                                        ////
////////////////////////////////////////////////////////////////////////////////
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.*;
import java.io.InputStream;

public class Menu extends Canvas{

    final int MAX_NT = 6;  // Maximum number of tiles per side
    
    public int NumTiles = 3;
    public int NumGames = 5;
    public int Level = 1;
    public boolean Warnings = true;

    private int width;	
    private int height;

    private Font font;
    private Color colorBG;	// Background color
    private Color colorC1;	// Text color
    private Color colorC2;	// Text color

    private Graphics gra;
    private Image buffer;
    public  Image cover;

    private Image ImgLeft;
    private Image ImgRight;

    int marX, marY;
    final int wd = 5;
    final int le = 6*wd;
    final int lewd = le + wd;
    boolean[] tileOK;
    int[][] title = { {       0,   lewd+wd, wd, le} 
    		     ,{    lewd,   lewd+wd, wd, le} 
    		     ,{      wd,      lewd, le, wd} 
    		     ,{      wd,    2*lewd, le, wd} 
    		     ,{    lewd, 2*lewd+wd, wd, le} 
    		     ,{    lewd, 3*lewd+wd, wd, le} 
    		     ,{      wd,    3*lewd, le, wd} 

    		     ,{  lewd+2*wd, lewd+wd, wd, le} 
    		     ,{2*lewd+2*wd, lewd+wd, wd, le} 
    		     //,{  lewd+3*wd,    lewd, le, wd} 
    		     ,{  lewd+3*wd,  2*lewd, le, wd} 

    		     ,{2*lewd+4*wd, wd, wd, le} 
    		     ,{3*lewd+4*wd, wd, wd, le} 
    		     ,{2*lewd+5*wd,    0   , le, wd} 
    		     ,{2*lewd+4*wd, lewd+wd, wd, le} 
    		     ,{3*lewd+4*wd, lewd+wd, wd, le} 
    		     ,{2*lewd+5*wd,    lewd, le, wd} 
    		     //,{2*lewd+5*wd,  2*lewd, le, wd} 

    		     ,{4*lewd+6*wd,      wd, wd, le} 
    		     ,{3*lewd+6*wd, lewd+wd, wd, le} 
    		     ,{4*lewd+6*wd, lewd+wd, wd, le} 
    		     ,{3*lewd+7*wd,    lewd, le, wd} 
    		     ,{3*lewd+7*wd,  2*lewd, le, wd} 

    		     ,{4*lewd+8*wd, lewd+wd, wd, le} 
    		     //,{5*lewd+8*wd, lewd+wd, wd, le} 
    		     ,{4*lewd+9*wd,    lewd, le, wd} 
    		     //,{4*lewd+9*wd,  2*lewd, le, wd} 

    		     ,{5*lewd+10*wd,      wd, wd, le} 
    		     ,{6*lewd+10*wd,      wd, wd, le} 
    		     ,{5*lewd+11*wd,       0, le, wd} 
    		     ,{5*lewd+10*wd, lewd+wd, wd, le} 
    		     ,{6*lewd+10*wd, lewd+wd, wd, le} 
    		     ,{5*lewd+11*wd,    lewd, le, wd} 
    		     //,{5*lewd+11*wd,  2*lewd, le, wd} 

    		     ,{6*lewd+12*wd,      wd, wd, le} 
    		     ,{6*lewd+12*wd, lewd+wd, wd, le} 
    		     //,{7*lewd+12*wd, lewd+wd, wd, le} 
    		     ,{6*lewd+13*wd,    lewd, le, wd} 
    		     ,{6*lewd+13*wd,  2*lewd, le, wd} 
		};

    Menu(int w, int h, Image c) {
	this.width   = w;
	this.height  = h;
	this.cover   = c;
	this.setBounds(0,0,w,h);
	try {
	    InputStream is = getClass().getResourceAsStream(
						"res/BordersDivide.ttf");
	    font = Font.createFont(Font.TRUETYPE_FONT,is).deriveFont(30f);
	    ImgLeft  = ImageIO.read(getClass().getResourceAsStream(
								"res/left.png"));
	    ImgRight = ImageIO.read(getClass().getResourceAsStream(
								"res/right.png"));
        } catch (Exception e){ }

	this.marX = (w -(18*wd + 7*le))/2;
	this.marY =  150;
	this.tileOK = new boolean[33];
	for (int i=0; i<33; i++) this.tileOK[i] = false;
       	colorBG = new Color(0,0,0);
	colorC1 = new Color(255,255,255);
	colorC2 = new Color(0,0,250);
    }

    public void update() {
	Graphics g = getGraphics();
	draw(g);
    }

    public void paint(Graphics g) {
        draw(g);
    }

    public void draw(Graphics gF) {
	buffer = createImage(width, height);
        gra = buffer.getGraphics();
        gra.setColor(colorBG);
        gra.fillRect(0,0,width,height);
	gra.drawImage(cover,0,0,this);

	gra.setFont(font);
	gra.setColor(colorC2);
	gra.drawString("Size:"    ,70,220);
	gra.drawString("Level:"   ,70,260);
	gra.drawString("N. games:",70,300);
	gra.drawString("Warnings:",70,340);

	gra.setColor(colorC1);
	gra.drawString("START",200,400);
	gra.drawString(""+NumTiles ,330,220);
	gra.drawString(""+Level    ,330,260);
	gra.drawString(""+NumGames ,330,300);
	if (Warnings) gra.drawString("ON",330,340);
	else  gra.drawString("OFF",330,340);

	gra.drawImage(ImgLeft ,280,190,40,30,this);
	gra.drawImage(ImgRight,380,190,40,30,this);

	gra.drawImage(ImgLeft ,280,230,40,30,this);
	gra.drawImage(ImgRight,380,230,40,30,this);

	gra.drawImage(ImgLeft ,280,270,40,30,this);
	gra.drawImage(ImgRight,380,270,40,30,this);

	gra.drawImage(ImgLeft ,280,310,40,30,this);
	gra.drawImage(ImgRight,380,310,40,30,this);

	gF.drawImage(buffer,0,0,this);
    }
    public boolean click(int x, int y) {
	// Start
	if ((x > 200) && (x < 300) && (y > 370) && (y <400)) return true;
	
	// Size
	if ((x > 280) && (x < 320) && (y > 190) && (y <220)) {
	    NumTiles--;
	    if (NumTiles < 3) NumTiles = 3;
	} else if ((x > 380) && (x < 420) && (y > 190) && (y <220)) {
	    NumTiles++;
	    if (NumTiles > 6) NumTiles = 6;
	}
	
	// Level
	if ((x > 280) && (x < 320) && (y > 230) && (y <270)) {
	    Level--;
	    if (Level < 1) Level = 1;
	    //if (Level >= 2) Warnings = false;
	} else if ((x > 380) && (x < 420) && (y > 230) && (y <270)) {
	    Level++;
	    if (Level > 5) Level = 5;
	    //if (Level >= 2) Warnings = false;
	}
	
	// Games
	if ((x > 280) && (x < 320) && (y > 270) && (y <310)) {
	    NumGames--;
	    if (NumGames < 1) NumGames = 1;
	} else if ((x > 380) && (x < 420) && (y > 270) && (y <310)) {
	    NumGames++;
	    if (NumGames > 15) NumGames = 15;
	}
	
	// Warnings
	if ((x > 280) && (x < 320) && (y > 310) && (y <350)) {
	    if (Warnings) Warnings =false;
	    else Warnings = true;
	} else if ((x > 380) && (x < 420) && (y > 310) && (y <350)) {
	    if (Warnings) Warnings =false;
	    else Warnings = true;
	}
	
	update();
	return false;
    }

}
