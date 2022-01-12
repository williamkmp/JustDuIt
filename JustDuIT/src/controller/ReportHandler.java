package controller;

import view.productManagement.ProductManagementFrame;
import view.report.ReportManagementPage;

public class ReportHandler {
	private static ReportHandler instance;
	private ReportManagementPage frame;
	private HomeController home;
	
	private ReportHandler() {
		this.home = HomeController.getInstance();
	}
	
	public void showPage() {
		if(this.frame == null) this.frame = ReportManagementPage.getPage();
		this.frame.setVisible(true);
	}
	
	public void hidePage() {
		if(this.frame == null) this.frame = ReportManagementPage.getPage();
		this.frame.setVisible(false);
	}
	
	public void backButtonHandler() {
		frame.dispose();
		home.showPage(home.getLoggedEmployee().getID());
	}
	
	public static synchronized ReportHandler getInstance() {
		if(instance == null) {
			instance = new ReportHandler();
		}
		return instance;
	}
}
