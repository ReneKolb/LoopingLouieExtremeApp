package de.renekolb.loopinglouieextreme.ui;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import de.renekolb.loopinglouieextreme.DeviceRole;
import de.renekolb.loopinglouieextreme.FragmentUtils;
import de.renekolb.loopinglouieextreme.FullscreenActivity;
import de.renekolb.loopinglouieextreme.GamePlayer;
import de.renekolb.loopinglouieextreme.PlayerColor;
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

        final TableRow trFirst = (TableRow) view.findViewById(R.id.tr_game_results_first);
        final TextView tvFirstName = (TextView) view.findViewById(R.id.tv_game_results_player_first);
        final TextView tvFirstPoints = (TextView) view.findViewById(R.id.tv_game_results_points_first);

        final TableRow trSecond = (TableRow) view.findViewById(R.id.tr_game_results_second);
        final TextView tvSecondName = (TextView) view.findViewById(R.id.tv_game_results_player_second);
        final TextView tvSecondPoints = (TextView) view.findViewById(R.id.tv_game_results_points_second);

        final TableRow trThird = (TableRow) view.findViewById(R.id.tr_game_results_third);
        final TextView tvThirdName = (TextView) view.findViewById(R.id.tv_game_results_player_third);
        final TextView tvThirdPoints = (TextView) view.findViewById(R.id.tv_game_results_points_third);

        final TableRow trFourth = (TableRow) view.findViewById(R.id.tr_game_results_fourth);
        final TextView tvFourthName = (TextView) view.findViewById(R.id.tv_game_results_player_fourth);
        final TextView tvFourthPoints = (TextView) view.findViewById(R.id.tv_game_results_points_fourth);

        final ImageView ivFirst = (ImageView) view.findViewById(R.id.iv_game_results_first);
        final ImageView ivSecond = (ImageView) view.findViewById(R.id.iv_game_results_second);
        final ImageView ivThird = (ImageView) view.findViewById(R.id.iv_game_results_third);


        GamePlayer first = fa.getGame().getGamePlayer(mFirstPlayer);
        tvFirstName.setText(first.getDisplayName());
        tvFirstName.setBackgroundColor(first.getPlayerColor().getColor());
        tvFirstPoints.setText(String.valueOf(4)); //winner receives 4 points
        if(first.getAvatar() != null){
            ivFirst.setImageBitmap(first.getAvatar());
        }else{
            ivFirst.setImageResource(getBallFromSlot(first.getPlayerColor()));
        }

        GamePlayer second = fa.getGame().getGamePlayer(mSecondPlayer);
        tvSecondName.setText(second.getDisplayName());
        tvSecondName.setBackgroundColor(second.getPlayerColor().getColor());
        tvSecondPoints.setText(String.valueOf(3));
        if(second.getAvatar() != null){
            ivSecond.setImageBitmap(second.getAvatar());
        }else{
            ivSecond.setImageResource(getBallFromSlot(second.getPlayerColor()));
        }

        if(mThirdPlayer != -1){
            trThird.setVisibility(View.VISIBLE);
            GamePlayer third = fa.getGame().getGamePlayer(mThirdPlayer);
            tvThirdName.setText(third.getDisplayName());
            tvThirdName.setBackgroundColor(third.getPlayerColor().getColor());
            tvThirdPoints.setText(String.valueOf(2));
            if(third.getAvatar() != null){
                ivThird.setImageBitmap(third.getAvatar());
            }else{
                ivThird.setImageResource(getBallFromSlot(third.getPlayerColor()));
            }
        }else{
            trThird.setVisibility(View.INVISIBLE);
            ivThird.setVisibility(View.INVISIBLE);
        }

        if(mFourthPlayer != -1){
            trFourth.setVisibility(View.VISIBLE);
            GamePlayer fourth = fa.getGame().getGamePlayer(mFourthPlayer);
            tvFourthName.setText(fourth.getDisplayName());
            tvFourthName.setBackgroundColor(fourth.getPlayerColor().getColor());
            tvFourthPoints.setText(String.valueOf(1));
        }else{
            trFourth.setVisibility(View.INVISIBLE);
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

    private int getBallFromSlot(PlayerColor playerColor){
        switch(playerColor){
            case RED:
                return R.drawable.ball_red;
            case PURPLE:
                return R.drawable.ball_purple;
            case YELLOW:
                return R.drawable.ball_yellow;
            case GREEN:
                return R.drawable.ball_green;
        }
        return -1;
    }

}
