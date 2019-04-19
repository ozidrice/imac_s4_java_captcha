package fr.upem.captcha.model;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import fr.upem.captcha.model.category.Category;
import fr.upem.captcha.model.category.Sign;

public class Image{
	private enum SavedImageDatas{
		PANNEAU_PRIORITE_DROITE("panneau_priorite_droite.jpg", new Sign()),
		PANNEAU_ROUTE_PRIORITAIRE("panneaux-route-prioritaire.jpg", new Sign()),
		PANNEAU_STOP("panneau_stop.gif", new Sign()),
		PANNEAU_SENS_INTERDIT("panneau-sens-interdit.jpg", new Sign()),
		PANNEAU_CEDER_LE_PASSAGE("panneau_ceder_le_passage.jpg", new Sign()),
		PANNEAU_TAS("panneaux-routierse-854x492.png", new Sign()),
		PANNEAU_AMERICAIN("panneau-routier-usa-01.jpg", new Sign()),
		PANNEAU_4MILES("4miles.jpg", new Sign()),
		PANNEAU_NEWYORK("NYsigns.jpg", new Sign()),
		PHOTOGRAPHIE_APPAREIL_PHOTO("toomanypics.jpg"),
		RANDOM1("téléchargement.jpg"),
		RANDOM2("13425a41957e98049d5cec145da06c69-pays-de-lorient-il-photographie-les-pollutions-sur-nos-plages.jpg"),
		RANDOM3("photographe-mariage-paris-30.jpg"),
		RANDOM4("hg08-tt-width-836-height-550-fill-1-bgcolor-000000.jpg"),
		RANDOM6("Livingtodie3of11.jpg"),
		RANDOM7("photographie-industrielle-fours-carbone-usine.jpg"),
		;
		
		private final String filename;
		private final ArrayList<Category> categories;
		
		private SavedImageDatas(String filename, Category...categories) {
			this.filename = filename;
			this.categories = new ArrayList<Category>(Arrays.asList(categories));
		}
		
		@Override
		public String toString() {
			return "SavedImageDatas [filename=" + filename + "]";
		}
	}
	
	private final String filename;
	private final ArrayList<Category> categories;
	
	private Image(SavedImageDatas imageFile) {
		this.filename = imageFile.filename;
		this.categories = imageFile.categories;
	}
	
	public String getFilename() {
		return this.filename;
	}
	
	public static Image random() {
		Random rand = new Random();
		SavedImageDatas[] list = SavedImageDatas.values();
		return new Image(list[rand.nextInt(list.length)]);
	}
	
	public static ArrayList<Image> random(int numberOfImages, Category category) {
		return random(numberOfImages, category, null);
	}
	
	public static ArrayList<Image> random(int numberOfImages, Category category, Category except) {
		if(numberOfImages < 0)
			throw new InvalidParameterException("number be cannot be negative, number given : " + Integer.toString(numberOfImages));
		
		//Getting file of the category and removing except images
		List<SavedImageDatas> listImage = new ArrayList<SavedImageDatas>(Arrays.asList(SavedImageDatas.values()));
		if(category != null)
			listImage = listImage.stream().filter(imageFile -> imageFile.categories.contains(category)).collect(Collectors.toList());			
		
		//Removing except images
		if(except != null)
			listImage = listImage.stream().filter(imageFile-> !imageFile.categories.contains(except)).collect(Collectors.toList());
		
		
		if(listImage.size() < numberOfImages)
			throw new InvalidParameterException("Not enough Image values to get (" + numberOfImages + ") elements");
		
		//Get elements
		Random rand = new Random();
		ArrayList<Image> result = new ArrayList<Image>();
		System.out.println();
		while(result.size() < numberOfImages) {
			int random_i = rand.nextInt(listImage.size());
			SavedImageDatas randomImage = listImage.get(random_i);
			if(category == null || randomImage.categories.contains(category))
				result.add(new Image(randomImage));
			listImage.remove(random_i);
		}
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Image other = (Image) obj;
		if (categories == null) {
			if (other.categories != null)
				return false;
		} else if (!categories.equals(other.categories))
			return false;
		if (filename == null) {
			if (other.filename != null)
				return false;
		} else if (!filename.equals(other.filename))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Image [filename=" + filename + "]";
	}
	
	
}
