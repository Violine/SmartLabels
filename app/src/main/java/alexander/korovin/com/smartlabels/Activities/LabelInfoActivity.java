package alexander.korovin.com.smartlabels.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import alexander.korovin.com.smartlabels.Models.Label;
import alexander.korovin.com.smartlabels.Models.LabelList;
import alexander.korovin.com.smartlabels.Models.LabelListFromDB;
import alexander.korovin.com.smartlabels.R;

public class LabelInfoActivity extends AppCompatActivity {
    private TextView labelName;
    private TextView labelDescription;
    private Button okButton;
    private Button editButton;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_label_info);
        position = getIntent().getIntExtra("LABEL_POSITION", 0);
        initUi();
    }

    private void initUi() {
        labelName = findViewById(R.id.activity_info_label_header_textview);
        labelDescription = findViewById(R.id.activity_info_description_edittext);
        okButton = findViewById(R.id.activity_info_ok_button);
        editButton = findViewById(R.id.edit_activity_info_button);

        labelName.setText(LabelListFromDB.getLabelList().get(position).getLabelHeader());
        labelDescription.setText(LabelListFromDB.getLabelList().get(position).getLabelDescription());

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Label oldLabel = LabelListFromDB.getLabelList().get(position);
                Intent intent = new Intent(LabelInfoActivity.this, AddLabelActivity.class);
                intent.putExtra("LABEL_HEADER", oldLabel.getLabelHeader());
                intent.putExtra("LABEL_DESCRIPTION", oldLabel.getLabelDescription());
                intent.putExtra("EDIT", "EDIT");
                intent.putExtra("POSITION", position);
                startActivity(intent);
            }
        });

    }

}
