package application;

import javafx.scene.image.Image;

public class MapCursor {
	public Image[] imageOption;
	public int current = 2;
	
	public int cursorCols;
	public int cursorRows;
	
	public MapCursor() {
		imageOption = new Image[3];
		imageOption[0] = new Image(MapCursor.class.getResourceAsStream("./green_cursor.gif"));
		imageOption[1] = new Image(MapCursor.class.getResourceAsStream("./red_cursor.gif"));
		imageOption[2] = new Image(MapCursor.class.getResourceAsStream("./normal_cursor.gif"));
		cursorCols = 17;
		cursorRows = 17;
		
	}
	
}
