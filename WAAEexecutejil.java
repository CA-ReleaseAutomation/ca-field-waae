/*******************************************************************************************************
*	Copyright (c) 2014 CA. All rights reserved.
*
*	This software and all information contained therein is confidential and proprietary and shall
*	not be duplicated, used, disclosed or disseminated in any way except as authorized by the
*	applicable license agreement, without the express written permission of CA. All authorized
*	reproductions must be marked with this language.
*
*	EXCEPT AS SET FORTH IN THE APPLICABLE LICENSE AGREEMENT, TO THE EXTENT PERMITTED BY APPLICABLE
*	LAW, CA PROVIDES THIS SOFTWARE WITHOUT WARRANTY OF ANY KIND, INCLUDING WITHOUT LIMITATION, ANY
*	IMPLIED WARRANTIES OF MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE. IN NO EVENT WILL CA
*	BE LIABLE TO THE END USER OR ANY THIRD PARTY FOR ANY LOSS OR DAMAGE, DIRECT OR INDIRECT, FROM
*	THE USE OF THIS SOFTWARE, INCLUDING WITHOUT LIMITATION, LOST PROFITS, BUSINESS INTERRUPTION,
*	GOODWILL, OR LOST DATA, EVEN IF CA IS EXPRESSLY ADVISED OF SUCH LOSS OR DAMAGE.
*
********************************************************************************************************/

package com.mycompany.cafieldwaae;

import com.ca.nolio.rdk.dto.*;
import com.ca.nolio.rdk.dto.exception.*;
import com.ca.nolio.rdk.template.helper.*;
import com.nolio.platform.shared.api.ActionDescriptor;
import com.nolio.platform.shared.api.ActionResult;
import com.nolio.platform.shared.api.ParameterDescriptor;
import com.nolio.platform.shared.api.Password;
import com.nolio.platform.shared.datamodel.Action;


import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import com.jayway.jsonpath.JsonPath;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.Executor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import javax.xml.xpath.XPathConstants;

/**
 * 
 * @author Joe Offenberg
 */
