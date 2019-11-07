package com.example.bledemo;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import static android.widget.Toast.LENGTH_LONG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Dispositivos.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Dispositivos#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Dispositivos extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Dispositivos() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Dispositivos.
     */
    // TODO: Rename and change types and number of parameters
    public static Dispositivos newInstance(String param1, String param2) {
        Dispositivos fragment = new Dispositivos();
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
    private String dispositivos[]={"Dispositivo 1","Dispositivo 2","Dispositivo 3","Dispositivo 4"};
    boolean conectado=true;
    private View v;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        v=inflater.inflate(R.layout.fragment_dispositivos, container, false);
        lv1=v.findViewById(R.id.lsDispositivos);

        //LLenar la lista de sipositivos
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, dispositivos);
        lv1.setAdapter(adapter);
        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        lv1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
            if (conectado) {


                //Guardar el dispositivo seleccionado
                Bundle datosAEnviar = new Bundle();
                datosAEnviar.putString("dispositivo", dispositivos[i]);

                //Inicializar el fragment de servicios y mandar los datos
                Servicios frServicios = new Servicios();
                frServicios.setArguments(datosAEnviar);

                //Cambiar de fragment
                FragmentTransaction transition = getFragmentManager().beginTransaction();
                transition.replace(R.id.contenedor, frServicios);
                transition.commit();
            }
                return false;
            }
        });
        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }



        });


        return v;
    }
    public void conectarse(){

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
}
