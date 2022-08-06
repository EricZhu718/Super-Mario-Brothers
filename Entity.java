import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

public abstract class Entity {
	// superclass for all the parts of the game that move and collide
	// coordinate system is in blocks (1 unit is 1 blocks length)
	public static Dimension screenDim; // Dimension class for the JPanel size
	private static Game game;
	// 1 unit is 1 blocks length
	private double xCenter;
	private double yCenter;
	private double width;
	private double height;

	// xVel and yVel are in blocks per second
	private double xVel;
	private double yVel;

	// each Entity will have a designation number which shows how many Entities have
	// been initialized before this Entity
	// so if this Entity was the ith Entitiy initialized, it would have the
	// designation number i
	public static int lastNumber;
	public int designationNumber;
	int score;
	String selectedEntityString;

	public static boolean collided(Entity one, Entity two) {
		// method that determines whether the two hitboxes overlap
		if (one instanceof Air || two instanceof Air) {
			return false;
		}
		if (one instanceof Block && two instanceof Block) {
			return false;
		}
		if (Math.abs(one.xCenter - two.xCenter) > 3) {
			return false;
		}
		if (Math.abs(one.yCenter - two.yCenter) > 10) {
			return false;
		}
		return Math.abs(one.xCenter - two.xCenter) < one.width / 2.0 + two.width / 2.0
				&& Math.abs(one.yCenter - two.yCenter) < one.height / 2.0 + two.height / 2.0;
	}

	public static void collideAction(Entity one, Entity two) {
		double verticalBordersOverlap = (one.height / 2.0 + two.height / 2.0 - Math.abs(one.yCenter - two.yCenter))
				/ Math.abs(one.yVel - two.yVel);
		// time since vertical borders overlaps
		double horizontalBordersOverlap = (one.width / 2.0 + two.width / 2.0 - Math.abs(one.xCenter - two.xCenter))
				/ Math.abs(one.xVel - two.xVel);
		// time since horizontal borders overlaps
		if (verticalBordersOverlap < horizontalBordersOverlap) {
			// vertical collision
			if (one.yCenter - one.yVel * verticalBordersOverlap < two.yCenter - two.yVel * verticalBordersOverlap) {
				// entity one is on top (up is negative for y coordinates)
				if (one.getyVel() - two.getyVel() > 0) {
					one.downCollide(two);
					two.upCollide(one);
				}
			} else {
				// entity two is on top
				if (one.getyVel() - two.getyVel() < 0) {
					two.downCollide(one);
					one.upCollide(two);
				}
			}
		} else {
			// horizontal collision
			if (one.xCenter - one.xVel * horizontalBordersOverlap > two.xCenter - two.xVel * horizontalBordersOverlap) {
				// entity one is to the left of entity two
				if (one.getxVel() - two.getxVel() < 0) {
					one.leftCollide(two);
					two.rightCollide(one);
				}

			} else {
				// entity one is to the right of entity two
				if (one.getxVel() - two.getxVel() > 0) {
					two.leftCollide(one);
					one.rightCollide(two);
				}
			}
		}
	}

	public abstract void leftCollide(Entity other);
	// you are being hit on the left by the entity other

	public abstract void rightCollide(Entity other);
	// you are being hit on the right by the entity other

	public abstract void upCollide(Entity other);
	// you are being hit on the top by the entity other

	public abstract void downCollide(Entity other);
	// you are being hit on the bottom by the entity other

	// the origin is at the top left of the screen, not bottom left for the
	// coordinate system for pixels
	// pixelx and pixely why will be the top left corner of the block
	public abstract void drawAt(Graphics g, int pixelx, int pixely);

	public void drawEntity(Graphics g, double camX, double camY) {
		// math to draw the block at the right pixels
		int pixelX = (int) ((getxCenter() - getWidth() / 2.0 - camX) * Display.BLOCK_SIZE_PIXELS
				+ screenDim.getWidth() / 2.0);
		int pixelY = (int) ((getyCenter() - getHeight() / 2.0 - camY) * Display.BLOCK_SIZE_PIXELS
				+ screenDim.getHeight() / 2.0);
		if (pixelX > -100 && pixelX < 1600) {
			drawAt(g, pixelX + 5, pixelY + 5);
		}

	}

	public void updatePosition(double seconds) {
		xCenter += seconds * xVel;
		yCenter += seconds * yVel;
	}

	public double getxCenter() {
		return xCenter;
	}

	public void setxCenter(double xCenter) {
		this.xCenter = xCenter;
	}

	public double getyCenter() {
		return yCenter;
	}

	public void setyCenter(double yCenter) {
		this.yCenter = yCenter;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public double getxVel() {
		return xVel;
	}

	public void setxVel(double xVel) {
		this.xVel = xVel;
	}

	public double getyVel() {
		return yVel;
	}

	public void setyVel(double yVel) {
		this.yVel = yVel;
	}

	public static Dimension getScreenDim() {
		return screenDim;
	}

	public static void setScreenDim(Dimension screenDim) {
		Entity.screenDim = screenDim;
	}

	public static Game getGame() {
		return game;
	}

	public static void setGame(Game game) {
		Entity.game = game;
	}

	public void disappear() {
		ArrayList<Entity> entities = game.getCurrentLevel().getEntities();
		entities.remove(this);
		if (this instanceof Block) {
			game.getCurrentLevel().getBlocks()[(int) xCenter][(int) yCenter] = new Air();
		}
		game.addScore(score);
	}

	public static Entity getEntityFromName(String fileName) {
		String Name = fileName.split(":")[0];
		if (Name.equals("Brick")) {
			return new Brick(fileName);
		} else if (Name.equals("Air")) {
			return new Air();
		} else if (Name.equals("Mystery")) {
			return new Mystery(fileName);
		} else if (Name.equals("Coin")) {
			return new Coin(fileName);
		} else if (Name.equals("Goomba")) {
			return new Goomba(fileName);
		} else if (Name.equals("Mushroom")) {
			return new Mushroom(fileName);
		} else if (Name.equals("Koopa")) {
			return new Koopa(fileName);
		} else if (Name.equals("PiranhaPlant")) {
			return new PiranhaPlant(fileName);
		} else if (Name.equals("FireBar")) {
			return new FireBar(fileName);
		} else if (Name.equals("Flagpole")) {
			return new Flagpole(fileName);
		} else if (Name.equals("Player")) {
			return new Player(fileName);
		} else {
			return null;
		}
	}
}
