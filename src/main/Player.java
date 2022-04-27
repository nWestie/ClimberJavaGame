package main;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import objs.Drawable;

public class Player extends Rectangle implements Drawable {
	BufferedImage body,wheel, lArm, rArm;
	public Player(int x, int y) {
		super(x,y,70,80);
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
	@Override
	public void draw(Graphics2D g2d) {
		g2d.drawImage(body, null, x,y);
	}

}
