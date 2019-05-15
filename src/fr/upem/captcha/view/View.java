package fr.upem.captcha.view;

import java.io.File;
import java.net.URL;
import java.util.Objects;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class View{
	private static final String TITLE = "Captcha";
	private static final int WIDHT = 1280;
	private static final int HEIGHT = 720;
	private Stage primaryStage;
	
	
	
	public View(Stage primaryStage) {
		this.primaryStage = Objects.requireNonNull(primaryStage);
	}
	
	public void launch() {
		try {
			//Loading FXML index page
			FXMLLoader loader = new FXMLLoader();
			URL url = new File("src/fr/upem/captcha/view/index.fxml").toURI().toURL();
			loader.setLocation(url);
			BorderPane root = (BorderPane) loader.load();
			
			//Creating scene
			Scene scene = new Scene(root, WIDHT, HEIGHT);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			this.primaryStage.setScene(scene);
			this.primaryStage.setTitle(TITLE);
			this.primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void alert(AlertType type, String message) {
		Alert alert = new Alert(type, message);
		alert.showAndWait();
	}
}
