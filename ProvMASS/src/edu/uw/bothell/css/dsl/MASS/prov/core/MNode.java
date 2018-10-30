/*

 	MASS Java Software License
	© 2012-2015 University of Washington

	Permission is hereby granted, free of charge, to any person obtaining a copy
	of this software and associated documentation files (the "Software"), to deal
	in the Software without restriction, including without limitation the rights
	to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
	copies of the Software, and to permit persons to whom the Software is
	furnished to do so, subject to the following conditions:

	The above copyright notice and this permission notice shall be included in
	all copies or substantial portions of the Software.

	The following acknowledgment shall be used where appropriate in publications, presentations, etc.:      

	© 2012-2015 University of Washington. MASS was developed by Computing and Software Systems at University of 
	Washington Bothell.

	THE SOFTWARE IS PROVIDED "AS IS"), WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
	IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
	FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
	AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
	LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
	OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
	THE SOFTWARE.

 */
package edu.uw.bothell.css.dsl.MASS.prov.core;
// PERFORMANCE DOCUMENTED

import java.io.InputStream;
import java.io.ObjectInputStream;         // For socket input/output
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.jcraft.jsch.Channel;  // Jsch used for Node connections
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import edu.uw.bothell.css.dsl.MASS.prov.core.logging.Log4J2Logger;
import edu.uw.bothell.css.dsl.MASS.prov.core.logging.LogLevel;
import edu.uw.bothell.css.dsl.MASS.prov.store.ProvenanceRecorder;
import edu.uw.bothell.css.dsl.MASS.prov.utils.ProvUtils;
import edu.uw.bothell.css.dsl.MASS.prov.utils.StopWatch;
import java.io.IOException;

/**
 * MNode represents a MASS compute Node and contains references to communication
 * channels with the Node which may be used to sending/receiving Messages
 * to/from the Node.
 *
 * @author mfukuda
 *
 */
