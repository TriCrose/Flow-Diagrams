package window;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import boxes.*;

public class TabbedPane extends JTabbedPane {
	private static final long serialVersionUID = -8194783928576922452L;
	
	private WindowPanel clipboard;
	
	public WindowPanel getClipboard() {
		return clipboard;
	}
	
	public TabbedPane(WindowPanel mainProgram) {
		super();
		addTab("Main Program", mainProgram);
		addFocusListener(mainProgram);
		updateMnemonics();
	}
	
	private WindowPanel getClipboardFromPanel(WindowPanel windowPanel) {
		WindowPanel newClipboard = windowPanel.deepClone();
		for (Component c : newClipboard.getComponents()) if (c instanceof Box) {
			if (((Box) c).isSelected()) ((Box) c).setSelected(false);
			else ((Box) c).setSelected(true);
		}
		newClipboard.deleteSelectedComponents();
		newClipboard.remove(newClipboard.getStartBox());
		for (Component c : newClipboard.getComponents()) if (c instanceof Box) ((Box) c).setSelected(true);
		return newClipboard;
	}
	
	public void copyToClipboard() {
		if (((WindowPanel) getComponentAt(getSelectedIndex())).isNothingSelected()) return;
		if (!(((WindowPanel) getComponentAt(getSelectedIndex())).isDoingNothing())) return;
		clipboard = getClipboardFromPanel(((WindowPanel) getComponentAt(getSelectedIndex())));
	}
	
	public void cutToClipboard() {
		copyToClipboard();
		((WindowPanel) getComponentAt(getSelectedIndex())).deleteSelectedComponents();
	}
	
	public void pasteFromClipboard() {
		if (clipboard == null) return;
		for (Component c : ((WindowPanel) getComponentAt(getSelectedIndex())).getComponents()) if (c instanceof Box) ((Box) c).setSelected(false);		// Deselect everything
		for (Component c : clipboard.getComponents()) if (c instanceof Box) c.setLocation(c.getX() + 120, c.getY());
		WindowPanel newClipboard = clipboard.deepClone();
		for (Component c : newClipboard.getComponents()) if (c instanceof Box) {
			((WindowPanel) getComponentAt(getSelectedIndex())).addBox((Box) c, c.getX(), c.getY());
		}
		MainWindow.setSaved(false);
	}
	
	public boolean availableSubroutineName(String name) {
		for (int i = 0; i < getTabCount(); i++) {
			if (((WindowPanel) getComponentAt(i)).getSubroutineName().contentEquals(name)) return false;
		}
		return true;
	}
	
	public String[] getSubroutineNamesExceptSelected() {
		ArrayList<String> subroutineNames = new ArrayList<String>();
		for (int i = 1; i < getTabCount(); i++) {
			if (i == getSelectedIndex()) continue;
			subroutineNames.add(((WindowPanel) getComponentAt(i)).getSubroutineName());
		}
		
		String[] array = new String[subroutineNames.size()];
		for (int i = 0; i < subroutineNames.size(); i++) array[i] = subroutineNames.get(i);
		return array;
	}
	
	public void addSubroutine(final WindowPanel subroutine) {
		JLabel tabTitle = new JLabel(subroutine.getSubroutineName());
		tabTitle.addMouseListener(new TabMouseListener(subroutine, this));
		addTab(subroutine.getSubroutineName(), subroutine);
		setTabComponentAt(getTabCount() - 1, tabTitle);
		addFocusListener(subroutine);
		updateMnemonics();
		MainWindow.setSaved(false);
	}
	
	public void renameSubroutine(String oldName) {
		String newSubName;
		do {
			newSubName = JOptionPane.showInputDialog(MainWindow.getMainWindow(), "Enter new subroutine name:", "Rename Subroutine", JOptionPane.PLAIN_MESSAGE);
			if (newSubName == null) return;
			else if (newSubName.contentEquals("")) JOptionPane.showMessageDialog(MainWindow.getMainWindow(), "Please enter a subroutine name.");
			else if (!availableSubroutineName(newSubName)) JOptionPane.showMessageDialog(MainWindow.getMainWindow(), "Subroutine name already exists.");
		} while (newSubName.contentEquals("") || !availableSubroutineName(newSubName));
		
		// Amend all occurrences of the old subroutine name
		for (int i = 0; i < getTabCount(); i++) {
			for (Component c : ((WindowPanel) getComponentAt(i)).getComponents()) if (c instanceof SubroutineBox) {
				if (((SubroutineBox) c).getSubroutineName() == oldName) ((SubroutineBox) c).setSubroutineName(newSubName);
			} if (((WindowPanel) getComponentAt(i)).getSubroutineName().contentEquals(oldName)) {
				((WindowPanel) getComponentAt(i)).setSubroutineName(newSubName);
				((JLabel) getTabComponentAt(i)).setText(newSubName);
			}
		}
		MainWindow.setSaved(false);
	}
	
	public void deleteSubroutine(String name) {
		if (JOptionPane.showConfirmDialog(MainWindow.getMainWindow(), "Delete subroutine: " + name + "?", "Delete Subroutine", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) return;
		for (int i = 0; i < getTabCount(); i++) {
			if (((WindowPanel) getComponentAt(i)).getSubroutineName().contentEquals(name)) remove(getComponentAt(i));
		}
		for (int i = 0; i < getTabCount(); i++) for (Component c : ((WindowPanel) getComponentAt(i)).getComponents()) {
			 if (c instanceof SubroutineBox) ((SubroutineBox) c).checkValid();
		}
		updateMnemonics();
		MainWindow.setSaved(false);
	}
	
	private void updateMnemonics() {
		for (int i = 0; i < getTabCount(); i++) {
			if (i == 9) setMnemonicAt(i, KeyEvent.VK_0);
			if (i >= 9) break;
			setMnemonicAt(i, KeyEvent.VK_1 + i);
		}
	}
}
