Feature: Validationg Place API's

@AddPlace @Regression
Scenario Outline: Verify place added successfully using AddPlaceAPI
Given Add place payload "<name>" "<language>" "<address>"
When user calls "AddPlaceAPI" using "POST" http request
Then the API call is success with status code 200
And "status" in response is "OK"
And "scope" in response is "APP"
And verify place_id created maps to "<name>" usign "getPlaceAPI"


Examples:
	|name     | language| address |
#	|ShiuHouse| English | Amravati|
	|Shivani  | Marathi | Nagpur  |
#	|Shiu     | Hindi   | MH      |

@DeletePlace @Regression
Scenario: Verify if deletePlace API working
Given DeletePlace Payload
When user calls "deletePlaceAPI" using "POST" http request	
Then the API call is success with status code 200

