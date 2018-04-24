package com.android.tickets;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class EditStation extends AppCompatActivity {
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference stationsRef = database.getReference("stations");
    TextView oldNameView, oldZoneView;
    EditText newNameEdit, newZoneEdit;
    private String oldName, oldZone;
    private Button submitButton;

    /**
     * Grabs the details ofthe station to be edited from an intent sent by the StationAdapter
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_edit_station);
        Intent intent = getIntent();
        oldName = intent.getStringExtra("STATION_NAME");
        oldZone = intent.getStringExtra("STATION_ZONE");
        super.onCreate(savedInstanceState);
        oldZoneView = (TextView)findViewById(R.id.textView_old_zone);
        oldNameView = (TextView)findViewById(R.id.textView_old_name);
        oldNameView.setText(oldName);
        oldZoneView.setText(oldZone);
        submitButton = (Button)findViewById(R.id.button_submit_edit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editStation(oldName, oldZone);
            }
        });

    }

    /**
     * Gets the data from the EditText views, checks whether the change would overwrite an existing station (in which case it prompts user), deletes the old station and creates a new one
     * If one of the fields is left empty the information of the "old" station will be used
     * @param oldName The current name of the station
     * @param oldZone The current area the station is in
     */
    private void editStation(final String oldName, String oldZone) {
        newNameEdit = (EditText)findViewById(R.id.editText_new_name);
        newZoneEdit = (EditText)findViewById(R.id.editText_new_zone);
        final String newName = getNewData(oldName, newNameEdit.getText().toString());
        final String newZone = getNewData(oldZone, newZoneEdit.getText().toString());
        final DatabaseReference newStationRef = database.getReference("stations/" + newName);
        stationsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //If there's no existing station with the same name or the name hasn't been changed
                if(dataSnapshot == null || !dataSnapshot.hasChild(newName) || oldName.equals(newName)) {
                    //Delete the old station
                    stationsRef.child(oldName).removeValue();
                    //Create a reference to the station we would like to add (the name is the key)
                    newStationRef.setValue(new Station(newName, newZone));
                    oldNameView.setText(newName);
                    oldZoneView.setText(newZone);
                }
                //There's an existing station with the name the user put in, dialog will be shown to make sure the user would like to overwrite
                else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(EditStation.this);
                            builder.setTitle("Are you sure you'd like to overwrite " + newName + "?")
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    Toast.makeText(EditStation.this, "Operation canceled, would not like to overwrite", Toast.LENGTH_LONG).show();
                                }
                            })
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    stationsRef.child(oldName).removeValue();
                                    newStationRef.setValue(new Station(newName, newZone));
                                    oldNameView.setText(newName);
                                    oldZoneView.setText(newZone);
                                }
                            })
                    .create().show();

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        newNameEdit.setText("");
        newZoneEdit.setText("");
    }

    /**
     * Checks whether the name/zone has been changed, if not returns the old value
     * @param oldData A string of data from the "old" station (name/area)
     * @param newData The new data to be entered
     * @return Checks whether the data has been entered and returns the new data or old data if nothing was inserted
     */
    private String getNewData(String oldData, String newData) {
        if(oldData.equals(newData) || newData.isEmpty()) return oldData;
        else return newData;
    }

}