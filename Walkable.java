
public abstract class Walkable extends Moving {
	// class for anything that walks on blocks
	boolean standing; // boolean to determine if the player is standing/walking on a block
	boolean huggingLeftWall; // boolean to determine if the player is touching a wall on the left side
	boolean huggingRightWall; // boolean to determine if the player is touching a wall on the right side

	public boolean detectIsStanding() {
		// determines whether the player is standing on a block
		boolean isStanding = false;
		if ((getyCenter() + getHeight() / 2.0 == Math.floor(getyCenter() + getHeight() / 2.0)
				&& Math.floor(getyCenter() + getHeight() / 2.0) >= 0.0)) {
			// if the bottom of the player edge of the hitbox is at an integer location and
			// that location is >=0
			for (int x = (int) Math.floor(getxCenter() - getWidth() / 2.0); x <= Math
					.floor(getxCenter() + getWidth() / 2.0); x++) {
				// System.out.print(x + ", ");
				if (x >= 0 && x < getGame().getCurrentLevel().getBlocks()[0].length) {
					// x coorindate is within the game map
					isStanding = isStanding || ((getGame().getCurrentLevel()
							.getBlocks()[(int) Math.floor(getyCenter() + getHeight() / 2.0)][x] instanceof Block)
							&& !(getGame().getCurrentLevel().getBlocks()[(int) Math
									.floor(getyCenter() + getHeight() / 2.0)][x] instanceof Air));
				} else {
					isStanding = false;
				}
			}
			// System.out.println("");
		}

		return isStanding;
	}

	public boolean detectIsLeftHuggingWall() {
		// determines whether the player is right next to a wall to the player's left
		boolean isHugging = false;
		if ((getxCenter() - getWidth() / 2.0 == Math.floor(getxCenter() - getWidth() / 2.0)
				&& Math.ceil(getxCenter() - getWidth() / 2.0) - 1 >= 0.0 && Math.ceil(getxCenter() - getWidth() / 2.0)
						- 1 < getGame().getCurrentLevel().getBlocks()[0].length)) {
			// System.out.print(Math.ceil(getxCenter() - getWidth() / 2.0) - 1);
			// if the left edge is at an integer position
			for (int y = (int) Math.floor(getyCenter() - getHeight() / 2.0); y <= Math
					.ceil(getyCenter() + getHeight() / 2.0) - 1; y++) {
				if (y >= 0 && y < getGame().getCurrentLevel().getBlocks().length) {
					// y coorindate is within the game map
					isHugging = isHugging || ((getGame().getCurrentLevel()
							.getBlocks()[y][(int) Math.ceil(getxCenter() - getWidth() / 2.0) - 1] instanceof Block)
							&& !(getGame().getCurrentLevel()
									.getBlocks()[y][(int) (Math.ceil(getxCenter() - getWidth() / 2.0)
											- 1)] instanceof Air));
				} else {
					isHugging = false;
				}
			}
			// System.out.println("");
		}
		return isHugging;
	}

	public boolean detectIsRightHuggingWall() {
		// determines whether the player is right next to a wall to the player's left
		boolean isHugging = false;
		if ((getxCenter() + getWidth() / 2.0 == Math.floor(getxCenter() + getWidth() / 2.0)
				&& Math.floor(getxCenter() + getWidth() / 2.0) >= 0.0
				&& Math.floor(getxCenter() + getWidth() / 2.0) < getGame().getCurrentLevel().getBlocks()[0].length)) {
			// x coordinate is within game map
			// System.out.print(Math.floor(getxCenter() + getWidth() / 2.0) +", ");
			// if the left edge is at an integer position
			for (int y = (int) Math.floor(getyCenter() - getHeight() / 2.0); y <= Math
					.ceil(getyCenter() + getHeight() / 2.0) - 1; y++) {
				// System.out.print(y+", ");
				if (y >= 0 && y < getGame().getCurrentLevel().getBlocks().length) {
					// y coorindate is within the game map
					isHugging = isHugging || ((getGame().getCurrentLevel().getBlocks()[y][(int) Math
							.floor(getxCenter() + getWidth() / 2.0)] instanceof Block)
							&& !(getGame().getCurrentLevel().getBlocks()[y][(int) (Math
									.floor(getxCenter() + getWidth() / 2.0))] instanceof Air));
				} else {
					isHugging = false;
				}
			}
			// System.out.println("");
		}
		return isHugging;
	}

	public void updatePosition(double seconds) {
		super.updatePosition(seconds);
		standing = detectIsStanding();
		huggingLeftWall = detectIsLeftHuggingWall();
		huggingRightWall = detectIsRightHuggingWall();
		// updates the booleans to detect whether the entity is standing on the floor or
		// is next to a wall
	}

	public abstract void updateVelocity(double seconds);

	public void leftCollide(Entity other) {
		if (other instanceof Block && !(other instanceof Air)) {
			setxCenter(other.getxCenter() + other.getWidth() / 2.0 + getWidth() / 2.0);
			// System.out.println(huggingLeftWall);
			setxVel(Math.abs(getxVel()));
			// System.out.println(leftFacing);
		}
	}

	public void rightCollide(Entity other) {
		if (other instanceof Block && other instanceof Block && !(other instanceof Air)) {
			setxCenter(other.getxCenter() - other.getWidth() / 2.0 - getWidth() / 2.0);
			setxVel(-1 * Math.abs(getxVel()));
			// System.out.println(leftFacing);
		}
	}

	public void downCollide(Entity other) {
		if (other instanceof Block && !(other instanceof Air)) {
			setyVel(0);
			setyCenter(other.getyCenter() - other.getHeight() / 2.0 - getHeight() / 2.0);
		}
	}
}
