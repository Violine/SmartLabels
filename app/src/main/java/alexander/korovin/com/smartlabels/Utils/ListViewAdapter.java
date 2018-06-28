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
import java.util.List;

import alexander.korovin.com.smartlabels.Models.Label;
import alexander.korovin.com.smartlabels.R;

public class ListViewAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<Label> labels;

    public ListViewAdapter(Context context, ArrayList<Label> labels) {
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.labels = labels;
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

    public void changeLabelList(ArrayList<Label> newLabels) {
            labels = newLabels;
            notifyDataSetChanged();
    }

}
