package com.vsb.sim0323.sokoban;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button resetBtn;
    Button nextLvlBtn;
    Button prevLvlBtn;
    SokoView sokoView;

    Level level;
    private SokoDB sokoDB;
    boolean scoreSaved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        level = (Level) getIntent().getSerializableExtra("level");

        sokoView = findViewById(R.id.sokoView);
        sokoView.addOnWinListener(this::onWin);
        resetBtn = findViewById(R.id.resetBtn);
        resetBtn.setOnClickListener(this::resetLevel);
        nextLvlBtn = findViewById(R.id.nextLvlBtn);
        nextLvlBtn.setOnClickListener(this::nextLevel);
        prevLvlBtn = findViewById(R.id.prevLvlBtn);
        prevLvlBtn.setOnClickListener(this::prevLevel);

        //the db was definitely created by now so I can pass null as levelLoader
        sokoDB = new SokoDB(this, null);
        scoreSaved = false;

        sokoView.setLevel(level);
    }

    private void onWin() {

        scoreSaved = true; //prevent score from being saved again
        sokoDB.insertScore(level.getId(), sokoView.getScore(), true);
        Toast.makeText(this, "Congratulations, you completed the level!", Toast.LENGTH_LONG).show();
    }

    private void saveScoreIfNotSaved() {
        //score is saved when the user has already won, otherwise here upon button actions or onPause
        int score = sokoView.getScore();
        if(!scoreSaved && score > 0) {
            //save current score
            sokoDB.insertScore(level.getId(), score, false);
        }
    }

    private void prevLevel(View view) {
        startLevel(level.getId() - 1);
    }
    private void nextLevel(View view) {
        startLevel(level.getId() + 1);
    }
    private void startLevel(int id) {
        saveScoreIfNotSaved();

        //change lvl
        scoreSaved = false;
        Level newLevel = sokoDB.getLevel(id);
        if(newLevel != null) {
            level = newLevel;
            sokoView.setLevel(level);
        }
        else {
            Toast.makeText(this, "No level found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveScoreIfNotSaved();
    }

    private void resetLevel(View view) {
        saveScoreIfNotSaved();
        sokoView.resetLevel();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Intent solverIntent = new Intent("nl.joriswit.sokosolver.SOLVE");
        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(solverIntent, PackageManager.MATCH_DEFAULT_ONLY);
        boolean isSolverInstalled = activities.size() > 0;
        menu.findItem(R.id.solverMenuItem).setEnabled(isSolverInstalled);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.levelsMenuItem:
                Intent intent = new Intent(this, LevelSelectionActivity.class);
                startActivity(intent);
                return true;
            case R.id.solverMenuItem:
                Intent solverIntent = new Intent("nl.joriswit.sokosolver.SOLVE");
                solverIntent.putExtra("LEVEL", new LevelLoader(null).saveLevel(level));
                startActivityForResult(solverIntent, SOLVER_REQUEST_CODE);
                return true;
            default:
                return true;
        }
    }

    public static final int SOLVER_REQUEST_CODE = 9522;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SOLVER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            String result = data.getStringExtra("SOLUTION");
            sokoView.setControls(new ReplaySolutionSokoControls(result));
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
