package nyc.c4q.unit5finalassessment.services.network;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import nyc.c4q.unit5finalassessment.model.LineStatus;

/**
 * Utility to parse XML from http://web.mta.info/status/serviceStatus.txt into
 * {@link nyc.c4q.unit5finalassessment.model.LineStatus} objects.
 * <p>
 * Created by charlie on 1/29/18.
 */

class MtaStatusXmlParser {

    private static final String TAG = "MtaStatusXmlParser";

    private XmlPullParser parser;
    private String timeStamp, tempName, tempStatus, tempText, tempDate, tempTime;
    private List<LineStatus> lineStatuses;
    private boolean moreToParse;

    MtaStatusXmlParser() throws XmlPullParserException {
        parser = XmlPullParserFactory.newInstance().newPullParser();
        resetTempVariables();
        lineStatuses = new ArrayList<>();
        moreToParse = false;
    }

    /**
     * Takes in XML data in the form of an InputStream and parses it into a List of LineStatus
     * objects. The MTA's service status XML (http://web.mta.info/status/serviceStatus.txt)
     * is structured like this:
     * <p>
     * <service>
     * <responsecode>0</responsecode>
     * <timestamp>1/29/2018 1:53:00 PM</timestamp>
     * <subway>
     * <line>
     * <name>123</name>
     * <status>PLANNED WORK</status>
     * <text>a bunch of html would be here</text>
     * <Date>01/29/2018</Date>
     * <Time> 1:52PM</Time>
     * </line>
     * <line>
     * ...
     * </line>
     * ...
     * </subway>
     * ...
     * </service>
     *
     * @param xmlInputStream the input stream (byte stream) of the XML text to be parsed.
     * @return a List of LineStatus objects which were described by the XML
     * @throws IOException            if the XML is malformed
     * @throws XmlPullParserException if the parser is unable to read the input stream
     */
    List<LineStatus> parseXml(InputStream xmlInputStream)
            throws IOException, XmlPullParserException {

        lineStatuses.clear();
        moreToParse = true;
        resetTempVariables();

        parser.setInput(xmlInputStream, null);

        while (moreToParse) {

            switch (parser.getEventType()) {
                case XmlPullParser.END_DOCUMENT:
                    moreToParse = false;
                    break;
                case XmlPullParser.START_TAG:
                    parseStartTag();
                    break;
                case XmlPullParser.END_TAG:
                    // This method will add LineStatus objects to lineStatuses
                    parseEndTag();
                    break;
                default:
                    parser.next();
                    break;
            }
        }

        return lineStatuses;
    }

    private void parseStartTag() throws IOException, XmlPullParserException {
        String tagName = parser.getName().trim().toLowerCase();
        switch (tagName) {
            // If it's the timestamp for the whole XML file, grab it
            case "timestamp":
                timeStamp = parser.nextText();
                break;
            // If it's a "line" tag, reset our temp variables
            case "line":
                resetTempVariables();
                parser.next();
                break;
            // Or if it's a tag for something within a "line", grab its text
            case "name":
                tempName = parser.nextText();
                break;
            case "status":
                tempStatus = parser.nextText();
                break;
            case "text":
                tempText = parser.nextText();
                break;
            case "date":
                tempDate = parser.nextText();
                break;
            case "time":
                tempTime = parser.nextText();
                break;
            default:
                parser.next();
        }
    }

    private void parseEndTag() throws IOException, XmlPullParserException {
        switch (parser.getName().trim().toLowerCase()) {
            // If we just finished a "line" it's time to construct a LineStatus object!
            case "line":
                lineStatuses.add(new LineStatus(tempName, tempStatus, tempText,
                        getDateTimeInMillis()));
                parser.next();
                break;
            // If we're done w/ "subway" then break the while loop.
            // Ignore buses, MetroNorth, LIRR, etc.
            case "subway":
                moreToParse = false;
                break;
            default:
                parser.next();
                break;
        }
    }

    /**
     * Get date/time as a long representing milliseconds since the Unix epoch
     * https://en.wikipedia.org/wiki/Unix_time
     */
    private long getDateTimeInMillis() {
        if (tempDate != null && tempDate.length() > 0
                && tempTime != null && tempTime.length() > 0) {
            // Use the date & time specific to that "line" object if available.
            return parseDateTimeString(tempDate + tempTime, "MM/dd/yyyy hh:mma");
        } else {
            // If not available, use the timestamp from the start of the XML file.
            return parseDateTimeString(timeStamp, "MM/dd/yyyy hh:mm:ss a");
        }
    }

    private long parseDateTimeString(String dateTimeString, String formatString) {
        // Specify the date format used by the MTA in their XML data
        SimpleDateFormat dateFormat = new SimpleDateFormat(formatString, Locale.getDefault());

        try {
            return dateFormat.parse(dateTimeString).getTime();
        } catch (ParseException e) {
            Log.e(TAG, "parseDateTimeString: Unable to parse " + dateTimeString, e);
            return -1L;
        }
    }

    private void resetTempVariables() {
        tempName = tempStatus = tempText = tempDate = tempTime = null;
    }
}
