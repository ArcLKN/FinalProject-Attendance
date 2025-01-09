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
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

public class FacialRecognition extends JFrame {

    private JLabel cameraScreen;

    private JButton btnCapture;

    private VideoCapture capture;
    private Mat image;

    private boolean clicked = false;

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

            }
        });

        setSize(new Dimension(640, 560));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void startCamera() {
        capture = new VideoCapture();
        image = new Mat();
        byte[] imageData;

        ImageIcon icon;
        while (true) {
            capture.read(image);

            final MatOfByte buf = new MatOfByte();
            Imgcodecs.imencode(".jpg", image, buf);

            imageData = buf.toArray();

            icon = new ImageIcon(imageData);

            if (clicked) {
                String name = JOptionPane.showInputDialog("Enter image name:");
                if (name == null) {
                    name = new SimpleDateFormat("yyyy-mm-dd-hh-mm-ss").format(new Date());
                }
                Imgcodecs.imwrite("images/" + name + ".jpg", image);
            }
        }
    }

    public static void main(String[] args) {
        String relativePath = "libs/opencv_java4100.dll";
        File file = new File(relativePath);
        String absolutePath = file.getAbsolutePath();

        // Load the native library
        System.load(absolutePath);
        System.out.println("load success");

        FacialRecognition facialRecognition = new FacialRecognition();
    }
}
