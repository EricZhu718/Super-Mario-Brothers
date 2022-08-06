import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Goomba extends Monster {

	Goomba(String fileName) {

		setHeight(1);
		setWidth(1);
		String[] strings = fileName.split(":");
		super.setxCenter(Double.parseDouble(strings[1]));
		super.setyCenter(Double.parseDouble(strings[2]));
		// speed is assumed to be positive
		double speed = Double.parseDouble(strings[3]);
		if (strings[4].equals("left")) {
			setxVel(-1 * speed);
		} else {
			setxVel(speed);
		}
		if (strings[5].equals("true")) {
			// the next two substrings will detail the x intervals that the goomba can walk
			double firstNumber = Double.parseDouble(strings[6]);
			double secondNumber = Double.parseDouble(strings[7]);
			leftBound = Math.min(firstNumber, secondNumber);
			rightBound = Math.max(firstNumber, secondNumber);
		}
	}

	public String toString() {
		String answer = "Goomba:" + getxCenter() + ":" + getyCenter() + ":" + Math.abs(getxVel()) + ":";
		if (getxVel() < 0) {
			answer += "left:";
		} else {
			answer += "right:";
		}
		answer += "false";
		return answer;
	}

	public void leftCollide(Entity other) {
		super.leftCollide(other);
	}

	public void rightCollide(Entity other) {
		super.rightCollide(other);
	}

	public void upCollide(Entity other) {
		super.upCollide(other);
	}

	public void downCollide(Entity other) {
		super.downCollide(other);
	}

	public void updateVelocity(double seconds) {
		super.updateVelocity(seconds);

	}

	public void updatePosition(double seconds) {
		super.updatePosition(seconds);
	}

	public void drawAt(Graphics g, int pixelx, int pixely) {
		g.drawImage(Images.returnGoomba(), pixelx, pixely, (int) (Display.BLOCK_SIZE_PIXELS * 1),
				(int) (Display.BLOCK_SIZE_PIXELS * 1), null);
	}

}
