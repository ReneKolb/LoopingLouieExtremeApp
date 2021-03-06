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
import android.widget.TextView;

import de.renekolb.loopinglouieextreme.FragmentUtils;
import de.renekolb.loopinglouieextreme.FullscreenActivity;
import de.renekolb.loopinglouieextreme.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GameFragment extends Fragment {

    private FullscreenActivity fa;

    private TextView tvSecondsDisplay;

    public static GameFragment newInstance() {
        return new GameFragment();
    }

    public GameFragment() {
        // Required empty public constructor
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_game, container, false);

        TextView tvRound = (TextView) view.findViewById(R.id.tv_game_round);
        tvRound.setText(String.format(fa.getResources().getString(R.string.game_round), fa.getGame().getCurrentRound(), fa.getGame().getMaxRounds()));

        this.tvSecondsDisplay = (TextView) view.findViewById(R.id.tv_game_seconds);

        return view;
    }

    public void onButtonPressed(int button) {
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

    public void updateSeconds(long seconds) {

        int sec = (int) (seconds % 60);
        seconds /= 60;
        int min = (int) seconds;

        if (tvSecondsDisplay == null) {
            Log.e("GAME FRAGMENT", "no seconds Display found");
            return;
        }
        this.tvSecondsDisplay.setText(String.format("%02d:%02d", min, sec));
    }

}