@ActionDescriptor(
	name = "WAAE-execute-jil",
	description = "Export an application in JIL format",
        category="Workload" )
    public class WAAEexecutejil extends Action {
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(WAAEexecutejil.class);
	private static final String SCAPE_CHAR = "\\";
	private static String SCRIPT_OUTPUT = "rdkScriptOutput.txt";
	private List<InputParam> inputParameters = null;	
	private List<OutputParam> outputParameters = null;
	private ExecutionResult execResult = null;





	@ParameterDescriptor(
		name = "Application",
		description = "AutoSys Application",
		out = false,
		in = true,
		nullable = false, // parameter is required and no default value set
		order = 10,
        defaultValueAsString = ""
    	)

    




            private String application1;
            

	@ParameterDescriptor(
		name = "AUTOUSER",
		description = "Location of AUTOUSER home directory",
		out = false,
		in = true,
		nullable = true, // parameter not required or default value set
		order = 20,
        defaultValueAsString = ""
    	)

    




            private String autouser2;
            

	@ParameterDescriptor(
		name = "File",
		description = "Path and File",
		out = false,
		in = true,
		nullable = true, // parameter not required or default value set
		order = 30,
        defaultValueAsString = ""
    	)

    




            private String file3;
            


	@ParameterDescriptor(
		name = "Execution Output",
		description = "This output parameter holds the standard output of the command execution.",
		out = true,
		in = false
	)
	
	
	private String executionoutput1;

	@ParameterDescriptor(
		name = "Error Output",
		description = "This output parameter holds the standard error output of the command execution.",
		out = true,
		in = false
	)
	
	
	private String erroroutput2;

	@ParameterDescriptor(
		name = "Exit Code",
		description = "This output parameter holds the returned exit code of the command execution.",
		out = true,
		in = false
	)
	
	
	private Integer exitcode3;





    // Getters and Setters
        	public String getapplication1() {
        	return application1;
    	}
    
    	public void setapplication1(String application1) {
    		this.application1 = application1;
    	}
	        	public String getautouser2() {
        	return autouser2;
    	}
    
    	public void setautouser2(String autouser2) {
    		this.autouser2 = autouser2;
    	}
	        	public String getfile3() {
        	return file3;
    	}
    
    	public void setfile3(String file3) {
    		this.file3 = file3;
    	}
	    // Getters and Setters
        	public String getexecutionoutput1() {
        	return executionoutput1;
    	}
    
    	public void setexecutionoutput1(String executionoutput1) {
    		this.executionoutput1 = executionoutput1;
    	}
	        	public String geterroroutput2() {
        	return erroroutput2;
    	}
    
    	public void seterroroutput2(String erroroutput2) {
    		this.erroroutput2 = erroroutput2;
    	}
	        	public Integer getexitcode3() {
        	return exitcode3;
    	}
    
    	public void setexitcode3(Integer exitcode3) {
    		this.exitcode3 = exitcode3;
    	}
	

	@Override
	public ActionResult execute() {
		try {
			final CliCommand cliCommand = CommandLineExecutorHelper.getExecutionCommand(getScriptAction());
        	if (cliCommand.getWaitForProcessToFinish()) {
        		execResult = CommandLineExecutorHelper.executeCommand(cliCommand);
        		cliCommand.getExecDirFile().delete();
        	} else {
    			new Thread() {
    				@Override
    				public void run() {
    					try {
    			            execResult = CommandLineExecutorHelper.executeCommand(cliCommand);
    		            } catch (Exception e) {
    			            log.error("Caught exception during background process execution.", e);
    		            } 
    				}
    			}.start();	
        	}
		} catch (Exception e) {
            log.error("Action execution failed.", e);
			return new ActionResult(false, e.getMessage());
		}

		populateOutputParameters(execResult);
			
		return getActionResult();
	}

	
	
	private void populateOutputParameters(ExecutionResult execResult) {
		if (execResult == null) {
			return;
		}
		if (execResult.isEmpty()) {
		    log.debug("Execution result is empty: skip populate parameters phasis!");
			return;
		}

        String output = null;
                        if (StringUtils.isNotEmpty(execResult.getStandardOutput())) {
            output = execResult.getStandardOutput();
        		        	try {
        		Pattern pattern = Pattern.compile("[\\s\\S]*");
        		Matcher matcher = pattern.matcher(output);
        		String stringMatch = null;
        		if (matcher.find()) {
        			stringMatch = matcher.group();
        		}
        
        		executionoutput1 = String.valueOf(stringMatch);
        	} catch (PatternSyntaxException e) {
        		log.error("Caught exception during populating output parameter: " + "Execution Output" + ". Bad pattern", e);
        	} catch (Exception e) {
        		log.error("Caught exception during populating output parameter: " + "Execution Output", e);
        	}
		        }
	                            if (StringUtils.isNotEmpty(execResult.getStandardError())) {
            output = execResult.getStandardError();
        		        	try {
        		Pattern pattern = Pattern.compile("[\\s\\S]*");
        		Matcher matcher = pattern.matcher(output);
        		String stringMatch = null;
        		if (matcher.find()) {
        			stringMatch = matcher.group();
        		}
        
        		erroroutput2 = String.valueOf(stringMatch);
        	} catch (PatternSyntaxException e) {
        		log.error("Caught exception during populating output parameter: " + "Error Output" + ". Bad pattern", e);
        	} catch (Exception e) {
        		log.error("Caught exception during populating output parameter: " + "Error Output", e);
        	}
		        }
	                if (execResult.getExitCode() != null) {
                    exitcode3 = execResult.getExitCode();
                }
    
	}

	private ActionResult getActionResult() {
	    	ActionResult actionResult = null;
    	try { 
    		actionResult = new ActionResult(true, ReplacementsHelper.replaceOutputParameters( ReplacementsHelper.replaceInputParameters( "${Execution Output}", getInput() ), getOutput() ) );
    	} catch (Exception e) {
    		log.error("There was an error on the replacement of parameters", e);
    	}

	
			StringBuilder errors = new StringBuilder();
																		if( ErrorHelper.testErrorCondition(exitcode3, Operations.valueOf("GREATER_THAN"), "0")) {
			   			   			try { 
			    				errors.append( ReplacementsHelper.replaceOutputParameters( ReplacementsHelper.replaceInputParameters( "${Error Output}", getInput() ), getOutput() ) ).append("<br/>");
			} catch(Exception e) {
				log.error("There was an error on the replacement of parameters", e);
			}    		
			   		}
								String errorString = errors.toString();
		if (StringUtils.isNotEmpty(errorString)) {
			actionResult = new ActionResult(false, errorString);
		}


		return actionResult;
	}

	
	private void createOutputLocationFile(String outputLocation, String content, Boolean overwriteOutput) throws ActionException {
		File outputDirs = new File(outputLocation);
		if (!outputDirs.exists()) {
			try {
	            outputDirs.mkdirs();
            } catch (SecurityException se) {
                log.error("Caught security exception while creating output location: " + outputLocation);
                throw new ActionException("Security exception while creating output location: " + se.getMessage(), se);
            }
		}
		
		String fileName = outputLocation + File.separator + SCRIPT_OUTPUT;
        try {
        	FileWriter fw = new FileWriter(fileName, !overwriteOutput);
	        fw.write(content);
	        fw.flush();
	        fw.close();
        } catch (IOException e) {
            log.error("Caught IO exception during writing to file");
            throw new ActionException("IO exception during writing to file: " + e.getMessage(), e);
        }
    }	

	
	private List<InputParam> getInput() {
				inputParameters = new ArrayList<InputParam>();
			InputParam p = null;
					p = new InputParam();
			p.setName("Application");
			            			//p.setValue("");
			p.setVariableName("application");
			p.setType(VarType.String);

									    			 				p.setValue( application1 );
			 								
			inputParameters.add(p);		
					p = new InputParam();
			p.setName("AUTOUSER");
			            			//p.setValue("");
			p.setVariableName("autouser");
			p.setType(VarType.String);

									    			 				p.setValue( autouser2 );
			 								
			inputParameters.add(p);		
					p = new InputParam();
			p.setName("File");
			            			//p.setValue("");
			p.setVariableName("file");
			p.setType(VarType.String);

									    			 				p.setValue( file3 );
			 								
			inputParameters.add(p);		
				
	 		return inputParameters;
	}	
	
	
	private List<OutputParam> getOutput() {
			if (outputParameters == null) {
			outputParameters = new ArrayList<OutputParam>();
			OutputParam p = null;
					p = new OutputParam();
			p.setName("Execution Output");
			            			p.setVariableName("executionoutput");
			p.setType(VarType.String);
			
							p.setFilterType(FilterType.REGEX);
						
									    			 			 	if(executionoutput1 != null){
					p.setFilterValue( executionoutput1.toString() );
				}
			 						
			outputParameters.add(p);					
					p = new OutputParam();
			p.setName("Error Output");
			            			p.setVariableName("erroroutput");
			p.setType(VarType.String);
			
							p.setFilterType(FilterType.REGEX);
						
									    			 			 	if(erroroutput2 != null){
					p.setFilterValue( erroroutput2.toString() );
				}
			 						
			outputParameters.add(p);					
					p = new OutputParam();
			p.setName("Exit Code");
			            			p.setVariableName("exitcode");
			p.setType(VarType.Integer);
			
							p.setFilterType(FilterType.REGEX);
						
									    			 								if(exitcode3 != null) {
					p.setFilterValue( exitcode3.toString() );
				}
			 						
			outputParameters.add(p);					
				}
			return outputParameters;
	}
	
	

	
		


	private List<ErrorCondition> getErrorConditions(){
		List<ErrorCondition> errorCondition = null;
		
		errorCondition = new ArrayList<ErrorCondition>();
		ErrorCondition ec = null;
					ec = new ErrorCondition();
			ec.setMessage("${Error Output}");		
			ec.setParameter("Exit Code");
			ec.setValue("0");
							ec.setOperation(Operations.GREATER_THAN);
							
		return errorCondition;
	}


	private List<Script> getScripts(){
		List<Script> scripts = new ArrayList<Script>();
		
		Script script = null;
		List<EnvVariable> envList = null;
		EnvVariable env = null;
				
						script = new Script();
							script.setOsType(OS.WINDOWS);
						
																									
			script.setContent("");
			script.setExecDir("%TEMP%");

                        script.setTimeout(30);
            
			script.setOverwriteOutput(true);
			script.setWaitForProcess(true);
			
							
			
			scripts.add(script);
					
				
						script = new Script();
							script.setOsType(OS.LINUX);
						
																									
			script.setContent(" . ${AUTOUSER}/autosys.bash.*\n jil < ${File}");
			script.setExecDir("/tmp/");

                        script.setTimeout(30);
            
			script.setOverwriteOutput(true);
			script.setWaitForProcess(true);
			
							
			
			scripts.add(script);
					
				
		
		return scripts;
	}




	

private com.ca.nolio.rdk.dto.ScriptAction getScriptAction() {
	com.ca.nolio.rdk.dto.ScriptAction action = new com.ca.nolio.rdk.dto.ScriptAction();

	action.setShellType(Shell.DEFAULT_OS);
		
	
	action.setScripts(getScripts());			
	action.setInputParamList(getInput());
	action.setName("WAAE-execute-jil");
	action.setDescription("Export an application in JIL format");
	action.setCategory("");	
	action.setSuccessMessage("${Execution Output}");
		
	action.setErrorCondition(getErrorConditions());
	/*
	protected String type;	
	protected Date lastModified;
	protected Boolean complete;
	protected List<InputParam> inputParamList;
	protected List<OutputParam> outputParamList;
	protected String successMessage;
	protected List<ErrorCondition> errorCondition;
	protected String imageLocation;
	protected String className;
	protected Boolean updated;
	protected String packageName;
	protected String category;
	*/		
	
	
	return action;
}
	
	
		
}
