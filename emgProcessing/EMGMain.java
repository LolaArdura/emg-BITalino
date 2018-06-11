/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emgProcessing;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javax.swing.JFrame;

/**
 *
 * @author LolaA
 */
public class EMGMain extends Application {
    public static void main(String[] args){
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        try{
        String macAddress="20:17:09:18:49:21";
        int samplingRate=1000;
        int[] channels={0,1};
        int[] thresholds={3,3};
        String[] commands={"move relative 10 up","move relative 10 down"};
        RealTimeSignal signal= new RealTimeSignal(macAddress,samplingRate,channels,commands,thresholds);
        signal.openConnection();
        
        FXMLLoader loader= new FXMLLoader(getClass().getResource("AcquisitionPanel.fxml"));
        Parent root= loader.load();
        Scene scene= new Scene(root,600,600);
        primaryStage.setScene(scene);
        
        AcquisitionPanelController controller= (AcquisitionPanelController) loader.getController();
        controller.initComponents(signal);
        
        primaryStage.centerOnScreen();
        primaryStage.show();
        
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
