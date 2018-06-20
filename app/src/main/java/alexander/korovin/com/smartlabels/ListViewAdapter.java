package alexander.korovin.com.smartlabels;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {
    Context context;
    ArrayList<Label> labels;
    LayoutInflater layoutInflater;
    private SparseBooleanArray selectedItemsIds;

    public ListViewAdapter(Context context, ArrayList<Label> labels) {
        this.context = context;
        this.labels = labels;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        selectedItemsIds = new SparseBooleanArray();
    }

    @Override
    public int getCount() {
        return labels.size();
    }

    @Override
    public Object getItem(int position) {
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
        String headerText = labels.get(position).getLabelHeader();
        TextView textView = convertView.findViewById(R.id.listview_item_textview);
        textView.setText(headerText);
        return convertView;
    }

}
