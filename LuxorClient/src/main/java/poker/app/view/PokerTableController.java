package poker.app.view;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Collections;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.SequentialTransitionBuilder;
import javafx.animation.TranslateTransition;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import poker.app.MainApp;
import pokerBase.Action;
import pokerBase.Card;
import pokerBase.Deck;
import pokerBase.GamePlayPlayerHand;
import pokerBase.Hand;
import pokerBase.Player;
import pokerBase.Table;
import pokerEnums.eAction;
import pokerEnums.ePlayerPosition;

public class PokerTableController {

	// Reference to the main application.
	private MainApp mainApp;

	public PokerTableController() {
	}

	@FXML
	private Label lblNumberOfPlayers;
	@FXML
	private TextArea txtPlayerArea;
	@FXML
	private ToggleButton btnPos1SitLeave;
	@FXML
	private ToggleButton btnPos2SitLeave;
	@FXML
	private ToggleButton btnPos3SitLeave;
	@FXML
	private ToggleButton btnPos4SitLeave;

	@FXML
	private Label lblPos1Name;
	@FXML
	private Label lblPos2Name;
	@FXML
	private Label lblPos3Name;
	@FXML
	private Label lblPos4Name;

	@FXML
	private void initialize() {
	}

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;

	}

	public void setlblNumberOfPlayers(Table tbl)
	{
		Iterator it = tbl.getHashPlayers().entrySet().iterator();
		txtPlayerArea.setText("Table ID: " + tbl.getTableID().toString() + '\n');
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			Player p = (Player) pair.getValue();
			txtPlayerArea.appendText("Player: " + p.getPlayerName() + "      Position: " + p.getiPlayerPosition() + "   ClientID: " + p.getiPokerClientID() + '\n');			
		}
	}
	@FXML
	private void handlePlay() {
	}

	@FXML
	public void GetGameState() {
		Action act = new Action(eAction.GameState, mainApp.getPlayer());
		mainApp.messageSend(act);
	}

	public void btnSitLeave_Click(ActionEvent event) {
		ToggleButton btnSitLeave = (ToggleButton) event.getSource();
		int iPlayerPosition = 0;
		if (btnSitLeave.isSelected()) {
			switch (btnSitLeave.getId().toString()) {
			case "btnPos1SitLeave":
				iPlayerPosition = ePlayerPosition.ONE.getiPlayerPosition();
				break;
			case "btnPos2SitLeave":
				iPlayerPosition = ePlayerPosition.TWO.getiPlayerPosition();
				break;
			case "btnPos3SitLeave":
				iPlayerPosition = ePlayerPosition.THREE.getiPlayerPosition();
				break;
			case "btnPos4SitLeave":
				iPlayerPosition = ePlayerPosition.FOUR.getiPlayerPosition();
				break;
			}
		}
		else
		{
			iPlayerPosition = 0;
		}
		
		mainApp.getPlayer().setiPlayerPosition(iPlayerPosition);
		Action act = new Action(btnSitLeave.isSelected() ? eAction.Sit : eAction.Leave, mainApp.getPlayer());
		act.setiPlayerPosition(iPlayerPosition);

		mainApp.messageSend(act);
	}

	public void btnSitLeave_Response(Table HubPokerTable) {
	
		lblPos1Name.setText("");
		lblPos2Name.setText("");
		lblPos3Name.setText("");
		lblPos4Name.setText("");
		
		btnPos1SitLeave.setVisible(true);
		btnPos2SitLeave.setVisible(true);
		btnPos3SitLeave.setVisible(true);
		btnPos4SitLeave.setVisible(true);
		
		btnPos1SitLeave.setText(btnPos1SitLeave.isSelected()? "Leave": "Sit");
		btnPos2SitLeave.setText(btnPos2SitLeave.isSelected()? "Leave": "Sit");
		btnPos3SitLeave.setText(btnPos3SitLeave.isSelected()? "Leave": "Sit");
		btnPos4SitLeave.setText(btnPos4SitLeave.isSelected()? "Leave": "Sit");
		
		Iterator it = HubPokerTable.getHashPlayers().entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			Player p = (Player) pair.getValue();
			switch (p.getiPlayerPosition()) {
			case 1:
				if (p.getPlayerID().equals(mainApp.getPlayer().getPlayerID()))
				{
					btnPos1SitLeave.setVisible(true);
					btnPos2SitLeave.setVisible(false);
					btnPos3SitLeave.setVisible(false);
					btnPos4SitLeave.setVisible(false);
				}
				else
				{
					btnPos1SitLeave.setVisible(false);
				}
				lblPos1Name.setText(p.getPlayerName().toString());
				break;
			case 2:
				if (p.getPlayerID().equals(mainApp.getPlayer().getPlayerID()))
				{
					btnPos1SitLeave.setVisible(false);
					btnPos2SitLeave.setVisible(true);
					btnPos3SitLeave.setVisible(false);
					btnPos4SitLeave.setVisible(false);
				}		
				else
				{
					btnPos2SitLeave.setVisible(false);
				}				
				lblPos2Name.setText(p.getPlayerName().toString());
				break;
			case 3:
				if (p.getPlayerID().equals(mainApp.getPlayer().getPlayerID()))
				{
					btnPos1SitLeave.setVisible(false);
					btnPos2SitLeave.setVisible(false);
					btnPos3SitLeave.setVisible(true);
					btnPos4SitLeave.setVisible(false);
				}		
				else
				{
					btnPos3SitLeave.setVisible(false);
				}				
				lblPos3Name.setText(p.getPlayerName().toString());
				break;
			case 4:
				if (p.getPlayerID().equals(mainApp.getPlayer().getPlayerID()))
				{
					btnPos1SitLeave.setVisible(false);
					btnPos2SitLeave.setVisible(false);
					btnPos3SitLeave.setVisible(false);
					btnPos4SitLeave.setVisible(true);
				}		
				else
				{
					btnPos4SitLeave.setVisible(false);
				}				
				lblPos4Name.setText(p.getPlayerName().toString());
				break;
			}
		}
	}

	@FXML
	void btnStart_Click(ActionEvent event) {
		Action act = new Action(eAction.StartGame, mainApp.getPlayer());
		mainApp.messageSend(act);
	}

	@FXML
	void btnDeal_Click(ActionEvent event) {
		Action act = new Action(eAction.Deal, mainApp.getPlayer());
		mainApp.messageSend(act);
	}

	@FXML
	public void btnFold_Click(ActionEvent event) {
		Button btnFold = (Button) event.getSource();
		Action act = new Action(eAction.Fold, mainApp.getPlayer());
		act.setiPlayerPosition(mainApp.getPlayer().getiPlayerPosition());
		mainApp.messageSend(act);		
	}
	
	@FXML
	public void btnCheck_Click(ActionEvent event) {
		Button btnFold = (Button) event.getSource();
		Action act = new Action(eAction.Fold, mainApp.getPlayer());
		act.setiPlayerPosition(mainApp.getPlayer().getiPlayerPosition());
		mainApp.messageSend(act);		
	}	



}

