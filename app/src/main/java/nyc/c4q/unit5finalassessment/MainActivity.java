package nyc.c4q.unit5finalassessment;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

import nyc.c4q.unit5finalassessment.model.LineStatus;
import nyc.c4q.unit5finalassessment.services.network.SimpleMtaStatusNetworkSvc;
import nyc.c4q.unit5finalassessment.services.network.MtaStatusNetworkSvc;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final int SAMPLE_JOB_ID = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate: hello from thread " + Thread.currentThread().getName());

        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        JobInfo jobInfo = new JobInfo.Builder(SAMPLE_JOB_ID, new ComponentName(this, SampleJobService.class))
                .setMinimumLatency(3000)
                .build();
        if (jobScheduler != null) {
            jobScheduler.schedule(jobInfo);
        }

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                MtaStatusNetworkSvc networkService = new SimpleMtaStatusNetworkSvc();
                List<LineStatus> lineStatuses = networkService.getLineStatuses();
                for (LineStatus lineStatus : lineStatuses) {
                    Log.d(TAG, "doInBackground: " + lineStatus.getName() + " " + lineStatus.getStatus());
                }
                return null;
            }
        }.execute();

    }
}
