package com.example.mtgvenndecks;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;

public class ResultsActivity extends AppCompatActivity {

    ClipboardManager clipboard;
    private String deck1List = "";
    private String deck2List = "";
    private String deckComList = "";
    private ArrayList<String> BASICS =
            new ArrayList<>(Arrays.asList("Plains", "Island", "Swamp", "Mountain", "Forest", "Wastes"));
    private int[] basicCount = {0, 0, 0, 0, 0, 0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        clipboard = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
        //String[] test1 = {"hello\nworld\nthis\nis\nsparta", "this\nis\nnot\na\ntest"};
        //splitDecks(test1);

        //Get data
        Intent intent = getIntent();
        String[] decks = intent.getStringArrayExtra(MainActivity.EXTRA_DECK_INFO);
        splitDecks(decks);
        displayDecks();

        Snackbar.make(toolbar, decks[0], Snackbar.LENGTH_LONG).setAction("Part of TODO", null).show();
    }

    //Handle deck data
    public void splitDecks(String[] input) {
        //String[] output = new String[3];

        //split input arrays into sets
        HashSet<String> deck1 = new HashSet<>();
        HashSet<String> deck2 = new HashSet<>();
        int loopCount = 0;
        while(loopCount < 2) {
            String tmp = input[0];
            if (loopCount == 1)
                tmp = input[1];
            while (true) {
                //Check for stopping condition or if current line is valid
                tmp = tmp.trim();
                int nextIndex = tmp.indexOf("\n");
                if (nextIndex == -1 && tmp.equals("")) {
                    break;
                }

                //Get start of next line
                nextIndex = tmp.indexOf("\n");
                if (nextIndex == -1) {
                    tmp = tmp.concat("\n");
                    nextIndex = tmp.length() - 1;
                }

                //Remove set and collector number if present
                String line = tmp.substring(0, nextIndex).trim();
                if (Character.isDigit(line.charAt(nextIndex - 1))) { //uses tappedout format w/ CN
                    line = line.substring(0, line.lastIndexOf(" ")).trim();
                    line = line.substring(0, line.lastIndexOf(" ")).trim();
                } else if (line.charAt(nextIndex - 1) == ')') { //uses tappedout format w/o CN
                    line = line.substring(0, line.lastIndexOf(" ")).trim();
                }

                //Remove count number if present
                if (Character.isDigit(line.charAt(0))) {
                    line = line.substring(line.indexOf(" ") + 1);
                }

                //Handle if basic
                int basicType = BASICS.indexOf(line);
                if (basicType != -1) {
                    int count = Integer.parseInt(tmp.substring(0, tmp.indexOf(" ")));
                    if (loopCount == 0)
                        basicCount[basicType] -= count;
                    else
                        basicCount[basicType] += count;
                    tmp = tmp.substring(nextIndex + 1);
                    continue;
                }

                //Add to respective deck
                if (loopCount == 0) {
                    deck1.add(line);
                } else {
                    deck2.add(line);
                }

                //Setup for next loop
                tmp = tmp.substring(nextIndex + 1);
            }
            loopCount++;
        }

        //zero out respective global variables
        deck1List = "";
        deck2List = "";
        deckComList = "";

        //filter into global variables
        for(String card : deck1) {
            if(deck2.contains(card)) {
                deckComList = deckComList.concat(card + "\n");
                deck2.remove(card);
                continue;
            }
            deck1List = deck1List.concat(card + "\n");
        }
        for(String card : deck2) {
            deck2List = deck2List.concat(card + "\n");
        }

        //add basics to common list
        for(int x = 0; x < basicCount.length; x++) {
            if (basicCount[x] == 0)
                continue;
            deckComList = deckComList.concat(basicCount[x] + " " + BASICS.get(x) + "\n");
        }

        //clean up deck lists
        deck1List = deck1List.trim();
        deck2List = deck2List.trim();
        deckComList = deckComList.trim();
        System.out.println(deck1List + "\n" + deckComList + "\n" + deck2List);
    }

    public void displayDecks() {
        TextView deck1TV = findViewById(R.id.deck1Results);
        TextView deck2TV = findViewById(R.id.deck2Results);
        TextView deckComTV = findViewById(R.id.commonResults);
        deck1TV.setText(deck1List);
        deck2TV.setText(deck2List);
        deckComTV.setText(deckComList);
    }

    // Default placeholder onClick method
    public void popSnackbar(View view) {
        Snackbar.make(view, "This functionality has not yet been implemented", Snackbar.LENGTH_LONG)
                .setAction("Part of TODO", null).show();
    }

    public void returnButton(View view) {
        finish();
    }

    // Copy respective list to clipboard
    // https://stackoverflow.com/questions/19177231/android-copy-paste-from-clipboard-manager
    public void copyDeck(View view) {
        // Get respective EditText field contents
        String content = "";
        if (view.getId() == R.id.cb_copy1)
            content = deck1List;
        else if (view.getId() == R.id.cb_copy2)
            content = deck2List;
        else if (view.getId() == R.id.cb_copy_common)
            content = deckComList;

        // Set clipboard text
        clipboard.setPrimaryClip(ClipData.newPlainText(null, content));
    }

}
