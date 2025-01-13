import java.sql.Blob;

public class FaceData {
    private int empId;
    private Blob faceImage;

    public FaceData(int empId, Blob faceImage) {
        this.empId = empId;
        this.faceImage = faceImage;
    }

    public int getEmpId() {
        return empId;
    }

    public void setEmpId(int empId) {
        this.empId = empId;
    }

    public Blob getFaceImage() {
        return faceImage;
    }

    public void setFaceImage(Blob faceImage) {
        this.faceImage = faceImage;
    }
}
