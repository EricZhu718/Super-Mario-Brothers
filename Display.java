import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.*;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;

public class Display extends JPanel {
	private JFrame frame;
	private Game game;
	private Display thisDisplay = this;

	// X and Y coordinates of the left-top of the screen in terms of blocks
	static double camX;
	static double camY;

	// explains what the JPanel is displaying so that the paintComponent knows what
	// to draw
	// changes the size of the blocks on the screen
	static int BLOCK_SIZE_PIXELS;
	Screen currentScreen;

	abstract class Screen implements MouseMotionListener, MouseListener, MouseWheelListener {
		public abstract void draw(Graphics g);

		public void mouseDragged(MouseEvent e) {

		}

		public void mouseMoved(MouseEvent e) {

		}

		public void mouseClicked(MouseEvent e) {

		}

		public void mouseEntered(MouseEvent e) {

		}

		public void mouseExited(MouseEvent e) {

		}

		public void mousePressed(MouseEvent e) {

		}

		public void mouseReleased(MouseEvent e) {

		}

		public void mouseWheelMoved(MouseWheelEvent e) {

		}
	}

	class WelcomeScreen extends Screen {
		WelcomeScreen() {
			removeAll();
			game = new Game(thisDisplay);
			while (getMouseListeners().length > 0) {
				removeMouseListener(getMouseListeners()[0]);
			}
			while (getMouseMotionListeners().length > 0) {
				removeMouseMotionListener(getMouseMotionListeners()[0]);
			}
			while (getMouseWheelListeners().length > 0) {
				removeMouseWheelListener(getMouseWheelListeners()[0]);
			}
			JButton mapSelection = new JButton("Maps");
			JButton mapMaker = new JButton("Make a Map");
			mapSelection.setSize(new Dimension(150, 75));
			mapSelection.setLocation(getPreferredSize().width / 2 - 100 - mapSelection.getWidth() / 2,
					getPreferredSize().height - 100);
			mapMaker.setSize(new Dimension(150, 75));
			mapMaker.setLocation(getPreferredSize().width / 2 + 100 - mapMaker.getWidth() / 2,
					getPreferredSize().height - 100);
			mapSelection.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					currentScreen = new MapSelectionScreen();
				}
			});
			mapMaker.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					currentScreen = new MapCreatorScreen();
				}
			});
			// centers the camera at the middle of the screen
			add(mapMaker);
			add(mapSelection);
			repaint();
		}

		public void draw(Graphics g) {
			try {
				g.drawImage(ImageIO.read(new File("src/Images/StartScreen.jpg")), 0, 0, getWidth(), getHeight(), null);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	class LevelScreen extends Screen {

		LevelScreen(String level) {
			removeAll();
			game = new Game(thisDisplay);
			while (getMouseListeners().length > 0) {
				removeMouseListener(getMouseListeners()[0]);
			}
			while (getMouseMotionListeners().length > 0) {
				removeMouseMotionListener(getMouseMotionListeners()[0]);
			}
			while (getMouseWheelListeners().length > 0) {
				removeMouseWheelListener(getMouseWheelListeners()[0]);
			}
			loadMap(level);
			game.startTimer();
			repaint();
			camX = game.getPlayer().getxCenter();
			camY = game.getCurrentLevel().getBlocks().length - (getHeight() + 10.0) / getBLOCK_SIZE_PIXELS() / 2.0;
			addMouseListener(this);
			setBLOCK_SIZE_PIXELS(getHeight() / game.getCurrentLevel().getBlocks().length);
		}

		public void draw(Graphics g) {
			drawLevel(g);
			g.setColor(Color.black);
			g.setFont(new Font("Arial", Font.BOLD, 30));
			g.drawString(game.getCoins() + "", getPreferredSize().width - 200, 50);
			g.drawString(game.getScore() + "", getPreferredSize().width - 200, 100);
		}
	}

	class MapSelectionScreen extends Screen {
		int nonPlayerLevels;
		int PlayerLevels;
		int currentPage = 0;
		JButton next;
		JButton back;
		JButton play;
		JButton menu;

		MapSelectionScreen() {
			removeAll();
			game = new Game(thisDisplay);
			while (getMouseListeners().length > 0) {
				removeMouseListener(getMouseListeners()[0]);
			}
			while (getMouseMotionListeners().length > 0) {
				removeMouseMotionListener(getMouseMotionListeners()[0]);
			}
			while (getMouseWheelListeners().length > 0) {
				removeMouseWheelListener(getMouseWheelListeners()[0]);
			}
			addMouseWheelListener(this);
			String line = "";
			// the bufferedreader throws IOExceptions, so we need a try-catch statememnt
			try {
				BufferedReader read = new BufferedReader(new FileReader("src/MapInformation"));
				line = read.readLine();
				read.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			nonPlayerLevels = Integer.parseInt(line.split(" ")[0]);
			PlayerLevels = Integer.parseInt(line.split(" ")[1]);
			next = new JButton("Next Map");
			next.setLocation(900, 600);
			next.setSize(new Dimension(150, 75));
			next.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					currentPage++;
					add(back);
					if (currentPage + 1 >= nonPlayerLevels + PlayerLevels) {
						remove(next);
					}
					repaint();
				}
			});
			back = new JButton("Back");
			back.setLocation(500, 600);
			back.setSize(new Dimension(150, 75));
			back.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					currentPage--;
					add(next);
					if (currentPage == 0) {
						remove(back);
					}
					repaint();
				}
			});
			if (nonPlayerLevels + PlayerLevels > 1) {
				add(next);
			}
			play = new JButton("Play");
			play.setLocation(700, 600);
			play.setSize(new Dimension(150, 75));
			play.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// System.out.println(currentPage);
					if (currentPage < nonPlayerLevels) {
						// System.out.println("Level" + (currentPage + 1));
						currentScreen = new LevelScreen("Level" + (currentPage + 1));
					} else {
						// System.out.println("PlayerLevel" + (currentPage - nonPlayerLevels + 1));
						currentScreen = new LevelScreen("PlayerLevel" + (currentPage - nonPlayerLevels + 1));
					}
					repaint();
				}
			});
			add(play);
			menu = new JButton("Menu");
			menu.setLocation(1300, 0);
			menu.setSize(new Dimension(150, 75));
			menu.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					currentScreen = new WelcomeScreen();
				}
			});
			add(menu);
			// revalidate();
			repaint();
		}

		private void loadMapPreview(String mapName, Graphics g) {
			try {
				// buffered reader requires a try-catch for IOExceptions
				game.setCurrentLevel(new Map(mapName));
				camX = game.getCurrentLevel().getBlocks()[0].length / 2.0;
				camY = game.getCurrentLevel().getBlocks().length / 2.0;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			drawLevel(g);
		}

		public void draw(Graphics g) {
			if (currentPage < nonPlayerLevels) {
				loadMapPreview("Level" + (currentPage + 1), g);
				g.setColor(Color.red);
				g.fillRect(648, 60, 200, 50);
				g.setColor(Color.BLACK);
				g.setFont(new Font("Arial", Font.BOLD, 30));
				g.drawString("Level " + (currentPage + 1), 700, 100);
			} else if (currentPage < nonPlayerLevels + PlayerLevels) {
				loadMapPreview("PlayerLevel" + (currentPage - nonPlayerLevels + 1), g);
				g.setColor(Color.red);
				g.fillRect(623, 60, 250, 50);
				g.setColor(Color.BLACK);
				g.setFont(new Font("Arial", Font.BOLD, 30));
				g.drawString("Player Level " + (currentPage + 1), 650, 100);
			}
		}

		public void mouseWheelMoved(MouseWheelEvent arg0) {
			// System.out.println(arg0.getWheelRotation());
			setBLOCK_SIZE_PIXELS(Math.min(Math.max(getBLOCK_SIZE_PIXELS() - arg0.getWheelRotation(), 5), 100));
			repaint();
			// System.out.println()
		}
	}

	class MapCreatorScreen extends Screen {
		int mouseX;
		int mouseY;
		JFrame selectedEntityOptions;
		ArrayList<optionBox> options = new ArrayList<optionBox>();
		ArrayList<Entity> entities = new ArrayList<Entity>();
		Entity entitySelected = null;
		JButton menu;
		int mapWidth = 100;
		int mapHeight = 25;
		double startx;
		double starty;
		double flagx;
		double flagy;

		Block[][] blocks = new Block[mapHeight][mapWidth];;

		class optionBox extends Rectangle {
			Entity entity;
			String hover;
			// string that draws when the mouse hover overs the rectangle
			String name;

			optionBox(int x, int y, int width, int height, Entity entity, String hover) {
				setRect(x, y, width, height);
				this.entity = entity;
				this.hover = hover;
				double[] centerCoords = pixelToBlockCoords((int) (x + width / 2.0), (int) (y + height / 2.0));
				// System.out.println((x + width / 2.0) + ", " + (y + height / 2.0));
				if (!(entity == null)) {
					entity.setxCenter(centerCoords[0]);
					entity.setyCenter(centerCoords[1]);
					// entity.updatePosition(0);
					// System.out.println(centerCoords[0] + ", " + centerCoords[1]);
				}
			}

			public void draw(Graphics g) {
				if (entity == null) {

				} else {
					double[] centerCoords = pixelToBlockCoords((int) (x + width / 2.0), (int) (y + height / 2.0));
					if (!(entity == null)) {
						entity.setxCenter(centerCoords[0]);
						entity.setyCenter(centerCoords[1]);
						// entity.updatePosition(0);
						// System.out.println(centerCoords[0] + ", " + centerCoords[1]);
					}
					entity.drawAt(g,
							(int) (this.getX() + this.getWidth() / 2.0
									- entity.getWidth() * Display.getBLOCK_SIZE_PIXELS() / 2.0),
							(int) (this.getY() + this.getHeight() / 2.0
									- entity.getHeight() * Display.getBLOCK_SIZE_PIXELS() / 2.0));

				} // System.out.println(Display.getBLOCK_SIZE_PIXELS());
				g.setColor(Color.BLACK);
				g.drawRect((int) getX(), (int) getY(), (int) getWidth(), (int) getHeight());
				if (contains(mouseX, mouseY) && entitySelected == null) {
					g.setFont(new Font("Arial", 0, 15));
					g.drawString(hover, mouseX, mouseY);
					// draws the string hover if the box is hovered over by the mouse
				}
			}

			public Entity getEntityCopy() {
				if (entity == null) {
					return null;
				} else {
					// System.out.println(entity.toString());
					return Entity.getEntityFromName(entity.toString());
				}
			}

		}

		MapCreatorScreen() {
			// removes all prior jcomponents and listeners
			removeAll();
			game = new Game(thisDisplay);
			while (getMouseListeners().length > 0) {
				removeMouseListener(getMouseListeners()[0]);
			}
			while (getMouseMotionListeners().length > 0) {
				removeMouseMotionListener(getMouseMotionListeners()[0]);
			}
			while (getMouseWheelListeners().length > 0) {
				removeMouseWheelListener(getMouseWheelListeners()[0]);
			}
			addMouseListener(this);
			addMouseMotionListener(this);
			addMouseWheelListener(this);

			camX = ((double) mapWidth) / 2.0;
			camY = ((double) mapHeight) / 2.0;

			// option boxes
			optionBox brick = new optionBox(1400, 0, 100, 100, Entity.getEntityFromName("Brick:0:false"), "Brick");
			optionBox mystery = new optionBox(1400, 100, 100, 100,
					Entity.getEntityFromName("Mystery:false:Mushroom:10:-10:0:left"), "Mystery Box");
			optionBox coin = new optionBox(1400, 200, 100, 100, Entity.getEntityFromName("Coin:100:0:0:1"), "Coin");
			optionBox fireBar = new optionBox(1400, 300, 100, 100, Entity.getEntityFromName("FireBar:5:6:0"),
					"FireBar");
			optionBox mushroomPowerUp = new optionBox(1400, 400, 100, 100,
					Entity.getEntityFromName("Mushroom:0:0:0:left"), "Mushroom Powerup");
			optionBox unselect = new optionBox(1400, 500, 100, 100, Entity.getEntityFromName("null"), "Erase");
			optionBox goomba = new optionBox(1300, 0, 100, 100, Entity.getEntityFromName("Goomba:10:5.5:5:left:false"),
					"Goomba");
			optionBox redKoopa = new optionBox(1300, 100, 100, 100,
					Entity.getEntityFromName("Koopa:18.5:6.5:5:15:red:true:false:false"), "Red Koopa Troopa");
			optionBox greenKoopa = new optionBox(1300, 200, 100, 100,
					Entity.getEntityFromName("Koopa:18.5:6.5:5:15:green:true:false:false"), "Green Koopa Troopa");
			optionBox redShell = new optionBox(1300, 300, 100, 100,
					Entity.getEntityFromName("Koopa:18.5:6.5:5:15:red:false:false:false"), "Red Shell");
			optionBox greenShell = new optionBox(1300, 400, 100, 100,
					Entity.getEntityFromName("Koopa:18.5:6.5:5:15:green:false:false:false"), "Green Shell");
			optionBox mario = new optionBox(1300, 500, 100, 100, new Player(startx, starty), "Set Spawn Point");
			optionBox flagpole = new optionBox(1300, 600, 100, 100, new Flagpole("Flagpole:0:0"),
					"Set Flagpole Position");
			optionBox piranha = new optionBox(1400, 600, 100, 100, Entity.getEntityFromName("PiranhaPlant:10.5:15:9:5"),
					"Piranha Plant");
			options.add(brick);
			options.add(mystery);
			options.add(coin);
			options.add(fireBar);
			options.add(mushroomPowerUp);
			options.add(unselect);
			options.add(goomba);
			options.add(redKoopa);
			options.add(greenKoopa);
			options.add(redShell);
			options.add(greenShell);
			options.add(mario);
			options.add(flagpole);
			options.add(piranha);
			// JTextFields to toggle the size of the map
			JLabel width = new JLabel("Enter Width");
			width.setBounds(0, 50, 75, 35);
			add(width);
			JTextField getWidth = new JTextField();
			getWidth.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					JTextField source = (JTextField) arg0.getSource();
					boolean validLastChar = false;
					for (int i = 0; i < 10; i++) {
						validLastChar = source.getText().charAt(source.getText().length() - 1) == ("" + i).charAt(0)
								|| validLastChar;
					}
					if (!validLastChar) {
						source.setText("Error, invalid");
					} else {
						Block[][] newBlocks = new Block[mapHeight][Integer.parseInt(source.getText())];
						for (int i = 0; i < Integer.parseInt(source.getText()); i++) {
							for (int j = 0; j < mapHeight; j++) {
								if (i < mapWidth) {
									newBlocks[j][i] = blocks[j][i];
								} else {
									newBlocks[j][i] = new Air();
									newBlocks[j][i].setXY(i, j);
								}
							}
						}
						blocks = newBlocks;
						mapWidth = Integer.parseInt(source.getText());
					}
				}
			});
			getWidth.setText("100");
			getWidth.setBounds(75, 50, 75, 35);
			add(getWidth);
			JLabel height = new JLabel("Enter Height");
			height.setBounds(0, 0, 75, 35);
			add(height);
			JTextField getHeight = new JTextField();
			getHeight.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					JTextField source = (JTextField) arg0.getSource();
					boolean validLastChar = false;
					for (int i = 0; i < 10; i++) {
						validLastChar = source.getText().charAt(source.getText().length() - 1) == ("" + i).charAt(0)
								|| validLastChar;
					}
					if (!validLastChar) {
						source.setText("Error, invalid");
					} else {
						Block[][] newBlocks = new Block[Integer.parseInt(source.getText())][mapWidth];
						for (int i = 0; i < mapWidth; i++) {
							for (int j = 0; j < Integer.parseInt(source.getText()); j++) {
								if (j < mapHeight) {
									newBlocks[j][i] = blocks[j][i];
								} else {
									newBlocks[j][i] = new Air();
									newBlocks[j][i].setXY(i, j);
								}
							}
						}
						blocks = newBlocks;
						mapHeight = Integer.parseInt(source.getText());
						repaint();
					}
				}
			});
			getHeight.setText("25");
			getHeight.setBounds(75, 0, 75, 35);
			add(getHeight);

			// go back to home screen button
			menu = new JButton("Menu");
			menu.setLocation(36, 150);
			menu.setSize(new Dimension(75, 50));
			menu.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					currentScreen = new WelcomeScreen();
				}
			});
			add(menu);

			// initialize map
			for (int i = 0; i < mapHeight; i++) {
				for (int j = 0; j < mapWidth; j++) {
					blocks[i][j] = new Air();
					// System.out.println(j + ", " + i);
					blocks[i][j].setXY(j, i);
					if (blocks[i][j] instanceof Air) {

					} else {
						entities.add(blocks[i][j]);
					}
				}
			}
			camX = mapWidth / 2.0;
			camY = mapHeight / 2.0;

			// button to save map into a file
			JButton save = new JButton("Save");
			save.setLocation(1350, 700);
			save.setSize(new Dimension(100, 50));
			save.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					currentScreen = new WelcomeScreen();
					BufferedReader read;
					int defaultLevels = 0;
					int playerMadeLevels = 0;
					// these actions require try-catch statements because the compiler needs it to
					// initialize
					try {
						// reads MapInformation to find the number of player levels already made
						read = new BufferedReader(new FileReader(new File("src/MapInformation")));
						String line = read.readLine();
						defaultLevels = Integer.parseInt(line.split(" ")[0]);
						playerMadeLevels = Integer.parseInt(line.split(" ")[1]) + 1;
						// System.out.println(playerMadeLevels);
						read.close();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					try {
						// writes the actual file
						BufferedWriter write = new BufferedWriter(
								new PrintWriter(new FileWriter("src/PlayerLevel" + playerMadeLevels)));
						write.write(mapHeight + " " + mapWidth);
						write.newLine();
						for (int i = 0; i < mapHeight; i++) {
							for (int j = 0; j < mapWidth; j++) {
								write.write(blocks[i][j].toString() + " ");
							}
							write.newLine();
						}
						write.write(startx + " " + starty);
						write.newLine();
						write.write((entities.size()) + "");
						write.newLine();
						for (int i = 0; i < entities.size(); i++) {
							if (entities.get(i) instanceof Block) {

							} else {
								write.write(entities.get(i).toString());
								write.newLine();
							}
						}
						write.write("Flagpole:" + flagx + ":" + flagy);
						write.close();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					try {
						BufferedWriter write = new BufferedWriter(
								new PrintWriter(new FileWriter("src/MapInformation")));
						write.write(defaultLevels + " " + playerMadeLevels);
						write.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			});
			add(save);

			repaint();
		}

		private double[] pixelToBlockCoords(int pixelX, int pixelY) {
			double blockX = camX + (pixelX - getWidth() / 2.0) / (double) Display.BLOCK_SIZE_PIXELS;
			double blockY = camY + (pixelY - getHeight() / 2.0) / (double) Display.BLOCK_SIZE_PIXELS;
			return new double[] { blockX, blockY };
		}

		private int[] blockToPixelCoords(double blockX, double blockY) {
			int pixelX = (int) ((blockX - camX) * Display.BLOCK_SIZE_PIXELS + getWidth() / 2.0);
			int pixelY = (int) ((blockY - camY) * Display.BLOCK_SIZE_PIXELS + getHeight() / 2.0);
			return new int[] { pixelX, pixelY };
		}

		private boolean mouseWithinMapSpace() {
			return (pixelToBlockCoords(mouseX, mouseY)[0] < mapWidth && pixelToBlockCoords(mouseX, mouseY)[0] >= 0
					&& pixelToBlockCoords(mouseX, mouseY)[1] < mapHeight && pixelToBlockCoords(mouseX, mouseY)[1] >= 0);
		}

		public void draw(Graphics g) {
			// draw option boxes
			boolean inOptionBox = false;
			for (int i = 0; i < options.size(); i++) {
				options.get(i).draw(g);
				inOptionBox = inOptionBox || options.get(i).contains(mouseX, mouseY);
			}

			// draw Entities already placed
			for (int i = 0; i < entities.size(); i++) {
				entities.get(i).drawEntity(g, camX, camY);
			}
			for (int i = 0; i < mapHeight; i++) {
				for (int j = 0; j < mapWidth; j++) {
					blocks[i][j].drawEntity(g, camX, camY);
				}
			}

			// draw map outline
			g.setColor(Color.BLACK);
			int[] leftTopCorner = blockToPixelCoords(0, 0);
			g.drawRect(leftTopCorner[0], leftTopCorner[1], (int) (mapWidth * getBLOCK_SIZE_PIXELS()),
					(int) (mapHeight * getBLOCK_SIZE_PIXELS()));

			// draw out of bounds or selected entity
			double blockCoords[] = pixelToBlockCoords(mouseX, mouseY);
			if (blockCoords[0] < 0 || blockCoords[0] >= mapWidth || blockCoords[1] < 0 || blockCoords[1] >= mapHeight) {
				// if out of bounds of the map, draw string "out of bounds"
				g.setColor(Color.BLACK);
				g.setFont(new Font("Arial", 0, 15));
				if (!inOptionBox) {
					g.drawString("out of bounds", mouseX, mouseY);
				}
			} else {
				// if in bounds of the map, draw selected entity
				if (!(entitySelected == null)) {
					if (entitySelected instanceof Block) {
						// if the entity is a block, snap it
						double x = Math.floor(pixelToBlockCoords(mouseX, mouseY)[0]);
						double y = Math.floor(pixelToBlockCoords(mouseX, mouseY)[1]);
						// System.out.println(drawX + ", " + drawY);
						((Block) entitySelected).setXY((int) x, (int) y);
						entitySelected.drawEntity(g, camX, camY);

					} else {
						// otherwise, do not snap
						double x = (pixelToBlockCoords(mouseX, mouseY)[0]);
						double y = (pixelToBlockCoords(mouseX, mouseY)[1]);
						entitySelected.setxCenter(x);
						entitySelected.setyCenter(y);
						entitySelected.drawEntity(g, camX, camY);
					}
				}
			}
			(new Player(startx, starty)).drawEntity(g, camX, camY);
			(new Flagpole("Flagpole:" + flagx + ":" + flagy)).drawEntity(g, camX, camY);
		}

		// variables to move the camera
		private double lastCamX; // before a drag, this is last position of cam X
		private double lastCamY; // before a drag, this is last position of cam Y
		private double dragPixelX; // when I started pressing, this is the position of the pixel
		private double dragPixelY; // when I started pressing, this is the position of the pixel

		public void mouseClicked(MouseEvent e) {
			mouseX = e.getX();
			mouseY = e.getY();
			boolean inOptionBox = false;
			for (int i = 0; i < options.size(); i++) {
				if (options.get(i).contains(e.getX(), e.getY())) {
					entitySelected = options.get(i).getEntityCopy();
					inOptionBox = true;
				}
			}
			if (!inOptionBox) {
				if (mouseWithinMapSpace()) {
					Entity copy = null;
					if (!(entitySelected == null)) {
						copy = Entity.getEntityFromName(entitySelected.toString());
					}
					if (entitySelected == null) {
						copy = new Brick("Brick:0:false");
						int x = (int) Math.floor(pixelToBlockCoords(mouseX, mouseY)[0]);
						int y = (int) Math.floor(pixelToBlockCoords(mouseX, mouseY)[1]);
						// replaces the block with air
						blocks[y][x] = new Air();

						// removes all entities that you clicked on
						double xCenter = pixelToBlockCoords(mouseX, mouseY)[0];
						double yCenter = pixelToBlockCoords(mouseX, mouseY)[1];
						copy.setxCenter(xCenter);
						copy.setyCenter(yCenter);
						int index = 0;
						while (index < entities.size()) {
							if (entities.get(index) != copy) {
								if (Entity.collided(entities.get(index), copy)) {
									// System.out.println(entities.get(index));
									entities.remove(index);
								} else {
									index++;
								}
							} else {
								index++;
							}
						}
					} else if (entitySelected instanceof Block) {
						int x = (int) Math.floor(pixelToBlockCoords(mouseX, mouseY)[0]);
						int y = (int) Math.floor(pixelToBlockCoords(mouseX, mouseY)[1]);
						((Block) copy).setXY(x, y);
						// replaces the block with air
						blocks[y][x] = (Block) copy;
					} else if (entitySelected instanceof Player) {
						startx = pixelToBlockCoords(mouseX, mouseY)[0];
						starty = pixelToBlockCoords(mouseX, mouseY)[1];
						for (int i = 0; i < entities.size(); i++) {
							if (entities.get(i) instanceof Player) {
								entities.get(i).setxCenter(startx);
								entities.get(i).setyCenter(starty);
							}
						}
					} else if (entitySelected instanceof Flagpole) {
						flagx = pixelToBlockCoords(mouseX, mouseY)[0];
						flagy = pixelToBlockCoords(mouseX, mouseY)[1];
						for (int i = 0; i < entities.size(); i++) {
							if (entities.get(i) instanceof Flagpole) {
								entities.get(i).setxCenter(flagx);
								entities.get(i).setyCenter(flagy);
							}
						}
					} else {
						if (copy instanceof Air) {

						} else {
							double[] coords = pixelToBlockCoords(mouseX, mouseY);
							copy.setxCenter(coords[0]);
							copy.setyCenter(coords[1]);
							entities.add(copy);
						}

					}
				}
			}
			repaint();
		}

		public void mouseMoved(MouseEvent e) {
			mouseX = e.getX();
			mouseY = e.getY();
			double[] centerCoords = pixelToBlockCoords((int) (mouseX), (int) (mouseY));
			// System.out.println(centerCoords[0] + ", " + centerCoords[1]);
			if (entitySelected != null) {
				// System.out.println(entitySelected.getxCenter() + ", " +
				// entitySelected.getyCenter());
				entitySelected.setxCenter(centerCoords[0]);
				entitySelected.setyCenter(centerCoords[1]);
			}
			repaint();
			// System.out.println("CamX: " + camX + " CamY: " + camY);
		}

		public void mousePressed(MouseEvent e) {
			// System.out.println("pressed " + e.getX() + ", " + e.getY());
			dragPixelX = e.getX();
			dragPixelY = e.getY();
			lastCamX = camX;
			lastCamY = camY;
			repaint();
		}

		public void mouseDragged(MouseEvent e) {
			// System.out.println("camx: " + camX + ", camy: " + camY);
			mouseX = e.getX();
			mouseY = e.getY();
			if ((entitySelected == null)) {
				// if we are dragging and we don't have an entity selected, move the camera
				// position
				camX = lastCamX - (e.getX() - dragPixelX) / (double) BLOCK_SIZE_PIXELS;
				camY = lastCamY - (e.getY() - dragPixelY) / (double) BLOCK_SIZE_PIXELS;
			} else if (!(entitySelected instanceof Player) || !(entitySelected instanceof Flagpole)) {
				int x = (int) Math.floor(pixelToBlockCoords(mouseX, mouseY)[0]);
				int y = (int) Math.floor(pixelToBlockCoords(mouseX, mouseY)[1]);
				Entity copy = Entity.getEntityFromName(entitySelected.toString());
				if (mouseWithinMapSpace()) {
					if (copy instanceof Block) {
						((Block) copy).setXY(x, y);
						blocks[y][x] = (Block) copy;
					}
				}
			} else if (entitySelected instanceof Player) {
				startx = pixelToBlockCoords(mouseX, mouseY)[0];
				starty = pixelToBlockCoords(mouseX, mouseY)[1];
				for (int i = 0; i < entities.size(); i++) {
					if (entities.get(i) instanceof Player) {
						entities.get(i).setxCenter(startx);
						entities.get(i).setyCenter(starty);
					}
				}
			} else if (entitySelected instanceof Flagpole) {
				flagx = pixelToBlockCoords(mouseX, mouseY)[0];
				flagy = pixelToBlockCoords(mouseX, mouseY)[1];
				for (int i = 0; i < entities.size(); i++) {
					if (entities.get(i) instanceof Flagpole) {
						entities.get(i).setxCenter(flagx);
						entities.get(i).setyCenter(flagy);
					}
				}
			}
			repaint();

		}

		public void mouseWheelMoved(MouseWheelEvent arg0) {
			// System.out.println(arg0.getWheelRotation());
			setBLOCK_SIZE_PIXELS(Math.min(Math.max(getBLOCK_SIZE_PIXELS() - arg0.getWheelRotation(), 5), 100));
			repaint();
			// System.out.println()
		}

	}

	Display(double camX, double camY) {
		new Images();
		Display.camX = camX;
		Display.camY = camY;
		game = new Game(this);
		// loadMapScreen("FirstLevel");

		// Sets the size to be the size of the user's screen
		Dimension screenSize = new Dimension(1500, 750);
		screenSize.setSize(1500, 750);
		setSize(screenSize);
		setPreferredSize(screenSize);

		Block.setScreenDim(getSize());
		setBLOCK_SIZE_PIXELS(20);
		setFocusable(true); // allows the keylistener to listen to keys on the jPanel
		setLayout(null);

		setBackground(Color.WHITE);

		currentScreen = new WelcomeScreen();
		frame = new JFrame();
		frame.setResizable(true);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.add(this);
		frame.pack();
		frame.setResizable(false);
		frame.setVisible(true);
	}

	private void drawLevel(Graphics g) {
		drawLevel(g, camX, camY);
	}

	private void drawLevel(Graphics g, double camX, double camY) {
		for (int i = 0; i < game.getCurrentLevel().getEntities().size(); i++) {
			// game.getCurrentLevel().getEntities().get(i).updatePosition(0);
			// System.out.println(game.getCurrentLevel().getEntities().get(i));
			game.getCurrentLevel().getEntities().get(i).drawEntity(g, camX, camY);
			/*
			 * if(game.getCurrentLevel().getEntities().get(i) instanceof Goomba) {
			 * System.out.println("Goomba"); }
			 */
		}
	}

	public void loadMap(String mapName) {
		try {
			// the try-catch statement is only here because
			// the BufferedReader throws Exceptions
			game.setCurrentLevel(new Map(mapName));
		} catch (IOException e) {
			System.out.println("ERROR");
		}
		addKeyListener(game.getPlayer().keyImplementation);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		currentScreen.draw(g);
	}

	public static int getBLOCK_SIZE_PIXELS() {
		return BLOCK_SIZE_PIXELS;
	}

	public static void setBLOCK_SIZE_PIXELS(int BLOCK_SIZE_PIXELS) {
		Display.BLOCK_SIZE_PIXELS = BLOCK_SIZE_PIXELS;
	}

	public double getCamX() {
		return camX;
	}

	public void setCamX(double camX) {
		this.camX = camX;
	}

	public double getCamY() {
		return camY;
	}

	public void setCamY(double camY) {
		this.camY = camY;
	}
}