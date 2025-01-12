import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class FacialRecognition extends JFrame {

    private JLabel cameraScreen;
    private JButton btnCapture;

    private VideoCapture capture;
    private Mat image;

    private CascadeClassifier faceDetector;

    private volatile boolean clicked = false;

    private Map<String, Mat> knownFaces;
    private Map<String, Mat> knownFaceEmbeddings = new HashMap<>();

    private String extractNameWithoutNumber(String fileName) {
        // Expression régulière pour capturer le nom avant le numéro
        Pattern pattern = Pattern.compile("([a-zA-Z]+)(\\d+)?");
        Matcher matcher = pattern.matcher(fileName);

        if (matcher.find()) {
            // Le groupe 1 est le nom sans le numéro
            return matcher.group(1);
        }
        return "Unknown";  // Retourne "Inconnu" si aucun nom valide n'est trouvé
    }

    public FacialRecognition() {
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
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        knownFaces = new HashMap<>();
        loadKnownFaces();
    }

    private void loadKnownFaces() {
        // Load images of known individuals and compute embeddings (dummy example)
        File[] files = new File("known_faces").listFiles();
        if (files != null) {
            for (File file : files) {
                String name = file.getName().replace(".jpg", "");
                Mat face = Imgcodecs.imread(file.getAbsolutePath());
                knownFaces.put(name, face); // Add to the map (actual embeddings are better)
            }
        }
    }

    private String recognizeFace(Mat faceRegion) {
        // Resize the captured face region to match the size of known faces (e.g., 100x100)
        int width = 100;  // Example size, adjust as needed
        int height = 100;
        Mat resizedFace = new Mat();
        Imgproc.resize(faceRegion, resizedFace, new org.opencv.core.Size(width, height));

        double minDistance = Double.MAX_VALUE;
        String closestName = "Unknown";

        for (Map.Entry<String, Mat> entry : knownFaces.entrySet()) {
            String name = entry.getKey();
            Mat knownFace = entry.getValue();

            // Resize the known face to the same size as the captured face
            Mat resizedKnownFace = new Mat();
            Imgproc.resize(knownFace, resizedKnownFace, new org.opencv.core.Size(width, height));

            // Calculate similarity (using norm)
            double distance = Core.norm(resizedFace, resizedKnownFace, Core.NORM_L2);
            System.out.println("Distance: " + distance + ", " + minDistance + " Name: " + name);
            if (distance < minDistance) {
                minDistance = distance;
                closestName = name;
            }
        }
        if (minDistance > 6000) {
            return "Unknown";
        }
        return closestName;
    }

    public void startCamera() {
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
        while (true) {
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
                    name = extractNameWithoutNumber(name);
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
                    String name;
                    int i = 0;
                    for (Rect face : facesArray) {
                        if (face.width > 100 && face.height > 100) {
                            Imgproc.rectangle(image, face.tl(), face.br(), new Scalar(0, 255, 0), 2);
                            Mat faceRegion = new Mat(image, face);

                            String previewName = recognizeFace(faceRegion);
                            previewName = extractNameWithoutNumber(previewName);

                            Image faceImage = matToBufferedImage(faceRegion);
                            name = openNamingWindow(faceImage, i, previewName);

                            if (name != null || !name.isEmpty()) {
                                Imgcodecs.imwrite("known_faces/" + name + ".jpg", faceRegion );
                            }
                            else {
                                name = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss").format(new Date());
                                Imgcodecs.imwrite("images/" + name + "-" + i + ".jpg", faceRegion);
                            }
                            i++;
                        }
                    }
                    clicked = false;
                }
            }
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

    public static void main(String[] args) {
        String relativePath = "libs/opencv_java4100.dll";
        File file = new File(relativePath);
        String absolutePath = file.getAbsolutePath();

        // Load the native library
        System.load(absolutePath);
        System.out.println("OpenCV Version: " + Core.VERSION);
        System.out.println("load success");

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                FacialRecognition facialRecognition = new FacialRecognition();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(33); // ~30 FPS
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                        facialRecognition.startCamera();
                    }
                }).start();
            }
        });
    }
}
