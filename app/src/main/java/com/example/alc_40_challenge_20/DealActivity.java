package com.example.alc_40_challenge_20;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class DealActivity extends AppCompatActivity {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private static final int PICTURE_RESULT = 42;
    EditText txt_title;
    EditText txt_price;
    EditText txt_description;
    ImageView imageView;
    TravelDeal deal;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("New Deal");

        FirebaseUtil.openFbReference("traveldeals", this);
        mFirebaseDatabase = FirebaseUtil.mFirebaseDatabase;
        mDatabaseReference = FirebaseUtil.mDatabaseReference;

        txt_title = findViewById(R.id.txt_title);
        txt_price = findViewById(R.id.txt_price);
        txt_description = findViewById(R.id.txt_description);
        imageView = findViewById(R.id.imageView);
        Button btnImage = findViewById(R.id.btnImage);
        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(intent.createChooser(intent, "Insert Picture"), PICTURE_RESULT);
            }
        });

        Intent intent = getIntent();
        TravelDeal deal = (TravelDeal) intent.getSerializableExtra("Deal");
        if(deal==null) {

            deal = new TravelDeal();
        }
        this.deal = deal;
        txt_title.setText(deal.getTitle());
        txt_description.setText(deal.getDescription());
        txt_price.setText(deal.getPrice());
        showImage(deal.getImageUrl());

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICTURE_RESULT && requestCode == RESULT_OK){
             final Uri ImageUri = data.getData();
            final StorageReference ref = FirebaseUtil.mStorageRef.child(ImageUri.getLastPathSegment());
            ref.putFile(ImageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        String pictureName = ImageUri.toString();
                        deal.setImageUrl(task.getResult().toString());
                        deal.setImageName(task.getResult().toString());
                    }
                }
            });


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu,menu);
//        if (FirebaseUtil.isAdmin){
//            menu.findItem(R.id.delete_menu).setVisible(true);
//            menu.findItem(R.id.save_menu).setVisible(true);
//            enableEditTexts(true);
//        }
//        else{
//            menu.findItem(R.id.delete_menu).setVisible(false);
//            menu.findItem(R.id.save_menu).setVisible(false);
//            enableEditTexts(false);
//        }

        return true;
    }

    private void showImage(String url){
        if (url != null && url.isEmpty() == false){

            int width = Resources.getSystem().getDisplayMetrics().widthPixels;
            Picasso.with(this)
                    .load(url)
                    .resize(width, width*2/3)
                    .centerCrop()
                    .into(imageView);

        }

    }

    private void saveDeal(){

        deal.setTitle( txt_title.getText().toString());
        deal.setDescription( txt_description.getText().toString());
        deal.setPrice(txt_price.getText().toString());
        deal.setImageUrl(url);
       if (deal.getId()==null) {
           mDatabaseReference.push().setValue(deal);
       }
       else {
           mDatabaseReference.child(deal.getId()).setValue(deal);
       }
    }

    private void clean(){

        txt_title.setText("");
        txt_price.setText("");
        txt_description.setText("");
        txt_title.requestFocus();
    }

    private void deleteDeal(){

        if (deal==null){
            Toast.makeText(this, "Please save the deal before deleting", Toast.LENGTH_LONG).show();
            return;
        }
        mDatabaseReference.child(deal.getId()).removeValue();

        if (deal.getImageName() != null && deal.getImageName().isEmpty() == false){
            StorageReference picRef = FirebaseUtil.mStorage.getReference().child(deal.getImageName());
            picRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(getApplicationContext(), "Deal deleted", Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Delete failed", Toast.LENGTH_LONG).show();
                    Log.d("Delete failed", e.getMessage());
                }
            });
        }
    }

    private void backToList(){

        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);
    }

//    private void enableEditTexts(boolean isEnabled){
//        txt_title.setEnabled(isEnabled);
//        txt_description.setEnabled(isEnabled);
//        txt_price.setEnabled(isEnabled);
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.save_menu:
                   saveDeal();
                Toast.makeText(this, "Deal saved", Toast.LENGTH_LONG).show();
                  clean();
                  backToList();
                return true;

            case R.id.delete_menu:
                deleteDeal();
                Toast.makeText(this, "Deal deleted", Toast.LENGTH_LONG).show();
                backToList();
                return true;
                default:
                    return super.onOptionsItemSelected(item);
        }

    }
}
