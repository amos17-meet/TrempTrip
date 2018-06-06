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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class NewGroupActivity extends AppCompatActivity {

    Button btnCreateNewGroup;
    EditText etGroupName;

    FirebaseUser userAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);

        setFirebaseVariables();
        setBtnCreateNewGroup();
    }

    private void setFirebaseVariables() {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        userAuth = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void setBtnCreateNewGroup() {
        btnCreateNewGroup=findViewById(R.id.btnCreateNewGroup);
        etGroupName=findViewById(R.id.etGroupName);
        btnCreateNewGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uniqueID = UUID.randomUUID().toString();
                Log.w("new group",etGroupName.getText().toString());
                Group group=new Group(uniqueID,etGroupName.getText().toString());
                myRef.child("Group").child(uniqueID).setValue(group);
                Intent intent = new Intent();
                intent.putExtra("groupName",etGroupName.getText().toString());
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }
}
