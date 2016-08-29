package com.salvin.mayatest.feed;

import java.util.ArrayList;

/**
 * Created by salvin on 8/25/16.
 */


public class Feed {

    private String url;
    private int size;


    private ArrayList<Content> contents;
    public Feed(){

        contents = new ArrayList<Content>();
    }

    public void setUrl(String url){ this.url = url; }
    public void addContent(Content ob){
  //      System.out.println("adding");
        this.contents.add(ob);

    }


    public void setContents(ArrayList<Content> contents){
       // System.out.println(String.valueOf(contents.size()));
     //   System.out.println("We are here\n");

        for( int i = 0 ; i < contents.size() ; i++ ) {
        //    System.out.println(contents.get(i).getName());
            addContent(contents.get(i));

        }
    }
    public ArrayList<Content> getContents(){
        return contents;
    }
    public String getUrl(){
        return url;
    }

    public int getContentsSize(){
        return contents.size();
    }
    public void setContentSize(int size){ this.size = size;}


}