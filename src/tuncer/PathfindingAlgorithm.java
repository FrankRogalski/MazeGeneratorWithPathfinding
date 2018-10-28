package tuncer;

import java.util.ArrayList;
import java.util.Random;

import frank.Cell;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;




public class PathfindingAlgorithm {
	Random r = new Random();
	private final int cols, rows;
	private final Cell[][] grid;
	
	private ArrayList<Cell> openSet = new ArrayList<Cell>(); 
	private ArrayList<Cell> closedSet = new ArrayList<Cell>();
	
	private Cell start, end;
	private double w, h;
	
	private ArrayList<Cell> path = new ArrayList<Cell>();
	
	private Cell current;
	
	public PathfindingAlgorithm(Scene scene, Canvas can, Cell zellen[][], int width) {
		this.cols = (int) (scene.getWidth() / width);
		this.rows = (int) (scene.getHeight() / width);
		w = width;
		h = width;
		
		grid = zellen;
		
		for (int i = 0;i < cols;i++) {
			for (int j = 0;j < rows;j++) {
				grid[i][j].addNeighbors(grid);
			}
		}
		
//		start = grid[r.nextInt(cols - 1)][r.nextInt(rows - 1)];
//		end = grid[r.nextInt(cols - 1)][r.nextInt(rows - 1)];
		start = grid[0][0];
		end = grid[cols - 1][rows - 1];
										
		openSet.add(start);
	}
	
	public void doDraw(Timeline tl_draw) {
		if (openSet.size() > 0) {
			int winner = 0;
			for (int i=0;i < openSet.size();i++) {
				if (openSet.get(i).getF() < openSet.get(winner).getF()) {
					winner = i;
				}
			}
			current = openSet.get(winner);
			
			
			if (openSet.get(winner) == end) {
				current.show(Color.BLUE);
				System.out.println("DONE!");
				tl_draw.stop();
			}
			
			openSet = removeFromArray(openSet, current);
			closedSet.add(current);
			
			ArrayList<Cell> neighbors = current.getNeighbors();
			for (int i=0;i < neighbors.size();i++) {
				Cell neighbor = neighbors.get(i);
				
				boolean canMove = true;
				
				final int x = (int) (current.getX() / w - neighbor.getX() / w);
				final int y = (int) (current.getY() / h - neighbor.getY() / h);
				if (x == 1 && current.getWall(3)
						|| x == -1 && current.getWall(1)
						|| y == 1 && current.getWall(0)
						|| y == -1 && current.getWall(2)) {
					canMove = false;
				}
				
				if (!closedSet.contains(neighbor) && canMove) {
					int tempG = current.getG() + 1;
					
					if (openSet.contains(neighbor)) {
						if (tempG < neighbor.getG()) {
							neighbor.setG(tempG);
						}
					} else {
						neighbor.setG(tempG);
						openSet.add(neighbor);
					}
					
					neighbor.setH(heuristic(neighbor, end));
					neighbor.setF(neighbor.getG() + neighbor.getH());
					neighbor.setPrevious(current);
				}
			}
		}
		
		for (int i=0;i < closedSet.size();i++) {
			closedSet.get(i).show(Color.RED);
		}

		for (int i=0;i < openSet.size();i++) {
			openSet.get(i).show(Color.GREEN);
		}
		
		this.path = new ArrayList<Cell>();
		Cell temp = current;
		for (int i = 0;temp.getPrevious() != null;i++) {
			path.add(temp.getPrevious());
			temp = temp.getPrevious();
			path.get(i).show(Color.BLUE);
		}
	}
	
	private int heuristic(Cell a, Cell b) {
		//int d = new Point2D(arg1, arg2).distance(arg3, arg4)
		int d = (int) (Math.abs(a.getX() / w - b.getX() / w) + Math.abs(a.getY() / w - b.getY() / w));
		return d;
	}
	
	private ArrayList<Cell> removeFromArray(ArrayList<Cell> arr, Cell elt) {
		for (int i = arr.size() - 1;i >= 0;i--) {
			if (arr.get(i) == elt) {
				arr.remove(i);
			}
		}
		return arr;
	}
	
	
	public ArrayList<Cell> splice(ArrayList<Cell> del, int von, int bis) {
		bis = von + bis;  
		for (int i = bis; i >= von; i--) {
				  del.remove(i);
		  }
		  return del;
	}
}
