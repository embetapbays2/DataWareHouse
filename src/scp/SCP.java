package scp;

import com.chilkatsoft.CkGlobal;
import com.chilkatsoft.CkScp;
import com.chilkatsoft.CkSsh;

public class SCP {
	static {
	    try {
	        System.loadLibrary("chilkat");
	    } catch (UnsatisfiedLinkError e) {
	      System.err.println("Native code library failed to load.\n" + e);
	      System.exit(1);
	    }
	  }

	  public static void scpDownload(String hostname, int port, String user, String pw, String remotePath, String localPath)
	  {
	    // This example requires the Chilkat API to have been previously unlocked.
	    // See Global Unlock Sample for sample code.

	    CkSsh ssh = new CkSsh();
	    
	    CkGlobal glob = new CkGlobal();
	    glob.UnlockBundle("Anything for 30-day trial");

	    // Hostname may be an IP address or hostname:
//	    String hostname = "www.some-ssh-server.com";
//	    int port = 22;

	    // Connect to an SSH server:
	    boolean success = ssh.Connect(hostname,port);
	    if (success != true) {
	        System.out.println(ssh.lastErrorText());
	        return;
	        }

	    // Wait a max of 5 seconds when reading responses..
	    ssh.put_IdleTimeoutMs(5000);

	    // Authenticate using login/password:
	    success = ssh.AuthenticatePw(user,pw);
	    if (success != true) {
	        System.out.println(ssh.lastErrorText());
	        return;
	        }

	    // Once the SSH object is connected and authenticated, we use it
	    // in our SCP object.
	    CkScp scp = new CkScp();

	    success = scp.UseSsh(ssh);
	    if (success != true) {
	        System.out.println(scp.lastErrorText());
	        return;
	        }

	    // This downloads a file from the "testApp/logs/" subdirectory (relative to the SSH user account's HOME directory).  
	    // For example, if the HOME directory is /Users/chilkat, then the following downloads
	    // /Users/chilkat/testApp/logs/test1.log
//	    String remotePath = "testApp/logs/test1.log";
//	    String localPath = "c:/testApp/logs/test1.log";
	    
	 // Download synchronization modes:
	    // mode=0: Download all files
	    // mode=1: Download all files that do not exist on the local filesystem.
	    // mode=2: Download newer or non-existant files.
	    // mode=3: Download only newer files.  
	    //         If a file does not already exist on the local filesystem, it is not downloaded from the server.
	    // mode=5: Download only missing files or files with size differences.
	    // mode=6: Same as mode 5, but also download newer files.
	    int mode = 2;
	    
	    
    // Set the SyncMustMatch property to "*.pem" to download only .pem files
//	    scp.put_SyncMustMatch("*.pem");
	    scp.put_SyncMustMatch("sinhvien_chieu*");
	    success = scp.SyncTreeDownload(remotePath,localPath,mode,false);
	    if (success != true) {
	        System.out.println(scp.lastErrorText());
	        return;
	        }

	    // The following call to DownloadFile specifies an absolute file path on the SSH server:
//	    remotePath = "/Users/chilkat/Documents/gecko.jpg";
//	    localPath = "c:/temp/images/gecko.jpg";
//	    success = scp.DownloadFile(remotePath,localPath);
//	    if (success != true) {
//	        System.out.println(scp.lastErrorText());
//	        return;
//	        }

	    System.out.println("SCP download file success.");

	    // Disconnect
	    ssh.Disconnect();
	  }public static void main(String[] args) {
		  String hostname = "drive.ecepvn.org";
		  int port = 2227;
		  String user ="guest_access";
		  String pw ="123456";
		  String  remotePath="/volume1/ECEP/song.nguyen/DW_2020/data";
		  String localPath = "D:\\ztinhdeptrai";
		scpDownload(hostname, port, user, pw, remotePath, localPath);
	}
}
