package com.s3.snitcher;

import java.util.ArrayList;
import java.util.List;

public class JenkinsMonitor {

	public List<JenkinsProject> projects = new ArrayList<JenkinsProject>();

	public JenkinsMonitor() {
		projects.add(new JenkinsProject("BeltAnalysis", "BeltAnalysis"));
	}

	public void updateStatus() {

		for (JenkinsProject project: projects) {
			project.updateStatus();
		}
	}

}
