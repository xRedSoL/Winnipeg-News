package com.johnhanlan.assignment7b;

import java.util.ArrayList;

/**
 * Created by johnjhanlan on 2018-02-09.
 */

public final class Helper {

    //Feeds
    public static ArrayList<Article> localArticles = new ArrayList<Article>();
    public static ArrayList<Article> breakingArticles = new ArrayList<Article>();
    public static ArrayList<Article> worldArticles = new ArrayList<Article>();

    public static ArrayList<ArrayList<Article>> articles = new ArrayList<ArrayList<Article>>();

    //positions
    public static int position = 0;
    public static int feed = 0;

    private Helper() {
        //localArticles = new ArrayList<Article>();
    }

}
