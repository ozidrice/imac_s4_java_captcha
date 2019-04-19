package fr.upem.captcha.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import fr.upem.captcha.model.Image;
import fr.upem.captcha.model.category.Category;
import fr.upem.captcha.model.category.RoundSign;
import fr.upem.captcha.model.category.Sign;
import fr.upem.captcha.view.View;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class AppController{
	
	public  static final int NUMBER_OF_IMAGE_PRINTED = 9;
	private static final int MAX_NUMBER_OF_IMAGE_OF_THE_CATEGORY = 4;
	private static final ArrayList<Category> LEVEL1_CATEGORY = new ArrayList<Category>(Arrays.asList(
		new Sign()
	));
	private static final ArrayList<Category> LEVEL2_CATEGORY = new ArrayList<Category>(Arrays.asList(
		new RoundSign()
	));
	
	private static ObservableList<Image> showedImage = FXCollections.observableArrayList(new ArrayList<Image>(NUMBER_OF_IMAGE_PRINTED));
	private static ArrayList<Image> imagesNeededToSuccess = new ArrayList<Image>();
	private static ObservableList<Integer> selectedImagesIndex = FXCollections.observableArrayList(new ArrayList<Integer>());
	private static StringProperty message = new SimpleStringProperty();
	
	/**
	 * Launch the application
	 * @param primaryStage given in the function start(Stage) when you extends javafx.Application
	 */
	public static void launch(Stage primaryStage) {
		createView(primaryStage); //Need to be before setup to init listeners
		setupCatpcha(LEVEL1_CATEGORY);		
	}
	
	private static void setupCatpcha(ArrayList<Category> categoryLevel) {
		Random rand = new Random();
		ArrayList<Image> tmpImagesToShow = new ArrayList<Image>();
		
		//Get random category
		Category category = categoryLevel.get(rand.nextInt(categoryLevel.size()));
		message.set("Veuillez sélectionner les images qui contiennent..." + category.getName());
		System.out.println(category.getLevel());
		
		//Set image of the selected category
		int numberOfImagesRandom = rand.nextInt(MAX_NUMBER_OF_IMAGE_OF_THE_CATEGORY)+1;
		tmpImagesToShow.addAll(Image.random(numberOfImagesRandom, category));
		imagesNeededToSuccess.addAll(tmpImagesToShow);
		
		//Pick random images to fill the array
		tmpImagesToShow.addAll(Image.random(NUMBER_OF_IMAGE_PRINTED - numberOfImagesRandom, null, category));
		
		//Randomise order
		Collections.shuffle(tmpImagesToShow);
		
		//Push to view
		showedImage.addAll(tmpImagesToShow);
	}
	
	/**
	 * Create and launch the view
	 * @param primaryStage given in the function start(Stage) when you extends javafx.Application
	 */
	private static void createView(Stage primaryStage) {
		View view = new View(primaryStage);
		view.launch();
	}
	
	public static void validate() {
		if(selectedImagesAreGood()) {
			View.alert(Alert.AlertType.INFORMATION, "Félicitation, vous n'êtes pas un robot !");
		}else {
			View.alert(Alert.AlertType.ERROR, "Désolé, vous n'avez pas pas réussi le test");
			setupCatpcha(LEVEL2_CATEGORY);
		}
		System.out.println();
		
	}
	
	public static boolean selectedImagesAreGood() {
		if(selectedImagesIndex.size() == imagesNeededToSuccess.size()) {
			@SuppressWarnings("unchecked")
			ArrayList<Image> cpyImagesNeededToSuccess = (ArrayList<Image>) imagesNeededToSuccess.clone();
			for (int index : selectedImagesIndex)
				cpyImagesNeededToSuccess.remove(showedImage.get(index));
			if(cpyImagesNeededToSuccess.size() == 0)
				return true;
		}
		return false;
	}

	/**
	 * Select or unselect the image of the index imgIndex
	 * @param imgIndex : index of the image
	 */
	public static void toggleSelectedImage(int imgIndex) {
		if(selectedImagesIndex.contains(imgIndex))
			selectedImagesIndex.remove((Integer)imgIndex); //Use remove(Obj) instead of remove(int)
		else
			selectedImagesIndex.add(imgIndex);
	}
	
	/**
	 * Used to set up listener in view
	 * @return ObservableList of Image that need to be printed on view
	 */
	public static ObservableList<Image> getObservableShowedImage(){
		return showedImage;
	}
	
	/**
	 * Used to set up listener in view
	 * @return ObservableList of Integer that represent the index of the selected images
	 */
	public static ObservableList<Integer> getObservableSelectedImage(){
		return selectedImagesIndex;
	}
	
	/**
	 * Used to set up listener in view
	 * @return StringProperty of the main message that need to be printed on view
	 */
	public static StringProperty getMessageProperty() {
		return message;
	}

}
