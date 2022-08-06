import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Player extends Walkable {
	boolean left=true;
	public KeyListener keyImplementation;
	boolean jumpCommand = false;
	boolean leftCommand = false;
	boolean rightCommand = false;
	boolean downCommand = false;

	private String playerMode = "Small Mario";
	// determines whether the player is small, tall, fire, ice, star, penguin, etc

	class DeathAnimation {
		boolean inAnimation = false;
		long startTime;

		public void start() {
			startTime = System.currentTimeMillis();
			inAnimation = true;
		}

		public void update() {
			if (System.currentTimeMillis() - startTime < 500) {
				// wait half a second
			} else if (System.currentTimeMillis() - startTime < 2000) {
				setyCenter(getyCenter() + (System.currentTimeMillis() - startTime - 500) / 100000000.0);
				// System.out.println(getyCenter());
			} else {
				inAnimation = false;
			}
		}
	}

	DeathAnimation death = new DeathAnimation();

	class FlagPoleAnimation {
		Flagpole pole;
		boolean inAnimation = false;
		long startTime;
		double yCenterStart;

		public void start() {
			startTime = System.currentTimeMillis();
			inAnimation = true;
			yCenterStart = getyCenter();
		}

		public void update() {
			// System.out.println("Hello");
			if (System.currentTimeMillis() - startTime < 2000) {
				setyCenter(yCenterStart - (System.currentTimeMillis() - startTime)
						* (yCenterStart - pole.getyCenter() - pole.getHeight() / 2.0 + getHeight() / 2.0) / 2000.0);
				// System.out.println(getyCenter());
			} else {
				inAnimation = false;
			}
		}
	}

	FlagPoleAnimation win = new FlagPoleAnimation();

	Player(double xStart, double yStart) {
		setxCenter(xStart);
		setyCenter(yStart);
		setWidth(1);
		setHeight(1);
		keyImplementation = new Player.keyImplementation();
	}

	Player(String fileName) {
		String[] splits = fileName.split(":");
		setxCenter(Double.parseDouble(splits[1]));
		setyCenter(Double.parseDouble(splits[2]));
		setWidth(1);
		setHeight(1);
		keyImplementation = new Player.keyImplementation();
	}

	public void win() {
		win.start();
		Entity.getGame().getGameTimer().cancel();
		Entity.getGame().getDisplay().repaint();
		Entity.getGame().gameEnded();
	}

	public void attacked() {
		// if Mario hits a monster, this method should be called
		if (System.currentTimeMillis() - Monster.lastCollisionWithPlayer > 1000) {
			// System.out.println("Getting attacked");
			Monster.lastCollisionWithPlayer = System.currentTimeMillis();
			if (!playerMode.equals("Small Mario")) {
				setPlayerMode("Small Mario");
			} else {
				death.start();
				Entity.getGame().getGameTimer().cancel();
				Entity.getGame().getDisplay().repaint();
				Entity.getGame().gameEnded();
			}
		}
	}

	public String toString() {
		return "Player:" + getxCenter() + ":" + getyCenter();
	}

	public void updatePosition(double seconds) {
		super.updatePosition(seconds);
		// System.out.println(
		// "jump: " + jumpCommand + ", " + "down: " + downCommand + ", " + "left: " +
		// leftCommand + ", " + "right: " + rightCommand);
		if (jumpCommand) {
			jumpKeyPressed();
		}
		if (leftCommand) {
			leftWalkPressed();
		}
		if (rightCommand) {
			rightWalkPressed();
		}
		if (downCommand) {
			downPressed();
		}
		Display.camX = getxCenter();
		Display.camY = getGame().getCurrentLevel().getBlocks().length
				- (screenDim.getHeight() + 10) / Display.getBLOCK_SIZE_PIXELS() / 2.0;
		if (getyCenter() > Entity.getGame().getCurrentLevel().getBlocks().length) {
			attacked();
			attacked();
		}
		// System.out.println(Display.camY);
	}

	public void updateVelocity(double seconds) {
		if (!standing) {
			setyVel(Math.min(Math.max(getyVel() + 35 * seconds, -35), 15));
		} else {
			// do nothing
		}
	}

	public void leftCollide(Entity other) {
		// System.out.println(other);
		// if colide with an air block, ignore
		if (!(other instanceof Air)) {
			if (other instanceof Block) {
				setxCenter(other.getxCenter() + other.getWidth() / 2.0 + getWidth() / 2.0);
				setxVel(0);
				// System.out.println("horizontal collision");
			}
		}
		if (other instanceof Flagpole) {
			win.pole = (Flagpole) other;
			win();
		}
	}

	public void rightCollide(Entity other) {
		// System.out.println(other);
		// if colide with an air block, ignore
		if (!(other instanceof Air)) {
			if (other instanceof Block) {
				setxCenter(other.getxCenter() - other.getWidth() / 2.0 - getWidth() / 2.0);
				setxVel(0);
				// System.out.println("horizontal collision");
			}
		}
		if (other instanceof Flagpole) {
			win.pole = (Flagpole) other;
			win();
		}
	}

	public void upCollide(Entity other) {
		// System.out.println(other);
		// if colide with an air block, ignore
		if (!(other instanceof Air)) {
			if (other instanceof Block) {
				if (((int) other.getyCenter() + 1 < Entity.getGame().getCurrentLevel().getBlocks().length
						&& Entity.getGame().getCurrentLevel().getBlocks()[(int) other.getyCenter() + 1][(int) other
								.getxCenter()] instanceof Air)
						|| (int) other.getyCenter() + 1 >= Entity.getGame().getCurrentLevel().getBlocks().length) {
					// the third if statement is to fix a bug where a vertical collision is logged
					// when the player is hugging a wall
					setyCenter(other.getyCenter() + other.getHeight() / 2 + getHeight() / 2);
					setyVel(0);
					// System.out.println("up collision");
				}
			}
		}
		if (other instanceof Flagpole) {
			win.pole = (Flagpole) other;
			win();
		}
	}

	public void downCollide(Entity other) {
		// System.out.println(other);
		// if colide with an air block, ignore
		if (!(other instanceof Air)) {
			if (other instanceof Block) {
				if (((int) other.getyCenter() - 1 >= 0 && Entity.getGame().getCurrentLevel()
						.getBlocks()[(int) other.getyCenter() - 1][(int) other.getxCenter()] instanceof Air)
						|| (int) other.getyCenter() - 1 < 0) {
					// the third if statement is to fix a bug where a vertical collision is logged
					// when the player is hugging a wall
					setyCenter(other.getyCenter() - other.getHeight() / 2 - getHeight() / 2);
					setyVel(0);
					// System.out.println("down collision");
				}
			}
			if (other instanceof Goomba) {
				setyVel(-10);
			}
		}
		if (other instanceof Flagpole) {
			win.pole = (Flagpole) other;
			win();
		}
	}

	public void drawAt(Graphics g, int pixelx, int pixely) {
		if(playerMode.equals("Small Mario")) {
			g.drawImage(Images.returnPlayer(getxVel(), getyVel(), left), pixelx, pixely, (int) (Display.BLOCK_SIZE_PIXELS * 1),
					(int) (Display.BLOCK_SIZE_PIXELS * 1), null);
		}else{
			g.drawImage(Images.returnPlayer(getxVel(), getyVel(), left), pixelx, pixely, (int) (Display.BLOCK_SIZE_PIXELS * 1),
					(int) (Display.BLOCK_SIZE_PIXELS * 2), null);
		}
				
			
	}

	private void jumpKeyPressed() {
		if (standing) {
			setyVel(-18);
		}
	}

	private void leftWalkPressed() {
		if (!huggingLeftWall) {
			setxVel(-10);
			left=true;
		}
	}

	private void rightWalkPressed() {
		if (!huggingRightWall) {
			setxVel(10);
			left=false;
		}
	}

	private void downPressed() {
		if (!standing) {
			setyVel(30);
		}
	}

	private void jumpKeyReleased() {
		// do nothing
	}

	private void leftWalkReleased() {
		setxVel(0);
	}

	private void rightWalkReleased() {
		setxVel(0);

	}

	private void downReleased() {
		// do nothing
	}

	public String getPlayerMode() {
		return playerMode;
	}

	public void setPlayerMode(String playerMode) {
		this.playerMode = playerMode;
		if (playerMode.equals("Tall Mario")) {
			setWidth(1);
			setHeight(2);
		}
		if (playerMode.equals("Small Mario")) {
			double changeInHeight = getHeight() - 1.0;
			setWidth(1);
			setHeight(1);
			setyCenter(getyCenter() + changeInHeight);
		}
	}

	public class keyImplementation implements KeyListener {
		// keycodes for the characters for jumping, walking to the left, walking to the
		// right, and going downwards respectfully
		private int[] jumpKeyCodes = { KeyEvent.getExtendedKeyCodeForChar('w') };
		private int[] leftWalkKeyCodes = { KeyEvent.getExtendedKeyCodeForChar('a') };
		private int[] rightWalkKeyCodes = { KeyEvent.getExtendedKeyCodeForChar('d') };
		private int[] downKeyCodes = { KeyEvent.getExtendedKeyCodeForChar('s') };
		// key codes of the keys that are being pressed down right now

		public void keyPressed(KeyEvent arg0) {
			// System.out.println(arg0.getKeyChar());
			for (int code : jumpKeyCodes) {
				if (code == arg0.getKeyCode()) {
					jumpCommand = true;
				}
			}
			for (int code : leftWalkKeyCodes) {
				if (code == arg0.getKeyCode()) {
					// System.out.println("left");
					leftCommand = true;
				}
			}
			for (int code : rightWalkKeyCodes) {
				if (code == arg0.getKeyCode()) {
					// System.out.println("right");
					rightCommand = true;
				}
			}
			for (int code : downKeyCodes) {
				if (code == arg0.getKeyCode()) {
					// System.out.println("down");
					downCommand = true;
				}
			}
		}

		public void keyReleased(KeyEvent arg0) {
			for (int code : jumpKeyCodes) {
				if (code == arg0.getKeyCode()) {
					// System.out.println("jump");
					jumpKeyReleased();
					jumpCommand = false;
				}
			}
			for (int code : leftWalkKeyCodes) {
				if (code == arg0.getKeyCode()) {
					// System.out.println("left");
					leftWalkReleased();
					leftCommand = false;
				}
			}
			for (int code : rightWalkKeyCodes) {
				if (code == arg0.getKeyCode()) {
					// System.out.println("right");
					rightWalkReleased();
					rightCommand = false;
				}
			}
			for (int code : downKeyCodes) {
				if (code == arg0.getKeyCode()) {
					// System.out.println("down");
					downReleased();
					downCommand = false;
				}
			}
		}

		public void keyTyped(KeyEvent arg0) {
			// System.out.println(arg0.getKeyChar());
		}

	}
}
