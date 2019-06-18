package it.polito.tdp.formulaone;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.formulaone.model.Driver;
import it.polito.tdp.formulaone.model.Model;
import it.polito.tdp.formulaone.model.Season;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FormulaOneController {
	
	Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<Season> boxAnno;

    @FXML
    private TextField textInputK;

    @FXML
    private TextArea txtResult;

    @FXML
    void doCreaGrafo(ActionEvent event) {

    	txtResult.clear();
    	Season anno = this.boxAnno.getValue();
    	
    	if(anno != null) {
    		
    		model.creaGrafo(anno);
    		txtResult.appendText(model.pilotaMigliore().toString());
    		
    	} else txtResult.appendText("Seleziona una stagione!");
    	
    }

    @FXML
    void doTrovaDreamTeam(ActionEvent event) {

    	txtResult.clear();
    	
    	try {
    		
    		int k = Integer.parseInt(textInputK.getText());
    		List<Driver> migliori = new ArrayList<Driver>(model.trovaDreamTeam(k));
    		for(Driver driver : migliori)
    			txtResult.appendText(driver.toString()+" con tasso: "+model.getTasso(driver, migliori)+"\n");
    		txtResult.appendText("Tasso: "+model.getTasso(model.trovaDreamTeam(k)));
    		
    	} catch(NumberFormatException e) {
    		e.printStackTrace();
    		txtResult.appendText("Inserisci un valore numerico!");
    	}
    }

    @FXML
    void initialize() {
        assert boxAnno != null : "fx:id=\"boxAnno\" was not injected: check your FXML file 'FormulaOne.fxml'.";
        assert textInputK != null : "fx:id=\"textInputK\" was not injected: check your FXML file 'FormulaOne.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'FormulaOne.fxml'.";

    }
    
    public void setModel(Model model){
    	this.model = model;
    	this.boxAnno.getItems().addAll(model.getSeasons());
    }
}
