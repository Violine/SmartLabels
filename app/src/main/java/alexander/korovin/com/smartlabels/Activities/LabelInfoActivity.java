package alexander.korovin.com.smartlabels.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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
    private FloatingActionButton editButton;
    private int position;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_label_info);
        position = getIntent().getIntExtra("LABEL_POSITION", 0);
        initUi();
    }

    private void initUi() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.label_info);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        labelName = findViewById(R.id.activity_info_label_header_textview);
        labelDescription = findViewById(R.id.activity_info_description_edittext);
        editButton = findViewById(R.id.edit_activity_info_button);

        labelName.setText(LabelListFromDB.getLabelList().get(position).getLabelHeader());
        labelDescription.setText(LabelListFromDB.getLabelList().get(position).getLabelDescription());


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share_label, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share_label: {
                shareResult(labelName.getText().toString());
                return true;
            }
            case android.R.id.home:
                Intent intent = new Intent(LabelInfoActivity.this, MainActivity.class);
                startActivity(intent);
                return true;
            default:
                return false;
        }
    }

    private void shareResult(String message) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, message);
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_resut)));
    }
}
