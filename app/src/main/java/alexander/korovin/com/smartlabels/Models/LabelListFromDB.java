package alexander.korovin.com.smartlabels.Models;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import alexander.korovin.com.smartlabels.Utils.LabelsBDHelper;
import alexander.korovin.com.smartlabels.Utils.LablesDataBase;

public class LabelListFromDB {
    private static ArrayList<Label> labels;
    private static SQLiteDatabase database;
    private static Context context;

    public LabelListFromDB(Context context) {
        this.context = context;
    }


    public static ArrayList<Label> getLabelList() {
        initDB();
        labels = LablesDataBase.getLabelsFromDataBase(database);
        return labels;
    }


    public static void addNewLabel(Label label) {
        initDB();
        LablesDataBase.addLabel(label, database);
        labels = LablesDataBase.getLabelsFromDataBase(database);

    }

    public static void removeLabel(int labelId) {
        if (labels.size() > 0) {
            LablesDataBase.removeLabel(labelId, database);
            labels = LablesDataBase.getLabelsFromDataBase(database);
        }
    }

    public static void editLabel(int position, Label label) {
        if (labels.size() > 0) {
            int labelToEditId = labels.get(position).getLabelId();
            LablesDataBase.editLabel(labelToEditId, label, database);
            labels = LablesDataBase.getLabelsFromDataBase(database);
        }
    }

    private static void initDB() {
        database = new LabelsBDHelper(context).getWritableDatabase();
    }
}
