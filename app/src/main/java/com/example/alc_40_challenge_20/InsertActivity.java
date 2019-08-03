package com.example.alc_40_challenge_20;

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

public class InsertActivity extends AppCompatActivity {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    EditText txt_title;
    EditText txt_price;
    EditText txt_description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child("traveldeals");
        txt_title = findViewById(R.id.txt_title);
        txt_price = findViewById(R.id.txt_price);
        txt_description = findViewById(R.id.txt_description);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu,menu);
        return true;
    }

    private void saveDeal(){

        String title = txt_title.getText().toString();
        String description = txt_description.getText().toString();
        String price = txt_price.getText().toString();
        TravelDeal deals = new TravelDeal(title, description, price, "");
        mDatabaseReference.push().setValue(deals);
    }

    private void clean(){

        txt_title.setText("");
        txt_price.setText("");
        txt_description.setText("");
        txt_title.requestFocus();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.save_menu:
                   saveDeal();
                Toast.makeText(this, "Deal saved", Toast.LENGTH_LONG).show();
                  clean();
                return true;
                default:
                    return super.onOptionsItemSelected(item);
        }

    }
}
