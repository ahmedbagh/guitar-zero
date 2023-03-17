package helloandroid.ut3.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class ScoreActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_score);

        SharedPreferences saved_values = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Integer currentScore = saved_values.getInt("current_score", 999999);
        Integer bestScore = saved_values.getInt("best_score", 999999);

        System.out.println("current" + currentScore);
        System.out.println("best" + bestScore);
        if (currentScore <= bestScore) {
            bestScore = currentScore;
            SharedPreferences.Editor editor = saved_values.edit();
            editor.putInt("best_score", bestScore);
            editor.commit();
        }

        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String BestTime = sdf.format(new Date(bestScore));

        TextView scoreText = findViewById(R.id.BestScore);
        scoreText.setText("The best score is: \n" + BestTime);

        String CurrentTime = sdf.format(new Date(currentScore));
        TextView currentScoreText = findViewById(R.id.CurrentScore);
        currentScoreText.setText("Your score was: \n" + CurrentTime);

        initListener();
    }

    private void initListener() {
        Button returnHomeBtn = findViewById(R.id.returnHomeBtn);

        returnHomeBtn.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), MainActivity.class);
            startActivity(intent);
        });
    }

    public ScoreActivity() {
    }
}
