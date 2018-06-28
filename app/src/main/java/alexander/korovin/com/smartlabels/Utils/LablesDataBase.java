package alexander.korovin.com.smartlabels.Utils;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;

import java.util.ArrayList;

import alexander.korovin.com.smartlabels.Models.Label;

public class LablesDataBase {
    private static final String TABLE_NAME = "Notes";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_LABEL_TITLE = "Title";
    private static final String COLUMN_LABEL_DESCRIPTION = "Description";
    private static ArrayList<Label> labels;

    public static void createTable(SQLiteDatabase database) {
        database.execSQL("CREATE TABLE " + TABLE_NAME + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_LABEL_TITLE + " TEXT, " + COLUMN_LABEL_DESCRIPTION + " TEXT);");
    }

    public static void addLabel(Label label, SQLiteDatabase database) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_LABEL_TITLE, label.getLabelHeader());
        values.put(COLUMN_LABEL_DESCRIPTION, label.getLabelDescription());
        database.insert(TABLE_NAME, null, values);
    }

    public static void removeLabel(int labelId, SQLiteDatabase database) {
        database.delete(TABLE_NAME, COLUMN_ID + " = " + labelId, null);
    }

    public static void editLabel(int labelToEditId, Label newLabel, SQLiteDatabase database) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_LABEL_TITLE, newLabel.getLabelHeader());
        values.put(COLUMN_LABEL_DESCRIPTION, newLabel.getLabelDescription());
        database.update(TABLE_NAME, values, COLUMN_ID + " = " + labelToEditId, null);
    }

    public static ArrayList<Label> getLabelsFromDataBase(SQLiteDatabase database) {
        Cursor cursor = database.query(TABLE_NAME, null, null, null, null, null, null);
        labels = new ArrayList<>();
        if (cursor != null & cursor.moveToFirst()) {
            int labelIDColumnIndex = cursor.getColumnIndex(COLUMN_ID);
            int labelTitleColumnIndex = cursor.getColumnIndex(COLUMN_LABEL_TITLE);
            int labelDescriptionColumnIndex = cursor.getColumnIndex(COLUMN_LABEL_DESCRIPTION);
            do {
                Label label = new Label(cursor.getString(labelTitleColumnIndex), cursor.getString(labelDescriptionColumnIndex));
                label.setLabelId(cursor.getInt(labelIDColumnIndex));
                labels.add(label);
            } while (cursor.moveToNext());
            try {
                cursor.close();
            } catch (Exception e) {

            }
        }
        return labels == null ? new ArrayList<Label>(0) : labels;
    }
}
