package de.renekolb.loopinglouieextreme.ui;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.renekolb.loopinglouieextreme.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GameResultFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GameResultFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GameResultFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_FIRST = "FIRST_PLAYER";
    private static final String ARG_SECOND = "SECOND_PLAYER";
    private static final String ARG_THIRD = "THIRD_PLAYER";
    private static final String ARG_FOURTH = "FOURTH_PLAYER";


    private int mFirstPlayer;
    private int mSecondPlayer;
    private int mThirdPlayer;
    private int mFourthPlayer;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GameResultFragment.
     */
    public static GameResultFragment newInstance(int firstPlayer, int secondPlayer, int thirdPlayer, int fourthPlayer) {
        GameResultFragment fragment = new GameResultFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_FIRST, firstPlayer);
        args.putInt(ARG_SECOND, secondPlayer);
        args.putInt(ARG_THIRD, thirdPlayer);
        args.putInt(ARG_FOURTH, fourthPlayer);

        Log.i("GameResults","1: "+firstPlayer+" 2: "+secondPlayer+" 3: "+thirdPlayer+" 4: "+fourthPlayer);

        fragment.setArguments(args);
        return fragment;
    }

    public GameResultFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mFirstPlayer = getArguments().getInt(ARG_FIRST);
            mSecondPlayer = getArguments().getInt(ARG_SECOND);
            mThirdPlayer = getArguments().getInt(ARG_THIRD);
            mFourthPlayer = getArguments().getInt(ARG_FOURTH);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_game_result, container, false);

        final TextView tvFirst = (TextView) view.findViewById(R.id.tv_game_results_first_player);
        final TextView tvSecond = (TextView) view.findViewById(R.id.tv_game_results_second_player);
        final TextView tvThird = (TextView) view.findViewById(R.id.tv_game_results_third_player);
        final TextView tvFourth = (TextView) view.findViewById(R.id.tv_game_results_fourth_player);

        tvFirst.setText("1. "+getPlayerNameFromIndex(mFirstPlayer)+"er Spieler");
        tvSecond.setText("2. "+getPlayerNameFromIndex(mSecondPlayer)+"er Spieler");
        if(mThirdPlayer != -1) {
            tvThird.setText("3. " + getPlayerNameFromIndex(mThirdPlayer) + "er Spieler");
        }else{
            tvThird.setVisibility(View.GONE);
        }
        if(mFourthPlayer != -1) {
            tvFourth.setText("4. " + getPlayerNameFromIndex(mFourthPlayer) + "er Spieler");
        }else{
            tvFourth.setVisibility(View.GONE);
        }

        return view;
    }

    public void onButtonPressed(int button) {
        if (mListener != null) {
            mListener.onFragmentInteraction(button);
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

    private String getPlayerNameFromIndex(int playerIndex){
        switch (playerIndex){
            case 0: return "Rot";
            case 1: return "Lila";
            case 2: return "Gelb";
            case 3: return "Grün";
            default: return "UNKNOWN";
        }
    }

}
