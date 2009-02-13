/**
 * 
 */
package View;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Collection;
import java.util.HashSet;

/**
 * @author GGirou
 * 
 */
public class Keyboard implements KeyListener {
	HashSet<Integer> keysPressed = new HashSet<Integer>();

	@Override
	public void keyPressed(KeyEvent e) {
		keysPressed.add(e.getKeyCode());
	}

	@Override
	public void keyReleased(KeyEvent e) {
		keysPressed.remove(e.getKeyCode());
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	public boolean isPressed(int keyCode) {
		return keysPressed.contains(keyCode);
	}

	public boolean isPressed(Collection<Integer> keyCodes) {
		return keysPressed.containsAll(keyCodes);
	}
}
