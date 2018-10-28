package frank;

import java.util.ArrayList;
import java.util.Random;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import tuncer.PathfindingAlgorithm;

public class MazeGeneratorWithPathfinding extends Application{
	private Canvas can;
	private GraphicsContext gc;
	
	private final int canSize = 800, width = 5;
	private int cols, rows;
	private ArrayList<Cell> stack = new ArrayList<Cell>();
	
	private Cell[][] zellen;
	
	private Cell current, next;
	
	private PathfindingAlgorithm pfa;
	private Timeline tl_draw;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void init() throws Exception {
		tl_draw = new Timeline(new KeyFrame(Duration.millis(1000/100), e -> {
			draw();
		}));
		tl_draw.setCycleCount(Timeline.INDEFINITE);
		tl_draw.play();
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		Pane root = new Pane();
		Scene scene = new Scene(root, canSize, canSize);
		
		can = new Canvas(scene.getWidth(), scene.getHeight());
		gc = can.getGraphicsContext2D();
		
		root.getChildren().add(can);
		root.setStyle("-fx-background-color: #000000");
		
		stage.setTitle("Random maze generator");
		
//		scene.widthProperty().addListener((obsv, oldVal, newVal) -> {
//		   can.setWidth(newVal.doubleValue());
//		});
//		
//		scene.heightProperty().addListener((obsv, oldVal, newVal) -> {
//			can.setHeight(newVal.doubleValue());
//		});
		
		stage.setScene(scene);
		stage.show();
		
		//setup
		cols = canSize / width;
		rows = canSize / width;
		zellen = new Cell[rows][cols];
		
		for(int i = 0; i < zellen.length; i++) {
			for(int n = 0; n < zellen[0].length; n++) {
				zellen[i][n] = new Cell(gc, width);
				zellen[i][n].setX(n * width);
				zellen[i][n].setY(i * width);
			}
		}
		
		current = zellen[0][0];
		
		pfa = new PathfindingAlgorithm(scene, can, zellen, width);
	}
	
	private void draw() {
		boolean unvisited = false;
		gc.clearRect(0, 0, can.getWidth(), can.getHeight());
		
		for(int i = 0; i < zellen.length; i++) {
			for(int n = 0; n < zellen[0].length; n++) {
				zellen[i][n].show();
				unvisited = !zellen[i][n].isVisited() ? true : unvisited;
			}
		}
		
		if (unvisited) {
			current.setVisited(true);
			current.highlight();
			
			//Step 1
			next = checkNeighbors((int) current.getX(),(int) current.getY());
			if (next != null) {
				next.setVisited(true);
				
				//Step2
				stack.add(current);
				
				//Step 3
				removeWalls();
				
				//Step 4
				current = next;
			} else if (stack.size() > 0){
				current = stack.get(stack.size() - 1);
				stack.remove(stack.size() - 1);
			}
		} else {
			pfa.doDraw(tl_draw);
		}
	}
	
	private Cell checkNeighbors(int x, int y) {
		Random r = new Random();
		ArrayList<Cell> neighbors = new ArrayList<Cell>();
		
		x /= width;
		y /= width;
		
		Cell top = y - 1 >= 0 ? zellen[y-1][x] : null;
		Cell right = x + 1 < zellen[0].length ? zellen[y][x+1] : null;
		Cell bottom = y + 1 < zellen.length ? zellen[y+1][x] : null;
		Cell left = x - 1 >= 0 ? zellen[y][x-1] : null;
		
		if (top != null && !top.isVisited()) {
			neighbors.add(top);
		}
		if (right != null && !right.isVisited()) {
			neighbors.add(right);
		}
		if (bottom != null && !bottom.isVisited()) {
			neighbors.add(bottom);
		}
		if (left != null && !left.isVisited()) {
			neighbors.add(left);
		}
		
		if (neighbors.size() > 0) {
			return neighbors.get(r.nextInt(neighbors.size()));
		}
		return null;
	}
	
	private void removeWalls() {
		final int x = (int) ((current.getX() / width) - (next.getX() / width));
		if (x == 1) {
			current.setWall(false, 3);
			next.setWall(false, 1);
		} else if (x == -1) {
			current.setWall(false, 1);
			next.setWall(false, 3);
		}
		
		final int y = (int) ((current.getY() / width) - (next.getY() / width));
		if (y == 1) {
			current.setWall(false, 0);
			next.setWall(false, 2);
		} else if (y == -1) {
			current.setWall(false, 2);
			next.setWall(false, 0);
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}