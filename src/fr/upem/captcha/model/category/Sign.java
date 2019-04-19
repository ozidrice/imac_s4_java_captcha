package fr.upem.captcha.model.category;

import java.util.ArrayList;
import java.util.Arrays;

import fr.upem.captcha.model.Image;

public class Sign extends Category{
	private static final String NAME = "Panneaux"; //Going right after "Veuillez sélectionner les images qui contiennent..."
	
	//Only for childrens
	protected Sign(String name){
		super(NAME + " " + name);
	}
	
	public Sign() {
		super(NAME);
	}
	
	
}
