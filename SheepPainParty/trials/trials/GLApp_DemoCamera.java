package trials;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

/**
 * Render a scene with two cameras, one still and one moving.  Hit SPACE to
 * toggle viewpoints between the two cameras.  Uses the GLCamera class to
 * hold camera position and orientation, and the GLCam class to move the
 * current camera in response to arrow key events.
 * <P>
 * @see GLCamera.java
 * @see GLCam.java
 * <P>
 * napier at potatoland dot org
 */
public class GLApp_DemoCamera extends GLApp {
    // Handle for texture
    int sphereTextureHandle = 0;
    int groundTextureHandle = 0;
    // Light position: if last value is 0, then this describes light direction.  If 1, then light position.
    float lightPosition[]= { -2f, 2f, 2f, 0f };
    // World coordinates of sphere
    float[] spherePos = {0f, 0f, 0f};
    // Camera position
    float[] cameraPos = {0f,3f,20f};
    // for cursor
    int cursorTextureHandle = 0;

    // two cameras and a cam to move them around scene
    GLCamera camera1 = new GLCamera();
    GLCamera camera2 = new GLCamera();
    GLCam cam = new GLCam(camera1);

    // vectors used to orient airplane motion
    GL_Vector UP = new GL_Vector(0,1,0);
    GL_Vector ORIGIN = new GL_Vector(0,0,0);

    // for earth rotation
    float degrees = 0;

    // model of airplane and sphere displaylist for earth
    GLModel airplane;
    int earth;

    // shadow handler will draw a shadow on floor plane
    GLShadowOnPlane airplaneShadow;

    public GL_Vector airplanePos;

    //------------------------------------------------------------------------
    // Run main loop of application.  Handle mouse and keyboard input.
    //------------------------------------------------------------------------
    public static void main(String args[]) {
    	GLApp_DemoCamera demo = new GLApp_DemoCamera();
        demo.VSyncEnabled = true;
        demo.fullScreen = false;
        demo.run();  // will call init(), render(), mouse functions
    }

    /**
     * Initialize the scene.  Called by GLApp.run()
     */
    public void setup()
    {
        // setup and enable perspective
        setPerspective();

        // Create a light (diffuse light, ambient light, position)
        setLight( GL11.GL_LIGHT1,
        		new float[] { 1f, 1f, 1f, 1f },
        		new float[] { 0.5f, 0.5f, .53f, 1f },
        		new float[] { 1f, 1f, 1f, 1f },
        		lightPosition );

        // Create a directional light (light green, to simulate reflection off grass)
        setLight( GL11.GL_LIGHT2,
        		new float[] { 0.15f, 0.0f, 0.1f, 1.0f },  // diffuse color
        		new float[] { 0.0f, 0.0f, 0.0f, 1.0f },   // ambient
        		new float[] { 0.0f, 0.0f, 0.0f, 1.0f },   // specular
        		new float[] { 0.0f, -10f, 0.0f, 0f } );   // direction (pointing up)

        // enable lighting and texture rendering
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_TEXTURE_2D);

        // Enable alpha transparency (so text will have transparent background)
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        // Create texture for spere
        sphereTextureHandle = makeTexture("images/ylommi.png"); //marble.jpg");

        // Create texture for ground plane
        groundTextureHandle = makeTexture("images/grass_1_512.jpg",true,true);

        // Load a cursor image and make it a texture
        cursorTextureHandle = makeTexture("images/cursorCrosshair32.gif");

        // set camera 1 position
        camera1.setCamera(0,4,15, 0,-.3f,-1, 0,1,0);
        //camera2.setCamera(0, 0, 0, 40);
        camera2.setCamera(-1,2f,0f, 10, 0, 0, 0, 1, 0);
        

//        // load the airplane model and make it a display list
//        airplane = new GLModel("models/JetFire/JetFire.obj");
//        airplane.mesh.regenerateNormals();
//        airplane.makeDisplayList();

        // make a sphere display list
        earth = beginDisplayList(); {
        	renderSphere();
        }
        endDisplayList();

