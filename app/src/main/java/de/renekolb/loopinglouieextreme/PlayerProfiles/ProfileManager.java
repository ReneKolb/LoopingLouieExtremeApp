package de.renekolb.loopinglouieextreme.PlayerProfiles;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.HashMap;

import de.renekolb.loopinglouieextreme.FullscreenActivity;

public class ProfileManager {

    private static final String PROFILES_LIST_FILE_NAME = "profiles.sav";
    private static final String PROFILE_FILE_NAME = "profile_";

    private Context context;
    private Handler mHandler;

    private int defaultID;
    private HashMap<Integer, PlayerProfile> storedProfiles;

    public ProfileManager(FullscreenActivity context, Handler mHandler) {
        this.context = context;
        this.mHandler = mHandler;
        this.defaultID = -1;
        this.storedProfiles = new HashMap<>();

        //TODO: Load den Spaß in loading Screen!!!

        //load available profiles.
        FileInputStream in = null;
        try {
            in = context.openFileInput(PROFILES_LIST_FILE_NAME);
        } catch (FileNotFoundException e) {
            //File does not exist -> No profiles has been created!
            Log.i("ProfileManager", "No profiles list found (first run?)");
        }

        if (in != null) {
            ObjectInputStream oin = null;
            try {
                oin = new ObjectInputStream(in);
                defaultID = oin.readInt();
                while (oin.available() > 0) {
                    int id = oin.readInt();
                    PlayerProfile p = loadProfile(id);
                    this.storedProfiles.put(id, p);
                }
            } catch (IOException e) {
                Log.e("ProfileManager", "Error while loading available profiles list", e);
            } finally {
                if (oin != null) {
                    try {
                        oin.close();
                    } catch (Exception e) {
                        //error closing file
                    }
                }
            }

            try {
                in.close();
            } catch (Exception e) {
                //error closing file
            }
        }
    }

