

/* 
package ai_project;

public class App {
    static {
        System.load("C:\\opencv\\build\\java\\x64\\opencv_java4120.dll");
    }

    public static void main(String[] args) {
        System.out.println("OpenCV Working!");
    }
}

*/




/* 

package ai_project;

import org.opencv.core.*;
import org.opencv.dnn.Dnn;
import org.opencv.dnn.Net;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.*;

public class App {

    static {
        System.load("C:\\opencv\\build\\java\\x64\\opencv_java4120.dll");
    }

    public static void main(String[] args) {

        String modelWeights = "yolo/yolov3.weights";
        String modelConfig = "yolo/yolov3.cfg";
        String imageFile = "signature.jpg"; // put any image in root folder

        Net net = Dnn.readNetFromDarknet(modelConfig, modelWeights);

        Mat image = Imgcodecs.imread(imageFile);
        Mat blob = Dnn.blobFromImage(image, 1/255.0, new Size(416,416),
                new Scalar(0), true, false);

        net.setInput(blob);

        List<Mat> result = new ArrayList<>();
        List<String> outBlobNames = net.getUnconnectedOutLayersNames();
        net.forward(result, outBlobNames);

        System.out.println("Detection Done!");
    }
}
    
*/




/* 

package ai_project;

import org.opencv.core.*;
import org.opencv.dnn.Dnn;
import org.opencv.dnn.Net;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.*;

public class App {

    static {
        System.load("C:\\opencv\\build\\java\\x64\\opencv_java4120.dll");
    }

    public static void main(String[] args) {

        // YOLO paths
        String modelWeights = "yolo/yolov3.weights";
        String modelConfig = "yolo/yolov3.cfg";
        String classesFile = "yolo/coco.names";

        // Image path (IMPORTANT: keep inside app folder)
        String imageFile = "signature.jpg";

        // Load image
        Mat image = Imgcodecs.imread(imageFile);

        if (image.empty()) {
            System.out.println("❌ Image not found! Check path.");
            return;
        }

        // Load YOLO model
        Net net = Dnn.readNetFromDarknet(modelConfig, modelWeights);

        // Convert image to blob
        Mat blob = Dnn.blobFromImage(image, 1 / 255.0, new Size(416, 416),
                new Scalar(0), true, false);

        net.setInput(blob);

        // Get output layer names
        List<String> outNames = net.getUnconnectedOutLayersNames();
        List<Mat> outputs = new ArrayList<>();

        net.forward(outputs, outNames);

        // Load class names
        List<String> classNames = new ArrayList<>();
        try (Scanner sc = new Scanner(new java.io.File(classesFile))) {
            while (sc.hasNextLine()) {
                classNames.add(sc.nextLine());
            }
        } catch (Exception e) {
            System.out.println("❌ Error loading class names");
            return;
        }

        float confThreshold = 0.5f;

        // Process detections
        for (Mat output : outputs) {
            for (int i = 0; i < output.rows(); i++) {

                double[] data = output.get(i, 0);
                float confidence = (float) data[4];

                if (confidence > confThreshold) {

                    int centerX = (int) (data[0] * image.cols());
                    int centerY = (int) (data[1] * image.rows());
                    int width = (int) (data[2] * image.cols());
                    int height = (int) (data[3] * image.rows());

                    int left = centerX - width / 2;
                    int top = centerY - height / 2;

                    // Draw rectangle
                    Imgproc.rectangle(image,
                            new Point(left, top),
                            new Point(left + width, top + height),
                            new Scalar(0, 255, 0), 2);

                    System.out.println("✅ Object detected!");
                }
            }
        }

        // Save output image
        Imgcodecs.imwrite("output.jpg", image);

        System.out.println("🎯 Detection completed. Output saved as output.jpg");
    }
}

*/


