package com.s3.snitcher.widgets;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JLabel;

import com.s3.snitcher.JenkinsProject;

public class StatusWidget extends JLabel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JenkinsProject project = null;
	private boolean flashOn = true;
	private int usedSize = 30;
	
	public StatusWidget(JenkinsProject newProject) {
		project = newProject;
		
		setPreferredSize(new Dimension(usedSize * 2, usedSize));
		//setOpaque(true);
		//setBackground(Color.red);
		
	}
	
	@Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        Color colorToUse = Color.gray;
        
        if (project.isBuilding()) {
        	if (flashOn) {
        		colorToUse = new Color(128, 128, 200);
        	}
        	else {
        		colorToUse = new Color(64, 64, 128);
        	}
        }
        else if (project.getStatus().equals("SUCCESS")) {
        	colorToUse = new Color(128, 200, 128);
        }
        else if (project.getStatus().equals("ABORTED")) {
        	colorToUse = new Color(128, 128, 128);
        }
        else {
        	colorToUse = new Color(200, 128, 128);
        }
        
        g2d.setColor(colorToUse);

        int offset = usedSize / 10;
        
        g2d.fillRect(offset, offset, usedSize * 2 - offset * 2, usedSize - offset * 2);

    }

	public void tick() {
		flashOn = !flashOn;
		repaint();
	}
}
