package de.hhu.cs.dbs.internship.project.gui;

import com.alexanderthelen.applicationkit.database.Data;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;

public class AuthenticationViewController extends com.alexanderthelen.applicationkit.gui.AuthenticationViewController {
    protected AuthenticationViewController(String name) {
        super(name);
    }

    public static AuthenticationViewController createWithName(String name) throws IOException {
        AuthenticationViewController viewController = new AuthenticationViewController(name);
        viewController.loadView();
        return viewController;
    }

    @Override
    public void loginUser(Data data) throws SQLException {
    	Logger logger = Logger.getLogger(this.getClass().getName() + " Login event");
    	logger.info("User tried to login: " + data.toString());
        throw new SQLException(getClass().getName() + ".loginUser(Data) nicht implementiert.");
    }

    @Override
    public void registerUser(Data data) throws SQLException {
        throw new SQLException(getClass().getName() + ".registerUser(Data) nicht implementiert.");
    }
}
