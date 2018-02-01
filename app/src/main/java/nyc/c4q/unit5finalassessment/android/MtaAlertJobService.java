package nyc.c4q.unit5finalassessment.android;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

import java.util.List;

import nyc.c4q.unit5finalassessment.model.LineStatus;
import nyc.c4q.unit5finalassessment.services.MtaStatusProvider;
import nyc.c4q.unit5finalassessment.services.MtaStatusProviderDbFirst;
import nyc.c4q.unit5finalassessment.services.database.MtaStatusDbService;
import nyc.c4q.unit5finalassessment.services.database.MtaStatusDbServiceSQLite;
import nyc.c4q.unit5finalassessment.services.network.MtaStatusNetworkSvc;
import nyc.c4q.unit5finalassessment.services.network.SimpleMtaStatusNetworkSvc;

/**
 * This service will refresh the app's MTA status data, then launch a notification showing some
 * of that data.
 * <p>
 * Created by charlie on 1/29/18.
 */

public class MtaAlertJobService extends JobService {

    private static final String TAG = "MtaAlertJobService";

    @Override
    public boolean onStartJob(final JobParameters jobParameters) {

        // Ideally these would be provided to the JobService via dependency injection,
        // but that is an advanced topic that you should not worry too much about for now.
        MtaStatusNetworkSvc networkSvc = new SimpleMtaStatusNetworkSvc();
        MtaStatusDbService dbService = MtaStatusDbServiceSQLite.getInstance(this);
        MtaStatusProvider statusProvider = new MtaStatusProviderDbFirst(networkSvc, dbService);

        statusProvider.getMtaStatusesAsync(true,
                new MtaStatusProvider.OnMtaStatusesReadyCallback() {
                    @Override
                    public void onMtaStatusesReady(List<LineStatus> lineStatuses) {
                        for (LineStatus lineStatus : lineStatuses) {
                            Log.d(TAG, "onMtaStatusesReady: " + lineStatus.getName() + " " +
                                    lineStatus.getStatus() + " " +
                                    lineStatus.getFormattedDateTime("M/d/yy h:mma"));

                            launchNotification(lineStatuses);

                            jobFinished(jobParameters, false);
                        }
                    }
                });
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }

    private void launchNotification(List<LineStatus> lineStatuses) {

    }
}
