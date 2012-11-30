package com.s3.snitcher.widgets;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

import com.s3.snitcher.JenkinsProject;

public class ProjectWidget extends JLabel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JenkinsProject project = null;
	
	private StatusWidget statusWidget = null;
	private JLabel healthWidget = null;
	
	public ProjectWidget(JenkinsProject newProject) {
		project = newProject;
		
		setBorder(BorderFactory.createTitledBorder(newProject.getName()));
		setPreferredSize(new Dimension(160, 60));

		LayoutManager layout = new GridLayout();
		setLayout(layout);
		
		healthWidget = new JLabel("0", JLabel.CENTER);
		add(healthWidget, 0);
		
		statusWidget = new StatusWidget(project);
		add(statusWidget, 1);
		
		setOpaque(true);
		setBackground(new Color(235, 235, 235));
		
		updateState();
	}
	
	public void updateState() {
		tick();
		repaint();
	}
	
	public void tick() {
		statusWidget.tick();
		
		int score = project.getScore();

		String scoreString = String.valueOf(score) + " %";
		healthWidget.setText(scoreString);
		
		int nominalColor = 235;
		int maxColorDeviation = 50;
		int redMagnitude = (int)(((float)(100 - score) / 100.0) * maxColorDeviation);
		setBackground(new Color(nominalColor, nominalColor - redMagnitude, nominalColor - redMagnitude));
	}

}
