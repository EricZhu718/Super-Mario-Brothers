import java.awt.Color;
import java.awt.Graphics;

public class Coin extends Entity {
	int coinAmount = 1;

	Coin(String fileName) {
		String[] partitions = fileName.split(":");
		score = Integer.parseInt(partitions[1]);
		setxCenter(Double.parseDouble(partitions[2]));
		setyCenter(Double.parseDouble(partitions[3]));
		if (4 < partitions.length) {
			coinAmount = Integer.parseInt(partitions[4]);
		}
		setWidth(1);
		setHeight(1);
	}

	public String toString() {
		return ("Coin:" + score + ":" + getxCenter() + ":" + getyCenter() + ":" + coinAmount);
	}

	public void leftCollide(Entity other) {
		if (other instanceof Player) {
			getGame().addCoins(coinAmount);
			disappear();
		}
	}

	public void rightCollide(Entity other) {
		if (other instanceof Player) {
			getGame().addCoins(coinAmount);
			disappear();
		}
	}

	public void downCollide(Entity other) {
		if (other instanceof Player) {
			getGame().addCoins(coinAmount);
			disappear();
		}
	}

	public void upCollide(Entity other) {
		if (other instanceof Player) {
			getGame().addCoins(coinAmount);
			disappear();
		}
	}

	public void drawAt(Graphics g, int pixelx, int pixely) {
		g.setColor(Color.YELLOW);
		g.fillOval(pixelx, pixely, Display.getBLOCK_SIZE_PIXELS(), Display.getBLOCK_SIZE_PIXELS());
		g.setColor(Color.BLACK);
		g.drawOval(pixelx, pixely, Display.getBLOCK_SIZE_PIXELS(), Display.getBLOCK_SIZE_PIXELS());
	}
}
