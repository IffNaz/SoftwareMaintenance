package application;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class MapDisplay {
	
	private int tileSize;
	int numCols;
	int numRows;
	private int[][] map;
	int numTilesAcross;
	Image tileset;
	Image items;
	private Image mapImage;
	
	public MapDisplay() {
		tileSize = 16;
		new TileMap(tileSize);
		loadMapFile("testmap.map");
		loadImagesFiles("testtileset.gif","items.gif");
		initialiseCanvas();
		
	}
	
	
	public void loadMapFile(String mapFile) {
		try {
			java.io.InputStream in = getClass().getResourceAsStream(mapFile);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			numCols = Integer.parseInt(br.readLine());
			numRows = Integer.parseInt(br.readLine());

			int[][] mapMatrix = new int[numRows][numCols];

			String delims = "\\s+";
			for(int row = 0; row < numRows; row++) {
				String line = br.readLine();
				String[] tokens = line.split(delims);
				for(int col = 0; col < numCols; col++) {
					mapMatrix[row][col] = Integer.parseInt(tokens[col]);
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void loadImagesFiles(String tilesetImage, String itemsImage) {
		try {
			tileset = new Image(MapDisplay.class.getResourceAsStream(tilesetImage));
			items = new Image(MapDisplay.class.getResourceAsStream(itemsImage));
			numTilesAcross = (int)tileset.getWidth() / tileSize;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void initialiseCanvas() {
		Canvas mainCanvas = new Canvas(640,640);
		int[][] tileType = new int[numRows][numCols];

		for(int row = 0; row < numRows; row++) {
			for(int col = 0; col < numCols; col++) {
				if(map[row][col] == 0) continue;

				int rc = map[row][col];

				int r = rc / numTilesAcross;
				int c = rc % numTilesAcross;

				if (r == 0) {
					mainCanvas.getGraphicsContext2D().drawImage(
							tileset, c * tileSize, 0, tileSize, tileSize,
							col * tileSize, row * tileSize, tileSize, tileSize);
				}
				else {
					mainCanvas.getGraphicsContext2D().drawImage(
							tileset, c * tileSize, tileSize, tileSize, tileSize,
							col * tileSize, row * tileSize, tileSize, tileSize);
					tileType[row][col] = 1;
				}

			}
		}
		mapImage = mainCanvas.snapshot(null, null);
	}
	
}