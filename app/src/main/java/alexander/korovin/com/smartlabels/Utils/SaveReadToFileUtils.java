package alexander.korovin.com.smartlabels.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import alexander.korovin.com.smartlabels.Models.Label;
import alexander.korovin.com.smartlabels.Models.LabelList;

public class SaveReadToFileUtils {

    public static ArrayList<Label> readFromFile(String fileName) {
        FileInputStream fileInputStream;
        ObjectInputStream objectInputStream;
        ArrayList<Label> labels = new ArrayList<>();

        try {
            fileInputStream = new FileInputStream(fileName);
            objectInputStream = new ObjectInputStream(fileInputStream);
            labels = (ArrayList<Label>) objectInputStream.readObject();
            LabelList.setLabelList(labels);
            fileInputStream.close();
            objectInputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return labels;
    }

    public static void saveToFile(String fileName) {
        File file;

        try {
            file = new File(fileName);

            FileOutputStream fileOutputStream;
            ObjectOutputStream objectOutputStream;
            if (!file.exists()) {
                file.createNewFile();
            }

            ArrayList<Label> labels = LabelList.getLabelList();
            fileOutputStream = new FileOutputStream(file, false);
            objectOutputStream = new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(labels);
            fileOutputStream.close();
            objectOutputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
