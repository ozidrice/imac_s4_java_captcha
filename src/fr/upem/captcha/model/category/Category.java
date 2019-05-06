package fr.upem.captcha.model.category;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Random;
import javax.naming.SizeLimitExceededException;

import fr.upem.captcha.model.Image;

public abstract  class Category {	
	private String name; //Going right after "Veuillez s�lectionner les images qui contiennent..."
	
	//Can only be created by others categories
	Category(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	/**
	 * @return: Complexity of the Category, incremented by one at each class extends 
	 */
	public int getLevel() {
		Class current = this.getClass();
		int count = 0;
		while(current != null) {
			current = current.getSuperclass();
			count++;
		}
		return count - 2; //Not counting Object and Category
	}

	
	@Override
	/**
	 * @return True if same Name
	 */
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Category other = (Category) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	
	
	
}
