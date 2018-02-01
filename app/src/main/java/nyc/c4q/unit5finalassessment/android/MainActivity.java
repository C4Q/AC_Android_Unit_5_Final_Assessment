package nyc.c4q.unit5finalassessment.android;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import nyc.c4q.unit5finalassessment.R;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final int MTA_ALERT_JOB_ID = 123;
    private static final int JOB_INTERVAL = 30 * 60 * 1000; // 30 minutes in milliseconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scheduleMtaAlertJobService();
        setupRecyclerView();
    }

    private void scheduleMtaAlertJobService() {

        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);

        JobInfo jobInfo = new JobInfo.Builder(MTA_ALERT_JOB_ID,
                new ComponentName(this, MtaAlertJobService.class))
                .setPeriodic(JOB_INTERVAL)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .build();

        if (jobScheduler != null) {
            jobScheduler.schedule(jobInfo);
        } else {
            Log.d(TAG, "scheduleMtaAlertJobService: Unable to get JobScheduler system service");
        }
    }

    private void setupRecyclerView() {
        //TODO
    }
}
