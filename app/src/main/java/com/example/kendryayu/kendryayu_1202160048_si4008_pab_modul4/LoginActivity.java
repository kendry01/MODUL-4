package com.example.kendryayu.kendryayu_1202160048_si4008_pab_modul4;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;

    TextInputLayout mName, mEmail, mPassword;

    TextView textView;
    FirebaseAuth.AuthStateListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        listener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    Intent pindah = new Intent(LoginActivity.this, MainActivity.class);
                    pindah.addFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                    startActivity(pindah);
                }
            }
        };

        firestore = FirebaseFirestore.getInstance();
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);

        textView = findViewById(R.id.pindahDaftar);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent aj = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(aj);
            }
        });
    }


    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(listener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(listener);
    }


    public void loginAkun(View view) {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        String email = mEmail.getEditText().getText().toString();
        String password = mPassword.getEditText().getText().toString();

        //cek kondisi inputan apakah ada atau tidak
        if (!TextUtils.isEmpty(email) || !TextUtils.isEmpty(password)) {

            //Login sesuai dengan username dan password user
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    //Ketika login berhasil
                    if (task.isSuccessful()) {
//                       onAuthSuccess(task.getResult().getUser());
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {

                                        Intent move = new Intent(LoginActivity.this, MainActivity.class);
                                        move.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(move);
                                        finish();
                                    }

                        //Ketika login gagal
                    } else {
                        Toast.makeText(LoginActivity.this, "Gagal Login, Periksa username dan password anda!", Toast.LENGTH_SHORT).show();
                    }

                    //Tutup dialog ketika login berhasil atau gagal
                }
            });

            //Kalau inputan kosong
        } else {
//            Snackbar.make(findViewById(R.id.rootlogin), "Data yang anda masukkan kosong!", Snackbar.LENGTH_LONG).show();
        }

    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login Gagal", Toast.LENGTH_LONG).show();


    }
    public boolean validate() {
        boolean valid = true;

        String email = mEmail.getEditText().getText().toString();
        String password = mPassword.getEditText().getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmail.getEditText().setError("Masukan alamat email yang valid!");
            valid = false;
        } else {
            mEmail.getEditText().setError(null);
        }

        if (password.isEmpty() || password.length() < 4) {
            mPassword.getEditText().setError("Password minimal image_4 karakter!");
            valid = false;
        } else {
            mPassword.getEditText().setError(null);
        }

        return valid;
    }
}
