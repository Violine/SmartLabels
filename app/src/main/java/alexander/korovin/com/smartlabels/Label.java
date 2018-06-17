package alexander.korovin.com.smartlabels;

public class Label {
    private int labelId;
    private String labelHeader;
    private String labelDescription;

    public Label(String labelHeader, String labelDescription, int labelId) {
        this.labelHeader = labelHeader;
        this.labelDescription = labelDescription;
        this.labelId = labelId;
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

    public void setLabelDescription(String labelDescription) {
        this.labelDescription = labelDescription;
    }

    public int getLabelId() {
        return this.labelId;
    }
}
