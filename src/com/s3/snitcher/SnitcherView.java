package com.s3.snitcher;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JTextArea;

public class SnitcherView extends JFrame implements KeyListener, WindowListener {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JTextArea displayArea;
    Snitcher control = null;
    public boolean ready = false;

    public SnitcherView(Snitcher newControl) {
        super("Snitcher");
        control = newControl;

    }
    
    public void createAndShowGUI() {

        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE );
        setSize(400, 400);
        
        addComponents();

        setVisible(true);
        
        ready = true;
    }



    
    public void showText(String text) {
        displayArea.setText(text);
    }

    private void addComponents() {
        displayArea = new JTextArea();
        displayArea.setText("Snitcher - Build Monitor");
        displayArea.setEditable(false);
        displayArea.addKeyListener(this);
        this.addWindowListener(this);
        
        getContentPane().add(displayArea, BorderLayout.CENTER);
    }
    

    @Override
    public void keyPressed(KeyEvent keyEvent) {
     
    	/*
        if (keyEvent.getKeyCode() == 32) {
            if (control.scrumming) { 
                control.nextTriggered();
            }
            else {
                control.startTriggered();
            }
        }
        else if (keyEvent.getKeyCode() == 27) {
            control.destroy();
        }
        */
    }

    @Override
    public void keyReleased(KeyEvent arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void keyTyped(KeyEvent arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void windowActivated(WindowEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
        control.destroy();
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void windowIconified(WindowEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void windowOpened(WindowEvent e) {
        // TODO Auto-generated method stub
        
    }

}