        // make a shadow handler
        // params:
        //		the light position,
        //		the plane the shadow will fall on,
        //		the color of the shadow,
        // 		this application,
        // 		the function that draws all objects that cast shadows
//        airplaneShadow = new GLShadowOnPlane(lightPosition, new float[] {0f,1f,0f,3f}, null, this, method(this,"drawObjects"));
    }

    /**
     * set the field of view and view depth.
     */
    public static void setPerspective()
    {
        // select projection matrix (controls perspective)
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        // fovy, aspect ratio, zNear, zFar
        GLU.gluPerspective(50f,         // zoom in or out of view
                           aspectRatio, // shape of viewport rectangle
                           .1f,         // Min Z: how far from eye position does view start
                           500f);       // max Z: how far from eye position does view extend
        // return to modelview matrix
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
    }

    /**
     * Render one frame.  Called by GLApp.run().
     */
    public void draw() {
    	degrees += 30f * GLApp.getSecondsPerFrame();

//        // place airplane in orbit around ball, and place camera slightly above airplane
//    	GL_Vector.rotationVector(degrees);
//    	//airplanePos = GL_Vector.multiply(new GL_Vector(10f,10f,10f),8);
//    	camera2.MoveTo(10f, 10f, 10f);
//
//    	// align airplane and camera2 (perpendicular to the radius and up vector)
//    	GL_Vector airplaneDirection = GL_Vector.CCross(UP, new GL_Vector(10f,10f,10f));
//    	camera2.viewDir( airplaneDirection );  // point camera in direction of airplane motion

        // user keystrokes adjust camera position
        cam.handleNavKeys((float)GLApp.getSecondsPerFrame());

        // combine user camera motion with current camera position (so user can look around while on the airplane)
        float apRot = camera2.RotatedY;  // how much is camera rotated?
    	camera2.RotatedY = 0;            // zero out rotation
    	camera2.RotateY(apRot);          // set rotation again (camera will add rotation to its current view direction)

        // clear depth buffer and color
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        // select model view for subsequent transforms
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();

        // do gluLookAt() with camera position, direction, orientation
        cam.render();

//        // draw the ground plane
//        GL11.glPushMatrix();
//        {
//            GL11.glTranslatef(0f, -3f, 0f); // down a bit
//            GL11.glScalef(15f, .01f, 15f);
//            GL11.glBindTexture(GL11.GL_TEXTURE_2D, groundTextureHandle);
//            renderCube();
//        }
//        GL11.glPopMatrix();

        // invokes the drawObjects() method to create shadows for objects in the scene
//        airplaneShadow.drawShadow();

        // draw sphere at center (rotate 10 degrees per second)
        rotation += 10f * getSecondsPerFrame();

        // draw the scene (after we draw the shadows, so everything layers correctly)
        drawObjects();

        // Place the light.  Light will move with the rest of the scene
        setLightPosition(GL11.GL_LIGHT1, lightPosition);

		// render some text using texture-mapped font
		print( 30, viewportH- 45, 0, "Use arrow keys to navigate:");
        print( 30, viewportH- 80, 1, "Left-Right arrows rotate camera");
        print( 30, viewportH-100, 1, "Up-Down arrows move camera forward and back");
        print( 30, viewportH-120, 1, "PageUp-PageDown move vertically");
        print( 30, viewportH-140, 1, "SPACE key switches cameras");


    }

    public void drawObjects() {
        // draw the airplane
//        GL11.glPushMatrix();
//        {
//        	///////////////////////// billboard /////////////////////////////
//        	// place plane at orbit point, and orient it toward sphere
//        	GLCamera.billboardPoint(airplanePos, ORIGIN, UP);
//        	/////////////////////////
//        	// turn plane toward direction of motion
//            GL11.glRotatef(-90, 0, 1, 0);
//            // make it big
//            GL11.glScalef(4f, 4f, 4f);
//        	airplane.render();
//        	// reset material, since model.render() will alter current material settings
//            setMaterial( new float[] {.8f, .8f, .7f, 1f}, .4f);
//        }
//        GL11.glPopMatrix();

    	// draw the earth
        GL11.glPushMatrix();
        {
            GL11.glTranslatef(spherePos[0], spherePos[1], spherePos[2]); // move
            GL11.glRotatef(rotation, 0, 0, 1);  // rotate around Y axis
            GL11.glScalef(2f, 2f, 2f);          // scale up
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, sphereTextureHandle);
            callDisplayList(earth);
        }
        GL11.glPopMatrix();
    }

    public void mouseMove(int x, int y) {
    }

    public void mouseDown(int x, int y) {
    }

    public void mouseUp(int x, int y) {
    }

    public void keyDown(int keycode) {
    	if (Keyboard.KEY_SPACE == keycode) {
    		cam.setCamera((cam.camera == camera1)? camera2 : camera1);
    	}
    }

    public void keyUp(int keycode) {
    }
}