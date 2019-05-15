package fr.upem.captcha.controller;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import fr.upem.captcha.images.Image;
import fr.upem.captcha.images.ImageLvl1;
import fr.upem.captcha.images.ImageLvl2;
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
	
	private static ObservableList<URL> showedImage = FXCollections.observableArrayList(new ArrayList<URL>(NUMBER_OF_IMAGE_PRINTED));
	private static ArrayList<URL> imagesNeededToSuccess = new ArrayList<URL>();
	private static ObservableList<Integer> selectedImagesIndex = FXCollections.observableArrayList(new ArrayList<Integer>());
	private static StringProperty message = new SimpleStringProperty();
	
	/**
	 * Launch the application
	 * @param primaryStage given in the function start(Stage) when you extends javafx.Application
	 */
	public static void launch(Stage primaryStage) {
		createView(primaryStage); //Need to be before setup to init listeners
		setupCatpcha(ImageLvl1.getAllClass());		
	}
	
	/**
	 * Create and launch the view
	 * @param primaryStage given in the function start(Stage) when you extends javafx.Application
	 */
	private static void createView(Stage primaryStage) {
		View view = new View(primaryStage);
		view.launch();
	}
	
	/**
	 * Clear all the saved values
	 */
	private static void clearAll() {
		showedImage.clear();
		imagesNeededToSuccess.clear();
		selectedImagesIndex.clear();
		message.set("");
	}
	
	/**
	 * Set up the catcha
	 * @param imageClassList List of class
	 */
	private static void setupCatpcha(List<Class<? extends Image>> imageClassList) {
		clearAll();
		
		Random rand = new Random();
		List<URL> tmpImagesToShow = new ArrayList<URL>();
		
		//Get random category
		Class<? extends Image> imageClass = imageClassList.get(rand.nextInt(imageClassList.size()));
		Image imageCategory = null;
		try {
			imageCategory = imageClass.getDeclaredConstructor().newInstance();
		
			message.set("Veuillez sélectionner les images qui contiennent..." + imageCategory.getName());
			
			//Set image of the selected category
			int numberOfImagesRandom = rand.nextInt(MAX_NUMBER_OF_IMAGE_OF_THE_CATEGORY)+1;
			tmpImagesToShow.addAll(imageCategory.getRandomPhotosURL(numberOfImagesRandom));
			imagesNeededToSuccess.addAll(tmpImagesToShow);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			System.err.println("Can't set new instance");
			e.printStackTrace();
		}
		
		//Pick random images to fill the array
		tmpImagesToShow.addAll(Image.getRandomURL(imageCategory, NUMBER_OF_IMAGE_PRINTED - tmpImagesToShow.size()));
		
		//Randomise order
		Collections.shuffle(tmpImagesToShow);
		
		//Push to view
		showedImage.addAll(tmpImagesToShow);
	}
	
	/**
	 * Function used when the captcha is validated
	 */
	public static void validate() {
		if(selectedImagesAreGood()) {
			View.alert(Alert.AlertType.INFORMATION, "Félicitation, vous n'êtes pas un robot !");
		}else {
			View.alert(Alert.AlertType.ERROR, "Désolé, vous n'avez pas pas réussi le test");
			setupCatpcha(ImageLvl2.getAllClass());
		}
		System.out.println();
		
	}
	
	/**
	 * @return true if all of the needed image were selected
	 */
	public static boolean selectedImagesAreGood() {
		if(selectedImagesIndex.size() == imagesNeededToSuccess.size()) {
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
	public static ObservableList<URL> getObservableShowedImage(){
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
