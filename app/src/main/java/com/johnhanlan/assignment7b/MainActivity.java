package com.johnhanlan.assignment7b;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ListView listView;
    private ProcessRSSTask processRSSTask;

    private SharedPreferences sharedPreferences;
    private LinearLayout main_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("general", 0);
        main_view = findViewById(R.id.main_content_background);

        Helper.localArticles.clear();
        Helper.breakingArticles.clear();
        Helper.worldArticles.clear();

        processRSSTask = new ProcessRSSTask();
        try{
            processRSSTask.execute().get();
        } catch (Exception e) {
            Toast.makeText(this, "Didn't wait for other thread", Toast.LENGTH_LONG).show();
        }

        if(Helper.articles.size() == 0) {
            Helper.articles.add(Helper.localArticles);
            Helper.articles.add(Helper.breakingArticles);
            Helper.articles.add(Helper.worldArticles);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        loadFeeds(new CustomAdapter(MainActivity.this, R.layout.list_item, Helper.articles.get(Helper.feed)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        Helper.setBackgroundLinear(sharedPreferences, main_view);
        loadFeeds(new CustomAdapter(MainActivity.this, R.layout.list_item, Helper.articles.get(Helper.feed), sharedPreferences));
    }

    private void loadFeeds(CustomAdapter customAdapter){
        customAdapter.notifyDataSetChanged();

        //set the adapter of the ListView
        listView = findViewById(R.id.nice_listview);
        listView.setAdapter(customAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Helper.position = position;

                Intent intent = new Intent(MainActivity.this, FullArticle.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, Settings.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_refresh) {
            finish();
            startActivity(getIntent());
            loadFeeds(new CustomAdapter(MainActivity.this, R.layout.list_item, Helper.articles.get(Helper.feed)));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_local_button) {

            Helper.feed = 0;

            loadFeeds(new CustomAdapter(
                    MainActivity.this,
                    R.layout.list_item,
                    Helper.localArticles));

        } else if (id == R.id.nav_breaking_button) {

            Helper.feed = 1;

            loadFeeds(new CustomAdapter(
                    MainActivity.this,
                    R.layout.list_item,
                    Helper.breakingArticles));

        } else if (id == R.id.nav_world_button) {

            Helper.feed = 2;

            loadFeeds(new CustomAdapter(
                    MainActivity.this,
                    R.layout.list_item,
                    Helper.worldArticles));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
