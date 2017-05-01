package com.spaghetti_jester.widdly_lap.flagquiz3;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public int questionNum = 1;
    public int numCorrect = 0;
    public int numIncorrect = 0;
    public String[] answers = new String[8];
    public int[] incorrect = new int[8];
    public String currentFlagPath;

    public int numOfAnswers = 4;
    public ArrayList<Integer> selectedReigons = new ArrayList<Integer>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        resetGame();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, MenuActivity.class);
            /*intent.putExtra("questionNum",questionNum);
            intent.putExtra("numCorrect",numCorrect);
            intent.putExtra("numIncorrect",numIncorrect);
            intent.putExtra("answers",answers);
            intent.putExtra("incorrect", incorrect);
            intent.putExtra("currentFlagPath", currentFlagPath);
            intent.putExtra("numOfAnswers",numOfAnswers);
            intent.putExtra("selectedReigons",selectedReigons);*/
            startActivity(intent);

            return true;
        }
        if (id == android.R.id.home) {

            invalidateOptionsMenu();
            MainActivityFragment fragment = (MainActivityFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_main);
            fragment.setViewLayout(R.layout.fragment_main);
            refreshMain();
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            setTitle("Flag Quiz");
        }

        return super.onOptionsItemSelected(item);
    }
    public void resetGame() {
        questionNum = 0;
        numCorrect = 0;
        numIncorrect = 0;
        if (getIntent().hasExtra("numOfAnswers")) {
            numOfAnswers = getIntent().getIntExtra("numOfAnswers", 0);
            System.out.println("numOfAnswers set");
        }
        if (getIntent().hasExtra("selectedReigons")) {
            selectedReigons = getIntent().getIntegerArrayListExtra("selectedReigons");
            System.out.println("selectedReigons set");
        } else {
            for(int i = 0; i < 6; i++) {
                selectedReigons.add(i);
            }
        }

        if (numOfAnswers >= 4) {
            findViewById(R.id.a3).setVisibility(View.VISIBLE);
            findViewById(R.id.a4).setVisibility(View.VISIBLE);
        }
        if (numOfAnswers >= 6) {
            findViewById(R.id.a5).setVisibility(View.VISIBLE);
            findViewById(R.id.a6).setVisibility(View.VISIBLE);
        }
        if (numOfAnswers == 8) {
            findViewById(R.id.a7).setVisibility(View.VISIBLE);
            findViewById(R.id.a8).setVisibility(View.VISIBLE);
        }
        nextFlag();

    }
    public void nextFlag() {
        if (questionNum <= 9) {
            questionNum++;
            enableAllAnswers();
            findViewById(R.id.correctAnswer).setVisibility(View.GONE);
            ((TextView) findViewById(R.id.progressText)).setText("Question " + questionNum + " of 10");
            setRandomImage();
            generateAnswers();
        } else {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            double finalPercent = (double)(numCorrect + numIncorrect);
            finalPercent = 10.0 / finalPercent;
            finalPercent *= 10000;
            finalPercent = Math.round(finalPercent);
            finalPercent /= 100;
            System.out.println(finalPercent);
            builder.setMessage((numCorrect + numIncorrect) + " guesses, " + finalPercent + "% correct")
                    .setPositiveButton("Reset Quiz", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            resetGame();
                        }
                    });

            // Create the AlertDialog object and return it
            builder.create().show();
        }

    }
    public void setRandomImage() {
        ImageView flagContainer = (ImageView)findViewById(R.id.currentFlag);
        String fullFlagPath = getRandomPath();
        flagContainer.setImageDrawable(loadFlagFromAsset(fullFlagPath));
        currentFlagPath = fullFlagPath;
    }
    public String getRandomPath() {
        String[] reigons = getResources().getStringArray(R.array.regions_list);
        String currentReigon;
        if (selectedReigons.size() > 1)
            currentReigon = reigons[selectedReigons.get((int)(Math.random() * (selectedReigons.size())))];
        else
            currentReigon = reigons[selectedReigons.get(0)];
        String[] flagList = {""};
        try {
            flagList = getAssets().list(currentReigon);
        }
        catch (IOException e) {}
        String outputFlagName = flagList[(int)(Math.random() * flagList.length)];
        String fullFlagPath = currentReigon + "/" + outputFlagName;
        System.out.println("Full path name: " + fullFlagPath);
        System.out.println("Fixed country name: " + getNameFromPath(fullFlagPath));
        return fullFlagPath;
    }
    public Drawable loadFlagFromAsset(String filename) {
        // load image
        try {
            InputStream ims = getAssets().open(filename);

            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inDensity = DisplayMetrics.DENSITY_HIGH;
            return Drawable.createFromResourceStream(this.getResources(), null, ims, filename, opts);
        }
        catch(IOException ex) {
            System.out.println("I/O Error: " + ex);
            return null;
        }

    }
    public void refreshMain() {
        ((TextView)findViewById(R.id.progressText)).setText("Question " + questionNum + " of 10");
    }
    public String getNameFromPath(String p) {
        String out = p;
        out = out.substring(out.indexOf("-")+1);
        out = out.substring(0,out.indexOf(".png"));
        out = out.replace("_"," ");
        return out;
    }
    public int[] answerIDs = {R.id.a1, R.id.a2, R.id.a3, R.id.a4, R.id.a5,R.id.a6,R.id.a7,R.id.a8};
    public void generateAnswers() {
        int correctAnswer = (int)(Math.random() * numOfAnswers);
        for (int i = 0; i < numOfAnswers; i++) {
            if (correctAnswer == i) {
                ((TextView)findViewById(answerIDs[i])).setText(getNameFromPath(currentFlagPath));
                answers[i] = getNameFromPath(currentFlagPath);
            } else {
                String name = getNameFromPath(getRandomPath());
                ((TextView)findViewById(answerIDs[i])).setText(name);
                answers[i] = name;
            }
        }
    }
    public void checkCorrect(View v) {
        if (((Button)v).getText().equals(getNameFromPath(currentFlagPath))) { //If the answer is correct
            findViewById(R.id.incorrectAnswer).setVisibility(View.GONE);
            ((TextView)findViewById(R.id.correctAnswer)).setText(getNameFromPath(currentFlagPath));
            findViewById(R.id.correctAnswer).setVisibility(View.VISIBLE);
            disableAllAnswers();
            numCorrect++;
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    nextFlag();
                }
            }, 1500);
        } else { //If the answer is incorrect
            findViewById(R.id.incorrectAnswer).setVisibility(View.VISIBLE);
            v.setClickable(false);
            v.setBackgroundColor(Color.parseColor("#CCCCCC"));
            for (int i = 0; i < incorrect.length; i++) {
                if (incorrect[i] <= 0)
                    incorrect[i] = v.getId();
            }
            numIncorrect++;
        }
    }
    public void disableAllAnswers() {
        View v;
        for (int i = 0; i < answerIDs.length; i++) {
            v = findViewById(answerIDs[i]);
            v.setClickable(false);
            v.setBackgroundColor(Color.parseColor("#CCCCCC"));
        }
    }
    public void disableAnswer(int n) {
        View v = findViewById(n);
        v.setClickable(false);
        v.setBackgroundColor(Color.parseColor("#CCCCCC"));
    }
    public void enableAllAnswers() {
        View v;
        for (int i = 0; i < answerIDs.length; i++) {
            v = findViewById(answerIDs[i]);
            v.setClickable(true);
            v.setBackgroundColor(Color.parseColor("#7994ff"));
        }
    }
    public String packInts(int[] ints) {
        String out = "";
        for(int i = 0; i < ints.length; i++) {
            out += ints[i];
        }
        return out;
    }
    public int[] unpackInts(String ints) {
        int[] out = new int[8];
        for(int i = 0; i < ints.length(); i++) {
            out[i] = Integer.parseInt("" + ints.charAt(i));
        }
        return out;
    }

}
