package com.example.bledemo;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.bledemo.ble.BLEManager;

import java.util.Date;
import java.util.List;
import java.util.UUID;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Caracteristicas.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Caracteristicas#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Caracteristicas extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Caracteristicas() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Caracteristicas.
     */
    // TODO: Rename and change types and number of parameters
    public static Caracteristicas newInstance(String param1, String param2) {
        Caracteristicas fragment = new Caracteristicas();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private ListView lv1;
    private TextView tv1;
    private String caracteristicas[];
    String servicio="";

    private View v;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v=inflater.inflate(R.layout.fragment_caracteristicas, container, false);
        lv1=v.findViewById(R.id.lsCaracteristicas);
        tv1=v.findViewById(R.id.txtCaracteristicas);
        System.out.println("TV1 = "+tv1);

        //Recibir datos
        Bundle datosRecuperados = getArguments();
        servicio = datosRecuperados.getString("servicio");
        caracteristicas= datosRecuperados.getStringArray("caracteristicas");
        tv1.setText("Caracteristicas de "+servicio);

        //LLenar la lista de Caracteristicas
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, caracteristicas);
        lv1.setAdapter(adapter);
        lv1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {


                //Guardar caracteristica seleccionada
                Bundle datosAEnviar = new Bundle();
                MainActivity ma = (MainActivity)getActivity();
                BLEManager bleManager = ma.getBleManager();
                BluetoothGattService s = bleManager.getGatt().getService(UUID.fromString(servicio));
                List<BluetoothGattCharacteristic> c = s.getCharacteristics();
                String t= caracteristicas[i].substring(0,caracteristicas[i].length()-1);
                datosAEnviar.putString("caracteristica", t);
                BluetoothGattCharacteristic caracteristica = s.getCharacteristic(UUID.fromString(t));
                //LLenar el array la info de la catacertistica seleccionada
                String UUID=caracteristica.getUuid().toString();
                String Re=(isCharacteristicReadable(caracteristica))?"R":"";
                String W=(isCharacteristicWriteable(caracteristica))?"W":"";
                String N=(isCharacteristicNotifiable(caracteristica))?"N":"";;
                String info[]={"UUIDS: "+UUID,Re,W,N};
                datosAEnviar.putStringArray("info", info);
                datosAEnviar.putString("servicio",servicio);
                //Inicializar el fragment de caracteristicas y mandar los datos
                InfoCaracteristica frInfoCaract = new InfoCaracteristica();
                frInfoCaract.setArguments(datosAEnviar);

                //Cambiar de fragment
                FragmentTransaction transition = getFragmentManager().beginTransaction();
                transition.replace(R.id.contenedor, frInfoCaract);
                transition.addToBackStack(null);
                transition.commit();
                ma.logs("Se ingresó a la información de la caracteristica "+caracteristica.getUuid().toString()+" "+new Date());
                return false;
            }
        });
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public boolean isCharacteristicWriteable(BluetoothGattCharacteristic characteristic) {
        return (characteristic.getProperties() &
                (BluetoothGattCharacteristic.PROPERTY_WRITE
                        | BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE)) != 0;
    }

    public boolean isCharacteristicReadable(BluetoothGattCharacteristic characteristic) {
        return ((characteristic.getProperties() & BluetoothGattCharacteristic.PROPERTY_READ) != 0);
    }

    public boolean isCharacteristicNotifiable(BluetoothGattCharacteristic characteristic) {
        return ((characteristic.getProperties() & BluetoothGattCharacteristic.PROPERTY_NOTIFY) != 0);
    }
}
