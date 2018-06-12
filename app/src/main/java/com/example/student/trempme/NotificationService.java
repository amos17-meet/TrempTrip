package com.example.student.trempme;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class NotificationService extends Service {

    Timer timer;
    TimerTask timerTask;
    String TAG = "Timers";
    int Your_X_SECS = 180;

    FirebaseUser userAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;

    List<Tremp> currentTremps=new ArrayList<>();
    List<String> currentUserTrempsIndex=new ArrayList<>();
    List<Trip> currentTrips=new ArrayList<>();
    List<String> currentUserTripsIndex=new ArrayList<>();
    User user;

    //we are going to use a handler to be able to run in our TimerTask
    final Handler handler = new Handler();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        setFirebaseVariables();


        return START_STICKY;
    }


    @Override
    public void onCreate(){
        Log.e(TAG, "onCreate");


    }

    @Override
    public void onDestroy(){
        Log.e(TAG, "onDestroy");
        stoptimertask();
        super.onDestroy();


    }


    public void startTimer() {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
        timer.schedule(timerTask, 5000, Your_X_SECS*1000); //
        //timer.schedule(timerTask, 5000,1000); //
    }

    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void initializeTimerTask() {

        timerTask = new TimerTask() {
            public void run() {

                //use a handler to run a toast that shows the current timestamp
                handler.post(new Runnable() {
                    public void run() {

                        deleteTrempOrTrip();

                    }
                });
            }
        };
    }

    private void sendNotification(){
        Intent intent = new Intent(this, ShowAllTripsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
//        NotificationCompat.Builder mBuilder =
//            new NotificationCompat.Builder(this)
//                    .setSmallIcon(R.drawable.places_ic_search)
//                    .setContentTitle("See all Trips")
//                    .setContentText("Some may fit your tremps ")
//                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
//                    .setContentIntent(pendingIntent); //Required on Gingerbread and below
//
//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
//
//        // notificationId is a unique int for each notification that you must define
//        notificationManager.notify(0, mBuilder.build());

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.places_ic_search)
                        .setContentTitle("see all Trips")
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setContentText("Some may fit your tremps");
        int NOTIFICATION_ID = 12345;


        builder.setContentIntent(pendingIntent);
        NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nManager.notify(NOTIFICATION_ID, builder.build());
    }

    private void checkForMatchTrempToTrip(){

        final Query myUser=myRef.child("users").orderByChild("userId").equalTo(userAuth.getUid());

        Log.w("PRINT QUERY",myUser+"");

        myUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.w("look for match", dataSnapshot.toString());
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){

                }
//                user = dataSnapshot.getValue(User.class);
//                deleteTrempOrTrip();



            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("TAG", "onCancelled", databaseError.toException());
            }
        });

    }

    private void hasMatch(){
        Log.w("has match","here");
        if (currentTremps!=null){
            //final List<Tremp> newUserTremps=new ArrayList<>();
            for (final Tremp tremp : currentTremps) {
                if(tremp.getUserId().equals(userAuth.getUid())){
                    if (!tremp.isNotificationSent()){
                        for (Trip trip : currentTrips){
                            if (trip.getFromId().equals(tremp.getFromId())&&trip.getToId().equals(tremp.getToId()) && !trip.getUserId().equals(userAuth.getUid())) {
                                currentTremps.remove(tremp);
                                tremp.setNotificationSent(true);
                                currentTremps.add(tremp);
                                sendNotification();
                            }
                        }
                    }
                }
            }
            myRef.child("tremps").setValue(currentTremps);
            currentTremps.clear();
            myRef.child("trips").setValue(currentTrips);
            currentTrips.clear();

        }

    }

    private void deleteTrempOrTrip(){
        Query groupTremps=myRef.child("tremps");

        Log.w("PRINT QUERY",groupTremps+"");

        groupTremps.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.w("deleteTrempOrTrip","dataChange-tremp");
                long currentTime= System.currentTimeMillis();
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                   Tremp tremp =singleSnapshot.getValue(Tremp.class);
                   currentTremps.add(tremp);
                   if(tremp.getDepartureTime()+3600000*24<currentTime){
                        currentTremps.remove(tremp);
                   }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("TAG", "onCancelled", databaseError.toException());
            }
        });

        Query groupTrips=myRef.child("trips");

        Log.w("PRINT QUERY",groupTrips+"");

        groupTrips.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.w("deleteTrempOrTrip","dataChange-tremp");
                long currentTime= System.currentTimeMillis();
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    Trip trip =singleSnapshot.getValue(Trip.class);
                    currentTrips.add(trip);
                    if(trip.getDepartureTime()+3600000*24<currentTime){
                        currentTrips.remove(trip);
                    }
                }
                hasMatch();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("TAG", "onCancelled", databaseError.toException());
            }

        });

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


                if(!dataSnapshot.getValue().getClass().equals(String.class)) {
                    Log.w("notific","User is object user");
                }
                else {
                    Log.w("setMyRef",dataSnapshot.toString());
                    String groupOfUser=dataSnapshot.getValue(String.class);
                    myRef=database.getReference().child("Group").child(groupOfUser);
                    startTimer();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }




}
