package boxes;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.AbstractButton;
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

public class ProcessBox extends Box {
	private static final long serialVersionUID = 1L;
	
	private int processTypeIndex;
	private int registerN;
	private int registerM;
	private int _byte;
	private String comment;
	
	public int getProcessTypeIndex() {
		return processTypeIndex;
	}

	public int getRegisterN() {
		return registerN;
	}
	
	public int getRegisterM() {
		return registerM;
	}
	
	public int getByte() {
		return _byte;
	}

	public String getComment() {
		return comment;
	}
	
	public ProcessBox() {
		super();
		processTypeIndex = 0;
		registerN = 0;
		registerM = 0;
		_byte = 0;
		comment = "";
		update();
	}
	
	public void openEditDialog() {
		final JDialog editDialog = new JDialog(MainWindow.getMainWindow(), "Edit Process Box", true);
		Box.installEscapeOnCloseOperation(editDialog);
		editDialog.setSize(450, 290);
		editDialog.setLayout(null);
		editDialog.setLocationRelativeTo(MainWindow.getMainWindow());
		editDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		editDialog.setResizable(false);
		
		JLabel labelProcessType = new JLabel("Process Type:");
		labelProcessType.setSize(labelProcessType.getPreferredSize());
		labelProcessType.setLocation(20, 20);
		editDialog.add(labelProcessType);
		
		final JRadioButton snEqualsByteProcess = new JRadioButton("Sn = Byte");
		snEqualsByteProcess.setSize(snEqualsByteProcess.getPreferredSize());
		snEqualsByteProcess.setLocation(40, 36);
		editDialog.add(snEqualsByteProcess);
		if (processTypeIndex == 0) snEqualsByteProcess.setSelected(true);
		final JRadioButton snEqualsSmProcess = new JRadioButton("Sn = Sm");
		snEqualsSmProcess.setSize(snEqualsSmProcess.getPreferredSize());
		snEqualsSmProcess.setLocation(40, 57);
		editDialog.add(snEqualsSmProcess);
		if (processTypeIndex == 1) snEqualsSmProcess.setSelected(true);
		final JRadioButton snPlusEqualsByteProcess = new JRadioButton("Sn += Byte");
		snPlusEqualsByteProcess.setSize(snPlusEqualsByteProcess.getPreferredSize());
		snPlusEqualsByteProcess.setLocation(40, 78);
		editDialog.add(snPlusEqualsByteProcess);
		if (processTypeIndex == 2) snPlusEqualsByteProcess.setSelected(true);
		final JRadioButton snMinusEqualsByteProcess = new JRadioButton("Sn -= Byte");
		snMinusEqualsByteProcess.setSize(snMinusEqualsByteProcess.getPreferredSize());
		snMinusEqualsByteProcess.setLocation(40, 99);
		editDialog.add(snMinusEqualsByteProcess);
		if (processTypeIndex == 3) snMinusEqualsByteProcess.setSelected(true);
		final JRadioButton waitProcess = new JRadioButton("Wait (<Byte> ms)");
		waitProcess.setSize(waitProcess.getPreferredSize());
		waitProcess.setLocation(40, 120);
		editDialog.add(waitProcess);
		if (processTypeIndex == 4) waitProcess.setSelected(true);
		
		final JRadioButton snAndEqualsByteProcess = new JRadioButton("Sn = Sn AND Byte");
		snAndEqualsByteProcess.setSize(snAndEqualsByteProcess.getPreferredSize());
		snAndEqualsByteProcess.setLocation(40, 141);
		editDialog.add(snAndEqualsByteProcess);
		if (processTypeIndex == 5) snAndEqualsByteProcess.setSelected(true);
		final JRadioButton snXorEqualsByteProcess = new JRadioButton("Sn = Sn XOR Byte");
		snXorEqualsByteProcess.setSize(snXorEqualsByteProcess.getPreferredSize());
		snXorEqualsByteProcess.setLocation(40, 162);
		editDialog.add(snXorEqualsByteProcess);
		if (processTypeIndex == 6) snXorEqualsByteProcess.setSelected(true);
		final JRadioButton snOrEqualsByteProcess = new JRadioButton("Sn = Sn OR Byte");
		snOrEqualsByteProcess.setSize(snOrEqualsByteProcess.getPreferredSize());
		snOrEqualsByteProcess.setLocation(40, 183);
		editDialog.add(snOrEqualsByteProcess);
		if (processTypeIndex == 7) snOrEqualsByteProcess.setSelected(true);
		final JRadioButton snPlusEqualsSmProcess = new JRadioButton("Sn += Sm");
		snPlusEqualsSmProcess.setSize(snPlusEqualsSmProcess.getPreferredSize());
		snPlusEqualsSmProcess.setLocation(40, 204);
		editDialog.add(snPlusEqualsSmProcess);
		if (processTypeIndex == 8) snPlusEqualsSmProcess.setSelected(true);
		final JRadioButton snMinusEqualsSmProcess = new JRadioButton("Sn -= Sm");
		snMinusEqualsSmProcess.setSize(snMinusEqualsSmProcess.getPreferredSize());
		snMinusEqualsSmProcess.setLocation(40, 225);
		editDialog.add(snMinusEqualsSmProcess);
		if (processTypeIndex == 9) snMinusEqualsSmProcess.setSelected(true);
		
		final ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(snEqualsByteProcess);
		buttonGroup.add(snEqualsSmProcess);
		buttonGroup.add(snPlusEqualsByteProcess);
		buttonGroup.add(snMinusEqualsByteProcess);
		buttonGroup.add(waitProcess);
		buttonGroup.add(snAndEqualsByteProcess);
		buttonGroup.add(snXorEqualsByteProcess);
		buttonGroup.add(snOrEqualsByteProcess);
		buttonGroup.add(snPlusEqualsSmProcess);
		buttonGroup.add(snMinusEqualsSmProcess);
		
		// ------------------
		
		int xOffset = 200;
		int yOffset = 0;
		
		JLabel labelRegisterN = new JLabel("Register Sn: ");
		labelRegisterN.setSize(labelRegisterN.getPreferredSize());
		labelRegisterN.setLocation(xOffset + 1, yOffset + 20);
		editDialog.add(labelRegisterN);
		
		final JComboBox<String> comboBoxRegisterN = new JComboBox<String>(new String[]{"S0", "S1", "S2", "S3", "S4", "S5", "S6", "S7"});
		comboBoxRegisterN.setSelectedIndex(registerN);
		comboBoxRegisterN.setLocation(xOffset + 83, yOffset + 16);
		comboBoxRegisterN.setPrototypeDisplayValue("------------------------------");
		comboBoxRegisterN.setSize(comboBoxRegisterN.getPreferredSize().width - 3, comboBoxRegisterN.getPreferredSize().height);
		editDialog.add(comboBoxRegisterN);
		
		JLabel labelRegisterM = new JLabel("Register Sm: ");
		labelRegisterM.setSize(labelRegisterM.getPreferredSize());
		labelRegisterM.setLocation(xOffset - 3, yOffset + 55);
		editDialog.add(labelRegisterM);
		
		final JComboBox<String> comboBoxRegisterM = new JComboBox<String>(new String[]{"S0", "S1", "S2", "S3", "S4", "S5", "S6", "S7"});
		comboBoxRegisterM.setSelectedIndex(registerM);
		comboBoxRegisterM.setLocation(xOffset + 83, yOffset + 51);
		comboBoxRegisterM.setPrototypeDisplayValue("------------------------------");
		comboBoxRegisterM.setSize(comboBoxRegisterM.getPreferredSize().width - 3, comboBoxRegisterM.getPreferredSize().height);
		editDialog.add(comboBoxRegisterM);
		
		JLabel labelByte = new JLabel("Byte:    0x");
		labelByte.setSize(labelByte.getPreferredSize());
		labelByte.setLocation(xOffset + 42, yOffset + 90);
		editDialog.add(labelByte);
		
		String hexNumber = Integer.toHexString(_byte).toUpperCase();
		if (hexNumber.length() == 1) hexNumber = new StringBuilder(hexNumber).insert(0, "0").toString();
		final JTextField byteField = new JTextField(hexNumber);
		byteField.setSize(byteField.getPreferredSize().width + 6, byteField.getPreferredSize().height);
		byteField.setLocation(xOffset + 97, yOffset + 88);
		editDialog.add(byteField);
		
		JLabel labelComment = new JLabel("Comment: ");
		labelComment.setSize(labelComment.getPreferredSize());
		labelComment.setLocation(xOffset + 12, yOffset + 125);
		editDialog.add(labelComment);
		
		final JTextArea commentTextArea = new JTextArea(5, 13);
		commentTextArea.setLineWrap(true);
		commentTextArea.setWrapStyleWord(true);
		commentTextArea.setText(comment);
		JScrollPane scrollPane = new JScrollPane(commentTextArea);
		scrollPane.setSize(scrollPane.getPreferredSize());
		scrollPane.setLocation(xOffset + 83, yOffset + 125);
		editDialog.add(scrollPane);
		
		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int newByte = Box.checkHexByte(byteField.getText());
				if (newByte == -1) {
					JOptionPane.showMessageDialog(editDialog, "Byte must be a hexadecimal number between 0x00 and 0xFF.");
					return;
				}
				if (getButtonGroupSelectedIndex(buttonGroup) != -1) processTypeIndex = getButtonGroupSelectedIndex(buttonGroup);
				registerN = comboBoxRegisterN.getSelectedIndex();
				registerM = comboBoxRegisterM.getSelectedIndex();
				_byte = newByte;
				comment = commentTextArea.getText();
				editDialog.dispose();
			}
		});
		okButton.setSize(okButton.getPreferredSize());
		okButton.setLocation(xOffset + 55, yOffset + 220);
		editDialog.add(okButton);
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editDialog.dispose();
			}
		});
		cancelButton.setSize(cancelButton.getPreferredSize());
		cancelButton.setLocation(xOffset + 125, yOffset + 220);
		editDialog.add(cancelButton);
		
		editDialog.setVisible(true);
		update();
	}
	
	public void update() {
		String hexNumber = Integer.toHexString(_byte).toUpperCase();
		if (hexNumber.length() == 1) hexNumber = new StringBuilder(hexNumber).insert(0, "0").toString();
		hexNumber = "0x" + hexNumber;
		
		if (processTypeIndex == 0) setText("S" + registerN + " = " + hexNumber);
		else if (processTypeIndex == 1) setText("S" + registerN + " = S" + registerM);
		else if (processTypeIndex == 2) setText("S" + registerN + " += " + hexNumber);
		else if (processTypeIndex == 3) setText("S" + registerN + " -= " + hexNumber);
		else if (processTypeIndex == 4) setText("Wait " + hexNumber);
		else if (processTypeIndex == 5) setText("S" + registerN + " = S" + registerN + " AND " + hexNumber);
		else if (processTypeIndex == 6) setText("S" + registerN + " = S" + registerN + " XOR " + hexNumber);
		else if (processTypeIndex == 7) setText("S" + registerN + " = S" + registerN + " OR " + hexNumber);
		else if (processTypeIndex == 8) setText("S" + registerN + " += S" + registerM);
		else if (processTypeIndex == 9) setText("S" + registerN + " -= S" + registerM);
		
		setSize(getPreferredSize().width + 40, getPreferredSize().height + 15);
	}
	
	// Returns -1 if no buttons are selected
	private static int getButtonGroupSelectedIndex(ButtonGroup buttonGroup) {
		ArrayList<JRadioButton> buttons = new ArrayList<JRadioButton>();
		Enumeration<AbstractButton> enumeration = buttonGroup.getElements();
		while (enumeration.hasMoreElements()) buttons.add((JRadioButton) enumeration.nextElement());
		for (int i = 0; i < buttons.size(); i++) if (buttonGroup.isSelected(buttons.get(i).getModel())) return i;
		return -1;
	}
}