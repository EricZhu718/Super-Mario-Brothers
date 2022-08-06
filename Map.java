import java.io.*;
import java.util.ArrayList;

public class Map {
	private Block[][] blocks;
	private ArrayList<Entity> entities = new ArrayList<Entity>();
	private double xStart;
	private double yStart;

	public Block[][] getBlocks() {
		return blocks;
	}

	public void setBlocks(Block[][] blocks) {
		this.blocks = blocks;
	}

	private void loadMapData(File data) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(data));
		// System.out.println("checkpoint 1");
		String line = reader.readLine();

		// Each file for a map will contain 2 integers in the top line: first integer is
		// for the height of the map and second for the length of the map
		int height = Integer.parseInt(line.split(" ")[0]);
		int length = Integer.parseInt(line.split(" ")[1]);

		blocks = new Block[height][length];
		for (int i = 0; i < height; i++) {
			line = reader.readLine();
			for (int j = 0; j < length; j++) {
				blocks[i][j] = (Block) Entity.getEntityFromName(line.split(" ")[j]);
				// System.out.println(j + ", " + i);
				blocks[i][j].setXY(j, i);
				entities.add(blocks[i][j]);
			}
		}

		// the 2 doubles right after determine the starting coordinates of the player
		line = reader.readLine();
		xStart = Double.parseDouble(line.split(" ")[0]);
		yStart = Double.parseDouble(line.split(" ")[1]);

		// next line indicates the number of other entities (powerups, coins, monsters
		// etc)
		int otherEntities = Integer.parseInt(reader.readLine());
		for (int i = 0; i < otherEntities; i++) {
			entities.add(Entity.getEntityFromName(reader.readLine()));
		}
		// last line indicates the flagpole
		entities.add(Entity.getEntityFromName(reader.readLine()));
		reader.close();
	}

	Map(File data) throws IOException {
		loadMapData(data);
	}

	Map(String fileName) throws IOException {
		loadMapData(new File("src/" + fileName));
	}

	public double getxStart() {
		return xStart;
	}

	public void setxStart(double xStart) {
		this.xStart = xStart;
	}

	public double getyStart() {
		return yStart;
	}

	public void setyStart(double yStart) {
		this.yStart = yStart;
	}

	public ArrayList<Entity> getEntities() {
		return entities;
	}

	public void setEntities(ArrayList<Entity> entities) {
		this.entities = entities;
	}

}
