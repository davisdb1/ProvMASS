/*

 	MASS Java Software License
	© 2012-2016 University of Washington

	Permission is hereby granted, free of charge, to any person obtaining a copy
	of this software and associated documentation files (the "Software"), to deal
	in the Software without restriction, including without limitation the rights
	to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
	copies of the Software, and to permit persons to whom the Software is
	furnished to do so, subject to the following conditions:

	The above copyright notice and this permission notice shall be included in
	all copies or substantial portions of the Software.

	The following acknowledgment shall be used where appropriate in publications, presentations, etc.:      

	© 2012-2016 University of Washington. MASS was developed by Computing and Software Systems at University of 
	Washington Bothell.

	THE SOFTWARE IS PROVIDED "AS IS"), WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
	IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
	FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
	AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
	LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
	OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
	THE SOFTWARE.

 */
package edu.uw.bothell.css.dsl.MASS.prov.core.MassData;
// PERFORMANCE DOCUMENTED

import edu.uw.bothell.css.dsl.MASS.prov.store.ProvenanceRecorder;
import edu.uw.bothell.css.dsl.MASS.prov.utils.ProvUtils;
import edu.uw.bothell.css.dsl.MASS.prov.utils.StopWatch;
import java.io.Serializable;
import java.net.URI;

/**
 * Intended to be expanded once the need for more preferences arises. Currently
 * just holds host and port data in a URI object so the user does not have to
 * enter it every time.
 *
 * @author Nicolas
 */
@SuppressWarnings("serial")
public class Preferences implements Serializable {

    /**
     * The use of URI ensures that host and port data are syntactically correct.
     * Before adding a URI to preferences, the existence of the host should be
     * confirmed.
     *
     */
    private URI uri;

    //if user wants auto connection on start, requires mass app to start first
    private boolean autoConnect;

    //is user using MASS java or c++ 
    private boolean java;

    //directory of MASS program for auto connection
    private String programDirectory;

    /**
     * Gets the current URI preferences
     *
     * @return current URI containing preferred host and port info
     */
    public URI getURI() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getURI"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getURI"), procRID, new StringBuffer("uri"), uri, null, new StringBuffer("current URI containing preferred host and port info"), true, false, false);
        StopWatch.stop(false);
        return uri;
    }

    /**
     * Sets the new URI preferences
     *
     * @param uri the URI containing validated host and port data
     */
    public void setURI(URI uri) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("setURI"), new StringBuffer("label"), true, new String[]{"uri"}, new Object[]{uri});
        this.setUri(uri);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("setURI"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    public boolean autoConnect() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("autoConnect"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("autoConnect"), procRID, new StringBuffer("this.isAutoConnect()"), this.isAutoConnect(), null, null, true, false, false);
        StopWatch.stop(false);
        return this.isAutoConnect();
    }

    public void setAutoConnect(boolean autoConnect) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("setAutoConnect"), new StringBuffer("label"), true, new String[]{"autoConnect"}, new Object[]{autoConnect});
        this.autoConnect = autoConnect;
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("setAutoConnect"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    /**
     * @return the uri
     */
    public URI getUri() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getUri"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getUri"), procRID, new StringBuffer("uri"), uri, null, new StringBuffer("the uri"), true, false, false);
        StopWatch.stop(false);
        return uri;
    }

    /**
     * @param uri the uri to set
     */
    public void setUri(URI uri) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("setUri"), new StringBuffer("label"), true, new String[]{"uri"}, new Object[]{uri});
        this.uri = uri;
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("setUri"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    /**
     * @return the autoConnect
     */
    public boolean isAutoConnect() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("isAutoConnect"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("isAutoConnect"), procRID, new StringBuffer("autoConnect"), autoConnect, null, new StringBuffer("the autoConnect"), true, false, false);
        StopWatch.stop(false);
        return autoConnect;
    }

    /**
     * @return the java
     */
    public boolean isJava() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("isJava"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("isJava"), procRID, new StringBuffer("java"), java, null, new StringBuffer("the java"), true, false, false);
        StopWatch.stop(false);
        return java;
    }

    /**
     * @param java the java to set
     */
    public void setJava(boolean java) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("setJava"), new StringBuffer("label"), true, new String[]{"java"}, new Object[]{java});
        this.java = java;
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("setJava"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    /**
     * @return the programDirectory
     */
    public String getProgramDirectory() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getProgramDirectory"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getProgramDirectory"), procRID, new StringBuffer("programDirectory"), programDirectory, null, new StringBuffer("the programDirectory"), true, false, false);
        StopWatch.stop(false);
        return programDirectory;
    }

    /**
     * @param programDirectory the programDirectory to set
     */
    public void setProgramDirectory(String programDirectory) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("setProgramDirectory"), new StringBuffer("label"), true, new String[]{"programDirectory"}, new Object[]{programDirectory});
        this.programDirectory = programDirectory;
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("setProgramDirectory"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

}