//-----------------IMAGE COPY PATH WORKING-------------------
/* 
package ai_project;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.dnn.Dnn;
import org.opencv.dnn.Net;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class App {

    static {
        System.load("C:\\opencv\\build\\java\\x64\\opencv_java4120.dll");
    }

    public static void main(String[] args) throws Exception {

        // ✅ UPDATED PATHS (important)
        String modelWeights = "C:\\Users\\LENOVO\\OneDrive\\Desktop\\AI_Project\\app\\yolo\\yolov3.weights";
        String modelConfig  = "C:\\Users\\LENOVO\\OneDrive\\Desktop\\AI_Project\\app\\yolo\\yolov3.cfg";
        String imageFile    = "C:\\Users\\LENOVO\\OneDrive\\Desktop\\AI_Project\\app\\WIN_20240404_04_58_59_Pro.jpg";

        // Load image
        Mat image = Imgcodecs.imread(imageFile);
        if (image.empty()) {
            System.out.println("❌ Image not found!");
            return;
        }

        // Load YOLO
        Net net = Dnn.readNetFromDarknet(modelConfig, modelWeights);

        // Create blob
        Mat blob = Dnn.blobFromImage(
                image,
                1 / 255.0,
                new Size(416, 416),
                new Scalar(0),
                true,
                false
        );

        net.setInput(blob);

        // Get output layers
        List<String> layerNames = net.getUnconnectedOutLayersNames();
        List<Mat> outputs = new ArrayList<>();
        net.forward(outputs, layerNames);

        // Load class names
        List<String> classNames = Files.readAllLines(Paths.get("C:\\Users\\LENOVO\\OneDrive\\Desktop\\AI_Project\\app\\yolo\\coco.names"));

        // Detection loop (FIXED)
        for (Mat output : outputs) {

            for (int i = 0; i < output.rows(); i++) {

                Mat row = output.row(i);

                float[] data = new float[(int) row.total()];
                row.get(0, 0, data);

                if (data.length < 5) continue;

                float objectness = data[4];

                if (objectness > 0.3) {

                    float maxScore = -1;
                    int classId = -1;

                    for (int j = 5; j < data.length; j++) {
                        if (data[j] > maxScore) {
                            maxScore = data[j];
                            classId = j - 5;
                        }
                    }

                    if (maxScore > 0.3 && classId >= 0) {

                        int centerX = (int) (data[0] * image.cols());
                        int centerY = (int) (data[1] * image.rows());
                        int width = (int) (data[2] * image.cols());
                        int height = (int) (data[3] * image.rows());

                        int left = Math.max(0, centerX - width / 2);
                        int top  = Math.max(0, centerY - height / 2);

                        // Draw box
                        Imgproc.rectangle(
                                image,
                                new Point(left, top),
                                new Point(left + width, top + height),
                                new Scalar(0, 255, 0),
                                2
                        );

                        // Label
                        String label = classNames.get(classId);

                        Imgproc.putText(
                                image,
                                label,
                                new Point(left, top - 5),
                                Imgproc.FONT_HERSHEY_SIMPLEX,
                                0.7,
                                new Scalar(0, 255, 0),
                                2
                        );

                        System.out.println("Detected: " + label);
                    }
                }
            }
        }

        // Save output (goes to root)
        Imgcodecs.imwrite("output.jpg", image);

        System.out.println("✅ Detection completed. Check output.jpg");
    }
}
*/

/*
 package ai_project;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.dnn.Dnn;
import org.opencv.dnn.Net;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

public class App {

    static {
        System.load("C:\\opencv\\build\\java\\x64\\opencv_java4120.dll");
    }

    public static void main(String[] args) throws Exception {

        // ===== YOLO Paths =====
        String modelWeights = "C:\\Users\\LENOVO\\OneDrive\\Desktop\\AI_Project\\app\\yolo\\yolov3.weights";
        String modelConfig  = "C:\\Users\\LENOVO\\OneDrive\\Desktop\\AI_Project\\app\\yolo\\yolov3.cfg";
        String cocoNames    = "C:\\Users\\LENOVO\\OneDrive\\Desktop\\AI_Project\\app\\yolo\\coco.names";

        // Load class names
        List<String> classNames = Files.readAllLines(Paths.get(cocoNames));

        // Load YOLO model
        Net net = Dnn.readNetFromDarknet(modelConfig, modelWeights);

        // ===== OPEN CAMERA =====
        VideoCapture cap = new VideoCapture(0); // 0 = default webcam

        if (!cap.isOpened()) {
            System.out.println(" Camera not found!");
            return;
        }

        Mat frame = new Mat();

        while (true) {

            cap.read(frame);

            if (frame.empty()) break;

            // Create blob
            Mat blob = Dnn.blobFromImage(frame, 1/255.0, new Size(416,416), new Scalar(0), true, false);
            net.setInput(blob);

            // Forward pass
            List<Mat> outputs = new ArrayList<>();
            net.forward(outputs, net.getUnconnectedOutLayersNames());

            int width = frame.cols();
            int height = frame.rows();

            // ===== DETECTION LOOP =====
            for (Mat output : outputs) {
                for (int i = 0; i < output.rows(); i++) {

                    float[] data = new float[(int) output.cols()];
                    output.get(i, 0, data);

                    float confidence = data[4];
                    if (confidence < 0.5) continue;

                    int classId = -1;
                    float maxScore = -1;

                    for (int j = 5; j < data.length; j++) {
                        if (data[j] > maxScore) {
                            maxScore = data[j];
                            classId = j - 5;
                        }
                    }

                    if (maxScore > 0.5) {

                        int centerX = (int)(data[0] * width);
                        int centerY = (int)(data[1] * height);
                        int w = (int)(data[2] * width);
                        int h = (int)(data[3] * height);

                        int x = centerX - w / 2;
                        int y = centerY - h / 2;

                        // Draw rectangle
                        Imgproc.rectangle(frame, new Point(x, y), new Point(x+w, y+h), new Scalar(0,255,0), 2);

                        String label = classNames.get(classId);

                        // Put label
                        Imgproc.putText(frame, label, new Point(x, y-5),
                                Imgproc.FONT_HERSHEY_SIMPLEX, 0.6, new Scalar(0,255,0), 2);

                        System.out.println("Detected: " + label);
                    }
                }
            }

            // Show window
            HighGui.imshow("YOLO Live Detection", frame);

            // Exit on 'q'
            if (HighGui.waitKey(1) == 27) {
                break;
            }
        }

        cap.release();
        HighGui.destroyAllWindows();
    }
} */



   package ai_project;

