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

public class Intro extends Canvas{

    private int width;	
    private int height;

    private Graphics gra;
    private Image buffer;
    private Font font;

    int marX, marY;
    int wd;
    int le;
    int lewd;
    boolean[] tileOK;
    int[][] title;


    Intro(int w, int h) {
	this.width   = w;
	this.height  = h;
	this.setBounds(0,0,w,h);
	try {
	    InputStream is = getClass().getResourceAsStream(
						"res/BordersDivide.ttf");
	    font = Font.createFont(Font.TRUETYPE_FONT,is).deriveFont(25f);
        } catch (Exception e){

        }
	wd = 8;
	le = 5*wd;
	lewd = wd+le;
	this.marX = (w -(18*wd + 7*le))/2;
	this.marY =  150;
	this.tileOK = new boolean[33];
	for (int i=0; i<33; i++) this.tileOK[i] = false;
    }

    private int[][] makeTitle() {
	int ret[][] =  { {       0,   lewd+wd, wd, le} 
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
		} ;
	return ret;
    }

    public void update() {
	Graphics g = getGraphics();
	draw(g);
    }

    public void paint(Graphics g) {
        draw(g);
    }

    public void draw(Graphics gF) {
	gF.drawImage(buffer,0,0,this);
    }
    
    public void start() {
	wd = 8;
	le = 5*wd;
	lewd = wd+le;
	this.marX = (width -(18*wd + 7*le))/2;
	this.marY =  150;
	title = makeTitle();

	buffer = createImage(width, height);
       	gra = buffer.getGraphics();
       	gra.setColor(Color.black);
       	gra.fillRect(0,0,width,height);
	update();
       	gra.setColor(Color.white);
	int left = 33;
	while (left > 0) {
	    boolean done = false;
	    while (!done) {
	    	int ind = (int)(33*Math.random());
	    	if (!tileOK[ind]) {
		    tileOK[ind] = true;
		    left--;
		    done = true;
	    	}
	    }
	    
	    for (int i=0; i<33; i++) {
		if (tileOK[i]) {
       		    gra.setColor(Color.white);
		    gra.fillRect(marX + title[i][0], marY + title[i][1]
						, title[i][2], title[i][3]);
		    gra.setColor(new Color(0, 0, 50+(int)(200*Math.random())));
		    gra.fillRect(2+marX + title[i][0], 2+marY + title[i][1]
						, title[i][2], title[i][3]);
		}
	    }
	    waitMS(100);
	    update();
	}
	for (int i=0; i<33; i++) {
	    if (tileOK[i]) {
       		gra.setColor(Color.white);
		gra.fillRect(marX + title[i][0], marY + title[i][1]
						, title[i][2], title[i][3]);
       		gra.setColor(Color.blue);
		gra.fillRect(2+marX + title[i][0], 2+marY + title[i][1]
						, title[i][2], title[i][3]);
	    }
	}
	gra.setFont(font);
	gra.setColor(Color.white);
	gra.drawString("Diego Gonzalez",300,320);
	gra.drawString("2014",240,450);
	update();
	waitMS(1500);
    }

    private void waitMS(int ms) {
	try {
	    Thread.sleep(ms);
	} catch (Exception e) { }
    }

    public BufferedImage createCover(int w, int h) {
	BufferedImage img = new BufferedImage(w,h, BufferedImage.TYPE_INT_RGB);
	gra = img.getGraphics();
	wd = 5;
	le = 6*wd;
	lewd = le + wd;
	marX = (w -(18*wd + 7*le))/2;
	marY = 30;
	title = makeTitle();
	for (int i=0; i<33; i++) {
	    gra.setColor(Color.white);
	    gra.fillRect(marX + title[i][0], marY + title[i][1]
						, title[i][2], title[i][3]);
	    gra.setColor(Color.blue);
	    gra.fillRect(2+marX + title[i][0], 2+marY + title[i][1]
						, title[i][2], title[i][3]);
	}
	gra.setFont(font);
	gra.setColor(Color.white);
	gra.drawString("Diego Gonzalez",300,300);

	return img;
    }

}
