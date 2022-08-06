import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Brick extends Block {
	int coins;
	boolean breakable;

	Brick(String fileName) {

		String[] splits = fileName.split(":");
		coins = Integer.parseInt(splits[1]);
		breakable = splits[2].equals("true");
	}

	public void downCollide(Entity other) {
		if (other instanceof Player) {
			if (coins == 0) {
				if (breakable) {
					if (!(((Player) other).getPlayerMode().equals("Small Mario")))
						disappear();
				}
			} else {
				Entity.getGame().addCoins(1);
				coins--;
			}
		}
	}

	public String toString() {
		return "Brick:" + coins + ":" + breakable;
	}

	public void drawAt(Graphics g, int pixelx, int pixely) {
		g.drawImage(Images.brick, pixelx, pixely, (int) (Display.BLOCK_SIZE_PIXELS * 1),
				(int) (Display.BLOCK_SIZE_PIXELS * 1), null);
	}
}
