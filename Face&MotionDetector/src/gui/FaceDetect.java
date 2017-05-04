package gui;

import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;

import com.googlecode.javacv.CanvasFrame;
import com.googlecode.javacv.FrameGrabber;
import com.googlecode.javacv.OpenCVFrameGrabber;
import com.googlecode.javacv.cpp.opencv_core;
import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_objdetect.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;
import com.googlecode.javacv.cpp.opencv_objdetect;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author ARA
 */
class FaceDetect extends Thread {

    JFrame jFrame;
    boolean logic = true;
    public JLabel label1;

    //JLabel label1;
    public FaceDetect() {
        start();
    }

    ///////////
    //create file to save images of face detection
    private void doF() {
        File mf = new File("FaceDetection");
        if (!mf.isDirectory()) {
            mf.mkdir();
        }
    }

    public void run() {


        doF();
        //jButton1.setEnabled(false);

        jFrame = new JFrame("FaceAndMotionFrame");
        jFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        jFrame.setVisible(true);
        jFrame.setSize(600, 500);


        JPanel jPanel = new JPanel();
        jFrame.add(jPanel);


        JButton button = new JButton("Stop Detection");
        button.setBackground(new java.awt.Color(0, 0, 0));
        button.setFont(new java.awt.Font("Times New Roman", 3, 24)); // NOI18N
        button.setForeground(new java.awt.Color(255, 255, 255));
        jPanel.add(button);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jFrame.dispose();
                logic = false;


            }
        });

        label1 = new JLabel();
        jPanel.add(label1);
        
        
        /////////
        //open webcam by grabber

        FrameGrabber grabber = null;
        try {
            grabber = new OpenCVFrameGrabber(0);
            grabber.start();
        } catch (Exception ex) {
            System.out.println("Camera can't start");
        }
        
        
        
        /////////////
        //if webcam becomes on....
        while (logic) {
            opencv_core.IplImage img = null;
            try {
                img = grabber.grab();


                
                int newWidth = 320;
                int newHeight = 240;
                opencv_core.IplImage small = opencv_core.IplImage.create(newWidth, newHeight, img.depth(), img.nChannels());
                cvResize(img, small);


                cvSmooth(small, small, CV_MEDIAN, 13);


                opencv_core.IplImage gray = opencv_core.IplImage.create(small.width(), small.height(), IPL_DEPTH_8U, 1);
                cvCvtColor(small, gray, CV_BGR2GRAY);
                cvEqualizeHist(gray, gray);
                
                ///////////////
                //Opencv file
                String face_classifier = "haarcascade_frontalface_alt.xml";
                opencv_objdetect.CvHaarClassifierCascade face_cascade = new opencv_objdetect.CvHaarClassifierCascade(cvLoad(face_classifier));
                opencv_core.CvMemStorage storage = opencv_core.CvMemStorage.create();
                opencv_core.CvSeq faces = cvHaarDetectObjects(gray, face_cascade, storage, 1.1, 1, 0);
                /////////////
                for (int i = 0; i < faces.total(); i++) {
                    opencv_core.CvRect r = new opencv_core.CvRect(cvGetSeqElem(faces, i));
                    cvRectangle(img, cvPoint(r.x() * 2, r.y() * 2), cvPoint((r.x() + r.width()) * 2, (r.y() + r.height()) * 2), opencv_core.CvScalar.RED, 2, CV_AA, 0);
                    // int ss = (r.x() + r.width()) * (r.y() + r.height());
                    int a = r.x();
                    int b = r.y();
                    int c = r.width();
                    int d = r.height();
                    System.out.println(a + "    " + b + "    " + c + "      " + d);

                    
                    ////////
                    //condition for minimize false detection
                    if (c > 100) {
                        long ct = System.currentTimeMillis();
                        String aa = "FaceDetection/save" + ct + ".jpg";
                        cvSaveImage(aa, img);
                    }
                }


                BufferedImage bi = img.getBufferedImage();
                ImageIcon ii = new ImageIcon(bi);
                label1.setIcon(ii);

                label1.validate();

            } catch (Exception ex) {
                System.out.println("Can't capture");
                break;
            }
        }


        try {
            grabber.stop();
            grabber.release();

        } catch (Exception ex) {
            System.out.println("Error");
        }


    }
}
