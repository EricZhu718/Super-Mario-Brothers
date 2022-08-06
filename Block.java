import java.awt.Dimension;
import java.awt.Graphics;

public abstract class Block extends Entity {
	// To represent information about a block in the map file, we write the String
	// from the toString method into the map file
	public abstract String toString();

	// Method that inputs the string of the block description on the file and
	// returns the appropriate object
	Block() {
		super.setHeight(1);
		super.setWidth(1);
	}

	public void setXY(int x, int y) {
		super.setxCenter(x + 1 / 2.0);
		super.setyCenter(y + 1 / 2.0);
		super.setxVel(0);
		super.setyVel(0);
	}

	public void leftCollide(Entity other) {
		// temporarily do nothing
	}

	public void rightCollide(Entity other) {
		// temporarily do nothing
	}

	public void upCollide(Entity other) {
		// temporarily do nothing
	}

	public void downCollide(Entity other) {
		// temporarily do nothing
	}
}
