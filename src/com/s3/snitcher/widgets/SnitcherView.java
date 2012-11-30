package com.s3.snitcher.widgets;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.Timer;

import com.s3.snitcher.JenkinsProject;
import com.s3.snitcher.Snitcher;

public class SnitcherView extends JFrame implements KeyListener, WindowListener, ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	JLabel displayArea;
	JLabel blameArea;
	JLabel projectArea;
	
	
	Snitcher control = null;
	public boolean ready = false;
	
	private Timer timer = new Timer(1000, this);
	
	
	List<ProjectWidget> projectWidgets = new ArrayList<ProjectWidget>();

	public SnitcherView(Snitcher newControl) {
		super("Snitcher");
		control = newControl;

		timer.start();
	}

	public void createAndShowGUI() {

		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setSize(400, 400);

		addComponents();

		ready = true;
	}

	public void update() {
		for (ProjectWidget widget: projectWidgets) {
			widget.updateState();
		}
	}

	private void addComponents() {
		
		LayoutManager displayLayout = new BorderLayout();
		
		displayArea = new JLabel();
		displayArea.setLayout(displayLayout);
		displayArea.addKeyListener(this);

		projectArea = new JLabel();
		LayoutManager projectLayout = new FlowLayout(FlowLayout.LEADING, 5, 5);
		projectArea.setLayout(projectLayout);
		projectArea.setPreferredSize(new Dimension(100, 100));
		//projectArea.revalidate();
		displayArea.add(projectArea);

		blameArea = new JLabel();
		blameArea.setFont(new Font("Book Antiqua", Font.PLAIN, 30));
		blameArea.setText("Noone to blame... yet!");
		//blameArea.setPreferredSize(new Dimension(100, 60));
		displayArea.add(blameArea, BorderLayout.SOUTH);

		
		
		this.addWindowListener(this);

		getContentPane().add(displayArea, BorderLayout.CENTER);
	}
	
	public void setBlames(List<String> blames) {
		
		String blameString = "Noone to blame... yet!";
		if (blames.size() > 0) {
			blameString = "Blames: " + blames.get(0);
			for (String blame: blames.subList(1, blames.size())) {
				blameString += ", " + blame;
			}
		}
		
		
		
		blameArea.setText(blameString);
	}
	
	public void addProject(JenkinsProject project) {
		ProjectWidget newWidget = new ProjectWidget(project);
		projectWidgets.add(newWidget);
		projectArea.add(newWidget);
		
	}

	@Override
	public void keyPressed(KeyEvent keyEvent) {

		/*
		 * if (keyEvent.getKeyCode() == 32) { if (control.scrumming) {
		 * control.nextTriggered(); } else { control.startTriggered(); } } else
		 * if (keyEvent.getKeyCode() == 27) { control.destroy(); }
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
		
		timer.stop();
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

	@Override
	public void actionPerformed(ActionEvent arg0) {
		update();
	}

}
