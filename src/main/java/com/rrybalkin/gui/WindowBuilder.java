package com.rrybalkin.gui;

import com.rrybalkin.utils.Messages;
import com.rrybalkin.utils.Resources;
import com.rrybalkin.utils.SuppressFBWarnings;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import java.io.File;
import java.net.URL;

@SuppressFBWarnings({"DM_EXIT", "REC_CATCH_EXCEPTION"})
public class WindowBuilder extends Application {
	private static final Logger LOG = Logger.getLogger(WindowBuilder.class);

	@Override
	public void start(Stage stage) throws Exception {
		try {
			File mainWindow = Resources.getResource(Resources.MAIN_WINDOW_FXML);
			URL fxmlURL = mainWindow.toURI().toURL();
			AnchorPane mainFrame = FXMLLoader.load(fxmlURL);
			Scene scene = new Scene(mainFrame);
			stage.setScene(scene);
			stage.setTitle(Messages.get("app.title"));
			File icon = Resources.getResource(Resources.ICON_IMAGE);
			stage.getIcons().add(new Image(icon.toURI().toString()));
			stage.show();
		} catch (Exception e) {
			LOG.error("Error while loading main window tab: ", e);
			throw e;
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
