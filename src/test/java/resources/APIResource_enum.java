package resources;

public enum APIResource_enum {
	
	AddPlaceAPI("/maps/api/place/add/json"),
	getPlaceAPI("/maps/api/place/get/json"),
	deletePlaceAPI("/maps/api/place/delete/json");

	private String resource;
	
	APIResource_enum(String resource) {
		// TODO Auto-generated constructor stub
		this.resource= resource;
	}
	
	public String getResource() {
		return resource;
	}
	
}
