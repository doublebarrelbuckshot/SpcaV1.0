package com.example.jedgar.spca;

import android.content.Context;
import android.util.Log;

import com.squareup.picasso.Picasso;

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
import java.util.Locale;

/**
 * Created by a on 4/22/2015.
 */


public class NewsWebAPI {

    String newsImage;
    String newsText;
    String newsHeadline;
    int errorCode; //if 0 all ok, else error occured

    public NewsWebAPI(Context c){
        String url = "http://doublebarrelbuckshot.github.io"; //"http://doublebarrelbuckshot.github.io/apptest/";

        try {
            HttpEntity page = getHttp(url);
            String content = EntityUtils.toString(page, HTTP.UTF_8);
            JSONObject js = new JSONObject(content);

            if(Locale.getDefault().getISO3Language().equals("eng"))
            {
                this.newsImage = js.getString("newsImageEN");
                this.newsText = js.getString("newsTextEN");
                this.newsHeadline = js.getString("newsHeadlineEN");
                this.errorCode = 0;
            }
            else{
                    this.newsImage = js.getString("newsImageFR");
                    this.newsText = js.getString("newsTextFR");
                    this.newsHeadline = js.getString("newsHeadlineFR");
                    this.errorCode = 0;
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
