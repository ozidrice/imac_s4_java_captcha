package fr.upem.captcha.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public class IndexController implements Initializable{	
	@FXML
	private BorderPane rootPane;
	
	@FXML
	private GridPane gridPane;
	
	@FXML
	private BorderPane bottomBorderPane;
	
	@FXML
	private Label label;
	
	@FXML
	private Button valider;
	
	/**
	 * Launched when the validator buttun is clicked
	 * @param evt Event throwed
	 */
	@FXML
	public void handleValidate(ActionEvent evt){
		AppController.validate();
	}

	/**
	 * Initalize the view, controller is ready to go when finished
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		setMessageListener();
		setSelectedImageListener();
		setShowedImageListener();
	}
	
	/**
	 * Set up message Listener
	 */
	private void setMessageListener() {
		AppController.getMessageProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				label.setText(newValue);
			}
		});
	}
	
	/**
	 * Set up images Listener
	 */
	private void setSelectedImageListener() {
		AppController.getObservableSelectedImage().addListener(new ListChangeListener<Integer>() {
			@Override
			public void onChanged(Change<? extends Integer> change) {
				while(change.next()) {
					if(change.wasAdded())
						for(Integer index : change.getAddedSubList())
							setSelectedStatus(index);
					if(change.wasRemoved())
						for(Integer index : change.getRemoved())
							unsetSelectedStatus(index);
				}
			}
		});
	}
	
	/**
	 * 
	 * @param index
	 */
	private void setSelectedStatus(int index) {
		Node imageNode = getIndexFromGridPane(index);
		if(imageNode != null) {
			imageNode.setScaleX(4);
		}
	}
	
	private void unsetSelectedStatus(int index) {
		Node imageNode = getIndexFromGridPane(index);
		if(imageNode != null) {
			imageNode.setScaleX(1);
		}
	}
	
	private Node getIndexFromGridPane(int index) {
		index = index+1; //first element is part of gridpane
		if(this.gridPane.getChildren().size() < index)
			throw new IndexOutOfBoundsException(index);
		return this.gridPane.getChildren().get(index);
	}
	
	private int nbRows() {
		return (int) Math.sqrt(AppController.NUMBER_OF_IMAGE_PRINTED);
	}
	
	private void setShowedImageListener() {
		AppController.getObservableShowedImage().addListener(new ListChangeListener<URL>() {
			@Override
			public void onChanged(Change<? extends URL> change) {
				if(AppController.getObservableShowedImage().size() == AppController.NUMBER_OF_IMAGE_PRINTED) {
					//Clear grid content
					Node node = gridPane.getChildren().get(0);
					gridPane.getChildren().clear();
					gridPane.getChildren().add(0,node);
					//Get images
					int nbRows = nbRows(); 
					for(int i = 0; i < nbRows; i++) {
						for(int j = 0; j < nbRows; j++) { 
							int index = i*nbRows+j;
							ImageView imageView = new ImageView(new Image(AppController.getObservableShowedImage().get(index).toExternalForm()));
							imageView.setUserData(change);
							imageView.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<Event>() {
								@Override
								public void handle(Event evt) {
									AppController.toggleSelectedImage(index);
								}
							});
							imageView.setFitWidth(130);
							imageView.setFitHeight(130);
							gridPane.add(imageView, j, i);
						}
					}
				}
			}
		});
	}
}
