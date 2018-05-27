package com.example.student.trempme;

import android.content.Intent;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    EditText etEmail, etPassword,etConfirmPassword;
    Button btnSubmit;
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseUser userAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setBtnSubmitListener();
        setFirebaseVariables();

    }

    private void setFirebaseVariables(){
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
    }

    private void setBtnSubmitListener(){
        btnSubmit=findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);
    }

    private void createAuthAccount(){
        mAuth.createUserWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "createUserWithEmail:success");
                            userAuth = mAuth.getCurrentUser();
                            if(createUserAccount()) {
                                signIn();
                            }

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();


                        }

                        // ...
                    }

                });
    }

    private boolean createUserAccount(){
        User user=new User(userAuth.getUid(),"",
                etEmail.getText().toString().toLowerCase(),
                etPassword.getText().toString(),
                "",
                "",
                null,
                null
                );

        myRef.child("User").child(userAuth.getUid()).setValue(user);
        return true;
    }


    @Override
    public void onClick(View view) {
        etEmail=findViewById(R.id.etEmail);
        etPassword=findViewById(R.id.etPassword);
        etConfirmPassword=findViewById(R.id.etConfirmPassword);
        if(etPassword.getText().toString().equals(etConfirmPassword.getText().toString())){
            createAuthAccount();
        }else{
            Toast.makeText(SignUpActivity.this,"Passwordes Don't Mach",Toast.LENGTH_LONG).show();
        }

    }

    private void signIn(){
        mAuth=FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithEmail:success");
                            startService(new Intent(SignUpActivity.this, NotificationService.class));
                            Intent intent=new Intent(SignUpActivity.this,MainActivity.class);
                            startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithEmail:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });

    }




}
