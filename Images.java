import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Images {
	static Image brick;
	static long lastTime;

	static int goombaLastImg = 0;
	static ArrayList<Image> goombaAnimation = new ArrayList<Image>();

	int koopaLastImg = 0;
	static ArrayList<Image> koopaAnimation = new ArrayList<Image>();

	int koopaLiveLastImg = 0;
	static ArrayList<Image> koopaLeftAnimation = new ArrayList<Image>();
	static ArrayList<Image> koopaRightAnimation = new ArrayList<Image>();

	static Image mushroom;

	static Image redShell;
	static ArrayList<Image> redKoopaAnimation = new ArrayList<Image>();
	static ArrayList<Image> piranhaPlantAnimation = new ArrayList<Image>();

	static ArrayList<Image> marioLeftAnimation= new ArrayList<Image>();
	static ArrayList<Image> marioRightAnimation= new ArrayList<Image>();
	static int marioLastImg=0;
	Images() {
		long time = System.currentTimeMillis();
		String timeString = Long.toString(time);

		try {
			brick = ImageIO.read(new File("src/Images/Brick.jpg"));

			goombaAnimation.add(ImageIO.read(new File("src/Images/GoombaLeft.png")));
			goombaAnimation.add(ImageIO.read(new File("src/Images/GoombaRight.png")));

			koopaAnimation.add(ImageIO.read(new File("src/Images/KoopaLeft.png")));
			koopaAnimation.add(ImageIO.read(new File("src/Images/KoopaRight.png")));

			koopaLeftAnimation.add(ImageIO.read(new File("src/Images/KoopaWalk1.png")));
			koopaRightAnimation.add(ImageIO.read(new File("src/Images/KoopaWalk2.png")));
			koopaRightAnimation.add(ImageIO.read(new File("src/Images/KoopaWalk3.png")));
			koopaLeftAnimation.add(ImageIO.read(new File("src/Images/KoopaWalk4.png")));

			mushroom = ImageIO.read(new File("src/Images/Mushroom.png"));

			redShell = ImageIO.read(new File("src/Images/redShell.png"));

			redKoopaAnimation.add(ImageIO.read(new File("src/Images/RedTroopaLeft.png")));
			redKoopaAnimation.add(ImageIO.read(new File("src/Images/RedTroopaRight.png")));

			piranhaPlantAnimation.add(ImageIO.read(new File("src/Images/PiranhaPlantOpen.png")));

			
			marioRightAnimation.add(ImageIO.read(new File("src/Images/MarioRightStill.png")));
			marioRightAnimation.add(ImageIO.read(new File("src/Images/MarioRunRight.png")));
			marioLeftAnimation.add(ImageIO.read(new File("src/Images/MarioLeftStill.png")));
			marioLeftAnimation.add(ImageIO.read(new File("src/Images/MarioRunLeft.png")));
			
		} catch (IOException e) {
			System.out.println("IO error");
		}
	}

	public static Image returnGoomba() {
		long currentTime = System.currentTimeMillis();
		if (currentTime - lastTime >= 500) {
			if (goombaLastImg == 0) {
				goombaLastImg = 1;
				lastTime = currentTime;
				return goombaAnimation.get(1);

			} else {
				goombaLastImg = 0;
				lastTime = currentTime;
				return goombaAnimation.get(0);
			}
		} else {
			return goombaAnimation.get(goombaLastImg);
		}

	}
	public static Image returnPlayer(double xVel, double yVel, boolean left) {
		if(yVel<0) {
			if(left)
				return marioLeftAnimation.get(1);
			return marioRightAnimation.get(1);
		}
		if(yVel==0 && xVel==0) {
			if(left)
				return marioLeftAnimation.get(0);
			return marioRightAnimation.get(0);
		}
		long currentTime = System.currentTimeMillis();
		if (currentTime - lastTime >= 200) {
			lastTime=currentTime;
			if(marioLastImg==0)
				marioLastImg=1;
			else
				marioLastImg=0;
			if(left)
				return marioLeftAnimation.get(marioLastImg);
			return marioRightAnimation.get(marioLastImg);
			
		}
		if(left)
			return marioLeftAnimation.get(marioLastImg);
		return marioRightAnimation.get(marioLastImg);
	}

}
