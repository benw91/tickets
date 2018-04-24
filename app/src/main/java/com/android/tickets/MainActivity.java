package com.android.tickets;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.android.tickets.Station.COMPARE_BY_NAME;
import static java.lang.Math.abs;

public class MainActivity extends AppCompatActivity {
    private final ArrayList<Station> allStations = new ArrayList<Station>();     //Save a list of all the stations
    Button submitButton, displayStationsButton;
    TextView price_text_view;
    Spinner sourceSpinner, destSpinner;

    DatabaseReference stationsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.setPersistenceEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        submitButton = (Button) findViewById(R.id.submit);
        stationsRef = database.getReference("stations");
        sourceSpinner = (Spinner) findViewById(R.id.spinner_source);
        destSpinner = (Spinner) findViewById(R.id.spinner_dest);
        price_text_view = (TextView) findViewById(R.id.textView_price);
        displayStationsButton = (Button) findViewById(R.id.Button_display_stations);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPrice(sourceSpinner.getSelectedItem().toString(), destSpinner.getSelectedItem().toString());
            }
        });

        displayStationsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                go_to_display_stations(v);
            }
        });
        //Populate spinner
        stationsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // initialize the List
                final List<String> stationNames = new ArrayList<String>();
                for (DataSnapshot nameSnapshot : dataSnapshot.getChildren()) {
                    String stationName = nameSnapshot.getKey();
                    String stationZone = nameSnapshot.child("zone").getValue(String.class);
                    allStations.add(new Station(stationName, stationZone));
                    stationNames.add(stationName);
                }
                Collections.sort(stationNames);
                ArrayAdapter<String> stationsAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, stationNames);
                stationsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sourceSpinner.setAdapter(stationsAdapter);
                destSpinner.setAdapter(stationsAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


    }

    /**
     * Calculates the price under the assumption that it's determined by |dest-source|+1 and sets the price view
     * @param source Area of the source station
     * @param dest Area of the destination
     */
    public void setPrice(String source, String dest) {
        int sourceZ = -1;
        int destZ = -1;
        if(source.equals(dest)) {
            Toast.makeText(this , "Source and destination can't be the same!", Toast.LENGTH_LONG).show();
            price_text_view.setText("0");
        }
        else {
            for (Station s : allStations) {
                if (s.getName().equals(source)) sourceZ = Integer.valueOf(s.getZone());
                if (s.getName().equals(dest)) destZ = Integer.valueOf(s.getZone());
            }
            String result = String.valueOf(abs(destZ - sourceZ) + 1);
            price_text_view.setText(result);
        }
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Creates the intent and opens the Add_station task
     * @param v
     */
    public void go_to_add_station(View v) {
        Intent intent = new Intent(this, Add_station.class);
        startActivity(intent);
    }

    /**
     * Creates the intent and opens the DisplayStation task
     * @param v
     */
    public void go_to_display_stations(View v) {

        Intent intent = new Intent(this, DisplayStations.class);
        startActivity(intent);

    }

	
}