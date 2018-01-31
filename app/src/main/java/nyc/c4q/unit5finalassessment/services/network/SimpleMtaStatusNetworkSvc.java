package nyc.c4q.unit5finalassessment.services.network;

import android.util.Log;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import nyc.c4q.unit5finalassessment.model.LineStatus;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Uses OkHttp to make a network request to MTA's subway status URL, receive XML, and parse into
 * LineStatus objects.
 * <p>
 * Created by charlie on 1/29/18.
 */

public class SimpleMtaStatusNetworkSvc implements MtaStatusNetworkSvc {

    private static final String TAG = "SimpleMtaStatusNetworkS";

    @Override
    public List<LineStatus> getLineStatuses() {

        Log.d(TAG, "getLineStatuses: ");
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(MTA_STATUS_URL)
                .build();

        InputStream xmlStream = null;

        try {
            Response response = client.newCall(request).execute();
            ResponseBody body = response.body();

            if (body == null) {
                throw new IOException("Unable to retrieve XML from network");
            } else {
                xmlStream = body.byteStream();

                MtaStatusXmlParser parser = new MtaStatusXmlParser();
                return parser.parseXml(xmlStream);
            }
        } catch (IOException e) {
            Log.e(TAG, "getLineStatuses: Unable to retrieve data from URL", e);
        } catch (XmlPullParserException e) {
            Log.e(TAG, "getLineStatuses: XML parsing error", e);
        } finally {
            if (xmlStream != null) {
                try {
                    xmlStream.close();
                } catch (IOException e) {
                    Log.e(TAG, "getLineStatuses: Error closing XML InputStream", e);
                }
            }
        }
        return null;
    }
}
