package de.renekolb.loopinglouieextreme.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import de.renekolb.loopinglouieextreme.Game;
import de.renekolb.loopinglouieextreme.GamePlayer;
import de.renekolb.loopinglouieextreme.R;

public class PlayerSettingsListAdapter extends BaseAdapter {

    public static final String NO_CHIP_AMOUNT = "\uFF0D";//"\u2796";

    private final ArrayList<PlayerSettingsListItem> list;
    private final LayoutInflater layoutInflater;

    private final int chipColorOK;
    private final int chipColorNOK;

    public PlayerSettingsListAdapter(Context context, Game game) {
        layoutInflater = LayoutInflater.from(context);

        list = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            list.add(new PlayerSettingsListItem(game.getGamePlayer(i)));
        }

        chipColorOK = context.getResources().getColor(R.color.chips_count_ok);
        chipColorNOK = context.getResources().getColor(R.color.chips_count_nok);
    }

    public void update(int index, GamePlayer player) {
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
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.listitem_player_settings, parent, false);
            holder = new ViewHolder();
            holder.colorDisplay = (ImageView) convertView.findViewById(R.id.iv_listitem_player_settings_color);
            holder.playerName = (TextView) convertView.findViewById(R.id.tv_listitem_player_settings_player_name);
            holder.booster = (TextView) convertView.findViewById(R.id.tv_listitem_player_settings_player_item);
            holder.chips = (TextView) convertView.findViewById(R.id.tv_listitem_player_settings_player_chips);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        PlayerSettingsListItem data = getItem(position);

        holder.colorDisplay.setBackgroundColor(data.getColor());

        switch (data.getConnectionState()) {
            case OPEN:
                holder.playerName.setTextColor(Color.argb(255, 0, 0, 255));
                holder.booster.setTextColor(Color.argb(255, 0, 0, 255));
                holder.playerName.setText(R.string.listitem_player_settings_open_slot);
                holder.booster.setText("");
                holder.booster.setVisibility(View.GONE);
                if (data.getChipAmount() < 0) {
                    holder.chips.setTextColor(Color.argb(255, 80, 80, 80));
                    holder.chips.setText(NO_CHIP_AMOUNT);
                    holder.chips.setTypeface(Typeface.DEFAULT);
                } else {
//                    holder.chips.setTextColor(Color.argb(255, 0, 180, 0));
                    if(data.getChipAmount() == 3) {
                        holder.chips.setTextColor(chipColorOK);
                        holder.chips.setTypeface(Typeface.DEFAULT_BOLD);
                    }else{
                        holder.chips.setTextColor(chipColorNOK);
                        holder.chips.setTypeface(Typeface.DEFAULT);
                    }
                    holder.chips.setText(String.valueOf(data.getChipAmount()));
                }
                holder.chips.setVisibility(View.VISIBLE);
                break;
            case CONNECTED:
                holder.playerName.setTextColor(Color.argb(255, 0, 255, 255));
                holder.booster.setTextColor(Color.argb(255, 0, 255, 255));
                holder.playerName.setText(data.getPlayerName());
                holder.booster.setText(data.getBooster() == null ? "none" : data.getBooster().getDisplayName());
                holder.booster.setVisibility(View.VISIBLE);
                if (data.getChipAmount() < 0) {
                    holder.chips.setTextColor(Color.argb(255, 80, 80, 80));
                    holder.chips.setText(NO_CHIP_AMOUNT);
                    holder.chips.setTypeface(Typeface.DEFAULT);
                } else {
                    //holder.chips.setTextColor(Color.argb(255, 0, 180, 0));
                    if(data.getChipAmount() == 3) {
                        holder.chips.setTextColor(chipColorOK);
                        holder.chips.setTypeface(Typeface.DEFAULT_BOLD);
                    }else{
                        holder.chips.setTextColor(chipColorNOK);
                        holder.chips.setTypeface(Typeface.DEFAULT);
                    }
                    holder.chips.setText(String.valueOf(data.getChipAmount()));
                }
                holder.chips.setVisibility(View.VISIBLE);
                break;
            case LOCAL:
                if (data.isGuest()) {
                    holder.playerName.setTextColor(Color.argb(255, 80, 120, 0));
                    holder.booster.setTextColor(Color.argb(255, 0, 0, 0));
                } else {
                    holder.playerName.setTextColor(Color.argb(255, 0, 0, 0));
                    holder.booster.setTextColor(Color.argb(255, 0, 0, 0));
                }
                holder.playerName.setText(data.getPlayerName());
                holder.booster.setText(data.getBooster() == null ? "none" : data.getBooster().getDisplayName());
                holder.booster.setVisibility(View.VISIBLE);
                if (data.getChipAmount() < 0) {
                    holder.chips.setTextColor(Color.argb(255, 80, 80, 80));
                    holder.chips.setText(NO_CHIP_AMOUNT);
                    holder.chips.setTypeface(Typeface.DEFAULT);
                } else {
                    //holder.chips.setTextColor(Color.argb(255, 0, 180, 0));
                    if(data.getChipAmount() == 3) {
                        holder.chips.setTextColor(chipColorOK);
                        holder.chips.setTypeface(Typeface.DEFAULT_BOLD);
                    }else{
                        holder.chips.setTextColor(chipColorNOK);
                        holder.chips.setTypeface(Typeface.DEFAULT);
                    }
                    holder.chips.setText(String.valueOf(data.getChipAmount()));
                }
                holder.chips.setVisibility(View.VISIBLE);
                break;
            case CLOSED:
                holder.playerName.setTextColor(Color.argb(255, 120, 120, 120));
                holder.playerName.setText(R.string.listitem_player_settings_disabled_player);
                holder.booster.setText("");
                holder.booster.setVisibility(View.GONE);
                holder.chips.setText("");
                holder.chips.setVisibility(View.GONE);
                break;
        }

        return convertView;
    }


    private class ViewHolder {
        ImageView colorDisplay;
        TextView playerName;
        TextView booster;
        TextView chips;
    }
}
