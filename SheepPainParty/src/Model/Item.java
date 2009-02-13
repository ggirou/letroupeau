/**
 * 
 */
package Model;

import java.awt.geom.Dimension2D;

/**
 * @author GGirou
 * 
 */
public abstract class Item {
	private static int currentId = 0;


	private int id = currentId++;

	private Position position;
	private Dimension2D size;
	private double direction;

	/**
	 * @param position
	 */
	public Item(Position position) {
		super();
		this.position = position;
	}

	/**
	 * @return the direction
	 */
	public double getDirection() {
		return direction;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return
	 */
	public Position getPosition() {
		return position;
	}

	/**
	 * @return
	 */
	public Dimension2D getSize() {
		return size;
	}

	/**
	 * @param size
	 */
	public void setSize(Dimension2D size) {
		this.size = size;
	}

	/**
	 * @param direction
	 *            the direction to set
	 */
	protected void setDirection(double direction) {
		this.direction = direction;
	}

	/**
	 * @param position
	 *            the position to set
	 */
	protected void setPosition(Position position) {
		this.position = position;
	}
}
