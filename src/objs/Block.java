package objs;

import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import main.ClimberMain;

/**
 * Defines the image and collision box for each block type
 * 
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
		Area[] areas = genAreas();
		try {
			BufferedImage master = ImageIO.read(new File(ClimberMain.dir, "/Imgs/Blocks.png"));
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 6; j++) {
					int ind1D = i * 6 + j;
					blocks[ind1D] = new Block(master.getSubimage(i * 153, j * 112, 151, 111), areas[ind1D]);
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private static Area[] genAreas() {
		Area[] a = new Area[blocks.length];
		a[0] = new Area(new Polygon(new int[] { 1, 149, 99, 1 }, new int[] { 0, 0, 110, 110 }, 4));
		a[1] = new Area(new Polygon(new int[] { 1, 100, 50, 1 }, new int[] { 0, 0, 110, 110 }, 4));
		a[2] = new Area(new Polygon(new int[] { 1, 51, 1 }, new int[] { 0, 0, 110 }, 3));
		a[3] = new Area(new Polygon(new int[] { 1, 51, 1 }, new int[] { 0, 110, 110 }, 3));
		a[4] = new Area(new Polygon(new int[] { 1, 51, 100, 1 }, new int[] { 0, 0, 110, 110 }, 4));
		a[5] = new Area(new Polygon(new int[] { 1, 100, 149, 1 }, new int[] { 0, 0, 110, 110 }, 4));
		a[6] = new Area(new Polygon(new int[] { 1, 149, 149, 51 }, new int[] { 0, 0, 110, 110 }, 4));
		a[7] = new Area(new Polygon(new int[] { 51, 149, 149, 100 }, new int[] { 0, 0, 110, 110 }, 4));
		a[8] = new Area(new Polygon(new int[] { 100, 149, 149 }, new int[] { 0, 0, 110 }, 3));
		a[9] = new Area(new Polygon(new int[] { 149, 149, 100 }, new int[] { 0, 110, 110 }, 3));
		a[10] = new Area(new Polygon(new int[] { 100, 149, 149, 50 }, new int[] { 0, 0, 110, 110 }, 4));
		a[11] = new Area(new Polygon(new int[] { 51, 149, 149, 1 }, new int[] { 0, 0, 110, 110 }, 4));
		a[12] = new Area(new Polygon(new int[] { 1, 149, 51, 1 }, new int[] { 0, 0, 110, 110 }, 4));
		a[13] = new Area(new Polygon(new int[] { 1, 51, 149, 1 }, new int[] { 0, 0, 110, 110 }, 4));
		a[14] = new Area(new Polygon(new int[] { 1, 149, 1 }, new int[] { 0, 0, 110 }, 3));
		a[15] = new Area(new Polygon(new int[] { 1, 149, 1 }, new int[] { 0, 110, 110 }, 3));
		a[16] = new Area(new Polygon(new int[] { 1, 99, 99, 1 }, new int[] { 0, 0, 110, 110 }, 4));
		a[17] = new Area(new Polygon(new int[] { 51, 149, 149, 51 }, new int[] { 0, 0, 110, 110 }, 4));
		a[18] = new Area(new Polygon(new int[] { 1, 149, 149, 100 }, new int[] { 0, 0, 110, 110 }, 4));
		a[19] = new Area(new Polygon(new int[] { 100, 149, 149, 1 }, new int[] { 0, 0, 110, 110 }, 4));
		a[20] = new Area(new Polygon(new int[] { 1, 149, 149 }, new int[] { 0, 0, 110 }, 3));
		a[21] = new Area(new Polygon(new int[] { 149, 149, 1 }, new int[] { 0, 110, 110 }, 3));
		a[22] = new Area(new Rectangle(1, 0, 148, 110));
		a[23] = new Area();
		return a;
	}

	public static Block[] getBlockList() {
		if (blocks == null)
			genBlocks();
		return blocks;
	}

	public BufferedImage getImg() {
		return img;
	}

	public Area getBounds() {
		return bounds;
	}
}
