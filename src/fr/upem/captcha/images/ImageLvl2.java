package fr.upem.captcha.images;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.upem.captcha.images.animaux.chats.Chats;
import fr.upem.captcha.images.animaux.chiens.Chiens;

public enum ImageLvl2 implements ImageLvl{
	Chats(Chats.class),
	Chien(Chiens.class);
	
	
	private Class<? extends Image> imageClass;
	private ImageLvl2(Class<? extends Image> imageClass) {
		this.imageClass = imageClass;
	}
	
	@Override
	public Class<?extends Image> getImageClass(){
		return this.imageClass;
	}
	
	public static List<Class<? extends Image>> getAllClass() {
		List<Class<? extends Image>> list = new ArrayList<Class<? extends Image>>();
		Arrays.asList(ImageLvl2.values()).stream().forEach(img -> list.add(img.imageClass));
		return list;
	}
}
