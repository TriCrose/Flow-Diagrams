package fileIO;

import java.awt.Component;

import boxes.*;
import window.*;

public class XMLGenerator {
	public static String generate() {
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
		for (int i = 0; i < MainWindow.getTabbedPane().getTabCount(); i++) {
			WindowPanel windowPanel = ((WindowPanel) MainWindow.getTabbedPane().getComponentAt(i));
			if (i == 0) xml += "<main start=\"" + getIDOfFirstBox(windowPanel) + "\">\n";
			else xml += "<sub start=\"" + getIDOfFirstBox(windowPanel) + "\" name=\"" + windowPanel.getSubroutineName() + "\">\n";
			
			for (Component c : windowPanel.getComponents()) if (c instanceof Box) {
				if (c == windowPanel.getStartBox()) continue;
				xml += "\t" + getXMLTagFromBox((Box) c, windowPanel) + "\n";
			}
			
			if (i == 0) xml += "</main>\n";
			else xml += "</sub>\n";
		}
		System.out.println("Generated XML.");
		return xml;
	}
	
	private static String getXMLTagFromBox(Box box, WindowPanel windowPanel) {
		if (box instanceof EndBox && box.getText().contentEquals("Start")) System.err.println("Start box being XML parsed!");
		
		String tag = "<box ";
		
		// 'type' attribute
		tag += "type=\"";
		if (box instanceof ADCBox) tag += "adc";
		else if (box instanceof BreakBox) tag += "break";
		else if (box instanceof DecisionBox) tag += "decision";
		else if (box instanceof EndBox) tag += "end";
		else if (box instanceof InputBox) tag += "input";
		else if (box instanceof OutputBox) tag += "output";
		else if (box instanceof ProcessBox) tag += "process";
		else if (box instanceof ReturnBox) tag += "return";
		else if (box instanceof SubroutineBox) tag += "subroutine";
		tag += "\" ";
		
		// 'command' attribute and parameters
		if (!(box instanceof ReturnBox || box instanceof EndBox)) {
			if (box instanceof SubroutineBox) tag += "command=\"" + box.getText() + "\" ";
			else if (box instanceof InputBox) tag += "Sd=\"S" + ((InputBox) box).getRegister() + "\" ";
			else if (box instanceof OutputBox) tag += "Sd=\"S" + ((OutputBox) box).getRegister() + "\" ";
			else if (box instanceof BreakBox) tag += "Sd=\"" + Box.toHex(((BreakBox) box).getBreakNumber()) + "\" ";
			else if (box instanceof ADCBox) tag += "Sd=\"S" + ((ADCBox) box).getRegister() + "\" ";
			
			else if (box instanceof DecisionBox) {
				tag += "command=\"" + (((DecisionBox) box).isEqualsTest() ? "=" : ">") + "\" ";
				tag += "Sd=\"S" + ((DecisionBox) box).getRegister() + "\" ";
				tag += "Ss=\"" + Box.toHex(((DecisionBox) box).getByte()) + "\" ";
			} else if (box instanceof ProcessBox) {
				ProcessBox b = (ProcessBox) box;
				switch (((ProcessBox) box).getProcessTypeIndex()) {
				case 0:
					tag += "command=\"=\" ";
					tag += "Sd=\"S" + b.getRegisterN() + "\" ";
					tag += "Ss=\"" + Box.toHex(b.getByte()) + "\" ";
					break;
				case 1:
					tag += "command=\"=\" ";
					tag += "Sd=\"S" + b.getRegisterN() + "\" ";
					tag += "Ss=\"S" + b.getRegisterM() + "\" ";
					break;
				case 2:
					tag += "command=\"+=\" ";
					tag += "Sd=\"S" + b.getRegisterN() + "\" ";
					tag += "Ss=\"" + Box.toHex(b.getByte()) + "\" ";
					break;
				case 3:
					tag += "command=\"-=\" ";
					tag += "Sd=\"S" + b.getRegisterN() + "\" ";
					tag += "Ss=\"" + Box.toHex(b.getByte()) + "\" ";
					break;
				case 4:
					tag += "command=\"WAIT\" ";
					tag += "Sd=\"" + Box.toHex(b.getByte()) + "\" ";
					break;
				case 5:
					tag += "command=\"AND\" ";
					tag += "Sd=\"S" + b.getRegisterN() + "\" ";
					tag += "Ss=\"" + Box.toHex(b.getByte()) + "\" ";
					break;
				case 6:
					tag += "command=\"XOR\" ";
					tag += "Sd=\"S" + b.getRegisterN() + "\" ";
					tag += "Ss=\"" + Box.toHex(b.getByte()) + "\" ";
					break;
				case 7:
					tag += "command=\"OR\" ";
					tag += "Sd=\"S" + b.getRegisterN() + "\" ";
					tag += "Ss=\"" + Box.toHex(b.getByte()) + "\" ";
					break;
				case 8:
					tag += "command=\"+=\" ";
					tag += "Sd=\"S" + b.getRegisterN() + "\" ";
					tag += "Ss=\"S" + b.getRegisterM() + "\" ";
					break;
				case 9:
					tag += "command=\"-=\" ";
					tag += "Sd=\"S" + b.getRegisterN() + "\" ";
					tag += "Ss=\"S" + b.getRegisterM() + "\" ";
					break;
				default:
					break;
				}
			}
		}
		
		// 'id' attribute
		tag += "id=\"";
		tag += getIDFromBox(box, windowPanel);
		tag += "\" ";
		
		// 'nextBox' attribute
		if (!(box instanceof ReturnBox || box instanceof EndBox)) {
			if (box instanceof DecisionBox) tag += "noBox=\"";
			else tag += "nextBox=\"";
			tag += getIDFromBox(box.getNextBox(), windowPanel);
			tag += "\" ";
		}
		
		// 'yesBox' attribute
		if (box instanceof DecisionBox) {
			tag += "yesBox=\"";
			tag += getIDFromBox(((DecisionBox) box).getYesBox(), windowPanel);
			tag += "\" ";
		}
		
		tag += "/>";
		return tag;
	}
	
	private static int getIDOfFirstBox(WindowPanel windowPanel) {
		return getIDFromBox(windowPanel.getStartBox().getNextBox(), windowPanel);
	}
	
	private static int getIDFromBox(Box box, WindowPanel windowPanel) {
		for (int i = 0; i < windowPanel.getComponentCount(); i++) {
			if (windowPanel.getComponent(i) == box) return i;
		}
		return -1;
	}
	
	private XMLGenerator() {}
}