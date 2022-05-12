package objs;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.ClimberMain;

public class Player implements Drawable {
	protected BufferedImage body, wheel, lArm, rArm;
	protected AffineTransform genTrans = new AffineTransform();
	protected Point laPiv, raPiv;
	protected double rot = 0;
	protected double xVel = 0;
	private double yVel = 0;
	protected int x, y;
	public int inpDir = 0;
	protected double acc = 4;
	protected double dAcc = .1;
	protected double accG = .5;
	protected Area bounds, movingBounds;

	public Player(int x, int y) {
		this.x = x;
		this.y = y;
		bounds = new Area(new Ellipse2D.Float(-25, -99, 50, 116));
		movingBounds = bounds;
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
		xVel += + acc * inpDir;
		xVel = (int) (xVel * (1 - dAcc));
		yVel += accG;
		x += xVel;
		y += yVel;
		//update transforms
		genTrans.setToTranslation(x, y);
//		genTrans.rotate((getxVel()) / 120.0);
		movingBounds = bounds.createTransformedArea(genTrans);

	}

	@Override
	public void draw(Graphics2D g2d) {
		g2d.setColor(Color.red);
		g2d.draw(movingBounds);

		AffineTransform prev = g2d.getTransform();
		AffineTransform drawTrans = (AffineTransform) prev.clone();
//		genTrans.scale(.9,.9);
		drawTrans.concatenate(genTrans);
		g2d.setTransform(drawTrans);

		g2d.drawImage(body, null, -25, -98);

		g2d.rotate(x / 38.0);
		g2d.drawImage(wheel, null, -18, -18);
//		g2d.fillOval(-18, -1, 3, 3);

		g2d.setTransform(drawTrans);
		g2d.translate(laPiv.x, laPiv.y);
		g2d.drawImage(lArm, null, -31, -42);
		g2d.translate(raPiv.x - laPiv.x, raPiv.y - laPiv.y);
		g2d.drawImage(rArm, null, -2, -42);
		g2d.setTransform(prev);
	}

	public void forceMove(int x, int y) {
		this.x += x;
		this.y += y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
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

	public Area getBounds() {
		return movingBounds;
	}

	public void setxVel(double xVel) {
		this.xVel = xVel;
	}

}
