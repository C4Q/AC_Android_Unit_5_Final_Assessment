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
    private static final int JOB_INTERVAL_MAX = 60 * 60 * 1000; // 60 minutes in milliseconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scheduleMtaAlertJobService();
        setupRecyclerView();
    }

    private void scheduleMtaAlertJobService() {

        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);

        // I decided not to use setPeriodic(JOB_INTERVAL) because there's no way to delay
        // the initial execution of a periodic job, and I don't want a notification to launch
        // right away when the app is opened. Instead, I set the minimum latency (delay) to be
        // JOB_INTERVAL, and then in the JobService when the work is done and jobFinished() is
        // called, I pass in true for the needsReschedule parameter so it reschedules.
        JobInfo jobInfo = new JobInfo.Builder(MTA_ALERT_JOB_ID,
                new ComponentName(this, MtaAlertJobService.class))
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPersisted(true)
                .setMinimumLatency(JOB_INTERVAL)
                .setOverrideDeadline(JOB_INTERVAL_MAX)
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
