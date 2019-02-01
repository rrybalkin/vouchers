package com.rrybalkin.gui.controller;

import com.rrybalkin.hibernate.dao.VisitorDAO;
import com.rrybalkin.hibernate.entity.Association;
import com.rrybalkin.hibernate.entity.Visitor;
import com.rrybalkin.hibernate.factory.DAOFactory;
import com.rrybalkin.utils.SuppressFBWarnings;
import javafx.scene.control.TabPane;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@SuppressFBWarnings(value = "MS_SHOULD_BE_FINAL", justification = "Need to have it not final")
public abstract class AbstractController {
	private static final Logger LOG = Logger.getLogger(AbstractController.class);

	protected final static String PATH_TO_REPORTS = "reports";
	
	protected static final VisitorDAO visitorsDAO;
	protected static final DAOFactory daoFactory;
	protected static AtomicReference<TabPane> mainTabPaneRef = new AtomicReference<>();
	
	static {
		File reports = new File(PATH_TO_REPORTS);
		if (!reports.exists()) {
			boolean created = reports.mkdir();
			LOG.info("Reports folder created: " + created);
		}
		daoFactory = DAOFactory.getInstance();
		visitorsDAO = daoFactory.getVisitorDAO();
	}

	protected List<Association> sortGroups(List<Association> groups) {
		groups.sort(Comparator.comparing(Association::getName));
		return groups;
	}

	protected List<Visitor> sortVisitors(List<Visitor> visitors) {
		visitors.sort((v1, v2) -> {
			String fio_1 = v1.getLastName() + " " + v1.getFirstName() + " "
					+ v1.getMiddleName();
			String fio_2 = v2.getLastName() + " " + v2.getFirstName() + " "
					+ v2.getMiddleName();
			return fio_1.compareTo(fio_2);
		});
		return visitors;
	}
}