package com.romansun.gui;

import java.io.File;
import java.net.URL;

import org.apache.log4j.Logger;

import com.romansun.utils.Resources;

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
			File mainWindow = Resources.getInstance().getResource(Resources.MAIN_WINDOW_FXML);
			URL fxmlURL = mainWindow.toURI().toURL();
			AnchorPane mainFrame = FXMLLoader.load(fxmlURL);
			Scene scene = new Scene(mainFrame);
			stage.setScene(scene);
			stage.setTitle("Vouchers");
			File icon = Resources.getInstance().getResource(Resources.ICON_IMAGE);
			stage.getIcons().add(new Image(icon.toURI().toString()));
			stage.show();
		} catch (Exception e) {
			LOG.error("Error while loading main window tab: ", e);
		}
	}

	@Override
	public void stop() throws Exception {
		LOG.info("Shutting down the application.");
		super.stop();
		System.exit(0);
	}

	public void show() {
		Application.launch(WindowBuilder.class);
	}
}
