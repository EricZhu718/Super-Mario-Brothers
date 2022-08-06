import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class Mystery extends Block {
	// an activated Mystery box will have a boolean activated == true
	boolean activated;
	// mysterbox content
	Entity content;

	Mystery(String fileName) {
		// the fileName is in the form Mystery:[activated]:[fileName for container
		// Moving]
		String[] split = fileName.split(":");
		if (split[1].equals("false")) {
			activated = false;
		} else {
			activated = true;
		}
		content = Entity.getEntityFromName(fileName.substring(2 + split[0].length() + split[1].length()));
	}

	public String toString() {
		return "Mystery:" + activated + ":" + content.toString();
	}

	public void drawAt(Graphics g, int pixelx, int pixely) {
		if (!activated) {
			g.setColor(Color.YELLOW);
		} else {
			g.setColor(Color.gray);
		}
		g.fillRect(pixelx, pixely, Display.BLOCK_SIZE_PIXELS, Display.BLOCK_SIZE_PIXELS);
		g.setColor(Color.black);
		g.drawRect(pixelx, pixely, Display.BLOCK_SIZE_PIXELS, Display.BLOCK_SIZE_PIXELS);
		g.setColor(Color.black);
		g.setFont(new Font("Arial", Font.BOLD, Math.max(Display.BLOCK_SIZE_PIXELS, 0)));

		// draws the question mark of the mystery block at the right position regardless
		// of zoom
		if (!activated) {
			g.drawString("?", pixelx + (int) (g.getFont().getSize() / 4.0),
					pixely + (int) (g.getFont().getSize() / 1.1));
		}
	}

	private void spawnContent() {
		content.setxCenter(getxCenter());
		content.setyCenter(getyCenter() - getWidth() / 2.0 - content.getWidth() / 2.0);
		Entity.getGame().getCurrentLevel().getEntities().add(content);
	}

	public void downCollide(Entity other) {
		super.downCollide(other);
		if (other instanceof Player) {
			if (!activated) {
				spawnContent();
				activated = true;
			}
		}
	}
}