@XmlRootElement(name = "node")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class MNode {

    private LogLevel logLevel;			// custom logging level for this node
    private String logFileName;			// custom logging filename for this node
    private String hostName;			// the host name of this node
    private String userName;			// for SSH login, the username - optional
    private String password;                        // WORKAROUND FOR CURRENT AUTHENTICATION ISSUES, REMEMBER TO REMOVE THIS
    private String javaHome;			// where the JVM is installed on this node - optional
    private String massHome;			// where MASS library is located - optional
    private String privateKey;		 	// path/filename containing the private key used for SSH connection to this node
    private boolean isMaster = false;	// is this the master node? - optional
    private int pid;              		// process ID
    private int port = 3400;			// the port number used for inter-node communications, defaults to 3400
    private Channel channel;            // JSCH channel
    private ObjectInputStream mainIOS;  // from remote to master
    private ObjectOutputStream mainOOS; // from master to remote
    private int resetCounter = 0;
    private Log4J2Logger logger = Log4J2Logger.getInstance();

    /**
     * Terminate all communications channels to the remote Node
     */
    public void closeMainConnection() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("closeMainConnection"), new StringBuffer("label"), true, null, null);

        try {

            mainIOS.close();
            mainOOS.close();
            Session session = channel.getSession();
            channel.disconnect();
            session.disconnect();

        } catch (Exception e) {

            logger.error("closeMainConnection error with rank[" + pid
                    + "] at " + hostName, e);
            System.exit(-1);

        }
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("closeMainConnection"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    /**
     * Get the JSCH communications channel connected to the node
     *
     * @return The JSCH communications channel
     */
    @XmlTransient
    public Channel getChannel() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getChannel"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getChannel"), procRID, new StringBuffer("channel"), channel, null, new StringBuffer("The JSCH communications channel"), true, false, false);
        StopWatch.stop(false);
        return channel;
    }

    /**
     * Return the Hostname or IP address of this Node
     *
     * @return The Hostname/IP address
     */
    @XmlElement(name = "hostname", required = true)
    public String getHostName() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getHostName"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getHostName"), procRID, new StringBuffer("hostName"), hostName, null, new StringBuffer("The Hostname/IP address"), true, false, false);
        StopWatch.stop(false);
        return hostName;
    }

    /**
     * Get the location on this node where the JVM is installed
     *
     * @return The JVM home location
     */
    @XmlElement(name = "javahome")
    public String getJavaHome() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getJavaHome"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getJavaHome"), procRID, new StringBuffer("javaHome"), javaHome, null, new StringBuffer("The JVM home location"), true, false, false);
        StopWatch.stop(false);
        return javaHome;
    }

    /**
     * Get the location where MASS (MASS.jar) resides on this node
     *
     * @return The location of MASS.jar
     */
    @XmlElement(name = "masshome")
    public String getMassHome() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getMassHome"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getMassHome"), procRID, new StringBuffer("massHome"), massHome, null, new StringBuffer("The location of MASS.jar"), true, false, false);
        StopWatch.stop(false);
        return massHome;
    }

    /**
     * Get the process ID (PID) of this Node. The process ID is a number used
     * within MASS to uniquely identify each Node. This number is assigned
     * during initialization of the Node.
     *
     * @return The unique process ID number for this Node
     */
    @XmlTransient
    public int getPid() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getPid"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getPid"), procRID, new StringBuffer("pid"), pid, null, new StringBuffer("The unique process ID number for this Node"), true, false, false);
        StopWatch.stop(false);
        return pid;
    }

    /**
     * Set the port number used to communicate with this node, for inter-node
     * socket communications
     *
     * @return The port number
     */
    public int getPort() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getPort"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getPort"), procRID, new StringBuffer("port"), port, null, new StringBuffer("The port number"), true, false, false);
        StopWatch.stop(false);
        return port;
    }

    /**
     * Get the path/filename of the private key used for SSH connections to this
     * node
     *
     * @return The private key path/filename
     */
    @XmlElement(name = "privatekey")
    public String getPrivateKey() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getPrivateKey"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getPrivateKey"), procRID, new StringBuffer("privateKey"), privateKey, null, new StringBuffer("The private key path/filename"), true, false, false);
        StopWatch.stop(false);
        return privateKey;
    }

    /**
     * Get the SSH login username for this node
     *
     * @return The login username
     */
    @XmlElement(name = "username")
    public String getUserName() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getUserName"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getUserName"), procRID, new StringBuffer("userName"), userName, null, new StringBuffer("The login username"), true, false, false);
        StopWatch.stop(false);
        return userName;
    }

    /**
     * WORKAROUND FOR CURRENT AUTHENTICATION ISSUES, REMEMBER TO REMOVE THIS
     *
     * Get the SSH login password for this node
     *
     * @return The login password
     */
    @XmlElement(name = "password")
    public String getPassword() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getPassword"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getPassword"), procRID, new StringBuffer("password"), "maskedSensitiveValue", null, new StringBuffer("The account password"), true, false, false);
        StopWatch.stop(false);
        return password;
    }

    /**
     * Perform actions necessary to initialize communications with this node
     */
    public void initialize() throws IOException, JSchException {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("initialize"), new StringBuffer("label"), true, null, null);
        try {
            // hostname should have been set already, if not, set to default
            if (getHostName() == null) {
                setHostName(InetAddress.getLocalHost().getCanonicalHostName());
            }

            // set input/output streams, then execute the command to start MProcess on the remote node
            InputStream is = channel.getInputStream();
            OutputStream os = channel.getOutputStream();
            channel.connect();

            // with input/output channels established, set object streams
            mainOOS = new ObjectOutputStream(os);
            mainOOS.flush();
            mainIOS = new ObjectInputStream(is);
        } // TODO - need better method of handling errors here rather than terminating application
        catch (Exception e) {
            logger.error("ERROR: mNode: Pid: {}", pid + ", " + hostName, e);
            System.exit(-1);
        }
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("initialize"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    /**
     * Get the master status for this node - if true, then the node represented
     * by this instance is the master node
     *
     * @return True if this is the master node, false if a remote node
     */
    @XmlElement(name = "master", required = false)
    public boolean isMaster() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("isMaster"), new StringBuffer("label"), true, null, null);
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("isMaster"), procRID, new StringBuffer("isMaster"), isMaster, null, new StringBuffer("True if this is the master node, false if a remote node"), true, false, false);
        StopWatch.stop(false);
        return isMaster;
    }

    /**
     * Get a Message send to this Node
     *
     * @return The Message received by this Node
     */
    public Message receiveMessage() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("receiveMessage"), new StringBuffer("label"), true, null, null);

        Message m = null;

        try {

            m = (Message) mainIOS.readObject();

        } catch (Exception e) {

            logger.error("receivMessage error from rank[" + pid + "] at "
                    + hostName, e);

            e.printStackTrace();

            System.exit(-1);

        }
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("receiveMessage"), procRID, new StringBuffer("m"), m, null, new StringBuffer("The Message received by this Node"), true, false, false);
        StopWatch.stop(false);
        return m;

    }

    /**
     * Send a message to the remote Node
     *
     * @param m The Message to send
     */
    public void sendMessage(Message m) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("sendMessage"), new StringBuffer("label"), true, new String[]{"m"}, new Object[]{m});

        try {

            mainOOS.writeObject(m);
            mainOOS.flush();
            resetCounter++;
            if (resetCounter == 5) {
                mainOOS.reset();
                resetCounter = 0;
            }

        } catch (Exception e) {

            logger.error("sendMessage error to rank[" + pid + "] at "
                    + hostName);

            System.exit(-1);

        }
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("sendMessage"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    /**
     * Set the JSCH channel (already established) with the remote Node
     *
     * @param channel The initialized JSCH channel connected to the remote Node
     */
    public void setChannel(Channel channel) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("setChannel"), new StringBuffer("label"), true, new String[]{"channel"}, new Object[]{channel});
        this.channel = channel;
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("setChannel"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    /**
     * Set the Hostname or IP address of this Node
     *
     * @param hostName The Hostname/IP address
     */
    public void setHostName(String hostName) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("setHostName"), new StringBuffer("label"), true, new String[]{"hostName"}, new Object[]{hostName});
        this.hostName = hostName;
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("setHostName"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    /**
     * Set the location where the JVM is installed on this node
     *
     * @param javaHome The JVM location
     */
    public void setJavaHome(String javaHome) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("setJavaHome"), new StringBuffer("label"), true, new String[]{"javaHome"}, new Object[]{javaHome});
        this.javaHome = javaHome;
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("setJavaHome"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    /**
     * Set the location where MASS (MASS.jar) resides on this node
     *
     * @param massHome The location of MASS.jar
     */
    public void setMassHome(String massHome) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("setMassHome"), new StringBuffer("label"), true, new String[]{"massHome"}, new Object[]{massHome});
        this.massHome = massHome;
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("setMassHome"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    /**
     * Set the master status for this node
     *
     * @param isMaster Set true if this instance represents the master node,
     * false if it represents a remote node
     */
    public void setMaster(boolean isMaster) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("setMaster"), new StringBuffer("label"), true, new String[]{"isMaster"}, new Object[]{isMaster});
        this.isMaster = isMaster;
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("setMaster"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    /**
     * Set the unique ID (process ID) for this Node
     *
     * @param pid The unique process ID number
     */
    public void setPid(int pid) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("setPid"), new StringBuffer("label"), true, new String[]{"pid"}, new Object[]{pid});
        this.pid = pid;
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("setPid"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    /**
     * Set the path/filename of the private key to use for SSH connections to
     * this node
     *
     * @param privateKey The path/filename of the private key to use when
     * connecting to this node
     */
    public void setPrivateKey(String privateKey) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("setPrivateKey"), new StringBuffer("label"), true, new String[]{"privateKey"}, new Object[]{privateKey});
        this.privateKey = privateKey;
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("setPrivateKey"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    /**
     * Set the SSH login username for this node
     *
     * @param userName The SSH login username
     */
    public void setUserName(String userName) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("setUserName"), new StringBuffer("label"), true, new String[]{"userName"}, new Object[]{userName});
        this.userName = userName;
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("setUserName"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    /**
     * WORKAROUND FOR CURRENT AUTHENTICATION ISSUES, REMEMBER TO REMOVE THIS
     *
     * Set the SSH login username for this node
     *
     * @param password
     */
    public void setPassword(String password) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("setPassword"), new StringBuffer("label"), true, new String[]{"password"}, new Object[]{password});
        this.password = password;
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("setPassword"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

    /**
     * Set the port number used to communicate with this node, for inter-node
     * socket communications
     *
     * @param port The port number to use
     */
    public void setPort(int port) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("setPort"), new StringBuffer("label"), true, new String[]{"port"}, new Object[]{port});
        this.port = port;
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("setPort"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }

}
