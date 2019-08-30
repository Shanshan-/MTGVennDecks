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

import java.util.HashSet;

public class ResultsActivity extends AppCompatActivity {

    ClipboardManager clipboard;
    private String deck1List = "";
    private String deck2List = "";
    private String deckComList = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        clipboard = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);

        //Get data
        Intent intent = getIntent();
        String[] decks = intent.getStringArrayExtra(MainActivity.EXTRA_DECK_INFO);
        splitDecks(decks);
        Snackbar.make(toolbar, decks[0], Snackbar.LENGTH_LONG).setAction("Part of TODO", null).show();
    }

    //Handle deck data
    //TODO: implementation
    public void splitDecks(String[] input) {
        //String[] output = new String[3];

        //split input arrays into sets(?)
        HashSet<String> deck1 = new HashSet<String>();
        String tmp = input[0];
        while(true) {
            //Check for stopping condition or if current line is valid
            int nextIndex = tmp.indexOf("\n");
            if (nextIndex == 0) {
                tmp = tmp.substring(nextIndex+1);
                continue;
            }
            if (nextIndex == -1) {
                break;
            }

            //Remove count number if present
            if (Character.isDigit(tmp.charAt(0))) {
                tmp = tmp.substring(tmp.indexOf(" ") + 1);
            }

            //Get start of next line
            nextIndex = tmp.indexOf("\n");
            if (nextIndex == -1) {
                deck1.add(tmp);
                break;
            }

            //Store current line
            if (Character.isDigit(tmp.charAt(nextIndex-1)))
                deck1.add((tmp.substring(0, nextIndex).substring(0, tmp.lastIndexOf(" "))
                                .substring(0, tmp.lastIndexOf(" "))).trim());
            else
                deck1.add(tmp.substring(0, nextIndex).trim());

            //Setup for next loop
            tmp = tmp.substring(nextIndex+1);
        }

        //save to respective global variables
        deck1List = "";
        deck2List = "";
        deckComList = "";
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
