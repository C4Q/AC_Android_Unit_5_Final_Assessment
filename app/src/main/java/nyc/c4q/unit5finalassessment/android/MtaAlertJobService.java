package nyc.c4q.unit5finalassessment.android;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.List;

import nyc.c4q.unit5finalassessment.R;
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
    private static final String NOTIFICATION_CHANNEL = "MTA_Status_Notifications";
    private static final int NOTIFICATION_ID = 23456;
    private static final String NOTIFICATION_TITLE = "MTA Subway Status Alerts";
    private static final String NOTIFICATION_TEXT = "Current alerts:";
    public static final String DATE_FORMAT_PATTERN = "M/d/yy h:mma";

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
                                    lineStatus.getFormattedDateTime(DATE_FORMAT_PATTERN));

                            launchNotification(lineStatuses);

                            jobFinished(jobParameters, true);
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
        NotificationManager manager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (manager == null) {
            Log.d(TAG, "launchNotification: Unable to retrieve Notification System Service");
            return;
        }

        // If API is 26 or above, must set up a notification channel
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            manager.createNotificationChannel(new NotificationChannel(
                    NOTIFICATION_CHANNEL, NOTIFICATION_CHANNEL,
                    NotificationManager.IMPORTANCE_DEFAULT));
        }

        // Add MTA service status data to an InboxStyle object to show a list in the notification
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        for (LineStatus lineStatus : lineStatuses) {
            if (!lineStatus.hasGoodService()) {
                inboxStyle.addLine(lineStatus.getName() + " " +
                        lineStatus.getStatus() + " " +
                        lineStatus.getFormattedDateTime(DATE_FORMAT_PATTERN));
            }
        }

        // Create a pending intent so the MainActivity launches when notification is tapped
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                (int) System.currentTimeMillis(),
                new Intent(this, MainActivity.class),
                PendingIntent.FLAG_CANCEL_CURRENT
        );

        // Build the actual notification
        Notification notification = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL)
                .setSmallIcon(R.drawable.ic_launcher_foreground) //TODO replace w/ real icon
                .setContentTitle(NOTIFICATION_TITLE)
                .setContentText(NOTIFICATION_TEXT)
                .setStyle(inboxStyle)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();

        // Finally, display the notification
        manager.notify(NOTIFICATION_ID, notification);
    }
}
