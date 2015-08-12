package window;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class TabMouseListener implements MouseListener {
	private WindowPanel subroutine;
	private TabbedPane pane;
	
	public TabMouseListener(WindowPanel subroutine, TabbedPane pane) {
		this.subroutine = subroutine;
		this.pane = pane;
	}
	
	public void mouseExited(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) pane.renameSubroutine(subroutine.getSubroutineName());
	}
	
	public void mouseReleased(MouseEvent e) {
		if (e.isPopupTrigger()) showPopup(e);
	}
	
	public void mousePressed(MouseEvent e) {
		if (e.isPopupTrigger()) showPopup(e);
		else pane.setSelectedComponent(subroutine);
	}
	
	private void showPopup(MouseEvent e) {
		JPopupMenu rightClickMenu = new JPopupMenu() {
			private static final long serialVersionUID = 1L;
			/* Constructor */ {
				final JMenuItem itemRename = new JMenuItem("Rename...");
				final JMenuItem itemDelete = new JMenuItem("Delete");
				ActionListener actionListener = new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (e.getActionCommand() == itemRename.getActionCommand()) {
							pane.renameSubroutine(subroutine.getSubroutineName());
						} else if (e.getActionCommand() == itemDelete.getActionCommand()) {
							pane.deleteSubroutine(subroutine.getSubroutineName());
						}
					}
				};
				
				itemRename.addActionListener(actionListener);
				itemDelete.addActionListener(actionListener);
				add(itemRename);
				add(itemDelete);
			}
		};
		rightClickMenu.show(e.getComponent(), e.getX(), e.getY());
	}
}
