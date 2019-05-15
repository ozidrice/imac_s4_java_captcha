package fr.upem.captcha.images;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.upem.captcha.images.animaux.Animaux;
import fr.upem.captcha.images.panneaux.Panneaux;

public enum ImageLvl1 implements ImageLvl{
	Panneaux(Panneaux.class),
	Animaux(Animaux.class);
	
	
	private Class<? extends Image> imageClass;
	private ImageLvl1(Class<? extends Image> imageClass) {
		this.imageClass = imageClass;
	}
	
	@Override
	public Class<? extends Image> getImageClass() {
		return this.imageClass;
	}
	
	public static List<Class<? extends Image>> getAllClass() {
		List<Class<? extends Image>> list = new ArrayList<Class<? extends Image>>();
		Arrays.asList(ImageLvl1.values()).stream().forEach(img -> list.add(img.imageClass));
		return list;
	}	
}
