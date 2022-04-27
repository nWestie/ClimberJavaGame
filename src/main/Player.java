package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import objs.Drawable;

public class Player implements Drawable {
	protected BufferedImage body, wheel, lArm, rArm;
	protected AffineTransform wTrans, bTrans, laTrans, raTrans;

	protected double rot = 0;
	protected int x, y;
	protected Rectangle bounds;

	public Player(int x, int y) {
		this.x = x;
		this.y = y;
		bounds = new Rectangle(-25, -100, 50, 115);
		wTrans = new AffineTransform();
		bTrans = new AffineTransform();
		laTrans = new AffineTransform();
		raTrans = new AffineTransform();

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

	public void updatePhysics() {
		bTrans.rotate(Math.PI / 36);
		wTrans.rotate(-Math.PI / 36);
	}

	@Override
	public void draw(Graphics2D g2d) {
		AffineTransform prev = g2d.getTransform();
		AffineTransform genTrans = (AffineTransform) prev.clone();
		genTrans.translate(x, y);
		g2d.setTransform(genTrans);
		g2d.setColor(Color.red);
		g2d.draw(bounds);
		g2d.drawOval(-5, -5, 10, 10);

		g2d.transform(bTrans);
		g2d.drawImage(body, null, -25, -98);
		g2d.setTransform(genTrans);
		g2d.transform(wTrans);
		g2d.drawImage(wheel, null, -12, -12);
		g2d.setTransform(prev);

//		g2d.drawImage(lArm, null, x - 29, y - 2);
	}

}
