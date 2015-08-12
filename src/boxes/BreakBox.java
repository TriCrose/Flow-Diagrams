package boxes;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import window.MainWindow;

public class BreakBox extends Box {
	private static final long serialVersionUID = 1L;
	
	private int breakNumber;
	private String comment;

	public int getBreakNumber() {
		return breakNumber;
	}

	public String getComment() {
		return comment;
	}

	public BreakBox() {
		super();
		breakNumber = 0;
		comment = "";
		update();
	}
	
	public void openEditDialog() {
		final JDialog editDialog = new JDialog(MainWindow.getMainWindow(), "Edit Breakpoint Box", true);
		Box.installEscapeOnCloseOperation(editDialog);
		editDialog.setSize(290, 213);
		editDialog.setLayout(null);
		editDialog.setLocationRelativeTo(MainWindow.getMainWindow());
		editDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		editDialog.setResizable(false);
		
		JLabel labelRegister = new JLabel("Break Number:    0x");
		labelRegister.setSize(labelRegister.getPreferredSize());
		labelRegister.setLocation(16, 20);
		editDialog.add(labelRegister);
		
		String hexNumber = Integer.toHexString(breakNumber).toUpperCase();
		if (hexNumber.length() == 1) hexNumber = new StringBuilder(hexNumber).insert(0, "0").toString();
		final JTextField breakNumberField = new JTextField(hexNumber);
		breakNumberField.setSize(breakNumberField.getPreferredSize().width + 6, breakNumberField.getPreferredSize().height);
		breakNumberField.setLocation(128, 18);
		editDialog.add(breakNumberField);
		
		JLabel labelComment = new JLabel("Comment: ");
		labelComment.setSize(labelComment.getPreferredSize());
		labelComment.setLocation(42, 55);
		editDialog.add(labelComment);
		
		final JTextArea commentTextArea = new JTextArea(5, 13);
		commentTextArea.setLineWrap(true);
		commentTextArea.setWrapStyleWord(true);
		commentTextArea.setText(comment);
		JScrollPane scrollPane = new JScrollPane(commentTextArea);
		scrollPane.setSize(scrollPane.getPreferredSize());
		scrollPane.setLocation(113, 55);
		editDialog.add(scrollPane);
		
		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int newBreakNumber = Box.checkHexByte(breakNumberField.getText());
				if (newBreakNumber == -1) {
					JOptionPane.showMessageDialog(editDialog, "Break number must be a hexadecimal number between 0x00 and 0xFF.");
					return;
				}
				
				breakNumber = newBreakNumber;
				comment = commentTextArea.getText();
				editDialog.dispose();
			}
		});
		okButton.setSize(okButton.getPreferredSize());
		okButton.setLocation(73, 150);
		editDialog.add(okButton);
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editDialog.dispose();
			}
		});
		cancelButton.setSize(cancelButton.getPreferredSize());
		cancelButton.setLocation(143, 150);
		editDialog.add(cancelButton);
		
		editDialog.setVisible(true);
		update();
	}
	
	public void update() {
		String hexNumber = Integer.toHexString(breakNumber).toUpperCase();
		if (hexNumber.length() == 1) hexNumber = new StringBuilder(hexNumber).insert(0, "0").toString();
		setText("Break 0x" + hexNumber);
		setSize(getPreferredSize().width + 40, getPreferredSize().height + 15);
	}
}