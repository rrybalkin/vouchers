package com.romansun.gui.controller.impl;

import java.net.URL;
import java.util.Collection;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import com.romansun.gui.controller.AbstractController;
import com.romansun.gui.utils.Dialog;
import com.romansun.hibernate.entity.Association;
import com.romansun.hibernate.entity.Talon;
import com.romansun.hibernate.entity.Visitor;
import com.romansun.utils.AdditionalUtils;

public class SecondTabController extends AbstractController implements Initializable {
	
	private final static Logger LOG = Logger.getLogger(SecondTabController.class);
	
	private static Observable observable = new Observable() {
		public void notifyObservers(Object arg) {
			setChanged();
			super.notifyObservers(arg);
		}
	};
	
	public static void addObserver(Observer o) {
		observable.addObserver(o);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		loadGroups();
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
	private ComboBox<Association> cbDelGroups;
	@FXML
	private Label lblDelGroup;
	
	@FXML
	protected void addVisitor(ActionEvent event) {
		Visitor visitor = new Visitor();
		String lastname = txtLastname.getText();
		String firstname = txtFirstname.getText();
		String middlename = txtMiddlename.getText();
		Association group = cbGroup.getValue();
		String description = taVisitorDescription.getText();
		
		if (!lastname.isEmpty() && !firstname.isEmpty()) {
			visitor.setLastname(AdditionalUtils.upFirst(lastname));
			visitor.setFirstname(AdditionalUtils.upFirst(firstname));
			visitor.setMiddlename(AdditionalUtils.upFirst(middlename));
			visitor.setAssociation(group);
			visitor.setDescription(description);
			
			Talon talon = new Talon();
			talon.setCount_dinner(0);
			talon.setCntOfLunches(0);
			visitor.setTalon(talon);
			try {
				dao.getTalonDAO().add(talon);
				visitorsDAO.add(visitor);
				Dialog.showInfo("New visitor created");
				observable.notifyObservers();
				LOG.info("New visitor = [" + visitor + "] was successfully added");
			}catch (Exception e) {
				LOG.error("Error while adding new visitor = [" + visitor + "]: ", e);
			}finally{
				reset("visitor");
			}
		}else{
			Dialog.showError("Error");
		}
	}
	
	@FXML
	protected void addGroup(ActionEvent event) {
		Association group = new Association();
		String name = txtGroupName.getText();
		String description = taGroupDescription.getText();
		if(!name.isEmpty()) {
			group.setName(AdditionalUtils.upFirst(name));
			group.setDescription(description);
			try {
				dao.getAssociationDAO().add(group);
				Dialog.showInfo("New group created");
				observable.notifyObservers();
				loadGroups();
			} catch (Exception e) {
				LOG.error("Error while creating group: ", e);
			}finally{
				reset("group");
			}
		}
	}
	
	@FXML
	protected void deleteGroup(ActionEvent event) {
		Association delGroup = cbDelGroups.getValue();
		if (delGroup != null) {
			try {
				dao.getAssociationDAO().delete(delGroup);
				Dialog.showInfo("Group deleted");
				observable.notifyObservers();
				loadGroups();
			}catch (Exception e){
				LOG.error("Error while deleting group: ", e);
			}finally{
				lblDelGroup.setText("");
			}
		}
	}
	
	@FXML
	protected void chooseDelGroup() {
		Association delGroup = cbDelGroups.getValue();
		if (delGroup != null) {
			lblDelGroup.setTextFill(Color.RED);
			lblDelGroup.setText("Delete group: \"" + delGroup.getName() + "\"");
		}
	}
	
	// Reset text in all fields
	private void reset(String type) {
		if (type.equals("visitor")) {
			txtLastname.clear();
			txtFirstname.clear();
			txtMiddlename.clear();
			loadGroups();
			taVisitorDescription.clear();
		} else {
			txtGroupName.clear();
			taGroupDescription.clear();
		}
	}
	
	private void loadGroups() {
		try {
			Collection<Association> associations = dao.getAssociationDAO().getAll();
			cbGroup.getItems().clear();
			cbGroup.getItems().setAll(associations);
			cbGroup.getSelectionModel().selectFirst();
			
			cbDelGroups.getItems().clear();
			cbDelGroups.getItems().addAll(associations);
			cbDelGroups.getItems().remove(0);
		} catch (Exception e) {
			LOG.error("Error while loading groups: ", e);
		}
	}
}
