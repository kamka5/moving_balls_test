package com.mycompany.lab7;  

import java.awt.Dimension; 
import javax.swing.JFrame;  

public class Main {      
    public static void main(String[] args) 
    {          
        JFrame frame = new JFrame("My window!");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new Panel());
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
        frame.setUndecorated(true);
//        frame.setPreferredSize(new Dimension(1000, 750));
//        frame.pack();
        frame.setVisible(true);
    } 
}
        
