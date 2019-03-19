package src.com.manastaneja.hackernews;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    protected String baseURL;
    protected String topStoriesURL;
    protected String newStoriesURL;
    protected String bestStoriesURL;
    protected StringBuilder content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        baseURL = "https://hacker-news.firebaseio.com/v0/";
        topStoriesURL = "topstories.json?print=pretty";
        newStoriesURL = "newstories.json?print=pretty";
        bestStoriesURL = "beststories.json?print=pretty";

        try {
            URL url = new URL(baseURL + topStoriesURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            if (con.getHeaderField("Content-Type").equals("application/json")) {
                Reader streamReader;

                if (con.getResponseCode() > 299) {
                    streamReader = new InputStreamReader(con.getErrorStream());
                } else {
                    streamReader = new InputStreamReader(con.getInputStream());
                }

                BufferedReader in = new BufferedReader(streamReader);

                String inputLine;
                content = new StringBuilder();

                while ((inputLine = in.readLine()) != null)
                    content.append(inputLine);
                in.close();
                System.out.println("CONTENT TO BE PRINTED DIRECTLY AFTER THIS");
                System.out.println(content);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
