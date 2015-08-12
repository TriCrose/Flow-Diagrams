package window;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.Timer;

import fileIO.*;

public class MainWindow extends JFrame implements ActionListener {
	private static final long serialVersionUID = 7235299242866423625L;
	
	public static final int WIDTH = 1000;
	public static final int HEIGHT = 600;
	
	private static MainWindow mainWindow;
	private static TabbedPane tabbedPane;
	private static Timer timer;
	
	private static boolean saved = true;
	private static String fileName = null;

	public static boolean isSaved() {
		return saved;
	}

	public static void setSaved(boolean saved) {
		MainWindow.saved = saved;
		mainWindow.setTitle((saved ? "" : "*") + (fileName == null ? "Untitled" : (new File(fileName)).getName()) + " - TriCrose Flow Diagrams");
	}
	
	public static String getFileName() {
		return fileName;
	}

	public static void setFileName(String fileName) {
		MainWindow.fileName = fileName;
	}

	public static MainWindow getMainWindow() {
		return mainWindow;
	}
	
	public static TabbedPane getTabbedPane() {
		return tabbedPane;
	}
	
	public static void new_() {
		if (!isSaved()) {
			int result = JOptionPane.showConfirmDialog(mainWindow, "Save changes?", "Save", JOptionPane.YES_NO_CANCEL_OPTION);
			if (result == JOptionPane.YES_OPTION) { if (!save()) return; }
			else if (result == JOptionPane.CANCEL_OPTION || result == JOptionPane.CLOSED_OPTION) return; 
		}
		
		mainWindow.remove(tabbedPane);
		WindowPanel mainProgramPanel = new WindowPanel(mainWindow, "Main Program");
		tabbedPane = new TabbedPane(mainProgramPanel);
		mainWindow.add(tabbedPane);
		((MenuBar) mainWindow.getJMenuBar()).setPane(tabbedPane);
		mainWindow.revalidate();
		mainWindow.repaint();
		
		fileName = null;
		setSaved(true);
	}
	
	public static void open() {
		String newFileName = FileDialogs.openFileDialog();
		if (newFileName == null) return;
		
		if (!isSaved()) {
			int result = JOptionPane.showConfirmDialog(mainWindow, "Save changes?", "Save", JOptionPane.YES_NO_CANCEL_OPTION);
			if (result == JOptionPane.YES_OPTION) { if (!save()) return; }
			else if (result == JOptionPane.CANCEL_OPTION || result == JOptionPane.CLOSED_OPTION) return; 
		}
		
		TabbedPane newTabbedPane = (TabbedPane) ObjectSerializer.deserialize(newFileName);
		if (newTabbedPane == null) return;
		fileName = newFileName;
		
		mainWindow.remove(tabbedPane);
		tabbedPane = newTabbedPane;
		mainWindow.add(tabbedPane);
		((MenuBar) mainWindow.getJMenuBar()).setPane(tabbedPane);
		
		for (int i = 1; i < tabbedPane.getTabCount(); i++) {
			if (tabbedPane.getTabComponentAt(i) == null) {
				JLabel tabTitle = new JLabel(((WindowPanel) tabbedPane.getComponentAt(i)).getSubroutineName());
				tabTitle.addMouseListener(new TabMouseListener(((WindowPanel) tabbedPane.getComponentAt(i)), tabbedPane));
				tabbedPane.setTabComponentAt(i, tabTitle);
			} else {
				for (MouseListener l : tabbedPane.getTabComponentAt(i).getMouseListeners()) tabbedPane.getTabComponentAt(i).removeMouseListener(l);
				tabbedPane.getTabComponentAt(i).addMouseListener(new TabMouseListener(((WindowPanel) tabbedPane.getComponentAt(i)), tabbedPane));
			}
		}
		
		mainWindow.revalidate();
		mainWindow.repaint();
		setSaved(true);
	}
	
	public static boolean save() {
		if (fileName == null) {
			return saveAs();
		} else {
			ObjectSerializer.serialize(fileName, tabbedPane);
			setSaved(true);
			return true;
		}
	}
	
