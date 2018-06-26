package alexander.korovin.com.smartlabels.Utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LabelsBDHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "smartlabels.db";
    private static final int DATABASE_VERSION = 1;


    public LabelsBDHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        LablesDataBase.createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
