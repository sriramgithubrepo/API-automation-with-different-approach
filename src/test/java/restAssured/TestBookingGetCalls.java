package restAssured;


import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import runners.Functional;
import runners.Regression;

@Category(Regression.class)
public class TestBookingGetCalls {
    private String baseURI="https://restful-booker.herokuapp.com/booking/";

    @Test
    /**@description This method used to invoke get all booking details API using GET
    request using RestAssured & validate response*/

    public void getAllBookings(){

        RestAssured.baseURI = baseURI;
        RequestSpecification request = RestAssured.given();
        Response response = request.get();
        JSONArray allBookingsJSObj = (JSONArray) JSONValue.parse(response.getBody().asString());
        System.out.println("All booking IDs :"+allBookingsJSObj);
        System.out.println("Total Booking Count is: "+allBookingsJSObj.size());

    }
    @Test
    /**@description This method used to invoke get booking details API based on querying firstname
    attribute using RestAssured & validate response*/

    public void getBookingByFN(){

        RestAssured.baseURI = baseURI;
        RequestSpecification request = RestAssured.given();
        Response response = request.queryParam("firstname", "Mark").get();
        JSONArray bookingsFNJSObj = (JSONArray) JSONValue.parse(response.getBody().asString());
        System.out.println("Booking IDs with FirstName Mark:"+bookingsFNJSObj);
        System.out.println("Total Booking Count is: "+bookingsFNJSObj.size());

    }
    @Test
    /**@description This method used to invoke get booking details API based on querying checkin and
    firstname attribute using RestAssured & validate response*/

    public void getBookingByCheckInFN(){

        RestAssured.baseURI = baseURI;
        RequestSpecification request = RestAssured.given();
        Response response = request.queryParam("firstname", "Susan").
                queryParam("checkin", "2015-12-26").get();
        JSONArray bookingsCheckInFnJSObj = (JSONArray) JSONValue.parse(response.getBody().asString());
        System.out.println("Booking IDs with FirstName & Checkin Date:"+bookingsCheckInFnJSObj);
        System.out.println("Total Booking Count is: "+bookingsCheckInFnJSObj.size());

    }
    @Test
    /**@description This method used to invoke get booking details API based on querying firstname,
    lastname,checkin and checkout attributes using RestAssured & validate response*/

    public void getBookingByAllAttributes(){

        RestAssured.baseURI = baseURI;
        RequestSpecification request = RestAssured.given();
        Response response = request.queryParam("firstname", "Mark").
                queryParam("lastname", "Ericsson").
                queryParam("checkin", "2013-12-26").
                queryParam("checkout", "2019-11-28").get();
        JSONArray bookingsAllAtrributeJSObj = (JSONArray) JSONValue.parse(response.getBody().asString());
        System.out.println("Booking IDs with All 4 search attributes:"+bookingsAllAtrributeJSObj);
        System.out.println("Total Booking Count is: "+bookingsAllAtrributeJSObj.size());

    }
}
