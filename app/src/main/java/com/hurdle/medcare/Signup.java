package com.hurdle.medcare;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Signup extends AppCompatActivity {
    EditText useremail;
    EditText userpassword;
    EditText repassword;
    TextView notify;
    Button signup;
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        progressDialog = new ProgressDialog(this);
        signup=(Button)findViewById(R.id.signup);
        firebaseAuth=FirebaseAuth.getInstance();   //initializes firebase auth
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }
    public void register(){
        useremail=(EditText)findViewById(R.id.emailid);
        userpassword=(EditText)findViewById(R.id.passwords);
        repassword=(EditText)findViewById(R.id.Re_passwords);
        notify=(TextView)findViewById(R.id.notify);
        if(useremail.getText().toString().isEmpty()){
            useremail.setError("Username can't be empty");
        }
        else if(userpassword.getText().toString().isEmpty()){
            userpassword.setError("Password can't be empty");
        }
        else if(!userpassword.getText().toString().equals(repassword.getText().toString())){
            repassword.setError("Incorrect password");
        }
        else
        {
            progressDialog.setMessage("Logging In...");
            progressDialog.show();
            firebaseAuth.createUserWithEmailAndPassword(useremail.getText().toString(),userpassword.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressDialog.dismiss();
                   // Toast.makeText(signup.this,"Verification email send, please verify it to sign in",Toast.LENGTH_SHORT);
                    if(task.isSuccessful()){
                        FirebaseUser verification=firebaseAuth.getCurrentUser();
                        if(verification.isEmailVerified()) {
                            Toast.makeText(Signup.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                            finish();
                            Intent intent = new Intent(Signup.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else{
                                notify.setVisibility(View.VISIBLE);
                                Toast.makeText(Signup.this,"Verification email is send.Please verifiy your email to login!",Toast.LENGTH_LONG).show();
                                verification.sendEmailVerification();
                        }

                    }
                    else
                    {

                        Toast.makeText(Signup.this,"Registration Failed! Please try it again... or check your mail for authentication",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }
}
