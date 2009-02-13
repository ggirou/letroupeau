package trials;
import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

/**
 * Camera class by Philipp Crocoll at CodeColony (codecolony.de).
 * Ported from C++ to Java by mark napier (potatoland.org).
 *
 * Can move camera position forward/back, pan left/right, shift up/down, and
 * rotate around X, Y, Z axes.
 *
 * Uses gluLookat() to orient scene to camera position.  See Camera.render().
 *
 * To use:
 *    renderScene() {
 *       GL11.glMatrixMode(GL11.GL_MODELVIEW);
 *       GL11.glLoadIdentity();
 *       camera.Render();
 *       // render scene ...
 *    }
 *
 * Requires GL_Vector to perform vector operations.
 *
 * jul13,2006: added move(x,y,z).  added ctor(pos,dir,up).
 */
public class GLCamera {
    static final float PIdiv180 = 0.0174532925f;
    GL_Vector ViewDir;
    GL_Vector RightVector;
    GL_Vector UpVector;
    GL_Vector Position;
    public float RotatedX, RotatedY, RotatedZ;

    public GLCamera() {
        setCamera( 	0f, 0f, 0f,         // position at origin
        			0f, 0f, -1f,        // looking down Z axis
        			0f, 1f, 0f );       // camera up axis is straight up Y axis
    }

    public GLCamera(float posx, float posy, float posz,
                   float dirx, float diry, float dirz,
                   float upx, float upy, float upz)
    {
        setCamera( posx,posy,posz, dirx,diry,dirz, upx,upy,upz );
    }

    /**
     * Set the camera position, view direction and up vector.  NOTE: direction
     * is direction the camera is facing NOT a target position (as in gluLookAt()).  
     * 
     * @param posx   Position of camera
     * @param posy
     * @param posz
     * @param dirx   Direction camera is facing
     * @param diry
     * @param dirz
     * @param upx    Up vector
     * @param upy
     * @param upz
     */
    public void setCamera(float posx, float posy, float posz,
    		float dirx, float diry, float dirz,
    		float upx, float upy, float upz)
    {
        if (upx == 0 && upy == 0 && upz == 0) {
        	System.out.println("GLCamera.setCamera(): Up vector needs to be defined");
        	upx=0; upy=1; upz=0;
        }
        if (dirx == 0 && diry == 0 && dirz == 0) {
        	System.out.println("GLCamera.setCamera(): ViewDirection vector needs to be defined");
        	dirx=0; diry=0; dirz=-1;
        }
    	Position 	= new GL_Vector(posx, posy, posz);
    	ViewDir 	= new GL_Vector(dirx, diry, dirz);
    	UpVector 	= new GL_Vector(upx, upy, upz);
    	RightVector	= GL_Vector.CCross(ViewDir, UpVector);
    	RotatedX = RotatedY = RotatedZ = 0.0f;
    }

    /**
     * Set the camera to look at a target.  Positions the camera on the same X and Y
     * as the target, at the Z value specified by the distance param, looking down 
     * the Z axis.
     *  
     * @param targetX     camera will face this XYZ
     * @param targetY
     * @param targetZ
     * @param distance    distance from target
     */
    public void setCamera(float targetX, float targetY, float targetZ,  float distance)
    {
    	Position 	= new GL_Vector(targetX, targetY, targetZ+distance);
    	ViewDir 	= new GL_Vector(0, 0, -1);
    	UpVector 	= new GL_Vector(0, 1, 0);
    	RightVector	= GL_Vector.CCross(ViewDir, UpVector);
    	RotatedX = RotatedY = RotatedZ = 0.0f;
    }
    
	/**
	 * Move camera position in the given direction
	 */
    public void viewDir(GL_Vector direction) {
        ViewDir = direction;
    	RightVector	= GL_Vector.CCross(ViewDir, UpVector);
    }

	/**
	 * Move camera position in the given direction
	 */
    public void Move(GL_Vector Direction) {
        Position = GL_Vector.add(Position, Direction);
    }

	/**
	 * Move camera position in the given direction
	 */
    public void Move(float x, float y, float z) {
        GL_Vector Direction = new GL_Vector(x,y,z);
        Position = GL_Vector.add(Position, Direction);
    }

	/**
	 * Move camera to the given xyz
	 */
    public void MoveTo(float x, float y, float z) {
        Position = new GL_Vector(x, y, z);
    }

    public void RotateX(float Angle) {
        RotatedX += Angle;

        //Rotate viewdir around the right vector:
        ViewDir = GL_Vector.CNormalize(
            GL_Vector.add(
				GL_Vector.multiply(ViewDir, (float) Math.cos(Angle * PIdiv180)),
				GL_Vector.multiply(UpVector, (float) Math.sin(Angle * PIdiv180))
            )
        );

        //now compute the new UpVector (by cross product)
        UpVector = GL_Vector.multiply(GL_Vector.CCross(ViewDir, RightVector), -1);
    }

