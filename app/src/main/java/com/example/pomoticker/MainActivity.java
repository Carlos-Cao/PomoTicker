package com.example.pomoticker;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final long WORK_TIME_IN_MILLIS = 1500000; // 25 minutes

    private static final long BREAK_TIME_IN_MILLIS = 300000; // 5 minutes

    private TextView textViewCountdown;

    private Button buttonStartPause;

    private Button buttonStartBreak;

    private CountDownTimer countDownTimer;

    private boolean timerRunning;

    private TextView textViewSessionLabel;

    private boolean isWorkTimer = true;

    private long timeLeftInMillis = WORK_TIME_IN_MILLIS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 1);
            }
        }

        textViewSessionLabel = findViewById(R.id.text_view_session_label);
        textViewCountdown = findViewById(R.id.text_view_countdown);
        buttonStartPause = findViewById(R.id.button_start_pause);
        Button buttonReset = findViewById(R.id.button_reset);
        buttonStartBreak = findViewById(R.id.button_start_break);
        updateSessionLabel();
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
                    sendSessionFinishedNotification("Work");
                } else {
                    isWorkTimer = true;
                    timeLeftInMillis = WORK_TIME_IN_MILLIS;
                    updateSessionLabel();
                    updateCountDownText();
                    buttonStartPause.setEnabled(true);
                    buttonStartBreak.setEnabled(false);
                    sendSessionFinishedNotification("Break");
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
        updateSessionLabel();
    }

    private void startBreak() {
        isWorkTimer = false;
        timeLeftInMillis = BREAK_TIME_IN_MILLIS;
        updateSessionLabel();
        updateCountDownText();
        buttonStartPause.setEnabled(true);
        buttonStartBreak.setEnabled(false);
        startTimer();
    }

    private void updateSessionLabel() {
        if (isWorkTimer) {
            textViewSessionLabel.setText(getResources().getString(R.string.work_session));
        } else {
            textViewSessionLabel.setText(getResources().getString(R.string.break_session));
        }
    }

    private void updateCountDownText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        textViewCountdown.setText(timeFormatted);
    }

    private void sendSessionFinishedNotification(String sessionType) {
        String channelId = "pomodoro_channel";
        String channelName = "Pomodoro Timer";
        String message = sessionType + " session finished!";

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Pomodoro Timer")
                .setContentText(message)
                .setAutoCancel(true);

        notificationManager.notify(sessionType.hashCode(), builder.build());
    }
}