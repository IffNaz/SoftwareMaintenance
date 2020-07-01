package application;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;

public class MapDisplay implements getPositions{
	private int boatRow = -1;
	private int boatCol = -1;
	private int axeRow = -1;
	private int axeCol = -1;
	
	public MapDisplay() {
		
	}
	
	public int getBoatRow() {
		return boatRow;
	}
	public int getBoatCol() {
		return boatCol;
	}
	public int getAxeRow() {
		return axeRow;
	}
	public int getAxeCol() {
		return axeCol;
	}
	
	private final int BOAT = 0;
	private final int AXE = 1;

	private int tileSize = 16;
	public int numCols;
	public int numRows;
	
	public MapCursor cursor;

	private int currentNumCols;
	private int currentNumRows;

	private int[][] mapMatrix;
	private int[][] tileType;
	
	private Image tileset;
	private int numTilesAcross;

	private Canvas mainCanvas;
	public Canvas currentCanvas;
	private Image mapImage;
	private Image originalMapImage;


	public int movesetCols;
	public int movesetRows;

	public Image items;
	public boolean axePut = false;
	public boolean boatPut = false;
//Takes the .map file as a string of numbers and prints out appropriate sprites based on the data received into a Map Matrix
	public void loadMapFile(String mapFile) {
		try {
			InputStream in = getClass().getResourceAsStream(mapFile);
			if (in == null) {
				System.out.print("Map not Found!");
			} else {
				System.out.println("Map Successfully Loaded!");
			}
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			numCols = Integer.parseInt(br.readLine());
			numRows = Integer.parseInt(br.readLine());
			currentNumCols = numCols;
			currentNumRows = numRows;

			mapMatrix = new int[numRows][numCols];
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

//initialises the canvas and renders the image onto it
	public void initialiseCanvas() {
		mainCanvas = new Canvas(640,640);
		currentCanvas = new Canvas(640, 640);
		tileType = new int[numRows][numCols];
		cursor = new MapCursor();

		for(int row = 0; row < numRows; row++) {
			for(int col = 0; col < numCols; col++) {
				if(mapMatrix[row][col] == 0) continue;

				int rc = mapMatrix[row][col];

				int r = rc / numTilesAcross;
				int c = rc % numTilesAcross;

				if (r == 0) {
					mainCanvas.getGraphicsContext2D().drawImage(
							tileset, c * tileSize, 0, tileSize, tileSize,
							col * tileSize, row * tileSize, tileSize, tileSize);
					currentCanvas.getGraphicsContext2D().drawImage(
							tileset, c * tileSize, 0, tileSize, tileSize,
							col * tileSize, row * tileSize, tileSize, tileSize);
					tileType[row][col] = 0;
				}
				else {
					mainCanvas.getGraphicsContext2D().drawImage(
							tileset, c * tileSize, tileSize, tileSize, tileSize,
							col * tileSize, row * tileSize, tileSize, tileSize);
					currentCanvas.getGraphicsContext2D().drawImage(
							tileset, c * tileSize, tileSize, tileSize, tileSize,
							col * tileSize, row * tileSize, tileSize, tileSize);
					tileType[row][col] = 1;
				}

			}
		}
		originalMapImage = mainCanvas.snapshot(null, null);
		drawCursorToMainCanvas();
		currentCanvas.getGraphicsContext2D().drawImage(
				cursor.imageOption[0], 0, 0, tileSize, tileSize,
				 cursor.cursorCols * tileSize, cursor.cursorRows * tileSize,
				 tileSize, tileSize);
		mapImage = mainCanvas.snapshot(null, null);
	}
//handles changes to the canvas, specifically the axe and boat
	private void replaceTileInMainCanvasToOriginal(int col, int row) {
		mainCanvas.getGraphicsContext2D().drawImage(
				originalMapImage,
				col * tileSize,
				row * tileSize,
				tileSize, tileSize,
				col * tileSize,
				row * tileSize,
				tileSize, tileSize);
	}
//draws the cursor onto the canvas
	private void drawCursorToMainCanvas() {
		mainCanvas.getGraphicsContext2D().drawImage(
				cursor.imageOption[0], 0, 0,
				tileSize, tileSize,
				cursor.cursorCols * tileSize,
				cursor.cursorRows * tileSize,
				tileSize, tileSize);
	}

	private void updateCurrentCanvas() {
		currentCanvas.getGraphicsContext2D().drawImage(
				mapImage,
				movesetCols * tileSize, movesetRows * tileSize,
				currentNumCols * tileSize, currentNumRows * tileSize,
				0, 0, 640, 640);
	}


	private void updateMoveset() {
		if (movesetCols > cursor.cursorCols) {
			movesetCols --;
		}
		else if (movesetRows > cursor.cursorRows) {
			movesetRows --;
		}
		else if (movesetCols + currentNumCols - 1 < cursor.cursorCols) {
			movesetCols ++;
		}
		else if (movesetRows + currentNumRows - 1 < cursor.cursorRows) {
			movesetRows ++;
		}

	}
	
	public void turningOnCursorColor() {
		replaceTileInMainCanvasToOriginal(cursor.cursorCols, cursor.cursorRows);
		updateItemsDraw();
		drawCursorToMainCanvas();
		mapImage = mainCanvas.snapshot(null, null);
		updateCurrentCanvas();

	}

//Handles cursor movements and updates the canvas accordinly
	public void cursorUp() {
		if (cursor.cursorRows > 0) {
			replaceTileInMainCanvasToOriginal(cursor.cursorCols, cursor.cursorRows);

			cursor.cursorRows --;

			updateItemsDraw();
			drawCursorToMainCanvas();

			mapImage = mainCanvas.snapshot(null, null);

			updateMoveset();
			updateCurrentCanvas();
		}
	}

	public void cursorDown() {
		if (cursor.cursorRows < numRows - 1 ) {
			replaceTileInMainCanvasToOriginal(cursor.cursorCols, cursor.cursorRows);

			cursor.cursorRows ++;

			updateItemsDraw();
			drawCursorToMainCanvas();

			mapImage = mainCanvas.snapshot(null, null);

			updateMoveset();
			updateCurrentCanvas();
		}
	}

	public void cursorLeft() {
		if (cursor.cursorCols > 0) {
			replaceTileInMainCanvasToOriginal(cursor.cursorCols, cursor.cursorRows);

			cursor.cursorCols --;

			updateItemsDraw();
			drawCursorToMainCanvas();

			mapImage = mainCanvas.snapshot(null, null);

			updateMoveset();
			updateCurrentCanvas();
		}
	}

	public void cursorRight() {
		if (cursor.cursorCols < numCols - 1 ) {
			replaceTileInMainCanvasToOriginal(cursor.cursorCols, cursor.cursorRows);
			cursor.cursorCols ++;

			updateItemsDraw();
			drawCursorToMainCanvas();

			mapImage = mainCanvas.snapshot(null, null);

			updateMoveset();
			updateCurrentCanvas();
		}
	}
	
	private void updateItemsDraw() {
		if (axePut) {
			mainCanvas.getGraphicsContext2D().drawImage(
					items,
					AXE  * tileSize, tileSize, tileSize, tileSize,
					axeCol * tileSize,
					axeRow * tileSize,
					tileSize, tileSize);
		}
		if (boatPut) {
			mainCanvas.getGraphicsContext2D().drawImage(
					items,
					BOAT  * tileSize, tileSize, tileSize, tileSize,
					boatCol * tileSize,
					boatRow * tileSize,
					tileSize, tileSize);
		}
	}
//Handles the placement of the axe and boat
	public int handleSetAxeRequest() {
		int handleType;

		replaceTileInMainCanvasToOriginal(cursor.cursorCols, cursor.cursorRows);
		if (tileType[cursor.cursorRows][cursor.cursorCols] == 1) {
			handleType = 1;
		}
		else {
			if (axePut) {
				replaceTileInMainCanvasToOriginal(axeCol, axeRow);
				
				tileType[axeRow][axeCol] = 0;
				tileType[cursor.cursorRows][cursor.cursorCols] = 1;
				
				handleType = 2;
			}
			else {
				handleType = 0;
			}
			
    		axePut = true;
	    	tileType[cursor.cursorRows][cursor.cursorCols] = 1;

	    	axeRow = cursor.cursorRows;
	    	axeCol = cursor.cursorCols;
		}

		updateItemsDraw();
    	drawCursorToMainCanvas();
		
    	mapImage = mainCanvas.snapshot(null, null);
    	updateCurrentCanvas();

    	return handleType;
	}

	public int handleSetBoatRequest() {
		int handleType;

		replaceTileInMainCanvasToOriginal(cursor.cursorCols, cursor.cursorRows);

		if (tileType[cursor.cursorRows][cursor.cursorCols] == 1) {
			handleType = 1;
		}
		else {
			if (boatPut) {
				replaceTileInMainCanvasToOriginal(boatCol, boatRow);
				
				tileType[boatRow][boatCol] = 0;
				tileType[cursor.cursorRows][cursor.cursorCols] = 1;
				
				handleType = 2;
			}
			else {
				handleType = 0;
			}

    		boatPut = true;   
	    	tileType[cursor.cursorRows][cursor.cursorCols] = 1;

	    	boatRow = cursor.cursorRows;
	    	boatCol = cursor.cursorCols;
	    	
		}
		updateItemsDraw();
		drawCursorToMainCanvas();
    	mapImage = mainCanvas.snapshot(null, null);
    	updateCurrentCanvas();
    	return handleType;
	}
}