import org.opencv.core.*;
import org.opencv.dnn.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.highgui.HighGui;
import org.opencv.videoio.VideoCapture;

import java.nio.file.*;
import java.util.*;

public class App {

    static {
        System.load("C:\\opencv\\build\\java\\x64\\opencv_java4120.dll");
    }

    public static void main(String[] args) throws Exception {

        // ===== YOUR EXACT PATHS =====
        String modelWeights = "C:\\Users\\LENOVO\\OneDrive\\Desktop\\AI_Project\\app\\yolo\\yolov3.weights";
        String modelConfig  = "C:\\Users\\LENOVO\\OneDrive\\Desktop\\AI_Project\\app\\yolo\\yolov3.cfg";
        String cocoNames    = "C:\\Users\\LENOVO\\OneDrive\\Desktop\\AI_Project\\app\\yolo\\coco.names";

        // ===== LOAD CLASS NAMES =====
        List<String> classNames = Files.readAllLines(Paths.get(cocoNames));

        // ===== LOAD YOLO MODEL =====
        Net net = Dnn.readNetFromDarknet(modelConfig, modelWeights);

        // ===== OPEN CAMERA =====
        VideoCapture cap = new VideoCapture(0);

        if (!cap.isOpened()) {
            System.out.println("❌ Camera not found!");
            return;
        }

        Mat frame = new Mat();

        // ===== LIVE LOOP =====
        while (true) {

            cap.read(frame);
            if (frame.empty()) break;

            // ===== CREATE BLOB =====
            Mat blob = Dnn.blobFromImage(
                    frame,
                    1 / 255.0,
                    new Size(416, 416),
                    new Scalar(0),
                    true,
                    false
            );

            net.setInput(blob);

            // ===== FORWARD PASS =====
            List<Mat> outputs = new ArrayList<>();
            net.forward(outputs, net.getUnconnectedOutLayersNames());

            // ===== DETECTION =====
            for (Mat output : outputs) {

                for (int i = 0; i < output.rows(); i++) {

                    float[] data = new float[(int) output.cols()];
                    output.get(i, 0, data);

                    // Find best class
                    float maxScore = -1;
                    int classId = -1;

                    for (int j = 5; j < data.length; j++) {
                        if (data[j] > maxScore) {
                            maxScore = data[j];
                            classId = j - 5;
                        }
                    }

                    // Confidence filter
                    if (maxScore > 0.7) {

                        int centerX = (int) (data[0] * frame.cols());
                        int centerY = (int) (data[1] * frame.rows());
                        int width = (int) (data[2] * frame.cols());
                        int height = (int) (data[3] * frame.rows());

                        int left = centerX - width / 2;
                        int top  = centerY - height / 2;

                        // Draw box
                        Imgproc.rectangle(
                                frame,
                                new Point(left, top),
                                new Point(left + width, top + height),
                                new Scalar(0, 255, 0),
                                2
                        );

                        // Label
                        String label = classNames.get(classId);

                        Imgproc.putText(
                                frame,
                                label,
                                new Point(left, top - 5),
                                Imgproc.FONT_HERSHEY_SIMPLEX,
                                0.6,
                                new Scalar(0, 255, 0),
                                2
                        );

                        System.out.println("Detected: " + label);
                    }
                }
            }

            // ===== SHOW WINDOW =====
            HighGui.imshow("Live Detection", frame);

            // ===== EXIT (ESC) =====
            if (HighGui.waitKey(1) == 27) {
                break;
            }
        }

        // ===== CLEANUP =====
        cap.release();
        HighGui.destroyAllWindows();
    }
}    