package fr.upem.captcha.images;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;


abstract public class Image {
	private static final List<Class<? extends Image>> IMAGE_CLASS_LIST = Stream.concat(ImageLvl1.getAllClass().stream(), ImageLvl2.getAllClass().stream()).collect(Collectors.toList());
	private static final List<URL> URL_LIST = getURLFromClass(IMAGE_CLASS_LIST);
	private final List<URL> photos;
	private final String name;

	//Only accessible from childs class because of the abstract
	public Image(String name) {
		this.photos = getImagesFromFolder("src/" + this.getClass().getPackageName().replace('.', '/'));
		this.name = name;
	}
	
	private static List<URL> getURLFromClass(List<Class<? extends Image>> imageClassList){
		List<URL> url = new ArrayList<URL>();
		for (Class<? extends Image> imageClass : imageClassList) {
			try {
				Image image;
				image = imageClass.getDeclaredConstructor().newInstance();
				url.addAll(image.getPhotos());
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
		}
		return url;
	}

	public static URL getRandomURL(List<URL> except) {
		URL url = null;
		Random rand = new Random();
		List<URL> tmpURL_LIST = new ArrayList<URL>(URL_LIST);
		
		while(url == null && tmpURL_LIST.size() != 0) {
			int i = rand.nextInt(tmpURL_LIST.size());
			if(except.contains(tmpURL_LIST.get(i))) {
				tmpURL_LIST.remove(i);
			}else {
				url = tmpURL_LIST.get(i);
			}
		}
		
		return url;
	}
	
	public static List<URL> getRandomURL(Image except, int count){
		List<URL> list = new ArrayList<URL>();
		List<URL> cpyExcept = new ArrayList<URL>(except.getPhotos());
		for (int i = 0; i < count; i++) {
			URL url = getRandomURL(cpyExcept);
			list.add(url);
			cpyExcept.add(url);
		}
		return list;
	}
	
	private static List<URL> getImagesFromFolder(String folder){
		List<URL> response = new ArrayList<URL>();
		
		File loadedfolder = new File(folder);
		if (loadedfolder.isDirectory()) {
			for (final File f : loadedfolder.listFiles()) {
				if(f.isDirectory()) {
					response.addAll(getImagesFromFolder(folder + "/" + f.getName()));
				}
				else {
					try {
						URL url = f.toURI().toURL();
						if((f.getName().endsWith(".jpg") || f.getName().endsWith(".png")) && !response.contains(url))
							response.add(url);
					} catch (MalformedURLException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return response;
	}
	
	
	public List<URL> getPhotos() {
		return this.photos;
	}
	
	public List<URL> getRandomPhotosURL(int returnedListSize){
		if(returnedListSize < 0)
			throw new InvalidParameterException("returnedListSize cannot be negative, number given : " + Integer.toString(returnedListSize));		
		if(photos.size() < returnedListSize)
			throw new InvalidParameterException("Not enough photos to get (" + returnedListSize + ") elements");
		
		//Get elements
		List<URL> result = new ArrayList<URL>();
		List<URL> photos_cpy = new ArrayList<URL>(this.photos);
		Random rand = new Random();
		
		while(result.size() < returnedListSize) {
			int random_i = rand.nextInt(photos_cpy.size());
			result.add(photos_cpy.get(random_i));
			photos_cpy.remove(random_i);
		}
		return result;
	}
	
	public URL getRandomPhotoURL(){
		Random rand = new Random();
		return this.photos.get(
				rand.nextInt(this.photos.size())
				);
	}
	
	public String getName() {
		return this.name;
	}
	
	
}
