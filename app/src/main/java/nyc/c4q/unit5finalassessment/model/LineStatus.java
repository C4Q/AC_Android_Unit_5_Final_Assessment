package nyc.c4q.unit5finalassessment.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Models the service status of a subway line, or related group of subway lines e.g. BDFM.
 * <p>
 * Created by charlie on 1/29/18.
 */

public class LineStatus {
    private static final String GOOD_SERVICE = "GOOD SERVICE";
    private static final String TIME_NOT_AVAILABLE_MSG = "Time not available";

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

    /**
     * Retrieves the date/time value formatted in a specified pattern as a String
     *
     * @param pattern e.g. "dd/MM/yy hh:mm a" as laid out in {@link SimpleDateFormat}
     * @return a String formatted according to the given pattern which represents the date/time
     * value for this instance
     * @see "https://developer.android.com/reference/java/text/SimpleDateFormat.html"
     */
    public String getFormattedDateTime(final String pattern) {
        if (dateTimeInMillis == -1L) {
            return TIME_NOT_AVAILABLE_MSG;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.getDefault());
        return simpleDateFormat.format(new Date(dateTimeInMillis));
    }
}
