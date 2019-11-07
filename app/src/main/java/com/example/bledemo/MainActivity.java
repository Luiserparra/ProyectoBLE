package com.example.bledemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import com.example.bledemo.adapters.BluetoothDeviceListAdapter;
import com.example.bledemo.ble.BLEManager;
import com.example.bledemo.ble.BLEManagerCallerInterface;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements BLEManagerCallerInterface, Dispositivos.OnFragmentInteractionListener,
        Servicios.OnFragmentInteractionListener, Caracteristicas.OnFragmentInteractionListener {

    public BLEManager bleManager;
    private MainActivity mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //BOTON DE INICIAR ESCANEO
        FloatingActionButton iniciarScan = (FloatingActionButton) findViewById(R.id.fab1);
        iniciarScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bleManager!=null){
                    Dispositivos frDispositivos = new Dispositivos();
                    FragmentTransaction transition =  getSupportFragmentManager().beginTransaction();
                    transition.replace(R.id.contenedor,frDispositivos);
                    transition.commit();
                    bleManager.scanDevices();
                }
            }
        });

        //BOTON DE DETENER ESCANEO
        FloatingActionButton detenerScna = (FloatingActionButton) findViewById(R.id.fab1);
        detenerScna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bleManager!=null){
                    Dispositivos frDispositivos = new Dispositivos();
                    FragmentTransaction transition =  getSupportFragmentManager().beginTransaction();
                    transition.replace(R.id.contenedor,frDispositivos);
                    transition.commit();
                    bleManager.scanDevices();
                }
            }
        });

        //BOTON DE CONECTARSE A DISPOSITIVO
        FloatingActionButton conectarse = (FloatingActionButton) findViewById(R.id.fab1);
        conectarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bleManager!=null){

                }
            }
        });

        //BOTON DE DESCONECTARSE A DISPOSITIVO
        FloatingActionButton desconectarse = (FloatingActionButton) findViewById(R.id.fab1);
        desconectarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bleManager!=null){
                    Dispositivos frDispositivos = new Dispositivos();
                    FragmentTransaction transition =  getSupportFragmentManager().beginTransaction();
                    transition.replace(R.id.contenedor,frDispositivos);
                    transition.commit();
                    bleManager.scanDevices();
                }
            }
        });
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "Su dispositivo no es compatible con el servicio BLE", Toast.LENGTH_LONG).show();
            finish();
        }else{
            Toast.makeText(this, "Su dispositivo es compatible con los serivicios BLE, Bienvenido", Toast.LENGTH_LONG).show();
        }

        bleManager=new BLEManager(this,this);
        if(!bleManager.isBluetoothOn()){
            bleManager.enableBluetoothDevice(this, 1001);
        }else{
            bleManager.requestLocationPermissions(this,1002);
        }
        mainActivity=this;
        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mReceiver, filter);
        //Deshabilitar el BLE
        //bleManager.disable();

        Dispositivos frDispositivos = new Dispositivos();
        FragmentTransaction transition =  getSupportFragmentManager().beginTransaction();
        transition.replace(R.id.contenedor,frDispositivos);
        transition.commit();


    }

    @Override
    /*public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }*/
    public void onStop() {
        super.onStop();

        // ...

        // Unregister broadcast listeners
        unregisterReceiver(mReceiver);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        boolean allPermissionsGranted=true;
        if (requestCode == 1002) {
            for (int currentResult:grantResults
            ) {
                if(currentResult!= PackageManager.PERMISSION_GRANTED){
                    allPermissionsGranted=false;
                    break;
                }
            }
            if(!allPermissionsGranted){
                AlertDialog.Builder builder=new AlertDialog.Builder(this)
                        .setTitle("Permissions")
                        .setMessage("Camera and Location permissions must be granted in order to execute the app")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                builder.show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try{

            if(requestCode==1001){
                if(resultCode!=Activity.RESULT_OK){
                    //Loop para que si o si prenda el BLE (QUITABLE)
                    if(!bleManager.isBluetoothOn()){
                        bleManager.enableBluetoothDevice(this, 1001);
                    }
                }else{
                    bleManager.requestLocationPermissions(this,1002);
                }
            }


        }catch (Exception error){

        }
    }

    @Override
    public void scanStartedSuccessfully() {

    }

    @Override
    public void scanStoped() {

    }

    @Override
    public void scanFailed(int error) {

    }

    @Override
    public void newDeviceDetected() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try{
//                    ListView listView=(ListView)findViewById(R.id.devices_list_id);
     //               BluetoothDeviceListAdapter adapter=new BluetoothDeviceListAdapter(getApplicationContext(),bleManager.scanResults,mainActivity);
   //                 listView.setAdapter(adapter);

                }catch (Exception error){

                }

            }
        });


    }
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                        BluetoothAdapter.ERROR);
                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        // Bluetooth has been turned off;
                        Toast.makeText(context, "Se apago el BLE", Toast.LENGTH_LONG).show();
                        bleManager.enableBluetoothDevice(mainActivity, 1001);
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        // Bluetooth is turning off;
                        break;
                    case BluetoothAdapter.STATE_ON:
                        // Bluetooth has been on
                        Toast.makeText(context, "Se prendio el BLE", Toast.LENGTH_LONG).show();
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        // Bluetooth is turning on
                        break;
                }
            }
        }
    };

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
