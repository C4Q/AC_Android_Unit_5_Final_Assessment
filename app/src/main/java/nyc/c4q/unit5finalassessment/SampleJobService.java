package nyc.c4q.unit5finalassessment;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.AsyncTask;
import android.util.Log;

import java.lang.ref.WeakReference;

/**
 * Created by charlie on 1/29/18.
 */

public class SampleJobService extends JobService {

    private static final String TAG = "SampleJobService";

    private SampleAsyncTask sampleAsyncTask;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        sampleAsyncTask = new SampleAsyncTask(this);
        sampleAsyncTask.execute(jobParameters);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        if (sampleAsyncTask != null) {
            sampleAsyncTask.cancel(true);
        }
        return false;
    }

    private static class SampleAsyncTask extends AsyncTask<JobParameters, Void ,JobParameters> {

        private final WeakReference<JobService> serviceWeakReference;

        SampleAsyncTask(JobService service) {
            serviceWeakReference = new WeakReference<>(service);
        }

        @Override
        protected JobParameters doInBackground(JobParameters... jobParameters) {
            Log.d(TAG, "onStartJob: hello from thread " + Thread.currentThread().getName());
            return jobParameters[0];
        }

        @Override
        protected void onPostExecute(JobParameters jobParameters) {
            JobService service = serviceWeakReference.get();
            if (service != null) {
                service.jobFinished(jobParameters, false);
            }
        }
    }
}
