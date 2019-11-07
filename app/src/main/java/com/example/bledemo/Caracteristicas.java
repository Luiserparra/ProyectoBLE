package com.example.bledemo;

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
        String servicio = datosRecuperados.getString("servicio");
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
                datosAEnviar.putString("caracteristica", caracteristicas[i]);

                //LLenar el array la info de la catacertistica seleccionada
                String UUID="12345";
                String Re="R";
                String W="W";
                String N="N";
                String descriptor="Descripcion";
                String info[]={"UUIDS: "+UUID,Re,W,N,"Descripción: "+descriptor };
                datosAEnviar.putStringArray("info", info);

                //Inicializar el fragment de caracteristicas y mandar los datos
                InfoCaracteristica frCaracteristicas = new InfoCaracteristica();
                frCaracteristicas.setArguments(datosAEnviar);

                //Cambiar de fragment
                FragmentTransaction transition = getFragmentManager().beginTransaction();
                transition.replace(R.id.contenedor, frCaracteristicas);
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
}
