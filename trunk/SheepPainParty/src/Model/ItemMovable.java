package Model;

/**
 * @author GGirou
 *
 */
public abstract class ItemMovable extends Item {

	/**
	 * @param position
	 */
	public ItemMovable(Position position) {
		super(position);
		// TODO Auto-generated constructor stub
	}

//	public void Move(double direction, double distance){
//		
//	}

	public void Move(Position to){
		this.setPosition(to);
	}
}
