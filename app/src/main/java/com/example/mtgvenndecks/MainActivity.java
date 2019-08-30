package com.example.mtgvenndecks;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.MenuItem;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    public final static String EXTRA_DECK_INFO = "com.example.mtgvenndecks.DECK_LISTS";
    ClipboardManager clipboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        clipboard = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);

        /*//Scrollable textViews
        findViewById(R.id.deck1Results).setMovementMethod(ScrollingMovementMethod.getInstance());
        findViewById(R.id.deck2Results).setMovementMethod(ScrollingMovementMethod.getInstance());

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Running", Snackbar.LENGTH_LONG)
                        .setAction("Comparing and Generating Lists", null).show();
            }
        });*/
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /***************************
     * BUTTON LISTENER METHODS
     ***************************/

    // Default placeholder onClick method
    public void popSnackbar(View view) {
        Snackbar.make(view, "This functionality has not yet been implemented", Snackbar.LENGTH_LONG)
                .setAction("Part of TODO", null).show();
    }

    // Start up the comparison process
    public void compareLists(View view) {
        Intent intent = new Intent(this, ResultsActivity.class);
        EditText editText1 = (EditText) findViewById(R.id.deck1InputET);
        EditText editText2 = (EditText) findViewById(R.id.deck2InputET);
        intent.putExtra(EXTRA_DECK_INFO, new String[]
                {editText1.getText().toString(), editText2.getText().toString()});
        startActivity(intent);
    }

    // Paste clipboard contents to respective deck list
    // https://stackoverflow.com/questions/19177231/android-copy-paste-from-clipboard-manager
    public void pasteDeck(View view) {
        // Get clipboard text
        String cb_contents = "";
        if(clipboard.hasPrimaryClip()) {
            ClipDescription description = clipboard.getPrimaryClipDescription();
            ClipData data = clipboard.getPrimaryClip();
            if (data != null && description != null &&
                    description.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                cb_contents = data.getItemAt(0).getText().toString();
            }
        } else {
            Snackbar.make(view, "Cannot paste clipboard contents", Snackbar.LENGTH_LONG)
                    .setAction("nothing to do", null).show();
        }

        // Insert into respective EditText field
        EditText editText = null;
        if (view.getId() == R.id.cb_paste1 )
                editText = (EditText) findViewById(R.id.deck1InputET);
        else
                editText = (EditText) findViewById(R.id.deck2InputET);
        editText.setText(cb_contents);
    }
}