    public void RotateY(float Angle) {
        RotatedY += Angle;

        //Rotate viewdir around the up vector:
        ViewDir = GL_Vector.CNormalize(
            GL_Vector.sub(
				GL_Vector.multiply(ViewDir, (float) Math.cos(Angle * PIdiv180)),
				GL_Vector.multiply(RightVector, (float) Math.sin(Angle * PIdiv180))
            ));

        //now compute the new RightVector (by cross product)
        RightVector = GL_Vector.CCross(ViewDir, UpVector);
    }

    public void RotateZ(float Angle) {
        RotatedZ += Angle;

        //Rotate viewdir around the right vector:
        RightVector = GL_Vector.CNormalize(
            GL_Vector.add(
				GL_Vector.multiply(RightVector, (float) Math.cos(Angle * PIdiv180)),
				GL_Vector.multiply(UpVector, (float) Math.sin(Angle * PIdiv180))
            ));

        //now compute the new UpVector (by cross product)
        UpVector = GL_Vector.multiply(GL_Vector.CCross(ViewDir, RightVector), -1);
    }

    public void MoveForward(float Distance) {
        Position = GL_Vector.add(Position, GL_Vector.multiply(ViewDir, -Distance));
    }

    public void MoveUpward(float Distance) {
        Position = GL_Vector.add(Position, GL_Vector.multiply(UpVector, Distance));
    }

    public void StrafeRight(float Distance) {
        Position = GL_Vector.add(Position, GL_Vector.multiply(RightVector, Distance));
    }

    /**
     * Call GLU.gluLookAt() to set view position, direction and orientation.  Be 
     * sure that the modelview matrix is current before calling Render() 
     * (glMatrixMode(GL_MODEL_VIEW)).
     */
    public void Render() {
        //The point at which the camera looks:
        GL_Vector ViewPoint = GL_Vector.add(Position, ViewDir);

        //as we know the up vector, we can easily use gluLookAt:
        GLU.gluLookAt(Position.x, Position.y, Position.z,
                      ViewPoint.x, ViewPoint.y, ViewPoint.z,
                      UpVector.x, UpVector.y, UpVector.z);

		//System.out.println(Position.x + "," + Position.y + "," + Position.z + "  " + 
		//		ViewDir.x + "," + ViewDir.y + "," + ViewDir.z + "  " + 
		//		UpVector.x + "," + UpVector.y + "," + UpVector.z + "  " 
		//		);
    }
    
	 /**
	  * Given position of billboard and target, create rotation matrix to
	  * orient billboard so it faces target.
	  */
	 public static void billboardPoint(GL_Vector bbPos, GL_Vector targetPos, GL_Vector targetUp)
	 {
		 // direction the billboard is looking:
		 GL_Vector look = GL_Vector.sub(targetPos,bbPos).normalize();
		 
		 // billboard Right vector is perpendicular to Look and Up (the targets Up vector)
		 GL_Vector right = GL_Vector.CCross(targetUp,look).normalize();
		 
		 // billboard Up vector is perpendicular to Look and Right
		 GL_Vector up = GL_Vector.CCross(look,right).normalize();
		 
		 // make a reusable buffer for matrix
		 if (bbmatrix == null) {
			 bbmatrix = GLApp.allocFloats(16);
		 }
		 
		 createBillboardMatrix(bbmatrix, right, up, look, bbPos);
		 
		 // apply the billboard
		 GL11.glMultMatrix(bbmatrix);
	 }
	 
	 
	 static FloatBuffer bbmatrix = null;
	 
	 /**
	  * vCreate the billboard matrix: a rotation matrix created from an arbitrary set
	  * of axis.  Store those axis values in the first 3 columns of the matrix.  Col
	  * 1 is the X axis, col 2 is the Y axis, and col 3 is the Z axis.  We are
	  * rotating right into X, up into Y, and look into Z.  The rotation matrix
	  * created from the rows will translate the arbitrary axis set to the global
	  * vaxis set.  Lastly, OpenGl stores the matrices by columns, so enter the data
	  * into the array columns first.
	  *
	  * pos: position of billboard
	  * right, up, look: orientation of billboard x,y,z axes
	  */
	 public static void createBillboardMatrix(FloatBuffer matrix, GL_Vector right, GL_Vector up, GL_Vector look, GL_Vector pos)
	 {
		 matrix.put(0,  right.x);
		 matrix.put(1,  right.y);
		 matrix.put(2,  right.z);
		 matrix.put(3,  0);
		 
		 matrix.put(4,  up.x);
		 matrix.put(5,  up.y);
		 matrix.put(6,  up.z);
		 matrix.put(7,  0);
		 
		 matrix.put(8,  look.x);
		 matrix.put(9,  look.y);
		 matrix.put(10, look.z);
		 matrix.put(11, 0);
		 
		 // Add the translation in as well.
		 matrix.put(12, pos.x);
		 matrix.put(13, pos.y);
		 matrix.put(14, pos.z);
		 matrix.put(15, 1);
	 }
	 
	 
	 /** 
	  * Return the current camera view direction
	  */
	 public GL_Vector getViewDir() {
		 return ViewDir;
	 }
	 
}
