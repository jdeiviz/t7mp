package com.googlecode.t7mp;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import com.googlecode.t7mp.util.TomcatUtil;


/**
 * 
 * @goal stop-forked
 * @requiresDependencyResolution runtime
 * 
 *
 */
public class StopForkedMojo extends AbstractT7Mojo {

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		ProcessBuilder processBuilder = new ProcessBuilder(TomcatUtil.getStopScriptName(), "stop");
		int exitValue = -1;
		try {
			File binDirectory = new File(getCatalinaBase(), "/bin/");
			Process p = processBuilder.directory(binDirectory).start();
			InputStream is = p.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line;
			while ((line = br.readLine()) != null) {
				getLog().info(line);
			}
			exitValue = p.waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			getLog().error(e.getMessage(), e);
		}
		getLog().debug("Exit-Value ForkedTomcatShutdownHook " + exitValue);
	}

}
