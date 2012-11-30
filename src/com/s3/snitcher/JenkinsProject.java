package com.s3.snitcher;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class JenkinsProject {
	private String projectName = null;
	private String jenkinsName = null;
	private String host = null;

	private boolean building = false;
	private int buildNumber = -1;
	private String buildResult = "SUCCESS";
	private String lastUser = "";
	private boolean notified = true;
	private int currentBuildNumber = 999999999;
	private int score = 0;

	public JenkinsProject(String newHost, String newProjectName,
			String newJenkinsName) {
		projectName = newProjectName;
		jenkinsName = newJenkinsName;
		host = newHost;
	}

	public String getLastUser() {
		return lastUser;
	}
	
	public String getName() {
		return projectName;
	}

	public String getJenkinsName() {
		return jenkinsName;
	}

	public String getStatus() {
		return buildResult;
	}

	public boolean mustNotify() {
		return !notified;
	}

	public void setNotified(boolean newNotified) {
		notified = newNotified;
	}

	public void updateStatus() {
		// every Hudson model object exposes the .../api/xml
		URL url;
		URL lastBuildURL;
		try {
			String urlString = host + "/job/" + jenkinsName + "/api/xml";
			url = new URL(urlString);

			// Debug write
			// -----------
			/*
			try {
				BufferedWriter out = new BufferedWriter(new FileWriter(
						"c:\\url.xml"));

				BufferedReader in = new BufferedReader(new InputStreamReader(
						url.openStream()));

				String inputLine;
				while ((inputLine = in.readLine()) != null) {
					//System.out.println(inputLine);
					out.write(inputLine);
				}

				in.close();
				out.close();
			} catch (IOException e) {
				System.out.println("Exception ");

			}
			*/

			// -----------

			// Firstly find latest build
			Document dom;
			dom = new SAXReader().read(url);

			Element lastBuild = (Element) dom.getRootElement().element(
					"lastBuild");
			String lastBuildURLString = lastBuild.elementText("url")
					+ "api/xml";

			lastBuildURL = new URL(lastBuildURLString);

			// Get details about latest build
			Document buildDom = new SAXReader().read(lastBuildURL);
			Element buildRoot = (Element) buildDom.getRootElement();

			building = Boolean.parseBoolean(buildRoot.elementText("building"));
			int newBuildNumber = Integer.parseInt(buildRoot
					.elementText("number"));
			String newBuildResult = buildRoot.elementText("result");

			currentBuildNumber = newBuildNumber;

			if ((!building) && (currentBuildNumber > buildNumber)) {
				if (!newBuildResult.equals("SUCCESS")) {
					notified = false;
				} else if ((buildResult.equals("FAILURE"))
						&& (newBuildResult.equals("SUCCESS"))) {
					notified = false;
				}

				if (!notified) {
					buildNumber = newBuildNumber;
					buildResult = newBuildResult;
				}
			}

			// Get details about last commit
			Element srcRoot = buildDom.getRootElement().element("changeSet");
			Element item = srcRoot.element("item");
			if (item != null) {
				lastUser = item.elementText("user");
			} else {
				lastUser = "anonymous";
			}

			// Get details about last commit
			Element healthReport = (Element) dom.getRootElement().element(
					"healthReport");
			score = 0;
			if (healthReport != null) {
				score = Integer.valueOf(healthReport.elementText("score"));
			}


		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public int getScore() {
		return score;
	}
	
	public String getNotifyString() {
		//String notifyString = projectName + " build " + buildNumber + " " + buildResult;
		String notifyString = projectName + " " + buildResult;
		
		if (!lastUser.equals("anonymous")) {
			notifyString += " thanks to " + lastUser + "!";
		}
				 
		return notifyString;
	}

	public boolean isBuilding() {
		return building;
	}
}
