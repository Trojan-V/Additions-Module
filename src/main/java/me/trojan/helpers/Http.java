package me.trojan.helpers;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Http {

    /**
     * @param urlString The URL the GET Request will be sent to.
     * @param requestProperties The request headers added to the connection before it's being sent.
     * @return The response from the GET Request as a String. Will usually return some kind of JSON, as most responses are in JSON format.
     * @throws IOException Thrown if the URL has been malformed or if the connection failed for any (unspecified) reason.
     */
    public static String sendHttpGetRequest(String urlString, HashMap<String, String> requestProperties) throws IOException {
        URL url = new URL(urlString);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setDoInput(true);
        /* Apply request properties to the connection, obtained from the second parameter of this method. */
        for (Map.Entry<String, String> entry : requestProperties.entrySet()) {
            connection.addRequestProperty(entry.getKey(), entry.getValue());
        }
        connection.setRequestMethod("GET");
        InputStream is = connection.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String inputLine;
        while((inputLine = br.readLine()) != null) {
            sb.append(inputLine);
        }
        br.close();
        is.close();
        connection.disconnect();
        return sb.toString();
    }

}
