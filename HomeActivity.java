package com.example.qrstaff;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    private MaterialCalendarView calendarView;
    private Button scanQrButton;
    private DatabaseReference databaseReference;
    private String punchInTime, punchOutTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize Firebase Database
        databaseReference = FirebaseDatabase.getInstance().getReference("attendance");

        calendarView = findViewById(R.id.calendarView);
        scanQrButton = findViewById(R.id.scanQrButton);

        scanQrButton.setOnClickListener(v -> initiateScan());

        // Set today's date as selected in the calendar
        calendarView.setSelectedDate(CalendarDay.today());
    }

    private void initiateScan() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        integrator.setPrompt("Scan a QR Code");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(true);
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                // Handle QR code scan result
                handleQrCodeResult(result.getContents());
            }
        }
    }

    private void handleQrCodeResult(String qrCodeContent) {
        // Simulate checking the QR code content for a valid user
        if (qrCodeContent.equals("https://quadcrag.com")) {
            punchInTime = getCurrentTime();
            Toast.makeText(this, "Punched In at " + punchInTime, Toast.LENGTH_SHORT).show();

            // Save punch-in time to Firebase
            saveToFirebase("in", punchInTime);

            // Highlight the current date in green
            highlightSelectedDate();

        } else {
            Toast.makeText(this, "Invalid QR Code", Toast.LENGTH_SHORT).show();
        }
    }

    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    private void saveToFirebase(String status, String punchInTime) {
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        Map<String, String> attendanceData = new HashMap<>();
        attendanceData.put("status", status);
        attendanceData.put("punchInTime", punchInTime);

        // Save data under the current date in Firebase
        databaseReference.child(date).setValue(attendanceData)
                .addOnSuccessListener(aVoid -> Toast.makeText(HomeActivity.this, "Data Saved", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(HomeActivity.this, "Failed to Save Data", Toast.LENGTH_SHORT).show());
    }

    private void highlightSelectedDate() {
        calendarView.addDecorator(new DayViewDecorator() {
            @Override
            public boolean shouldDecorate(CalendarDay day) {
                // Highlight the current date
                return day.equals(CalendarDay.today());
            }

            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void decorate(DayViewFacade view) {
                // Change the background to green for the selected date
                view.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_circles));
            }
        });
    }
}
