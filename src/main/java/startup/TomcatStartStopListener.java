package startup;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class TomcatStartStopListener implements ServletContextListener {

	public void contextInitialized(ServletContextEvent sce) {
		System.out.println("WE ARE STARTING UP");
		System.out.println("WE ARE STARTING UP");
		System.out.println("WE ARE STARTING UP");
		System.out.println("WE ARE STARTING UP");
		System.out.println("WE ARE STARTING UP");
		System.out.println("WE ARE STARTING UP");
		System.out.println("WE ARE STARTING UP");
		System.out.println("WE ARE STARTING UP");
		System.out.println("WE ARE STARTING UP");
		System.out.println("WE ARE STARTING UP");
		System.out.println("WE ARE STARTING UP");
		System.out.println("WE ARE STARTING UP");
		System.out.println("WE ARE STARTING UP");
		System.out.println(System.getenv("JDBC_DATABASE_URL"));
		System.out.println(System.getProperty("JDBC_DATABASE_URL"));
	}

	public void contextDestroyed(ServletContextEvent sce) {

	}

}
