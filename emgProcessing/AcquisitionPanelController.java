/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emgProcessing;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class AcquisitionPanelController  {
    private RealTimeSignal realTimeSignal;
    
    @FXML
    private Button startButton;

    @FXML
    private Button stopButton;

    @FXML
    void startClicked(ActionEvent event) {
         realTimeSignal.start();
    }
    
     @FXML
    void startKeyPressed(KeyEvent event) {
        if(event.getCode().equals(KeyCode.ENTER)){
            realTimeSignal.start();
       }
    }

    @FXML
    void stopClicked(ActionEvent event) {
         realTimeSignal.stopAcquisition();
    }
    
    @FXML
    void stopKeyPressed(KeyEvent event) {
        if(event.getCode().equals(KeyCode.ENTER)){
            realTimeSignal.stopAcquisition();
        }
    }
    
    public void initComponents (RealTimeSignal realTimeSignal){
        this.realTimeSignal=realTimeSignal;
    }

}

