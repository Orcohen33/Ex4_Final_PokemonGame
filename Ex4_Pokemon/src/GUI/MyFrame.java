package GUI;

import api.*;
import gameClients.Agent;
import gameClients.AgentsNPokemons;
import gameClients.Pokemon;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * This class represents a very simple GUI class to present a
 * game on a graph.
 */

public class MyFrame extends JFrame {

	public static final double EPS =0.0001;
	private AgentsNPokemons arena;
	double[] minmaxX = new double[2];
	double[] minmaxY = new double[2];
	private int kRADIUS = 2;
	public static int time, duration = -1, grade, moves, level;
	public MyFrame(String a) {super(a);}

	public void update(AgentsNPokemons ar) {
		this.arena = ar;
		updateFrame();}

	private void updateFrame() {fillminmax();}

	public void paintComponents(Graphics g) {

		int w = this.getWidth();
		int h = this.getHeight();

		BufferedImage resizedImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics2D = resizedImage.createGraphics();

		graphics2D.dispose();

		updateFrame();
		drawBackground(g);
		drawGraph(g);
		drawPokemons(g);
		drawAgants(g);
		drawScore(g);
	}

	private void drawBackground(Graphics g) {
		Image img = Toolkit.getDefaultToolkit().getImage("src/GUI/Images/background.jpg");
		g.drawImage(img,0,0,this.getWidth()+300,this.getHeight()+300,null);
	}
	public void paint(Graphics g){
		this.repaint();
		int w = this.getWidth();
		int h = this.getHeight();

		Image buffer_image;
		Graphics buffer_graphics;
		// Create a new "canvas"
		buffer_image = createImage(w, h);
		buffer_graphics=buffer_image.getGraphics();
		paintComponents(buffer_graphics);
		// Draw on the new "canvas"
		// "Switch" the old "canvas" for the new one
		g.drawImage(buffer_image, 0, 0, this);

	}
	private void drawGraph(Graphics g) {
		DirectedWeightedGraph gg = arena.getGraph();

		Iterator<NodeData> iter= gg.nodeIter();

		while (iter.hasNext()) {
			NodeData n = iter.next();
			g.setColor(Color.blue);
			drawNode(n, 5, g);
			Iterator<EdgeData> itr = gg.edgeIter(n.getKey());
			while (itr.hasNext()) {
				EdgeData e = itr.next();
				g.setColor(Color.gray);
				drawEdge(e, g);
			}
		}
	}
	private void drawPokemons(Graphics g) {
		List<gameClients.Pokemon> fs = arena.getPokemons();
		if (fs != null) {
			Iterator<Pokemon> itr = fs.iterator();

			while (itr.hasNext()) {
				gameClients.Pokemon f = itr.next();
				Point3D c = f.getLocation();
				if (c != null) {
					// DRAW THE POKEMONS
					double ansx = scalex(c.x(),minmaxX[0],minmaxX[1] );
					double ansy = scaley(c.y(),minmaxY[0],minmaxY[1] );
					if(f.getType() < 0){
						Image img = Toolkit.getDefaultToolkit().getImage("src/GUI/Images/pika.png");
						g.drawImage(img,(int) ansx-5,(int) ansy-5, 30, 30 ,null);
					}else {
						Image img = Toolkit.getDefaultToolkit().getImage("src/GUI/Images/squirtle.png");
						g.drawImage(img,(int) ansx-5,(int) ansy-5, 30, 30 ,null);
					}


				}
			}
		}
	}
	private void drawAgants(Graphics g) {
		List<Agent> rs = arena.getAgents();
		//	Iterator<OOP_Point3D> itr = rs.iterator();
		g.setColor(Color.red);
		int i = 0;
		while (rs != null && i < rs.size()) {
			GeoLocation c = rs.get(i).getPos();
			int r = 8;
			i++;
			if (c != null) {
				double ansx = scalex(c.x(),minmaxX[0],minmaxX[1] );
				double ansy = scaley(c.y(),minmaxY[0],minmaxY[1] );
				Image img = Toolkit.getDefaultToolkit().getImage("src/GUI/Images/pokeball.png");
				g.drawImage(img,(int) ansx,(int) ansy, 25, 25 ,null);
//				g.drawImage(img,(int) (ansx), (int) (ansy),null,null);
//				g.fillOval((int) (ansx) + 5, (int) (ansy) + 5, 13, 13);

			}
		}
	}
	private void drawNode(NodeData n, int r, Graphics g) {
		GeoLocation pos = n.getLocation();

		double ansx = scalex(pos.x(),minmaxX[0],minmaxX[1] );
		double ansy = scaley(pos.y(),minmaxY[0],minmaxY[1] );

		g.setColor(Color.BLACK);
//                    g.drawArc((int) (ansx), (int) (ansy), 20, 20, 0, 360);
		g.fillOval((int) (ansx) + 5, (int) (ansy) + 5, 13, 13);

		((Graphics2D) g).setStroke(new BasicStroke(3));
		//node key
		g.drawString(String.valueOf(n.getKey()), (int) (ansx) - kRADIUS, (int) (ansy));
	}
	private void drawEdge(EdgeData e, Graphics g) {
		Iterator<EdgeData> edgeIter = arena.getGraph().edgeIter();

		//Increase font size
		g.setFont(g.getFont().deriveFont(15.0F));
		double x1 = arena.getGraph().getNode(e.getSrc()).getLocation().x();
		double x2 = arena.getGraph().getNode(e.getDest()).getLocation().x();
		double y1 = arena.getGraph().getNode(e.getSrc()).getLocation().y();
		double y2 = arena.getGraph().getNode(e.getDest()).getLocation().y();
		//Edges
		x1 = (int) scalex(x1, minmaxX[0], minmaxX[1]);
		x2 = (int) scalex(x2, minmaxX[0], minmaxX[1]);
		y1 = (int) scaley(y1, minmaxY[0], minmaxY[1]);
		y2 = (int) scaley(y2, minmaxY[0], minmaxY[1]);
		g.setColor(Color.BLACK);
                    ((Graphics2D) g).setStroke(new BasicStroke(3));
		drawArrow(g, (int) (x1) + 10, (int) (y1) + 10, (int)x2 + 10,(int) y2 + 10);

	}
	private void drawScore(Graphics g){
		String lvl = "Level: "+level;
		String Timer = "Timer: "+time+"/"+duration;
		String Grade = "Grade: "+grade;
		String Moves = "Moves: "+moves+"/"+duration*10;
		g.setColor(Color.BLACK);
		g.drawString(lvl,50,50);
		g.drawString(Timer,50,65);
		g.drawString(Grade,50,80);
		g.drawString(Moves,50,95);
	}

