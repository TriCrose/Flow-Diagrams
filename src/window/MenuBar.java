package window;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import boxes.*;

// old_TODO: add undo button (remember to add a setSaved(false))
// old_TODO: add snap to grid functionality (remember to add a setSaved(false))

public class MenuBar extends JMenuBar implements ActionListener {
	private static final long serialVersionUID = 1398877354165276648L;
	
	private MainWindow parent;
	private TabbedPane pane;
	
	private JMenu fileMenu;
	private JMenuItem file_new, file_open, file_save, file_saveAs, file_import, file_export, file_exit;
	private JMenu editMenu;
	private JMenuItem edit_cut, edit_copy, edit_paste;
	private JMenu insertMenu;
	private JMenuItem insert_input, insert_process, insert_output, insert_decision, insert_end, insert_adc, insert_sub, insert_sub_box, insert_break, insert_return;
	private JMenu selectionMenu;
	private JMenuItem selection_clearSelection, selection_invertSelection, selection_deleteLinks, selection_deleteSelected;
	private JMenu outputMenu;
	private JMenuItem output_exportXML;
	private JMenu viewMenu;
	private JMenuItem view_findStart;
	private JMenu helpMenu;
	private JMenuItem help_showDiagram, help_about;
	
	public void setPane(TabbedPane pane) {
		this.pane = pane;
	}
	
