package com.romansun.gui.viewbuilder;

import java.io.File;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class WindowBuilder extends Application {
	
	private static final String PATH_TO_FILE = "fxml\\main_window.fxml";

	@Override
	public void start(Stage stage) throws Exception {
		File f = new File(PATH_TO_FILE);
    	URL fxmlURL=f.toURL();
    	if(fxmlURL == null) {
    		throw new IllegalArgumentException("FXML file cannot be load");
    	}
    	AnchorPane mainFrame=FXMLLoader.load(fxmlURL);
    	Scene scene=new Scene(mainFrame);
    	stage.setScene(scene);
    	stage.setTitle("Учет талонов");
    	stage.show();
	}
	
	public void show() {
		Application.launch(WindowBuilder.class);
	}
}
