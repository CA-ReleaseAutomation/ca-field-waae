/*******************************************************************************************************
*   Copyright (c) 2014 CA. All rights reserved.
*
*   This software and all information contained therein is confidential and proprietary and shall
*   not be duplicated, used, disclosed or disseminated in any way except as authorized by the
*   applicable license agreement, without the express written permission of CA. All authorized
*   reproductions must be marked with this language.
*
*   EXCEPT AS SET FORTH IN THE APPLICABLE LICENSE AGREEMENT, TO THE EXTENT PERMITTED BY APPLICABLE
*   LAW, CA PROVIDES THIS SOFTWARE WITHOUT WARRANTY OF ANY KIND, INCLUDING WITHOUT LIMITATION, ANY
*   IMPLIED WARRANTIES OF MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE. IN NO EVENT WILL CA
*   BE LIABLE TO THE END USER OR ANY THIRD PARTY FOR ANY LOSS OR DAMAGE, DIRECT OR INDIRECT, FROM
*   THE USE OF THIS SOFTWARE, INCLUDING WITHOUT LIMITATION, LOST PROFITS, BUSINESS INTERRUPTION,
*   GOODWILL, OR LOST DATA, EVEN IF CA IS EXPRESSLY ADVISED OF SUCH LOSS OR DAMAGE.
*
********************************************************************************************************/

package com.mycompany.cafieldwaae;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.Map;
import java.util.HashMap;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.lang.IllegalArgumentException;

import com.nolio.platform.shared.api.ActionDescriptor;
import com.nolio.platform.shared.api.ActionResult;
import com.nolio.platform.shared.api.ParameterDescriptor;
import com.nolio.platform.shared.api.Password;
import com.nolio.platform.shared.datamodel.Action;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import com.jayway.jsonpath.JsonPath;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import com.ca.nolio.rdk.dto.Operations;
import com.ca.nolio.rdk.dto.VarType;
import com.ca.nolio.rdk.dto.FilterType;
import com.ca.nolio.rdk.dto.InputParam;
import com.ca.nolio.rdk.dto.OutputParam;
import com.ca.nolio.rdk.dto.Authentication;
import com.ca.nolio.rdk.dto.HttpMethod;
import com.ca.nolio.rdk.dto.QOP;
import com.ca.nolio.rdk.dto.CredentialsInfo;
import com.ca.nolio.rdk.dto.exception.ActionException;
import com.ca.nolio.rdk.model.helper.*;
import com.ca.nolio.rdk.model.RestClientManager;
import com.ca.nolio.rdk.dto.HttpRDKResponse;
import com.ca.nolio.rdk.dto.RDKHeader;

import com.ca.nolio.rdk.template.helper.ErrorHelper;
import com.ca.nolio.rdk.template.helper.ReplacementsHelper;

import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import javax.xml.xpath.XPathConstants;

import net.minidev.json.*;


/**
 *
 * @author Joe Offenberg
 */
