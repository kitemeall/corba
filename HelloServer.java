import HelloApp.*;
import org.omg.CosNaming.*;
import org.omg.CosNaming.NamingContextPackage.*;
import org.omg.CORBA.*;
import org.omg.PortableServer.*;
import org.omg.PortableServer.POA;
import java.util.Properties;
import java.lang.Thread;
class HelloImpl extends HelloPOA 
{
	private ORB orb;
	public void setORB(ORB orb_val) 
	{
		orb = orb_val; 
	}
	public String sayHello( String str) 
	{
		System.out.println("Server recieved message: " + str);
		try {
    		Thread.sleep(5000);                 //1000 milliseconds is one second.
		} catch(InterruptedException ex) {
    		Thread.currentThread().interrupt();
		}




		return  String.valueOf(str.length());
	}
	public void shutdown() 
	{
		orb.shutdown(true);
	}
}
public class HelloServer 
{
	public static void main(String args[]) 
	{
		try
		{
			ORB orb = ORB.init(args, null);
			POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
			rootpoa.the_POAManager().activate();
			HelloImpl helloImpl = new HelloImpl();
			helloImpl.setORB(orb); 
			org.omg.CORBA.Object ref = rootpoa.servant_to_reference(helloImpl);
			Hello href = HelloHelper.narrow(ref);
			org.omg.CORBA.Object objRef =orb.resolve_initial_references("NameService");
	      // Use NamingContextExt which is part of the Interoperable
	      // Naming Service (INS) specification.
	      NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
	
	      // bind the Object Reference in Naming
	      String name = "Hello";
	      NameComponent path[] = ncRef.to_name( name );
	      ncRef.rebind(path, href);

	      System.out.println("HelloServer ready and waiting ...");

	      // wait for invocations from clients
	      orb.run();
	    } 
		
	      catch (Exception e) {
	        System.err.println("ERROR: " + e);
	        e.printStackTrace(System.out);
	      }
		  
     System.out.println("HelloServer Exiting ...");
	  }
	}
