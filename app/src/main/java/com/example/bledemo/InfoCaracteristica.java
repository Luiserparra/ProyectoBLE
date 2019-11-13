package com.example.bledemo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InfoCaracteristica.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InfoCaracteristica#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InfoCaracteristica extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public InfoCaracteristica() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InfoCaracteristica.
     */
    // TODO: Rename and change types and number of parameters
    public static InfoCaracteristica newInstance(String param1, String param2) {
        InfoCaracteristica fragment = new InfoCaracteristica();
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
    private EditText nuevoValET;
    private View v;
    private String info[];
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_info_caracteristica, container, false);
        lv1=v.findViewById(R.id.lsInfoCaracteristica);
        tv1=v.findViewById(R.id.txtInfoCaract);
        //Recibir datos
        Bundle datosRecuperados = getArguments();
        final String caracteristica = datosRecuperados.getString("caracteristica");
        String ClaseCaract= datosRecuperados.getString("ClaseCaract");
        final String ValorCaracteristica="El valor de la caracteristica";
        info= datosRecuperados.getStringArray("info");
        tv1.setText("Informacion de "+caracteristica);
        //LLenar la lista con los datos
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, info);
        lv1.setAdapter(adapter);
        lv1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i==1 && info[1].equals("Leible")){
                    //POP UP PARA LEER LA CARACTERISTICA
                    AlertDialog.Builder builder =new AlertDialog.Builder(getActivity());
                    builder.setCancelable(true);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    builder.setTitle(caracteristica);
                    builder.setMessage(ValorCaracteristica);
                    builder.show();
                }else {
                    if (i==2 && info[2].equals("Editable")){
                        AlertDialog.Builder builder =new AlertDialog.Builder(getActivity());
                        LayoutInflater inflater1 =getActivity().getLayoutInflater();
                        View view1=inflater1.inflate(R.layout.write_dialog,null);
                        nuevoValET=view1.findViewById(R.id.nuevoVal);

                        builder.setCancelable(true);
                        builder.setView(view1);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                final String nuevoVal=nuevoValET.getText().toString();
                                System.out.println("NUEVO VAL: "+nuevoVal);
                                //SETEAR EL nuevoVal A LA CARACTERISTICA
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        builder.setTitle("Nuevo valor");

                        builder.show();
                    }
                }

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
