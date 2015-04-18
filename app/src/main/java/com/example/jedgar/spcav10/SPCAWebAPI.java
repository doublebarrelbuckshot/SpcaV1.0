package com.example.jedgar.spcav10;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.ViewDebug;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class SPCAWebAPI {

    final String ID_ID = "ID";
    final String ID_NAME = "Name";
    final String ID_SPECIES = "Species";
    final String ID_SEX = "Sex";
    final String ID_PRIMARY_BREED = "PrimaryBreed";
    final String ID_SECONDARY_BREED = "SecondaryBreed";
    final String ID_AGE = "Age";
    final String ID_SIZE = "Size";
    final String ID_PRIMARY_COLOR = "PrimaryColor";
    final String ID_SECONDARY_COLOR = "SecondaryColor";
    final String ID_DECLAWED = "Declawed";
    final String ID_STERILE = "SN";
    final String ID_ALTERED = "Altered";
    final String ID_INTAKE_DATE = "LastIntakeDate";
    final String ID_DESC = "Dsc";
    final String ID_PHOTO1 = "Photo1";
    final String ID_PHOTO2 = "Photo2";
    final String ID_PHOTO3 = "Photo3";

    ArrayList<Animal> animals;

    public SPCAWebAPI()
    {
        Log.d("SPCAWebAPI: ", "In SPCAWebAPI constructor.");
        animals = new ArrayList<Animal>();
    }

    public void callAdoptableSearch() throws Exception
    {
        Log.d("SPCAWebAPI: ", "In callAdoptableSearch()");

        String url = "http://www.petango.com/webservices/wsadoption.asmx";
        String soapEnvelope = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
                "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
                               "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" " +
                               "xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                  "<soap:Body>" +
                    "<AdoptableSearch xmlns=\"http://www.petango.com/\">" +
                      "<authkey>iiwolk70d54va4bxa5qcnokemt6utc7oplyys0ijld6svm2vgu</authkey>" +
                      "<speciesID></speciesID>" +
                      "<sex></sex>" +
                      "<ageGroup>all</ageGroup>" +
                      "<location></location>" +
                      "<site></site>" +
                      "<onHold></onHold>" +
                      "<orderBy>ID</orderBy>" +
                      "<primaryBreed></primaryBreed>" +
                      "<secondaryBreed></secondaryBreed>" +
                      "<specialNeeds></specialNeeds>" +
                      "<noDogs></noDogs>" +
                      "<noCats></noCats>" +
                      "<noKids></noKids>" +
                      "<stageID></stageID>" +
                    "</AdoptableSearch>" +
                  "</soap:Body>" +
                "</soap:Envelope>";

        DefaultHttpClient client = new DefaultHttpClient();
        HttpPost req = new HttpPost(url);
        StringEntity se = new StringEntity(soapEnvelope,HTTP.UTF_8);
        se.setContentType("text/xml");
        req.addHeader("SOAPAction", "\"http://www.petango.com/AdoptableSearch\"");
        req.setEntity(se);
        HttpResponse resp = client.execute(req);
        StatusLine status = resp.getStatusLine();
        if (status.getStatusCode() != 200) {
            throw new Exception("PETANGO:HTTP error, invalid server status code: " + resp.getStatusLine());
        }

        HttpEntity ent = resp.getEntity();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(ent.getContent());
        Node root = doc.getDocumentElement();
        //Log.d("WEB: ", "root name:" + root.getNodeName());
        Node node = root.getFirstChild();
        //Log.d("WEB: ", "node name:" + node.getNodeName());
        node = node.getFirstChild();
        //Log.d("WEB: ", "node child name:" + node.getNodeName());
        node = node.getFirstChild();
        //Log.d("WEB: ", "node grandchild name:" + node.getNodeName());
        int i = 0;
        for(Node animal_node = node.getFirstChild();
            animal_node != null;
            animal_node = animal_node.getNextSibling()) {

            String nil = ((Element)animal_node).getAttribute("xsi:nil");
            if (nil != null && nil.equals("true"))
                continue;

            Animal animal = new Animal();

            //Log.d("WEB: ", "animal_node name:" + animal_node.getNodeName());
            Node temp = animal_node.getFirstChild();
            for (temp = temp.getFirstChild(); temp != null; temp = temp.getNextSibling()) {
                //Log.d("WEB: ", "temp name:" + temp.getNodeName());
                if (temp.getNodeName().compareTo(ID_ID) == 0) {
                    animal.id = temp.getTextContent();
                    //Log.d("WEB: ", "ID:" + animal.id);
                }
                else if (temp.getNodeName().compareTo(ID_NAME) == 0) {
                    animal.name = temp.getTextContent();
                }
                else if (temp.getNodeName().compareTo(ID_SPECIES) == 0) {
                    animal.species = temp.getTextContent();
                }
                else if (temp.getNodeName().compareTo(ID_SEX) == 0) {
                    animal.sex = temp.getTextContent();
                }
                else if (temp.getNodeName().compareTo(ID_STERILE) == 0) {
                    String sterile = temp.getTextContent();
                    animal.sterile = "N";
                    if (sterile == null)
                        animal.sterile = "U";
                    else {
                        if (sterile.equals("Spayed"))
                            animal.sterile = "Y";
                        else if (sterile.equals("Neutered"))
                            animal.sterile = "Y";
                    }
                }
            }

            animals.add(animal);
        }
/*
            for(int i =0;i<lineupItems.length();i++){
                JSONObject item = lineupItems.getJSONObject(i);
                String title = item.getString("Title");
                String description = item.getString("Description");
                String image_url = item.getString("ImageUrl");
                if(item.getString("Template").equals("multiple-content")) {
                    titres.add(title);
                    descriptions.add(description);
                }
            }
            */

    }

    public Animal callAdoptableDetails(int idx)
            throws Exception
    {
        Log.d("SPCAWebAPI: ", "In callAdoptableDetails()");

        Animal animal = animals.get(idx);
        if (animal == null) {
            throw new Exception("SPCAWebAPI:Unknown animalID");
        }

        String url = "http://www.petango.com/webservices/wsadoption.asmx";
        String soapEnvelope =
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
                        "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
                        "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" " +
                        "xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                        "<soap:Body>" +
                        "<AdoptableDetails xmlns=\"http://www.petango.com/\">" +
                        "<animalID>" + animal.id + "</animalID>" +
                        "<authkey>iiwolk70d54va4bxa5qcnokemt6utc7oplyys0ijld6svm2vgu</authkey>" +
                        "</AdoptableDetails>" +
                        "</soap:Body>" +
                        "</soap:Envelope>";

        DefaultHttpClient client = new DefaultHttpClient();
        HttpPost req = new HttpPost(url);
        StringEntity se = new StringEntity(soapEnvelope,HTTP.UTF_8);
        se.setContentType("text/xml");
        req.addHeader("SOAPAction", "\"http://www.petango.com/AdoptableDetails\"");
        req.setEntity(se);
        HttpResponse resp = client.execute(req);
        StatusLine status = resp.getStatusLine();
        if (status.getStatusCode() != 200) {
            throw new Exception("PETANGO:HTTP error, invalid server status code: " + resp.getStatusLine());
        }

        HttpEntity ent = resp.getEntity();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(ent.getContent());
        Node root = doc.getDocumentElement();
        //Log.d("WEB: ", "root name:" + root.getNodeName());
        Node node = root.getFirstChild();
        //Log.d("WEB: ", "node name:" + node.getNodeName());
        node = node.getFirstChild();
        //Log.d("WEB: ", "node child name:" + node.getNodeName());
        /*
        node = node.getFirstChild();
        Log.d("WEB: ", "node grandchild name:" + node.getNodeName());*/
        int i = 0;
        for(node = node.getFirstChild(); node != null; node = node.getNextSibling()) {

            Node animal_node = node.getFirstChild();
            //Log.d("WEB: ", "animal_node name:" + animal_node.getNodeName());
            Node temp = animal_node.getFirstChild();
            for (; temp != null; temp = temp.getNextSibling()) {
                //Log.d("WEB: ", "temp name:" + temp.getNodeName());

                String nodeName = temp.getNodeName();
                //String nodeValue = temp.getTextContent();
                if (nodeName.compareTo(ID_PRIMARY_BREED) == 0) {
                    animal.primaryBreed = temp.getTextContent();
                } else if (nodeName.compareTo(ID_SECONDARY_BREED) == 0) {
                    animal.secondaryBreed = temp.getTextContent();
                } else if (nodeName.compareTo(ID_AGE) == 0) {
                    animal.age = temp.getTextContent();
                } else if (nodeName.compareTo(ID_SIZE) == 0) {
                    animal.size = temp.getTextContent();
                } else if (nodeName.compareTo(ID_PRIMARY_COLOR) == 0) {
                    animal.primaryColor = temp.getTextContent();
                } else if (nodeName.compareTo(ID_SECONDARY_COLOR) == 0) {
                    animal.secondaryColor = temp.getTextContent();
                } else if (nodeName.compareTo(ID_DECLAWED) == 0) {
                    animal.declawed = temp.getTextContent();
                } else if (nodeName.compareTo(ID_INTAKE_DATE) == 0) {
                    animal.intake_date = temp.getTextContent();
                } else if (nodeName.compareTo(ID_DESC) == 0) {
                    animal.description = temp.getTextContent();
                } else if (nodeName.compareTo(ID_ALTERED) == 0) {
                    String sterile = temp.getTextContent();
                    if (sterile.equals("Yes")) {
                        animal.sterile = "Y";
                    } else if (sterile.equals("No")) {
                        animal.sterile = "N";
                    }
                } else if (nodeName.compareTo(ID_PHOTO1) == 0) {
                    String text  = temp.getTextContent();
                    //Log.d("WEB: ", animal.id + ":" + ID_PHOTO1 + ":" + text);
                    animal.photo1 = temp.getTextContent();
                } else if (nodeName.compareTo(ID_PHOTO2) == 0) {
                    String text  = temp.getTextContent();
                    //Log.d("WEB: ", animal.id + ":" + ID_PHOTO2 + ":" + text);
                    animal.photo2 = temp.getTextContent();
                } else if (nodeName.compareTo(ID_PHOTO3) == 0) {
                    String text  = temp.getTextContent();
                    //Log.d("WEB: ", animal.id + ":" + ID_PHOTO3 + ":" + text);
                    animal.photo3 = temp.getTextContent();
                }
            }
        }

        //animal.dump();
        return animal;
    }

    /*
    public void downloadFile(String url, String filename) throws Exception {
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet http = new HttpGet(url);
        HttpResponse response = httpClient.execute(http);
        StatusLine status = response.getStatusLine();
        if (status.getStatusCode() != 200) {
            throw new Exception("PETANGO:HTTP error, invalid server status code: " + response.getStatusLine());
        }
        HttpEntity ent = response.getEntity();
        byte[] buffer = new byte[0x4000]; //16kB.
        int len;
        InputStream inputStream = ent.getContent();
        FileOutputStream outputStream = new FileOutputStream(filename);
        while((len = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, len);
        }
        inputStream.close();
        outputStream.close();
    }
*/
}
