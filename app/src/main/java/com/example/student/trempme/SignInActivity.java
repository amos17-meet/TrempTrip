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
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {
    EditText etEmail,etPassword;
    Button btnSignIn, btnGoToSignUp;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        setBtnSignUpListener();

        setBtnGoToSignUpListener();
    }



    private void setBtnSignUpListener(){
        btnSignIn=findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view==btnSignIn) {
            etEmail = findViewById(R.id.etEmail);
            etPassword=findViewById(R.id.etPassword);
            auth=FirebaseAuth.getInstance();
            auth.signInWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("TAG", "signInWithEmail:success");
                                Intent intent=new Intent(SignInActivity.this,MainActivity.class);
                                startActivity(intent);

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("TAG", "signInWithEmail:failure", task.getException());
                                Toast.makeText(SignInActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }

                            // ...
                        }
                    });

        }

        if(view==btnGoToSignUp){
            Intent intent=new Intent(SignInActivity.this,SignUpActivity.class);
            startActivity(intent);

        }
    }

    private void setBtnGoToSignUpListener(){
        btnGoToSignUp=findViewById(R.id.btnGoToSignUp);
        btnGoToSignUp.setOnClickListener(this);
    }
}
