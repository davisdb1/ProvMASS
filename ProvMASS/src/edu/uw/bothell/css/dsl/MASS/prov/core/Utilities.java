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

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;

import edu.uw.bothell.css.dsl.MASS.prov.core.logging.Log4J2Logger;
import edu.uw.bothell.css.dsl.MASS.prov.store.ProvenanceRecorder;
import edu.uw.bothell.css.dsl.MASS.prov.utils.ProvUtils;
import edu.uw.bothell.css.dsl.MASS.prov.utils.StopWatch;

/**
 * MASS Utilities
 *
 * A series of helper methods for various components of MASS
 *
 * @author Dr. Munehiro Fukuda
 *
 */
class Utilities {

    private static final int SSH_PORT = 22;

    // reference to the SSH library - not initialized by default so it can be
    // replaced by a mock object for testing
    private JSch jsch = null;

    // logging
    private Log4J2Logger logger = Log4J2Logger.getInstance();

    /**
     * Obtain a communications channel with a remote host and execute a command.
     * This method returns a channel, so that consumers will have access to
     * Input and Outputstream (for interacting with the host). Also note that
     * the consumers need to explicitly call the channel.disconnect(); &
     * channel.getSession().disconnect(); when communications with the remote
     * host are no longer required.
     *
     * @param Host The hostname or IP address of the remote host
     * @param PortNumber The port number of the listener on the remote host
     * @param Command The "exec" command to execute upon connection
     * @param UserName When connecting to the remote host, use the supplied
     * username
     * @param Password When connecting to the remote host, use the supplied
     * password
     * @return An open communications channel with the remote host
     */
    @Deprecated
    protected Channel LaunchRemoteProcess(String Host, int PortNumber,
            String Command, String UserName,
            String Password) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("LaunchRemoteProcess"), new StringBuffer("label"), true, new String[]{"Host", "PortNumber", "Command", "UserName", "Password"}, new Object[]{Host, PortNumber, Command, UserName, Password});

        ChannelExec channel = null;

        try {

            // instantiate the SSH library if necessary (might be replaced
            // by a mock object during unit testing)
            if (jsch == null) {
                jsch = new JSch();
            }

            // initiate SSH connection to the remote host
            Session session = jsch.getSession(UserName, Host, PortNumber);

            // username and password will be given via UserInfo interface.
            UserInfo ui = new MyUserInfo(Password);
            session.setUserInfo(ui);

            // authenticate and complete connection sequence to the remote host
            session.connect();

            // set the command to be executed upon channel connection
            channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(Command);

        } catch (Exception e) {

            // "display" the error message
            System.err.println(e);

            // TODO - should we return NULL here to prevent the return of a partially connected channel?
        }
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("LaunchRemoteProcess"), procRID, new StringBuffer("channel"), channel, null, new StringBuffer("An open communications channel with the remote host"), true, false, false);
        StopWatch.stop(false);
        return channel;

    }

    protected Channel LaunchRemoteProcess(String Command, MNode remoteNode) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("LaunchRemoteProcess"), new StringBuffer("label"), true, new String[]{"Command", "remoteNode"}, new Object[]{Command, remoteNode});

        // must provide required parameters
        // TODO - should throw IllegalArgumentException instead of returning NULL
        if (Command == null || Command.length() == 0) //return null;
        {
            throw new IllegalArgumentException("Command is empty or equal to null");
        }
        if (remoteNode == null) //return null;
        {
            throw new IllegalArgumentException("remoteNode is equal to null");
        }

        ChannelExec channel = null;
        Properties config = new Properties();

        try {
            // instantiate the SSH library if necessary (might be replaced
            // by a mock object during unit testing)
            if (jsch == null) {
                jsch = new JSch();
            }

            // add reference to SSH key
            if (remoteNode.getPrivateKey() != null) {
                logger.debug("Adding private key: {}", remoteNode.getPrivateKey());
                jsch.addIdentity(remoteNode.getPrivateKey());
            }
            // set SSH connection properties
            logger.debug("Setting hostname to {}", remoteNode.getHostName());
            logger.debug("Setting username to {}", remoteNode.getUserName());
            logger.debug("Connecting to port {}", SSH_PORT);
            Session session = jsch.getSession(remoteNode.getUserName(), remoteNode.getHostName(), SSH_PORT);
            if (remoteNode.getPrivateKey() == null) {
//                System.err.println("No Private Key File Specified! Setting ordered "
//                        + "preferred authentication to: publickey,keyboard-interactive,password");
                logger.debug("No Private Key File Specified! Setting ordered "
                        + "preferred authentication to: publickey,keyboard-interactive,password");
                config.put("PreferredAuthentications", "publickey,keyboard-interactive,password");
                if (remoteNode.getPassword() != null) {
//                    System.err.println("Manually setting session password from configuration...");
                    logger.debug("Manually setting session password from configuration...");
                    session.setPassword(remoteNode.getPassword());
                }
            }
            logger.debug("Disabling string host key checking");
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);

            // authenticate and complete connection sequence to the remote host
            logger.debug("Attempting to connect and authenticate...");
            session.connect();
            logger.debug("Connected!");

            // set the command to be executed upon channel connection
            logger.debug("Executing remote command: {}", Command);
            channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(Command);
            logger.debug("Command executed!");
        } catch (Exception e) {
            // log the error message
            logger.error("Caught exception while attempting to connect/authenticate/execute on remote node", e);
            if(channel != null){
                channel.disconnect();
            }
            // TODO - should we return NULL here to prevent the return of a partially connected channel?
            ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("LaunchRemoteProcess"), procRID, new StringBuffer("channel"), channel, null, null, true, false, false);
            StopWatch.stop(false);
            return null;
        }
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("LaunchRemoteProcess"), procRID, new StringBuffer("channel"), channel, null, null, true, false, false);
        StopWatch.stop(false);
        return channel;
    }

    /**
     * User credentials for initiating remote connections using SSH
     *
     * @author Dr. Munehiro Fukuda
     */
    private class MyUserInfo implements UserInfo {

        // Private data members
        private String _passwd = null;	// Users password

        // Constructor sets up password
        public MyUserInfo(String passwd) {
            StopWatch.start(false);
            StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("MyUserInfo"), new StringBuffer("label"), true, new String[]{"passwd"}, new Object[]{passwd});
            this._passwd = passwd;
            ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("MyUserInfo"), procRID, null, null, null, null, true, false, false);
            StopWatch.stop(false);
        }

        // Because passphrase does not apply use null
        public String getPassphrase() {
            StopWatch.start(false);
            StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getPassphrase"), new StringBuffer("label"), true, null, null);
            ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getPassphrase"), procRID, null, null, null, null, true, false, false);
            StopWatch.stop(false);
            return null;
        }

        ;
	
    	// Returns the password of the user
    	public String getPassword() {
            StopWatch.start(false);
            StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getPassword"), new StringBuffer("label"), true, null, null);
            ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getPassword"), procRID, new StringBuffer("_passwd"), _passwd, null, new StringBuffer("the password of the user"), true, false, false);
            StopWatch.stop(false);
            return _passwd;
        }

        ;
	
    	// You may only set password during construction of UserInfo
    	public boolean promptPassword(String Message) {
            StopWatch.start(false);
            StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("promptPassword"), new StringBuffer("label"), true, new String[]{"Message"}, new Object[]{Message});
            ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("promptPassword"), procRID, new StringBuffer("true"), true, null, null, true, false, false);
            StopWatch.stop(false);
            return true;
        }

        ;
	
    	// Because passphrase does not apply this function simply returns true
    	public boolean promptPassphrase(String message) {
            StopWatch.start(false);
            StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("promptPassphrase"), new StringBuffer("label"), true, new String[]{"message"}, new Object[]{message});
            ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("promptPassphrase"), procRID, new StringBuffer("true"), true, null, null, true, false, false);
            StopWatch.stop(false);
            return true;
        }

        ;
	
    	// Because the program is run remotely we don't want to prompt the user
    	public boolean promptYesNo(String message) {
            StopWatch.start(false);
            StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("promptYesNo"), new StringBuffer("label"), true, new String[]{"message"}, new Object[]{message});
            ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("promptYesNo"), procRID, new StringBuffer("true"), true, null, null, true, false, false);
            StopWatch.stop(false);
            return true;
        }

        ;
    	public void showMessage(String message) {
            StopWatch.start(false);
            StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("showMessage"), new StringBuffer("label"), true, new String[]{"message"}, new Object[]{message});
            ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("showMessage"), procRID, null, null, null, null, true, false, false);
            StopWatch.stop(false);
        }
    ;

    }

    /**
     * Get the hostname or IP address of this node
     * @return The network address of this node
     */
    public String getLocalHostname() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getLocalHostname"), new StringBuffer("label"), true, null, null);
        String hostname = null;
        try {
            hostname = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            // no biggie, at least not now
        }
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("getLocalHostname"), procRID, new StringBuffer("hostname"), hostname, null, new StringBuffer("The network address of this node"), true, false, false);
        StopWatch.stop(false);
        return hostname;
    }
}
