package fr.upem.captcha.view;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import fr.upem.captcha.model.Image;
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
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public class indexController implements Initializable{	
	private static final String IMAGE_FOLDER = "/fr/upem/captcha/images/";
	
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
	
	@FXML
	public void handleValidate(ActionEvent evt){
		AppController.validate();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		setMessageListener();
		setSelectedImageListener();
		setShowedImageListener();
	}
	
	private void setMessageListener() {
		AppController.getMessageProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				label.setText(newValue);
			}
		});
	}
	
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
		AppController.getObservableShowedImage().addListener(new ListChangeListener<fr.upem.captcha.model.Image>() {
			@Override
			public void onChanged(Change<? extends fr.upem.captcha.model.Image> change) {
				if(change.getList().size() == AppController.NUMBER_OF_IMAGE_PRINTED) {
					//Get images
					int nbRows = nbRows(); 
					for(int i = 0; i < nbRows; i++) {
						for(int j = 0; j < nbRows; j++) { 
							int index = i*nbRows+j;
							Image image = change.getList().get(index);
							ImageView imageView = new ImageView(IMAGE_FOLDER+image.getFilename());
							imageView.setUserData(image);
							imageView.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<Event>() {
								@Override
								public void handle(Event evt) {
									ImageView clickedImage = (ImageView) evt.getTarget();
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
