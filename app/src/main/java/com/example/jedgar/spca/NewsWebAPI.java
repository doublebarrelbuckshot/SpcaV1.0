package com.example.jedgar.spca;

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

/**
 * Created by a on 4/22/2015.
 */


public class NewsWebAPI {

    String newsImage;
    String newsText;
    String newsHeadline;

    public NewsWebAPI(){
        String url = "http://doublebarrelbuckshot.github.io/apptest/";

        try {
            HttpEntity page = getHttp(url);
            String content = EntityUtils.toString(page, HTTP.UTF_8);
            JSONObject js = new JSONObject(content);
            this.newsImage = js.getString("newsImage");
            this.newsText = js.getString("newsText");
            this.newsHeadline = js.getString("newsHeadline");


        } catch (IOException e) {
            Log.d("Web ", "Erreur: " + e.getMessage());
        } catch (JSONException e) {
            Log.d("JSON ", "Erreur: " + e.getMessage());
        }

        Log.d("NEWS", "Image: " + newsImage + "   Text:" + newsText + "    Headline:" + newsHeadline );
    }




    public HttpEntity getHttp(String url) throws ClientProtocolException, IOException {
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet http = new HttpGet(url);
        HttpResponse response = httpClient.execute(http);
        return response.getEntity();
    }

}
