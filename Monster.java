
public abstract class Monster extends Walkable {
	double leftBound = Double.MIN_VALUE;
	// mob will turn to the right after hitting this x value
	double rightBound = Double.MAX_VALUE;
	// mob will turn to the left after hitting this x value
	static long lastCollisionWithPlayer;
	Monster(){
		score = 100;
	}
	public void updateVelocity(double seconds) {
		if (getxCenter() < leftBound) {
			setxVel(Math.abs(getxVel()));
		}
		if (getxCenter() > rightBound) {
			setxVel(-Math.abs(getxVel()));
		}
		if (!standing) {
			setyVel(getyVel() + 35 * seconds);
		}
	}

	public void leftCollide(Entity other) {
		super.leftCollide(other);
		if (other instanceof Player) {
			((Player) other).attacked();
		}
	}

	public void rightCollide(Entity other) {
		super.rightCollide(other);
		if (other instanceof Player) {
			((Player) other).attacked();
		}
	}

	public void upCollide(Entity other) {
		if (other instanceof Player) {
			((Player) other).setyVel(-10);
			disappear();
		}
	}

	public void downCollide(Entity other) {
		super.downCollide(other);
		if (other instanceof Player) {
			((Player) other).attacked();
		}
	}
}
