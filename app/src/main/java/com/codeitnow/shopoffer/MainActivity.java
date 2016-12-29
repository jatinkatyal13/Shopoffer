package com.codeitnow.shopoffer;

import android.Manifest;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.codeitnow.shopoffer.network.ApiClient;
import com.codeitnow.shopoffer.network.NetworkDataManager;
import com.codeitnow.shopoffer.network.responses.ApiResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    private static final String TAG = MainActivity.class.getSimpleName();
    private ArrayAdapter<String> arrayAdapter;
    private LinearLayout mContainer;
    private ProgressDialog progressDialog;
    private static String[] mPermissions = { Manifest.permission.ACCESS_FINE_LOCATION};
    private MyApp.OnListRefreshListener onListRefreshListener;

    @Override
    protected void onResume() {
        super.onResume();
        onListRefreshListener = new MyApp.OnListRefreshListener() {
            @Override
            public void onListRefresh() {
                notifyListChange();
            }
        };
        MyApp.getInstance().onListRefreshListener = onListRefreshListener;
        MyApp.getInstance().context = this;
    }

    private void notifyListChange(){
        if(arrayAdapter != null){
            List<String> items = new ArrayList<>(MyApp.getInstance().regionNameList);
            arrayAdapter.clear();
            //arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
            for (int i = 0; i < items.size(); i++) {
                int k = Integer.valueOf(items.get(i));
                //Log.d("s",String.valueOf(k));
               // Toast.makeText(this, String.valueOf(k), Toast.LENGTH_SHORT).show();
                if(k==1)
                {
                    arrayAdapter.add("Pizza Hut");
                }
                else if(k==2)
                {
                    arrayAdapter.add("Dominos");
                }
                else if(k==3)
                {
                    arrayAdapter.add("Burger King");
                }
                else if(k==4)
                {
                    arrayAdapter.add("KFC");
                }
                else if(k==5)
                {
                    arrayAdapter.add("Coding Ninjas");
                }
                else if(k==6)
                {
                    arrayAdapter.add("Kinley");
                }
                else if(k==7)
                {
                    arrayAdapter.add("Adidas");
                }
            }
//            arrayAdapter.addAll(items);
            arrayAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!havePermissions()) {
            Log.i(TAG, "Requesting permissions needed for this app.");
            requestPermissions();
        }

        if (!isBlueEnable()) {
            Intent bluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(bluetoothIntent);
        }

        listView = (ListView) findViewById(R.id.region);
        List<String> items = new ArrayList<>(MyApp.getInstance().regionNameList);
        //List<String>names = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<>(this, R.layout.contactlist);
        for (int i = 0; i < items.size(); i++) {
            int k = Integer.valueOf(items.get(i));
            //Log.d("s",String.valueOf(k));
            Toast.makeText(this, String.valueOf(k), Toast.LENGTH_SHORT).show();
            if(k==1)
            {
                arrayAdapter.add("Pizza Hut");
            }
            else if(k==2)
            {
                arrayAdapter.add("Dominos");
            }
            else if(k==3)
            {
                arrayAdapter.add("Burger King");
            }
            else if(k==4)
            {
                arrayAdapter.add("KFC");
            }
            else if(k==5)
            {
                arrayAdapter.add("Coding Ninjas");
            }
            else if(k==6)
            {
                arrayAdapter.add("Kinley");
            }
            else if(k==7)
            {
                arrayAdapter.add("Adidas");
            }
        }
        //arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, names);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (!MyApp.getInstance().regionList.isEmpty()) {
                    try {
                        String beaconSSN = MyApp.getInstance().regionList.get(i).getId2().toHexString();
                        Intent regionIntent = new Intent(MainActivity.this,ShowOffer.class);
                        regionIntent.putExtra("beacon_ssn",beaconSSN);
                        regionIntent.putExtra("name", MyApp.getInstance().regionNameList.get(i));
                        startActivity(regionIntent);
                    } catch (ArrayIndexOutOfBoundsException e) {/*Do nothing*/}
                }
            }


        });
        listView.setAdapter(arrayAdapter);
    }

    private boolean isBlueEnable() {
        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
        return bluetoothAdapter.isEnabled();

    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this,
                mPermissions, PERMISSIONS_REQUEST_CODE);
    }

    private boolean havePermissions() {
        for(String permission:mPermissions){
            if(ActivityCompat.checkSelfPermission(this,permission)!= PackageManager.PERMISSION_GRANTED){
                return  false;
            }
        }
        return true;
    }

    private void enterRegion(final String beaconSSN){
        NetworkDataManager<ApiResponse> manager = new NetworkDataManager<>();
        NetworkDataManager.NetworkResponseListener listener = manager.new NetworkResponseListener() {
            @Override
            public void onSuccessResponse(ApiResponse response) {
                Log.i("TAG","Enter Update Success for beacon: " + beaconSSN);
            }

            @Override
            public void onFailure(int code, String message) {
                Log.i("TAG","Enter Update Fail for beacon: " + beaconSSN);
            }
        };
        Call<ApiResponse> call= ApiClient.authorizedApiService().addUserInRegion(beaconSSN);
        manager.execute(call,listener);
    }

    private void exitRegion(final String beaconSSN){
        NetworkDataManager<ApiResponse> manager = new NetworkDataManager<>();
        NetworkDataManager.NetworkResponseListener listener = manager.new NetworkResponseListener() {
            @Override
            public void onSuccessResponse(ApiResponse response) {
                Log.i("TAG","Exit Update Success for beacon: " + beaconSSN);
            }

            @Override
            public void onFailure(int code, String message) {
                Log.i("TAG","Exit Update Fail for beacon: " + beaconSSN);
            }
        };
        Call<ApiResponse> call = ApiClient.authorizedApiService().removeUserFromRegion(beaconSSN);
        manager.execute(call,listener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        MyApp.getInstance().onListRefreshListener = null;
        MyApp.getInstance().context = null;
    }
}
