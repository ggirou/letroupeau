package trials;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ARBTransposeMatrix;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.Sphere;

public class try3dball {

	private float view_rotx = 20.0f;

	private float view_roty = 30.0f;

	private float view_rotz = 0.0f;

	private int gear1;

	private int gear2;

	private int gear3;

	private float angle = 0.0f;

	public static void main(String[] args) {
		new try3dball().execute();
		System.exit(0);
	}

	/**
		 * 
		 */
	private void execute() {
		try {
			init();
		} catch (LWJGLException le) {
			le.printStackTrace();
			System.out.println("Failed to initialize Gears.");
			return;
		}

		loop();

		destroy();
	}

	/**
		 * 
		 */
	private void destroy() {
		Display.destroy();
	}

	/**
		 * 
		 */
	private void loop() {
		long startTime = System.currentTimeMillis() + 5000;
		long fps = 0;

		while (!Display.isCloseRequested()) {
			angle += 0.1f;

			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

			GL11.glPushMatrix();
			GL11.glRotatef(view_rotx, 1.0f, 0.0f, 0.0f);
			GL11.glRotatef(view_roty, 0.0f, 1.0f, 0.0f);
			GL11.glRotatef(view_rotz, 0.0f, 0.0f, 1.0f);

			GL11.glPushMatrix();
			GL11.glTranslatef(-3.0f, -2.0f, 0.0f);
			GL11.glRotatef(angle, angle, angle, 1.0f);
			GL11.glCallList(gear1);
			GL11.glPopMatrix();

			GL11.glPopMatrix();

			Display.update();
			if (startTime > System.currentTimeMillis()) {
				fps++;
			} else {
				long timeUsed = 5000 + (startTime - System.currentTimeMillis());
				startTime = System.currentTimeMillis() + 5000;
				System.out.println(fps + " frames in "
						+ (float) (timeUsed / 1000f) + " seconds = "
						+ (fps / (timeUsed / 1000f)));
				fps = 0;
			}
		}
	}

	   /**
     * Load an image from the given file and return a ByteBuffer containing ARGB pixels.<BR>
     * Can be used to create textures. <BR>
     * @param imgFilename
     * @return
     */
    public static ByteBuffer loadImagePixels(String imgFilename) {
        GLImage img = new GLImage(imgFilename);
        return img.pixelBuffer;
    }

	
	/**
		 * 
		 */
	private void init() throws LWJGLException {
		// create Window of size 300x300
		Display.setLocation((Display.getDisplayMode().getWidth() - 300) / 2,
				(Display.getDisplayMode().getHeight() - 300) / 2);
		Display.setDisplayMode(new DisplayMode(300, 300));
		Display.setTitle("Gears");
		Display.create();

		// setup ogl
		FloatBuffer pos = FloatBuffer.wrap(new float[] { 5.0f, 5.0f, 10.0f,
				0.0f });
		FloatBuffer pos2 = FloatBuffer.wrap(new float[] { 0.0f, 5.0f, 10.0f,
				0.0f });
		FloatBuffer red = FloatBuffer
				.wrap(new float[] { 0.8f, 0.1f, 0.0f, 1.0f });

		FloatBuffer mtlspecular = FloatBuffer
		.wrap(new float[] { 0.3f, 0.3f, 0.3f, 0.3f });

		float[] reflect = {0.5f, 0.3f, 0.3f, 1};
		mtlspecular.put(reflect).flip();         // reflected light

		
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, pos);

		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_LIGHT0);
		//GL11.glEnable(GL11.GL_LIGHT1);
		GL11.glEnable(GL11.GL_DEPTH_TEST);

		/* make the gears */
		gear1 = GL11.glGenLists(1);
		GL11.glNewList(gear1, GL11.GL_COMPILE);
		GL11.glMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT_AND_DIFFUSE, red);
		GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, mtlspecular);
		gear(1.0f, 4.0f, 1.0f, 20, 0.7f);
		GL11.glEndList();

		GL11.glEnable(GL11.GL_NORMALIZE);

		GL11.glMatrixMode(GL11.GL_PROJECTION);

		System.err.println("GL_VENDOR: " + GL11.glGetString(GL11.GL_VENDOR));
		System.err
				.println("GL_RENDERER: " + GL11.glGetString(GL11.GL_RENDERER));
		System.err.println("GL_VERSION: " + GL11.glGetString(GL11.GL_VERSION));
		System.err.println();
		System.err.println("glLoadTransposeMatrixfARB() supported: "
				+ GLContext.getCapabilities().GL_ARB_transpose_matrix);
		if (!GLContext.getCapabilities().GL_ARB_transpose_matrix) {
			// --- not using extensions
			GL11.glLoadIdentity();
		} else {
			// --- using extensions
			final FloatBuffer identityTranspose = BufferUtils
					.createFloatBuffer(16).put(
							new float[] { 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0,
									0, 0, 0, 1 });
			identityTranspose.flip();
			ARBTransposeMatrix.glLoadTransposeMatrixARB(identityTranspose);
		}

		float h = (float) 300 / (float) 300;
		GL11.glFrustum(-1.0f, 1.0f, -h, h, 5.0f, 60.0f);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GL11.glTranslatef(0.0f, 0.0f, -40.0f);
	}

	private void gear(float inner_radius, float outer_radius, float width,
			int teeth, float tooth_depth) {

		GL11.glShadeModel(GL11.GL_FLAT);

		GL11.glNormal3f(0.0f, 1.0f, 1.0f);

		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glColor4f(1f, 0f, 0f, 1f);

		Sphere sphere = new Sphere();
		sphere.draw(5f, 100, 100);

		GL11.glColor3f(1.0f, 1.0f, 1.0f);
        //GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, mtlspecular);

		
		GL11.glEnable(GL11.GL_TEXTURE_2D);

	}
}
