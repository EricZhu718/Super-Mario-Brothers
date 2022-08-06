import java.awt.Graphics;

public class Pipe extends Block {

	// comes out to this pipe, multiple pipes can go to one exit pipe
	private Pipe otherPipe;
	private String direction; // can either be "to other pipe", "from other pipe", or "nothing"
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void drawAt(Graphics g, int pixelx, int pixely) {
		// TODO Auto-generated method stub

	}

	public Pipe getOtherPipe() {
		return otherPipe;
	}

	public void setOtherPipe(Pipe otherPipe) {
		this.otherPipe = otherPipe;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

}