	public MenuBar(MainWindow parent, TabbedPane pane) {
		super();
		this.parent = parent;
		this.pane = pane;
		
		// File menu
		fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		add(fileMenu);
		
		file_new = createMenuItem("New", KeyEvent.VK_N, KeyEvent.VK_N, ActionEvent.CTRL_MASK);
		file_open = createMenuItem("Open...", KeyEvent.VK_O, KeyEvent.VK_O, ActionEvent.CTRL_MASK);
		file_save = createMenuItem("Save", KeyEvent.VK_S, KeyEvent.VK_S, ActionEvent.CTRL_MASK);
		file_saveAs = createMenuItem("Save As...", KeyEvent.VK_A, KeyEvent.VK_S, ActionEvent.CTRL_MASK + ActionEvent.SHIFT_MASK);
		file_import = createMenuItem("Import...", KeyEvent.VK_I, KeyEvent.VK_I, ActionEvent.CTRL_MASK);
		file_export = createMenuItem("Export Subroutine...", KeyEvent.VK_E, KeyEvent.VK_E, ActionEvent.CTRL_MASK);
		file_exit = createMenuItem("Exit", KeyEvent.VK_X);
		
		fileMenu.add(file_new);
		fileMenu.add(file_open);
		fileMenu.add(file_save);
		fileMenu.add(file_saveAs);
		fileMenu.addSeparator();
		fileMenu.add(file_import);
		fileMenu.add(file_export);
		fileMenu.addSeparator();
		fileMenu.add(file_exit);
		
		// Edit menu
		editMenu = new JMenu("Edit");
		editMenu.setMnemonic(KeyEvent.VK_E);
		add(editMenu);
		
		edit_cut = createMenuItem("Cut", KeyEvent.VK_T, KeyEvent.VK_X, ActionEvent.CTRL_MASK);
		edit_copy = createMenuItem("Copy", KeyEvent.VK_C, KeyEvent.VK_C, ActionEvent.CTRL_MASK);
		edit_paste = createMenuItem("Paste", KeyEvent.VK_P, KeyEvent.VK_V, ActionEvent.CTRL_MASK);
		
		editMenu.add(edit_cut);
		editMenu.add(edit_copy);
		editMenu.add(edit_paste);
		
		// Insert menu
		insertMenu = new JMenu("Insert");
		insertMenu.setMnemonic(KeyEvent.VK_I);
		add(insertMenu);
		
		insert_input = createMenuItem("Input Box", KeyEvent.VK_I, KeyEvent.VK_I, 0);
		insert_process = createMenuItem("Process Box", KeyEvent.VK_P, KeyEvent.VK_P, 0);
		insert_output = createMenuItem("Output Box", KeyEvent.VK_O, KeyEvent.VK_O, 0);
		insert_decision = createMenuItem("Decision Box", KeyEvent.VK_D, KeyEvent.VK_D, 0);
		insert_end = createMenuItem("End Box", KeyEvent.VK_E, KeyEvent.VK_E, 0);
		insert_adc = createMenuItem("Read ADC Box", KeyEvent.VK_R, KeyEvent.VK_R, 0);
		insert_sub = createMenuItem("Subroutine...", KeyEvent.VK_S, KeyEvent.VK_S, ActionEvent.CTRL_MASK + ActionEvent.ALT_MASK);
		insert_sub_box = createMenuItem("Subroutine Box", KeyEvent.VK_S, KeyEvent.VK_S, 0);
		insert_break = createMenuItem("Break Box", KeyEvent.VK_B, KeyEvent.VK_B, 0);
		insert_return = createMenuItem("Return Box", KeyEvent.VK_R, KeyEvent.VK_T, 0);
		
		insertMenu.add(insert_input);
		insertMenu.add(insert_process);
		insertMenu.add(insert_output);
		insertMenu.add(insert_decision);
		insertMenu.add(insert_adc);
		insertMenu.add(insert_end);
		insertMenu.add(insert_break);
		insertMenu.addSeparator();
		insertMenu.add(insert_sub);
		insertMenu.add(insert_sub_box);
		insertMenu.add(insert_return);
		
		// Selection menu
		selectionMenu = new JMenu("Selection");
		selectionMenu.setMnemonic(KeyEvent.VK_S);
		add(selectionMenu);
		
		selection_clearSelection = createMenuItem("Clear Selection", KeyEvent.VK_C, KeyEvent.VK_ESCAPE, 0);
		selection_invertSelection = createMenuItem("Invert Selection", KeyEvent.VK_I, KeyEvent.VK_F1, 0);
		selection_deleteLinks = createMenuItem("Delete Selected Links", KeyEvent.VK_D, KeyEvent.VK_F2, 0);
		selection_deleteSelected = createMenuItem("Delete Selected Boxes", KeyEvent.VK_D, KeyEvent.VK_DELETE, 0);
		
		selectionMenu.add(selection_clearSelection);
		selectionMenu.add(selection_invertSelection);
		selectionMenu.add(selection_deleteLinks);
		selectionMenu.add(selection_deleteSelected);
		
		// Output menu
		outputMenu = new JMenu("Output");
		outputMenu.setMnemonic(KeyEvent.VK_O);
		add(outputMenu);
		output_exportXML = createMenuItem("Export XML...", KeyEvent.VK_X, KeyEvent.VK_F5, 0);
		outputMenu.add(output_exportXML);
		
		// View menu
		viewMenu = new JMenu("View");
		viewMenu.setMnemonic(KeyEvent.VK_V);
		add(viewMenu);
		view_findStart = createMenuItem("Find Start Box", KeyEvent.VK_F, KeyEvent.VK_F, ActionEvent.CTRL_MASK);
		viewMenu.add(view_findStart);
		
		// Help menu
		helpMenu = new JMenu("Help");
		helpMenu.setMnemonic(KeyEvent.VK_H);
		add(helpMenu);
		
		help_showDiagram = createMenuItem("Show Circuit Diagram...", KeyEvent.VK_D, KeyEvent.VK_D, ActionEvent.CTRL_MASK);
		help_about = createMenuItem("About...", KeyEvent.VK_A);
		
		helpMenu.add(help_showDiagram);
		helpMenu.add(help_about);
	}
	
	private JMenuItem createMenuItem(String text, int hotKey, int shortcutButton, int mask) {
		JMenuItem item = new JMenuItem(text, hotKey);
		item.setAccelerator(KeyStroke.getKeyStroke(shortcutButton, mask));
		item.addActionListener(this);
		return item;
	}
	
