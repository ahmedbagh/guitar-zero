package helloandroid.ut3.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        initListener();
    }

    private void initListener() {
        Button playBtn = findViewById(R.id.playBtn);
        Button scoreBtn = findViewById(R.id.scoreBtn);
        Button guideBtn = findViewById(R.id.guideBtn);

        playBtn.setOnClickListener(v -> {
//            Intent intent = new Intent(v.getContext(), GameActivity.class);
//            startActivity(intent);
            Toast.makeText(this, "Start game",
                    Toast.LENGTH_LONG).show();
        });


        scoreBtn.setOnClickListener(v -> {
//            Intent intent = new Intent(v.getContext(), ScoreActivity.class);
//            startActivity(intent);
            Toast.makeText(this, "Score",
                    Toast.LENGTH_LONG).show();
        });

        guideBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, GuideActivity.class);
            startActivity(intent);
        });
    }
}