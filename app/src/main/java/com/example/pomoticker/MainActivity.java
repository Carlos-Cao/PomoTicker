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

public class MainActivity extends AppCompatActivity {

    private TextView textViewCountdown;

    private TextView textViewSessionLabel;

    private Button buttonStartPause;

    private Button buttonStartBreak;

    private PomodoroTimer pomodoroTimer;

    private CountDownTimer countDownTimer;

    private boolean timerRunning;

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

        pomodoroTimer = new PomodoroTimer();

        textViewSessionLabel = findViewById(R.id.text_view_session_label);
        textViewCountdown = findViewById(R.id.text_view_countdown);
        buttonStartPause = findViewById(R.id.button_start_pause);
        Button buttonReset = findViewById(R.id.button_reset);
        buttonStartBreak = findViewById(R.id.button_start_break);
        updateUI();
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
        countDownTimer = new CountDownTimer(pomodoroTimer.getTimeLeftInMillis(), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                pomodoroTimer.setTimeLeftInMillis(millisUntilFinished);
                updateCountdownText();
            }

            @Override
            public void onFinish() {
                timerRunning = false;
                buttonStartPause.setText(getString(R.string.start));
                if (pomodoroTimer.isWorkTimer()) {
                    buttonStartBreak.setEnabled(true);
                    buttonStartPause.setEnabled(false);
                    sendSessionFinishedNotification("Work");
                } else {
                    pomodoroTimer.switchToWork();
                    updateUI();
                    buttonStartPause.setEnabled(true);
                    buttonStartBreak.setEnabled(false);
                    sendSessionFinishedNotification("Break");
                }
            }
        }.start();

        timerRunning = true;
        buttonStartPause.setText(getString(R.string.pause));
        buttonStartBreak.setEnabled(false);
    }

    private void pauseTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        timerRunning = false;
        buttonStartPause.setText(getString(R.string.start));
    }

    private void resetTimer() {
        pomodoroTimer.reset();
        updateUI();
        pauseTimer();
        buttonStartPause.setEnabled(true);
        buttonStartBreak.setEnabled(false);
    }

    private void startBreak() {
        pomodoroTimer.startBreak();
        updateUI();
        buttonStartPause.setEnabled(true);
        buttonStartBreak.setEnabled(false);
        startTimer();
    }

    private void updateUI() {
        updateSessionLabel();
        updateCountdownText();
    }

    private void updateSessionLabel() {
        textViewSessionLabel.setText(pomodoroTimer.getSessionLabel());
    }

    private void updateCountdownText() {
        textViewCountdown.setText(pomodoroTimer.getFormattedTime());
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