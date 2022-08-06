import java.awt.Color;
import java.awt.Graphics;

public class Flagpole extends Entity {
	double flagPos = 8;

	// 0 is all the way at the bottom, getHeight() is all the way at the top
	Flagpole(String fileName) {
		String[] splits = fileName.split(":");
		setxCenter(Double.parseDouble(splits[1]));
		setyCenter(Double.parseDouble(splits[2]));
		setWidth(0.1);
		setHeight(8);
	}

	public String toString() {
		return "Flagpole:" + getxCenter() + ":" + getyCenter();
	}

	public void leftCollide(Entity other) {
		
	}

	public void rightCollide(Entity other) {
		
	}

	public void upCollide(Entity other) {
		
	}

	public void downCollide(Entity other) {
		
	}

	public void drawAt(Graphics g, int pixelx, int pixely) {
		g.setColor(Color.GRAY);
		g.fillRect(pixelx, pixely, (int) (getWidth() * Display.getBLOCK_SIZE_PIXELS()),
				(int) (getHeight() * Display.getBLOCK_SIZE_PIXELS()));
		// draws flag
		g.setColor(Color.WHITE);
		g.fillPolygon(new int[] { pixelx, pixelx, pixelx - Display.getBLOCK_SIZE_PIXELS() },
				new int[] { pixely - (int) ((flagPos - getHeight()) * Display.getBLOCK_SIZE_PIXELS()),
						pixely - (int) ((flagPos - .75 - getHeight()) * Display.getBLOCK_SIZE_PIXELS()),
						pixely - (int) ((flagPos - getHeight()) * Display.getBLOCK_SIZE_PIXELS()) },
				3);
	}

}
