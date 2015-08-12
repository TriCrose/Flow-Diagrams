package boxes;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import window.MainWindow;

public class DecisionBox extends Box {
	private static final long serialVersionUID = 1L;
	
	// The nextBox refers to the no box, and the yesBox refers to the yes box
	private Box yesBox;
	
	public Box getYesBox() {
		return yesBox;
	}

	public void setYesBox(Box yesBox) {
		this.yesBox = yesBox;
	}
	
	private boolean equalsTest;
	private int register;
	private int _byte;
	private String comment;

	public boolean isEqualsTest() {
		return equalsTest;
	}
	
	public int getRegister() {
		return register;
	}
	
	public int getByte() {
		return _byte;
	}

	public String getComment() {
		return comment;
	}
	
	public DecisionBox() {
		super();
		equalsTest = true;
		register = 0;
		_byte = 0;
		comment = "";
		update();
	}
	
	public void openEditDialog() {
		final JDialog editDialog = new JDialog(MainWindow.getMainWindow(), "Edit Decision Box", true);
		Box.installEscapeOnCloseOperation(editDialog);
		editDialog.setSize(250, 295);
		editDialog.setLayout(null);
		editDialog.setLocationRelativeTo(MainWindow.getMainWindow());
		editDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		editDialog.setResizable(false);
		
		JLabel labelTestType = new JLabel("Test Type:");
		labelTestType.setSize(labelTestType.getPreferredSize());
		labelTestType.setLocation(19, 17);
		editDialog.add(labelTestType);
		
		final JRadioButton buttonEqualsTest = new JRadioButton("Register = Byte?");
		buttonEqualsTest.setSize(buttonEqualsTest.getPreferredSize());
		buttonEqualsTest.setLocation(40, 37);
		editDialog.add(buttonEqualsTest);
		final JRadioButton buttonGreaterTest = new JRadioButton("Register > Byte?");
		buttonGreaterTest.setSize(buttonGreaterTest.getPreferredSize());
		buttonGreaterTest.setLocation(40, 60);
		editDialog.add(buttonGreaterTest);
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(buttonEqualsTest);
		buttonGroup.add(buttonGreaterTest);
		if (equalsTest) buttonEqualsTest.setSelected(true);
		else buttonGreaterTest.setSelected(true);
		
		JLabel labelRegister = new JLabel("Register: ");
		labelRegister.setSize(labelRegister.getPreferredSize());
		labelRegister.setLocation(19, 100);
		editDialog.add(labelRegister);
		
		final JComboBox<String> comboBox = new JComboBox<String>(new String[]{"S0", "S1", "S2", "S3", "S4", "S5", "S6", "S7"});
		comboBox.setSelectedIndex(register);
		comboBox.setLocation(83, 96);
		comboBox.setSize(comboBox.getPreferredSize().width + 3, comboBox.getPreferredSize().height);
		editDialog.add(comboBox);
		
		JLabel labelByte = new JLabel("Byte:   0x");
		labelByte.setSize(labelByte.getPreferredSize());
		labelByte.setLocation(153, 100);
		editDialog.add(labelByte);
		
		String hexNumber = Integer.toHexString(_byte).toUpperCase();
		if (hexNumber.length() == 1) hexNumber = new StringBuilder(hexNumber).insert(0, "0").toString();
		final JTextField byteField = new JTextField(hexNumber);
		byteField.setSize(byteField.getPreferredSize().width + 6, byteField.getPreferredSize().height);
		byteField.setLocation(205, 98);
		editDialog.add(byteField);
		
		JLabel labelComment = new JLabel("Comment: ");
		labelComment.setSize(labelComment.getPreferredSize());
		labelComment.setLocation(12, 135);
		editDialog.add(labelComment);
		
		final JTextArea commentTextArea = new JTextArea(5, 13);
		commentTextArea.setLineWrap(true);
		commentTextArea.setWrapStyleWord(true);
		commentTextArea.setText(comment);
		JScrollPane scrollPane = new JScrollPane(commentTextArea);
		scrollPane.setSize(scrollPane.getPreferredSize());
		scrollPane.setLocation(83, 135);
		editDialog.add(scrollPane);
		
		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int newByte = Box.checkHexByte(byteField.getText());
				if (newByte == -1) {
					JOptionPane.showMessageDialog(editDialog, "Byte must be a hexadecimal number between 0x00 and 0xFF.");
					return;
				}
				
				equalsTest = buttonEqualsTest.isSelected();
				comment = commentTextArea.getText();
				register = comboBox.getSelectedIndex();
				_byte = newByte;
				editDialog.dispose();
			}
		});
		okButton.setSize(okButton.getPreferredSize());
		okButton.setLocation(55, 230);
		editDialog.add(okButton);
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editDialog.dispose();
			}
		});
		cancelButton.setSize(cancelButton.getPreferredSize());
		cancelButton.setLocation(125, 230);
		editDialog.add(cancelButton);
		
		editDialog.setVisible(true);
		update();
	}
	
	public void update() {
		String hexNumber = Integer.toHexString(_byte).toUpperCase();
		if (hexNumber.length() == 1) hexNumber = new StringBuilder(hexNumber).insert(0, "0").toString();
		setText("S" + register + " " + (equalsTest ? "=" : ">") + " 0x" + hexNumber + "?");
		setSize(getPreferredSize().width + 75, getPreferredSize().height + 50);
	}
}