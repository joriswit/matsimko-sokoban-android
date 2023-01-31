package com.vsb.sim0323.sokoban;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

public class ScoresActivity extends AppCompatActivity {

    private SokoDB sokoDB;
    private Level level;
    private TextView scoresLevelText;
    private BaseSokoView baseSokoView;
    private RecyclerView recyclerView;
    private TextRecycleViewAdapter adapter;
    private TextView completedText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);

        scoresLevelText = findViewById(R.id.scoresLevelText);
        baseSokoView = findViewById(R.id.scoresBaseSokoView);
        completedText = findViewById(R.id.scoresCompletedText);

        sokoDB = new SokoDB(this, null);
        level = (Level) getIntent().getSerializableExtra("level");
        baseSokoView.setLevel(level);
        scoresLevelText.setText(level.getName());
        List<Score> scores = sokoDB.getScoresForLevel(level.getId());

        String[] scoresStrings = new String[scores.size()];
        boolean completed = false;
        for(int i = 0 ; i < scores.size(); i++) {
            scoresStrings[i] = String.valueOf(scores.get(i).getScore());
            completed = completed || scores.get(i).isCompleted();
        }

        if(completed) {
            completedText.setText("Completed: YES");
        }
        else {
            completedText.setText("Completed: NO");
        }

        recyclerView = findViewById(R.id.scoresRecyclerView);
        adapter = new TextRecycleViewAdapter(scoresStrings);
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); //needs to be done here or in recyclerView xml
        recyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);
    }
}