	private JMenuItem createMenuItem(String text, int hotKey) {
		JMenuItem item = new JMenuItem(text, hotKey);
		item.addActionListener(this);
		return item;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// File menu
		if (e.getActionCommand() == file_new.getActionCommand()) {
			MainWindow.new_();
		} else if (e.getActionCommand() == file_open.getActionCommand()) {
			MainWindow.open();
		} else if (e.getActionCommand() == file_save.getActionCommand()) {
			MainWindow.save();
		} else if (e.getActionCommand() == file_saveAs.getActionCommand()) {
			MainWindow.saveAs();
		} else if (e.getActionCommand() == file_import.getActionCommand()) {
			MainWindow.import_();
		} else if (e.getActionCommand() == file_export.getActionCommand()) {
			MainWindow.export();
		} else if (e.getActionCommand() == file_exit.getActionCommand()) {
			MainWindow.getMainWindow().dispatchEvent(new WindowEvent(MainWindow.getMainWindow(), WindowEvent.WINDOW_CLOSING));
		}
		
		// Edit menu
		else if (e.getActionCommand() == edit_cut.getActionCommand()) {
			MainWindow.getTabbedPane().cutToClipboard();
		} else if (e.getActionCommand() == edit_copy.getActionCommand()) {
			MainWindow.getTabbedPane().copyToClipboard();
		} else if (e.getActionCommand() == edit_paste.getActionCommand()) {
			MainWindow.getTabbedPane().pasteFromClipboard();
		}
		
		// Insert menu
		else if (e.getActionCommand() == insert_input.getActionCommand()) {
			((WindowPanel) pane.getComponentAt(pane.getSelectedIndex())).addBox(new InputBox(), 0, 0);
			MainWindow.setSaved(false);
		} else if (e.getActionCommand() == insert_process.getActionCommand()) {
			((WindowPanel) pane.getComponentAt(pane.getSelectedIndex())).addBox(new ProcessBox(), 0, 0);
			MainWindow.setSaved(false);
		} else if (e.getActionCommand() == insert_output.getActionCommand()) {
			((WindowPanel) pane.getComponentAt(pane.getSelectedIndex())).addBox(new OutputBox(), 0, 0);
			MainWindow.setSaved(false);
		} else if (e.getActionCommand() == insert_decision.getActionCommand()) {
			((WindowPanel) pane.getComponentAt(pane.getSelectedIndex())).addBox(new DecisionBox(), 0, 0);
			MainWindow.setSaved(false);
		} else if (e.getActionCommand() == insert_end.getActionCommand()) {
			((WindowPanel) pane.getComponentAt(pane.getSelectedIndex())).addBox(new EndBox("Stop"), 0, 0);
			MainWindow.setSaved(false);
		} else if (e.getActionCommand() == insert_adc.getActionCommand()) {
			((WindowPanel) pane.getComponentAt(pane.getSelectedIndex())).addBox(new ADCBox(), 0, 0);
			MainWindow.setSaved(false);
		} else if (e.getActionCommand() == insert_sub_box.getActionCommand()) {
			((WindowPanel) pane.getComponentAt(pane.getSelectedIndex())).addBox(new SubroutineBox(), 0, 0);
			MainWindow.setSaved(false);
		} else if (e.getActionCommand() == insert_sub.getActionCommand()) {
			String subName;
			do {
				subName = JOptionPane.showInputDialog(parent, "Enter new subroutine name:", "Create New Subroutine", JOptionPane.PLAIN_MESSAGE);
				if (subName == null) return;
				else if (subName.contentEquals("")) JOptionPane.showMessageDialog(parent, "Please enter a subroutine name.");
				else if (!pane.availableSubroutineName(subName)) JOptionPane.showMessageDialog(parent, "Subroutine name already exists.");
			} while (subName.contentEquals("") || !pane.availableSubroutineName(subName));
			WindowPanel newSubroutine = new WindowPanel(parent, subName);
			pane.addSubroutine(newSubroutine);
			pane.setSelectedComponent(newSubroutine);
			MainWindow.setSaved(false);
		} else if (e.getActionCommand() == insert_break.getActionCommand()) {
			((WindowPanel) pane.getComponentAt(pane.getSelectedIndex())).addBox(new BreakBox(), 0, 0);
			MainWindow.setSaved(false);
		} else if (e.getActionCommand() == insert_return.getActionCommand()) {
			if (pane.getSelectedIndex() == 0) { JOptionPane.showMessageDialog(parent, "Cannot create return boxes in Main Program."); return; }
			((WindowPanel) pane.getComponentAt(pane.getSelectedIndex())).addBox(new ReturnBox(), 0, 0);
			MainWindow.setSaved(false);
		}
		
		// Selection  menu
		else if (e.getActionCommand() == selection_clearSelection.getActionCommand()) {
			for (Component c : ((WindowPanel) pane.getComponentAt(pane.getSelectedIndex())).getComponents()) if (c instanceof Box) {
				((Box) c).setSelected(false);
			}
		} else if (e.getActionCommand() == selection_invertSelection.getActionCommand()) {
			for (Component c : ((WindowPanel) pane.getComponentAt(pane.getSelectedIndex())).getComponents()) if (c instanceof Box) {
				if (((Box) c).isSelected()) ((Box) c).setSelected(false);
				else ((Box) c).setSelected(true);
			}
		} else if (e.getActionCommand() == selection_deleteLinks.getActionCommand()) {
			for (Component c : ((WindowPanel) pane.getComponentAt(pane.getSelectedIndex())).getComponents()) if (c instanceof Box) if (((Box) c).isSelected()) {
				if (((Box) c).getNextBox() != null) MainWindow.setSaved(false);
				if (c instanceof DecisionBox && ((DecisionBox) c).getYesBox() != null) MainWindow.setSaved(false);
				((Box) c).setNextBox(null);
				if (c instanceof DecisionBox) ((DecisionBox) c).setYesBox(null);
				for (Component c2 : ((WindowPanel) pane.getComponentAt(pane.getSelectedIndex())).getComponents()) if (c instanceof Box) {
					if (((Box) c2).getNextBox() == c) {
						((Box) c2).setNextBox(null);
						MainWindow.setSaved(false);
					}
					if (c2 instanceof DecisionBox && ((DecisionBox) c2).getYesBox() == c) {
						((DecisionBox) c2).setYesBox(null);
						MainWindow.setSaved(false);
					}
				}
			}
		} else if (e.getActionCommand() == selection_deleteSelected.getActionCommand()) {
			((WindowPanel) pane.getComponentAt(pane.getSelectedIndex())).deleteSelectedComponents();
		}
		
		// Output menu
		else if (e.getActionCommand() == output_exportXML.getActionCommand()) {
			MainWindow.exportXML();
		}
		
		// View menu
		else if (e.getActionCommand() == view_findStart.getActionCommand()) {
			Point distanceToMove = new Point(parent.getWidth()/2 - 62 - ((WindowPanel) pane.getComponentAt(pane.getSelectedIndex())).getStartBox().getLocation().x, 40 - ((WindowPanel) pane.getComponentAt(pane.getSelectedIndex())).getStartBox().getLocation().y);
			for (Component c : ((WindowPanel) pane.getComponentAt(pane.getSelectedIndex())).getComponents()) if (c instanceof Box) {
				c.setLocation(c.getLocation().x + distanceToMove.x, c.getLocation().y + distanceToMove.y);
			}
		}
		
		// Help menu
		else if (e.getActionCommand() == help_showDiagram.getActionCommand()) {
			showCircuitDiagram();
		} else if (e.getActionCommand() == help_about.getActionCommand()) {
			JOptionPane.showMessageDialog(parent, "TriCrose Flow Diagrams v1.0\n© 2015", "About...", JOptionPane.PLAIN_MESSAGE);
		}
	}
	
	private static void showCircuitDiagram() {
		final JDialog circuitWindow = new JDialog(MainWindow.getMainWindow(), "Circuit Diagram");
		circuitWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		circuitWindow.setSize(530, 345);
		circuitWindow.setResizable(false);
		circuitWindow.setLocationRelativeTo(MainWindow.getMainWindow());
		circuitWindow.add(new JLabel(new ImageIcon("res/circuit.png")));
		circuitWindow.setVisible(true);
		circuitWindow.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) circuitWindow.dispose();
			}
			public void keyReleased(KeyEvent e) {}
			public void keyTyped(KeyEvent e) {}
		});
	}
}
