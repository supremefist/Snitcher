package com.s3.snitcher;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.Timer;

import com.s3.snitcher.arduino.ArduinoSnitcher;
import com.s3.snitcher.arduino.ProjectState;
import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

public class Snitcher implements ActionListener {

	VoiceManager voiceManager = VoiceManager.getInstance();
	Voice voice = null;

	static Random random = new Random();
	SnitcherView view = null;
	private boolean running = true;
	private JenkinsMonitor monitor = new JenkinsMonitor();
	private Timer timer = null;
	private ArduinoSnitcher arduino = new ArduinoSnitcher();

	/**
	 * @param args
	 */

	public Snitcher() {
		view = new SnitcherView(this);
		view.createAndShowGUI();

		initializeVoice();

		timer = new Timer(2000, this);
		timer.start();

		arduino.initialize("COM14");

		monitor.addProject(new JenkinsProject("Alpaca", "Alpaca"));
		monitor.addProject(new JenkinsProject("BeltAnalysis", "BeltAnalysis"));
		monitor.addProject(new JenkinsProject("Lynxx", "Lynxx"));
		monitor.addProject(new JenkinsProject("FrothSensor", "FrothSensor"));
	}

	public static void listAllVoices() {
		System.out.println();
		System.out.println("All voices available:");
		VoiceManager voiceManager = VoiceManager.getInstance();
		Voice[] voices = voiceManager.getVoices();
		for (int i = 0; i < voices.length; i++) {
			System.out.println("    " + voices[i].getName() + " ("
					+ voices[i].getDomain() + " domain)");
		}
	}

	public void initializeVoice() {
		// listAllVoices();
		voice = voiceManager.getVoice("kevin16");

		voice.setDurationStretch(1.0f);
		/*
		 * voice.setPitch(100.0f); voice.setPitchRange(11.0f);
		 * voice.setPitchShift(1.0f); voice.setDurationStretch(1.0f);
		 */
		voice.setPitch(100.0f);
		voice.setPitchRange(11.0f);
		voice.setPitchShift(1.0f);
		voice.setDurationStretch(1f);

		// System.out.println()
		voice.allocate();

	}

	/*
	 * public void start() {
	 * 
	 * say("Started");
	 * 
	 * while (running) { System.out.println("Check");
	 * 
	 * 
	 * try { Thread.sleep(2000); } catch (InterruptedException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } } }
	 */

	public void say(String text) {
		System.out.println("Stitcher: " + text);
		voice.speak(text);

	}

	public void destroy() {
		System.out.println("Destroy called!");
		view.dispose();

		running = false;

		voice.deallocate();

		timer.stop();
	}

	String secondsToTimeString(int milliseconds) {

		int minutes = milliseconds / 60000;
		int seconds = (int) (milliseconds / 1000) % 60;
		return minutes + " minutes and " + seconds + " seconds";

	}

	public static void main(String[] args) {

		// Schedule a job for event dispatch thread:
		// creating and showing this application's GUI.
		final Snitcher snitcher = new Snitcher();
		// snitcher.start();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		monitor.updateStatus();

		int offset = 0;
		for (JenkinsProject project : monitor.projects) {

			try {
				System.out.println(project.getStatus());
				if (project.getStatus().equals("SUCCESS")) {
					arduino.setChannelToState(offset,
							ProjectState.BUILD_SUCCEEDED);
				} else {
					arduino.setChannelToState(offset,
							ProjectState.BUILD_FAILED);

				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (project.mustNotify()) {
				String notifyString = project.getNotifyString();
				view.showText(notifyString);
				say(project.getNotifyString());

				project.setNotified(true);
			}

			offset += 1;
		}
	}

}
