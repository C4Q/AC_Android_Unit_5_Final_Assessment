package nyc.c4q.unit5finalassessment.services.network;

import java.util.List;

import nyc.c4q.unit5finalassessment.model.LineStatus;

/**
 * Defines how a network service providing subway status data will work. Note that this interface
 * does not include callbacks, and assumes its methods will return synchronously. Threading must
 * be handled elsewhere.
 * <p>
 * Created by charlie on 1/29/18.
 */

public interface MtaStatusNetworkSvc {
    String MTA_STATUS_URL = "http://web.mta.info/status/serviceStatus.txt";

    List<LineStatus> getLineStatuses();
}
