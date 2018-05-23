package com.example.student.trempme;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity{
    FirebaseUser userAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;
    Button btnSignOut, btnGoToNewTremp,btnGoToNewTrip,btnShowAllTremps,btnShowAllTrips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent=new Intent(this,MyTrempsRequestsActivity.class);
        startActivity(intent);
        setBtnSignOutListener();
        SetIntentButtons();




    }

    @Override
    protected void onStart() {
        super.onStart();
        setFirebaseVariables();
        Log.e("PRINT USER AUTH",userAuth+"");
        if(userAuth!=null){

            Query user=myRef.child("User").orderByChild(userAuth.getUid());

            Log.e("PRINT QUERY",user+"");

            user.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.w("TAG",dataSnapshot+"");
                    hasAllDitails(dataSnapshot);

                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e("TAG", "onCancelled", databaseError.toException());
                }
            });

        }else{
            Intent intent=new Intent(MainActivity.this,SignInActivity.class);
            startActivity(intent);
        }


    }



    private void setFirebaseVariables(){
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        userAuth = FirebaseAuth.getInstance().getCurrentUser();
    }



    public void hasAllDitails(DataSnapshot dataSnapshot){
        User user =dataSnapshot.child(userAuth.getUid()).getValue(User.class);
        if(user.getFullName().equals("")||user.getFullName().equals("")){
            Intent intent =new Intent(MainActivity.this,JoinGroupActivity.class);
            startActivity(intent);
        }
    }

    private void SetIntentButtons(){
        btnGoToNewTremp=findViewById(R.id.btnGoToCreateTremp);
        btnGoToNewTremp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,AskForTrempActivity.class);
                startActivity(intent);
            }
        });

        btnGoToNewTrip=findViewById(R.id.btnGoToCreateTrip);
        btnGoToNewTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,CreateNewTripActivity.class);
                startActivity(intent);
            }
        });

        btnShowAllTremps=findViewById(R.id.btnShowAllTremps);
        btnShowAllTremps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,ShowAllTrempsActivity.class);
                startActivity(intent);
            }
        });

        btnShowAllTrips=findViewById(R.id.btnShowAllTrips);
        btnShowAllTrips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,ShowAllTripsActivity.class);
                startActivity(intent);
            }
        });

    }



    private void setBtnSignOutListener(){
        btnSignOut=findViewById(R.id.btnSignOut);
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });
    }

    private void signOut(){
        FirebaseAuth.getInstance().signOut();
        Intent intent=new Intent(MainActivity.this,SignInActivity.class);
        startActivity(intent);

    }


}
