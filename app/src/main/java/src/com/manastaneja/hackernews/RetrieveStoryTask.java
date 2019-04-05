package src.com.manastaneja.hackernews;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class RetrieveStoryTask extends AsyncTask<String, Void, Integer> {

    protected ArrayList content = new ArrayList();

    protected Integer doInBackground(String... urls) {
        try {
            for (String str: urls){
                getStoryData(str);
            }
            for (Object obj : content) {
                System.out.println("BIG CHUNGUS: " + obj);
            }
            //MainActivity.setContentJSON(content);
            return 0;
        } catch (StoryFailureException e) {
            e.customPrintStackTrace();
            return 1;
        }
    }

    private void getStoryData(String currentURL) throws StoryFailureException {
        try {
            URL url = new URL(currentURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");

            Reader streamReader;

            if (con.getResponseCode() > 299) {
                streamReader = new InputStreamReader(con.getErrorStream());
                throw new StoryFailureException("Could Not Get Story Data");
            } else
                streamReader = new InputStreamReader(con.getInputStream());

            BufferedReader in = new BufferedReader(streamReader);

            String inputLine;

            while ((inputLine = in.readLine()) != null)
                content.add(inputLine);
            in.close();
            System.out.println("CONTENT TO BE PRINTED DIRECTLY AFTER THIS");
            System.out.println(content);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
