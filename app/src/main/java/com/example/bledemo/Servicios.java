package com.example.bledemo;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
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

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Servicios.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Servicios#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Servicios extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Servicios() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Servicios.
     */
    // TODO: Rename and change types and number of parameters
    public static Servicios newInstance(String param1, String param2) {
        Servicios fragment = new Servicios();
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
        setHasOptionsMenu(true);
    }




    private ListView lv1;
    private TextView tv1;
    private String servicios[];

    private View v;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v=inflater.inflate(R.layout.fragment_servicios, container, false);
        lv1=v.findViewById(R.id.lsServicios);
        tv1=v.findViewById(R.id.txtServcios);

        //Recibir datos
        Bundle datosRecuperados = getArguments();
        String dispositivo = datosRecuperados.getString("dispositivo");
        servicios=datosRecuperados.getStringArray("servicios");
        tv1.setText("Servicios de "+dispositivo);

        //LLenar la lista de servicios
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, servicios);
        lv1.setAdapter(adapter);

        lv1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {


                //Guardar el servicio seleccionado
                Bundle datosAEnviar = new Bundle();
                datosAEnviar.putString("servicio", servicios[i]);

                //LLenar el array las caracteristicas del servicio seleccionado
                MainActivity ma = (MainActivity)getActivity();
                BLEManager bleManager = ma.getBleManager();
                List <BluetoothGattService> services = bleManager.getGatt().getServices();
                BluetoothGattService service = null;
                for(BluetoothGattService s : services){
                    if(servicios[i].equals(s.getUuid().toString())){
                        service = s;
                        break;
                    }
                }
                List<BluetoothGattCharacteristic> characteristics = service.getCharacteristics();
                String caracteristicas[]=new String[characteristics.size()];
                int j = 0;
                for (BluetoothGattCharacteristic c : characteristics){
                    String carac;
                    carac = c.getUuid().toString()+" ";
                    if(isCharacteristicReadable(c)){
                        carac = carac + "R";
                    }
                    if(isCharacteristicWriteable(c)){
                        carac = carac + "W";
                    }
                    if(isCharacteristicNotifiable(c)){
                        carac = carac + "N";
                    }
                    caracteristicas[j] = carac;
                    j++;
                }


                datosAEnviar.putStringArray("caracteristicas", caracteristicas);

                //Inicializar el fragment de caracteristicas y mandar los datos
                Caracteristicas frCaracteristicas = new Caracteristicas();
                frCaracteristicas.setArguments(datosAEnviar);

                //Cambiar de fragment
                FragmentTransaction transition = getFragmentManager().beginTransaction();
                transition.replace(R.id.contenedor, frCaracteristicas);
                transition.addToBackStack(null);
                transition.commit();

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
