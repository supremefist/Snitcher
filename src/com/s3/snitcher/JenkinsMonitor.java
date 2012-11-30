package com.s3.snitcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class JenkinsMonitor {

	public List<JenkinsProject> projects = new ArrayList<JenkinsProject>();

	public JenkinsMonitor() {
	}
	
	private static Comparator<JenkinsProject> COMPARATOR = new Comparator<JenkinsProject>() {
		// This is where the sorting happens.
		public int compare(JenkinsProject o1, JenkinsProject o2) {
			return o1.getName().compareTo(o2.getName());
		}
	};
	
	public void addProject(JenkinsProject project) {
		projects.add(project);
	}

	public void updateStatus() {

		for (JenkinsProject project: projects) {
			project.updateStatus();
		}
	}
	
	public void sortProjects() {
		Collections.sort(projects, COMPARATOR);
	}

}
