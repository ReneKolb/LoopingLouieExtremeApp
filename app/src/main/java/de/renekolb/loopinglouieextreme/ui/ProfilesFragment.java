package de.renekolb.loopinglouieextreme.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import de.renekolb.loopinglouieextreme.FullscreenActivity;
import de.renekolb.loopinglouieextreme.PlayerProfiles.PlayerProfile;
import de.renekolb.loopinglouieextreme.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * interface
 * to handle interaction events.
 * Use the {@link ProfilesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfilesFragment extends Fragment {


    private int mSelectedItem;

    private FullscreenActivity fa;
    private PlayerProfilesListAdapter listAdapter;

    public ProfilesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ProfilesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfilesFragment newInstance() {
        return new ProfilesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            this.listAdapter = new PlayerProfilesListAdapter(fa);
            this.listAdapter.setSelectedIndex(listAdapter.getIndex(fa.getProfileManager().getDefaultProfileID()));
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profiles, container, false);

        ImageButton btnAddProfile = (ImageButton) view.findViewById(R.id.btn_profiles_add_profile);
        btnAddProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View dialogView = inflater.inflate(R.layout.dialog_user_name, null);
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(fa);

                dialogBuilder.setView(dialogView);
                final EditText input = (EditText) dialogView.findViewById(R.id.et_dialog_user_name_input);

                dialogBuilder.setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                addPlayer(input.getText().toString());
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //do nothing
                            }
                        });

                dialogBuilder.create().show();

                /*PlayerProfile newProfile = fa.profileManager.createNewPlayerProfile("blaa blub"+(new Random()).nextInt(500));
                int listIndex = listAdapter.add(newProfile);
                listAdapter.setSelectedIndex(listIndex);
                fa.getProfileManager().setDefaultProfileID(newProfile.getProfileID());
                fa.setCurrentPlayerProfile(newProfile);*/
            }
        });

        ImageButton btnEditProfile = (ImageButton) view.findViewById(R.id.btn_profiles_edit_profile);
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectedItem != -1) {
                    final PlayerProfile profile = listAdapter.getItem(mSelectedItem);

                    View dialogView = inflater.inflate(R.layout.dialog_user_name, null);
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(fa);

                    dialogBuilder.setView(dialogView);
                    final EditText input = (EditText) dialogView.findViewById(R.id.et_dialog_user_name_input);

                    input.setText(profile.getPlayerName());

                    dialogBuilder.setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    editPlayerName(profile, input.getText().toString());
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //do nothing
                                }
                            });
                    dialogBuilder.create().show();


                    //Log.i("BLUB", "edit profile " + profile.getProfileID() + ": " + profile.getPlayerName());
                } else {
                    Log.w("BLUB", "No profile selected to edit");
                }
            }
        });

        this.mSelectedItem = this.listAdapter.getIndex(fa.getProfileManager().getDefaultProfileID());

        ImageButton btnDeleteProfile = (ImageButton) view.findViewById(R.id.btn_profiles_delete_profile);
        btnDeleteProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectedItem != -1) {
                    //TODO: add confirmation window
                    PlayerProfile profile = listAdapter.getItem(mSelectedItem);
                    fa.getProfileManager().deleteProfile(profile.getProfileID());
                    listAdapter.remove(mSelectedItem);
                    mSelectedItem = -1;
                } else {
                    Log.w("BLUB", "No profile selected to delete");
                }
            }
        });
        ImageButton btnSelectProfile = (ImageButton) view.findViewById(R.id.btn_profiles_select_profile);
        btnSelectProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectedItem != -1) {
                    PlayerProfile profile = listAdapter.getItem(mSelectedItem);
                    listAdapter.setSelectedIndex(mSelectedItem);
                    fa.getProfileManager().setDefaultProfileID(profile.getProfileID());
                    fa.setCurrentPlayerProfile(profile);
                } else {
                    Log.w("BLUB", "No profile selected to delete");
                }
            }
        });

        ListView lvProfilesList = (ListView) view.findViewById(R.id.lv_profiles_profiles_list);
        lvProfilesList.setAdapter(this.listAdapter);
        if (this.mSelectedItem != -1) {
            lvProfilesList.setSelection(this.mSelectedItem);
        }
        lvProfilesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mSelectedItem != position) {
                    mSelectedItem = position;

                }
            }
        });

        return view;
    }

    private void addPlayer(String name) {
        PlayerProfile newProfile = fa.profileManager.createNewPlayerProfile(name);
        int listIndex = listAdapter.add(newProfile);
        listAdapter.setSelectedIndex(listIndex);
        fa.getProfileManager().setDefaultProfileID(newProfile.getProfileID());
        fa.setCurrentPlayerProfile(newProfile);
    }

    private void editPlayerName(PlayerProfile profile, String newName) {
        profile.editPlayerName(newName);
        fa.getProfileManager().saveProfile(profile.getProfileID());
        int listIndex = listAdapter.getIndex(profile.getProfileID());
        listAdapter.updateName(listIndex, newName);
    }

    public void updateProfilesList() {
        listAdapter.refresh();
    }

    public void onButtonPressed(int button) {
        if (fa != null) {
            fa.onFragmentInteraction(button);
        }
    }

    @Override
    public void onAttach(Activity activity) {
//    public void onAttach(Context context) {
        super.onAttach(activity);
//        super.onAttach(context);
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
