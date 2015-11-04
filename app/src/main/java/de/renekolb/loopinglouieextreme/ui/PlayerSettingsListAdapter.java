package de.renekolb.loopinglouieextreme.ui;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import de.renekolb.loopinglouieextreme.Game;
import de.renekolb.loopinglouieextreme.GamePlayer;
import de.renekolb.loopinglouieextreme.PlayerColor;
import de.renekolb.loopinglouieextreme.R;

public class PlayerSettingsListAdapter extends BaseAdapter {

    ArrayList<PlayerSettingsListItem> list;
    LayoutInflater layoutInflater;

    public PlayerSettingsListAdapter(Context context, Game game){
        layoutInflater = LayoutInflater.from(context);

        list = new ArrayList<>();
        for(int i=0;i<4;i++){
            list.add(new PlayerSettingsListItem(game.getGamePlayer(i)));
        }
    }

    public void update(int index, GamePlayer player){
        list.get(index).update(player);
        this.notifyDataSetChanged();
    }

/*    public void updateName(int index, String newName){
        list.get(index).setPlayerName(newName);
        this.notifyDataSetChanged();
    }

    public void updateBooster(int index, String newBooster){
        list.get(index).setBooster(newBooster);
        this.notifyDataSetChanged();
    }*/

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public PlayerSettingsListItem getItem(int position) {
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

        PlayerSettingsListItem data = getItem(position);

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
