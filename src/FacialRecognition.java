import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

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

public class FacialRecognition extends JFrame {

    private JLabel cameraScreen;
    private JButton btnCapture;

    private VideoCapture capture;
    private Mat image;

    private CascadeClassifier faceDetector;

    private volatile boolean clicked = false;

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
                    // String name = JOptionPane.showInputDialog("Enter image name:");
                    int i = 0;
                    for (Rect face : facesArray) {
                        if (face.width > 100 && face.height > 100) {
                            Imgproc.rectangle(image, face.tl(), face.br(), new Scalar(0, 255, 0), 2);
                            Mat faceRegion = new Mat(image, face);
                            name = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss").format(new Date());
                            Imgcodecs.imwrite("images/" + name + "-" + i + ".jpg", faceRegion );
                            i++;
                        }
                    }
                    clicked = false;
                }
            }
        }
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
                        facialRecognition.startCamera();
                    }
                }).start();
            }
        });
    }
}
