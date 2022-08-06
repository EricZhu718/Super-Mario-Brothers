import java.awt.Color;
import java.awt.Graphics;

public class Mushroom extends PowerUp {
	double speed;
	boolean leftFacing;

	Mushroom(String fileName) {
		super.setHeight(1);
		super.setWidth(1);
		String[] strings = fileName.split(":");
		super.setxCenter(Double.parseDouble(strings[1]));
		super.setyCenter(Double.parseDouble(strings[2]));
		// speed is assumed to be positive
		speed = Double.parseDouble(strings[3]);
		leftFacing = strings[4].equals("left");
		if (leftFacing) {
			setxVel(-1 * speed);
		} else {
			setxVel(speed);
		}
	}

	public String toString() {
		String answer = "Mushroom:" + getxCenter() + ":" + getyCenter() + ":" + speed + ":";
		if (leftFacing) {
			answer += "left";
		} else {
			answer += "right";
		}
		return answer;
	}

	public void leftCollide(Entity other) {
		if (other instanceof Block && !(other instanceof Air)) {
			setxCenter(other.getxCenter() + other.getWidth() / 2.0 + getWidth() / 2.0);
			// System.out.println(huggingLeftWall);
			setxVel(speed);
			// System.out.println(leftFacing);
		}
		if (other instanceof Player) {
			disappear();
			((Player) other).setPlayerMode("Tall Mario");
		}
	}

	public void rightCollide(Entity other) {
		if (other instanceof Block && !(other instanceof Air)) {
			setxCenter(other.getxCenter() - other.getWidth() / 2.0 - getWidth() / 2.0);
			setxVel(-1 * speed);
		}
		if (other instanceof Player) {
			disappear();
			((Player) other).setPlayerMode("Tall Mario");
		}
	}

	public void upCollide(Entity other) {
		if (other instanceof Player) {
			disappear();
			((Player) other).setPlayerMode("Tall Mario");
		}
	}

	public void downCollide(Entity other) {
		if (other instanceof Block && !(other instanceof Air)) {
			setyVel(0);
			setyCenter(other.getyCenter() - other.getHeight() / 2.0 - getHeight() / 2.0);
		}
		if (other instanceof Player) {
			disappear();
			((Player) other).setPlayerMode("Tall Mario");
		}
	}

	public void updateVelocity(double seconds) {
		if (!standing) {
			setyVel(getyVel() + 35 * seconds);
		}
	}

	public void updatePosition(double seconds) {
		super.updatePosition(seconds);
	}

	public void drawAt(Graphics g, int pixelx, int pixely) {
		// System.out.println("Hello");
		g.drawImage(Images.mushroom, pixelx, pixely, (int) (Display.BLOCK_SIZE_PIXELS * 1),
				(int) (Display.BLOCK_SIZE_PIXELS * 1), null);
	}

	public void disappear() {
		super.disappear();
		Entity.getGame().addScore(200);
	}

}
