package trials;


/**
 * A 3D vector
 */
public class GL_Vector
{
	public float x=0;
	public float y=0;
	public float z=0;


	public GL_Vector ()
	{
	}

	public GL_Vector (float xpos, float ypos, float zpos)
	{
		x=xpos;
		y=ypos;
		z=zpos;
	}


	public GL_Vector (float[] float3)
	{
		x = float3[0];
		y = float3[1];
		z = float3[2];
	}


	// Normalizes the vector
	public GL_Vector normalize()
	{
		float dist=length();
		if (dist==0) return this;
		float invdist=1/dist;
		x*=invdist;
		y*=invdist;
		z*=invdist;
		return this;
	}

	// Reverses the vector
	public GL_Vector reverse()
	{
		x=-x;
		y=-y;
		z=-z;
		return this;
	}

	// Lenght of this vector
	public float length()
	{
		return (float)Math.sqrt(x*x+y*y+z*z);
	}

	// returns the normal vector of the plane defined by the two vectors
	public static GL_Vector getNormal(GL_Vector a, GL_Vector b)
	{
		return vectorProduct(a,b).normalize();
	}

    // returns the normal vector of the plane defined by the two vectors
    public static GL_Vector getNormal(GL_Vector a, GL_Vector b, GL_Vector c)
    {
        return vectorProduct(a,b,c).normalize();
    }

    // returns a x b
    public static GL_Vector vectorProduct(GL_Vector a, GL_Vector b)
    {
        return new GL_Vector(a.y*b.z-b.y*a.z, a.z*b.x-b.z*a.x, a.x*b.y-b.x*a.y);
    }

    // returns (b-a) x (c-a)
    public static GL_Vector vectorProduct(GL_Vector a, GL_Vector b, GL_Vector c)
    {
        return vectorProduct(sub(b,a),sub(c,a));
    }

    public static GL_Vector multiply(GL_Vector v, float r) {
        GL_Vector res = new GL_Vector();
        res.x = v.x * r;
        res.y = v.y * r;
        res.z = v.z * r;
        return res;
    }

	// returns the angle between 2 vectors
	public static float angle(GL_Vector a, GL_Vector b)
	{
		a.normalize();
		b.normalize();
		return (a.x*b.x+a.y*b.y+a.z*b.z);
	}

	// adds 2 vectors
	public static GL_Vector add(GL_Vector a, GL_Vector b)
	{
		return new GL_Vector(a.x+b.x,a.y+b.y,a.z+b.z);
	}

    // subtracts 2 vectors
    public static GL_Vector sub(GL_Vector a, GL_Vector b)
    {
        return new GL_Vector(a.x-b.x,a.y-b.y,a.z-b.z);
    }

	// scales the vector
	public static GL_Vector scale(float f, GL_Vector a)
	{
		return new GL_Vector(f*a.x,f*a.y,f*a.z);
	}

	// length of vector
	public static float len(GL_Vector a)
	{
		return (float)Math.sqrt(a.x*a.x+a.y*a.y+a.z*a.z);
	}

	public String toString()
	{
		return new String ("<vector x="+x+" y="+y+" z="+z+">");
	}

	public GL_Vector getClone()
	{
		return new GL_Vector(x,y,z);
	}

	/**
	 *  return the dot product of two vectors
	 */
	public static float dotProduct(GL_Vector u, GL_Vector v)
	{
		return u.x * v.x + u.y * v.y + u.z * v.z;
	}
	
	/**
	 * return a vector rotated the given number of degrees around the Y axis  
	 */
	public static GL_Vector rotationVector(float degrees) {
		GL_Vector v = new GL_Vector(
				(float)Math.sin(degrees * GLApp.PIOVER180),
				0,
				(float)Math.cos(degrees * GLApp.PIOVER180) );
		return v;
	}
	
    ////////////////////////////////////////////////////

    // Computes the magnitude of a normal.   (magnitude = sqrt(x^2 + y^2 + z^2)
    public static  float CMag(GL_Vector norm) {
        return (float)Math.sqrt(norm.x*norm.x + norm.y*norm.y + norm.z*norm.z);
    }

    // Calculates a vector between 2 points and returns the result
    public static GL_Vector CVector(GL_Vector vPoint1, GL_Vector vPoint2)
    {
        GL_Vector vVector = new GL_Vector(							// The variable to hold the resultant vector
        vPoint1.x - vPoint2.x,			// Subtract point1 and point2 x's
        vPoint1.y - vPoint2.y,			// Subtract point1 and point2 y's
        vPoint1.z - vPoint2.z);			// Subtract point1 and point2 z's
        return vVector;								// Return the resultant vector
    }

    // Adds 2 vectors together and returns the result
    public static GL_Vector CAddVector(GL_Vector vVector1, GL_Vector vVector2)
    {
        GL_Vector vResult = new GL_Vector(						// The variable to hold the resultant vector
        vVector2.x + vVector1.x,		// Add Vector1 and Vector2 x's
        vVector2.y + vVector1.y,		// Add Vector1 and Vector2 y's
        vVector2.z + vVector1.z );		// Add Vector1 and Vector2 z's
        return vResult;								// Return the resultant vector
    }

    // Divides a vector by a single number (scalar) and returns the result
    public static GL_Vector CDivideVectorByScaler(GL_Vector vVector1, float Scaler)
    {
        GL_Vector vResult = new GL_Vector(							// The variable to hold the resultant vector
        vVector1.x / Scaler,			// Divide Vector1's x value by the scaler
        vVector1.y / Scaler,			// Divide Vector1's y value by the scaler
        vVector1.z / Scaler);			// Divide Vector1's z value by the scaler
        return vResult;								// Return the resultant vector
    }

    // Returns the cross product between 2 vectors
    public static GL_Vector CCross(GL_Vector vVector1, GL_Vector vVector2)
    {
        GL_Vector vCross = new GL_Vector(								// The vector to hold the cross product
                                                    // Get the X value
        ((vVector1.y * vVector2.z) - (vVector1.z * vVector2.y)),// Get the Y value
        ((vVector1.z * vVector2.x) - (vVector1.x * vVector2.z)), // Get the Z value
        ((vVector1.x * vVector2.y) - (vVector1.y * vVector2.x))  );
        return vCross;								// Return the cross product
    }

    // Return the normal of a vector
    public static GL_Vector CNormalize(GL_Vector vNormal)
    {
        double Magnitude = CMag(vNormal);			// Get the magnitude

        vNormal.x /= (float)Magnitude;				// Divide each axis by the magnitude
        vNormal.y /= (float)Magnitude;
        vNormal.z /= (float)Magnitude;

        return vNormal;								// Return the normal
    }
}