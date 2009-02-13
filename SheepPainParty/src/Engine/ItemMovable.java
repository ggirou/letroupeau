/**
 * 
 */
package Engine;

/**
 * @author GGirou
 *
 */
public class ItemMovable extends Item {

	/**
	 * @param modelItem
	 */
	public ItemMovable(Model.ItemMovable modelItem) {
		super(modelItem);
	}

	/* (non-Javadoc)
	 * @see Engine.Item#getModeleItem()
	 */
	@Override
 	protected Model.ItemMovable getModelItem() {
		return (Model.ItemMovable)super.getModelItem();
	}	

}
