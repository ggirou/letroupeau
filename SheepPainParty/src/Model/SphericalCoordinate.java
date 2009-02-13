/**
 * 
 */
package Model;

/**
 * @author GGirou
 * 
 */
public class SphericalCoordinate {
	private static float TWO_PI = (float) (2 * Math.PI);
	private float radius, theta, phi;

	/**
	 * @param radius
	 * @param theta
	 * @param phi
	 */
	public SphericalCoordinate(float radius, float theta, float phi) {
		super();
		this.radius = radius;
		this.theta = theta % TWO_PI;
		this.phi = phi % TWO_PI;
	}

	/**
	 * @return the phi
	 */
	public float getPhi() {
		return phi;
	}

	/**
	 * @return the radius
	 */
	public float getRadius() {
		return radius;
	}

	/**
	 * @return the theta
	 */
	public float getTheta() {
		return theta;
	}

	/**
	 * @param phi
	 *            the phi to set
	 */
	public void setPhi(float phi) {
		this.phi = phi % TWO_PI;
	}

	/**
	 * @param radius
	 *            the radius to set
	 */
	public void setRadius(float radius) {
		this.radius = radius;
	}

	/**
	 * @param theta
	 *            the theta to set
	 */
	public void setTheta(float theta) {
		this.theta = theta % TWO_PI;
	}
}
