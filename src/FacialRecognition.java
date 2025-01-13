import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import org.opencv.objdetect.CascadeClassifier;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class FacialRecognition extends JFrame {

    private GUI gui;
    private UserDAO userDAO;

    static {
        String relativePath = "libs/opencv_java4100.dll";
        File file = new File(relativePath);
        String absolutePath = file.getAbsolutePath();
        System.load(absolutePath);

        // Load the native library
        System.out.println("OpenCV Version: " + Core.VERSION);
        System.out.println("load success");
    }

    boolean doRegisterFace;

    boolean doUserExists;

    private volatile boolean cameraRunning = true;

    private JLabel cameraScreen;
    private JButton btnCapture;

    private VideoCapture capture;
    private Mat image;

    private CascadeClassifier faceDetector;

    private volatile boolean clicked = false;

    private Map<String, Mat> knownFaces;
    private Map<String, Mat> knownFaceEmbeddings = new HashMap<>();

    public FacialRecognition(GUI gui, UserDAO userDAO, boolean doRegisterFace) throws SQLException {
        setLayout(null);

        cameraScreen = new JLabel();
        cameraScreen.setBounds(0, 0, 640, 480);
        add(cameraScreen);

        btnCapture = new JButton("Capture");
        btnCapture.setBounds(280, 480, 80, 40);
        add(btnCapture);
        btnCapture.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clicked = true;
            }
        });

        setSize(new Dimension(640, 560));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this . gui = gui;
        this . userDAO = userDAO;
        this . doRegisterFace = doRegisterFace;

        knownFaces = new HashMap<>();
        loadKnownFaces();
    }

    private void loadKnownFaces() throws SQLException {
        // Get images and ids from the database
        List<FaceData> faceDataList = userDAO.getImages();  // Assuming you have the userDAO object to fetch images from the database

        if (faceDataList != null) {
            for (FaceData faceData : faceDataList) {
                int id = faceData.getEmpId();
                Blob imageBlob = faceData.getFaceImage();

                // Convert Blob to Mat
                byte[] imageBytes = imageBlob.getBytes(1, (int) imageBlob.length());
                Mat face = Imgcodecs.imdecode(new MatOfByte(imageBytes), Imgcodecs.IMREAD_COLOR);

                if (!face.empty()) {
                    // Resize the face image to the desired size
                    Mat resizedFace = new Mat();
                    Imgproc.resize(face, resizedFace, new org.opencv.core.Size(100, 100));

                    // Use the id as part of the name
                    String name = "person_" + id;

                    // Store the resized face in the knownFaces map
                    knownFaces.put(name, resizedFace);  // knownFaces is a map of name to Mat
                }
            }
        }
    }


    private String recognizeFace(Mat faceRegion) {
        // Resize the captured face region to a fixed size (e.g., 100x100)
        int width = 100;  // Example size, adjust as needed
        int height = 100;
        Mat resizedFace = new Mat();
        Imgproc.resize(faceRegion, resizedFace, new org.opencv.core.Size(width, height));

        double minDistance = Double.MAX_VALUE;
        String closestName = "Unknown";

        // Resize all known faces once when loading them and compare with resized captured face
        for (Map.Entry<String, Mat> entry : knownFaces.entrySet()) {
            String name = entry.getKey();
            Mat knownFace = entry.getValue();

            // Calculate similarity (using norm)
            double distance = Core.norm(resizedFace, knownFace, Core.NORM_L2);
            // System.out.println("Distance: " + distance + ", " + minDistance + " Name: " + name);
            if (distance < minDistance) {
                minDistance = distance;
                closestName = name;
            }
        }

        if (minDistance > 7000) {
            return "Unknown";
        }
        return closestName;
    }

    public void startCamera() throws SQLException {
        capture = new VideoCapture(0);
        if (!capture.isOpened()) {
            JOptionPane.showMessageDialog(null, "Cannot open the camera.");
            return;
        }

        faceDetector = new CascadeClassifier("ressources/haarcascade_frontalface_default.xml");
        if (faceDetector.empty()) {
            JOptionPane.showMessageDialog(null, "Failed to load face detection model.");
            return;
        }

        image = new Mat();
        byte[] imageData;

        File directory = new File("images");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        ImageIcon icon;
        while (cameraRunning) {
            if (capture.read(image)) {
                // Detect faces
                Mat grayscaleImage = new Mat();
                Imgproc.cvtColor(image, grayscaleImage, Imgproc.COLOR_BGR2GRAY);
                Imgproc.equalizeHist(grayscaleImage, grayscaleImage);

                MatOfRect faces = new MatOfRect();
                faceDetector.detectMultiScale(grayscaleImage, faces);

                Rect[] facesArray = faces.toArray();

                // Draw rectangles around detected faces
                for (Rect face : facesArray) {
                    Imgproc.rectangle(image, face.tl(), face.br(), new Scalar(0, 255, 0), 2);
                    Mat faceRegion = new Mat(image, face);
                    String name = recognizeFace(faceRegion);
                    Imgproc.putText(image, name, new org.opencv.core.Point(face.x, face.y - 10),
                            Imgproc.FONT_HERSHEY_SIMPLEX, 0.8, new Scalar(0, 255, 0), 2);
                }

                final MatOfByte buf = new MatOfByte();
                Imgcodecs.imencode(".jpg", image, buf);

                imageData = buf.toArray();

                icon = new ImageIcon(imageData);
                ImageIcon finalIcon = icon;
                SwingUtilities.invokeLater(() -> cameraScreen.setIcon(finalIcon));
                if (clicked) {
                    System.out.println("Capture");
                    String user_id;
                    int i = 0;
                    for (Rect face : facesArray) {
                        if (face.width > 100 && face.height > 100) {
                            Imgproc.rectangle(image, face.tl(), face.br(), new Scalar(0, 255, 0), 2);
                            Mat faceRegion = new Mat(image, face);

                            String previewName = recognizeFace(faceRegion);

                            Image faceImage = matToBufferedImage(faceRegion);

                            if (doRegisterFace) {
                                user_id = openNamingWindow(faceImage, i, previewName);

                                doUserExists = false;
                                if (user_id != null && !user_id.isEmpty()) {
                                    doUserExists = userDAO.searchUser(Integer.valueOf(user_id));
                                    if (doUserExists) {
                                        Blob faceBlob = matToBlob(faceRegion);
                                        userDAO.saveImage(faceBlob, Integer.valueOf(user_id));
                                    }
                                    else {
                                        System.out.println("User " + user_id + " does not exist.");
                                    }
                                }
                            }
                            else {
                                int extractedId = Integer.parseInt(previewName.replace("person_", ""));
                                gui.signInFromFaceId(extractedId);
                            }
                            i++;
                        }
                    }
                    clicked = false;
                }
            }
        }
    }

    public void stopCamera() {
        cameraRunning = false; // Stop the loop
        if (capture != null && capture.isOpened()) {
            capture.release();  // Release the camera
        }
    }

    public Blob matToBlob(Mat mat) {
        // Convert Mat to MatOfByte
        MatOfByte matOfByte = new MatOfByte();
        Imgcodecs.imencode(".jpg", mat, matOfByte); // Encoding Mat to MatOfByte (not ByteArrayOutputStream)

        byte[] imageBytes = matOfByte.toArray(); // Extract byte array from MatOfByte

        // Convert byte array to Blob
        try {
            return new javax.sql.rowset.serial.SerialBlob(imageBytes); // This is JDBC Blob
        } catch (SQLException e) {
            System.err.println("Error creating Blob from image data: " + e.getMessage());
            return null; // Handle error appropriately
        }
    }

    private String openNamingWindow(Image faceImage, int index, String previewName) {
        final String[] result = {null};

        // Create a dialog
        JDialog dialog = new JDialog((Frame) null, "Name Face " + index, true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 300);
        dialog.setResizable(false);

        // Add image preview
        JLabel imageLabel = new JLabel(new ImageIcon(faceImage));
        dialog.add(imageLabel, BorderLayout.CENTER);

        JLabel previewNameLabel = new JLabel(previewName);
        previewNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        dialog.add(previewNameLabel, BorderLayout.NORTH);

        // Add input field for name
        JPanel inputPanel = new JPanel(new FlowLayout());
        JTextField nameField = new JTextField(20);
        JButton saveButton = new JButton("Save");
        inputPanel.add(new JLabel("Name: "));
        inputPanel.add(nameField);
        inputPanel.add(saveButton);

        dialog.add(inputPanel, BorderLayout.SOUTH);

        // Add action listener to the save button
        saveButton.addActionListener(e -> {
            result[0] = nameField.getText(); // Get the name from the text field
            dialog.dispose(); // Close the dialog
        });

        dialog.setVisible(true); // Show the dialog
        return result[0]; // Return the entered name
    }

    private Image matToBufferedImage(Mat mat) {
        MatOfByte matOfByte = new MatOfByte();
        Imgcodecs.imencode(".jpg", mat, matOfByte);
        byte[] byteArray = matOfByte.toArray();
        BufferedImage bufferedImage = null;

        try {
            InputStream in = new ByteArrayInputStream(byteArray);
            bufferedImage = ImageIO.read(in);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bufferedImage;
    }

    public void StartFacialRecognition() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(33); // ~30 FPS
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                // Start the camera feed
                try {
                    startCamera();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

    }
}
