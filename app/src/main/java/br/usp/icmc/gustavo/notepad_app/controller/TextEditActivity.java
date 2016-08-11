package br.usp.icmc.gustavo.notepad_app.controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import br.usp.icmc.gustavo.notepad_app.R;

public class TextEditActivity extends AppCompatActivity implements View.OnClickListener {


    private ImageButton savebutton_activity2;
    private ImageButton deletebutton_activity2;
    private EditText file_text_edittext;
    private File textfile_to_open;
    private String filename, file_path;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        init_views();
        init_listeners();
        file_path = getIntent().getExtras().getString("filePath");
        filename = getIntent().getExtras().getString("fileName");

        textfile_to_open =new File(file_path,filename);
        try {
            file_text_edittext.setText(read_file(textfile_to_open));
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_notepad_main, menu);
        return true;
    }


    /*
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
    */

    private void init_views(){
        savebutton_activity2 = (ImageButton) findViewById(R.id.save_button_toolbar_activity2);
        deletebutton_activity2 = (ImageButton) findViewById(R.id.trash_button_toolbar_activity2);
        file_text_edittext = (EditText) findViewById(R.id.EditText_activity2);
    }


    private void init_listeners(){
        savebutton_activity2.setOnClickListener(this);
        deletebutton_activity2.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.save_button_toolbar_activity2:

                try {

                    FileWriter writer_textfile = new FileWriter(textfile_to_open, false);
                    writer_textfile.write(file_text_edittext.getText().toString());
                    writer_textfile.flush();
                    writer_textfile.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(),"Text was saved!" , Toast.LENGTH_LONG).show();
                finish();
                break;


            case R.id.trash_button_toolbar_activity2:
                textfile_to_open.delete();
                Toast.makeText(getApplicationContext(),"File was deleted!" , Toast.LENGTH_LONG).show();
                finish();
                break;
        }
    }


    private String read_file(File textfile_to_open) throws IOException {
        BufferedReader buffReader = new BufferedReader(new FileReader(textfile_to_open));
        String text_feed;
        String all_text = "";

        while((text_feed = buffReader.readLine()) != null) {
            all_text = all_text +text_feed + "\n";
        }

        return all_text;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {

            FileWriter writer_textfile = new FileWriter(textfile_to_open, false);
            writer_textfile.write(file_text_edittext.getText().toString());
            writer_textfile.flush();
            writer_textfile.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(getApplicationContext(),"Text was saved!" , Toast.LENGTH_LONG).show();
        finish();

    }



    //still dont know if I should save onPause
    /*
    @Override
    protected void onPause() {
        super.onPause();

        try {

            FileWriter writer_textfile = new FileWriter(textfile_to_open, false);
            writer_textfile.write(file_text_edittext.getText().toString());
            writer_textfile.flush();
            writer_textfile.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(getApplicationContext(),"Text was saved!" , Toast.LENGTH_LONG).show();
        finish();
    }
    */

}
