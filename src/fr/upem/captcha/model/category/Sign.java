package fr.upem.captcha.model.category;

public class Sign extends Category{
	private static final String NAME = "Panneaux"; //Going right after "Veuillez s�lectionner les images qui contiennent..."
	
	//Only for childrens
	protected Sign(String name){
		super(NAME + " " + name);
	}
	
	public Sign() {
		super(NAME);
	}
	
	
}
