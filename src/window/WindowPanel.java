package window;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.JPanel;

import boxes.*;

public class WindowPanel extends JPanel implements MouseListener, FocusListener, MouseWheelListener {
	private static final long serialVersionUID = 6312551008988280515L;
	private static final int ENDBOX_CURVE_SIZE = 40;
	private static Point sub(Point p1, Point p2) { return new Point(p1.x - p2.x, p1.y - p2.y); }
	
	private String subroutineName;
	
	public String getSubroutineName() {
		return subroutineName;
	}
	
	public void setSubroutineName(String subroutineName) {
		this.subroutineName = subroutineName;
	}
	
	private int selectedColourCounter = 0;
	private static final int SELECT_FLICKER = 25;
	private Color selectedColour;
	private Box startBox;
	
	private Box movingBox = null;
	private Point oldMousePosition;
	
	private Box boxBeingLinked = null;
	private boolean yesPartBeingLinked = false;
	
	private boolean panning = false;
	private Point panningStartPoint;
	
	private boolean boxSelecting = false;
	private Point boxSelectingStart;
	
	public Box getStartBox() {
		return startBox;
	}
	
	public boolean isDoingNothing() {
		return (boxBeingLinked == null && movingBox == null && !panning);
	}
	
	public WindowPanel(MainWindow window, String subroutineName) {
		super();
		setLayout(null);
		setBackground(Color.WHITE);
		
		this.subroutineName = subroutineName;
		
		startBox = new EndBox("Start");
		addBox(startBox, window.getWidth()/2 - 62, 40);
		
		try { oldMousePosition = MouseInfo.getPointerInfo().getLocation(); }
		catch(NullPointerException e) {}
		
		selectedColour = new Color(0, 190, 190);
		addMouseListener(this);
		addMouseWheelListener(this);
	}
	
	public void tick() {
		try {
			if (boxSelecting) {
				Point point1 = boxSelectingStart;
				Point point2 = new Point(MouseInfo.getPointerInfo().getLocation().x - getLocationOnScreen().x, MouseInfo.getPointerInfo().getLocation().y - getLocationOnScreen().y);
				Point minPoint = new Point(Math.min(point1.x, point2.x), Math.min(point1.y, point2.y));
				Point maxPoint = new Point(Math.max(point1.x, point2.x), Math.max(point1.y, point2.y));
				for (Component c : getComponents()) if (c instanceof Box) {
					Point boxMinPoint = c.getLocation();
					Point boxMaxPoint = new Point(c.getLocation().x + c.getWidth(), c.getLocation().y + c.getHeight());
					// AABB collision detection
					if (minPoint.x < boxMaxPoint.x && maxPoint.x > boxMinPoint.x && 
						minPoint.y < boxMaxPoint.y && maxPoint.y > boxMinPoint.y) {
						((Box) c).setToBeSelected(true);
					} else {
						((Box) c).setToBeSelected(false);
					}
				}
			}
			
			selectedColourCounter++;
			if (selectedColourCounter >= SELECT_FLICKER) selectedColourCounter = 0;
			if (movingBox != null) {
				int differenceX = MouseInfo.getPointerInfo().getLocation().x - oldMousePosition.x;
				int differenceY = MouseInfo.getPointerInfo().getLocation().y - oldMousePosition.y;
				movingBox.setLocation(movingBox.getLocation().x + differenceX, movingBox.getLocation().y + differenceY);
				if (movingBox.isSelected()) {
					for (Component c : getComponents()) if (c instanceof Box && ((Box) c).isSelected()) {
						if (c == movingBox) continue;
						c.setLocation(c.getLocation().x + differenceX, c.getLocation().y + differenceY);
					}
				}
			} else if (panning) {
				int xDifference = MouseInfo.getPointerInfo().getLocation().x - panningStartPoint.x;
				int yDifference = MouseInfo.getPointerInfo().getLocation().y - panningStartPoint.y;
				for (Component c : getComponents()) if (c instanceof Box) {
					c.setLocation(c.getLocation().x + xDifference, c.getLocation().y + yDifference);
				}
				panningStartPoint = MouseInfo.getPointerInfo().getLocation();
			}
			oldMousePosition = MouseInfo.getPointerInfo().getLocation();
			
			// Check subroutines are still valid
			for (Component c : getComponents()) if (c instanceof SubroutineBox) ((SubroutineBox) c).checkValid();
		} catch(NullPointerException e) {}
		
		// Repaint JPanel
		repaint();
	}
	