@ActionDescriptor(
        name = "WAAE-start-job",
        description = "WAAE sendevent start-job through webservice",
        category="Workload." )
    public class WAAEstartjob extends Action {
    private static final long serialVersionUID = 1L;
    private static final Logger log = Logger.getLogger(WAAEstartjob.class);
    private static final String SCAPE_CHAR = "\\";
    private static final String CHAR_SET = "UTF-8";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String ENCODING = "Accept-Encoding";
    private static String WEBSERVICE_OUTPUT = "rdkWebserviceOutput.txt";
    private List<InputParam> inputParameters = null;    
    private List<OutputParam> outputParameters = null;  


                    
    @ParameterDescriptor(
        name = "WAAE Web Server",
        description = "WAAS Webserver in https://webserver:port  example  https://WSServer:9443",
        out = false,
        in = true,
        nullable = false, // parameter is required
        order = 10,
        defaultValueAsString = ""
        )


            private String waaewebserver1;
        @ParameterDescriptor(
        name = "Username",
        description = "The Username to authenticate",
        out = false,
        in = true,
        nullable = false, // parameter is required
        order = 20,
        defaultValueAsString = ""
        )

                 private String username2;
                @ParameterDescriptor(
        name = "Password",
        description = "The Password to use",
        out = false,
        in = true,
        nullable = false, // parameter is required
        order = 30,
        defaultValueAsString = ""
        )

                 private Password password3;
                @ParameterDescriptor(
        name = "jobname",
        description = "jobname Description",
        out = false,
        in = true,
        nullable = false, // parameter is required
        order = 40,
        defaultValueAsString = ""
        )


            private String jobname4;
        @ParameterDescriptor(
        name = "comment",
        description = "comment Description",
        out = false,
        in = true,
        nullable = true, // parameter not required
        order = 50,
        defaultValueAsString = ""
        )


            private String comment5;
    
    @ParameterDescriptor(
        name = "Authentication", 
        description = "Authenthication type.", 
        out = false, 
        in = true, 
        nullable = true,
        defaultValueAsString = "Authentication.BASIC",
        order = 60
    )
    private Authentication authType = Authentication.BASIC;


    @ParameterDescriptor(
            name = "Response Body",
                    description = "This output parameter holds the standard response of the request.",
                out = true, 
            in = false     
        )
        
    private String responsebody1;

    @ParameterDescriptor(
            name = "Response Headers",
                    description = "The Response Headers from the request.",
                out = true, 
            in = false     
        )
        
    private String responseheaders2;

    @ParameterDescriptor(
            name = "Response Code",
                    description = "The Response Code.",
                out = true, 
            in = false     
        )
        
    private Integer responsecode3;


    // Getters and Setters
            public String getwaaewebserver1() {
        return waaewebserver1;
    } 
    
    public void setwaaewebserver1(String waaewebserver1) {
        this.waaewebserver1 = waaewebserver1;
    }
                public String getusername2() {
        return username2;
    } 
    
    public void setusername2(String username2) {
        this.username2 = username2;
    }
                public Password getpassword3() {
        return password3;
    } 
    
    public void setpassword3(Password password3) {
        this.password3 = password3;
    }
                public String getjobname4() {
        return jobname4;
    } 
    
    public void setjobname4(String jobname4) {
        this.jobname4 = jobname4;
    }
                public String getcomment5() {
        return comment5;
    } 
    
    public void setcomment5(String comment5) {
        this.comment5 = comment5;
    }
        // Getters and Setters
        public String getresponsebody1() {
        return responsebody1;
    }
    
    public void setresponsebody1(String responsebody1) {
        this.responsebody1 = responsebody1;
    }
            public String getresponseheaders2() {
        return responseheaders2;
    }
    
    public void setresponseheaders2(String responseheaders2) {
        this.responseheaders2 = responseheaders2;
    }
            public Integer getresponsecode3() {
        return responsecode3;
    }
    
    public void setresponsecode3(Integer responsecode3) {
        this.responsecode3 = responsecode3;
    }
    
    @Override
    public ActionResult execute() {
        HttpRDKResponse response = new HttpRDKResponse("NO RESPONSE", null, -1);
        try {
            log.debug("A call was received to execute a Restful Action: " + "WAAE-start-job");

            String rawRequestValue = "{\"jobName\":\"${jobname}\",\"comment\":\"${comment}\"}";

            response = new RestClientManager().restCall(HttpMethod.POST, authType, getInput(),
                "${WAAE Web Server}/AEWS/event/start-job", 30, "", "application/json",
                rawRequestValue, getRequestParameters(), getHeaders());

            log.debug("REST call response: " + response);

                                        } catch (Exception e) {
            response = new HttpRDKResponse("There was an error trying to execute a REST Call Action: '"
                + "WAAE-start-job" + "' for Action Pack: "   + "", null, -1);
            log.error(response.getContent(), e);
            return new ActionResult(false, response.getContent() + e.getMessage());
        }

        populateOutputParameters(response);
        return getActionResult();
    }   

    private void populateOutputParameters(HttpRDKResponse output) {
        if (StringUtils.isBlank(output.getContent())) {
            log.debug("Output is empty");
        }
        
                   
                    try {
                Pattern pattern = Pattern.compile("[\\s\\S]*");
                System.out.println("Response Body");
                                    Matcher matcher = pattern.matcher(output.getContent());
                                String stringMatch = null;
                if (matcher.find()) {
                    stringMatch = matcher.group();
                }
        
              responsebody1 = String.valueOf(stringMatch);
            } catch (PatternSyntaxException e) {
                log.error("Caught exception during populating output parameter: " + "Response Body" + ". Bad pattern", e);
            } catch (Exception e) {
                log.error("Caught exception during populating output parameter: " + "Response Body", e);
            }
                               
                    try {
                Pattern pattern = Pattern.compile("[\\s\\S]*");
                System.out.println("Response Headers");
                                    RDKHeader[] headers = output.getHeaders();;
                    JSONArray jsonArray = new JSONArray();
                    for(RDKHeader header : headers){
                        JSONObject obj = new JSONObject();
                        obj.put("value", header.getValue());
                        obj.put("header", header.getHeader());
                        jsonArray.add(obj);
                    }
                    
                    Matcher matcher = pattern.matcher(jsonArray.toJSONString());
                                String stringMatch = null;
                if (matcher.find()) {
                    stringMatch = matcher.group();
                }
        
              responseheaders2 = String.valueOf(stringMatch);
            } catch (PatternSyntaxException e) {
                log.error("Caught exception during populating output parameter: " + "Response Headers" + ". Bad pattern", e);
            } catch (Exception e) {
                log.error("Caught exception during populating output parameter: " + "Response Headers", e);
            }
                               
                    log.debug("Response Code: " + output.getResponseCode());
            responsecode3 = output.getResponseCode();
                }
    
    private ActionResult getActionResult() {
            ActionResult actionResult = null;
        try { 
            actionResult = new ActionResult(true, ReplacementsHelper.replaceOutputParameters( ReplacementsHelper.replaceInputParameters( "Execution succeeded", getInput() ), getOutput() ) );
        } catch (Exception e) {
            log.error("There was an error on the replacement of parameters", e);
        }
        

        return actionResult;
    }

    private void createOutputLocationFile(String outputLocation, String content) throws ActionException {
        File outputDirs = new File(outputLocation);
        if (!outputDirs.exists()) {
            try {
                outputDirs.mkdirs();
            } catch (SecurityException se) {
                log.error("Caught security exception while creating output location: " + outputLocation);
                throw new ActionException("Security exception while creating output location: " + se.getMessage(), se);
            }
        }
        
        String fileName = outputLocation + File.separator + WEBSERVICE_OUTPUT;
        try {
            FileWriter fw = new FileWriter(fileName);
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
            p.setName("WAAE Web Server");
                                    p.setVariableName("waaewebserver");
            p.setPrefix("");
            p.setType(VarType.String);
                                                                     p.setValue(waaewebserver1);
                                             
            inputParameters.add(p);     
                    p = new InputParam();
            p.setName("Username");
                                    p.setVariableName("username");
            p.setPrefix("");
            p.setType(VarType.String);
                                                                     p.setValue(username2);
                                             
            inputParameters.add(p);     
                    p = new InputParam();
            p.setName("Password");
                                    p.setVariableName("password");
            p.setPrefix("");
            p.setType(VarType.Password);
                                                                                     if(password3 != null) {
                    p.setValue(password3.toString());
                }
                                             
            inputParameters.add(p);     
                    p = new InputParam();
            p.setName("jobname");
                                    p.setVariableName("jobname");
            p.setPrefix("");
            p.setType(VarType.String);
                                                                     p.setValue(jobname4);
                                             
            inputParameters.add(p);     
                    p = new InputParam();
            p.setName("comment");
                                    p.setVariableName("comment");
            p.setPrefix("");
            p.setType(VarType.String);
                                                                     p.setValue(comment5);
                                             
            inputParameters.add(p);     
                
        
        
        return inputParameters;
    }   

    private List<OutputParam> getOutput() {
            if (outputParameters == null) {
            outputParameters = new ArrayList<OutputParam>();
            OutputParam p = null;
                    p = new OutputParam();
            p.setName("Response Body");
                                    p.setVariableName("responsebody");
            p.setType(VarType.String);
            
                            p.setFilterType(FilterType.REGEX);
                        
                                                                     p.setFilterValue( responsebody1 );
                                     
            outputParameters.add(p);                    
                    p = new OutputParam();
            p.setName("Response Headers");
                                    p.setVariableName("responseheaders");
            p.setType(VarType.String);
            
                            p.setFilterType(FilterType.REGEX);
                        
                                                                     p.setFilterValue( responseheaders2 );
                                     
            outputParameters.add(p);                    
                    p = new OutputParam();
            p.setName("Response Code");
                                    p.setVariableName("responsecode");
            p.setType(VarType.Integer);
            
                            p.setFilterType(FilterType.REGEX);
                        
                                                                                     if(responsecode3 != null) {
                    p.setFilterValue( responsecode3.toString() );
                }
                                     
            outputParameters.add(p);                    
                }
            return outputParameters;
    }

    public HashMap getRequestParameters() {
        HashMap reqParams = null;
        
        return reqParams;
    }

    public HashMap getHeaders() {
        HashMap headers = null;
        
        return headers;
    }
}