package com.spaghetti_jester.widdly_lap.flagquiz3;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {

    public int numOfAnswers = 1;
    public ArrayList<Integer> selectedReigons = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //System.out.println("Current question: " + questionNum);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            if(selectedReigons.isEmpty()) {
                for (int i = 0; i < 6; i++) {
                    selectedReigons.add(i);
                }
            }
            Intent intent = new Intent(this,MainActivity.class);
            intent.putExtra("numOfAnswers",numOfAnswers);
            intent.putExtra("selectedReigons", selectedReigons);
            startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }
    public void setNumOfAnswers(int in) {
        numOfAnswers = in;
    }
    public void setSelectedReigons(ArrayList<Integer> in) {
        selectedReigons = in;
    }
}
