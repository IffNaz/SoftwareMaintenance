package application;

import javafx.scene.image.Image;

public class MapCursor {
	public Image[] imageOption;
	
	public int cursorCols;
	public int cursorRows;
	
	public MapCursor() {
		imageOption = new Image[1];
		imageOption[0] = new Image(MapCursor.class.getResourceAsStream("./normal_cursor.gif"));
		cursorCols = 17;
		cursorRows = 17;
		
	}
	
}