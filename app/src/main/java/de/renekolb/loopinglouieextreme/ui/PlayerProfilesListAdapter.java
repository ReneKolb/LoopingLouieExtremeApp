package de.renekolb.loopinglouieextreme.ui;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import de.renekolb.loopinglouieextreme.FullscreenActivity;
import de.renekolb.loopinglouieextreme.GamePlayer;
import de.renekolb.loopinglouieextreme.PlayerProfiles.PlayerProfile;
import de.renekolb.loopinglouieextreme.R;

/**
 * Created by Admi on 04.03.2016.
 */
public class PlayerProfilesListAdapter extends BaseAdapter {

    private int selectedIndex;
    private ArrayList<PlayerProfile> list;
    private final LayoutInflater layoutInflater;

    private FullscreenActivity fa;

    public PlayerProfilesListAdapter(FullscreenActivity fa){
        this.fa = fa;
        this.layoutInflater = LayoutInflater.from(fa);
        this.list = new ArrayList<>(fa.getProfileManager().getAvailableProfiles());
        this.selectedIndex = -1;
    }

    public void updateName(int index, String newName) {
        list.get(index).editPlayerName(newName);
        this.notifyDataSetChanged();
    }

    public int add(PlayerProfile p){
        list.add(p);
        this.notifyDataSetChanged();
        return list.size()-1;
    }

    public void remove(int index){
        list.remove(index);
        if(selectedIndex >= index){
            selectedIndex--;
        }
        this.notifyDataSetChanged();
    }

    public void setSelectedIndex(int index){
        this.selectedIndex = index;
        this.notifyDataSetChanged();
    }

    public void refresh(){
        this.list = new ArrayList<>(fa.getProfileManager().getAvailableProfiles());
        this.notifyDataSetChanged();
    }

    public int getIndex(int profileID){
        for(int i=0;i<this.list.size();i++){
            if(list.get(i).getProfileID() == profileID){
                return i;
            }
        }
        return -1;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public PlayerProfile getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.listitem_player_profile, parent, false);
            holder = new ViewHolder();
            holder.icon = (ImageView) convertView.findViewById(R.id.iv_listitem_player_profile_icon);
            holder.profileName = (TextView) convertView.findViewById(R.id.tv_listitem_player_profile_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        PlayerProfile data = getItem(position);

        holder.profileName.setText(data.getPlayerName());
        if(selectedIndex == position){
            holder.icon.setImageResource(R.drawable.ic_grade);
            holder.icon.setVisibility(View.VISIBLE);
        }else{
            holder.icon.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    private class ViewHolder {
        ImageView icon;
        TextView profileName;
    }

}
