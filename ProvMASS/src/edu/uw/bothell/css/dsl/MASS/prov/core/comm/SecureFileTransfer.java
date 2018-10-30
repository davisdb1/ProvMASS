package edu.uw.bothell.css.dsl.MASS.prov.core.comm;
// PERFORMANCE DOCUMENTED

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.SftpProgressMonitor;
import edu.uw.bothell.css.dsl.MASS.prov.store.ProvenanceRecorder;
import edu.uw.bothell.css.dsl.MASS.prov.utils.ProvUtils;
import edu.uw.bothell.css.dsl.MASS.prov.utils.StopWatch;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

/**
 * Provides abbreviated SSF/FTP functionality. This includes
 * uploading/downloading files and execution of commands on a remote machine.
 *
 * @author Delmar B. Davis
 */
public class SecureFileTransfer {

    // <editor-fold defaultstate="collapsed" desc="Members">
    private final int port = 22;
    private Session session;
    private final int BUFFER_SIZE = 1024;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Construction">
    /**
     * Responsible for initialization of members and construction of
     * SecureFileTransfer objects
     */
    public SecureFileTransfer() {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("SecureFileTransfer"), new StringBuffer("label"), true, null, null);
        session = null;
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("SecureFileTransfer"), procRID, null, null, null, null, true, false, false);
        StopWatch.stop(false);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Remote Operations">
    /**
     * Tests for the availability of an SFTP connection with host
     *
     * @param timeOut - The amount of time, in milliseconds, to wait for the
     * connection to be established. Setting this value less than zero will
     * result in connect being called without a timeout
     * @param hostname - The name of the host machine to connect to
     * @param username - The user's login username
     * @param password - The user's login password
     * @return True if the connection succeeded, otherwise false
     */
    public boolean testConnection(int timeOut, String hostname, String username,
            char[] password) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("testConnection"), new StringBuffer("label"), true, new String[]{"timeOut", "hostname", "username", "password"}, new Object[]{timeOut, hostname, username, password});
        boolean success = true;
        try {
            JSch jsch = new JSch();

            // apply user info to connection attempt
            session = jsch.getSession(username, hostname, port);
            session.setPassword(String.valueOf(password));
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            if (timeOut > 0) {
                session.connect(timeOut);
            } else {
                session.connect();
            }
            success = true;
        } catch (JSchException e) {
            success = false;
        } finally {
            if (session != null && session.isConnected()) {
                session.disconnect();
            }
        }
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("testConnection"), procRID, new StringBuffer("success"), success, null, new StringBuffer("True if the connection succeeded, otherwise false"), true, false, false);
        StopWatch.stop(false);
        return success;
    }

    /**
     * Uploads a file to specified directory on remote host.
     *
     * @param filename - path to source file
     * @param remoteDirectory - path to target file. Note: Do not set this to ~,
     * as this is interpreted as ~/~/. Instead, pass a null reference to upload
     * to the default directory of the user account specified by username.
     * @param hostname - The name of the host machine to connect to
     * @param username - The user's login username
     * @param password - The user's login password
     * @return True if the upload succeeded, otherwise false
     * @throws JSchException
     * @throws FileNotFoundException
     * @throws SftpException
     */
    public boolean uploadFile(String filename, String remoteDirectory,
            String hostname, String username, char[] password)
            throws JSchException, FileNotFoundException, SftpException {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("uploadFile"), new StringBuffer("label"), true, new String[]{"filename", "remoteDirectory", "hostname", "username", "password"}, new Object[]{filename, remoteDirectory, hostname, username, password});
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("uploadFile"), procRID, new StringBuffer("uploadFile(filename,_remoteDirectory,_hostname,_username,_password,_null)"), uploadFile(filename, remoteDirectory, hostname, username,
                password, null), null, new StringBuffer("True if the upload succeeded, otherwise false"), true, false, false);
        StopWatch.stop(false);
        return uploadFile(filename, remoteDirectory, hostname, username,
                password, null);
    }

    /**
     * Uploads a file to specified directory on remote host.
     *
     * @param filename - path to source file
     * @param remoteDirectory - path to target file. Note: Do not set this to ~,
     * as this is interpreted as ~/~/. Instead, pass a null reference to upload
     * to the default directory of the user account specified by username.
     * @param hostname - The name of the host machine to connect to
     * @param username - The user's login username
     * @param password - The user's login password
     * @param progressMonitor - An optional handler for progress reporting on
     * the upload operation
     * @return True if the upload succeeded, otherwise false
     * @throws JSchException
     * @throws FileNotFoundException
     * @throws SftpException
     */
    public boolean uploadFile(String filename, String remoteDirectory,
            String hostname, String username, char[] password,
            SftpProgressMonitor progressMonitor) throws JSchException, FileNotFoundException, SftpException {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("uploadFile"), new StringBuffer("label"), true, new String[]{"filename", "remoteDirectory", "hostname", "username", "password", "progressMonitor"}, new Object[]{filename, remoteDirectory, hostname, username, password, progressMonitor});
        boolean success = true;
        if (remoteDirectory == null) {
            remoteDirectory = "";
        }
        try {
            JSch jsch = new JSch();
            // apply user info to connection attempt
            session = jsch.getSession(username, hostname, port);
            session.setPassword(String.valueOf(password));
            // generic setting up
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();
            // upload file
            ChannelSftp channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();
            if (filename != null) {
                File f = new File(filename);
                if (f.exists()) {
                    if (progressMonitor != null) {
                        channelSftp.put(new FileInputStream(f),
                                remoteDirectory + f.getName(), progressMonitor,
                                ChannelSftp.OVERWRITE);
                    } else {
                        channelSftp.put(new FileInputStream(f),
                                remoteDirectory + f.getName(),
                                ChannelSftp.OVERWRITE);
                    }
                } else {
                    success = false;
                }
            } else {
                success = false;
            }
            channelSftp.disconnect();
        } catch (JSchException | FileNotFoundException | SftpException e) {
            success = false;
            throw e;
        } finally {
            if (session != null && session.isConnected()) {
                session.disconnect();
            }
        }
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("uploadFile"), procRID, new StringBuffer("success"), success, null, new StringBuffer("True if the upload succeeded, otherwise false"), true, false, false);
        StopWatch.stop(false);
        return success;
    }

    /**
     * Downloads a specified file from the remote machine.
     *
     * @param remoteFilePath - The full path to the file on the remote machine.
     * This is a path (including the file name) relative to the root connection
     * directory for the user.
     * @param localFilePath - The location on the local machine where the file
     * should be written to
     * @param hostname - The name of the host machine to connect to
     * @param username - The user's login username
     * @param password - The user's login password
     * @return True if the file was downloaded successfully, otherwise false
     * @throws JSchException
     * @throws SftpException
     */
    public boolean downloadFile(String remoteFilePath,
            String localFilePath, String hostname, String username,
            char[] password) throws JSchException, SftpException {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("downloadFile"), new StringBuffer("label"), true, new String[]{"remoteFilePath", "localFilePath", "hostname", "username", "password"}, new Object[]{remoteFilePath, localFilePath, hostname, username, password});
        boolean success = true;

        try {
            JSch jsch = new JSch();
            // apply user info to connection attempt
            session = jsch.getSession(username, hostname, port);
            session.setPassword(String.valueOf(password));
            // generic setting up
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();
            // download file
            ChannelSftp channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();
            channelSftp.get(remoteFilePath, localFilePath);
            channelSftp.disconnect();
        } catch (JSchException | SftpException e) {
            success = false;
            throw e;
        } finally {
            if (session != null && session.isConnected()) {
                session.disconnect();
            }
        }
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("downloadFile"), procRID, new StringBuffer("success"), success, null, new StringBuffer("True if the file was downloaded successfully, otherwise false"), true, false, false);
        StopWatch.stop(false);
        return success;
    }

    /**
     * Executes a command on the remote machine.
     *
     * @param command - The command to execute on the remote machine.
     * @param hostname - The name of the host machine to connect to
     * @param username - The user's login username
     * @param password - The user's login password
     * @param readInputStream - Determines whether the result of the execution
     * should be read. If the command is executed through nohup, it is possible
     * to disconnect and leave the command running in the background. If this is
     * set to true, the command will be executed synchronously from the
     * viewpoint of the local machine, regardless of whether it was executed
     * through nohup.
     * @return True if the command was executed successfully, otherwise false
     */
    public boolean executeCommand(String command, String hostname, String username, char[] password, boolean readInputStream) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("executeCommand"), new StringBuffer("label"), true, new String[]{"command", "hostname", "username", "password", "readInputStream"}, new Object[]{command, hostname, username, password, readInputStream});
        boolean success = false;
        try {
            JSch jsch = new JSch();
            // apply user info to connection attempt
            session = jsch.getSession(username, hostname, port);
            session.setPassword(String.valueOf(password));
            // generic setting up
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();
            // setup command
            ChannelExec cExec = (ChannelExec) session.openChannel("exec");
            cExec.setCommand(command);
            // setup I/O
            if (readInputStream) {
                cExec.setInputStream(null);
                cExec.setOutputStream(System.out);
                cExec.setErrStream(System.err);

                InputStream in = cExec.getInputStream();
                cExec.connect();
                success = readInputStream(in, cExec);
            } else {
                cExec.connect();
                success = true;
            }
        } catch (JSchException | IOException e) {
        } finally {
            if (session != null && session.isConnected()) {
                session.disconnect();
                System.out.println("disconnected from server @ " + (new Date()));
            }
        }
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("executeCommand"), procRID, new StringBuffer("success"), success, null, new StringBuffer("True if the command was executed successfully, otherwise false"), true, false, false);
        StopWatch.stop(false);
        return success;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Helper Functions">
    // helper function for the execute command only
    private boolean readInputStream(InputStream in, ChannelExec cExec) {
        StopWatch.start(false);
        StringBuffer procRID = ProvenanceRecorder.documentProcedure(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("readInputStream"), new StringBuffer("label"), true, new String[]{"in", "cExec"}, new Object[]{in, cExec});
        boolean success = false;
        try {
            byte[] incomingBytes = new byte[BUFFER_SIZE];
            while (true) {
                // bytes are available to read from stream
                while (in.available() > 0) {
                    int i = in.read(incomingBytes, 0, BUFFER_SIZE);
                    // no bytes were read from the input stream, so stop
                    if (i < 0) {
                        break;
                    }
                    System.out.print(new String(incomingBytes, 0, i));
                }
                // channel is closed
                if (cExec.isClosed()) {
                    // no more bytes left to read
                    if (in.available() == 0) {
                        // no bytes available, get exit status from command run
                        System.out.println("exit-status: "
                                + cExec.getExitStatus());
                        // determine whether the command was successful or not
                        success = cExec.getExitStatus() == 0;
                        break;
                    }
                }
                try { // separate attempts by one second
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                }
            }
        } catch (IOException e) {
        }
        ProvenanceRecorder.endProcedureDocumentation(ProvUtils.getStoreOfCurrentThread(), this, new StringBuffer("readInputStream"), procRID, new StringBuffer("success"), success, null, null, true, false, false);
        StopWatch.stop(false);
        return success;
    }
    // </editor-fold>
}
