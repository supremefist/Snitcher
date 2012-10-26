package com.s3.snitcher;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class JenkinsProject {
	private String projectName = null;
	private String jenkinsName = null;
	private String host = "http://winhudson.stonethree.com:8080";
	
	private boolean building = false;
	private int buildNumber = -1;
	private String buildResult = "";
	private String lastUser = "";

	public JenkinsProject(String newProjectName, String newJenkinsName) {
		projectName = newProjectName;
		jenkinsName = newJenkinsName;
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

	public void updateStatus() {
		// every Hudson model object exposes the .../api/xml, but in this
		// example
		// we'll just take the root object as an example
		URL url;
		URL lastBuildURL;
		try {
			url = new URL(host + "/job/" + jenkinsName + "/api/xml");

			System.out.println(url);

			// Firstly find latest build
			Document dom;
			dom = new SAXReader().read(url);

			Element lastBuild = (Element) dom.getRootElement().element(
					"lastBuild");
			String lastBuildURLString = lastBuild.elementText("url") + "api/xml";
			lastBuildURL = new URL(lastBuildURLString);
			System.out.println(lastBuildURLString);

			// Get details about latest build
			Document buildDom = new SAXReader().read(lastBuildURL);
			Element buildRoot = (Element) buildDom.getRootElement();
			
			building = Boolean.parseBoolean(buildRoot.elementText("building"));
			buildNumber = Integer.parseInt(buildRoot.elementText("number"));
			buildResult = buildRoot.elementText("result");
			
			// Get details about last commit
			Element srcRoot = buildDom.getRootElement().element("changeSet");
			Element item = srcRoot.element("item");
			lastUser = item.elementText("user");
			
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
}
