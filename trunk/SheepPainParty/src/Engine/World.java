/**
 * 
 */
package Engine;

import Model.Position;
import Model.SphericalCoordinate;

/**
 * @author GGirou
 * 
 */
public class World {
	/**
	 * @param width
	 * @param height
	 * @return
	 */
	public static World create(float radius) {
		return new World(new Model.World(radius));
	}

	private Model.World modelWorld;

	/**
	 * @param modelWorld
	 */
	private World(Model.World modelWorld) {
		super();
		this.modelWorld = modelWorld;
	}

	/**
	 * @param item
	 */
	public void addItem(Item item) {
		this.modelWorld.addItem(item.getModelItem());
	}

	public SheepUser createSheepUser(SphericalCoordinate coordinate) {
		SheepUser o = new SheepUser(new Model.Sheep(new Position(
				this.modelWorld, coordinate)));
		return o;
	}

	/**
	 * @return the modelWorld
	 */
	protected Model.World getModelWorld() {
		return modelWorld;
	}
}
