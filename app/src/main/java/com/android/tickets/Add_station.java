package com.android.tickets;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * Handles the "Add station" task
 */
public class Add_station extends AppCompatActivity {


    Button btn_submitStation;
    EditText editArea;
    EditText editName;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference stationsDataRef = database.getReference("stations");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_station);
        btn_submitStation = (Button)findViewById(R.id.btn_submit_station);
        editName = (EditText) findViewById(R.id.editText_station_name);
        editArea = (EditText) findViewById(R.id.editText_station_area);


        /**
         * The onclick listener for submit, grabs the name and area and passes them to add to DB
         */
        btn_submitStation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addStation(editName.getText().toString(), editArea.getText().toString());

            }
        });
    }

    /**
     * Makes sure both parameters are correct and adds to the database
     * @param name Name of the new station
     * @param area The are in which the station is (used to calculate price), must be a number
     */
    public void addStation(String name, String area) {

        if(name.isEmpty() && area.isEmpty()) {
            Toast.makeText(this , "Cannot leave name and area empty!", Toast.LENGTH_LONG).show();
        }

        else if(name.isEmpty()) {
            Toast.makeText(this , "Cannot leave name empty!", Toast.LENGTH_LONG).show();
        }
        else if(area.isEmpty()) {
            Toast.makeText(this , "Cannot leave area empty!", Toast.LENGTH_LONG).show();

        }
        else if(!isInt(area)) {
            Toast.makeText(this , "Area has to be a number!", Toast.LENGTH_LONG).show();
        }

        else {
            stationsDataRef.child(name).setValue(new Station(name, area));
            if(editName != null && editArea != null) {
                editName.setText("");
                editArea.setText("");
                Toast.makeText(this, name + " station added", Toast.LENGTH_LONG).show();
            }
        }

    }

    /**
     * Makes sure the given string is a number (can become an int). Used to avoid a crash if an are that's not a number is entered
     * @param s the string to test
     * @return returns True of False if the parsing is successful
     */
    boolean isInt(String s) {
            try {
                Integer.parseInt(s);
            }
            catch(NumberFormatException n) {
                return false;
        }
        return true;
    }
}
