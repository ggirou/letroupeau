/**
 * 
 */
package Engine;


/**
 * @author GGirou
 * 
 */
public class Item {
	private Model.Item modelItem;

	/**
	 * @return the modelItem
	 */
	protected Model.Item getModelItem() {
		return modelItem;
	}

	/**
	 * @param modeleItem
	 */
	public Item(Model.Item modelItem) {
		super();
		this.modelItem = modelItem;
	}

}
