package restAssured;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runners.MethodSorters;
import restAssuredRequestPojo.Bookingdates;
import restAssuredRequestPojo.createBookingAttributes;
import restAssuredRequestPojo.createBookingResponse;
import runners.Functional;

import static org.junit.Assert.assertEquals;
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Category(Functional.class)
public class TestBookingcreate {

    private String baseURI = "https://restful-booker.herokuapp.com/booking/";
    private static createBookingResponse createResponse;
    private static createBookingAttributes bookingAttributes;
    @Test
    /**@description This method used to invoke create booking API call using POST request
    using RestAssured & validate response */

    public void acreateBooking(){

        Bookingdates date= new Bookingdates("2018-01-01","2019-01-01");
        createBookingAttributes bookingAttributes= new createBookingAttributes("Jim","Brown",111,true,
                date,"Breakfast");
        RestAssured.baseURI = baseURI;
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        request.header("Accept", "application/json");
        Response response = request.body(bookingAttributes).post();
        String resultResponse = response.asString();
        createResponse =response.getBody().as(createBookingResponse.class);
        System.out.println("Created booking using RestAssured and the ID is :"+createResponse.getBookingid());
        System.out.println(resultResponse);

    }
    @Test
    /**@description This method used to invoke partial/patch update booking API using PATCH
    request using RestAssured & validate response*/

    public void bpartialUpdate(){

        RestAssured.baseURI="https://restful-booker.herokuapp.com/";
        RequestSpecification requestToken = RestAssured.given();
        requestToken.header("Content-type", "application/json");
        JSONObject tokenObj =new JSONObject();
        tokenObj.put("username","admin");
        tokenObj.put("password","password123");
        requestToken.body(tokenObj);
        Response tokenResp = requestToken.post("auth");
        JSONObject tokenjsp= (JSONObject) JSONValue.parse(tokenResp.getBody().asString());
        String authToken= (String) tokenjsp.get("token");

        RestAssured.baseURI=baseURI+createResponse.getBookingid();
        RequestSpecification request = RestAssured.given();
        request.header("Accept", "application/json");
        request.header("Content-type","application/json");
        request.header("Cookie","token="+authToken);
        JSONObject partialUpdateObj =new JSONObject();
        partialUpdateObj.put("firstname","FNUpdate");
        partialUpdateObj.put("lastname","LNUpdate");
        request.body(partialUpdateObj);
        Response resp=request.patch();
        assertEquals(200, resp.statusCode());
        bookingAttributes=resp.getBody().as(createBookingAttributes.class);
        System.out.println("Patch Update of FirstName & LastName using RestAssured and details:"+resp.asString());
        System.out.println("LastName after Patch update is:"+bookingAttributes.getLastname());

    }
    @Test
    /**@description This method used to invoke get booking details ByID API using GET
    request with RestAssured & validate response*/

    public void cgetBookingById(){

        RestAssured.baseURI=baseURI+createResponse.getBookingid();
        RequestSpecification request = RestAssured.given();
        request.header("Accept", "application/json");
        Response resp=request.get();
        assertEquals(200, resp.statusCode());
        bookingAttributes=resp.getBody().as(createBookingAttributes.class);
        System.out.println("Booking details of ID "+createResponse.getBookingid()+" using RestAssured:"+resp.asString());
        assertEquals("FNUpdate",bookingAttributes.getFirstname());

    }
    @Test
    /**@description This method used to invoke delete booking API using DELETE
    request with RestAssured & validate response*/

    public void dDeleteBookingById(){

        RestAssured.baseURI=baseURI+createResponse.getBookingid();
        RequestSpecification request = RestAssured.given();
        request.header("Content-type", "application/json");
        request.header("Authorization","Basic YWRtaW46cGFzc3dvcmQxMjM=");
        Response resp=request.delete();
        assertEquals(201, resp.statusCode());
        System.out.println("Deleted Booking using RestAssured & status code is: "+resp.statusCode());
    }

}
