package resources;

import java.util.ArrayList;
import java.util.List;

import pojo.AddPlace;
import pojo.Location;

public class TestDataBuild {
	
	public AddPlace addPlacePayload(String name, String lang, String addr) {
		
		AddPlace ap = new AddPlace();
		ap.setAccuracy(50);
		ap.setAddress(addr);
		ap.setLanguage(lang);
		ap.setWebsite("http://google.com");
		ap.setName(name);
		ap.setPhone_number("(+91) 983 893 3937");
		
		List<String> typesList = new ArrayList<String>();
		typesList.add("shoe park");
		typesList.add("shop");
		ap.setTypes(typesList);
		
		Location lobject= new Location();
		lobject.setLat(-38.383494);
		lobject.setLng(33.427362);
		ap.setLocation(lobject);
		
		return ap;
	}
	
	public String deletePlacePayload(String placeId) {
		return "\r\n"
				+ "    \"place_id\":\""+placeId+"\"\r\n"
				+ "}\r\n"
				+ "";
	}
}
