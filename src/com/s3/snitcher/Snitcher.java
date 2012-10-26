package com.s3.snitcher;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Random;

import javax.swing.Timer;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

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
	private Timer timer = new Timer(2000, this);
	private ArduinoSnitcher arduino = new ArduinoSnitcher();
	private String host = null;
	private String comPort = null;

	/**
	 * @param args
	 */

	@SuppressWarnings("unchecked")
	public void readSettingsFromXML() {

		String settingsFilename = "snitcher.xml";

		File xml = new File(settingsFilename);
		if (!xml.exists()) {
			createDefaultXMLFile(settingsFilename);
		}
		
		try {
			SAXReader reader = new SAXReader();
			Document doc;
			doc = reader.read(xml);
			Element root = doc.getRootElement();

			comPort = root.elementText("com");
			host = root.elementText("host");

			System.out.println("Monitoring: " + host);
			System.out.println("Arduino on: " + comPort);

			Element projectsElement = root.element("projects");

			List<Element> projects = (List<Element>) projectsElement
					.elements("project");

			for (Element project : projects) {
				System.out.println("Project: "
						+ project.elementText("display"));
				monitor.addProject(new JenkinsProject(host, project
						.elementText("display"), project
						.elementText("jenkins")));
			}

		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		arduino.initialize(comPort);
	}

	private void createDefaultXMLFile(String settingsFilename) {
		try {
			FileOutputStream fos = new FileOutputStream(settingsFilename);
			OutputStreamWriter out = new OutputStreamWriter(fos, "UTF-8");

			out.write(	"<snitcher>\n" + 
						"  <host>http://winhudson.stonethree.com:8080</host>\n" +
					    "  <com>COM14</com>\n" +
					    "  <projects>\n" +
					    "    <project>\n" +
					    "      <display>Lynxx</display>\n" +
					    "      <jenkins>Lynxx</jenkins>\n" +
					    "    </project>\n" +
					    "    <project>\n" +
					    "      <display>Froth Sensor</display>\n" +
					    "      <jenkins>FrothSensor</jenkins>\n" +
					    "    </project>\n" +
					    "  </projects>\n" +
					    "</snitcher>\n");
			out.close();
			fos.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public Snitcher() {

		readSettingsFromXML();

		view = new SnitcherView(this);
		view.createAndShowGUI();

		initializeVoice();

		timer.start();

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

		arduino.close();
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
				// System.out.println(project.getStatus());
				if (project.isBuilding()) {
					arduino.setChannelToState(offset, ProjectState.BUILDING);
				} else if (project.getStatus().equals("SUCCESS")) {
					arduino.setChannelToState(offset,
							ProjectState.BUILD_SUCCEEDED);
				} else {
					arduino.setChannelToState(offset, ProjectState.BUILD_FAILED);

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
