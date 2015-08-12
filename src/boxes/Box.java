package boxes;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

public abstract class Box extends JLabel implements Serializable {
	private static final long serialVersionUID = 1L;
	private Box nextBox;
	private boolean selected;
	private boolean toBeSelected;
	
	public void setNextBox(Box nextBox) {
		this.nextBox = nextBox;
	}
	
	public Box getNextBox() {
		return nextBox;
	}
	
	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public boolean isToBeSelected() {
		return toBeSelected;
	}

	public void setToBeSelected(boolean toBeSelected) {
		this.toBeSelected = toBeSelected;
	}
	
	public Box() {
		super("PlaceHolder");
		setFont(new Font("Calibri", Font.PLAIN, 21));
		setHorizontalAlignment(SwingConstants.CENTER);
		nextBox = null;
		selected = false;
		toBeSelected = false;
	}
	
	public Box deepClone() {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(this);
		
			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(bais);
			Box newBox = (Box) ois.readObject();
			newBox.setNextBox(nextBox);
			return newBox;
		} catch (IOException | ClassNotFoundException e) {
			System.err.println("Could not deep clone: " + e.getMessage());
			return null;
		}
	}
	
	public void openEditDialog() {}
	
	public void update() {
		setSize(getPreferredSize().width + 40, getPreferredSize().height + 15);
	}
	
	private static final KeyStroke escapeStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
	
	public static void installEscapeOnCloseOperation(final JDialog dialog) {
		Action dispatchClosing = new AbstractAction() {
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e) {
				dialog.dispatchEvent(new WindowEvent(dialog, WindowEvent.WINDOW_CLOSING));
			}
		};
		JRootPane root = dialog.getRootPane();
		root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeStroke, "WINDOW_CLOSING");
		root.getActionMap().put("WINDOW_CLOSING", dispatchClosing);
	}
	
	// Returns the int value of the byte if formatted correctly, or -1 if not
	public static int checkHexByte(String hex) {
		String hexNumber = checkOnlyZeros(hex) ? "00" : hex.replaceAll("^0+", "");	// Remove leading zeros
		if (hexNumber.length() > 2 || hexNumber.matches(".*[^a-fA-F0-9].*") || hexNumber.contentEquals("")) return -1;
		return Integer.parseInt(hexNumber, 16);
	}
	
	private static boolean checkOnlyZeros(String str) {
		for (char c : str.toCharArray()) {
			if (c != '0') return false;
		}
		return true;
	}
	
	public static String toHex(int n) {
		String hexNumber = Integer.toHexString(n).toUpperCase();
		if (hexNumber.length() == 1) hexNumber = new StringBuilder(hexNumber).insert(0, "0").toString();
		return "0x" + hexNumber;
	}
}
