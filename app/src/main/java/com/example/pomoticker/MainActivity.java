package com.example.pomoticker;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final long WORK_TIME_IN_MILLIS = 1500000; // 25 minutes

    private static final long BREAK_TIME_IN_MILLIS = 300000; // 5 minutes

    private TextView textViewCountdown;

    private Button buttonStartPause;

    private Button buttonStartBreak;

    private CountDownTimer countDownTimer;

    private boolean timerRunning;

    private boolean isWorkTimer = true;

    private long timeLeftInMillis = WORK_TIME_IN_MILLIS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewCountdown = findViewById(R.id.text_view_countdown);
        buttonStartPause = findViewById(R.id.button_start_pause);
        Button buttonReset = findViewById(R.id.button_reset);
        buttonStartBreak = findViewById(R.id.button_start_break);

        updateCountDownText();
        buttonStartBreak.setEnabled(false);

        buttonStartPause.setOnClickListener(v -> {
            if (timerRunning) {
                pauseTimer();
            } else {
                startTimer();
            }
        });

        buttonReset.setOnClickListener(v -> resetTimer());

        buttonStartBreak.setOnClickListener(v -> startBreak());
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timerRunning = false;
                buttonStartPause.setText(getResources().getString(R.string.start));
                if (isWorkTimer) {
                    buttonStartBreak.setEnabled(true);
                    buttonStartPause.setEnabled(false);
                }
            }
        }.start();

        timerRunning = true;
        buttonStartPause.setText(getResources().getString(R.string.pause));
        buttonStartBreak.setEnabled(false);
    }

    private void pauseTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        timerRunning = false;
        buttonStartPause.setText(getResources().getString(R.string.start));
    }

    private void resetTimer() {
        if (isWorkTimer) {
            timeLeftInMillis = WORK_TIME_IN_MILLIS;
        } else {
            timeLeftInMillis = BREAK_TIME_IN_MILLIS;
        }
        updateCountDownText();
        pauseTimer();
        buttonStartPause.setEnabled(true);
        buttonStartBreak.setEnabled(false);
    }

    private void startBreak() {
        isWorkTimer = false;
        timeLeftInMillis = BREAK_TIME_IN_MILLIS;
        updateCountDownText();
        buttonStartPause.setEnabled(true);
        buttonStartBreak.setEnabled(false);
        startTimer();
    }

    private void updateCountDownText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        textViewCountdown.setText(timeFormatted);
    }
}