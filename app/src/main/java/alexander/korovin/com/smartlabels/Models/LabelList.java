package alexander.korovin.com.smartlabels.Models;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

public class LabelList implements Serializable {
    private static ArrayList<Label> labelList = new ArrayList<>();
    private static int labelId;

    static {
        for (int i = 0; i < 15; i++) {
            labelList.add(new Label("Тестовая заметка " + i, "id" + labelId, labelId));
            labelId++;
        }
    }

    public static ArrayList<Label> getLabelList() {
        return labelList;
    }

    public static void addLabelToList(String labelHeaderText, String labelDescriptionText) {
        labelId++;
        labelList.add(new Label(labelHeaderText, labelDescriptionText, labelId));
    }

    public static void editLabelToPosition(int labelId, String labelHeaderText, String labelDescriptionText) {
        int position = getPositionFromId(labelId);
        if (position == 666) {
            Log.e("getPositionFromId", "Error");
        } else {
            labelList.set(position, new Label(labelHeaderText, labelDescriptionText, labelId));
        }
    }

    public static void removeLabelToPosition(int positionInListView) {
        if (labelList.size() > 0) {
            int position = getPositionFromId(positionInListView);
            labelList.remove(position);
        }
    }

    private static int getPositionFromId(int labelId) {
        int thisLabelId = 666;
        if (labelList.size() > 0) {
            for (int i = 0; i < labelList.size(); i++) {
                if (labelList.get(i).getLabelId() == labelId) {
                    thisLabelId = i;
                    break;
                } else thisLabelId = 666;
            }
        }
        return thisLabelId;
    }

    public static void setLabelList(ArrayList<Label> newLabelList) {
        labelList = newLabelList;
    }
}
