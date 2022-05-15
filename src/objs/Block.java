package objs;

import java.awt.Point;
import java.awt.geom.Line2D;
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
	private final Line2D.Float[] bounds;
	private final float[][] vecs;
	public final static int width = 151, height = 111;
	private static Block[] blocks;

	public Block(BufferedImage img, Line2D.Float[] bounds, float[][] vecs) {
		this.img = img;
		this.bounds = bounds;
		this.vecs = vecs;
	}

	public void addCollisionVecs(int xBlock, int yBlock, Point[] obj, float[] outVec) {
		Point[] tObj = new Point[obj.length];
		Line2D.Float[] tLines = new Line2D.Float[obj.length];
		int xOff = xBlock * width;
		int yOff = yBlock * height;
		for (int i = 0; i < tObj.length; i++)
			tObj[i] = new Point(obj[i].x - xOff, obj[i].y - yOff);
		tLines = Player.genLines(tObj);
		for (int i = 0; i < bounds.length; i++) {
			for (int j = 0; j < tObj.length; j++) {
				if (bounds[i].intersectsLine(tLines[j])) {
					outVec[0] += getVecs()[i][0];
					outVec[1] += getVecs()[i][1];
					break;
				}
			}
		}
	}

	private static void genBlocks() {
		blocks = new Block[24];
		Line2D.Float[][] areas = genBoundLines();
		float[][][] vecList = genCollisionVectors();
		try {
			BufferedImage master = ImageIO.read(new File(ClimberMain.dir, "/Imgs/Blocks.png"));
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 6; j++) {
					int ind1D = i * 6 + j;
					blocks[ind1D] = new Block(master.getSubimage(i * 153, j * 112, 151, 111), areas[ind1D],
							vecList[ind1D]);
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static float[][][] genCollisionVectors() {
		float[][][] vecs = new float[blocks.length][][];
		vecs[0] = new float[][] { { 0f, 1f }, { .91f, -.414f }, { 0f, -1f }, { -1f, 0f } };
		vecs[1] = new float[][] { { 0f, 1f }, { .91f, -.414f }, { 0f, -1f }, { -1f, 0f } };
		vecs[2] = new float[][] { { 0f, 1f }, { .91f, -.414f }, { -1f, 0f } };
		vecs[3] = new float[][] { { .91f, .414f }, { 0f, -1f }, { -1f, 0f } };
		vecs[4] = new float[][] { { 0f, 1f }, { .91f, .414f }, { 0f, -1f }, { -1f, 0f } };
		vecs[5] = new float[][] { { 0f, 1f }, { .91f, .414f }, { 0f, -1f }, { -1f, 0f } };
		vecs[6] = new float[][] { { 0f, 1f }, { 1f, 0f }, { 0f, -1f }, { -.91f, -.414f }, };
		vecs[7] = new float[][] { { 0f, 1f }, { 1f, 0f }, { 0f, -1f }, { -.91f, -.414f }, };
		vecs[8] = new float[][] { { 0f, 1f }, { 1f, 0f }, { -.91f, -.414f }, };
		vecs[9] = new float[][] { { 1f, 0f }, { 0f, -1f }, { -.91f, .414f }, };
		vecs[10] = new float[][] { { 0f, 1f }, { 1f, 0f }, { 0f, -1f }, { -.91f, .414f }, };
		vecs[11] = new float[][] { { 0f, 1f }, { 1f, 0f }, { 0f, -1f }, { -.91f, .414f }, };
		vecs[12] = new float[][] { { 0f, 1f }, { .74f, -.673f }, { 0f, -1f }, { -1f, 0f } };
		vecs[13] = new float[][] { { 0f, 1f }, { .74f, .673f }, { 0f, -1f }, { -1f, 0f } };
		vecs[14] = new float[][] { { 0f, 1f }, { .591f, -.806f }, { -1f, 0f } };
		vecs[15] = new float[][] { { .591f, .806f }, { 0f, -1f }, { -1f, 0f } };
		vecs[16] = new float[][] { { 0f, 1f }, { 1f, 0f }, { 0f, -1f }, { -1f, 0f } };
		vecs[17] = new float[][] { { 0f, 1f }, { 1f, 0f }, { 0f, -1f }, { -1f, 0f } };
		vecs[18] = new float[][] { { 0f, 1f }, { 1f, 0f }, { 0f, -1f }, { -.74f, -.673f } };
		vecs[19] = new float[][] { { 0f, 1f }, { 1f, 0f }, { 0f, -1f }, { -.74f, .673f } };
		vecs[20] = new float[][] { { 0f, 1f }, { 1f, 0f }, { -.591f, -.806f } };
		vecs[21] = new float[][] { { 1f, 0f }, { 0f, -1f }, { -.591f, .806f } };
		vecs[22] = new float[][] { { 0f, 1f }, { 1f, 0f }, { 0f, -1f }, { -1f, 0f } };
		vecs[23] = new float[][] {};

		return vecs;
	}

	private static Line2D.Float[][] genBoundLines() {
		Line2D.Float[][] a = new Line2D.Float[blocks.length][];
		a[0] = makeLineArray(new int[] { 0, 151, 99, 0 }, new int[] { 0, 0, 111, 111 }, 4);
		a[1] = makeLineArray(new int[] { 0, 100, 50, 0 }, new int[] { 0, 0, 111, 111 }, 4);
		a[2] = makeLineArray(new int[] { 0, 51, 0 }, new int[] { 0, 0, 111 }, 3);
		a[3] = makeLineArray(new int[] { 0, 51, 0 }, new int[] { 0, 111, 111 }, 3);
		a[4] = makeLineArray(new int[] { 0, 51, 100, 0 }, new int[] { 0, 0, 111, 111 }, 4);
		a[5] = makeLineArray(new int[] { 0, 100, 151, 0 }, new int[] { 0, 0, 111, 111 }, 4);
		a[6] = makeLineArray(new int[] { 0, 151, 151, 51 }, new int[] { 0, 0, 111, 111 }, 4);
		a[7] = makeLineArray(new int[] { 51, 151, 151, 100 }, new int[] { 0, 0, 111, 111 }, 4);
		a[8] = makeLineArray(new int[] { 100, 151, 151 }, new int[] { 0, 0, 111 }, 3);
		a[9] = makeLineArray(new int[] { 151, 151, 100 }, new int[] { 0, 111, 111 }, 3);
		a[10] = makeLineArray(new int[] { 100, 151, 151, 50 }, new int[] { 0, 0, 111, 111 }, 4);
		a[11] = makeLineArray(new int[] { 51, 151, 151, 0 }, new int[] { 0, 0, 111, 111 }, 4);
		a[12] = makeLineArray(new int[] { 0, 151, 51, 0 }, new int[] { 0, 0, 111, 111 }, 4);
		a[13] = makeLineArray(new int[] { 0, 51, 151, 0 }, new int[] { 0, 0, 111, 111 }, 4);
		a[14] = makeLineArray(new int[] { 0, 151, 0 }, new int[] { 0, 0, 111 }, 3);
		a[15] = makeLineArray(new int[] { 0, 151, 0 }, new int[] { 0, 111, 111 }, 3);
		a[16] = makeLineArray(new int[] { 0, 99, 99, 0 }, new int[] { 0, 0, 111, 111 }, 4);
		a[17] = makeLineArray(new int[] { 51, 151, 151, 51 }, new int[] { 0, 0, 111, 111 }, 4);
		a[18] = makeLineArray(new int[] { 0, 151, 151, 100 }, new int[] { 0, 0, 111, 111 }, 4);
		a[19] = makeLineArray(new int[] { 100, 151, 151, 0 }, new int[] { 0, 0, 111, 111 }, 4);
		a[20] = makeLineArray(new int[] { 0, 151, 151 }, new int[] { 0, 0, 111 }, 3);
		a[21] = makeLineArray(new int[] { 151, 151, 0 }, new int[] { 0, 111, 111 }, 3);
		a[22] = makeLineArray(new int[] { 0, 151, 151, 0 }, new int[] { 0, 0, 111, 111 }, 4);
		a[23] = makeLineArray(new int[] {}, new int[] {}, 0);
		return a;
	}

	private static Line2D.Float[] makeLineArray(int[] x, int[] y, int num) {
		Line2D.Float[] lines = new Line2D.Float[x.length];
		for (int i = 0; i < x.length; i++) {
			int n = (i + 1) % x.length;
			lines[i] = new Line2D.Float(x[i], y[i], x[n], y[n]);
		}
		return lines;
	}

	public static Block[] getBlockList() {
		if (blocks == null)
			genBlocks();
		return blocks;
	}

	public BufferedImage getImg() {
		return img;
	}

	public Line2D.Float[] getBounds() {
		return bounds;
	}

	public float[][] getVecs() {
		return vecs;
	}
}