	// ==================== PRIVATE METHODS ===============================
	private double scalex(double x, double min, double max) {
		return ((((x - min) * (this.getWidth() - 100)) / (max - min)) + 15);//% (_WIDTH - 50);
	}
	private double scaley(double y, double min, double max) {
		return ((((y - min) * (this.getHeight() - 150)) / (max - min)) + 25);// % (_HEIGHT - 100);
	}
	private void fillminmax() {
		HashMap<Integer, Double> xs = new HashMap<>();
		HashMap<Integer, Double> ys = new HashMap<>();
		if(arena!=null) {
			Iterator<NodeData> it = arena.getGraph().nodeIter();
			while (it.hasNext()) {
				NodeData temp = it.next();
				GeoLocation point = temp.getLocation();
				xs.put(temp.getKey(), point.x());
				ys.put(temp.getKey(), point.y());
			}
		}
		double minx = Collections.min(xs.values());
		double maxx = Collections.max(xs.values());
		double miny = Collections.min(ys.values());
		double maxy = Collections.max(ys.values());
		minmaxX[0] = minx;
		minmaxY[0] = miny;
		minmaxX[1] = maxx;
		minmaxY[1] = maxy;

	}
	private void drawArrow(Graphics g1, int x1, int y1, int x2, int y2) {
		Graphics2D g = (Graphics2D) g1.create();
		int m = 10;
		double dx = x2 - x1, dy = y2 - y1;
		double angle = Math.atan2(dy, dx);
		int len = (int) Math.sqrt(dx * dx + dy * dy);
		AffineTransform at = AffineTransform.getTranslateInstance(x1, y1);
		at.concatenate(AffineTransform.getRotateInstance(angle));
		g.transform(at);
		g.setStroke(new BasicStroke(2));
		// Draw horizontal arrow starting in (0, 0)
		len = len - (int) (EPS * len);
		g.drawLine(0, 0, len + (int) (EPS * len), 0);
		int ARR_SIZE = 11;
		g.fillPolygon(new int[]{len, len - ARR_SIZE, len - ARR_SIZE, len},
				new int[]{0, -ARR_SIZE, ARR_SIZE, 0}, 4);
	}
	//====================================================================
}