	public void addBox(Box box, int x, int y) {
		box.setLocation(x, y);
		for (MouseListener l : box.getMouseListeners()) box.removeMouseListener(l);
		box.addMouseListener(this);
		add(box);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		if (!(g instanceof Graphics2D)) return;
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(Color.BLACK);
		g2.setStroke(new BasicStroke(2));
		
		drawComponents(g2);
		if (boxSelecting) {
			try {
				g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10.0f, new float[]{7.0f}, 0.0f));
				Point point1 = boxSelectingStart;
				Point point2 = new Point(MouseInfo.getPointerInfo().getLocation().x - getLocationOnScreen().x, MouseInfo.getPointerInfo().getLocation().y - getLocationOnScreen().y);
				if (point1.x < point2.x) {
					if (point1.y < point2.y) g2.drawRect(point1.x, point1.y, point2.x - point1.x, point2.y - point1.y);
					else g2.drawRect(point1.x, point2.y, point2.x - point1.x, point1.y - point2.y);
				} else {
					if (point1.y < point2.y) g2.drawRect(point2.x, point1.y, point1.x - point2.x, point2.y - point1.y);
					else g2.drawRect(point2.x, point2.y, point1.x - point2.x, point1.y - point2.y);
				}
				g2.setStroke(new BasicStroke(2));
			} catch(NullPointerException e) {}
		}
	}
	
	private enum Direction {
		UP, DOWN, LEFT, RIGHT
	}
	
	private void drawArrow(Graphics2D g2, Direction direction, Point p) {
		g2.setStroke(new BasicStroke(1.5f));
		if (direction == Direction.DOWN) {
			g2.drawLine(p.x, p.y, p.x - 5, p.y - 5);
			g2.drawLine(p.x, p.y, p.x + 5, p.y - 5);
		} else if (direction == Direction.UP) {
			g2.drawLine(p.x, p.y, p.x - 5, p.y + 5);
			g2.drawLine(p.x, p.y, p.x + 5, p.y + 5);
		} else if (direction == Direction.LEFT) {
			g2.drawLine(p.x, p.y, p.x + 5, p.y + 5);
			g2.drawLine(p.x, p.y, p.x + 5, p.y - 5);
		} else if (direction == Direction.RIGHT) {
			g2.drawLine(p.x, p.y, p.x - 5, p.y - 5);
			g2.drawLine(p.x, p.y, p.x - 5, p.y + 5);
		}
		g2.setStroke(new BasicStroke(2));
	}
	
	private void drawLine(Graphics2D g2, Point p1, Point p2, boolean withArrow) { 
		g2.drawLine(p1.x, p1.y, p2.x, p2.y);
		if (!withArrow || p1.distance(p2) < 30.0) return;
		if (p1.x == p2.x) {
			if (p1.y < p2.y) drawArrow(g2, Direction.DOWN, new Point(p1.x, (p1.y + p2.y)/2));
			else drawArrow(g2, Direction.UP, new Point(p1.x, (p1.y + p2.y)/2));
		} else if (p1.y == p2.y) {
			if (p1.x < p2.x) drawArrow(g2, Direction.RIGHT, new Point((p1.x + p2.x)/2, p1.y));
			else drawArrow(g2, Direction.LEFT, new Point((p1.x + p2.x)/2, p1.y));
		}
	}
	
	private void drawLineBoxToBox(Graphics2D g2, Box start, Box end, boolean fromYes) {
		Point startPoint =	fromYes ?
							new Point(start.getX() + start.getWidth(), start.getY() + start.getHeight()/2) : 
							new Point(start.getX() + start.getWidth()/2, start.getY() + start.getHeight());
		Point endPoint = new Point(end.getX() + end.getWidth()/2, end.getY());
		if (fromYes) {
			if (startPoint.x < endPoint.x && startPoint.y < endPoint.y) {
				Point p1 = startPoint;
				Point p2 = new Point(endPoint.x, startPoint.y);
				Point p3 = endPoint;
				drawLine(g2, p1, p2, true);
				drawLine(g2, p2, p3, true);
			} else if (startPoint.y >= endPoint.y) {
				Point p1 = startPoint;
				Point p2, p3, p4, p5;
				if (end.getX() <= startPoint.x + 20 && (end.getY() + end.getHeight() <= startPoint.y || end.getX() + end.getWidth() <= startPoint.x)) {
					if (end.getX() + end.getWidth() - startPoint.x <= 15) p2 = new Point(startPoint.x + 30, p1.y);
					else p2 = new Point(end.getX() + end.getWidth() + 20, p1.y);
					if (endPoint.getY() >= start.getY()) p3 = new Point(p2.x, start.getY() - 20); 
					else p3 = new Point(p2.x, end.getY() - 20);
				} else {
					p2 = new Point((startPoint.x + end.getX())/2, startPoint.y);
					p3 = new Point(p2.x, endPoint.y - 20);
				}
				p4 = new Point(endPoint.x, p3.y);
				p5 = endPoint;
				drawLine(g2, p1, p2, true);
				drawLine(g2, p2, p3, true);
				drawLine(g2, p3, p4, true);
				drawLine(g2, p4, p5, true);
			} else if (startPoint.x >= endPoint.x) {
				if (end.getY() < start.getY() + start.getHeight()) {
					Point p1 = startPoint;
					Point p2 = new Point(startPoint.x + 30, p1.y);
					Point p3 = new Point(p2.x, start.getY() - 20);
					Point p4 = new Point(endPoint.x, p3.y);
					Point p5 = endPoint;
					drawLine(g2, p1, p2, true);
					drawLine(g2, p2, p3, true);
					drawLine(g2, p3, p4, true);
					drawLine(g2, p4, p5, true);
				} else {
					drawLineConnecting(g2, startPoint, endPoint, true);
				}
			}
		} else {
			if (startPoint.y < endPoint.y - 7) drawLineConnecting(g2, startPoint, endPoint, start instanceof DecisionBox || end instanceof DecisionBox);
			else {
				Point p1 = startPoint;
				Point p2 = new Point(startPoint.x, Math.max(startPoint.y, endPoint.y + end.getHeight()) + 20);
				Point p3, p4;
				
				boolean startOnLeft = startPoint.x - endPoint.x < 0;
				if (startOnLeft ? end.getX() - start.getX() - start.getWidth() > 20 : start.getX() - end.getX() - end.getWidth() > 20 ) {
					int xCoordOfPoints3and4;
					if (startOnLeft) xCoordOfPoints3and4 = (start.getX() + start.getWidth() + end.getX())/2;
					else xCoordOfPoints3and4 = (end.getX() + end.getWidth() + start.getX())/2;
					p3 = new Point(xCoordOfPoints3and4, p2.y);
					p4 = new Point(xCoordOfPoints3and4, end.getY() - 20);
				} else {
					p3 = new Point(end.getX() + (endPoint.x <= startPoint.x ? -20 : end.getWidth() + 20), p2.y);
					p4 = new Point(p3.x, end.getY() - 20);
				}
				
				Point p5 = new Point(endPoint.x, p4.y);
				Point p6 = endPoint;
				drawLine(g2, p1, p2, true);
				drawLine(g2, p2, p3, true);
				drawLine(g2, p3, p4, true);
				drawLine(g2, p4, p5, true);
				drawLine(g2, p5, p6, true);
			}
		}
	}
	
	private void drawLineBoxToPoint(Graphics2D g2, Box start, Point endPoint, boolean fromYes) {
		if (lastEntered instanceof Box && lastEntered != startBox) {
			drawLineBoxToBox(g2, start, (Box) lastEntered, fromYes);
			return;
		}
		Point startPoint =	fromYes ?
							new Point(start.getX() + start.getWidth(), start.getY() + start.getHeight()/2) :
							new Point(start.getX() + start.getWidth()/2, start.getY() + start.getHeight());
		
		if (fromYes) {
			Point p1 = startPoint;
			Point p2, p3, p4;
			if (endPoint.x - startPoint.x > 30) {
				p2 = new Point(endPoint.x, p1.y);
				p3 = endPoint;
				drawLine(g2, p1, p2, true);
				drawLine(g2, p2, p3, true);
			} else {
				p2 = new Point(startPoint.x + 30, startPoint.y);
				p3 = new Point(p2.x, endPoint.y);
				p4 = endPoint;
				drawLine(g2, p1, p2, true);
				drawLine(g2, p2, p3, true);
				drawLine(g2, p3, p4, true);
			}
		} else {
			if ((endPoint.y - startPoint.y)/2 > 30) drawLineConnecting(g2, startPoint, endPoint, start instanceof DecisionBox);
			else {
				Point p1 = startPoint;
				Point p2 = new Point(startPoint.x, startPoint.y + 30);
				Point p3 = new Point(endPoint.x, startPoint.y + 30);
				Point p4 = endPoint;
				drawLine(g2, p1, p2, true);
				drawLine(g2, p2, p3, true);
				drawLine(g2, p3, p4, true);
			}
		}
	}
	
	private void drawLineConnecting(Graphics2D g2, Point startPoint, Point endPoint, boolean decisionBox) {
		if (startPoint.x == endPoint.x || startPoint.y == endPoint.y) {
			drawLine(g2, startPoint, endPoint, true);
		} else if (Math.abs(startPoint.x - endPoint.x) < 30 && !decisionBox) {
			drawLine(g2, new Point((startPoint.x + endPoint.x)/2, startPoint.y), new Point((startPoint.x + endPoint.x)/2, endPoint.y), true);
		} else {
			drawLine(g2, startPoint, new Point(startPoint.x, (startPoint.y + endPoint.y)/2), true);
			drawLine(g2, new Point(endPoint.x, (startPoint.y + endPoint.y)/2), new Point(endPoint.x, endPoint.y), true);
			drawLine(g2, new Point(startPoint.x, (startPoint.y + endPoint.y)/2), new Point(endPoint.x, (startPoint.y + endPoint.y)/2), true);
		}
	}
	
	private void drawComponents(Graphics2D g2) {
		for (Component c : getComponents()) {
			if (c instanceof Box) {
				// Draw line to next box(es)
				if (((Box) c).getNextBox() != null) drawLineBoxToBox(g2, (Box) c, ((Box) c).getNextBox(), false);
				if (c instanceof DecisionBox && ((DecisionBox) c).getYesBox() != null) drawLineBoxToBox(g2, (Box) c, ((DecisionBox) c).getYesBox(), true);
			}
			
			if (c instanceof EndBox || c instanceof ReturnBox) {
				if (selectedColourCounter >= SELECT_FLICKER/2) if (((Box) c).isSelected() || ((Box) c).isToBeSelected()) g2.setColor(selectedColour);
				g2.drawRoundRect(c.getX(), c.getY(), c.getWidth(), c.getHeight(), ENDBOX_CURVE_SIZE, ENDBOX_CURVE_SIZE);
			} else if (c instanceof BreakBox) {
				g2.setColor(Color.RED);
				if (selectedColourCounter >= SELECT_FLICKER/2) if (((Box) c).isSelected() || ((Box) c).isToBeSelected()) g2.setColor(selectedColour);
				g2.drawRoundRect(c.getX(), c.getY(), c.getWidth(), c.getHeight(), ENDBOX_CURVE_SIZE, ENDBOX_CURVE_SIZE);
			} else if (c instanceof ProcessBox || c instanceof SubroutineBox) {
				if (selectedColourCounter >= SELECT_FLICKER/2) if (((Box) c).isSelected() || ((Box) c).isToBeSelected()) g2.setColor(selectedColour);
				g2.drawRect(c.getX(), c.getY(), c.getWidth(), c.getHeight());
			} else if (c instanceof OutputBox || c instanceof InputBox || c instanceof ADCBox) {
				if (selectedColourCounter >= SELECT_FLICKER/2) if (((Box) c).isSelected() || ((Box) c).isToBeSelected()) g2.setColor(selectedColour);
				g2.drawLine(c.getX() + 10, c.getY(), c.getX() + c.getWidth(), c.getY());
				g2.drawLine(c.getX(), c.getY() + c.getHeight(), c.getX() + c.getWidth() - 10, c.getY() + c.getHeight());
				g2.drawLine(c.getX(), c.getY() + c.getHeight(), c.getX() + 10, c.getY());
				g2.drawLine(c.getX() + c.getWidth() - 10, c.getY() + c.getHeight(), c.getX() + c.getWidth(), c.getY());
			} else if (c instanceof DecisionBox) {
				if (selectedColourCounter >= SELECT_FLICKER/2) if (((Box) c).isSelected() || ((Box) c).isToBeSelected()) g2.setColor(selectedColour);
				g2.drawLine(c.getX() + 0, c.getY() + c.getHeight()/2, c.getX() + c.getWidth()/2, c.getY() + 0);
				g2.drawLine(c.getX() + c.getWidth()/2, c.getY() + 0, c.getX() + c.getWidth() - 0, c.getY() + c.getHeight()/2);
				g2.drawLine(c.getX() + 0, c.getY() + c.getHeight()/2, c.getX() + c.getWidth()/2, c.getY() + c.getHeight() - 0);
				g2.drawLine(c.getX() + c.getWidth()/2, c.getY() + c.getHeight() - 0, c.getX() + c.getWidth() - 0, c.getY() + c.getHeight()/2);
				g2.setFont(new Font("Calibri", Font.PLAIN, 16));
				g2.setColor(new Color(0, 128, 0));
				g2.drawString("Y", c.getX() + c.getWidth() - 25, c.getY() + c.getHeight()/2 + 6);
				g2.setColor(Color.RED);
				g2.drawString("N", c.getX() + c.getWidth()/2 - 6, c.getY() + c.getHeight() - 9);
			}
			
			g2.setColor(Color.BLACK);
		}
		
		try {
			// Draw line from box to current position of the mouse cursor (or, to the box that the mouse cursor is in)
			if (boxBeingLinked != null && sub(mousePositionWhenLinking, MouseInfo.getPointerInfo().getLocation()).distance(new Point()) > 0.0) {
				if (boxBeingLinked instanceof DecisionBox && yesPartBeingLinked) {
					drawLineBoxToPoint(g2, boxBeingLinked, sub(MouseInfo.getPointerInfo().getLocation(), getLocationOnScreen()), true);
				} else {
					drawLineBoxToPoint(g2, boxBeingLinked, sub(MouseInfo.getPointerInfo().getLocation(), getLocationOnScreen()), false);
				}
			}
		} catch(NullPointerException e) {}
	}
	
	public boolean isNothingSelected() {
		for (Component c : getComponents()) if (c instanceof Box && (((Box) c).isSelected()) && c != startBox) return false;
		return true;
	}
	
	public void deleteSelectedComponents() {
		for (Component c : getComponents()) if (c instanceof Box && isDoingNothing()) {
			if ((Box) c != startBox && ((Box) c).isSelected()) {
				remove((Box) c);
				MainWindow.setSaved(false);
				for (Component c2 : getComponents()) if (c2 instanceof Box) {
					if (((Box) c2).getNextBox() == c && (!((Box) c2).isSelected() || c2 == startBox)) ((Box) c2).setNextBox(null);
					if (c2 instanceof DecisionBox && ((DecisionBox) c2).getYesBox() == c && !((Box) c2).isSelected()) ((DecisionBox) c2).setYesBox(null);
				}
			}
		}
	}
	
	public void deleteReturnBoxes() {
		for (Component c : getComponents()) if (c instanceof ReturnBox && isDoingNothing()) {
			remove((Box) c);
			MainWindow.setSaved(false);
			for (Component c2 : getComponents()) if (c2 instanceof Box) {
				if (((Box) c2).getNextBox() == c && (!((Box) c2).isSelected() || c2 == startBox)) ((Box) c2).setNextBox(null);
				if (c2 instanceof DecisionBox && ((DecisionBox) c2).getYesBox() == c && !((Box) c2).isSelected()) ((DecisionBox) c2).setYesBox(null);
			}
		}
	}

	public void mouseExited(MouseEvent e) {}
	
	private Component lastEntered;
	public void mouseEntered(MouseEvent e) {
		lastEntered = e.getComponent();
	}
	
	public void mouseWheelMoved(MouseWheelEvent e) {}
	
	public void mouseClicked(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			if (e.getClickCount() == 2 && e.getSource() instanceof Box) ((Box) e.getSource()).openEditDialog();
		} else if (e.getButton() == MouseEvent.BUTTON3) {
			if (e.getSource() instanceof Box) {
				if (((Box) e.getSource()).isSelected()) ((Box) e.getSource()).setSelected(false);
				else ((Box) e.getSource()).setSelected(true);
			}
		}
	}
	
	Point mousePositionWhenLinking;
	
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			if (e.getSource() instanceof Box) {
				if (boxBeingLinked == null && !panning) {
					movingBox = (Box) e.getSource();
				}
			}
		} else if (e.getButton() == MouseEvent.BUTTON2) {
			if (movingBox == null && !boxSelecting) {
				setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
				panning = true;
				panningStartPoint = e.getLocationOnScreen();
			}
		} else if (e.getButton() == MouseEvent.BUTTON3) {
			if (e.getSource() instanceof Box) {
				if (movingBox == null && !panning && !boxSelecting) {
					if (!(e.getSource() instanceof EndBox || e.getSource() instanceof ReturnBox) || ((Box) e.getSource()) == startBox) {
						if (e.getSource() instanceof DecisionBox && e.getX() > ((Box) e.getSource()).getWidth() * 0.7f) {
							yesPartBeingLinked = true;
						} 
						boxBeingLinked = (Box) e.getSource();
						setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
						mousePositionWhenLinking = MouseInfo.getPointerInfo().getLocation();
					}
				}
			} else if (e.getSource() == this) {
				if (boxBeingLinked == null && !panning && movingBox == null) {
					boxSelecting = true;
					boxSelectingStart = new Point(e.getLocationOnScreen().x - getLocationOnScreen().x, e.getLocationOnScreen().y - getLocationOnScreen().y);
				}
			}
		}
	}
	
	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			if (boxBeingLinked == null && !panning && !boxSelecting) {
				if (movingBox != null) MainWindow.setSaved(false);
				movingBox = null;
			}
		} else if (e.getButton() == MouseEvent.BUTTON2) {
			if (panning) panning = false;
			if (boxBeingLinked != null) setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			else setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		} else if (e.getButton() == MouseEvent.BUTTON3) {
			if (movingBox == null && !boxSelecting) {
				if (boxBeingLinked != null && lastEntered instanceof Box && lastEntered != startBox) {
					if (boxBeingLinked != lastEntered || MouseInfo.getPointerInfo().getLocation().distance(mousePositionWhenLinking) != 0.0) {
						if (yesPartBeingLinked && boxBeingLinked instanceof DecisionBox) ((DecisionBox) boxBeingLinked).setYesBox((Box) lastEntered);
						else boxBeingLinked.setNextBox((Box) lastEntered);
					}
				}
				boxBeingLinked = null;
				yesPartBeingLinked = false;
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				MainWindow.setSaved(false);
			}
			if (boxSelecting) {
				boxSelecting = false;
				for (Component c : getComponents()) if (c instanceof Box) {
					if (((Box) c).isToBeSelected()) {
						((Box) c).setToBeSelected(false);
						((Box) c).setSelected(true);
					}
				}
			}
		}
	}

	public void focusGained(FocusEvent e) {}

	public void focusLost(FocusEvent e) {
		if (movingBox != null) {
			movingBox = null;
			MainWindow.setSaved(false);
		}
		if (panning) panning = false;
		if (boxSelecting) {
			boxSelecting = false;
			for (Component c : getComponents()) if (c instanceof Box) {
				if (((Box) c).isToBeSelected()) {
					((Box) c).setToBeSelected(false);
					((Box) c).setSelected(true);
				}
			}
		}
		if (boxBeingLinked != null) {
			boxBeingLinked = null;
			MainWindow.setSaved(false);
		}
		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	public WindowPanel deepClone() {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(this);
			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(bais);
			return (WindowPanel) ois.readObject();
		} catch (IOException | ClassNotFoundException e) {
			System.err.println("Could not deep clone: " + e.getStackTrace());
			return null;
		}
	}
}
