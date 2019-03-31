package com.example.kendryayu.kendryayu_1202160048_si4008_pab_modul4;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kendryayu.kendryayu_1202160048_si4008_pab_modul4.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Collection;
import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;

    TextInputLayout mName, mEmail, mPassword;

    TextView textView;
    FirebaseAuth.AuthStateListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        listener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    Intent pindah = new Intent(RegisterActivity.this, LoginActivity.class);
                    pindah.addFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                    startActivity(pindah);
                }
            }
        };

        firestore = FirebaseFirestore.getInstance();
        mName = findViewById(R.id.nama);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);

        textView = findViewById(R.id.pindahLogin);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent aj = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(aj);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(listener);
    }


    public void daftarAkun(View view) {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        final String name = mName.getEditText().getText().toString();
        final String email = mEmail.getEditText().getText().toString();
        final String password = mPassword.getEditText().getText().toString();

        // TODO: Implement your own signup logic here.

        //Jika tidak :

        //Membuat user baru berdasarkan input user
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //Kondisi jika pembuatan user berhasil
                if (task.isSuccessful()) {

                    CollectionReference dbUser = firestore.collection("User");
                    User user = new User(name,email,password);
                    dbUser.add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(RegisterActivity.this, "Register Berhasil", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                            finish();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RegisterActivity.this, "Register Gagal", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });



                    //Kondisi ketika pembuatan user baru gagal
                } else {
                    Log.w("Firebase", task.getException());
                    Toast.makeText(RegisterActivity.this, "Gagal membuat akun baru!", Toast.LENGTH_SHORT).show();
                    mEmail.getEditText().setText(null);
                    mPassword.getEditText().setText(null);
                    mName.getEditText().setText(null);
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(listener);
    }


    public void onSignupSuccess() {
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Signup failed", Toast.LENGTH_LONG).show();

    }

    public boolean validate() {
        boolean valid = true;

        String name = mName.getEditText().getText().toString();
        String email = mEmail.getEditText().getText().toString();
        String password = mPassword.getEditText().getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            mName.getEditText().setError("Minimal image_3 karakter");
            valid = false;
        } else {
            mName.getEditText().setError(null);
        }


        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmail.getEditText().setError("Masukan alamat email valid");
            valid = false;
        } else {
            mEmail.getEditText().setError(null);
        }


        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            mPassword.getEditText().setError("Masukan password antara image_4 hingga 10 karakter");
            valid = false;
        } else {
            mPassword.getEditText().setError(null);
        }


        return valid;
    }
    }

