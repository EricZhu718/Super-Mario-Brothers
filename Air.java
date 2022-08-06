import java.awt.Graphics;

public class Air extends Block {

	public String toString() {
		return "Air";
	}

	public void drawAt(Graphics g, int pixelx, int pixely) {
		// intentionally does nothing because you don't draw anything for air
	}

	public void leftCollide(Entity other) {
		// do nothing
	}

	public void rightCollide(Entity other) {
		// do nothing

	}

	public void upCollide(Entity other) {
		// do nothing

	}

	public void downCollide(Entity other) {
		// do nothing

	}

}
