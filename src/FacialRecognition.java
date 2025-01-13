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

    // References from other classes.
    private GUI gui;
    private UserDAO userDAO;

    // Static block to initialize once OpenCV.
    static {
        // get the relative path of opencv .dll and find it's absolute using the file created using the relative path.
        String relativePath = "libs/opencv_java4100.dll";
        File file = new File(relativePath);
        String absolutePath = file.getAbsolutePath();

        // Load the native library
        System.load(absolutePath);
        System.out.println("OpenCV Version: " + Core.VERSION);
        System.out.println("load success");
    }

    // Variable used to know for what should the camera be used for.
    // Registration or Login.
    boolean doRegisterFace;

    boolean doUserExists;

    // Variable used to stop the camera from running.
    private volatile boolean cameraRunning = true;

    // Different UI elements
    private JLabel cameraScreen;
    private JButton btnCapture;

    private VideoCapture capture;
    private Mat image;

    private CascadeClassifier faceDetector;

    private volatile boolean clicked = false;

    // List composed of the name of the person corresponding to each face.
    private Map<String, Mat> knownFaces;

    // Constructor of the class with three necessary parameters.
    public FacialRecognition(GUI gui, UserDAO userDAO, boolean doRegisterFace) throws SQLException {
        // Determine position of the different UI elements.
        setLayout(null);

        cameraScreen = new JLabel();
        cameraScreen.setBounds(0, 0, 640, 480);
        add(cameraScreen);

        btnCapture = new JButton("Capture");
        btnCapture.setBounds(280, 480, 80, 40);
        add(btnCapture);
        // Change the status of the button to clicked when clicked to enable actions when true.
        btnCapture.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clicked = true;
            }
        });
        // Set different parameters of the window.
        setSize(new Dimension(640, 560));
        setLocationRelativeTo(null);
        // The use of Dispose instead of EXIT enables to continue running the application while closing a particular window.
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this . gui = gui;
        this . userDAO = userDAO;
        this . doRegisterFace = doRegisterFace;

        knownFaces = new HashMap<>();
        loadKnownFaces();
    }

    // Load images of the employees from the database. This is done each time the window is opened.
    private void loadKnownFaces() throws SQLException {
        // Get images and ids from the database using a custom FaceData structure which contain a blob and an int.
        List<FaceData> faceDataList = userDAO.getImages();

        // Check if the list is empty.
        if (faceDataList != null) {
            // If it isn't goes through it to transform the blobs into images.
            for (FaceData faceData : faceDataList) {
                int id = faceData.getEmpId();
                Blob imageBlob = faceData.getFaceImage();

                // Convert Blob to byte array which is then decoded thanks to Imgcodecs and transformed into a
                // Mat array.
                // Imgcodecs.IMREAD_COLOR is used to know how to read the image.
                byte[] imageBytes = imageBlob.getBytes(1, (int) imageBlob.length());
                Mat face = Imgcodecs.imdecode(new MatOfByte(imageBytes), Imgcodecs.IMREAD_COLOR);

                if (!face.empty()) {
                    // Resize the faces to make the comparison between images easier later.
                    Mat resizedFace = new Mat();
                    Imgproc.resize(face, resizedFace, new org.opencv.core.Size(100, 100));

                    // Use the id as part of the name to also use it back later when identifying the images.
                    String name = "employee_" + id;

                    // Store the resized face in the knownFaces map that will be used each frame to identify
                    // if there is any similarity between the faces in it and the camera.
                    knownFaces.put(name, resizedFace);
                }
            }
        }
    }

    // Function to determine the distance between a face and those of the Database.
    private String recognizeFace(Mat faceRegion) {
        // Resize the captured face region to the same size as the one of the DB.
        // It's necessary.
        int width = 100;
        int height = 100;
        Mat resizedFace = new Mat();
        Imgproc.resize(faceRegion, resizedFace, new org.opencv.core.Size(width, height));

        double minDistance = Double.MAX_VALUE;
        String closestName = "Unknown";

        // We will go through every images of known faces to calculate for each the similarity.
        // The closest image will be kept and if it is under a maximum score then it will be identified as the same.
        // And the name of the corresponding known face will be used as we structured the data like this.
        for (Map.Entry<String, Mat> entry : knownFaces.entrySet()) {
            String name = entry.getKey();
            Mat knownFace = entry.getValue();

            // Calculate similarity (using norm), calculate the magnitude of the difference between two matrices.
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

    // Main method.
    // It runs the camera and changes the image each frame.
    // It identify faces on each camera frames and put a green rect on them.
    // Also check if Button is clicked.
    public void startCamera() throws SQLException {
        // Initialize the camera of index 0.
        capture = new VideoCapture(0);
        // Check if camera works.
        if (!capture.isOpened()) {
            JOptionPane.showMessageDialog(null, "Cannot open the camera.");
            return;
        }

        // Thing used to identify faces.
        faceDetector = new CascadeClassifier("ressources/haarcascade_frontalface_default.xml");
        if (faceDetector.empty()) {
            JOptionPane.showMessageDialog(null, "Failed to load face detection model.");
            return;
        }

        image = new Mat();
        byte[] imageData;

        ImageIcon icon;
        while (cameraRunning) {
            // Insert / Read a frame into the image Mat we created each... frame.
            if (capture.read(image)) {
                // Detect faces using a grayscale image.
                Mat grayscaleImage = new Mat();
                Imgproc.cvtColor(image, grayscaleImage, Imgproc.COLOR_BGR2GRAY);
                Imgproc.equalizeHist(grayscaleImage, grayscaleImage);
                MatOfRect faces = new MatOfRect();
                // detectMultiScale is the function from our CascadeClassifier that detects the faces from the grayscaleimage.
                // The output is then put into faces, a matrix of rect. Each rect representing the position of the face.
                faceDetector.detectMultiScale(grayscaleImage, faces);

                // MatOfRect transformed to an array for easier use.
                Rect[] facesArray = faces.toArray();

                // Draw rectangles around detected faces according to each rect detected thanks to "detectMultiScale"
                for (Rect face : facesArray) {
                    // Remove some noises and false positive by setting a minimum size.
                    if (face.width > 100 && face.height > 100) {
                        // For each face we draw a green rectangle at their coordinates.
                        Imgproc.rectangle(image, face.tl(), face.br(), new Scalar(0, 255, 0), 2);
                        // Each face is also sent to our function recognizeFace to determine whose face is it.
                        Mat faceRegion = new Mat(image, face);
                        String name = recognizeFace(faceRegion);
                        // We then get a String that we draw onto our image.
                        Imgproc.putText(image, name, new org.opencv.core.Point(face.x, face.y - 10),
                                Imgproc.FONT_HERSHEY_SIMPLEX, 0.8, new Scalar(0, 255, 0), 2);
                    }
                }

                // Updates our Window with the new image.
                final MatOfByte buf = new MatOfByte();
                Imgcodecs.imencode(".jpg", image, buf);
                imageData = buf.toArray();
                icon = new ImageIcon(imageData);
                ImageIcon finalIcon = icon;
                SwingUtilities.invokeLater(() -> cameraScreen.setIcon(finalIcon));

                if (clicked) {
                    String user_id;
                    int i = 0;
                    // If our Capture button is clicked, check every faces we found.
                    for (Rect face : facesArray) {
                        // Remove the smaller faces to get less noises and false positive.
                        if (face.width > 100 && face.height > 100) {
                            System.out.println("Capture");

                            // Crop the region for each face and create an image of it.
                            Imgproc.rectangle(image, face.tl(), face.br(), new Scalar(0, 255, 0), 2);
                            Mat faceRegion = new Mat(image, face);
                            Image faceImage = matToBufferedImage(faceRegion);

                            String previewName = recognizeFace(faceRegion);

                            // Action depends of if Registering or Login using Face Id.
                            if (doRegisterFace) {
                                // If registering, a window will pop up asking for id of the person.
                                user_id = openNamingWindow(faceImage, i, previewName);

                                doUserExists = false;
                                if (user_id != null && !user_id.isEmpty()) {
                                    doUserExists = userDAO.searchUser(Integer.valueOf(user_id));
                                    if (doUserExists) {
                                        // If user exists, transform the image to a storable blob.
                                        Blob faceBlob = matToBlob(faceRegion);
                                        // Saves the image in the database using the id entered by the user.
                                        userDAO.saveImage(faceBlob, Integer.valueOf(user_id));
                                    }
                                    else {
                                        System.out.println("User " + user_id + " does not exist.");
                                    }
                                }
                            }
                            else {
                                // Otherwise tries to sign-in.
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

    // Method to stop the camera, used when clicking on the close button of the window.
    public void stopCamera() {
        cameraRunning = false; // Stop the loop
        if (capture != null && capture.isOpened()) {
            capture.release();  // Release the camera
        }
    }

    // Transform a matrix to a database storable type of data (a blob).
    public Blob matToBlob(Mat mat) {
        // Convert Mat to a matrix of bytes
        MatOfByte matOfByte = new MatOfByte();
        // Encoded into the proper format.
        Imgcodecs.imencode(".jpg", mat, matOfByte); // Encoding Mat to MatOfByte (not ByteArrayOutputStream)
        // Another time converted but as a simply bytes array.
        byte[] imageBytes = matOfByte.toArray(); // Extract byte array from MatOfByte

        // Convert byte array to Blob using dark magic.
        try {
            return new javax.sql.rowset.serial.SerialBlob(imageBytes); // ???
        } catch (SQLException e) {
            System.err.println("Error creating Blob from image data: " + e.getMessage());
            return null; // Handle error appropriately
        }
    }

    // Function used to open a window showing preview of the captured face and asking for id of the face.
    private String openNamingWindow(Image faceImage, int index, String previewName) {
        final String[] result = {null};

        // Create a dialog box that goes on top of the window.
        JDialog dialog = new JDialog((Frame) null, "Id Face " + index, true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 300);
        dialog.setResizable(false);

        // Add image preview of our captured face.
        JLabel imageLabel = new JLabel(new ImageIcon(faceImage));
        dialog.add(imageLabel, BorderLayout.CENTER);

        // We also got the name preview to know if the algorithm recognized the face.
        JLabel previewNameLabel = new JLabel(previewName);
        previewNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        dialog.add(previewNameLabel, BorderLayout.NORTH);

        // Add input field for id.
        JPanel inputPanel = new JPanel(new FlowLayout());
        JTextField nameField = new JTextField(20);
        JButton saveButton = new JButton("Save");
        inputPanel.add(new JLabel("ID: "));
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

    // Another method to convert a matrix into a showable image.
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

    // Method called to start all of the start camera stuff and to put some multi threading in it.
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
