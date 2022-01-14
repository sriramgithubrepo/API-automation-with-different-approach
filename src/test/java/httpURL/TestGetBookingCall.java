package httpURL;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import runners.Regression;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


import static org.junit.Assert.assertEquals;
@Category(Regression.class)
public class TestGetBookingCall {
    private static HttpURLConnection connections;

    @Test
    /**@description This method used to invoke get all booking details API using GET
    request with HttpURLConnection & validate response*/

    public void getAllBookings() throws IOException {

        URL url = new URL("https://restful-booker.herokuapp.com/booking/");
        connections = (HttpURLConnection) url.openConnection();
        connections.setRequestMethod("GET");
        assertEquals(200, connections.getResponseCode());
        StringBuffer response = readerMethod(connections.getInputStream());
        System.out.println("All Booking details using httpURLConnection "+ response);

    }

    @Test
    /**@description This method used to invoke get booking details API based on querying firstname
     attribute with HttpURLConnection & validate response*/

    public void getBookingsByFN() throws IOException {

        String fn ="Mark";
        String uri ="?firstname="+fn;
        StringBuilder fulluri = new StringBuilder("https://restful-booker.herokuapp.com/booking");
        fulluri.append(uri);
        URL url = new URL(fulluri.toString());

        connections = (HttpURLConnection) url.openConnection();
        connections.setRequestMethod("GET");
        assertEquals(200, connections.getResponseCode());
        StringBuffer response = readerMethod(connections.getInputStream());
        System.out.println("Booking for User "+fn +"using httpURLConnection"+ response);

    }
    @Test
    /**@description This method used to invoke get booking details API based on querying checkin and
    firstname attribute with HttpURLConnection & validate response*/

    public void getBookingsByCheckInFN() throws IOException {

        String fn ="Mark";
        String checkIn="2015-12-26";
        String uri ="?firstname="+fn+"&"+"checkin="+checkIn;
        StringBuilder fulluri = new StringBuilder("https://restful-booker.herokuapp.com/booking");
        fulluri.append(uri);
        URL url = new URL(fulluri.toString());

        connections = (HttpURLConnection) url.openConnection();
        connections.setRequestMethod("GET");
        assertEquals(200, connections.getResponseCode());
        StringBuffer response = readerMethod(connections.getInputStream());
        System.out.println("Booking for User Mark based on Checkin Date using httpURLConnection" + response);

    }
    @Test
    /**@description This method used to invoke get booking details API based on querying firstname,
    lastname,checkin and checkout attributes with HttpURLConnection & validate response*/

    public void getBookingsMultipleAttribute() throws IOException {

        String fn ="Mark";
        String ln ="Ericsson";
        String checkIn="2018-02-09";
        String checkOut="2019-11-28";
        String uri ="?firstname="+fn+"&"+"lastname="+ln+"&"+"checkin="+checkIn+"&"+"checkout="+checkOut;
        StringBuilder fulluri = new StringBuilder("https://restful-booker.herokuapp.com/booking");
        fulluri.append(uri);
        URL url = new URL(fulluri.toString());

        connections = (HttpURLConnection) url.openConnection();
        connections.setRequestMethod("GET");
        assertEquals(200, connections.getResponseCode());
        StringBuffer response = readerMethod(connections.getInputStream());
        System.out.println("Booking for User Mark based on All 4 parameters using httpURLConnection" + response);

    }
    /**@description This method used to read the response for httpURLConnections call
    and returns as String Buffer*/

    private StringBuffer readerMethod(InputStream inputStream) throws IOException {

        String code;
        StringBuffer response = new StringBuffer();
        BufferedReader rdr = new BufferedReader(new InputStreamReader(inputStream));
        while ((code = rdr.readLine()) != null) {
            response.append(code);
        }
        rdr.close();
        return response;

    }
}
