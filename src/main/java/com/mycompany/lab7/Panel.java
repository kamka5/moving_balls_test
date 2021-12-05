package com.mycompany.lab7;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import static java.lang.Math.atan;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Panel extends JPanel {
    
    private ArrayList<Kula> listaKul;
    private int size = 24;
    private Timer timer;
    private final int DELAY = 8;
    private int ball_limit = 10;

    public Panel() {          
        listaKul = new ArrayList<>();
        setBackground(Color.BLACK);
        addMouseListener(new Event());
        addMouseWheelListener(new Event());
        timer = new Timer(DELAY, new Event());
        timer.start();     
    }      
    
    @Override     
    protected void paintComponent(Graphics g) 
    {
        super.paintComponent(g);
        
        for (Kula k : listaKul)
        {
            g.setColor(k.color);
            g.fillOval(k.x, k.y, k.size, k.size);
        }        
        
        g.setColor(Color.RED);
        g.drawString(Integer.toString(listaKul.size()),15,25);     
    }
    
    private class Event implements MouseListener, ActionListener, MouseWheelListener
    {   
        @Override         
        public void mouseClicked(MouseEvent e)
        {
        }
        
        @Override         
        public void mouseWheelMoved(MouseWheelEvent e)
        {
            size += e.getWheelRotation();
        }
        
        @Override
        public void mousePressed(MouseEvent e)
        {
            if(listaKul.size() < ball_limit)
            {
                listaKul.add(new Kula(e.getX(), e.getY(), size));
                repaint();  
            }
            else
            {
                if(listaKul.size() < ball_limit*1.3)
                {
                     listaKul.add(new Kula(e.getX(), e.getY(), size));
                     repaint(); 
                     if(listaKul.size() % 2 == 0) ball_limit++;
                     listaKul.get(ball_limit/2).size = 15;
                }
                else
                {
                    listaKul.add(new Kula(e.getX(), e.getY(), size));
                    repaint();  
                    ball_limit++;
                    listaKul.get(ball_limit/2).size = 5;
                }
            }
        }
        
        @Override
        public void mouseReleased(MouseEvent e)
        {
        }
        
        @Override
        public void mouseEntered(MouseEvent e) {
            timer.start();
        }
        
        @Override
        public void mouseExited(MouseEvent e) {
            timer.stop();
        }
        
        @Override 
        public void actionPerformed(ActionEvent e) 
        { 
            for (Kula k : listaKul)
            {  
                k.update();   
            }  
            
            repaint(); 
        }     
    }  
    
    private class Kula
    {  
        public int x, y, size;
        public double xspeed, yspeed;
         
        public Color color;  
        private final int MAX_SPEED = 8; 
        public Kula(int x, int y, int size)
        {
            this.x = x;
            this.y = y; 
            this.size = size; 
            color = new Color((float) Math.random(), (float) Math.random(), (float) Math.random());   
        
            setXspeed((int) (Math.random() * MAX_SPEED * 2 - MAX_SPEED));    
            setYspeed((int) (Math.random() * MAX_SPEED * 2 - MAX_SPEED));    
        }      
        public void setXspeed(int Xspeed) {
            while(Xspeed == 0)
            {
                Xspeed = (int) (Math.random() * MAX_SPEED * 2 - MAX_SPEED);
            }
            this.xspeed = Xspeed;
        }

        public void setYspeed(int Yspeed) {
            while(Yspeed == 0)
            {
                Yspeed = (int) (Math.random() * MAX_SPEED * 2 - MAX_SPEED);
            }
            this.yspeed = Yspeed;  
        }
        
        public void update() 
        {   
            if(listaKul.size() > 50) timer.setDelay(17);
            
            x += xspeed;      
            y += yspeed;
                        
            if (x <= 0 || x >= getWidth() - this.size)
            {     
                xspeed = -xspeed;       
            }
            
            if (y <= 0 || y >= getHeight() - this.size)
            {   
                yspeed = -yspeed;      
            }   
            
            for (Kula kula : listaKul) {
                if(!kula.equals(this))
                {
                    double xDif = kula.x - this.x;
                    double yDif = kula.y - this.y;
                    double sizeSum = kula.size/2.0 + this.size/2.0;
                    double distanceSquared = xDif * xDif + yDif * yDif;
                    boolean collision = distanceSquared <= sizeSum*sizeSum;
                    
                    if(collision)
                    {
                        double tg_ball = kula.yspeed/kula.xspeed;
                        double tg_this = this.yspeed/this.xspeed;
                        double rad_ball = atan(tg_ball);
                        double rad_this = atan(tg_this);
                        double v_ball = kula.xspeed/Math.cos(rad_ball);
                        double v_this = this.xspeed/Math.cos(rad_this);
                        double phase = Math.atan2(Math.abs(this.x-kula.x),Math.abs(this.y-kula.y));
                        double weight_ball = 4*Math.PI*Math.pow(kula.size/2.0, 3)/3.0;
                        double weight_this = 4*Math.PI*Math.pow(this.size/2.0, 3)/3.0;
                        
                        kula.xspeed = ((v_ball*Math.cos(rad_ball-phase)*(weight_ball-weight_this)+2*weight_this*v_this*Math.cos(rad_this-phase))/(weight_ball+weight_this))*Math.cos(phase)+v_ball*Math.sin(rad_ball-phase)*Math.cos(phase+Math.PI/2);
                        kula.yspeed = ((v_ball*Math.cos(rad_ball-phase)*(weight_ball-weight_this)+2*weight_this*v_this*Math.cos(rad_this-phase))/(weight_ball+weight_this))*Math.sin(phase)+v_ball*Math.sin(rad_ball-phase)*Math.sin(phase+Math.PI/2);
                        this.xspeed = ((v_this*Math.cos(rad_this-phase)*(weight_this-weight_ball)+2*weight_ball*v_ball*Math.cos(rad_ball-phase))/(weight_ball+weight_this))*Math.cos(phase)+v_this*Math.sin(rad_this-phase)*Math.cos(phase+Math.PI/2);
                        this.yspeed = ((v_this*Math.cos(rad_this-phase)*(weight_this-weight_ball)+2*weight_ball*v_ball*Math.cos(rad_ball-phase))/(weight_ball+weight_this))*Math.sin(phase)+v_this*Math.sin(rad_this-phase)*Math.sin(phase+Math.PI/2);
                    } 
                }
            }
        } 
    }       
}
