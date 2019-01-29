package eu.els.oss.schematronCompiler.maven;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import java.io.File;

import org.codehaus.plexus.components.io.filemappers.FileMapper;

/**
 * An instance of this class is used to specify a set of files, which are compiled against a common schema.
 */
public class SchematronCompilationSet
{
    
    private File dir;

    private String[] includes;

    private String[] excludes;

    private File outputDir;
    
    private FileMapper[] fileMappers;
    
    private Parameter[] parameters;

    /**
     * Returns a directory, which is scanned for files to compile.
     * @return The directory to scan.
     */
    public File getDir()
    {
        return this.dir;
    }

    /**
     * Returns patterns of files, which are being excluded from the compilation set.
     * @return Patters of excluded files.
     */
    public String[] getExcludes()
    {
        return excludes;
    }

    /**
     * Returns patterns of files, which are being included into the compilation set.
     * @return Patters of included files.
     */
    public String[] getIncludes()
    {
        return includes;
    }


    /**
     * Sets a directory, which is scanned for files to compile.
     * @param pDir The directory to scan.
     */
    public void setDir( File pDir )
    {
        dir = pDir;
    }

    /**
     * Sets patterns of files, which are being excluded from the compilation set.
     * @param pExcludes Patters of excluded files.
     */
    public void setExcludes( String[] pExcludes )
    {
        excludes = pExcludes;
    }

	public File getOutputDir() {
		return outputDir;
	}

	public void setOutputDir(File outputDir) {
		this.outputDir = outputDir;
	}

	public FileMapper[] getFileMappers() {
		return fileMappers;
	}

	public void setFileMappers(FileMapper[] fileMappers) {
		this.fileMappers = fileMappers;
	}

	public Parameter[] getParameters() {
		return parameters;
	}

	public void setParameters(Parameter[] parameters) {
		this.parameters = parameters;
	}


  
}
