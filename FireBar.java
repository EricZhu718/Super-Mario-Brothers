import java.awt.Color;
import java.awt.Graphics;

public class FireBar extends Block {

	FireBar thisReference = this;

	double period;
	FireBall[] fireballs;
	double angleRad;
	// from the positive x axis, counterclockwise
	double timeElapsed = 0;

	FireBar(String fileName) {
		String[] splits = fileName.split(":");
		period = Double.parseDouble(splits[1]);
		fireballs = new FireBall[Integer.parseInt(splits[2])];
		for (int i = 0; i < fireballs.length; i++) {
			fireballs[i] = new FireBall();
		}
		angleRad = Double.parseDouble(splits[3]);
	}

	public String toString() {
		return "FireBar:" + period + ":" + fireballs.length + ":" + (angleRad - 2 * Math.PI / period * timeElapsed);
	}

	public void setxCenter(double newX) {
		super.setxCenter(newX);
		for (int i = 0; i < fireballs.length; i++) {
			// counterclockwise in radians
			// System.out.println(angleRad);
			fireballs[i].setyCenter(getyCenter() + 1 * i * Math.sin(angleRad));
			fireballs[i].setxCenter(getxCenter() + 1 * i * Math.cos(angleRad));
		}
	}

	public void setyCenter(double newY) {
		super.setyCenter(newY);
		for (int i = 0; i < fireballs.length; i++) {
			// counterclockwise in radians
			// System.out.println(angleRad);
			fireballs[i].setyCenter(getyCenter() + 1 * i * Math.sin(angleRad));
			fireballs[i].setxCenter(getxCenter() + 1 * i * Math.cos(angleRad));
		}
	}

	public void updatePosition(double seconds) {
		for (int i = 0; i < fireballs.length; i++) {
			getGame().entities.remove(fireballs[i]);
		}
		for (int i = 0; i < fireballs.length; i++) {
			getGame().entities.add(fireballs[i]);
		}
		timeElapsed += seconds;
		angleRad -= 2 * Math.PI * seconds / period;
		for (int i = 0; i < fireballs.length; i++) {
			// counterclockwise in radians
			// System.out.println(angleRad);
			fireballs[i].setyCenter(getyCenter() + 1 * i * Math.sin(angleRad));
			fireballs[i].setxCenter(getxCenter() + 1 * i * Math.cos(angleRad));
		}
		for (int i = 0; i < fireballs.length; i++) {
			// counterclockwise in radians
			// System.out.println(angleRad);
			fireballs[i].setyVel(-i * Math.cos(angleRad) * 2 * Math.PI / period);
			fireballs[i].setxVel(i * Math.sin(angleRad) * 2 * Math.PI / period);
		}
	}

	public void drawAt(Graphics g, int pixelx, int pixely) {
		// getGame().entities.remove(this);
		// adds to the end of the list so that the fireballs load at the end
		g.setColor(Color.darkGray);
		g.fillRect(pixelx, pixely, Display.BLOCK_SIZE_PIXELS, Display.BLOCK_SIZE_PIXELS);
		g.setColor(Color.black);
		g.drawRect(pixelx, pixely, Display.BLOCK_SIZE_PIXELS, Display.BLOCK_SIZE_PIXELS);
		for (int i = 0; i < fireballs.length; i++) {
			fireballs[i].setyCenter(getyCenter() + 1 * i * Math.sin(angleRad));
			fireballs[i].setxCenter(getxCenter() + 1 * i * Math.cos(angleRad));
			// System.out.println(angleRad);
			fireballs[i].drawEntity(g, Display.camX, Display.camY);
		}
	}

}
