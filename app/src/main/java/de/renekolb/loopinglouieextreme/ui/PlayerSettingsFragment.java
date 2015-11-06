package de.renekolb.loopinglouieextreme.ui;

import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import de.renekolb.loopinglouieextreme.FullscreenActivity;
import de.renekolb.loopinglouieextreme.ItemType;
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

    //private OnFragmentInteractionListener mListener;
    private FullscreenActivity fa;

    private int mSelectedItem;

    private boolean mPlayerNameEdible = true;

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
            this.playerSettingsListAdapter = new PlayerSettingsListAdapter(fa,fa.getGame());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_player_settings, container, false);

        final Button btnTurbo = (Button)view.findViewById(R.id.btn_player_settings_turbo);
        final Button btnSlow = (Button)view.findViewById(R.id.btn_player_settings_slow);
        final Button btnReverse = (Button)view.findViewById(R.id.btn_player_settings_reverse);
        final Button btnBlackout = (Button)view.findViewById(R.id.btn_player_settings_blackout);

        final TextView tvPlayerName = (TextView)view.findViewById(R.id.tv_player_settings_player_name_title);
        final EditText etPlayerName = (EditText)view.findViewById(R.id.et_player_settings_player_name_edit);

        tvPlayerName.setVisibility(View.INVISIBLE);
        etPlayerName.setVisibility(View.INVISIBLE);

        etPlayerName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(mSelectedItem!=-1){
                    fa.getGame().getGamePlayer(mSelectedItem).setDisplayName(s.toString());
                    playerSettingsListAdapter.update(mSelectedItem,fa.getGame().getGamePlayer(mSelectedItem));
                }
            }
        });

        btnTurbo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if(mSelectedItem!=-1){
                  btnTurbo.setEnabled(false);
                  btnSlow.setEnabled(true);
                  btnReverse.setEnabled(true);
                  btnBlackout.setEnabled(true);
                  fa.getGame().getGamePlayer(mSelectedItem).setDefaultItemType(ItemType.TURBO);
                  playerSettingsListAdapter.update(mSelectedItem, fa.getGame().getGamePlayer(mSelectedItem));
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
                    fa.getGame().getGamePlayer(mSelectedItem).setDefaultItemType(ItemType.SLOW);
                    playerSettingsListAdapter.update(mSelectedItem, fa.getGame().getGamePlayer(mSelectedItem));
                    //playerSettingsListAdapter.updateBooster(mSelectedItem,"Slow");
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
                    fa.getGame().getGamePlayer(mSelectedItem).setDefaultItemType(ItemType.REVERSE);
                    playerSettingsListAdapter.update(mSelectedItem, fa.getGame().getGamePlayer(mSelectedItem));
                    //playerSettingsListAdapter.updateBooster(mSelectedItem,"Reverse");
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
                    fa.getGame().getGamePlayer(mSelectedItem).setDefaultItemType(ItemType.BLACKOUT);
                    playerSettingsListAdapter.update(mSelectedItem, fa.getGame().getGamePlayer(mSelectedItem));
                    //playerSettingsListAdapter.updateBooster(mSelectedItem,"Blackout");
                }
            }
        });

        mSelectedItem = -1;

        final ListView lvPlayers = (ListView) view.findViewById(R.id.lv_player_settings_players);
        lvPlayers.setAdapter(this.playerSettingsListAdapter);
        lvPlayers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mSelectedItem == -1 || mSelectedItem != position) {
                    mSelectedItem = position;

                    PlayerSettingsListAdapter p = (PlayerSettingsListAdapter) parent.getAdapter();

                    //if(mPlayerNameEdible){
                        tvPlayerName.setVisibility(View.VISIBLE);
                        etPlayerName.setVisibility(View.VISIBLE);
                    //}else{
                        //tvPlayerName.setVisibility(View.INVISIBLE);
                        //etPlayerName.setVisibility(View.INVISIBLE);
                    //}

                    etPlayerName.setText(p.getItem(position).getPlayerName());
                    if(!mPlayerNameEdible){
                        etPlayerName.setEnabled(false);
                    }else{
                        etPlayerName.setSelection(etPlayerName.getText().length());
                    }

                    //UGLY!!!
                    btnTurbo.setEnabled(!p.getItem(position).getBooster().equals("Turbo"));
                    btnSlow.setEnabled(!p.getItem(position).getBooster().equals("Slow"));
                    btnReverse.setEnabled(!p.getItem(position).getBooster().equals("Reverse"));
                    btnBlackout.setEnabled(!p.getItem(position).getBooster().equals("Blackout"));

// cannot unselect
//                    } else if (mSelectedItem == position) {
//                    mSelectedItem = -1;
                }
            }
        });

        Button btnStart = (Button)view.findViewById(R.id.btn_player_settings_start);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonPressed(Constants.buttons.PLAYER_SETTINGS_START_GAME);
            }
        });

        return view;
    }

    public void onButtonPressed(int button) {
        if (fa != null) {
            fa.onFragmentInteraction(button);
        }
    }

    public void setPlayerNameEdible(boolean edible){
        this.mPlayerNameEdible = edible;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            //mListener = (OnFragmentInteractionListener) activity;
            fa = (FullscreenActivity)activity;
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
