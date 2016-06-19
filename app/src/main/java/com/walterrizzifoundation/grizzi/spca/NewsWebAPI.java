package com.walterrizzifoundation.grizzi.spca;

import android.content.Context;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by a on 4/22/2015.
 */


public class NewsWebAPI {

    private static NewsWebAPI instance = null;

    public static NewsWebAPI getInstance(Context c){
        if(instance == null)
            instance = new NewsWebAPI(c);
        return instance;
    }

    ArrayList<String> newsImage = new ArrayList<String>();
    ArrayList<String> newsText= new ArrayList<String>();
    ArrayList<String> newsHeadline= new ArrayList<String>();
    int itemCount = 0;
    int errorCode; //if 0 all ok, else error occured

    private NewsWebAPI(Context c){
        String url = "http://spcamontreal.github.io"; //"http://doublebarrelbuckshot.github.io/apptest/";
        try {
            HttpEntity page = getHttp(url);
            String content = EntityUtils.toString(page, HTTP.UTF_8);
            JSONObject js = new JSONObject(content);

            if(Locale.getDefault().getISO3Language().equals("eng"))
            {
                for(int i = 1; i<5; i++) {
                    if(!js.getString("newsImageEN" + i).equals("")) {
                        this.newsImage.add(js.getString("newsImageEN" + i));
                        this.newsText.add(js.getString("newsTextEN" + i));
                        this.newsHeadline.add(js.getString("newsHeadlineEN" + i));
                        this.errorCode = 0;
                        this.itemCount++;
                    }
                }
            }
            else{
                for(int i = 1; i<5; i++) {
                    if(!js.getString("newsImageFR" + i).equals("")) {
                        this.newsImage.add(js.getString("newsImageFR" + i));
                        this.newsText.add(js.getString("newsTextFR" + i));
                        this.newsHeadline.add(js.getString("newsHeadlineFR" + i));
                        this.errorCode = 0;
                        this.itemCount++;
                    }
                }
            }

        }
        catch (IOException e) {
            Log.d("Web ", "Erreur: " + e.getMessage());
            this.errorCode = 1;


        } catch (JSONException e) {
            Log.d("JSON ", "Erreur: " + e.getMessage());
            this.errorCode = 1;
        }

        //Log.d("NEWS", "Image: " + newsImage + "   Text:" + newsText + "    Headline:" + newsHeadline );
    }




    public HttpEntity getHttp(String url) throws ClientProtocolException, IOException {
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet http = new HttpGet(url);
        HttpResponse response = httpClient.execute(http);
        return response.getEntity();
    }

}
