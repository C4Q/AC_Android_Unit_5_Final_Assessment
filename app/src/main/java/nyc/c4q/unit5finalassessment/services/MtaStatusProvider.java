package nyc.c4q.unit5finalassessment.services;

import java.util.List;

import nyc.c4q.unit5finalassessment.model.LineStatus;

/**
 * Defines how a service providing subway status data will work. Note that this interface includes
 * callbacks, and assumes its methods will return asynchronously. Threading must be handled by
 * any classes implementing this interface.
 * <p>
 * Created by charlie on 1/31/18.
 */

public interface MtaStatusProvider {
    void getMtaStatusesAsync(boolean forceUpdateFromNetwork, OnMtaStatusesReadyCallback callback);

    interface OnMtaStatusesReadyCallback {
        void onMtaStatusesReady(List<LineStatus> lineStatuses);
    }
}
