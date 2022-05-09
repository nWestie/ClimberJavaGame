package objs;

import java.awt.Rectangle;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import main.ClimberMain;
/**
 * Defines the image and collision box for each block type
 * @author 210731
 *
 */
public class Block {
	private final BufferedImage img;
	private final Area bounds;
	private static Block[] blocks; 
	public Block(BufferedImage img, Area bounds) {
		this.img = img;
		this.bounds = bounds;
	}
	private static void genBlocks() {
		blocks = new Block[24];
		Block[] b = blocks;
		try {
			BufferedImage master = ImageIO.read(new File(ClimberMain.dir, "/Imgs/Blocks.png"));
			Area def = new Area(new Rectangle(0,0,151,111));
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 6; j++) {
					blocks[i*6+j] = new Block(master.getSubimage(i*153,j*112,151,111),def);
				}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public static Block[] getBlockList() {
		if(blocks == null) genBlocks();
		return blocks;
	}
	public BufferedImage getImg() {
		return img;
	}
	public Area getBounds() {
		return bounds;
	}
}
