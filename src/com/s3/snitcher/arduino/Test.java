package com.s3.snitcher.arduino;

public class Test {

	public static void main(String[] args) throws Exception {
		ArduinoSnitcher main = new ArduinoSnitcher();
		main.initialize("COM14");
		System.out.println("Started");
		
		while (true)
		{
			main.setChannelToState(0, ProjectState.BUILD_SUCCEEDED);
			Thread.sleep(500);
			main.setChannelToState(0, ProjectState.BUILD_FAILED);
			Thread.sleep(500);
			main.setChannelToState(1, ProjectState.BUILD_SUCCEEDED);
			Thread.sleep(500);
			main.setChannelToState(1, ProjectState.BUILD_FAILED);
			Thread.sleep(500);
			main.setChannelToState(2, ProjectState.BUILD_SUCCEEDED);
			Thread.sleep(500);
			main.setChannelToState(2, ProjectState.BUILD_FAILED);
			Thread.sleep(500);
			main.setChannelToState(3, ProjectState.BUILD_SUCCEEDED);
			Thread.sleep(500);
			main.setChannelToState(3, ProjectState.BUILD_FAILED);
			Thread.sleep(500);
			
		}
		
	}
}
