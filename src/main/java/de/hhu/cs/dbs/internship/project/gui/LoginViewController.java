package de.hhu.cs.dbs.internship.project.gui;

import com.alexanderthelen.applicationkit.database.Data;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.ArrayList;

public class LoginViewController extends com.alexanderthelen.applicationkit.gui.LoginViewController {
    @SuppressWarnings("restriction")
	@FXML
    protected TextField emailTextField;
    @SuppressWarnings("restriction")
	@FXML
    protected PasswordField passwordPasswordField;

    protected LoginViewController(String name) {
        super(name, LoginViewController.class.getResource("LoginView.fxml"));
    }

    public static LoginViewController createWithName(String name) throws IOException {
        LoginViewController viewController = new LoginViewController(name);
        viewController.loadView();
        return viewController;
    }

    @SuppressWarnings("restriction")
	@Override
    public ArrayList<Node> getInputNodes() {
        ArrayList<Node> inputNodes = new ArrayList<>();
        inputNodes.add(emailTextField);
        inputNodes.add(passwordPasswordField);
        return inputNodes;
    }

    @SuppressWarnings("restriction")
	@Override
    public Data getInputData() {
        Data data = new Data();
        data.put("email", emailTextField.getText());
        data.put("password", passwordPasswordField.getText());
        return data;
    }
}
