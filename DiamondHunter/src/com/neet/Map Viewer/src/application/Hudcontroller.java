package application;

import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class Hudcontroller {

	@FXML
	private void handleKeyAction(KeyEvent event) {

	    if (event.getCode() == KeyCode.W || event.getCode() == KeyCode.UP) {
	    	Main.mapDisplay.cursorUp();
	    }
	    else if (event.getCode() == KeyCode.S || event.getCode() == KeyCode.DOWN) {
	    	Main.mapDisplay.cursorDown();
	    }
	    else if (event.getCode() == KeyCode.A || event.getCode() == KeyCode.LEFT) {
	    	Main.mapDisplay.cursorLeft();
	    }
	    else if (event.getCode() == KeyCode.D || event.getCode() == KeyCode.RIGHT) {
	    	Main.mapDisplay.cursorRight();
	    }
	}

    @FXML
    void initialize() {

    }
}
