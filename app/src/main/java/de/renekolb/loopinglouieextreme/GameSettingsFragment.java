package de.renekolb.loopinglouieextreme;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TableRow;
import android.widget.TextView;

import java.awt.font.TextAttribute;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GameSettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GameSettingsFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment GameSettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GameSettingsFragment newInstance() {
        GameSettingsFragment fragment = new GameSettingsFragment();
        return fragment;
    }

    public GameSettingsFragment() {
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
        View view = inflater.inflate(R.layout.fragment_game_settings, container, false);

        Switch swRandomSpeed = (Switch) view.findViewById(R.id.sw_game_settings_random_speed);
        swRandomSpeed.setChecked(FullscreenActivity.gameSettings.getRandomSpeed());
        swRandomSpeed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                FullscreenActivity.gameSettings.setRandomSpeed(isChecked);
            }
        });


        final TextView tvStartSpeedCnt = (TextView) view.findViewById(R.id.tv_game_settings_start_speed_cnt);
        tvStartSpeedCnt.setText(String.valueOf(FullscreenActivity.gameSettings.getStartSpeed()));

        SeekBar sbStartSpeed = (SeekBar) view.findViewById(R.id.sb_game_settings_start_speed);
        sbStartSpeed.setMax(170 - 49);
        sbStartSpeed.setProgress(FullscreenActivity.gameSettings.getStartSpeed() - 49);
        sbStartSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvStartSpeedCnt.setText(String.valueOf(49 + progress));
                FullscreenActivity.gameSettings.setStartSpeed((byte)(49 + progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        final TextView tvSpeedMinDelayCnt = (TextView) view.findViewById(R.id.tv_game_settings_speed_min_delay_cnt);
        tvSpeedMinDelayCnt.setText(String.valueOf(FullscreenActivity.gameSettings.getSpeedMinDelay()));

        SeekBar sbSpeedMinDelay = (SeekBar) view.findViewById(R.id.sb_game_settings_speed_min_delay);
        sbSpeedMinDelay.setMax(15000);
        sbSpeedMinDelay.setProgress(FullscreenActivity.gameSettings.getSpeedMinDelay());
        sbSpeedMinDelay.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvSpeedMinDelayCnt.setText(String.valueOf(progress));
                FullscreenActivity.gameSettings.setSpeedMinDelay((short)progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });


        final TableRow trChefRoulette = (TableRow) view.findViewById(R.id.tr_game_settings_chef_roulette);
        final TableRow trChefDelay = (TableRow) view.findViewById(R.id.tr_game_settings_change_chef_delay);
        final TableRow trChefCooldown = (TableRow) view.findViewById(R.id.tr_game_settings_chef_cooldown);

        Switch swChefMode = (Switch) view.findViewById(R.id.sw_game_settings_chef_mode);
        swChefMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    trChefRoulette.setVisibility(View.VISIBLE);
                    trChefDelay.setVisibility(View.VISIBLE);
                    trChefCooldown.setVisibility(View.VISIBLE);
                }else{
                    trChefRoulette.setVisibility(View.GONE);
                    trChefDelay.setVisibility(View.GONE);
                    trChefCooldown.setVisibility(View.GONE);
                }
            }
        });

        Button btnStart = (Button) view.findViewById(R.id.btn_game_settings_start);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonPressed(Constants.BUTTON_GAME_SETTINGS_START);
            }
        });

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

}
