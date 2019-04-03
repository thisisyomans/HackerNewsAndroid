package src.com.manastaneja.hackernews;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private AsyncTask at;
    protected RetrieveFeedTask rft;
    private static ArrayList<Integer> content3;
    private static ArrayList contentJSON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] urls = {"https://hacker-news.firebaseio.com/v0/",
                         "topstories.json?print=pretty",
                         "newstories.json?print=pretty",
                         "beststories.json?print=pretty"};

        content3 = new ArrayList<Integer>();
        new RetrieveFeedTask().execute(urls);
    }

    public void storyGetter(ArrayList<Integer> intArray) {
        String[] storyURLs = new String[50];

        int i = 0;
        for (Integer integ: intArray) {
            storyURLs[i] = ("https://hacker-news.firebaseio.com/v0/item/"+integ+".json?print=pretty");
            i++;
        }

        new RetrieveFeedTask().execute(storyURLs);
    }

    public static void setContentJSON (ArrayList arrayList) {
        contentJSON = arrayList;
    }

    public static void setContent3(ArrayList<Integer> intArray){
        content3 = intArray;
    }
}
