package com.android.tickets;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * The adapter is used to populate the spinner in the MainActivity
 */

public class StationsAdapter extends RecyclerView.Adapter<StationsAdapter.StationsClickViewHolder> {

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference stationsDataRef = database.getReference("stations");

    public class StationsClickViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView stationName, stationArea;
        Button deleteStation, editStation;
        public StationsClickViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            stationName = (TextView) itemView.findViewById(R.id.textView_display_station_name);
            stationArea = (TextView) itemView.findViewById(R.id.textView_display_area);
            deleteStation = (Button) itemView.findViewById(R.id.button_delete_station);
            editStation = (Button)itemView.findViewById(R.id.button_edit_station);
        }

        @Override
        public void onClick(View v) {
            TextView sName = (TextView)itemView.findViewById(R.id.textView_display_station_name);

        }
    }

    private ArrayList<Station> stationsArray;

    /**
     * Sets the private ArrayList to the info to be presented
     * @param list A list of the stations to be displayed
     */
    public StationsAdapter(ArrayList<Station> list) {
        stationsArray = list;
    }



    @Override
    public StationsClickViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.station_row, parent, false);
        return new StationsClickViewHolder(view);
    }

    /**
     * Binds the station details and the required buttons
     * @param holder The view holder
     * @param position the position in the recycler view
     */
    @Override
    public void onBindViewHolder(StationsClickViewHolder holder, final int position) {
        Station st = stationsArray.get(position);
        String stationName = st.getName();
        final String name = stationName;
        final String zone = st.getZone();
        holder.stationName.setText(name);
        holder.stationArea.setText(zone);
        holder.deleteStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stationsDataRef.child(name).removeValue();
                stationsArray.remove(position);
                notifyDataSetChanged();
            }
        });
        holder.editStation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                goToEdit(v, name, zone);
            }
        });

    }

    @Override
    public int getItemCount() {
        return stationsArray.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recycler) {
        super.onAttachedToRecyclerView(recycler);
    }

    private OnEntryClickListener mOnClickListener;

    public interface OnEntryClickListener    {
        void onEntryClick (View view,int pos);
    }

    public void setOnEntryClickListener(OnEntryClickListener onEntryListener) {
        mOnClickListener = onEntryListener;
    }

    /**
     * Creates the intent (with name and zone) and send to the "Edit" screen
     * @param v
     * @param stationName The name of the station to be edited
     * @param area The area in which the station currently is
     */
    public void goToEdit(View v, String stationName, String area) {
        Intent intent = new Intent(v.getContext(), EditStation.class);
        intent.putExtra("WHOLE_STATION", new Station(stationName, area));
        v.getContext().startActivity(intent);

    }

}
