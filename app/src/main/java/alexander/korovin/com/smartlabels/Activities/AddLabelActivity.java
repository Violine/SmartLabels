package alexander.korovin.com.smartlabels.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import alexander.korovin.com.smartlabels.Models.Label;
import alexander.korovin.com.smartlabels.Models.LabelList;
import alexander.korovin.com.smartlabels.Models.LabelListFromDB;
import alexander.korovin.com.smartlabels.R;

public class AddLabelActivity extends AppCompatActivity {
    private Button addLabelButton;
    private Button cancelButton;
    private EditText labelHeaderText;
    private EditText labelDescriptionText;
    private String oldHeaderText;
    private String oldDescriptionText;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_label_activty);

        initUI();
        if (getIntent().getStringExtra("LABEL_HEADER") != null) {
            oldHeaderText = getIntent().getStringExtra("LABEL_HEADER");
            oldDescriptionText = getIntent().getStringExtra("LABEL_DESCRIPTION");
            position = getIntent().getIntExtra("POSITION", 0);
            labelDescriptionText.setText(oldDescriptionText);
            labelHeaderText.setText(oldHeaderText);
        }
    }

    private void initUI() {
        labelDescriptionText = findViewById(R.id.add_label_description_edittext);
        labelHeaderText = findViewById(R.id.add_label_header_textview);
        addLabelButton = findViewById(R.id.add_label_button);
        cancelButton = findViewById(R.id.cancel_add_label_button);

        addLabelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getIntent().getStringExtra("EDIT") == null) {
                    if (!labelHeaderText.getText().toString().equals("")) {
                        if (labelDescriptionText.getText().toString().equals("")) {
                            labelDescriptionText.setText("");
                        }
                        LabelListFromDB.addNewLabel(new Label(labelHeaderText.getText().toString(), labelDescriptionText.getText().toString()));
                        finish();
                    } else {
                        Toast.makeText(AddLabelActivity.this, R.string.enter_label_name, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    LabelListFromDB.editLabel(position, new Label(labelHeaderText.getText().toString(), labelDescriptionText.getText().toString()));
                    Intent intent = new Intent(AddLabelActivity.this, LabelInfoActivity.class);
                    intent.putExtra("LABEL_POSITION", position);
                    startActivity(intent);
                }
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
