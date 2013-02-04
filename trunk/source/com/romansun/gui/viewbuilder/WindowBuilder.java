package com.romansun.gui.viewbuilder;

import java.io.File;
import java.net.URL;

import org.apache.log4j.Logger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class WindowBuilder extends Application {
	private static final Logger LOG = Logger.getLogger(WindowBuilder.class);
	private static final String PATH_TO_FILE = "fxml\\main_window.fxml";

	@Override
	public void start(Stage stage) throws Exception {
		try {
			File f = new File(PATH_TO_FILE);
			URL fxmlURL = f.toURL();
			if (fxmlURL == null) {
				throw new IllegalArgumentException("FXML file cannot be load");
			}
			AnchorPane mainFrame = FXMLLoader.load(fxmlURL);
			Scene scene = new Scene(mainFrame);
			stage.setScene(scene);
			stage.setTitle("Учет талонов");
			stage.getIcons().add(new Image("file:resource/icon.png"));
			stage.show();
		} catch (Exception ex) {
			LOG.error("Ошибка при выполнении загрузки главного окна: "
					+ ex.getStackTrace());
		}
	}
	
	public void show() {
		Application.launch(WindowBuilder.class);
	}
}
