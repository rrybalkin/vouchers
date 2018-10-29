package com.romansun.gui.controller.impl;

import com.romansun.gui.Dialog;
import com.romansun.gui.controller.AbstractController;
import com.romansun.hibernate.entity.Association;
import com.romansun.hibernate.entity.Talon;
import com.romansun.hibernate.entity.Visitor;
import com.romansun.utils.Messages;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import org.apache.log4j.Logger;

import java.net.URL;
import java.util.Collection;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

import static com.romansun.utils.Utils.upFirst;

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
    private TextField txtFirstName;
    @FXML
    private TextField txtLastName;
    @FXML
    private TextField txtMiddleName;
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
    void addVisitor() {
        Visitor visitor = new Visitor();
        final String firstName = txtFirstName.getText();
        final String lastName = txtLastName.getText();
        final String middleName = txtMiddleName.getText();
        final String description = taVisitorDescription.getText();
        final Association group = cbGroup.getValue();

        if (!lastName.isEmpty() && !firstName.isEmpty()) {
            visitor.setLastName(upFirst(lastName));
            visitor.setFirstName(upFirst(firstName));
            visitor.setMiddleName(upFirst(middleName));
            visitor.setAssociation(group);
            visitor.setDescription(description);

            final Talon talon = new Talon();
            visitor.setTalon(talon);
            try {
                daoFactory.getTalonDAO().add(talon);
                visitorsDAO.add(visitor);
                Dialog.showInfo(Messages.get("dialog.info.visitor-created"));
                observable.notifyObservers();
                LOG.info("New visitor = [" + visitor + "] was successfully added");
            } catch (Exception e) {
                Dialog.showErrorOnException(e);
                LOG.error("Error while adding new visitor = [" + visitor + "]: ", e);
            } finally {
                reset("visitor");
            }
        } else {
            Dialog.showWarning(Messages.get("dialog.warn.visitor-empty-fields"));
        }
    }

    @FXML
    void addGroup() {
        Association group = new Association();
        final String name = txtGroupName.getText();
        final String description = taGroupDescription.getText();
        if (!name.isEmpty()) {
            group.setName(upFirst(name));
            group.setDescription(description);
            try {
                daoFactory.getAssociationDAO().add(group);
                Dialog.showInfo(Messages.get("dialog.info.group-created"));
                observable.notifyObservers();
                loadGroups();
            } catch (Exception e) {
                Dialog.showErrorOnException(e);
                LOG.error("Error while creating group: ", e);
            } finally {
                reset("group");
            }
        }
    }

    @FXML
    void deleteGroup(ActionEvent event) {
        Association delGroup = cbDelGroups.getValue();
        if (delGroup != null) {
            final int answer = Dialog.showQuestion(Messages.get("dialog.question.do-group-delete", delGroup.getName()), event);
            if (answer == Dialog.YES) {
                try {
                    long countVisitors = daoFactory.getVisitorDAO().getCountVisitors(delGroup);
                    if (countVisitors > 0) {
                        Dialog.showWarning(Messages.get("dialog.warning.deleting-group-is-not-empty"));
                    } else {
                        daoFactory.getAssociationDAO().delete(delGroup);
                        Dialog.showInfo(Messages.get("dialog.info.group-deleted"));
                        observable.notifyObservers();
                        loadGroups();
                        LOG.debug("Group " + delGroup + " deleted with all visitors");
                    }
                } catch (Exception e) {
                    Dialog.showErrorOnException(e);
                    LOG.error("Error while deleting group: ", e);
                } finally {
                    lblDelGroup.setText("");
                }
            }
        }
    }

    @FXML
    void chooseDelGroup() {
        Association delGroup = cbDelGroups.getValue();
        if (delGroup != null) {
            lblDelGroup.setTextFill(Color.RED);
            lblDelGroup.setText(Messages.get("label.deleting-group", delGroup.getName()));
        }
    }

    // Reset text in all fields
    private void reset(String type) {
        if (type.equals("visitor")) {
            txtLastName.clear();
            txtFirstName.clear();
            txtMiddleName.clear();
            loadGroups();
            taVisitorDescription.clear();
        } else if (type.equals("group")) {
            txtGroupName.clear();
            taGroupDescription.clear();
        }
    }

    private void loadGroups() {
        try {
            Collection<Association> associations = daoFactory.getAssociationDAO().getAll();
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
