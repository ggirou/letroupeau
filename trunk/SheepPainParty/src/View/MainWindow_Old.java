package View;

import java.awt.Canvas;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import config.Constants;

public class MainWindow_Old extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Sprite texture1;
	private TextureLoader textureLoader;

	public void destroy() {
		Display.destroy();

	}

	/**
	 * Initializes the window
	 * 
	 * @return true in case of success
	 */
	public boolean initialize() {

		try {
			Canvas canvas = new Canvas();
			this.add(new JPanel().add(canvas));
			// this.pack();
			this.setSize(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
			this.setVisible(true);

			Display.setParent(canvas);
			Display.setTitle(Constants.WINDOW_TITLE);
			Display.setLocation(Constants.WINDOW_X, Constants.WINDOW_Y);
			Display.setDisplayMode(new DisplayMode(Constants.WINDOW_WIDTH,
					Constants.WINDOW_HEIGHT));
			Display.create();

			// Enable textures

			GL11.glEnable(GL11.GL_TEXTURE_2D);

			// Deactivate it since we are using 2d graphics
			GL11.glDisable(GL11.GL_DEPTH_TEST);

			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			GL11.glOrtho(0, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT, 0,
					-1, 1);

			textureLoader = new TextureLoader();

			texture1 = new Sprite(textureLoader, Constants.IMG_PATH
					+ "metalslug.jpg");

			return true;
		} catch (LWJGLException e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * Render display
	 */
	public void render() {
		texture1.draw(30, 30);
	}

	/**
	 * Refresh window display
	 */
	public void update() {
		// clear screen
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();

		render();
		Display.update();
	}

}