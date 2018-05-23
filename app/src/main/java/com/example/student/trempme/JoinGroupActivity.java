package com.example.student.trempme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class JoinGroupActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etGroupId,etFullName;
    Button btnJoinGroup;

    FirebaseUser userAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group);

        setFirebaseVariables();
        setBtnJoinGroupListener();
    }

    private void setFirebaseVariables(){
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        userAuth = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void setBtnJoinGroupListener() {
        btnJoinGroup=findViewById(R.id.btnJoinGroup);
        btnJoinGroup.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view==btnJoinGroup){
            etFullName=findViewById(R.id.etFullName);
            etGroupId=findViewById(R.id.etGroupId);
            Query userQuery=myRef.child("User").orderByChild(userAuth.getUid());

            Log.e("PRINT QUERY",userQuery+"");

            userQuery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user=dataSnapshot.child(userAuth.getUid()).getValue(User.class);
                    user.setFullName(etFullName.getText().toString());
                    user.setGroupId(etGroupId.getText().toString());
                    myRef.child("User").child(userAuth.getUid()).setValue(user);
                    Intent intent=new Intent(JoinGroupActivity.this,MainActivity.class);
                    startActivity(intent);

                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e("TAG", "onCancelled", databaseError.toException());
                }
            });


        }
    }
}
