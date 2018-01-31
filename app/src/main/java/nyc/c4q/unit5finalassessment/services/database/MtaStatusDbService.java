package nyc.c4q.unit5finalassessment.services.database;

import java.util.List;

import nyc.c4q.unit5finalassessment.model.LineStatus;

/**
 * Defines how a database service locally caching subway status data will work. Note that this
 * interface does not include callbacks, and assumes its methods will return synchronously.
 * Threading must be handled elsewhere.
 * <p>
 * Created by charlie on 1/31/18.
 */

public interface MtaStatusDbService {

    List<LineStatus> getLineStatuses();

    /**
     * This method must clear out the local database, then re-populate it will the provided data.
     * @param lineStatuses are the new data objects to be saved in the database.
     */
    void updateLineStatuses(List<LineStatus> lineStatuses);
}
