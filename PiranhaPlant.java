import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class PiranhaPlant extends Monster {
	double movementPeriod;
	// how long will it take to go up and down once in seconds?
	double startingPosition;
	// how much of the Plant is above ground?
	double yCenterFullyUp;
	// position of the center when the plant is completely out
	double timeElapsed = 0;

	public void drawAt(Graphics g, int pixelx, int pixely) {
		// System.out.println(yCenterFullyUp);
		BufferedImage original = (BufferedImage) Images.piranhaPlantAnimation.get(0);
		BufferedImage drawThis = (original.getSubimage(0, 0, original.getWidth(),
				Math.max((int) (original.getWidth() * getHeight() / getWidth()), 1)));
		g.drawImage(drawThis, pixelx, pixely, (int) (Display.getBLOCK_SIZE_PIXELS() * getWidth()),
				(int) (Display.getBLOCK_SIZE_PIXELS() * getHeight()), null);
	}

	PiranhaPlant(String fileName) {
		String[] pieces = fileName.split(":");
		setxCenter(Double.parseDouble(pieces[1]));
		yCenterFullyUp = (Double.parseDouble(pieces[2]));
		// y Center is when fully up
		startingPosition = Double.parseDouble(pieces[3]);
		movementPeriod = Double.parseDouble(pieces[4]);
		setWidth(1);
		setHeight(2);
		setyCenter(yCenterFullyUp);
		// updatePosition(0);
	}

	public String toString() {
		return "PiranhaPlant:" + getxCenter() + ":" + getyCenter() + ":" + getyCenter() + ":" + movementPeriod;
	}

	public void updatePosition(double seconds) {
		timeElapsed += seconds;
		setHeight(2.0 * Math.sin(-Math.PI * 2.0 / movementPeriod * timeElapsed));
		setyCenter(yCenterFullyUp + 1.0 - getHeight() / 2.0);
		// makes the y position sinusoidal
	}

	public void updateVelocity(double seconds) {
		setyVel(Math.PI * 2.0 / movementPeriod * 2.0 * Math.cos(
				Math.asin((startingPosition - yCenterFullyUp) / 2.0) - Math.PI * 2.0 / movementPeriod * timeElapsed));
		// System.out.println(getyVel());
	}

	public void upCollide(Entity other) {
		if (other instanceof Player) {
			((Player) other).attacked();
		}
		if (other instanceof Monster) {
			if (other instanceof Koopa && ((Koopa) other).shell) {

			} else {
				((Monster) other).disappear();
			}
		}
	}

	public void downCollide(Entity other) {
		if (other instanceof Player) {
			((Player) other).attacked();
		}
		if (other instanceof Monster) {
			if (other instanceof Koopa && ((Koopa) other).shell) {

			} else {
				((Monster) other).disappear();
			}
		}
	}

	public void leftCollide(Entity other) {
		if (other instanceof Player) {
			((Player) other).attacked();
		}
		if (other instanceof Monster) {
			if (other instanceof Koopa && ((Koopa) other).shell) {

			} else {
				((Monster) other).disappear();
			}
		}
	}

	public void rightCollide(Entity other) {
		if (other instanceof Player) {
			((Player) other).attacked();
		}
		if (other instanceof Monster) {
			if (other instanceof Koopa && ((Koopa) other).shell) {

			} else {
				((Monster) other).disappear();
			}
		}
	}
}
