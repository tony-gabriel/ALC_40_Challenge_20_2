package com.example.alc_40_challenge_20;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DealActivity extends AppCompatActivity {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    EditText txt_title;
    EditText txt_price;
    EditText txt_description;
    TravelDeal deal;

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

        Intent intent = getIntent();
        TravelDeal deal = (TravelDeal) intent.getSerializableExtra("Deal");
        if(deal==null) {

            deal = new TravelDeal();
        }
        this.deal = deal;
        txt_title.setText(deal.getTitle());
        txt_description.setText(deal.getDescription());
        txt_price.setText(deal.getPrice());

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu,menu);

        return true;
    }

    private void saveDeal(){

        deal.setTitle( txt_title.getText().toString());
        deal.setDescription( txt_description.getText().toString());
        deal.setPrice(txt_price.getText().toString());
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
    }

    private void backToList(){

        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);
    }

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
