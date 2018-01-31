package nyc.c4q.unit5finalassessment.model;

/**
 * Models the service status of a subway line, or related group of subway lines e.g. BDFM.
 * <p>
 * Created by charlie on 1/29/18.
 */

public class LineStatus {
    private static final String GOOD_SERVICE = "GOOD SERVICE";

    private final String name, status, textHtml;
    private final long dateTimeInMillis;

    public LineStatus(String name, String status, String textHtml, long dateTimeInMillis) {
        this.name = name.trim();
        this.status = status.trim();
        this.textHtml = textHtml;
        this.dateTimeInMillis = dateTimeInMillis;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public String getTextHtml() {
        return textHtml;
    }

    public long getDateTimeInMillis() {
        return dateTimeInMillis;
    }

    public boolean hasGoodService() {
        return status.toUpperCase().equals(GOOD_SERVICE);
    }
}
