package com.vsb.sim0323.sokoban;


import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LevelRecycleViewAdapter extends RecyclerView.Adapter<LevelRecycleViewAdapter.ViewHolder> {


    //clients can implement this method to respond to click events
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnScoresClickListener {
        void onClick(View view, int position);
    }

    public static class ItemData {
        //I will just assign value directly instead of using setters
        private int score;
        private boolean completed;
        private Level level;

        public ItemData(Level level, int score, boolean isCompleted) {
            this.level = level;
            this.score = score;
            this.completed = isCompleted;
        }
    }

    private List<ItemData> dataSet;
    private OnItemClickListener onItemClickListener;
    private OnScoresClickListener onScoresClickListener;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView levelText;
        private final TextView scoreText;
        private final BaseSokoView baseSokoView;
        private final Button scoresBtn;
        private final View view;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            view.setOnClickListener(this);
            levelText = (TextView) view.findViewById(R.id.levelText);
            scoreText = (TextView) view.findViewById(R.id.scoreText);
            baseSokoView = (BaseSokoView) view.findViewById(R.id.sokoImage);
            scoresBtn = (Button) view.findViewById(R.id.scoresBtn);
            scoresBtn.setOnClickListener((v) -> {
                if(onScoresClickListener != null) {
                    onScoresClickListener.onClick(scoresBtn, getAdapterPosition());
                }
            });
            this.view = view;
        }

        public TextView getLevelText() {
            return levelText;
        }

        public TextView getScoreText() {
            return scoreText;
        }

        public BaseSokoView getBaseSokoView() {
            return baseSokoView;
        }

        public void setBgColor(String color) {
            view.setBackgroundColor(Color.parseColor(color));
        }

        @Override
        public void onClick(View view) {
            if(onItemClickListener != null) {
                onItemClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     * @param dataSet
     */
    public LevelRecycleViewAdapter(List<ItemData> dataSet) {
        this.dataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.level_list_item, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        ItemData itemData = dataSet.get(position);
        viewHolder.getLevelText().setText(itemData.level.getName());
        viewHolder.getScoreText().setText("Highscore: " + String.valueOf(itemData.score));
        viewHolder.getBaseSokoView().setLevel(itemData.level);

        //if level completed, change bg to green
        if(itemData.completed) {
            viewHolder.setBgColor("#80ff80");
            Log.d("greenColor", "onBindViewHolder: " + itemData.level.getName() + ": " + itemData.completed);
        }
        else {
            //the green color otherwise somehow stays on some views that shouldn't have it
            viewHolder.setBgColor("#fafafa");
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return dataSet.size();
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnScoresClickListener(OnScoresClickListener onScoresClickListener) {
        this.onScoresClickListener = onScoresClickListener;
    }

    public Level getLevel(int position) {
        return dataSet.get(position).level;
    }

    public void setData(List<ItemData> itemsData) {
        dataSet = itemsData;
        notifyDataSetChanged();
    }


}

