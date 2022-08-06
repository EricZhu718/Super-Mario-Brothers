import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Koopa extends Monster {
	String type;
	// either says "red" or "green"
	boolean shell;
	boolean rest;
	double walkSpeed;
	double shellSpeed;
	long lastTime;
	int lastRight = 0;
	int lastLeft = 0;

	Koopa(String fileName) {

		long time = System.nanoTime();
		String timeString = Long.toString(time);
		timeString = timeString.substring(0, 6);
		lastTime = Integer.parseInt(timeString);

		String[] strings = fileName.split(":");
		super.setxCenter(Double.parseDouble(strings[1]));
		super.setyCenter(Double.parseDouble(strings[2]));
		// speed is assumed to be positive
		walkSpeed = Double.parseDouble(strings[3]);
		shellSpeed = Double.parseDouble(strings[4]);
		if (!rest) {
			if (!shell) {
				if (strings[4].equals("left")) {
					setxVel(-1 * walkSpeed);
				} else {
					setxVel(walkSpeed);
				}
			} else {
				if (strings[4].equals("left")) {
					setxVel(-1 * shellSpeed);
				} else {
					setxVel(shellSpeed);
				}
			}
		} else {
			setxVel(0);
		}

		type = strings[5];
		shell = strings[6].equals("false");
		rest = strings[7].equals("true");
		if (strings[8].equals("true")) {
			// the next two substrings will detail the x intervals that the goomba can walk
			double firstNumber = Double.parseDouble(strings[9]);
			double secondNumber = Double.parseDouble(strings[10]);
			leftBound = Math.min(firstNumber, secondNumber);
			rightBound = Math.max(firstNumber, secondNumber);
		}
		if (shell) {
			setHeight(.75);
			setWidth(1);
			setxVel(0);
		} else {
			setHeight(1.5);
			setWidth(1);
		}
	}

	public String toString() {
		String answer = "Koopa:" + getxCenter() + ":" + getyCenter() + ":" + walkSpeed + ":" + shellSpeed + ":" + type
				+ ":" + !shell + ":" + rest + ":false";
		return answer;

	}

	private void turnToShell() {
		shell = true;
		rest = true;
		setHeight(.75);
		setWidth(1);
		setxCenter(getxCenter() - .75);
		setxVel(0);
		Entity.getGame().addScore(50);
	}

	public void leftCollide(Entity other) {
		if (other instanceof Player) {
			// lastCollisionWithPlayer = System.currentTimeMillis();
			// System.out.println("Left Collide");
			if (!shell || (shell && getxVel() != 0)) {
				((Player) other).attacked();
			} else if (shell) {
				if (other.getxCenter() < getxCenter()) {
					setxVel(shellSpeed);
					setxCenter(other.getxCenter() + other.getWidth() / 2.0 + getWidth() / 2.0);
				} else {
					setxCenter(other.getxCenter() - other.getWidth() / 2.0 - getWidth() / 2.0);
					setxVel(-shellSpeed);
				}
			}
		}
		if (other instanceof Block && !(other instanceof Air)) {
			setxCenter(other.getxCenter() + other.getWidth() / 2.0 + getWidth() / 2.0);
			// System.out.println(huggingLeftWall);
			setxVel(Math.abs(getxVel()));
			// System.out.println(leftFacing);
		}
		if (other instanceof Monster) {
			if (shell) {
				if (getxVel() != 0) {
					other.disappear();
					if (type.equals("green")) {
						disappear();
					}
				} else {
					if (other.getxCenter() < getxCenter()) {
						setxVel(shellSpeed);
						setxCenter(other.getxCenter() + other.getWidth() / 2.0 + getWidth() / 2.0);
					} else {
						setxCenter(other.getxCenter() - other.getWidth() / 2.0 - getWidth() / 2.0);
						setxVel(-shellSpeed);
					}
				}
			}
		}
	}

	public void rightCollide(Entity other) {
		if (other instanceof Player) {
			// lastCollisionWithPlayer = System.currentTimeMillis();
			// System.out.println("Right Collide");
			if (!shell || (shell && getxVel() != 0)) {
				((Player) other).attacked();
			} else if (shell) {
				if (other.getxCenter() < getxCenter()) {
					setxVel(shellSpeed);
					setxCenter(other.getxCenter() + other.getWidth() / 2.0 + getWidth() / 2.0);
				} else {
					setxVel(-shellSpeed);
					setxCenter(other.getxCenter() - other.getWidth() / 2.0 - getWidth() / 2.0);
				}
			}
		}
		if (other instanceof Block && other instanceof Block && !(other instanceof Air)) {
			setxCenter(other.getxCenter() - other.getWidth() / 2.0 - getWidth() / 2.0);
			setxVel(-1 * Math.abs(getxVel()));
			// System.out.println(leftFacing);
		}
		if (other instanceof Monster) {
			if (shell) {
				if (getxVel() != 0) {
					other.disappear();
					if (type.equals("green")) {
						disappear();
					}
				} else {
					if (other.getxCenter() < getxCenter()) {
						setxVel(shellSpeed);
						setxCenter(other.getxCenter() + other.getWidth() / 2.0 + getWidth() / 2.0);
					} else {
						setxCenter(other.getxCenter() - other.getWidth() / 2.0 - getWidth() / 2.0);
						setxVel(-shellSpeed);
					}
				}
			}
		}
	}

	public void upCollide(Entity other) {
		if (other instanceof Player) {
			// lastCollisionWithPlayer = System.currentTimeMillis();
			// System.out.println("Up Collide");
			if (shell) {
				if (getxVel() != 0) {
					setxVel(0);
					// System.out.println(other.getyCenter());
				} else {
					if (other.getxCenter() < getxCenter()) {
						setxVel(shellSpeed);
					} else {
						setxVel(-shellSpeed);
					}
				}
			} else {
				turnToShell();
			}
			other.setyVel(-10);
			other.setyCenter(getyCenter() - other.getHeight() / 2.0 - getHeight() / 2.0);
		}
	}

	public void downCollide(Entity other) {
		if (other instanceof Player) {
			// lastCollisionWithPlayer = System.currentTimeMillis();
			// System.out.println("Down Collide");
			if (!shell || (shell && getxVel() != 0)) {
				((Player) other).attacked();
			} else if (shell) {
				if (other.getxCenter() < getxCenter()) {
					setxVel(shellSpeed);
				} else {
					setxVel(-shellSpeed);
				}
			}
		}

		if (other instanceof Block && !(other instanceof Air)) {
			setyVel(0);
			setyCenter(other.getyCenter() - other.getHeight() / 2.0 - getHeight() / 2.0);
		}
	}

	public void updateVelocity(double seconds) {
		if (!shell && getxCenter() < leftBound) {
			setxVel(Math.abs(getxVel()));
		}
		if (!shell && getxCenter() > rightBound) {
			setxVel(-Math.abs(getxVel()));
		}
		if (!standing) {
			setyVel(getyVel() + 35 * seconds);
		}
	}

	public Image returnImg() {
		long currentTime = System.currentTimeMillis();
		if (currentTime - lastTime >= 500) {
			if (getxVel() > 0) {
				lastTime = currentTime;
				if (lastRight == 0) {
					lastRight = 1;
					return Images.koopaRightAnimation.get(1);
				} else {
					lastRight = 0;
					return Images.koopaRightAnimation.get(0);
				}

			} else {
				lastTime = currentTime;
				if (lastLeft == 0) {
					lastLeft = 1;
					return Images.koopaLeftAnimation.get(1);
				} else {
					lastLeft = 0;
					return Images.koopaLeftAnimation.get(0);
				}
			}

		} else {
			if (getxVel() > 0) {
				return Images.koopaRightAnimation.get(lastRight);
			}
			return Images.koopaLeftAnimation.get(lastLeft);

		}
	}

	public Image returnRedImg() {
		if (getxVel() > 0) {
			return Images.redKoopaAnimation.get(1);
		}
		return Images.redKoopaAnimation.get(0);
	}

	public void drawAt(Graphics g, int pixelx, int pixely) {
		if (type.equals("green")) {
			if (shell) {
				g.drawImage(Images.koopaAnimation.get(0), pixelx, pixely, (int) (Display.BLOCK_SIZE_PIXELS * 1),
						(int) (Display.BLOCK_SIZE_PIXELS * 1), null);
			} else {
				g.drawImage(returnImg(), pixelx, pixely, (int) (Display.BLOCK_SIZE_PIXELS * 1),
						(int) (Display.BLOCK_SIZE_PIXELS * 2), null);
			}
		} else {
			if (shell) {
				g.drawImage(Images.redShell, pixelx, pixely, (int) (Display.BLOCK_SIZE_PIXELS * 1),
						(int) (Display.BLOCK_SIZE_PIXELS * 1), null);
			} else {
				g.drawImage(returnRedImg(), pixelx, pixely, (int) (Display.BLOCK_SIZE_PIXELS * 1),
						(int) (Display.BLOCK_SIZE_PIXELS * 2), null);
			}

		}

	}
}
