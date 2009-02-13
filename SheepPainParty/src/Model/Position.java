/**
 * 
 */
package Model;


/**
 * @author GGirou
 * 
 */
public class Position extends SphericalCoordinate {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private World world;

	/**
	 * @param world
	 */
	public Position(World world, SphericalCoordinate coordinate) {
		super(coordinate.getRadius(), coordinate.getTheta(), coordinate.getPhi());
		this.world = world;
	}

	/**
	 * @return
	 */
	public World getWorld() {
		return world;
	}

}