    private void saveProfilesList() {
        FileOutputStream out = null;
        try {
            out = context.openFileOutput(PROFILES_LIST_FILE_NAME, Context.MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            Log.e("ProfileManager", "File not found to save", e);
        }

        if (out != null) {
            ObjectOutputStream oout = null;
            try {
                oout = new ObjectOutputStream(out);
                oout.writeInt(this.defaultID);
                for (int profileID : this.storedProfiles.keySet()) {
                    oout.writeInt(profileID);
                }
                Log.v("ProfileManager", "saved profiles list successfully");
            } catch (IOException e) {
                Log.e("ProfileManager", "Error while saving profiles list", e);
            } finally {
                if (oout != null) {
                    try {
                        oout.close();
                    } catch (IOException e) {
                        //error closing file
                    }
                }
            }


            try {
                out.close();
            } catch (IOException e) {
                //error while closing file
            }
        }
    }

    public PlayerProfile getProfile(int profileID) {
        if (!storedProfiles.containsKey(profileID)) {
            Log.e("ProfileManager", "Cannot return Profile. Unkown PlayerProfile ID");
            return null;
        }

        if (storedProfiles.get(profileID) == null) {
            return loadProfile(profileID);
        } else {
            return storedProfiles.get(profileID);
        }
    }

    private PlayerProfile loadProfile(int profileID) {
        FileInputStream in = null;

        try {
            in = context.openFileInput(PROFILE_FILE_NAME + profileID + ".sav");
        } catch (FileNotFoundException e) {
            Log.e("ProfileManager", "Cannot find player profile file");
        }

        if (in != null) {
            ObjectInputStream oin = null;
            String playerName = null;
            PlayerStatistics playerStats = null;
            try {
                oin = new ObjectInputStream(in);
                playerName = oin.readUTF();
                playerStats = (PlayerStatistics) oin.readObject();
            } catch (IOException e) {
                Log.e("ProfileManager", "Error while loading profile", e);
            } catch (ClassNotFoundException e) {
                Log.e("ProfileManager", "Error while loading profile. Class not found", e);
            } finally {
                if (oin != null) {
                    try {
                        oin.close();
                    } catch (IOException e) {
                        //error closing file
                    }
                }
            }

            try {
                in.close();
            } catch (IOException e) {
                //error closing file
            }

            if (playerName != null && playerStats != null) {
                PlayerAchievements ach = new PlayerAchievements(this.mHandler);
                playerStats.setPlayerAchievements(ach);
                playerStats.updateAllAchievements(false);
                PlayerProfile playerProfile = new PlayerProfile(profileID, playerName, playerStats);

                this.storedProfiles.put(profileID, playerProfile); // fill Map with the data
                return playerProfile;
            } else {
                Log.e("ProfileManager", "PlayerName or PlayerStats == null");
            }
        }

        return null;
    }

//    public Set<Integer> getAvailableProfiles(){
//        return storedProfiles.keySet();
    //}

    public Collection<PlayerProfile> getAvailableProfiles() {
        return this.storedProfiles.values();
    }

    public void saveProfile(int profileID) {
        if (!storedProfiles.containsKey(profileID)) {
            Log.e("ProfileManager", "Cannot save Profile. Unkown PlayerProfile ID");
        }
        PlayerProfile p = storedProfiles.get(profileID);

        FileOutputStream out = null;
        try {
            out = context.openFileOutput(PROFILE_FILE_NAME + profileID + ".sav", Context.MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            Log.e("ProfileManager", "File not found to save", e);
        }

        if (out != null) {
            ObjectOutputStream oout = null;
            try {
                oout = new ObjectOutputStream(out);

                oout.writeUTF(p.getPlayerName());
                oout.writeObject(p.getPlayerStatistics());
                Log.v("ProfileManager", "saved profile successfully");
            } catch (IOException e) {
                Log.e("ProfileManager", "Error while saving profile", e);
            } finally {
                if (oout != null) {
                    try {
                        oout.close();
                    } catch (IOException e) {
                        //error closing file
                    }
                }
            }

            try {
                out.close();
            } catch (IOException e) {
                //error closing file
            }
        }

        //TODO: autosave progress
    }

    public PlayerProfile createNewPlayerProfile(String playerName) {
        Log.i("ProfileManager", "Create new Profile: " + playerName);
        int profileID = -1;
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            if (!storedProfiles.containsKey(i)) {
                profileID = i;
                break;
            }
        }

        if (profileID == -1) {
            Log.e("ProfileManager", "Error generating ProfileID");
            return null;
        }

        PlayerAchievements ach = new PlayerAchievements(this.mHandler);
        PlayerStatistics stats = new PlayerStatistics(ach);
        PlayerProfile p = new PlayerProfile(profileID, playerName, stats);

        storedProfiles.put(profileID, p);
        saveProfilesList();
        saveProfile(profileID);
        return p;
    }

    public void deleteProfile(int profileID) {
        File f = new File(context.getFilesDir(), PROFILE_FILE_NAME + profileID + ".sav");
        if (f.exists()) {
            if (f.delete()) {
                Log.v("ProfileManager", "profile file deleted successfully");
            }
        }

        storedProfiles.remove(profileID);
        saveProfilesList();
    }


    public void setDefaultProfileID(int profileID) {
        if (profileID != this.defaultID) {
            this.defaultID = profileID;
            saveProfilesList();
        }
    }

    public int getDefaultProfileID() {
        if (this.storedProfiles.containsKey(this.defaultID)) {
            return this.defaultID;
        } else {
            return this.defaultID = -1;
        }
    }

    @Deprecated
    public void wipeFiles() {
        File[] files = context.getFilesDir().listFiles();

        for (File d : files) {
            if (d.getName().startsWith(PROFILE_FILE_NAME) || d.getName().startsWith(PROFILES_LIST_FILE_NAME)) {
                if (d.delete()) {
                    Log.i("ProfileManager", "Wipe: deleted: " + d.getName());
                }
            }
        }

        Log.i("ProfileManager", "Wiping complete");
    }

}
