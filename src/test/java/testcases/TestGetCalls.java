package testcases;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import runners.Functional;
import runners.Regression;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.Assert.assertEquals;

@Category(Regression.class)
public class TestGetCalls {
    private String baseURI = "https://restful-booker.herokuapp.com/booking/";

    @Test
    /**@description This method used to invoke get all booking details API using GET
    request with apache HttpClient & validate response*/

    public void getAllBookings() throws IOException {

        HttpGet request = new HttpGet(baseURI);
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
        String responseString = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
        JSONArray array = (JSONArray) JSONValue.parse(responseString);
        System.out.println("All Bookings using apache HttpClient:" + array);
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        assertEquals(200, statusCode);

    }

    @Test
    /**@description This method used to invoke get booking details API based on querying firstname
    attribute with apache HttpClient & validate response*/

    public void getBookingByFirstName() throws IOException,URISyntaxException {

        URIBuilder builder = new URIBuilder(baseURI);
        builder.setParameter("firstname", "Mark");
        HttpGet getReq = new HttpGet(builder.build());

        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(getReq);
        String responseString = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
        JSONArray array = (JSONArray) JSONValue.parse(responseString);
        System.out.println("Booking IDs based on FirstName using HttpClient:" + array);
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        assertEquals(200, statusCode);

    }

    @Test
    /**@description This method used to invoke get booking details API based on querying checkin and
    firstname attribute with apache HttpClient & validate response*/

    public void getBookingByCheckInFirstName() throws IOException, URISyntaxException {

        URIBuilder builder = new URIBuilder(baseURI);
        builder.setParameter("checkin", "2015-12-26");
        builder.setParameter("firstname", "Susan");

        HttpGet getReq = new HttpGet(builder.build());
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(getReq);
        String responseString = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
        JSONArray array = (JSONArray) JSONValue.parse(responseString);
        System.out.println("Booking IDs based on Checkin & FirstName using HttpClient:" + array);
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        assertEquals(200, statusCode);

    }

    @Test
    /**@description This method used to invoke get booking details API based on querying firstname,
    lastname,checkin and checkout attributes with apache HttpClient & validate response*/

    public void getBookingByMultipleAttribute() throws IOException, URISyntaxException {

        URIBuilder builder = new URIBuilder(baseURI);
        builder.setParameter("firstname", "Mark");
        builder.setParameter("lastname", "Ericsson");
        builder.setParameter("checkin", "2013-02-09");
        builder.setParameter("checkout", "2019-11-28");

        HttpGet getReq = new HttpGet(builder.build());
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(getReq);
        String responseString = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
        JSONArray array = (JSONArray) JSONValue.parse(responseString);
        System.out.println("Booking IDs based on All 4 parameters using HttpClient:" + array);
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        assertEquals(200, statusCode);

    }
}
