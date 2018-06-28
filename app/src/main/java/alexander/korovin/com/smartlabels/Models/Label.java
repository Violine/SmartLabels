package alexander.korovin.com.smartlabels.Models;

import java.io.Serializable;

public class Label implements Serializable {
    private int labelId;
    private String labelHeader;
    private String labelDescription;
    private boolean isChecked;

    public Label(String labelHeader, String labelDescription) {
        this.labelHeader = labelHeader;
        this.labelDescription = labelDescription;

    }

    public String getLabelHeader() {
        return labelHeader;
    }

    public String getLabelDescription() {
        return labelDescription;
    }

    public void setLabelHeader(String labelHeader) {

        this.labelHeader = labelHeader;
    }

    public void setLabelId(int labelId) {
        this.labelId = labelId;
    }

    public int getLabelId() {
        return labelId;
    }

    public void setLabelDescription(String labelDescription) {
        this.labelDescription = labelDescription;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public boolean isChecked() {
        return isChecked;
    }

}
