package com.android.tickets;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Handles the task that displays the stations from the database
 */
public class DisplayStations extends AppCompatActivity {

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference stationsRef;

    RecyclerView recycler;

    /**
     * An array containing the names of all the stations in the DB is created and populated
     * The recycle views are populated with the list (used to calculate the price if Submit button is pressed)
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final ArrayList<Station> allStations = new ArrayList<Station>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_stations);
        recycler = (RecyclerView)findViewById(R.id.RecyclerView_allStations);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        //Populate the array of stations
        stationsRef = database.getReference("stations");
        stationsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                allStations.clear();
                for (DataSnapshot nameSnapshot : dataSnapshot.getChildren()) {
                    String stationName = nameSnapshot.child("name").getValue(String.class);
                    //If the name is too long splits it into multiple lines
                    if(stationName.length()>11) {
                        stationName = stationName.substring(0, 10) + "-\n" + stationName.substring(11, stationName.length()-1);
                    }
                    String stationZone = nameSnapshot.child("zone").getValue(String.class);
                    allStations.add(new Station(stationName, stationZone));
                    StationsAdapter stationAdapter = new StationsAdapter(allStations);
                    stationAdapter.setOnEntryClickListener(new StationsAdapter.OnEntryClickListener() {
                        @Override
                        public void onEntryClick(View view, int pos) {


                        }
                    });
                    recycler.setAdapter(stationAdapter);

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
            });

    }

}