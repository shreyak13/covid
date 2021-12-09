package com.example.covid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity  {
private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(getApplicationContext());
        TextInputEditText editid=findViewById(R.id.editID);
        TextInputEditText editpass=findViewById(R.id.editpass);
        Button button=findViewById(R.id.buttonSignIn);
        TextView tosignup=findViewById(R.id.toSignUp);
        FirebaseAuth firebaseauth =FirebaseAuth.getInstance();
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        FirebaseFirestore db=FirebaseFirestore.getInstance();

        if(firebaseauth.getCurrentUser()!=null){
            progressDialog.setMessage("please wait .... Loading");
            progressDialog.show();
            String cur=firebaseauth.getCurrentUser().getEmail().trim();
            db.document("user/"+cur).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    user obj=documentSnapshot.toObject(user.class);
                    if(obj.getType()==0){
                        progressDialog.cancel();
                        startActivity(new Intent(getApplicationContext(),user_home.class));
                        finish();
                    }
                    else{
                        progressDialog.cancel();
                        startActivity(new Intent(getApplicationContext(),admin_home.class));
                        finish();
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                progressDialog.cancel();
                    Toast.makeText(MainActivity.this, "please sign in again", Toast.LENGTH_SHORT).show();
                }
            });

        }
        button.setOnClickListener((View.OnClickListener) this);
        tosignup.setOnClickListener((View.OnClickListener) this);


    }
    private FirebaseFirestore db;
    private TextInputLayout editID;
    private TextInputLayout editPass;
    private Button buttonSignIn;
    private TextView toSignUp;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private boolean verifyEmailId()
    {
        String emailId=editID.getEditText().getText().toString().trim();
        if(emailId.isEmpty())
        {   editID.setErrorEnabled(true);
            editID.setError("Email ID Required");
            return true;
        }
        else
        {
            editID.setErrorEnabled(false);
            return false;
        }
    }
    private boolean verifyPass()
    {
        String pass=editPass.getEditText().getText().toString().trim();
        if(pass.isEmpty())
        {   editPass.setErrorEnabled(true);
            editPass.setError("Password Required");
            return true;
        }
        else
        {
            editPass.setErrorEnabled(false);
            return false;
        }
    }
    }
