package com.example.kendryayu.kendryayu_1202160048_si4008_pab_modul4;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.kendryayu.kendryayu_1202160048_si4008_pab_modul4.Model.MenuProduk;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class TambahMenu extends AppCompatActivity {

    TextInputLayout mTitle, mHarga, mDeskripsi;

    ImageView imageView;

    Button mChooseImage;

    FirebaseFirestore firestore;

    StorageReference storage;

    private FirebaseAuth mAuth;

    private Uri imageUri;


    String stringUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_menu);

        mAuth = FirebaseAuth.getInstance();

        mTitle = findViewById(R.id.namamenu);
        mHarga = findViewById(R.id.hargamenu);
        mDeskripsi = findViewById(R.id.deskmenu);

        storage = FirebaseStorage.getInstance().getReference();

        firestore = FirebaseFirestore.getInstance();

        mChooseImage = findViewById(R.id.btn_choose_image);
        imageView = findViewById(R.id.img_post);

        mChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickImage = new Intent(Intent.ACTION_PICK);
                pickImage.setType("image/*");

                //Mulai intent untuk memilih foto dan mendapatkan hasil
                startActivityForResult(pickImage, 1);

            }
        });
    }

    public void tambahproduk(View view) {

        StorageReference filepath = storage.child("Produk").child(mTitle.getEditText().getText().toString());

        //Mendapatkan gambar dari Imageview untuk diupload
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = imageView.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        final UploadTask task = filepath.putBytes(data);

        task.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            //Method ketika upload gambar berhasil
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //Inisialisasi post untuk disimpan di FirebaseDatabase
//                Task<Uri> aa = task.getSnapshot().getMetadata().getReference().getDownloadUrl();
//                stringUri = aa.toString();
                task.getSnapshot().getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        stringUri = uri.toString();
                        CollectionReference dbProduk = firestore.collection("Menu");
                        MenuProduk user = new MenuProduk(mTitle.getEditText().getText().toString(),mHarga.getEditText().getText().toString(),mDeskripsi.getEditText().getText().toString(),stringUri);
                        dbProduk.add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(TambahMenu.this, "Upload berhasil", Toast.LENGTH_SHORT).show();
                                Intent pindah = new Intent(TambahMenu.this,MainActivity.class);
                                startActivity(pindah);
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(TambahMenu.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                    //Ketika upload gambar gagal
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(TambahMenu.this, "Gagal Upload!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {

                //Mendapatkan data dari intent
                imageUri = data.getData();
                try {
                    //Merubah data menjadi inputstream yang diolah menjadi bitmap dan ditempatkan pada imageview
                    InputStream stream = getContentResolver().openInputStream(imageUri);
                    Bitmap gambar = BitmapFactory.decodeStream(stream);
                    imageView.setImageBitmap(gambar);
                } catch (FileNotFoundException e) {
                    Log.w("FileNotFoundException", e.getMessage());
                    Toast.makeText(this, "Tidak dapat mengupload gambar", Toast.LENGTH_SHORT).show();
                }
            }

            //Ketika user tidak memilih foto
        } else {
            Toast.makeText(this, "Tidak ada gambar yang dipilih", Toast.LENGTH_SHORT).show();
        }

    }
}
