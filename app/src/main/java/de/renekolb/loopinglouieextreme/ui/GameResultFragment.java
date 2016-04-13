package de.renekolb.loopinglouieextreme.ui;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import de.renekolb.loopinglouieextreme.DeviceRole;
import de.renekolb.loopinglouieextreme.FragmentUtils;
import de.renekolb.loopinglouieextreme.FullscreenActivity;
import de.renekolb.loopinglouieextreme.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
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

    //    private OnFragmentInteractionListener mListener;
    private FullscreenActivity fa;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment GameResultFragment.
     */
    public static GameResultFragment newInstance(int firstPlayer, int secondPlayer, int thirdPlayer, int fourthPlayer) {
        GameResultFragment fragment = new GameResultFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_FIRST, firstPlayer);
        args.putInt(ARG_SECOND, secondPlayer);
        args.putInt(ARG_THIRD, thirdPlayer);
        args.putInt(ARG_FOURTH, fourthPlayer);

        fragment.setArguments(args);
        return fragment;
    }

    public GameResultFragment() {
        // Required empty public constructor
    }

    public void setPlayers(int firstPlayer, int secondPlayer, int thirdPlayer, int fourthPlayer) {
//        Bundle args = new Bundle();
//        args.putInt(ARG_FIRST, firstPlayer);
//        args.putInt(ARG_SECOND, secondPlayer);
//        args.putInt(ARG_THIRD, thirdPlayer);
//        args.putInt(ARG_FOURTH, fourthPlayer);
//        setArguments(args);

        mFirstPlayer = firstPlayer;
        mSecondPlayer = secondPlayer;
        mThirdPlayer = thirdPlayer;
        mFourthPlayer = fourthPlayer;
    }

    @Override
    public Animator onCreateAnimator(int transit, boolean enter, int nextAnim) {
        if (FragmentUtils.disableAnimations) {
            Animator a = AnimatorInflater.loadAnimator(fa, nextAnim);
            a.setDuration(0);
            return a;
        } else {
            return super.onCreateAnimator(transit, enter, nextAnim);
        }
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


        tvFirst.setText("1. " + fa.getGame().getGamePlayer(mFirstPlayer).getDisplayName() + "  +4 (" + fa.getGame().getGamePlayer(mFirstPlayer).getPoints() + ")");
        tvFirst.setBackgroundColor(fa.getGame().getGamePlayer(mFirstPlayer).getPlayerColor().getColor());
        tvSecond.setText("2. " + fa.getGame().getGamePlayer(mSecondPlayer).getDisplayName() + "  +3 (" + fa.getGame().getGamePlayer(mSecondPlayer).getPoints() + ")");
        tvSecond.setBackgroundColor(fa.getGame().getGamePlayer(mSecondPlayer).getPlayerColor().getColor());
        if (mThirdPlayer != -1) {
            tvThird.setText("3. " + fa.getGame().getGamePlayer(mThirdPlayer).getDisplayName() + "  +2 (" + fa.getGame().getGamePlayer(mThirdPlayer).getPoints() + ")");
            tvThird.setBackgroundColor(fa.getGame().getGamePlayer(mThirdPlayer).getPlayerColor().getColor());
        } else {
            tvThird.setVisibility(View.GONE);
        }
        if (mFourthPlayer != -1) {
            tvFourth.setText("4. " + fa.getGame().getGamePlayer(mFourthPlayer).getDisplayName() + "  +1 (" + fa.getGame().getGamePlayer(mFourthPlayer).getPoints() + ")");
            tvFourth.setBackgroundColor(fa.getGame().getGamePlayer(mFourthPlayer).getPlayerColor().getColor());
        } else {
            tvFourth.setVisibility(View.GONE);
        }

        Button btnWheelOfFortune = (Button) view.findViewById(R.id.btn_game_results_wheel_of_fortune);
        if (fa.deviceRole == DeviceRole.SERVER) {
            btnWheelOfFortune.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onButtonPressed(Constants.buttons.GAME_RESULTS_WHEEL_OF_FORTUNE);
                }
            });
        } else if (fa.deviceRole == DeviceRole.CLIENT) {
            btnWheelOfFortune.setVisibility(View.GONE);
        }

        TextView tvTime = (TextView) view.findViewById(R.id.tv_game_results_time);

        long time = fa.getGame().getSecondsRunning();
        int sec = (int) (time % 60);
        time /= 60;
        int min = (int) time;

        tvTime.setText(String.format(fa.getResources().getString(R.string.player_results_time), min, sec));

        return view;
    }

    private void onButtonPressed(int button) {
        if (fa != null) {
            fa.onFragmentInteraction(button);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            fa = (FullscreenActivity) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fa = null;
    }

    private static String getPlayerNameFromIndex(int playerIndex) {
        switch (playerIndex) {
            case 0:
                return "Rot";
            case 1:
                return "Lila";
            case 2:
                return "Gelb";
            case 3:
                return "Grün";
            default:
                return "UNKNOWN";
        }
    }

}
