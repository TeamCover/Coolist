package com.flipbox.cover.coolist.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.flipbox.cover.coolist.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegPassFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RegPassFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegPassFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String ARG_EMAIL = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Button btnNext;
    private EditText txtPaswsword;

    // TODO: Rename and change types of parameters
    private String email;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    // TODO: Rename and change types and number of parameters
    public static RegPassFragment newInstance(String param1, String param2) {
        RegPassFragment fragment = new RegPassFragment();
        Bundle args = new Bundle();
        args.putString(ARG_EMAIL, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public RegPassFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reg_pass, container, false);
        btnNext = (Button)view.findViewById(R.id.btnNext);
        txtPaswsword = (EditText)view.findViewById(R.id.userPass);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass = txtPaswsword.getText().toString();
                onButtonPressed(pass);
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle arg = getArguments();
        if (arg != null) {
            email = getArguments().getString(ARG_EMAIL);
            TextView name = (TextView)getView().findViewById(R.id.Username);
            name.setText(email);
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(final String uri) {
        if (mListener != null) {
            mListener.onFragmentPassInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentPassInteraction(String uri);
    }

}
