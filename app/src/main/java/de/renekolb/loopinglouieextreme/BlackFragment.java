package de.renekolb.loopinglouieextreme;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BlackFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BlackFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public static BlackFragment newInstance() {
        BlackFragment fragment = new BlackFragment();
        return fragment;
    }

    public BlackFragment() {
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
        return inflater.inflate(R.layout.fragment_black, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String msg) {
        if (mListener != null) {
            mListener.onFragmentInteraction(msg);
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

}
