package de.renekolb.loopinglouieextreme;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TableRow;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CustomGameSettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CustomGameSettingsFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CustomGameSettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CustomGameSettingsFragment newInstance() {
        CustomGameSettingsFragment fragment = new CustomGameSettingsFragment();
        return fragment;
    }

    public CustomGameSettingsFragment() {
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
        View view = inflater.inflate(R.layout.fragment_custom_game_settings, container, false);

        Switch swRandomSpeed = (Switch) view.findViewById(R.id.sw_custom_game_settings_random_speed);
        swRandomSpeed.setChecked(FullscreenActivity.customGameSettings.getRandomSpeed());
        swRandomSpeed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                FullscreenActivity.customGameSettings.setRandomSpeed(isChecked);
            }
        });


        final TextView tvStartSpeedCnt = (TextView) view.findViewById(R.id.tv_custom_game_settings_start_speed_cnt);
        tvStartSpeedCnt.setText(String.valueOf(FullscreenActivity.customGameSettings.getStartSpeed()));

        SeekBar sbStartSpeed = (SeekBar) view.findViewById(R.id.sb_custom_game_settings_start_speed);
        sbStartSpeed.setMax(170 - 49);
        sbStartSpeed.setProgress(FullscreenActivity.customGameSettings.getStartSpeed() - 49);
        sbStartSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvStartSpeedCnt.setText(String.valueOf(49 + progress));
                FullscreenActivity.customGameSettings.setStartSpeed((byte)(49 + progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        final TextView tvSpeedMinDelayCnt = (TextView) view.findViewById(R.id.tv_custom_game_settings_speed_min_delay_cnt);
        tvSpeedMinDelayCnt.setText(String.valueOf(FullscreenActivity.customGameSettings.getSpeedMinDelay()));

        SeekBar sbSpeedMinDelay = (SeekBar) view.findViewById(R.id.sb_custom_game_settings_speed_min_delay);
        sbSpeedMinDelay.setMax(15000);
        sbSpeedMinDelay.setProgress(FullscreenActivity.customGameSettings.getSpeedMinDelay());
        sbSpeedMinDelay.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvSpeedMinDelayCnt.setText(String.valueOf(progress));
                FullscreenActivity.customGameSettings.setSpeedMinDelay((short)progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });


        final TableRow trChefRoulette = (TableRow) view.findViewById(R.id.tr_custom_game_settings_chef_roulette);
        final TableRow trChefDelay = (TableRow) view.findViewById(R.id.tr_custom_game_settings_change_chef_delay);
        final TableRow trChefCooldown = (TableRow) view.findViewById(R.id.tr_custom_game_settings_chef_cooldown);

        Switch swChefMode = (Switch) view.findViewById(R.id.sw_custom_game_settings_chef_mode);
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

        final Button btnOK = (Button) view.findViewById(R.id.btn_custom_game_settings_ok);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
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
