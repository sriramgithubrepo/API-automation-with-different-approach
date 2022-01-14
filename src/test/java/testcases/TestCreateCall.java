package testcases;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runners.MethodSorters;
import runners.Functional;
import runners.Regression;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
@Category(Functional.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestCreateCall {
    private static String bookingId;
    private String baseURI = "https://restful-booker.herokuapp.com/booking/";

    @Test
    /**@description This method used to invoke create booking API call using POST request
    with apache HttpClient & validate response */

    public void aCreateBooking() throws IOException {

        HttpPost postReq = new HttpPost(baseURI);
        String reqBody = "{\"firstname\" : \"Jim\",\n" +
                "    \"lastname\" : \"Brown\",\n" +
                "    \"totalprice\" : 111,\n" +
                "    \"depositpaid\" : true,\n" +
                "    \"bookingdates\" : {\n" +
                "        \"checkin\" : \"2018-01-01\",\n" +
                "        \"checkout\" : \"2019-01-01\"\n" +
                "    },\n" +
                "    \"additionalneeds\" : \"Breakfast\"\n" +
                "}";
        StringEntity reqBodyEntity = new StringEntity(reqBody);
        postReq.setEntity(reqBodyEntity);
        postReq.setHeader("Accept", "application/json");
        postReq.setHeader("Content-type", "application/json");
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(postReq);

        int statusCode = httpResponse.getStatusLine().getStatusCode();
        assertEquals(200, statusCode);
        String responseString = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
        System.out.println("Created Booking using Apache HttpClient and details below");
        System.out.println(responseString);
        JSONObject jsBooking = (JSONObject) JSONValue.parse(responseString);
        bookingId = jsBooking.get("bookingid").toString();
        System.out.println("Booking ID is: "+bookingId);
        JSONObject booking = (JSONObject) jsBooking.get("booking"); //as booking details are under booking ohject
        System.out.println("FirstName for created booking: "+booking.get("firstname").toString());
        assertTrue(jsBooking.containsKey("bookingid"));

    }

    @Test
    /**@description This method used to invoke partial/patch update booking API using PATCH
    request in apache HttpClient & validate response*/

    public void bPartialUpdate() throws IOException {

        HttpPost tokenPost = new HttpPost("https://restful-booker.herokuapp.com/auth");
        String tokenBody = "{\n" +
                "    \"username\" : \"admin\",\n" +
                "    \"password\" : \"password123\"\n" +
                "}";
        StringEntity reqTokenBodyEntity = new StringEntity(tokenBody);
        tokenPost.setEntity(reqTokenBodyEntity);
        tokenPost.setHeader("Content-type", "application/json");
        HttpResponse httpTokenResponse = HttpClientBuilder.create().build().execute(tokenPost);

        String responseTokenString = EntityUtils.toString(httpTokenResponse.getEntity(), "UTF-8");
        System.out.println("Token generated and will be used in Patch: "+responseTokenString);
        JSONObject respJSON = (JSONObject) JSONValue.parse(responseTokenString);
        String authToken = (String) respJSON.get("token");

        HttpPatch updatePatch = new HttpPatch(baseURI + bookingId);
        String reqBody = "{\n" +
                "    \"firstname\" : \"FNUpdate\",\n" +
                "    \"lastname\" : \"LNUpdate\"\n" +
                "}";
        StringEntity reqBodyEntity = new StringEntity(reqBody);
        updatePatch.setEntity(reqBodyEntity);
        updatePatch.setHeader("Accept", "application/json");
        updatePatch.setHeader("Content-type", "application/json");
        updatePatch.setHeader("Cookie", "token=" + authToken);

        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(updatePatch);
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        assertEquals(200, statusCode);
        String responseString = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
        System.out.println("Patch update of First & Last Name using Apache HttpClient & details below");
        System.out.println(responseString);
        JSONObject respPartialUpdate = (JSONObject) JSONValue.parse(responseString);
        System.out.println("LastName after patch update for bookingID " + bookingId + " is " + respPartialUpdate.get("lastname"));

    }

    @Test
    /**@description This method used to invoke get booking details ByID API using GET
    request with apache HttpClient & validate response*/

    public void cgetBookingById() throws IOException {

        HttpGet getReq = new HttpGet(baseURI + bookingId);
        getReq.setHeader("Accept", "application/json");
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(getReq);

        int statusCode = httpResponse.getStatusLine().getStatusCode();
        assertEquals(200, statusCode);
        String respBody = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
        JSONObject respJson = (JSONObject) JSONValue.parse(respBody);
        assertEquals("FNUpdate",respJson.get("firstname"));
        System.out.println("Booking details based on bookingID " + bookingId + " is " + respJson);
        System.out.println("FirstName for bookingID " + bookingId + " is " + respJson.get("firstname"));

    }

    @Test
    /**@description This method used to invoke delete booking API using DELETE
    request with apache HttpClient & validate response*/

    public void dDeleteBookingById() throws IOException {

        HttpDelete delete = new HttpDelete(baseURI + bookingId);
        delete.setHeader("Content-type", "application/json");
        delete.setHeader(HttpHeaders.AUTHORIZATION, "Basic YWRtaW46cGFzc3dvcmQxMjM=");
        HttpResponse httpDeleteResponse = HttpClientBuilder.create().build().execute(delete);
        int statusCode = httpDeleteResponse.getStatusLine().getStatusCode();
        System.out.println("Deleted Booking using Apache HttpClient and status code is: "+statusCode);
        assertEquals(201, statusCode);

    }
}
