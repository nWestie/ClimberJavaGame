package objs;

import java.awt.geom.Area;
import java.awt.image.BufferedImage;
/**
 * Defines the image and collision box for each block type
 * @author 210731
 *
 */
public class BlockType {
	private final BufferedImage img;
	private final Area bounds;
	public BlockType(BufferedImage img, Area bounds) {
		this.img = img;
		this.bounds = bounds;
	}
	public BufferedImage getImg() {
		return img;
	}
	public Area getBounds() {
		return bounds;
	}
}
