package de.renekolb.loopinglouieextreme.ui;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.renekolb.loopinglouieextreme.FullscreenActivity;
import de.renekolb.loopinglouieextreme.PlayerProfiles.StatisticType;
import de.renekolb.loopinglouieextreme.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StatisticsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StatisticsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatisticsFragment extends Fragment {

    private FullscreenActivity fa;


    private TextView tvRoundsPlayed;
    private TextView tvGamesPlayed;
    private TextView tvRoundsWon;
    private TextView tvGamesWon;

    public StatisticsFragment() {
        // Required empty public constructor
    }

    public static StatisticsFragment newInstance() {
        StatisticsFragment fragment = new StatisticsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);
        tvRoundsPlayed = (TextView) view.findViewById(R.id.tv_statistics_rounds_played_display);
        tvGamesPlayed = (TextView) view.findViewById(R.id.tv_statistics_games_played_display);
        tvRoundsWon = (TextView) view.findViewById(R.id.tv_statistics_rounds_won_display);
        tvGamesWon = (TextView) view.findViewById(R.id.tv_statistics_games_won_display);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateStatistics();
    }

    private void updateStatistics() {
        tvRoundsPlayed.setText(String.valueOf(fa.getCurrentPlayer().getPlayerStatistics().getAmount(StatisticType.TOTAL_ROUNDS_PLAYED)));
        tvGamesPlayed.setText(String.valueOf(fa.getCurrentPlayer().getPlayerStatistics().getAmount(StatisticType.TOTAL_GAMES_PLAYED)));
        tvRoundsWon.setText(String.valueOf(fa.getCurrentPlayer().getPlayerStatistics().getAmount(StatisticType.TOTAL_ROUNDS_WON)));
        tvGamesWon.setText(String.valueOf(fa.getCurrentPlayer().getPlayerStatistics().getAmount(StatisticType.TOTAL_GAMES_WON)));
    }


    public void onButtonPressed(int button) {
        if (fa != null) {
            fa.onFragmentInteraction(button);
        }
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        if (context instanceof FullscreenActivity) {
            fa = (FullscreenActivity) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fa = null;
    }
}
