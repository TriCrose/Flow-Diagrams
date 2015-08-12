package fileIO;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import window.MainWindow;
import window.WindowPanel;

public class FileDialogs {
	public static String openFileDialog() {
		JFileChooser fc = new JFileChooser();
		fc.setFileFilter(new FileNameExtensionFilter("TFD Files (.tfd)", "tfd"));
		fc.setAcceptAllFileFilterUsed(false);
		if (fc.showOpenDialog(MainWindow.getMainWindow()) == JFileChooser.APPROVE_OPTION) {
			return fc.getSelectedFile().getAbsolutePath();
		} else return null;
	}
	
	public static String saveFileDialog() {
		JFileChooser fc = new JFileChooser() {
			private static final long serialVersionUID = -931957095842376614L;
			public void approveSelection() {
				File file = getSelectedFile();
				if (file.exists()) {
					int result = JOptionPane.showConfirmDialog(this, "Overwrite existing file?", "Confirm Overwrite", JOptionPane.YES_NO_CANCEL_OPTION);
					if (result == JOptionPane.NO_OPTION || result == JOptionPane.CLOSED_OPTION) return;
					else if (result == JOptionPane.CANCEL_OPTION) {
						cancelSelection();
						return;
					}
				}
				super.approveSelection();
			}
		};
		fc.setFileFilter(new FileNameExtensionFilter("TFD Files (.tfd)", "tfd"));
		fc.setAcceptAllFileFilterUsed(false);
		if (fc.showSaveDialog(MainWindow.getMainWindow()) == JFileChooser.APPROVE_OPTION) {
			String fileName = fc.getSelectedFile().getAbsolutePath();
			if (fileName.endsWith(".tfd")) return fileName;
			else return fileName + ".tfd";
		} else return null;
	}
	
	public static String importFileDialog() {
		JFileChooser fc = new JFileChooser();
		fc.setDialogTitle("Import Subroutine");
		fc.setFileFilter(new FileNameExtensionFilter("TSR Files (.tsr)", "tsr"));
		fc.setAcceptAllFileFilterUsed(false);
		if (fc.showOpenDialog(MainWindow.getMainWindow()) == JFileChooser.APPROVE_OPTION) {
			return fc.getSelectedFile().getAbsolutePath();
		} else return null;
	}
	
	public static String exportFileDialog() {
		JFileChooser fc = new JFileChooser() {
			private static final long serialVersionUID = -931957095842376614L;
			public void approveSelection() {
				File file = getSelectedFile();
				if (file.exists()) {
					int result = JOptionPane.showConfirmDialog(this, "Overwrite existing file?", "Confirm Overwrite", JOptionPane.YES_NO_CANCEL_OPTION);
					if (result == JOptionPane.NO_OPTION || result == JOptionPane.CLOSED_OPTION) return;
					else if (result == JOptionPane.CANCEL_OPTION) {
						cancelSelection();
						return;
					}
				}
				super.approveSelection();
			}
		};
		fc.setDialogTitle("Export Subroutine \"" + ((WindowPanel) MainWindow.getTabbedPane().getComponentAt(MainWindow.getTabbedPane().getSelectedIndex())).getSubroutineName() + "\"");
		fc.setFileFilter(new FileNameExtensionFilter("TSR Files (.tsr)", "tsr"));
		fc.setAcceptAllFileFilterUsed(false);
		if (fc.showSaveDialog(MainWindow.getMainWindow()) == JFileChooser.APPROVE_OPTION) {
			String fileName = fc.getSelectedFile().getAbsolutePath();
			if (fileName.endsWith(".tsr")) return fileName;
			else return fileName + ".tsr";
		} else return null;
	}
	
	public static String exportXMLFileDialog() {
		JFileChooser fc = new JFileChooser() {
			private static final long serialVersionUID = -931957095842376614L;
			public void approveSelection() {
				File file = getSelectedFile();
				if (file.exists()) {
					int result = JOptionPane.showConfirmDialog(this, "Overwrite existing file?", "Confirm Overwrite", JOptionPane.YES_NO_CANCEL_OPTION);
					if (result == JOptionPane.NO_OPTION || result == JOptionPane.CLOSED_OPTION) return;
					else if (result == JOptionPane.CANCEL_OPTION) {
						cancelSelection();
						return;
					}
				}
				super.approveSelection();
			}
		};
		fc.setDialogTitle("Export to XML");
		fc.setFileFilter(new FileNameExtensionFilter("XML Files (.xml)", "xml"));
		fc.setAcceptAllFileFilterUsed(false);
		if (fc.showSaveDialog(MainWindow.getMainWindow()) == JFileChooser.APPROVE_OPTION) {
			String fileName = fc.getSelectedFile().getAbsolutePath();
			if (fileName.endsWith(".xml")) return fileName;
			else return fileName + ".xml";
		} else return null;
	}
}
