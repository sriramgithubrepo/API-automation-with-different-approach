package httpURL;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runners.MethodSorters;
import runners.Functional;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Category(Functional.class)
public class TestCreateBooking {

    private static HttpURLConnection connections;
    private static String bookingid;
    private static String authToken;
    /**
     * @description: This allowMethod is used as an workaround to make PATCH request
     with HttpURLConnection*/
    private static void allowMethods(String... methods) throws NoSuchFieldException, IllegalAccessException {

        Field methodsField = HttpURLConnection.class.getDeclaredField("methods");
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(methodsField, methodsField.getModifiers() & ~Modifier.FINAL);
        methodsField.setAccessible(true);
        String[] oldMethods = (String[]) methodsField.get(null);
        Set<String> methodsSet = new LinkedHashSet<String>(Arrays.asList(oldMethods));
        methodsSet.addAll(Arrays.asList(methods));
        String[] newMethods = methodsSet.toArray(new String[0]);
        methodsField.set(null/*static field*/, newMethods);

    }

    @Test
    /**@description This method used to invoke create booking API call using POST request
    with HttpURLConnection & validate response */

    public void aCreateBooking() throws IOException {

        URL url = new URL("https://restful-booker.herokuapp.com/booking/");
        connections = (HttpURLConnection) url.openConnection();
        connections.setDoOutput(true);
        connections.setRequestMethod("POST");
        connections.setRequestProperty("Accept", "application/json");
        connections.setRequestProperty("Content-type", "application/json");
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
        OutputStream ops= connections.getOutputStream();
        ops.write(reqBody.getBytes());
        ops.flush();

        StringBuffer response = readerMethod(connections.getInputStream());
        JSONObject jobj = (JSONObject) JSONValue.parse(response.toString());
        System.out.println("Created booking using HttpURLConnections and details are: "+response);
        bookingid=jobj.get("bookingid").toString();
        System.out.println("Booking ID is: "+bookingid);

    }

    @Test
    /**@description This method used to invoke token generation API call using POST request
    with HttpURLConnection & generate token */

    public void bTokenGeneration() throws IOException{

        URL url = new URL("https://restful-booker.herokuapp.com/auth");
        connections = (HttpURLConnection) url.openConnection();
        connections.setDoOutput(true);
        connections.setRequestMethod("POST");
        connections.setRequestProperty("Content-type", "application/json");
        String tokenBody = "{\n" +
                "    \"username\" : \"admin\",\n" +
                "    \"password\" : \"password123\"\n" +
                "}";
        OutputStream ops= connections.getOutputStream();
        ops.write(tokenBody.getBytes());
        ops.flush();

        StringBuffer response = readerMethod(connections.getInputStream());
        JSONObject jobj = (JSONObject) JSONValue.parse(response.toString());
        authToken =  jobj.get("token").toString();

    }

    @Test

    /**@description This method used to invoke partial/patch update booking API using PATCH
    request(with help of workaround method created above)in HttpURLConnection & validate response*/

    public void cPartialUpdate() throws IOException, NoSuchFieldException, IllegalAccessException {

        allowMethods("PATCH");
        URL urlpartialupdate = new URL("https://restful-booker.herokuapp.com/booking/"+bookingid);
        String cookie = "token=" + authToken;
        connections = (HttpURLConnection) urlpartialupdate.openConnection();
        connections.setRequestMethod("PATCH");
        connections.setDoOutput(true);
        connections.setRequestProperty("Accept", "application/json");
        connections.setRequestProperty("Content-type", "application/json");
        connections.setRequestProperty("Cookie", cookie);
        String partialUpdateBody = "{\n" +
                "    \"firstname\" : \"FNUpdate\",\n" +
                "    \"lastname\" : \"LNUpdate\"\n" +
                "}";
        OutputStream outputs= connections.getOutputStream();
        outputs.write(partialUpdateBody.getBytes());
        outputs.flush();

        StringBuffer responsePartialUpdate = readerMethod(connections.getInputStream());
        JSONObject jobjpartialupdate = (JSONObject) JSONValue.parse(responsePartialUpdate.toString());
        System.out.println("Patch update of FirstName & LastName using HttpURLConnections and details are:");
        System.out.println(responsePartialUpdate);
        System.out.println("FirstName for bookingID " + bookingid + " is " + jobjpartialupdate.get("firstname"));

    }

    @Test
    /**@description This method used to invoke get booking details ByID API using GET
    request with HttpURLConnection & validate response*/

    public void dgetBookingById() throws IOException {

        URL url = new URL("https://restful-booker.herokuapp.com/booking/"+bookingid);
        connections = (HttpURLConnection) url.openConnection();
        connections.setRequestMethod("GET");
        connections.setRequestProperty("Accept", "application/json");
        assertEquals(200, connections.getResponseCode());

        StringBuffer response = readerMethod(connections.getInputStream());
        JSONObject jobjGetBookingById = (JSONObject) JSONValue.parse(response.toString());
        assertEquals("FNUpdate",jobjGetBookingById.get("firstname"));
        System.out.println("Booking details for booking id :" +bookingid);
        System.out.println(jobjGetBookingById);

    }

    @Test
    /**@description This method used to invoke delete booking API using DELETE
    request with HttpURLConnection & validate response*/

    public void edeleteBooking() throws IOException {
        URL url = new URL("https://restful-booker.herokuapp.com/booking/"+bookingid);
        connections = (HttpURLConnection) url.openConnection();
        connections.setRequestMethod("DELETE");
        connections.setRequestProperty("Content-type", "application/json");
        connections.setRequestProperty("Authorization","Basic YWRtaW46cGFzc3dvcmQxMjM=");
        assertEquals(201, connections.getResponseCode());
        System.out.println("Deleted Booking using httpURLConnection and status code is: "+connections.getResponseCode());

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
