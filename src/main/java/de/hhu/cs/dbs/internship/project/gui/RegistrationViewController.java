package de.hhu.cs.dbs.internship.project.gui;

import com.alexanderthelen.applicationkit.database.Data;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.ArrayList;

public class RegistrationViewController extends com.alexanderthelen.applicationkit.gui.RegistrationViewController {
    @SuppressWarnings("restriction")
	@FXML
    protected TextField firstNameTextField;
    @SuppressWarnings("restriction")
	@FXML
    protected TextField lastNameTextField;
    @SuppressWarnings("restriction")
	@FXML
    protected TextField eMailTextField;
    @SuppressWarnings("restriction")
	@FXML
    protected PasswordField password1PasswordField;
    @SuppressWarnings("restriction")
    @FXML
    protected PasswordField password2PasswordField;
    @SuppressWarnings("restriction")
    @FXML
    protected TextField streetTextField;
    @SuppressWarnings("restriction")
    @FXML
    protected TextField houseNumberTextField;
    @SuppressWarnings("restriction")
    @FXML
    protected TextField zipCodeTextField;
    @SuppressWarnings("restriction")
    @FXML
    protected TextField cityTextField;
    

    protected RegistrationViewController(String name) {
        super(name, RegistrationViewController.class.getResource("RegistrationView.fxml"));
    }

    public static RegistrationViewController createWithName(String name) throws IOException {
        RegistrationViewController viewController = new RegistrationViewController(name);
        viewController.loadView();
        return viewController;
    }

    @SuppressWarnings("restriction")
    @Override
    @FXML
    protected void initialize() {

    }

    @SuppressWarnings("restriction")
    @Override
    public ArrayList<Node> getInputNodes() {
        ArrayList<Node> inputNodes = new ArrayList<>();
        inputNodes.add(firstNameTextField);
        inputNodes.add(lastNameTextField);
        inputNodes.add(eMailTextField);
        inputNodes.add(password1PasswordField);
        inputNodes.add(password2PasswordField);
        inputNodes.add(streetTextField);
        inputNodes.add(houseNumberTextField);
        inputNodes.add(zipCodeTextField);
        inputNodes.add(cityTextField);
        return inputNodes;
    }

    @SuppressWarnings("restriction")
    @Override
    public Data getInputData() {
        Data data = new Data();
        data.put("firstName", firstNameTextField.getText());
        data.put("lastName", lastNameTextField.getText());
        data.put("eMail", eMailTextField.getText());
        data.put("password1", password1PasswordField.getText());
        data.put("password2", password2PasswordField.getText());
        data.put("street", streetTextField.getText());
        data.put("houseNumber", houseNumberTextField.getText());
        data.put("zipCode", zipCodeTextField.getText());
        data.put("city", cityTextField.getText());
        return data;
    }
}
