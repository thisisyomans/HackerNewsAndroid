package src.com.manastaneja.hackernews;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.widget.ScrollView;
import android.widget.TextClock;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    protected static AsyncTask rft;
    protected static AsyncTask rst;
    private static ArrayList<Integer> content3;
    private static String[] storyURLs;
    private static ArrayList<JSONObject> contentJSON;
    private static ScrollView sv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] urls = {"https://hacker-news.firebaseio.com/v0/",
                         "topstories.json?print=pretty",
                         "newstories.json?print=pretty",
                         "beststories.json?print=pretty"};

        sv = (ScrollView) findViewById(R.id.storiesView);

        contentJSON = new ArrayList<JSONObject>();
        storyURLs = new String[500];
        content3 = new ArrayList<Integer>();

        rft = new RetrieveFeedTask(this).execute(urls);
    }

    public static void storyGetter(ArrayList<Integer> intArray) {
        int i = 0;
        for (Integer integ: intArray) {
            storyURLs[i] = ("https://hacker-news.firebaseio.com/v0/item/"+integ+".json?print=pretty");
            i++;
        }
    }

    public static void seeContent3(){
        for (Integer integ: content3) {
            System.out.println("Testing content3: "+integ);
        }
    }

    public static class RetrieveFeedTask extends AsyncTask<String, Void, String[]> {
        protected StringBuilder content;
        protected String[] content2;

        private static WeakReference<MainActivity> activityReference;

        public RetrieveFeedTask(MainActivity activity) {
            activityReference = new WeakReference<>(activity);
        }

        @Override
        protected void onPostExecute(String[] s) {
            //super.onPostExecute(s);
            rst = new RetrieveStoryTask().execute(storyURLs);
            while (rst.getStatus() == Status.PENDING || rst.getStatus() == Status.FINISHED) {
                for (JSONObject obj : contentJSON) {
                    try {
                        CardView cv = new CardView(activityReference.get());
                        TextView tv1 = new TextView(activityReference.get());
                        tv1.setText(obj.getString("title"));
                        cv.addView(tv1);
                        sv.addView(cv);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        @Override
        protected String[] doInBackground(String... urls) {
            try {
                getStories(urls[0] + urls[2]);

                content2 = content.toString().split(", ");
                content2[0] = content2[0].substring(2);
                content2[content2.length - 1] = content2[content2.length - 1].substring(0, content2[content2.length - 1].length() - 2);

                for (String str: content2) {
                    content3.add(Integer.parseInt(str));
                }

                storyGetter(content3);

                //System.out.println(content3.size());

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
                //con.connect();

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

    public static class RetrieveStoryTask extends AsyncTask<String, Void, Integer> {

        protected Integer doInBackground(String... urls) {
            try {
                /*if (rft.getStatus() == Status.FINISHED) {
                    rft.cancel(true);
                }*/
                rft.cancel(true);
                //System.out.println("Stories ArrayList: "+storyURLs.length);
                for (String str: urls){
                    getStoryData(str);
                }
                /*for (Object obj : content) {
                    System.out.println("BIG CHUNGUS: " + obj);
                }*/
                System.out.println("JSON ArrayList: "+contentJSON.size());
                System.out.println("JSON ArrayList Test: "+contentJSON.get(0));
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
                StringBuilder tempJSON = new StringBuilder();
                JSONObject actualJSON;

                while ((inputLine = in.readLine()) != null) {
                    tempJSON.append(inputLine);
                }
                try {
                    actualJSON = new JSONObject(tempJSON.toString());
                    contentJSON.add(actualJSON);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                in.close();
                //System.out.println("CONTENT TO BE PRINTED DIRECTLY AFTER THIS 2");
                //System.out.println(content);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
