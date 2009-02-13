/**
 * 
 */
package Model;

import java.util.Hashtable;

/**
 * @author GGirou
 * 
 */
public class World {
	private Hashtable<Integer, Item> itemSet = new Hashtable<Integer, Item>();

	private float radius;

	/**
	 * @param dimension
	 */
	public World(float radius) {
		super();
		this.radius = radius;
	}

	/**
	 * @param item
	 */
	public void addItem(Item item) {
		this.itemSet.put(item.getId(), item);
	}

	/**
	 * @param id
	 * @return
	 */
	public Item getItem(int id) {
		return this.itemSet.get(id);
	}

	/**
	 * @return the radius
	 */
	public float getRadius() {
		return radius;
	}

}
