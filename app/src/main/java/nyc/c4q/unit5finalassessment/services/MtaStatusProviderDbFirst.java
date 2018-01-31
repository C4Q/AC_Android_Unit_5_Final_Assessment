package nyc.c4q.unit5finalassessment.services;

import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import nyc.c4q.unit5finalassessment.model.LineStatus;
import nyc.c4q.unit5finalassessment.services.database.MtaStatusDbService;
import nyc.c4q.unit5finalassessment.services.network.MtaStatusNetworkSvc;

/**
 * This implementation of the MtaStatusProvider interface coordinates between network and local
 * database sources, and ensures those tasks will be performed on background threads, via AsyncTask.
 * <p>
 * Created by charlie on 1/31/18.
 */

public class MtaStatusProviderDbFirst implements MtaStatusProvider {

    private MtaStatusNetworkSvc mtaStatusNetworkSvc;
    private MtaStatusDbService mtaStatusDbService;

    public MtaStatusProviderDbFirst(MtaStatusNetworkSvc mtaStatusNetworkSvc,
                                    MtaStatusDbService mtaStatusDbService) {
        this.mtaStatusNetworkSvc = mtaStatusNetworkSvc;
        this.mtaStatusDbService = mtaStatusDbService;
    }

    @Override
    public void getMtaStatusesAsync(boolean forceUpdateFromNetwork,
                                    OnMtaStatusesReadyCallback callback) {
        new GetMtaStatusesAsyncTask(mtaStatusNetworkSvc, mtaStatusDbService, callback)
                .execute(forceUpdateFromNetwork);
    }

    private static class GetMtaStatusesAsyncTask extends AsyncTask<Boolean, Void, List<LineStatus>> {
        private static final String TAG = "GetMtaStatusesAsyncTask";

        private MtaStatusNetworkSvc mtaStatusNetworkSvc;
        private MtaStatusDbService mtaStatusDbService;
        private OnMtaStatusesReadyCallback callback;

        GetMtaStatusesAsyncTask(MtaStatusNetworkSvc mtaStatusNetworkSvc,
                                       MtaStatusDbService mtaStatusDbService,
                                       OnMtaStatusesReadyCallback callback) {
            this.mtaStatusNetworkSvc = mtaStatusNetworkSvc;
            this.mtaStatusDbService = mtaStatusDbService;
            this.callback = callback;
        }

        @Override
        protected List<LineStatus> doInBackground(Boolean... booleans) {

            Log.d(TAG, "doInBackground: hello from thread " + Thread.currentThread().getName());

            boolean forceUpdateFromNetwork = booleans[0];

            if (forceUpdateFromNetwork) {
                return getFromNetworkSaveInDb();
            } else {
                List<LineStatus> lineStatuses = mtaStatusDbService.getLineStatuses();
                if (lineStatuses.size() == 0) {
                    return getFromNetworkSaveInDb();
                } else {
                    return lineStatuses;
                }
            }
        }

        @Override
        protected void onPostExecute(List<LineStatus> lineStatuses) {
            callback.onMtaStatusesReady(lineStatuses);
        }

        private List<LineStatus> getFromNetworkSaveInDb() {
            List<LineStatus> lineStatuses = mtaStatusNetworkSvc.getLineStatuses();
            mtaStatusDbService.updateLineStatuses(lineStatuses);
            return lineStatuses;
        }
    }
}
