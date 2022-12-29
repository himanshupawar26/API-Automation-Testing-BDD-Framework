package stepDefinitions;

import java.io.IOException;

import io.cucumber.java.Before;

public class Hooks {
	
	@Before("@DeletePlace")
	public void beforeScenario() throws IOException {
		// code for place Id
		
		StepDefinition sd = new StepDefinition();
		if(StepDefinition.placeId==null) {
		//spec builder
		sd.add_place_payload("Shivani", "english", "india");
		//post method
		sd.user_calls_using_http_request("AddPlaceAPI", "POST");
		//CREATE AND STORE placeid
		sd.verify_place_id_created_maps_to_usign("Shivani", "getPlaceAPI");
		
		}
	}
}
