package alexander.korovin.com.smartlabels.Utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import alexander.korovin.com.smartlabels.Models.Label;
import alexander.korovin.com.smartlabels.R;

public class ListViewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Label> labels = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private SQLiteDatabase database;

    public ListViewAdapter(Context context, SQLiteDatabase database) {
        this.context = context;
        this.database = database;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        labels = LablesDataBase.getLabelsFromDataBase(database);
    }

    public void addNewLabel(Label label) {
        labels.add(label);
        LablesDataBase.addLabel(label, database);
        notifyDataSetChanged();
    }

    public void removeLabel(int labelId) {
        if (labels.size() > 0) {
            LablesDataBase.removeLabel(labelId, database);
            labels = LablesDataBase.getLabelsFromDataBase(database);
            notifyDataSetChanged();
        }
    }

    public void editLabel(int labelToEditId, Label label) {
        if (labels.size() > 0) {
            LablesDataBase.editLabel(labelToEditId, label, database);
            labels = LablesDataBase.getLabelsFromDataBase(database);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return labels.size();
    }

    @Override
    public Label getItem(int position) {
        return labels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.listview_item_layout, parent, false);
        }
        if (labels.get(position).isChecked()) {
            convertView.setBackgroundColor(context.getResources().getColor(R.color.checkedElementListView));
        } else {
            convertView.setBackgroundColor(Color.TRANSPARENT);
        }
        String headerText = labels.get(position).getLabelHeader();
        TextView textView = convertView.findViewById(R.id.listview_item_textview);
        textView.setText(headerText);
        return convertView;
    }

}
