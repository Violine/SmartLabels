package alexander.korovin.com.smartlabels;

public class Label {
    private String labelHeader;
    private String labelDescription;

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

    public void setLabelDescription(String labelDescription) {
        this.labelDescription = labelDescription;
    }
}
