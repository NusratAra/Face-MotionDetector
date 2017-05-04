
package gui;

/**
 *
 * @author ARA
 */


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;


public class MotionDetector1 extends JFrame 
{
  // GUI components
  private MotionPanel motionPanel;


  public MotionDetector1()
  {
    super("Motion Detector1");

    Container c = getContentPane();
    c.setLayout( new BorderLayout() );   

    motionPanel = new MotionPanel(); // the sequence of pictures appear here
    c.add( motionPanel, BorderLayout.CENTER);

    addWindowListener( new WindowAdapter() {
      public void windowClosing(WindowEvent e)
      { motionPanel.closeDown();    // stop snapping pics
       // System.exit(0);
      }
    });

    setResizable(false);
    pack();  
    setLocationRelativeTo(null);
    setVisible(true);
  } // end of MotionDetector()


  // -------------------------------------------------------


} // end of MotionDetector class
