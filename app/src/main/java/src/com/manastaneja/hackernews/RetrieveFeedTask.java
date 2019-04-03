package src.com.manastaneja.hackernews;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class RetrieveFeedTask extends AsyncTask<String, Void, String[]> {

    protected StringBuilder content;
    protected String[] content2;
    protected ArrayList<Integer> content3 = new ArrayList<Integer>();

    protected String[] doInBackground(String... urls) {
        try {
            getStories(urls[0] + urls[2]);

            content2 = content.toString().split(", ");
            content2[0] = content2[0].substring(2);
            content2[content2.length - 1] = content2[content2.length - 1].substring(0, content2[content2.length - 1].length() - 2);

            for (String str: content2) {
                content3.add(Integer.parseInt(str));
            }

            MainActivity.setContent3(content3);

        } catch (StoryFailureException e) {
            e.customPrintStackTrace();
        }
        return content2;
    }

    private void getStories(String currentURL) throws StoryFailureException {
        try {
            URL url = new URL(currentURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");

            Reader streamReader;

            if (con.getResponseCode() > 299) {
                streamReader = new InputStreamReader(con.getErrorStream());
                throw new StoryFailureException("Could Not Get Story Feed");
            } else
                streamReader = new InputStreamReader(con.getInputStream());

            BufferedReader in = new BufferedReader(streamReader);

            String inputLine;
            content = new StringBuilder();

            while ((inputLine = in.readLine()) != null)
                content.append(inputLine);
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
