package de.renekolb.loopinglouieextreme.ui;

import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import de.renekolb.loopinglouieextreme.FullscreenActivity;
import de.renekolb.loopinglouieextreme.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PlayerSettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlayerSettingsFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private FullscreenActivity fa;

   int mSelectedItem;

    private PlayerSettingsListAdapter playerSettingsListAdapter;

    public static PlayerSettingsFragment newInstance() {
        PlayerSettingsFragment fragment = new PlayerSettingsFragment();
        return fragment;
    }

    public PlayerSettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

        if(savedInstanceState == null){
            this.playerSettingsListAdapter = new PlayerSettingsListAdapter(fa);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_player_settings, container, false);

        mSelectedItem = -1;

        final ListView lvPlayers = (ListView) view.findViewById(R.id.lv_player_settings_players);
        lvPlayers.setAdapter(this.playerSettingsListAdapter);
        lvPlayers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
           //     for (int a = 0; a < parent.getChildCount(); a++) {
            //        parent.getChildAt(a).setBackgroundColor(Color.TRANSPARENT);
             //   }
                if (mSelectedItem == -1 || mSelectedItem != position) {
             //       view.setBackgroundColor(0xAAAAAA);
                    mSelectedItem = position;
                } else if (mSelectedItem == position) {
                    mSelectedItem = -1;
                }
            }
        });

        this.playerSettingsListAdapter.updateName(0, "Test Player");

        final Button btnTurbo = (Button)view.findViewById(R.id.btn_player_settings_turbo);
        final Button btnSlow = (Button)view.findViewById(R.id.btn_player_settings_slow);
        final Button btnReverse = (Button)view.findViewById(R.id.btn_player_settings_reverse);
        final Button btnBlackout = (Button)view.findViewById(R.id.btn_player_settings_blackout);

        btnTurbo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if(mSelectedItem!=-1){
                  btnTurbo.setEnabled(false);
                  btnSlow.setEnabled(true);
                  btnReverse.setEnabled(true);
                  btnBlackout.setEnabled(true);
                  playerSettingsListAdapter.updateBooster(mSelectedItem,"Turbo");
              }
            }
        });

        btnSlow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSelectedItem!=-1){
                    btnTurbo.setEnabled(true);
                    btnSlow.setEnabled(false);
                    btnReverse.setEnabled(true);
                    btnBlackout.setEnabled(true);
                    playerSettingsListAdapter.updateBooster(mSelectedItem,"Slow");
                }
            }
        });

        btnReverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSelectedItem!=-1){
                    btnTurbo.setEnabled(true);
                    btnSlow.setEnabled(true);
                    btnReverse.setEnabled(false);
                    btnBlackout.setEnabled(true);
                    playerSettingsListAdapter.updateBooster(mSelectedItem,"Reverse");
                }
            }
        });

        btnBlackout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSelectedItem!=-1){
                    btnTurbo.setEnabled(true);
                    btnSlow.setEnabled(true);
                    btnReverse.setEnabled(true);
                    btnBlackout.setEnabled(false);
                    playerSettingsListAdapter.updateBooster(mSelectedItem,"Blackout");
                }
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
            fa = (FullscreenActivity)activity;
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
