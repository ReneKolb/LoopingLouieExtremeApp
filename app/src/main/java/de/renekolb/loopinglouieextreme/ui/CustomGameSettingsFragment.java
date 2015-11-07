package de.renekolb.loopinglouieextreme.ui;

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

import java.text.DecimalFormat;

import de.renekolb.loopinglouieextreme.FullscreenActivity;
import de.renekolb.loopinglouieextreme.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CustomGameSettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CustomGameSettingsFragment extends Fragment {

    //private OnFragmentInteractionListener mListener;
    private FullscreenActivity fa;

    private DecimalFormat format;

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
        format = new DecimalFormat("#0.0 sec");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_custom_game_settings, container, false);

        final TextView tvStartSpeedCnt = (TextView) view.findViewById(R.id.tv_custom_game_settings_start_speed_cnt);
        tvStartSpeedCnt.setText(String.valueOf(fa.getGame().getGameSettings().getStartSpeed() - 50));

        SeekBar sbStartSpeed = (SeekBar) view.findViewById(R.id.sb_custom_game_settings_start_speed);
        sbStartSpeed.setMax(70);
        sbStartSpeed.setProgress(fa.getGame().getGameSettings().getStartSpeed() - 50);
        sbStartSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvStartSpeedCnt.setText(String.valueOf(progress));
                fa.getGame().getGameSettings().setStartSpeed((byte) (50 + progress));//50 .. 120
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        final TableRow trSpeedMinDelay = (TableRow) view.findViewById(R.id.tr_custom_game_settings_speed_min_delay);
        final TableRow trSpeedMaxDelay = (TableRow) view.findViewById(R.id.tr_custom_game_settings_speed_max_delay);
        final TableRow trSpeedMinStepSize = (TableRow) view.findViewById(R.id.tr_custom_game_settings_speed_min_step_size);
        final TableRow trSpeedMaxStepSize = (TableRow) view.findViewById(R.id.tr_custom_game_settings_speed_max_step_size);
        final TableRow trEnableReverse = (TableRow) view.findViewById(R.id.tr_custom_game_settings_enable_reverse);

        Switch swRandomSpeed = (Switch) view.findViewById(R.id.sw_custom_game_settings_random_speed);
        swRandomSpeed.setChecked(fa.getGame().getGameSettings().getRandomSpeed());
        if(swRandomSpeed.isChecked()){
            trSpeedMinDelay.setVisibility(View.VISIBLE);
            trSpeedMaxDelay.setVisibility(View.VISIBLE);
            trSpeedMinStepSize.setVisibility(View.VISIBLE);
            trSpeedMaxStepSize.setVisibility(View.VISIBLE);
            trEnableReverse.setVisibility(View.VISIBLE);
        }else{
            trSpeedMinDelay.setVisibility(View.GONE);
            trSpeedMaxDelay.setVisibility(View.GONE);
            trSpeedMinStepSize.setVisibility(View.GONE);
            trSpeedMaxStepSize.setVisibility(View.GONE);
            trEnableReverse.setVisibility(View.GONE);
        }
        swRandomSpeed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                fa.getGame().getGameSettings().setRandomSpeed(isChecked);
                if(isChecked){
                    trSpeedMinDelay.setVisibility(View.VISIBLE);
                    trSpeedMaxDelay.setVisibility(View.VISIBLE);
                    trSpeedMinStepSize.setVisibility(View.VISIBLE);
                    trSpeedMaxStepSize.setVisibility(View.VISIBLE);
                    trEnableReverse.setVisibility(View.VISIBLE);
                }else{
                    trSpeedMinDelay.setVisibility(View.GONE);
                    trSpeedMaxDelay.setVisibility(View.GONE);
                    trSpeedMinStepSize.setVisibility(View.GONE);
                    trSpeedMaxStepSize.setVisibility(View.GONE);
                    trEnableReverse.setVisibility(View.GONE);
                }
            }
        });

        final TextView tvSpeedMinDelayCnt = (TextView) view.findViewById(R.id.tv_custom_game_settings_speed_min_delay_cnt);
        tvSpeedMinDelayCnt.setText(format.format(fa.getGame().getGameSettings().getSpeedMinDelay() / 1000f));

        SeekBar sbSpeedMinDelay = (SeekBar) view.findViewById(R.id.sb_custom_game_settings_speed_min_delay);
        sbSpeedMinDelay.setMax(150);
        sbSpeedMinDelay.setProgress(fa.getGame().getGameSettings().getSpeedMinDelay() / 100);
        sbSpeedMinDelay.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvSpeedMinDelayCnt.setText(format.format(progress / 10f));
                fa.getGame().getGameSettings().setSpeedMinDelay((short) (progress * 100));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        final TextView tvSpeedMaxDelay = (TextView) view.findViewById(R.id.tv_custom_game_settings_speed_max_delay_cnt);
        tvSpeedMaxDelay.setText(format.format(fa.getGame().getGameSettings().getSpeedMaxDelay() / 1000f));

        SeekBar sbSpeedMaxDelay = (SeekBar) view.findViewById(R.id.sb_custom_game_settings_speed_max_delay);
        sbSpeedMaxDelay.setMax(150);
        sbSpeedMaxDelay.setProgress(fa.getGame().getGameSettings().getSpeedMaxDelay() / 100);
        sbSpeedMaxDelay.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvSpeedMaxDelay.setText(format.format(progress / 10f));
                fa.getGame().getGameSettings().setSpeedMaxDelay((short) (progress * 100));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        final TextView tvSpeedMinStepSizeCnt = (TextView) view.findViewById(R.id.tv_custom_game_settings_speed_min_step_size_cnt);
        tvSpeedMinStepSizeCnt.setText(String.valueOf(fa.getGame().getGameSettings().getSpeedMinStepSize()));

        SeekBar sbSpeedMinStepSize = (SeekBar) view.findViewById(R.id.sb_custom_game_settings_speed_min_step_size);
        sbSpeedMinStepSize.setMax(30);
        sbSpeedMinStepSize.setProgress(fa.getGame().getGameSettings().getSpeedMinStepSize());
        sbSpeedMinStepSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvSpeedMinStepSizeCnt.setText(String.valueOf(progress));
                fa.getGame().getGameSettings().setSpeedMinStepSize((byte) progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        final TextView tvSpeedMaxStepSizeCnt = (TextView) view.findViewById(R.id.tv_custom_game_settings_speed_max_step_size_cnt);
        tvSpeedMaxStepSizeCnt.setText(String.valueOf(fa.getGame().getGameSettings().getSpeedMaxStepSize()));

        SeekBar sbSpeedMaxStepSize = (SeekBar) view.findViewById(R.id.sb_custom_game_settings_speed_max_step_size);
        sbSpeedMaxStepSize.setMax(30);
        sbSpeedMaxStepSize.setProgress(fa.getGame().getGameSettings().getSpeedMaxStepSize());
        sbSpeedMaxStepSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvSpeedMaxStepSizeCnt.setText(String.valueOf(progress));
                fa.getGame().getGameSettings().setSpeedMaxStepSize((byte) progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        Switch swEnableReverse = (Switch) view.findViewById(R.id.sw_custom_game_settings_enable_reverse);
        swEnableReverse.setChecked(fa.getGame().getGameSettings().getEnableReverse());
        swEnableReverse.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                fa.getGame().getGameSettings().setEnableReverse(isChecked);
            }
        });


        final TableRow trChefRoulette = (TableRow) view.findViewById(R.id.tr_custom_game_settings_chef_roulette);
        final TableRow trChefDelay = (TableRow) view.findViewById(R.id.tr_custom_game_settings_change_chef_delay);
        final TableRow trChefCooldown = (TableRow) view.findViewById(R.id.tr_custom_game_settings_chef_cooldown);

        Switch swChefMode = (Switch) view.findViewById(R.id.sw_custom_game_settings_chef_mode);
        swChefMode.setChecked(fa.getGame().getGameSettings().getChefMode());
        if(swChefMode.isChecked()){
            trChefRoulette.setVisibility(View.VISIBLE);
            trChefDelay.setVisibility(View.VISIBLE);
            trChefCooldown.setVisibility(View.VISIBLE);
        }else{
            trChefRoulette.setVisibility(View.GONE);
            trChefDelay.setVisibility(View.GONE);
            trChefCooldown.setVisibility(View.GONE);
        }
        swChefMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                fa.getGame().getGameSettings().setChefMode(isChecked);
                if (isChecked) {
                    trChefRoulette.setVisibility(View.VISIBLE);
                    trChefDelay.setVisibility(View.VISIBLE);
                    trChefCooldown.setVisibility(View.VISIBLE);
                } else {
                    trChefRoulette.setVisibility(View.GONE);
                    trChefDelay.setVisibility(View.GONE);
                    trChefCooldown.setVisibility(View.GONE);
                }
            }
        });

        Switch swChefRoulette = (Switch) view.findViewById(R.id.sw_custom_game_settings_chef_roulette);
        swChefRoulette.setChecked(fa.getGame().getGameSettings().getChefRoulette());
        swChefRoulette.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                fa.getGame().getGameSettings().setChefRoulette(isChecked);
            }
        });

        final TextView tvChefChangeDelayCnt = (TextView) view.findViewById(R.id.tv_custom_game_settings_change_chef_delay_cnt);
        tvChefChangeDelayCnt.setText(format.format(fa.getGame().getGameSettings().getChefChangeDelay() / 1000f));

        SeekBar sbChefChangeDelay = (SeekBar) view.findViewById(R.id.sb_custom_game_settings_change_chef_delay);
        sbChefChangeDelay.setMax(150);
        sbChefChangeDelay.setProgress(fa.getGame().getGameSettings().getChefChangeDelay() / 100);
        sbChefChangeDelay.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvChefChangeDelayCnt.setText(format.format(progress / 10f));
                fa.getGame().getGameSettings().setChefChangeDelay((short) (progress * 100));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        Switch swChefCooldown = (Switch) view.findViewById(R.id.sw_custom_game_settings_chef_cooldown);
        swChefCooldown.setChecked(fa.getGame().getGameSettings().getChefHasShorterCooldown());
        swChefCooldown.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                fa.getGame().getGameSettings().setChefHasShorterCooldown(isChecked);
            }
        });

        final Switch swEnableItems = (Switch) view.findViewById(R.id.sw_custom_game_settings_enable_items);
        swEnableItems.setChecked(fa.getGame().getGameSettings().getEnableItems());
        swEnableItems.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                fa.getGame().getGameSettings().setEnableItems(isChecked);
            }
        });

        final Switch swEnableEvents = (Switch) view.findViewById(R.id.sw_custom_game_settings_enable_events);
        swEnableEvents.setChecked(fa.getGame().getGameSettings().getEnableEvents());
        swEnableEvents.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                fa.getGame().getGameSettings().setEnableEvents(isChecked);
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

}
