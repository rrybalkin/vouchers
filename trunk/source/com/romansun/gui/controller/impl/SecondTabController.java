package com.romansun.gui.controller.impl;

import java.net.URL;
import java.sql.SQLException;
import java.util.Collection;
import java.util.ResourceBundle;

import name.antonsmirnov.javafx.dialog.Dialog;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import com.romansun.gui.controller.AbstractController;
import com.romansun.hibernate.logic.Association;
import com.romansun.hibernate.logic.Talon;
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
	private TextField txtGroupName;
	@FXML
	private TextArea taGroupDescription;
	
	@FXML
	protected void addVisitor(ActionEvent event) {
		Visitor visitor = new Visitor();
		String lastname = txtLastname.getText();
		System.out.println(lastname);
		String firstname = txtFirstname.getText();
		System.out.println(firstname);
		String middlename = txtMiddlename.getText();
		System.out.println(middlename);
		Association group = cbGroup.getValue();
		String description = taVisitorDescription.getText();
		
		if (!lastname.isEmpty() && !firstname.isEmpty() && !middlename.isEmpty()) {
			visitor.setLastname(lastname);
			visitor.setFirstname(firstname);
			visitor.setMiddlename(middlename);
			visitor.setAssociation(group);
			visitor.setDescription(description);
			
			Talon talon = new Talon();
			talon.setCount_dinner(0);
			talon.setCount_lunch(0);
			visitor.setTalon(talon);
			try {
				dao.getTalonDAO().addTalon(talon);
				dao.getVisitorDAO().addVisitor(visitor);
				Dialog.showInfo("Ура, все добавилось", "Новый посетитель успешно добавлен в базу данных!");
			}catch (SQLException e) {
				e.printStackTrace();
			}finally{
				reset();
			}
		}else{
			Dialog.showError("Упс, Ошибка", "Необходимо заполнить обзательные поля,\nпомеченные звездочкой *!");
		}
	}
	
	@FXML
	protected void addGroup(ActionEvent event) {
		Association group = new Association();
		String name = txtGroupName.getText();
		String description = taGroupDescription.getText();
		if(!name.isEmpty()) {
			group.setName(name);
			group.setDescription(description);	
			try {
				dao.getAssociationDAO().addAssociation(group);
				Dialog.showInfo("Ура, все добавилось", "Новая группа успешно добавлена в базу данных!");
			} catch (SQLException e) {
				e.printStackTrace();
			}finally{
				reset();
			}
		}
		else {
			Dialog.showError("Упс, Ошибка", "Необходимо заполнить обзательные поля,\nпомеченные звездочкой *!");
		}
	}
	
	// Reset text in all fields
	private void reset() {
		txtLastname.clear();
		txtFirstname.clear();
		txtMiddlename.clear();
		cbGroup.getSelectionModel().selectFirst();
		taVisitorDescription.clear();
		
		txtGroupName.clear();
		taGroupDescription.clear();
	}
}
