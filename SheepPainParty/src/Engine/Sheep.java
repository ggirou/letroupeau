/**
 * 
 */
package Engine;

/**
 * @author GGirou
 * 
 */
public abstract class Sheep extends ItemMovable {

	/**
	 * 
	 */
	protected Sheep(Model.Sheep modelSheep) {
		super(modelSheep);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Engine.Item#getModeleItem()
	 */
	@Override
	protected Model.Sheep getModelItem() {
		return (Model.Sheep) super.getModelItem();
	}

	public void updatePosition() {

	}
}
