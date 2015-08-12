package fileIO;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import javax.swing.JOptionPane;

import window.MainWindow;

public class ObjectSerializer {
	public static boolean serialize(String fileName, Serializable object) {
		try {
			if (fileName == null) return false;
			FileOutputStream fos = new FileOutputStream(fileName);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(object);
			oos.close();
			fos.close();
			System.out.println("Saved serialized object \"" + object.getClass() + "\" to file: " + fileName + ".");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(MainWindow.getMainWindow(), "Failed to write serialized object to \"" + fileName + "\".");
			return false;
		}
	}
	
	public static Object deserialize(String fileName) {
		Object object = null;
		try {
			if (fileName == null) return null;
			FileInputStream fis = new FileInputStream(fileName);
			ObjectInputStream ois = new ObjectInputStream(fis);
			object = ois.readObject();
			fis.close();
			ois.close();
			System.out.println("Loaded serialized object \"" + object.getClass() + "\" from file: " + fileName + ".");
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(MainWindow.getMainWindow(), "Failed to open serialized object from \"" + fileName + "\".");
		}
		return object;
	}
	
	private ObjectSerializer() {}
}
