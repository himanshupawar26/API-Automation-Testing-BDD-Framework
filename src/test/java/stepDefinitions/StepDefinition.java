package stepDefinitions;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import pojo.AddPlace;
import pojo.Location;
import resources.APIResource_enum;
import resources.TestDataBuild;
import resources.Utils;

public class StepDefinition extends Utils {
	RequestSpecification res;
	ResponseSpecification resp;
	Response response;
	TestDataBuild tdb = new TestDataBuild();
	static String placeId; //static because its value in 1st scenario  needs to be used for 2nd scenario 
	
	@Given("Add place payload {string} {string} {string}")
	public void add_place_payload(String name, String lang, String addr) throws IOException {
		
		 res = given().spec(requestSpecification())
				.body(tdb.addPlacePayload(name,lang,addr));
		
	}
	
	@When("user calls {string} using {string} http request")
	public void user_calls_using_http_request(String resource,String method) {
	    
		APIResource_enum resourceAPI = APIResource_enum.valueOf(resource);
		System.out.println(resourceAPI.getResource());
		
		ResponseSpecification resp = new ResponseSpecBuilder().expectStatusCode(200)
				.expectContentType(ContentType.JSON).build();
		
		if(method.equalsIgnoreCase("POST"))
		{ response = res.when().post(resourceAPI.getResource());
		}else if(method.equalsIgnoreCase("GET")) {
			response = res.when().get(resourceAPI.getResource());
		}
	}
	
	@Then("the API call is success with status code {int}")
	public void the_api_call_is_success_with_status_code(Integer int1) {
	   assertEquals(response.getStatusCode(),200);
	}
	
	@Then("{string} in response is {string}")
	public void in_response_is(String string1, String string2) {
	    // Write code here that turns the phrase above into concrete actions
	    
	    assertEquals(getJsonPath(response, string1),string2);   
	}
	
	@Then("verify place_id created maps to {string} usign {string}")
	public void verify_place_id_created_maps_to_usign(String expName, String resource) throws IOException {
	    
		// PREPARE REQUEST SPEC
		placeId = getJsonPath(response, "place_id");
		res = given().spec(requestSpecification()).queryParam("place_id",placeId);
		user_calls_using_http_request(resource, "GET");
		String actName = getJsonPath(response, "name");
		
		assertEquals(actName,expName);
		
	}
	
	@Given("DeletePlace Payload")
	public void delete_place_payload() throws IOException {
	    // Write code here that turns the phrase above into concrete actions
	    
		res = given().spec(requestSpecification()).body(tdb.deletePlacePayload(placeId));
		
		
	}


}
