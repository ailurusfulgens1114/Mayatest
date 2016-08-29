package com.salvin.mayatest.feed;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by salvin on 8/25/16.
 */
public class FeedService {
    int code;

    public ArrayList<Content> getContents(String urlPath) {
        ArrayList<Content> contents = new ArrayList<>();

        BufferedReader in = null;
        try {
            URL url = new URL(urlPath);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            code = con.getResponseCode();
            con.setConnectTimeout(15000);


            JSONParser parser = new JSONParser();


            try {
                in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String jsonData = "", line;
                while ((line = in.readLine()) != null) {
                    jsonData += line + "\n";
                }

            //    System.out.println(jsonData);

                Object obj = parser.parse(jsonData);

                JSONObject jsonObject = (JSONObject) obj;


                JSONArray feed = (JSONArray) jsonObject.get("feed");
                for (int i = 0; i < feed.size(); i++) {


                    JSONObject doc = (JSONObject) feed.get(i);

                    System.out.println(String.valueOf(feed.size()));
                    //System.out.println(doc.get("name").toString());

                    Content content = new Content();

                    // System.out.println(doc.get("url").toString());

                    content.setId(Integer.valueOf(doc.get("id").toString()));
                    content.setName(doc.get("name").toString());

                    try {

                        Object img = doc.get("image");

                        if (img == null || img.toString().equals("")) {
                            content.setImage("");
                        } else content.setImage(img.toString());

                    } catch (Exception e) {
                        System.out.println("Image data parse ---" + String.valueOf(e));
                    }


                    content.setStatus(doc.get("status").toString());
                    content.setProfilePic(doc.get("profilePic").toString());
                    content.setTimeStamp(doc.get("timeStamp").toString());

                    try {
                        Object tempUrl = doc.get("url");

                        if (tempUrl == null || tempUrl.toString().equals(""))
                            content.setUrl("");
                        else content.setUrl(tempUrl.toString());
                    } catch (Exception e) {
                        System.out.println("Url data parse ---" + String.valueOf(e));
                    }

                    contents.add(content);

                }


            } catch (Exception e) {
                System.out.println("Data Parse Bufferreader exception---" + String.valueOf(e));

            } finally {
                con.disconnect();
            }

        } catch (Exception e) {

            System.out.println("URL Malform exception ---" + String.valueOf(e));
            e.printStackTrace();

        }
        return contents;
    }
}
