package com.codeitnow.shopoffer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.database.DatabaseReference;

public class ShowOffer extends AppCompatActivity {

    TextView shopname,shopoffer,shopdesc;
    int id;
    final static String DB_URL = "https://shopoffer-464d9.firebaseio.com/shops/";
    private DatabaseReference mDatabase;
    private static final String TAG = MainActivity.class.getSimpleName();
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_offer);
        Firebase.setAndroidContext(this);
        Firebase ref = new Firebase(DB_URL);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    //Getting the data from snapshot
                    Shop shop = postSnapshot.getValue(Shop.class);
                    if(id==Integer.parseInt(shop.getId())) {
                        shopname.setText(shop.getName());
                        shopoffer.setText(shop.getOffer());
                        shopdesc.setText(shop.getDesc());
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        shopname = (TextView) findViewById(R.id.shopname);
        shopoffer = (TextView) findViewById(R.id.shopoffer);
        shopdesc = (TextView) findViewById(R.id.shopdesc);
      /*  mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl(DB_URL);
        if (TextUtils.isEmpty(userId)) {
            userId = mDatabase.push().getKey();
        }
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Shop shop = dataSnapshot.getValue(Shop.class);
                shopname.setText(shop.getDesc());
           }
           @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
*/
        Intent i = getIntent();
        if(i.hasExtra("beacon_ssn")){
//            beaconSSN = i.getStringExtra("beacon_ssn");
//            fetchUsers();
        }
        if(i.hasExtra("name")){
            String name = i.getStringExtra("name");
            id = Integer.parseInt(name);
//            getSupportActionBar().setTitle(name);
//            TextView textView = (TextView)findViewById(R.id.region_detail_text_view);
//            textView.setText("Users Inside " + name);
        }
    }
}
