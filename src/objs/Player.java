package objs;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.ClimberMain;

public class Player {
	protected BufferedImage body, wheel, lArm, rArm;
	protected AffineTransform genTrans = new AffineTransform();
	protected Point laPiv, raPiv;

	protected double xVel = 0;
	private double yVel = 0;
	private double x, y;

	private boolean rLatched = false;
	private Point ropeEnd, pointer;
	private double ropeAng;
	private double[] ropeVec = new double[2];

	public int inpDir = 0;
	protected double acc = 3.4;
	protected double dAcc = .1;
	protected double accG = 4;
	protected Point[] boundPts, mvBoundPts;

	public Player(int x, int y) {
		this.x = x;
		this.y = y;
		int[] xList = { 0, 12, 19, 14, 26, 13, 12, -12, -13, -26, -14, -19, -12 };
		int[] yList = { 19, 14, 4, -13, -53, -71, -97, -97, -71, -53, -13, 4, 14 };
		boundPts = new Point[xList.length];
		for (int i = 0; i < xList.length; i++) {
			boundPts[i] = new Point(xList[i], yList[i]);
		}
		mvBoundPts = new Point[xList.length];
		for (int i = 0; i < xList.length; i++)
			mvBoundPts[i] = (Point) boundPts[i].clone();
		genLines(mvBoundPts);
		laPiv = new Point(-14, -56);
		raPiv = new Point(14, -56);
		try {
			body = ImageIO.read(new File(ClimberMain.dir, "/Imgs/Player/Body.png"));
			wheel = ImageIO.read(new File(ClimberMain.dir, "/Imgs/Player/Wheel.png"));
			lArm = ImageIO.read(new File(ClimberMain.dir, "/Imgs/Player/LeftArm.png"));
			rArm = ImageIO.read(new File(ClimberMain.dir, "/Imgs/Player/RightArm.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void updateRequestVelocity() {
		xVel += acc * inpDir;
		xVel = (int) (xVel * (1 - dAcc));
		yVel += accG;
		x += xVel;
		y += yVel;
		updateBounds();
	}

	public void updateBounds() {
		// update transforms
		genTrans.setToTranslation(x, y);
		genTrans.rotate((getxVel()) / 120.0);
		genTrans.transform(boundPts, 0, mvBoundPts, 0, boundPts.length);
	}

	public static Line2D.Float[] genLines(Point[] pts) {
		Line2D.Float[] lines = new Line2D.Float[pts.length];
		for (int i = 0; i < pts.length - 1; i++) {
			int nI = i + 1;
			lines[i] = new Line2D.Float(pts[i].x, pts[i].y, pts[nI].x, pts[nI].y);
		}
		int i = lines.length - 1;
		int nI = 0;
		lines[i] = new Line2D.Float(pts[i].x, pts[i].y, pts[nI].x, pts[nI].y);
		return lines;
	}

	public void draw(Graphics2D g2d, Point cursor) {

		AffineTransform prev = g2d.getTransform();
		AffineTransform drawTrans = (AffineTransform) prev.clone();
		drawTrans.concatenate(genTrans);
		g2d.setTransform(drawTrans);

		g2d.drawImage(body, null, -25, -98);

		g2d.rotate(x / 38.0);
		g2d.drawImage(wheel, null, -18, -18);

		g2d.setTransform(drawTrans);
		if (pointer == null)
			ropeAng = -Math.atan2((cursor.y - y + 56), (cursor.x - x));
		if (Math.abs(ropeAng) > Math.PI / 2) {
			g2d.translate(laPiv.x, laPiv.y);
			g2d.rotate(-ropeAng-5*Math.PI/4);
			g2d.drawImage(lArm, null, -31, -42);
			g2d.setTransform(drawTrans);
			g2d.translate(raPiv.x, raPiv.y);
			g2d.drawImage(rArm, null, -2, -42);
		} else {
			g2d.translate(laPiv.x, laPiv.y);
			g2d.drawImage(lArm, null, -31, -42);
			g2d.translate(raPiv.x - laPiv.x, raPiv.y - laPiv.y);
			g2d.rotate(-ropeAng-7*Math.PI/4);
			g2d.drawImage(rArm, null, -2, -42);
		}
		g2d.setTransform(prev);
		g2d.setColor(Color.red);
		g2d.drawString(String.format("%.2f", ropeAng * 180 / Math.PI), (int) x + 20, (int) y + 20);
		g2d.drawLine((int) x, (int) y - 56, (int) (x + 100 * Math.cos(ropeAng)),
				(int) (y - 56 - 100 * Math.sin(ropeAng)));
//		Line2D.Float[] bounds = genLines(mvBoundPts);
//		for (Line2D.Float l : bounds)
//			g2d.draw(l);
	}

	public void updateRope() {

	}

	public void ropeTo(Point p) {
		pointer = p;
		ropeAng = -Math.atan2((p.y - y + 56), (p.x - x));
		ropeVec[0] = Math.cos(ropeAng);
		ropeVec[1] = Math.sin(ropeAng);
	}

	public void release() {
		pointer = null;
		ropeEnd = null;
		rLatched = false;
	}

	public void forceMove(double x, double y) {
		this.x += x;
		this.y += y;
	}

	public Line2D.Float[] getBounds() {
		genTrans.setToTranslation(x, y);
		genTrans.rotate((getxVel()) / 120.0);
		genTrans.transform(boundPts, 0, mvBoundPts, 0, boundPts.length);
		return genLines(mvBoundPts);
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public double getxVel() {
		return xVel;
	}

	public double getyVel() {
		return yVel;
	}

	public void setyVel(double yVel) {
		this.yVel = yVel;
	}

	public void setxVel(double xVel) {
		this.xVel = xVel;
	}

	public Point[] getMvBoundPts() {
		return mvBoundPts;
	}

}
