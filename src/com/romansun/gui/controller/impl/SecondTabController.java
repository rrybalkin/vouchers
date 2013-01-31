package com.romansun.gui.controller.impl;

import java.net.URL;
import java.sql.SQLException;
import java.util.Collection;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import com.romansun.gui.controller.AbstractController;
import com.romansun.hibernate.logic.Association;
import com.romansun.hibernate.logic.Visitor;

public class SecondTabController extends AbstractController implements Initializable {

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			Collection<Association> associations = dao.getAssociationDAO().getAllAssociations();
			cbGroup.getItems().clear();
			cbGroup.getItems().setAll(associations);
			cbGroup.getSelectionModel().selectFirst();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private TextField txtLastname;
	@FXML
	private TextField txtFirstname;
	@FXML
	private TextField txtMiddlename;
	@FXML
	private ComboBox<Association> cbGroup;
	@FXML
	private TextArea taVisitorDescription;
	@FXML
	private Button btnAddVisitor;
	@FXML
	private TextField txtGroupName;
	@FXML
	private TextArea taGroupDescription;
	@FXML
	private Button btnAddGroup;
	
	@FXML
	private void addVisitor(ActionEvent event) {
		Visitor visitor = new Visitor();
		String lastname = txtLastname.getText();
		String firstname = txtFirstname.getText();
		String middlename = txtMiddlename.getText();
		Association group = cbGroup.getValue();
		String description = taVisitorDescription.getText();
		
		if (!lastname.isEmpty() && !firstname.isEmpty() && !middlename.isEmpty()) {
			visitor.setLastname(lastname);
			visitor.setFirstname(firstname);
			visitor.setMiddlename(middlename);
		}else{
			System.out.println("Необходимо заполнить обязательные поля!");
		}
		
		visitor.setAssociation(group);
		visitor.setDescription(description);
		
		try {
			dao.getVisitorDAO().addVisitor(visitor);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private void addGroup(ActionEvent event) {
		
	}
}
