package com.walterrizzifoundation.grizzi.spca;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by a on 4/22/2015.
 */


public class ReceiptAPI {


    public  ReceiptAPI( String paymentID){
        String url = "http://spca.walterrizzifoundation.com/pdf/payment/" + paymentID;
        try {
            Log.d("RECEIPTAPI", url);
            HttpEntity page = this.getHttp(url);
            String content = EntityUtils.toString(page, HTTP.UTF_8);
            //JSONObject js = new JSONObject(content);
            Log.d("RECEIPTAPI", content);

        }
        catch(ClientProtocolException e){
            Log.d("CPI", "CLIENT PROTO EXCEPTION: " + e.getMessage());
        }
        catch (IOException e) {
            Log.d("Web ", "Erreur in ReceiptAPI: " + e.getMessage());

        }

    }




    public HttpEntity getHttp(String url) throws ClientProtocolException, IOException {
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet http = new HttpGet(url);
        HttpResponse response = httpClient.execute(http);
        Log.d("HTTPRESPONSE", "HTTP RESPONSE COMPLETE" + response.getEntity().toString());
        return response.getEntity();
    }

}
