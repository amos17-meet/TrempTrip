package com.example.student.trempme;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBufferResponse;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyTripsActivity extends AppCompatActivity {

    ListView lvMyTrips;
    List<Trip> myTrips=new ArrayList<>();
    List<Place> placeList=new ArrayList<>();
    TripListAdapter tripListAdapter;

    FirebaseUser userAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;

    private GeoDataClient mGeoDataClient;
    private PlaceDetectionClient mPlaceDetectionClient;

    int sizeOfPlaceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_trips);
        setFirebaseVariables();
        setGoogleAPIVar();
        setMyTrips();
    }

    private void setFirebaseVariables(){
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        userAuth = FirebaseAuth.getInstance().getCurrentUser();
    }
    private void setGoogleAPIVar(){
        mGeoDataClient = Places.getGeoDataClient(this, null);
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);
    }

    public void setLvMyTrips(){
        Log.w("LV","here");
        lvMyTrips=findViewById(R.id.lvMyTrips);
        tripListAdapter=new TripListAdapter(this, 0,0,myTrips);
        lvMyTrips.setAdapter(tripListAdapter);

    }

    public void setMyTrips(){
        Log.w("set my trips","here");
        Query allMyTrips=myRef.child("Trip").orderByChild("userId").equalTo(userAuth.getUid());
        Log.w("set my trips",allMyTrips.toString());
        allMyTrips.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    Log.w("event listener",singleSnapshot.toString());
                    Trip trip=singleSnapshot.getValue(Trip.class);
                    Log.w("event listener",trip.getUserId());
                    if(trip.getUserId().equals(userAuth.getUid())){
                        Log.w("event listener","added");
                        myTrips.add(trip);
                    }

                }
                setPlaceList();
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("event listener","cancelled");

            }
        });
    }

    private void setPlaceList(){
        Log.w("PlaceList","here");
        for(Trip trip : myTrips){
            getStartAndEndName(trip.getFromId(),trip.getToId());

        }
    }

    private void getStartAndEndName(String startPlaceId,String endPlaceId){
        Log.w("Place by id", "here");
        final Place[] myPlaces=new Place[2];
        mGeoDataClient.getPlaceById(startPlaceId).addOnCompleteListener(new OnCompleteListener<PlaceBufferResponse>() {
            @Override
            public void onComplete(@NonNull Task<PlaceBufferResponse> task) {
                if (task.isSuccessful()) {
                    PlaceBufferResponse places = task.getResult();

                    Place place = places.get(0).freeze();
                    myPlaces[0]=place;
                    Log.w("Place by id", "Place found: " + place.getName());
                    Log.w("myPlaces-start",myPlaces[0].getName()+"");
                    placeList.add(place);
                    places.release();



                } else {
                    Log.w("Place by id", "Place not found.");
                }
            }

        });

        mGeoDataClient.getPlaceById(endPlaceId).addOnCompleteListener(new OnCompleteListener<PlaceBufferResponse>() {
            @Override
            public void onComplete(@NonNull Task<PlaceBufferResponse> task) {
                if (task.isSuccessful()) {
                    PlaceBufferResponse places = task.getResult();
                    Place place = places.get(0).freeze();
                    myPlaces[1]=place;
                    Log.w("Place by id", "Place found: " + place.getName());
                    Log.w("myPlaces-end"," "+myPlaces[1].getName());
                    //placeList.add(myPlaces);
                    placeList.add(place);
                    places.release();
                    sizeOfPlaceList++;
                    canContinueToLv(sizeOfPlaceList);

                } else {
                    Log.w("Place by id", "Place not found.");
                }
            }

        });

    }

    private void canContinueToLv(int sizeOfPlaceList){
        Log.w("can cuntinue", sizeOfPlaceList+" "+myTrips.size());
        if(sizeOfPlaceList==myTrips.size()){
            Log.w("can cuntinue", "yes");
            addPlacesNamesToTrempObject();
        }
    }

    private void addPlacesNamesToTrempObject(){
        int i=0;
        for(Trip trip : myTrips){
            trip.setFromName(placeList.get(i*2).getName().toString());
            trip.setToName(placeList.get((i*2)+1).getName().toString());
            i++;
        }
        setLvMyTrips();
    }




}
