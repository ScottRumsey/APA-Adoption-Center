package srumsey.apa;

public class PetListElement {

	String name;
	String url;
	String img;
	
	public PetListElement(String pet,String page,String imageLink){
		name = pet;
		url = page;
		img = imageLink;
	}
	
	public String getName(){
		return name;
	}
	public String getURL(){
		return url;
	}
	public String getImgURL(){
		return img;
	}

}
