package eu.els.oss.schematronCompiler.maven;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.components.io.filemappers.FileMapper;
import org.codehaus.plexus.util.DirectoryScanner;

import eu.els.schematronCompiler.SchematronCompilationException;
import eu.els.schematronCompiler.SchematronCompiler;

@Mojo(name = "compile-schematron-to-xslt")
public class CompileSchematronMojo extends AbstractMojo {

	private static final Charset UTF_8 = Charset.forName("UTF-8");

	private static final String DEFAULT_SUFFIX = ".compiled.sch";

	@Parameter(defaultValue = "${project.basedir}", required = true, readonly = true)
	private File basedir;

	@Parameter
	private String encoding;
	
	@Parameter
	private String[] catalogs;

	@Parameter
	private SchematronCompilationSet[] schematronCompilationSets;
	
	private List<String> absoluteCatalogs;
	
	private SchematronCompiler compiler;


	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		try {
			
            this.absoluteCatalogs = prepareAbsoluteCatalogs();
            getLog().debug("Using catalogs : " + this.absoluteCatalogs.toString());
            
    		this.compiler = new SchematronCompiler(this.absoluteCatalogs.toArray(new String[this.absoluteCatalogs.size()]));

            
            for (SchematronCompilationSet compilationSet : this.schematronCompilationSets) {
				processSet(compilationSet);
			}		
		} catch (Exception e) {
			throw new MojoFailureException("An exception occured during schematron compilation", e);
		}

	}

	/**
	 * Runs the compilation process for each file mapped by the given compilationSet.
	 * 
	 * @param compilationSet
	 * @throws MojoFailureException
	 * @throws MojoExecutionException
	 */
	private void processSet(SchematronCompilationSet compilationSet) throws MojoFailureException, MojoExecutionException {
		List<FilePair> filePairs = filePairs(compilationSet);
		
		for (FilePair filePair : filePairs) {
			try {
				this.compiler.compile(filePair.getInput(), filePair.getOutput());
			} catch (SchematronCompilationException e) {
				throw new MojoFailureException("An exception occured compiling " + filePair.getInput().getAbsolutePath(), e);
			}
		}
	}

	/**
	 * 
	 * @param compilationSet
	 * @return A list of input(schematron) / output(target compiled xsl) File pairs for the given compilationSet
	 * @throws MojoFailureException
	 * @throws MojoExecutionException
	 */
	private List<FilePair> filePairs(SchematronCompilationSet compilationSet) throws MojoFailureException, MojoExecutionException {
		
		List<FilePair> filePairs = new ArrayList<>();
		File destinationDir = asAbsoluteFile(compilationSet.getOutputDir());
		for(String fileName : getIncludedFileNames(compilationSet.getDir(),compilationSet.getIncludes(),compilationSet.getExcludes())){
			FilePair pair = new FilePair();
			
			pair.setInput(new File(asAbsoluteFile(compilationSet.getDir()), fileName));

			File targetFile = new File(destinationDir,mapTargetFile(compilationSet, fileName));
			pair.setOutput(targetFile);
			
			filePairs.add(pair);
		}
		return filePairs;
	}

	/**
	 * 
	 * @param compilationSet
	 * @param fileName 
	 * @return The target file name given by the FileMapper, if any exists.
	 */
	private String mapTargetFile(SchematronCompilationSet compilationSet, String fileName) {
		String targetName = fileName + DEFAULT_SUFFIX;
		
		if(compilationSet.getFileMappers() != null){
			for(FileMapper fileMapper : compilationSet.getFileMappers()){
				targetName = fileMapper.getMappedFileName(fileName);
			}
		}
		
		return targetName; // The last fileMapper is used, like in xml-maven-plugin	
	}

	/**
	 * Resolves the <code>&lt;catalogs&gt;</code> files referenced in the POM<br>
	 * File that do not exists or can't be resolved are ignored.
	 * 
	 * @return a list of absolute paths of the catalogs.
	 */
	private List<String> prepareAbsoluteCatalogs() {
		List<String> absoluteCatalogs = new ArrayList<>(this.catalogs.length);
		for (String relativeCatalog : this.catalogs) {
			File catalog = new File(getBasedir(), relativeCatalog);
			if(catalog.exists()){
				absoluteCatalogs.add(catalog.getAbsolutePath());
			} else {
				getLog().error("Ignoring not found Catalog : " + relativeCatalog);
			}
		}
		
		return absoluteCatalogs;	
	}
	
	protected String[] getIncludedFileNames( File pDir, String[] pIncludes, String[] pExcludes )
	        throws MojoFailureException, MojoExecutionException
	    {
	        if ( pDir == null )
	        {
	            throw new MojoFailureException( "A ValidationSet or TransformationSet"
	                + " requires a nonempty 'dir' child element." );
	        }
	        final File dir = asAbsoluteFile( pDir );
	        if ( !dir.isDirectory() )
	        {
	            throw new MojoExecutionException("The directory " + dir.getPath()
	                + ", which is a base directory of a ValidationSet or TransformationSet, does not exist.");
	        }
	        final DirectoryScanner ds = new DirectoryScanner();
	        ds.setBasedir( dir );
	        if ( pIncludes != null && pIncludes.length > 0 )
	        {
	            ds.setIncludes( pIncludes );
	        }
	        if ( pExcludes != null && pExcludes.length > 0 )
	        {
	            ds.setExcludes( pExcludes );
	        }
	        ds.scan();
	        return ds.getIncludedFiles();
	    }
	
	 protected File asAbsoluteFile( File f )
	    {
	        if ( f.isAbsolute() )
	        {
	            return f;
	        }
	        return new File( getBasedir(), f.getPath() );
	    }


	public File getBasedir() {
		return basedir;
	}


	public void setBasedir(File basedir) {
		this.basedir = basedir;
	}


	public String getEncoding() {
		return encoding;
	}


	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}


	public String[] getCatalogs() {
		return catalogs;
	}


	public void setCatalogs(String[] catalogs) {
		this.catalogs = catalogs;
	}


	public SchematronCompilationSet[] getSchematronCompilationSets() {
		return schematronCompilationSets;
	}


	public void setSchematronCompilationSets(SchematronCompilationSet[] schematronCompilationSets) {
		this.schematronCompilationSets = schematronCompilationSets;
	}


	
	

}
