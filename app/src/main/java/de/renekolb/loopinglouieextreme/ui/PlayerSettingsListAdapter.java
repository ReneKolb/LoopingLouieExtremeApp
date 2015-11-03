package de.renekolb.loopinglouieextreme.ui;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import de.renekolb.loopinglouieextreme.R;

public class PlayerSettingsListAdapter extends BaseAdapter {

    ArrayList<PlayerSettingsData> list;
    LayoutInflater layoutInflater;

    public PlayerSettingsListAdapter(Context context){
        layoutInflater = LayoutInflater.from(context);

        list = new ArrayList<>();
        list.add(new PlayerSettingsData("Player 1",Color.argb(255,255,0,0),"None"));
        list.add(new PlayerSettingsData("Player 2",Color.argb(255,136,0,136),"None"));
        list.add(new PlayerSettingsData("Player 3",Color.argb(255,221,221,0),"None"));
        list.add(new PlayerSettingsData("Player 4",Color.argb(255,0,255,0),"None"));
    }

    public void updateName(int index, String newName){
        list.get(index).setPlayerName(newName);
        this.notifyDataSetChanged();
    }

    public void updateBooster(int index, String newBooster){
        list.get(index).setBooster(newBooster);
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public PlayerSettingsData getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null) {
            convertView = layoutInflater.inflate(R.layout.listitem_player_settings, parent, false);
            holder = new ViewHolder();
            holder.colorDisplay = (ImageView)convertView.findViewById(R.id.iv_listitem_player_settings_color);
            holder.playerName = (TextView)convertView.findViewById(R.id.tv_listitem_player_settings_player_name);
            holder.booster = (TextView)convertView.findViewById(R.id.tv_listitem_player_settings_player_item);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        PlayerSettingsData data = getItem(position);

        holder.colorDisplay.setBackgroundColor(data.getColor());
        holder.playerName.setText(data.getPlayerName());
        holder.booster.setText(data.getBooster());

        return convertView;
    }


    private class ViewHolder{
        ImageView colorDisplay;
        TextView playerName;
        TextView booster;
    }
}
