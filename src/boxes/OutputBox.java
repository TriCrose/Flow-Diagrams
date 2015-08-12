package boxes;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import window.MainWindow;

public class OutputBox extends Box {
	private static final long serialVersionUID = 1L;
	
	private int register;
	private String comment;

	public int getRegister() {
		return register;
	}

	public String getComment() {
		return comment;
	}

	public OutputBox() {
		super();
		register = 0;
		comment = "";
		update();
	}
	
	public void openEditDialog() {
		final JDialog editDialog = new JDialog(MainWindow.getMainWindow(), "Edit Output Box", true);
		Box.installEscapeOnCloseOperation(editDialog);
		editDialog.setSize(250, 213);
		editDialog.setLayout(null);
		editDialog.setLocationRelativeTo(MainWindow.getMainWindow());
		editDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		editDialog.setResizable(false);
		
		JLabel labelRegister = new JLabel("Register: ");
		labelRegister.setSize(labelRegister.getPreferredSize());
		labelRegister.setLocation(19, 20);
		editDialog.add(labelRegister);
		
		final JComboBox<String> comboBox = new JComboBox<String>(new String[]{"S0", "S1", "S2", "S3", "S4", "S5", "S6", "S7"});
		comboBox.setSelectedIndex(register);
		comboBox.setLocation(83, 16);
		comboBox.setPrototypeDisplayValue("------------------------------");
		comboBox.setSize(comboBox.getPreferredSize().width - 3, comboBox.getPreferredSize().height);
		editDialog.add(comboBox);
		
		JLabel labelComment = new JLabel("Comment: ");
		labelComment.setSize(labelComment.getPreferredSize());
		labelComment.setLocation(12, 55);
		editDialog.add(labelComment);
		
		final JTextArea commentTextArea = new JTextArea(5, 13);
		commentTextArea.setLineWrap(true);
		commentTextArea.setWrapStyleWord(true);
		commentTextArea.setText(comment);
		JScrollPane scrollPane = new JScrollPane(commentTextArea);
		scrollPane.setSize(scrollPane.getPreferredSize());
		scrollPane.setLocation(83, 55);
		editDialog.add(scrollPane);
		
		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				comment = commentTextArea.getText();
				register = comboBox.getSelectedIndex();
				editDialog.dispose();
			}
		});
		okButton.setSize(okButton.getPreferredSize());
		okButton.setLocation(55, 150);
		editDialog.add(okButton);
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editDialog.dispose();
			}
		});
		cancelButton.setSize(cancelButton.getPreferredSize());
		cancelButton.setLocation(125, 150);
		editDialog.add(cancelButton);
		
		editDialog.setVisible(true);
		update();
	}
	
	public void update() {
		setText("Output S" + register);
		setSize(getPreferredSize().width + 40, getPreferredSize().height + 15);
	}
}