package boxes;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import window.MainWindow;
import window.WindowPanel;

public class SubroutineBox extends Box {
	private static final long serialVersionUID = 1L;

	private String subroutineName;
	private String comment;
	
	public String getSubroutineName() {
		return subroutineName;
	}
	
	public void setSubroutineName(String subroutineName) {
		this.subroutineName = subroutineName;
		update();
	}
	
	public String getComment() {
		return comment;
	}
	
	public SubroutineBox() {
		super();
		subroutineName = "";
		comment = "";
		update();
	}
	
	public void checkValid() {
		if (subroutineName == "") return;
		if (MainWindow.getTabbedPane().availableSubroutineName(subroutineName)) subroutineName = "";
		update();
	}
	
	public void openEditDialog() {
		final JDialog editDialog = new JDialog(MainWindow.getMainWindow(), "Edit Subroutine Box", true);
		Box.installEscapeOnCloseOperation(editDialog);
		editDialog.setSize(254, 213);
		editDialog.setLayout(null);
		editDialog.setLocationRelativeTo(MainWindow.getMainWindow());
		editDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		editDialog.setResizable(false);
		
		JLabel labelSubroutine = new JLabel("Subroutine: ");
		labelSubroutine.setSize(labelSubroutine.getPreferredSize());
		labelSubroutine.setLocation(10, 20);
		editDialog.add(labelSubroutine);
		
		final JComboBox<String> comboBox = new JComboBox<String>(MainWindow.getTabbedPane().getSubroutineNamesExceptSelected());
		comboBox.setLocation(88, 16);
		comboBox.setPrototypeDisplayValue("------------------------------");
		comboBox.setSize(comboBox.getPreferredSize().width - 3, comboBox.getPreferredSize().height);
		if (subroutineName != "") comboBox.setSelectedItem(subroutineName);
		editDialog.add(comboBox);
		
		JLabel labelComment = new JLabel("Comment: ");
		labelComment.setSize(labelComment.getPreferredSize());
		labelComment.setLocation(17, 55);
		editDialog.add(labelComment);
		
		final JTextArea commentTextArea = new JTextArea(5, 13);
		commentTextArea.setLineWrap(true);
		commentTextArea.setWrapStyleWord(true);
		commentTextArea.setText(comment);
		JScrollPane scrollPane = new JScrollPane(commentTextArea);
		scrollPane.setSize(scrollPane.getPreferredSize());
		scrollPane.setLocation(88, 55);
		editDialog.add(scrollPane);
		
		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String newSubroutineName = (comboBox.getSelectedItem() == null) ? "" : comboBox.getSelectedItem().toString();
				if (((WindowPanel) getParent()).getSubroutineName().contentEquals(newSubroutineName)) {
					JOptionPane.showMessageDialog(editDialog, "Subroutine cannot call itself.");
					return;
				}
				
				comment = commentTextArea.getText();
				subroutineName = newSubroutineName;
				editDialog.dispose();
			}
		});
		okButton.setSize(okButton.getPreferredSize());
		okButton.setLocation(60, 150);
		editDialog.add(okButton);
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editDialog.dispose();
			}
		});
		cancelButton.setSize(cancelButton.getPreferredSize());
		cancelButton.setLocation(130, 150);
		editDialog.add(cancelButton);
		
		editDialog.setVisible(true);
		update();
	}
	
	public void update() {
		setText((subroutineName.contentEquals("")) ? "..." : subroutineName + "()" );
		setSize(getPreferredSize().width + 40, getPreferredSize().height + 15);
	}
}