import java.awt.Color;
import java.awt.Graphics;

class FireBall extends Entity {
	FireBall() {
		setWidth(.75);
		setHeight(.75);
	}

	public void setxCenter(double x) {
		super.setxCenter(x);
		// System.out.println("x: " + x);
	}

	public void setyCenter(double y) {
		super.setyCenter(y);
		// System.out.println("y: " + y);
	}

	public void leftCollide(Entity other) {
		if (other instanceof Monster) {
			((Monster) other).disappear();
		}
		if (other instanceof Player) {
			((Player) other).attacked();
		}
	}

	public void rightCollide(Entity other) {
		if (other instanceof Monster) {
			((Monster) other).disappear();
		}
		if (other instanceof Player) {
			((Player) other).attacked();
		}
	}

	public void upCollide(Entity other) {
		if (other instanceof Monster) {
			((Monster) other).disappear();
		}
		if (other instanceof Player) {
			((Player) other).attacked();
		}
	}

	public void downCollide(Entity other) {
		if (other instanceof Monster) {
			((Monster) other).disappear();
		}
		if (other instanceof Player) {
			((Player) other).attacked();
		}

	}

	public void drawAt(Graphics g, int pixelx, int pixely) {
		g.setColor(Color.red);
		// System.out.println(getxCenter() + ", " + getyCenter());
		g.fillOval(pixelx, pixely, (int) (Display.getBLOCK_SIZE_PIXELS() * this.getWidth()),
				(int) (Display.getBLOCK_SIZE_PIXELS() * this.getHeight()));
		// System.out.println(pixelx + ", " + pixely);
		// System.out.println(pixelx + ", " + pixely);

	}
}