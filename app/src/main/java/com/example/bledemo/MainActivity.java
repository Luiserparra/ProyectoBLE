package com.example.bledemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanResult;
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
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements BLEManagerCallerInterface, Dispositivos.OnFragmentInteractionListener,
        Servicios.OnFragmentInteractionListener ,Caracteristicas.OnFragmentInteractionListener, InfoCaracteristica.OnFragmentInteractionListener {

    public BLEManager bleManager;
    private MainActivity mainActivity;
    public String [] Devices;
    public volatile boolean run = true;

    public void go(){
        run = true;
        hilo.start();
    }
    public void stop(){
        run = false;
    }


String dispSelec="";
public void dispSelec(String ds){
    dispSelec=ds;

}
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
                    transition.addToBackStack(null);
                    System.out.println("INICIAR ESCANEO");
                    transition.commit();
                    go();
                }
            }
        });

        //BOTON DE DETENER ESCANEO
        FloatingActionButton detenerScna = (FloatingActionButton) findViewById(R.id.fab2);
        detenerScna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bleManager!=null){
                    Dispositivos frDispositivos = new Dispositivos();
                    FragmentTransaction transition =  getSupportFragmentManager().beginTransaction();
                    transition.replace(R.id.contenedor,frDispositivos);
                    System.out.println("DETENER ESCANEO");
                    transition.commit();
                    stop();
                    System.out.println("Mate a potter :'(");
                }
            }
        });

        //BOTON DE CONECTARSE A DISPOSITIVO
        FloatingActionButton conectarse = (FloatingActionButton) findViewById(R.id.fab3);
        conectarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("EL DISPOSITIVO SELECCIONADO ES: "+dispSelec);
                if(bleManager!=null){
                    if (!dispSelec.equals("")) {
                        //Guardar el dispositivo seleccionado
                        Bundle datosAEnviar = new Bundle();
                        datosAEnviar.putString("dispositivo", dispSelec);
                        //LLenar el array de servicios con los servicios del dispositivo seleccionado
                        String servicios[] = {"Servicio 1", " Servicio 2", "Servicio 3"};
                        datosAEnviar.putStringArray("servicios", servicios);

                        System.out.println("CONECTARSE");

                        //Inicializar el fragment de servicios y mandar los datos
                        Servicios frServicios = new Servicios();
                        frServicios.setArguments(datosAEnviar);

                        //Cambiar de fragment
                        FragmentTransaction transition = getSupportFragmentManager().beginTransaction();
                        transition.replace(R.id.contenedor, frServicios);
                        transition.addToBackStack(null);
                        transition.commit();
                        BluetoothDevice device = null;
                        for(ScanResult sr : bleManager.scanResults){
                            if (sr.getDevice().getName()!=null) {
                                if (sr.getDevice().getName().equals("Galaxy S7 edge")) {
                                    device = sr.getDevice();
                                }
                            }
                        }
                        bleManager.connectToGATTServer(device);

                    }
                }
            }
        });

        //BOTON DE DESCONECTARSE DE UN DISPOSITIVO
        FloatingActionButton desconectarse = (FloatingActionButton) findViewById(R.id.fab4);
        desconectarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bleManager!=null){
                    Dispositivos frDispositivos = new Dispositivos();
                    FragmentTransaction transition =  getSupportFragmentManager().beginTransaction();
                    transition.replace(R.id.contenedor,frDispositivos);
                    transition.commit();
                    System.out.println("DESCONECTARSE");
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

        Bundle datosAEnviar = new Bundle();
        System.out.println("DISPOSITIVOOOS"+Devices);
        datosAEnviar.putStringArray("Devices", Devices);

        FragmentTransaction transition =  getSupportFragmentManager().beginTransaction();
        Dispositivos frDispositivos = new Dispositivos();
        frDispositivos.setArguments(datosAEnviar);
        transition.replace(R.id.contenedor,frDispositivos);
        transition.commit();


    }

    @Override
    public void onBackPressed(){

            FragmentManager manager = getSupportFragmentManager();
            if(manager.getBackStackEntryCount() > 0 ) {
                manager.popBackStack();//Pops one of the added fragments
            }

        else {
            super.onBackPressed();
        }
    }

    /* @Override
     public boolean onCreateOptionsMenu(Menu menu) {
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
                    if (run) {
                        ListView listView = (ListView) findViewById(R.id.devices_list_id);
                        BluetoothDeviceListAdapter adapter = new BluetoothDeviceListAdapter(getApplicationContext(), bleManager.scanResults, mainActivity);
                        listView.setAdapter(adapter);
                        Devices = new String[adapter.scanResultList.size()];
                        for (int i = 0; i < adapter.scanResultList.size(); i++) {
                            String d = "Device: " + adapter.scanResultList.get(i).getDevice() + "; " + "Device Name: " + adapter.scanResultList.get(i).getDevice().getName() + "; " + "Signal: " + adapter.scanResultList.get(i).getRssi() + "dBm";
                            Devices[i] = d;
                            System.out.println(d);
                        }
                    }
//                    ListView listView=(ListView)findViewById(R.id.devices_list_id);
     //               BluetoothDeviceListAdapter adapter=new BluetoothDeviceListAdapter(getApplicationContext(),bleManager.scanResults,mainActivity);
   //                 listView.setAdapter(adapter)
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
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            while (run) {
                try {
                    System.out.println("Me imprimo cada 5 segundos");
                    bleManager.scanDevices();
                    Thread.sleep(5000);
                    System.out.println("EL valor de Run es "+run);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };
    Thread hilo = new Thread(runnable);
}
