package nyc.c4q.unit5finalassessment.services.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import nyc.c4q.unit5finalassessment.model.LineStatus;

/**
 * Implentation of the Mta Status Database Service interface, utilizing SQLite
 * <p>
 * Created by charlie on 1/31/18.
 */

public class MtaStatusDbServiceSQLite extends SQLiteOpenHelper implements MtaStatusDbService {

    //------------------------------------------------------------------------------------
    // Constants to describe the database, table, and columns
    //------------------------------------------------------------------------------------
    private static final String DB_NAME = "MTA_STATUS_DB";
    private static final int DB_VERSION = 1;

    private static final String TABLE_NAME = "MTA_STATUS";
    private static final String COL_NAME_LINE_NAME = "LINE_NAME";
    private static final String COL_NAME_STATUS = "STATUS";
    private static final String COL_NAME_TEXT_HTML = "TEXT_HTML";
    private static final String COL_NAME_TIME_IN_MILLIS = "TIME_IN_MILLIS";

    private static final String CREATE = "CREATE TABLE " + TABLE_NAME + " (" +
            "_ID INTEGER PRIMARY KEY," +
            COL_NAME_LINE_NAME + " TEXT," +
            COL_NAME_STATUS + " TEXT," +
            COL_NAME_TEXT_HTML + " TEXT," +
            COL_NAME_TIME_IN_MILLIS + " INTEGER)";

    private static final String DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;


    //------------------------------------------------------------------------------------
    // Make this class a singleton - only one instance allowed
    //------------------------------------------------------------------------------------
    private static MtaStatusDbServiceSQLite instance;

    public static MtaStatusDbServiceSQLite getInstance(Context context) {
        if (instance == null) {
            instance = new MtaStatusDbServiceSQLite(context.getApplicationContext());
        }
        return instance;
    }

    private MtaStatusDbServiceSQLite(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    //------------------------------------------------------------------------------------
    // Implementation of abstract methods from SQLiteOpenHelper
    //------------------------------------------------------------------------------------
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DROP);
        onCreate(sqLiteDatabase);
    }

    //------------------------------------------------------------------------------------
    // Implementation of methods declared in the MtaStatusDbService interface
    //------------------------------------------------------------------------------------
    @Override
    public List<LineStatus> getLineStatuses() {
        SQLiteDatabase db = getReadableDatabase();

        List<LineStatus> lineStatuses = new ArrayList<>();

        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            lineStatuses.add(new LineStatus(
                    cursor.getString(cursor.getColumnIndex(COL_NAME_LINE_NAME)),
                    cursor.getString(cursor.getColumnIndex(COL_NAME_STATUS)),
                    cursor.getString(cursor.getColumnIndex(COL_NAME_TEXT_HTML)),
                    cursor.getLong(cursor.getColumnIndex(COL_NAME_TIME_IN_MILLIS))
            ));
            cursor.moveToNext();
        }

        cursor.close();
        db.close();
        return lineStatuses;
    }

    @Override
    public void updateLineStatuses(List<LineStatus> lineStatuses) {
        SQLiteDatabase db = getWritableDatabase();

        // First, delete existing data in the table
        db.delete(TABLE_NAME, null, null);

        // Then, save the new items
        for (LineStatus lineStatus : lineStatuses) {

            ContentValues values = new ContentValues();
            values.put(COL_NAME_LINE_NAME, lineStatus.getName());
            values.put(COL_NAME_STATUS, lineStatus.getStatus());
            values.put(COL_NAME_TEXT_HTML, lineStatus.getTextHtml());
            values.put(COL_NAME_TIME_IN_MILLIS, lineStatus.getDateTimeInMillis());

            db.insert(TABLE_NAME, null, values);
        }

        db.close();
    }
}
