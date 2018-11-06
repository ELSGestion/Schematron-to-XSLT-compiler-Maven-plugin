package eu.els.oss.schematronCompiler.maven;

import java.io.File;

/**
 * A pair of <code>File</code> for a schematron
 * 
 * <ul>
 * 	<li><b>input</b> is the schematron file</li>
 * 	<li><b>output</b> is the target XSL file</li>
 * </ul>
 * 
 * @author ext-jetevenard
 *
 */
public class FilePair {
	
	private File input;
	private File output;
	
	public FilePair(File input, File output) {
		this.input = input;
		this.output = output;
	}
	
	public FilePair(File input) {
		this.input = input;

	}
	
	public FilePair() {

	}

	public File getInput() {
		return input;
	}

	public void setInput(File input) {
		this.input = input;
	}

	public File getOutput() {
		return output;
	}

	public void setOutput(File output) {
		this.output = output;
	}
	
	
	
	

}
