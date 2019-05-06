package fr.upem.captcha.application;
	
import fr.upem.captcha.controller.AppController;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		AppController.launch(primaryStage);
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
