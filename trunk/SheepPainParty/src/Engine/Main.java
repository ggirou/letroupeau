/**
 * 
 */
package Engine;

import Model.SphericalCoordinate;
import View.MainWindow;

/**
 * @author GGirou
 * 
 */
public final class Main {
	static World world;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Variables
		MainWindow mainWindow = new MainWindow();

		mainWindow.start();
	}

	public static void CreateWorld() {
		world = World.create(100f);
		world.createSheepUser(new SphericalCoordinate(50, 50, 50));

	}
}
