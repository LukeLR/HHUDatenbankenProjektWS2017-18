package de.hhu.cs.dbs.internship.project;

import java.util.logging.Logger;

import com.alexanderthelen.applicationkit.database.Connection;
import com.alexanderthelen.applicationkit.gui.MasterDetailViewController;
import com.alexanderthelen.applicationkit.gui.WindowController;
import de.hhu.cs.dbs.internship.project.gui.AuthenticationViewController;
import de.hhu.cs.dbs.internship.project.gui.LoginViewController;
import de.hhu.cs.dbs.internship.project.gui.MasterViewController;
import de.hhu.cs.dbs.internship.project.gui.RegistrationViewController;

public class Project extends com.alexanderthelen.applicationkit.Application {
	@SuppressWarnings("restriction")
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start() throws Exception {
		Logger logger = Logger.getLogger(this.getClass().getName());
		logger.info("Startup programs");
		setConnection(new Connection("jdbc:sqlite:database.sqlite"));

		WindowController mainWindowController = WindowController.createWithName("window");
		mainWindowController.setTitle("Projekt");

		AuthenticationViewController authenticationViewController = AuthenticationViewController
				.createWithName("authentication");
		authenticationViewController.setTitle("Authentifizierung");

		LoginViewController loginViewController = LoginViewController.createWithName("login");
		loginViewController.setTitle("Anmeldung");
		authenticationViewController.setLoginViewController(loginViewController);

		RegistrationViewController registrationViewController = RegistrationViewController
				.createWithName("registration");
		registrationViewController.setTitle("Registrierung");
		authenticationViewController.setRegistrationViewController(registrationViewController);

		MasterDetailViewController mainViewController = MasterDetailViewController.createWithName("main");
		mainViewController.setTitle("Projekt");
		mainViewController.setMasterViewController(MasterViewController.createWithName("master"));
		authenticationViewController.setMainViewController(mainViewController);

		mainWindowController.setViewController(authenticationViewController);
		setWindowController(mainWindowController);
		show();
	}
}