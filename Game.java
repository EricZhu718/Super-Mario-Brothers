import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Game {
	public static double TICKS_PER_SECOND = 100;
	private Display display;
	private Player player;
	private Flagpole pole;
	private Map currentLevel;
	ArrayList<Entity> entities = new ArrayList<Entity>();
	private Timer gameTimer = new Timer();
	private TimerTask gameTimerTask = new TimerTask() {
		long lastTime = 0;

		public void run() {
			// System.out.println("new line");
			long currentTime = System.currentTimeMillis();
			if (lastTime != 0) {
				// System.out.println((currentTime - lastTime) / 1000.0);
				for (int i = 0; i < entities.size(); i++) {
					Entity entity = entities.get(i);
					// (currentTime - lastTime) / 1000.0 is the duration of the previous iteration
					// of the Timer loop in seconds
					entity.updatePosition((currentTime - lastTime) / 1000.0);
					if (entity instanceof Moving) {
						((Moving) entity).updateVelocity((currentTime - lastTime) / 1000.0);
					}
				}
				for (int i = 0; i < entities.size() - 1; i++) {
					for (int j = i + 1; j < entities.size(); j++) {
						if (Entity.collided(entities.get(i), entities.get(j))) {
							Entity.collideAction(entities.get(i), entities.get(j));
						}
					}
				}
				// player.updateVelocity((currentTime - lastTime) / 1000.0);
				display.repaint();
			}
			// System.out.println("x Center " + player.getxCenter() + ", y Center " +
			// player.getyCenter());
			// System.out.println(currentTime-lastTime);
			// System.out.println(Monster.lastCollisionWithPlayer);
			lastTime = currentTime;
		}
	};

	private int coins = 0;
	private long score = 0;

	public void addCoins(int amount) {
		coins += amount;
	}

	public void addScore(long amount) {
		score += amount;
	}

	Game(Display display) {
		this.display = display;
		Entity.setGame(this);
	}

	public Map getCurrentLevel() {
		return currentLevel;
	}

	public void setCurrentLevel(Map currentLevel) {
		this.currentLevel = currentLevel;
		player = new Player(currentLevel.getxStart(), currentLevel.getyStart());
		entities = currentLevel.getEntities();
		entities.add(player);
	}

	public void startTimer() {
		gameTimer.schedule(gameTimerTask, 0, (int) (1000 / TICKS_PER_SECOND));
	}

	public void gameEnded() {
		while (player.death.inAnimation) {
			player.death.update();
			display.repaint();
		}
		while (player.win.inAnimation) {
			player.win.update();
			display.repaint();
		}
		// System.out.println("hi");
		display.currentScreen = display.new MapSelectionScreen();
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Timer getGameTimer() {
		return gameTimer;
	}

	public Display getDisplay() {
		return display;
	}

	public int getCoins() {
		return coins;
	}

	public void setCoins(int coins) {
		this.coins = coins;
	}

	public long getScore() {
		return score;
	}

	public void setScore(long score) {
		this.score = score;
	}
}
