package frank;

import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Cell {
	private double x, y, width;
	private int f, g, h;
	private GraphicsContext gc;
	private boolean walls [] = {true, true, true, true};
	private boolean visited = false;
	private Cell previous = null;
	private ArrayList<Cell> neighbors = new ArrayList<Cell>();
	
	public Cell(GraphicsContext gc, int width) {
		this.gc = gc;
		this.width = width;
	}
	
	public void show() {
		if (visited) {
			gc.setFill(Color.rgb(100, 0, 100));
			gc.fillRect(x, y, width, width);
		}
		fillWalls();
	}
	
	public void show(Color color) {
		this.gc.setFill(color);
		this.gc.fillRect(x, y, width, width);
//		this.gc.strokeRect(this.i*width, this.j*height, width - 1, height - 1);
		fillWalls();
	}
	
	private void fillWalls() {
		gc.setStroke(Color.rgb(255, 255, 255));
		//oben, rechts, unten, links
		if (walls[0]) {
			gc.strokeLine(x, y, x + width, y);
		}
		
		if (walls[1]) {
			gc.strokeLine(x + width, y, x + width, y + width);
		}
		
		if (walls[2]) {
			gc.strokeLine(x + width, y + width, x, y + width);
		}
		
		if (walls[3]) {
			gc.strokeLine(x, y + width, x, y);
		}
	}
	
	public void addNeighbors(Cell[][] grid) {
		int i = (int) (y / width);
		int j = (int) (x / width);
		
		if (i < gc.getCanvas().getHeight() / width - 1) {
			neighbors.add(grid[i+1][j]);
		}
		
		if (i > 0) {
			neighbors.add(grid[i-1][j]);
		}

		if (j < gc.getCanvas().getWidth() / width - 1) {
			neighbors.add(grid[i][j+1]);
		}

		if (j > 0) {
			neighbors.add(grid[i][j-1]);
		}
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public double getX() {
		return x;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	public double getY() {
		return y;
	}
	
	public void setVisited(boolean visited) {
		this.visited = visited;
	}
	
	public boolean isVisited() {
		return visited;
	}
	
	public void setWall(boolean val, int index) {
		walls[index] = val;
	}
	
	public boolean getWall(int index) {
		return walls[index];
	}
	
	public void highlight() {
		gc.setFill(Color.rgb(255, 0, 255));
		gc.fillRect(x, y, width, width);
	}
	
	public void setPrevious(Cell previous) {
		this.previous = previous;
	}
	
	public Cell getPrevious() {
		return previous;
	}
	
	public void setG(int g) {
		this.g = g;
	}
	
	public int getG() {
		return this.g;
	}
	
	public void setH(int h) {
		this.h = h;
	}
	
	public int getH() {
		return this.h;
	}
	
	public void setF(int f) {
		this.f = f;
	}
	
	public int getF() {
		return this.f;
	}
	
	public ArrayList<Cell> getNeighbors() {
		return this.neighbors;
	}
}