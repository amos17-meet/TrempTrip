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

public class MyTrempsRequestsActivity extends AppCompatActivity {
    ListView lvMyTrempsRequests;
    List<TrempListObject> myTremps=new ArrayList<>();
    List<Place> placeList=new ArrayList<>();
    TrempListAdapter trempListAdapter;

    FirebaseUser userAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;

    private GeoDataClient mGeoDataClient;
    private PlaceDetectionClient mPlaceDetectionClient;

    int sizeOfPlaceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tremps_requests);
        setFirebaseVariables();
        setGoogleAPIVar();

    }

    private void setFirebaseVariables() {
        database = FirebaseDatabase.getInstance();
        userAuth = FirebaseAuth.getInstance().getCurrentUser();
        setMyRef();
    }
    public void setMyRef() {
        Query myUser=database.getReference().child("User").child(userAuth.getUid());

        myUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.w("setMyRef",dataSnapshot.toString());
                String groupOfUser=dataSnapshot.getValue(String.class);
                myRef=database.getReference().child("Group").child(groupOfUser);
                setMyTremps();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setGoogleAPIVar(){
        mGeoDataClient = Places.getGeoDataClient(this, null);
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);
    }

    public void setLvMyTrempsRequests(){
        Log.w("LV","here");
        lvMyTrempsRequests=findViewById(R.id.lvMyTrempsRequests);
        trempListAdapter=new TrempListAdapter(this, 0,0,myTremps);
        lvMyTrempsRequests.setAdapter(trempListAdapter);

    }



    public void setMyTremps(){
        Log.w("set my tremps","here");
        Query allMyTremps=myRef.child("tremps").orderByChild("userId").equalTo(userAuth.getUid());
        Log.w("set my tremps",allMyTremps.toString());
        allMyTremps.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.w("event listener",dataSnapshot.toString());
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    Log.w("event listener",singleSnapshot.toString());
                    Tremp tremp=singleSnapshot.getValue(Tremp.class);
                    Log.w("event listener",tremp.getUserId());
                    Log.w("event listener","added");
                    myTremps.add(new TrempListObject(tremp,null,null,null,null));
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
        for(Tremp tremp:myTremps){
            getStartAndEndName(tremp.getFromId(),tremp.getToId());

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
        Log.w("can cuntinue", sizeOfPlaceList+" "+myTremps.size());
        if(sizeOfPlaceList==myTremps.size()){
            Log.w("can cuntinue", "yes");
            completeTrempListObject();
        }
    }

    private void completeTrempListObject(){
        int i = 0;
        for(final TrempListObject tremp:myTremps){
            tremp.setFromName(placeList.get(i *2).getName().toString());
            tremp.setToName(placeList.get((i *2)+1).getName().toString());
            Query userQuery=myRef.child("users").orderByChild("userId").equalTo(userAuth.getUid());
            userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot singleSnapshot: dataSnapshot.getChildren()){
                        User user=singleSnapshot.getValue(User.class);
                        tremp.setUserName(user.getFullName());
                        tremp.setUserPhoneNumber(user.getPhoneNumber());
                    }
                    setLvMyTrempsRequests();


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
        i++;

    }


}
