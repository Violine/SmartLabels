package alexander.korovin.com.smartlabels.Models;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;

import java.util.ArrayList;

import alexander.korovin.com.smartlabels.Utils.LablesDataBase;

public class LabelListFromDB {
    private static ArrayList<Label> labels;
    private Context context;


    public static ArrayList<Label> getLabelListFromDB(SQLiteDatabase database) {
        labels = LablesDataBase.getLabelsFromDataBase(database);
    }


    public static void addNewLabel(Label label) {
        labels.add(label);
        LablesDataBase.addLabel(label, database);
    }

    public static void removeLabel(int labelId) {
        if (labels.size() > 0) {
            LablesDataBase.removeLabel(labelId, database);
            labels = LablesDataBase.getLabelsFromDataBase(database);
        }
    }

    public static void editLabel(int labelToEditId, Label label) {
        if (labels.size() > 0) {
            LablesDataBase.editLabel(labelToEditId, label, database);
            labels = LablesDataBase.getLabelsFromDataBase(database);
        }
    }
}
