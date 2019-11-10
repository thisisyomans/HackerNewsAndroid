package src.com.manastaneja.hackernews;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.view.Gravity;
import android.graphics.Color;
import android.view.ViewGroup;
import android.view.View;
import android.graphics.Typeface;
import android.content.Intent;
import android.net.Uri;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static String[] urls;
    private static ArrayList<Integer> content3;
    private static String[] storyURLs;
    private ScrollView sv;
    private LinearLayout ll;
    private RequestQueue mRequestQueue;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();

        mRequestQueue = Volley.newRequestQueue(context);

        urls = new String[]{"https://hacker-news.firebaseio.com/v0/",
                "topstories.json?print=pretty",
                "newstories.json?print=pretty",
                "beststories.json?print=pretty"};

        sv = (ScrollView) findViewById(R.id.storiesView);
        ll = (LinearLayout) findViewById(R.id.linLay);
        ll.setPadding(25, 0, 25, 0);

        storyURLs = new String[500];
        content3 = new ArrayList<Integer>();

        //rft = new RetrieveFeedTask().execute(urls);
        getStoryIndices();
    }

    public static void storyLinkMaker(ArrayList<Integer> intArray) {
        int i = 0;
        for (Integer integ: intArray) {
            storyURLs[i] = ("https://hacker-news.firebaseio.com/v0/item/"+integ+".json?print=pretty");
            i++;
        }
    }

    /*public static void seeContent3(){
        for (Integer integ: content3) {
            System.out.println("Testing content3: "+integ);
        }
    }*/

    private void getStoryIndices() {

        StringRequest mStringRequest = new StringRequest(Request.Method.GET, urls[0] + urls[2], new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //System.out.println("ARRAY OF STORY NUMBERS: " + response);

                String[] content2;
                content2 = response.split(", ");
                content2[0] = content2[0].substring(2);
                content2[content2.length - 1] = content2[content2.length - 1].substring(0, content2[content2.length - 1].length() - 3);

                for (String str: content2) {
                    content3.add(Integer.parseInt(str));
                }

                storyLinkMaker(content3);

                //System.out.println("ARRAY OF STORY URLS: " + storyURLs.length);
                getStories();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error :" + error.toString());
            }
        });

        mRequestQueue.add(mStringRequest);
    }

    private void getStories() {
        for (String url : storyURLs) {
            JsonObjectRequest mJsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(final JSONObject jsonResponse) {
                    //System.out.println(response.toString());
                    try {
                        CardView cv = new CardView(getApplicationContext());
                        cv.setCardBackgroundColor(0x0000FF00);
                        cv.setContentPadding(25, 50, 25, 50);
                        cv.setCardElevation(4);
                        //cv.setPadding(20, 50, 20, 50);

                        LinearLayout.LayoutParams cardViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        cv.setLayoutParams(cardViewParams);
                        ViewGroup.MarginLayoutParams cardViewMarginParams = (ViewGroup.MarginLayoutParams) cv.getLayoutParams();
                        cardViewMarginParams.setMargins(0, 30, 0, 30);
                        cv.requestLayout();  //Dont forget this line

                        TextView tv1 = new TextView(getApplicationContext());
                        if (jsonResponse.getString("title").equals(""))
                            tv1.setText("No Title");
                        else
                            tv1.setText(jsonResponse.getString("title"));
                        tv1.setGravity(Gravity.CENTER_HORIZONTAL);
                        tv1.setTextColor(Color.rgb(255, 255, 255));
                        tv1.setTextSize(18);
                        Typeface face = Typeface.createFromAsset(getAssets(),
                                "fonts/sourcecodeproregular.ttf");
                        tv1.setTypeface(face);

                        //TODO: add author name in new textview within card below title
                        /*TextView tv2 = new TextView(getApplicationContext());
                        if (response.getString("by").equals(""))
                            tv2.setText("Anonymous");
                        else
                            tv2.setText(response.getString("by"));
                        tv2.setGravity(Gravity.CENTER_HORIZONTAL);
                        tv2.setTextColor(Color.rgb(200, 200, 200));*/

                        LinearLayout ll2 = new LinearLayout(getApplicationContext());
                        ll2.setGravity(Gravity.CENTER);
                        ll2.addView(tv1);
                        //ll2.addView(tv2);

                        cv.addView(ll2);

                        cv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String url;
                                try {
                                    url = jsonResponse.getString("url");
                                } catch (JSONException e) {
                                    System.out.println(e);
                                    url = "";
                                }
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                startActivity(browserIntent);
                            }
                        });

                        ll.addView(cv);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("Error :" + error.toString());
                }
            });

            mRequestQueue.add(mJsonObjectRequest);
        }
        /*JsonObjectRequest mJsonObjectRequest = new JsonObjectRequest(Request.Method.GET, storyURLs[0], null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                System.out.println("JSON OBJECT INDEX 0: " + response.toString());
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error :" + error.toString());
            }
        });

        mRequestQueue.add(mJsonObjectRequest);*/
    }
}
