package com.vsb.sim0323.sokoban;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.List;

public class LevelSelectionActivity extends AppCompatActivity {

    /**
     * A solution without db would be to make a serializable class that would have the list of levels
     * and this would be sent together with an index of the current level to the MainActivity
     */

    private List<LevelRecycleViewAdapter.ItemData> itemsData;
    public static final int NO_LEVEL = -2;

    private RecyclerView levelsRecyclerView;
    private LevelRecycleViewAdapter adapter;
    private SokoDB sokoDB;
    private Button refreshBtn;
    private Button scoresBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_selection);

        AssetManager assetManager = getAssets();
        LevelLoader levelLoader = new LevelLoader(assetManager);
        //for now the db will be filled with these levels only on the first creation of the db
        sokoDB = new SokoDB(this, levelLoader);

        itemsData = sokoDB.getItemDataForAllLevels();
        levelsRecyclerView = findViewById(R.id.levelsRecyclerView);

        adapter = new LevelRecycleViewAdapter(itemsData);
        adapter.setOnItemClickListener((view, position) -> startLevel(position));
        adapter.setOnScoresClickListener(this::showScores);
        levelsRecyclerView.setLayoutManager(new LinearLayoutManager(this)); //needs to be done here or in recyclerView xml
        levelsRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
        levelsRecyclerView.setAdapter(adapter);

        refreshBtn = findViewById(R.id.refreshBtn);
        refreshBtn.setOnClickListener(this::refresh);

    }

    private void showScores(View view, int position) {
        Level level = adapter.getLevel(position);
        Intent intent = new Intent(this, ScoresActivity.class);
        intent.putExtra("level", level);
        startActivity(intent);
    }

    private void refresh(View view) {
        itemsData = sokoDB.getItemDataForAllLevels();
        adapter.setData(itemsData);
    }


    private void startLevel(int itemPosition) {
        Level level = adapter.getLevel(itemPosition);
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("level", level);
        startActivity(intent);
    }
}