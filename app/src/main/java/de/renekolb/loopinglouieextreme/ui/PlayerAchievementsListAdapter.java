package de.renekolb.loopinglouieextreme.ui;

import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.LinkedHashMap;

import de.renekolb.loopinglouieextreme.FullscreenActivity;
import de.renekolb.loopinglouieextreme.PlayerProfiles.Achievement;
import de.renekolb.loopinglouieextreme.PlayerProfiles.Achievements;
import de.renekolb.loopinglouieextreme.PlayerProfiles.PlayerProfile;
import de.renekolb.loopinglouieextreme.R;

public class PlayerAchievementsListAdapter extends BaseAdapter {

    private LinkedHashMap<Integer, Boolean> list; //AchievementID, unlocked
    private final LayoutInflater layoutInflater;

    private FullscreenActivity fa;
    private PlayerProfile profile;

    public PlayerAchievementsListAdapter(FullscreenActivity fa, PlayerProfile playerProfile) {
        this.fa = fa;
        this.profile = playerProfile;
        this.layoutInflater = LayoutInflater.from(fa);
        this.list = new LinkedHashMap<>();
        for (int id : Achievements.getAchievementIDs()) {
            this.list.put(id, playerProfile.getPlayerAchievements().hasUnlocked(id));
        }
//        sortList();
    }

   /* private void sortList(){
        List<Map.Entry<Integer, Boolean>> tmplist =
                new LinkedList<>( this.list.entrySet() );
        Collections.sort(tmplist, new Comparator<Map.Entry<Integer, Boolean>>() {
            public int compare(Map.Entry<Integer, Boolean> o1, Map.Entry<Integer, Boolean> o2) {
                if (o1.getValue() == o2.getValue()) {
                    return 0;
                } else {
                    if (o2.getValue())
                        return 1;
                    else
                        return -1;
                }
            }
        });

        this.list.clear();

        for (Map.Entry<Integer, Boolean> entry : tmplist)
        {
            this.list.put(entry.getKey(), entry.getValue());
        }

    }*/

    public void changePlayerProfile(PlayerProfile newProfile) {
        this.profile = newProfile;
        for (int id : Achievements.getAchievementIDs()) {
            this.list.put(id, newProfile.getPlayerAchievements().hasUnlocked(id));
        }
        //   sortList();
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Boolean getItem(int position) {
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
            convertView = layoutInflater.inflate(R.layout.listitem_player_achievement, parent, false);
            holder = new ViewHolder();
            holder.icon = (ImageView) convertView.findViewById(R.id.iv_listitem_player_achievement_icon);
            holder.title = (TextView) convertView.findViewById(R.id.tv_listitem_player_achievement_title);
            holder.progress = (ProgressBar) convertView.findViewById(R.id.pb_listitem_player_achievement_progress);
            holder.description = (TextView) convertView.findViewById(R.id.tv_listitem_player_achievement_description);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        boolean unlocked = getItem(position);
        Achievement ach = Achievements.getAchievement(position);

        holder.title.setText(ach.getTitle());
        holder.icon.setImageResource(ach.getDrawableID());

        if (unlocked) {
            holder.title.setTextColor(Color.parseColor("#000000"));
            holder.progress.setVisibility(View.GONE);
            holder.description.setText(ach.getUnlockedDescription());
            holder.description.setTextColor(Color.parseColor("#000000"));
            holder.icon.clearColorFilter();
        } else {
            //grayscale
            holder.title.setTextColor(Color.parseColor("#999999"));

            long maxAmount = ach.getUnlockAmount();
            long amount = profile.getPlayerStatistics().getAmount(ach.getStatType());

            holder.description.setText(String.format(fa.getResources().getString(R.string.listitem_player_achievements_progress), amount, maxAmount, fa.getResources().getString(ach.getUnlockDescription())));
            holder.description.setTextColor(Color.parseColor("#999999"));

            //if(amount > 0){
            holder.progress.setVisibility(View.VISIBLE);
            holder.progress.setMax((int) maxAmount);
            holder.progress.setProgress((int) amount);
            /*}else{
                holder.progress.setVisibility(View.GONE);
            }*/


            //TODO: maybe use special not-unlocked icons?
            ColorMatrix matrix = new ColorMatrix();
            matrix.setSaturation(0);  //0 means grayscale
            ColorMatrixColorFilter cf = new ColorMatrixColorFilter(matrix);

            holder.icon.setColorFilter(cf);
        }

        return convertView;
    }

    private class ViewHolder {
        ImageView icon;
        TextView title;
        ProgressBar progress;
        TextView description;
    }
}