	public static boolean saveAs() {
		String newFileName = FileDialogs.saveFileDialog();
		if (newFileName == null) return false;
		ObjectSerializer.serialize(newFileName, tabbedPane);
		fileName = newFileName;
		setSaved(true);
		return true;
	}
	
	public static void import_() {
		String subFileName = FileDialogs.importFileDialog();
		if (subFileName == null) return;
		WindowPanel newSubroutine = (WindowPanel) ObjectSerializer.deserialize(subFileName);
		if (newSubroutine == null) return;
		
		String subName = newSubroutine.getSubroutineName();
		if (!tabbedPane.availableSubroutineName(subName)) do {
			subName = JOptionPane.showInputDialog(mainWindow, "Subroutine name already taken, enter new subroutine name:", "Rename Subroutine", JOptionPane.PLAIN_MESSAGE);
			if (subName == null) return;
			else if (subName.contentEquals("")) JOptionPane.showMessageDialog(mainWindow, "Please enter a subroutine name.");
			else if (!tabbedPane.availableSubroutineName(subName)) JOptionPane.showMessageDialog(mainWindow, "Subroutine name already exists.");
		} while (subName.contentEquals("") || !tabbedPane.availableSubroutineName(subName));
		
		newSubroutine.setSubroutineName(subName);
		tabbedPane.addSubroutine(newSubroutine);
		tabbedPane.setSelectedComponent(newSubroutine);
		MainWindow.setSaved(false);
	}
	
	public static void export() {
		if (tabbedPane.getSelectedIndex() == 0) {
			JOptionPane.showMessageDialog(mainWindow, "Cannot export main program as subroutine.", "Export Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		String subFileName = FileDialogs.exportFileDialog();
		if (subFileName == null) return;
		ObjectSerializer.serialize(subFileName, ((WindowPanel) tabbedPane.getComponentAt(tabbedPane.getSelectedIndex())));
	}
	
	public static void exportXML() {
		String xmlFileName = FileDialogs.exportXMLFileDialog();
		if (xmlFileName == null) return;
		try {
			PrintWriter writer = new PrintWriter(xmlFileName, "UTF-8");
			writer.print(XMLGenerator.generate());
			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(mainWindow, "Error exporting XML, check stack trace.", "XML Export Error", JOptionPane.ERROR_MESSAGE);
		}
		
	}
	
	public MainWindow() {
		// Create window
		super("Untitled - TriCrose Flow Diagrams");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setSize(WIDTH, HEIGHT);
		setIconImage(new ImageIcon("res/icon.png").getImage());
		setLocationRelativeTo(null);
		
		// Window panel and tabs
		WindowPanel mainProgramPanel = new WindowPanel(this, "Main Program");
		tabbedPane = new TabbedPane(mainProgramPanel);
		add(tabbedPane);
		
		// Menu
		MenuBar menuBar = new MenuBar(this, tabbedPane);
		setJMenuBar(menuBar);
		
		// Show window
		setVisible(true);
	}
	
	public static void main(String args[]) {
		mainWindow = new MainWindow();
		mainWindow.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if (!isSaved()) {
					int result = JOptionPane.showConfirmDialog(mainWindow, "Save changes?", "Save", JOptionPane.YES_NO_CANCEL_OPTION);
					if (result == JOptionPane.YES_OPTION) { if (!save()) return; }
					else if (result == JOptionPane.CANCEL_OPTION || result == JOptionPane.CLOSED_OPTION) return;
				}
				System.exit(0);
			}
		});
		timer = new Timer(17, mainWindow);
		timer.start();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		mainWindow.setTitle((saved ? "" : "*") + (fileName == null ? "Untitled" : (new File(fileName)).getName()) + " - TriCrose Flow Diagrams");
		((WindowPanel) tabbedPane.getComponentAt(0)).deleteReturnBoxes();
		for (int i = 0; i < tabbedPane.getTabCount(); i++) {
			((WindowPanel) tabbedPane.getComponentAt(i)).tick();
		}
	}
}