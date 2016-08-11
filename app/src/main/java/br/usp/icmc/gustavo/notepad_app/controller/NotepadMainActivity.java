package br.usp.icmc.gustavo.notepad_app.controller;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;

import br.usp.icmc.gustavo.notepad_app.R;

public class NotepadMainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = NotepadMainActivity.class.getSimpleName();
    private ListView files_listview;
    private File file_list_available[];
    private ArrayList<String> filename_list_available;
    private ImageButton btn_add_activity1 , btn_refresh_activity1 , btn_delete_activity1;
    private ArrayAdapter<String> adapter;
    private Button btn_canceldelete , btn_confirmdelete;
    private String new_file_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notepad_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        /*
        try {
            initialze_dummy_testfiles();
        } catch (IOException e) {
            e.printStackTrace();
        }
        */

       // initialize_writtenfile();



        update_file_list();
        Log.d(TAG,"after dummy files " + this.toString());
        init_views();
        Log.d(TAG,"after init view " + this.toString());
        init_listeners();
        Log.d(TAG,"after init listeners " + this.toString());
        arrayadapter_listview_build();


    }

    private void init_views(){
        btn_add_activity1 = (ImageButton) findViewById(R.id.add_button_toolbar_activity1);
        btn_refresh_activity1 = (ImageButton) findViewById(R.id.refresh_button_toolbar_activity1);
        btn_delete_activity1 = (ImageButton) findViewById(R.id.trash_button_toolbar_activity1);
        files_listview = (ListView) findViewById(R.id.listview_text_archives);
        btn_canceldelete = (Button)findViewById(R.id.btn_canceldelete_activity1);
        btn_confirmdelete = (Button)findViewById(R.id.btn_delete_selecteditems_activity1);



    }

    private void init_listeners(){
        btn_canceldelete.setOnClickListener(this);
        btn_confirmdelete.setOnClickListener(this);
        btn_delete_activity1.setOnClickListener(this);
        btn_refresh_activity1.setOnClickListener(this);
        btn_add_activity1.setOnClickListener(this);

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

    public void update_file_list() {
        //goes to the app file directory and gets all the files there that ends with .txt
        file_list_available= null;
        file_list_available = new File(getApplicationContext().getApplicationInfo().dataDir).listFiles(new FilenameFilter() {
            public boolean accept(File dir, String filename)
            { return filename.endsWith(".txt"); }
        } );

        filename_list_available = new ArrayList();

        for(File file : file_list_available){
            filename_list_available.add(file.getName());
        }

        //Collections.sort(filename_list_available);



    }

    //builds adapter so it is a listview of normal selectable text
    private void arrayadapter_listview_build(){
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, filename_list_available);
        files_listview.setAdapter(adapter);

        files_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {


                // ListView Clicked item value
                String  itemValue    = (String) files_listview.getItemAtPosition(position);

                // Show Alert
                //Toast.makeText(getApplicationContext(),"Position :"+itemPosition+"  ListItem : " +itemValue , Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getApplicationContext(), TextEditActivity.class);
                intent.putExtra("filePath", getApplicationContext().getApplicationInfo().dataDir);
                intent.putExtra("fileName", itemValue);
                startActivity(intent);

            }

        });
    }

    //builds adapter so it is a listview of checkboxes
    private void arrayadapter_deletelistview_build(){
        adapter = new ArrayAdapter<String>(this, R.layout.listview_delete_items_layout,R.id.checkbox_filelist_activity1, filename_list_available);
        //adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, filename_list_available);
        //files_listview.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        files_listview.setAdapter(adapter);







    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_button_toolbar_activity1:
                //pop up message with name insertion


                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Write a valid text file name (only alphanumeric)");

                // Set up the input
                final EditText input = new EditText(this);


                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                // Set up the buttons, the create button needs to be set up with a custom onclick listener

                /*
                builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        new_file_name = input.getText().toString();

                        if(checkValidFileName(new_file_name)){
                            if (!alreadyexists_filename(new_file_name)){
                                File new_textfile = new File(getApplicationContext().getApplicationInfo().dataDir,new_file_name + ".txt");
                                try {
                                    new_textfile.createNewFile();
                                    update_file_list();
                                    arrayadapter_listview_build();

                                } catch (IOException e) {
                                    Toast.makeText(getApplicationContext(),"Error creating file" , Toast.LENGTH_LONG).show();
                                    e.printStackTrace();
                                }
                            }else{
                                Toast.makeText(getApplicationContext(),"File name already taken" , Toast.LENGTH_LONG).show();
                                input.setError("File name already taken");
                            }

                        }else{
                            Toast.makeText(getApplicationContext(),"Invalid file name" , Toast.LENGTH_LONG).show();
                            input.setError("File name already taken");
                        }
                    }


                });
                */

                //fake onclick listener
                builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {}
                });




                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                final AlertDialog alertdialog_activity1 = builder.create();

                //we need to show the dialog before making a custom onclicklistener for create button
                alertdialog_activity1.show();


                //we extract the positive button and build the custom listener, checking if the filename is valid and non-existant
                final Button createButton =
                        alertdialog_activity1.getButton(DialogInterface.BUTTON_POSITIVE);
                createButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new_file_name = input.getText().toString();

                        if(checkValidFileName(new_file_name)){
                            if (!alreadyexists_filename(new_file_name)){
                                File new_textfile = new File(getApplicationContext().getApplicationInfo().dataDir,new_file_name + ".txt");
                                try {
                                    new_textfile.createNewFile();
                                    update_file_list();
                                    arrayadapter_listview_build();
                                    alertdialog_activity1.dismiss();

                                } catch (IOException e) {
                                    Toast.makeText(getApplicationContext(),"Error creating file" , Toast.LENGTH_LONG).show();
                                    e.printStackTrace();
                                }
                            }else{
                                Toast.makeText(getApplicationContext(),"File name already taken" , Toast.LENGTH_LONG).show();
                                input.setError("File name already taken");
                            }

                        }else{
                            Toast.makeText(getApplicationContext(),"Invalid file name" , Toast.LENGTH_LONG).show();
                            input.setError("Invalid file name");
                        }
                    }
                });




                break;

            case R.id.refresh_button_toolbar_activity1:

                btn_canceldelete.setVisibility(View.GONE);
                btn_confirmdelete.setVisibility(View.GONE);
                update_file_list();
                arrayadapter_listview_build();
                Toast.makeText(getApplicationContext(),"list refreshed!" , Toast.LENGTH_LONG).show();

                break;

            case R.id.trash_button_toolbar_activity1:
                Toast.makeText(getApplicationContext(),"Mark unwanted files and press delete!" , Toast.LENGTH_LONG).show();
                //opens listview with checkboxes for delete and make delete and cancel buttons visible
                btn_canceldelete.setVisibility(View.VISIBLE);
                btn_confirmdelete.setVisibility(View.VISIBLE);
                update_file_list();
                arrayadapter_deletelistview_build();
                break;
            case R.id.btn_canceldelete_activity1:
                //remove buttons and rebuild normal text adapter
                btn_canceldelete.setVisibility(View.GONE);
                btn_confirmdelete.setVisibility(View.GONE);
                update_file_list();
                arrayadapter_listview_build();
                break;

            case R.id.btn_delete_selecteditems_activity1:
                //delete all selected items and rebuild normal adapter

                CheckBox cb;
                for (int x = 0; x<files_listview.getChildCount();x++){
                    cb = (CheckBox)files_listview.getChildAt(x).findViewById(R.id.checkbox_filelist_activity1);
                    if(cb.isChecked()){
                        File checkedfile = new File(getApplicationContext().getApplicationInfo().dataDir, String.valueOf(cb.getText()));
                        checkedfile.delete();
                    }
                }

                update_file_list();
                arrayadapter_listview_build();

                btn_canceldelete.setVisibility(View.GONE);
                btn_confirmdelete.setVisibility(View.GONE);
                break;

        }
    }

    //for test purposes only
    public void initialze_dummy_testfiles() throws IOException {
        File dummy1 = new File(getApplicationContext().getApplicationInfo().dataDir,"file1.txt");
        dummy1.createNewFile();
        File dummy2 = new File(getApplicationContext().getApplicationInfo().dataDir,"file2.txt");
        dummy2.createNewFile();
        File dummy3 = new File(getApplicationContext().getApplicationInfo().dataDir,"file3.txt");
        dummy3.createNewFile();
        File dummy4 = new File(getApplicationContext().getApplicationInfo().dataDir,"file4.txt");
        dummy4.createNewFile();
        File dummy5 = new File(getApplicationContext().getApplicationInfo().dataDir,"file5.txt");
        dummy5.createNewFile();

        File dummy6 = new File(getApplicationContext().getApplicationInfo().dataDir,"file6.txt");
        dummy6.createNewFile();
        File dummy7 = new File(getApplicationContext().getApplicationInfo().dataDir,"file7.txt");
        dummy7.createNewFile();
        File dummy8 = new File(getApplicationContext().getApplicationInfo().dataDir,"file8.txt");
        dummy8.createNewFile();
        File dummy9 = new File(getApplicationContext().getApplicationInfo().dataDir,"file9.txt");
        dummy9.createNewFile();
        File dummy10 = new File(getApplicationContext().getApplicationInfo().dataDir,"file10.txt");
        dummy10.createNewFile();

        File dummy11 = new File(getApplicationContext().getApplicationInfo().dataDir,"file11.txt");
        dummy11.createNewFile();
        File dummy12 = new File(getApplicationContext().getApplicationInfo().dataDir,"file12.txt");
        dummy12.createNewFile();
        File dummy13 = new File(getApplicationContext().getApplicationInfo().dataDir,"file13.txt");
        dummy13.createNewFile();
        File dummy14 = new File(getApplicationContext().getApplicationInfo().dataDir,"file14.txt");
        dummy14.createNewFile();
        File dummy15 = new File(getApplicationContext().getApplicationInfo().dataDir,"file15.txt");
        dummy15.createNewFile();

        File dummy16 = new File(getApplicationContext().getApplicationInfo().dataDir,"file16.txt");
        dummy16.createNewFile();
        File dummy17 = new File(getApplicationContext().getApplicationInfo().dataDir,"file17.txt");
        dummy17.createNewFile();
    }

    //true if the filename is valid and false otherwise
    private boolean checkValidFileName(String file_name){
        // TODO: 07/07/2016 check if filename is valid (without any invalid characters)
        return isAlphanumeric(file_name);

    }

    //true if it already exists and false otherwise
    private boolean alreadyexists_filename(String file_name){
        // TODO: 12/07/2016 check if the filename already exists
        return filename_list_available.contains(file_name+".txt");
    }
    

    @Override
    protected void onResume() {
        btn_canceldelete.setVisibility(View.GONE);
        btn_confirmdelete.setVisibility(View.GONE);
        update_file_list();
        arrayadapter_listview_build();
        super.onResume();
    }

    @Override
    protected void onRestart() {
        btn_canceldelete.setVisibility(View.GONE);
        btn_confirmdelete.setVisibility(View.GONE);
        update_file_list();
        arrayadapter_listview_build();
        super.onRestart();
    }




    //for test purposes only
    private void initialize_writtenfile(){
        File dummy17 = new File(getApplicationContext().getApplicationInfo().dataDir,"file17.txt");
        try {
            dummy17.createNewFile();
            FileWriter writer_textfile = new FileWriter(dummy17, true);
            writer_textfile.write("asdasdasdasdasdasd\n" + "asdasdasdasdasdasdasd\n" + "asdasdasdd\n" + "dddddddd");
            writer_textfile.flush();
            writer_textfile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Toast.makeText(getApplicationContext(),read_file(dummy17) , Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    //for test purposes only
    private String read_file(File textfile_to_open) throws IOException {
        BufferedReader buffReader = new BufferedReader(new FileReader(textfile_to_open));
        String text_feed;
        String all_text = "";

        while((text_feed = buffReader.readLine()) != null) {
            all_text = all_text +text_feed + "\n";
        }

        return all_text;
    }

    private boolean isAlphanumeric(String str) {
        for (int i=0; i<str.length(); i++) {
            char c = str.charAt(i);
            if (c < 0x30 || (c >= 0x3a && c <= 0x40) || (c > 0x5a && c <= 0x60) || c > 0x7a)
                return false;
        }

        return true;
    }
}
