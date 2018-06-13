package alexander.korovin.com.smartlabels;

import java.util.ArrayList;

public class LabelList {
    private static ArrayList<Label> labelList = new ArrayList<>();

    static {
        labelList.add(new Label("Тестовая заметка 1", "Описание тестовой заметки 1"));
        labelList.add(new Label("Тестовая заметка 2", "Описание тестовой заметки 2"));
        labelList.add(new Label("Тестовая заметка 3", "Описание тестовой заметки 3"));
        labelList.add(new Label("Тестовая заметка 4", "Описание тестовой заметки 4"));
        labelList.add(new Label("Тестовая заметка 5", "Описание тестовой заметки 5"));
        labelList.add(new Label("Тестовая заметка 6", "Описание тестовой заметки 6"));
        labelList.add(new Label("Тестовая заметка 7", "Описание тестовой заметки 7"));
        labelList.add(new Label("Тестовая заметка 8", "Описание тестовой заметки 8"));
    }

    public static ArrayList<Label> getLabelList() {
        return labelList;
    }

    private void addNewLabel(String labelHeader, String labelDescription) {
        labelList.add(new Label(labelHeader, labelDescription));
    }

    private void removeLabel(int position) {
        labelList.remove(position);
    }

    private void editLabel() {
        // пока не придумал что написать
    }
}
