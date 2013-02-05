package com.romansun.gui.viewbuilder;

import java.net.URL;

import org.apache.log4j.Logger;

import com.romansun.gui.utils.Resources;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class WindowBuilder extends Application {
	private static final Logger LOG = Logger.getLogger(WindowBuilder.class);

	@Override
	public void start(Stage stage) throws Exception {
		try {
			URL fxmlURL = new Resources().getMainWindowFXML().toURL();
			if (fxmlURL == null) {
				throw new IllegalArgumentException("FXML file cannot be load");
			}
			AnchorPane mainFrame = FXMLLoader.load(fxmlURL);
			Scene scene = new Scene(mainFrame);
			stage.setScene(scene);
			stage.setTitle("Учет талонов");
			stage.getIcons().add(new Image("file:resource/icon.png"));
			stage.show();
		} catch (Exception e) {
			LOG.error("Ошибка при выполнении загрузки главного окна: ", e);
		}
	}
	
	public void show() {
		Application.launch(WindowBuilder.class);
	}
}
