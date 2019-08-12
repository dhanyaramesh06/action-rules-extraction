package kdd;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.awt.Color;

import javax.swing.*;

public class Main {

  public static void main(String[] args) throws FileNotFoundException, IOException {
      
	  UI ui = new UI();
      
      JFrame frame = new JFrame();
      frame.setSize(800, 650);
      frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      frame.setVisible(true);   
     
      frame.setTitle("Action Rules");
      frame.getContentPane().add(ui).setBackground(Color.WHITE);
      
      
  }
  
}