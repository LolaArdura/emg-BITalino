/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emgProcessing;

/**
 *
 * @author LolaA
 */
public class EMGMain {
    public static void main(String[] args){
        String macAddress="20:17:09:18:49:21";
        int samplingRate=1000;
        int[] channels={0,2};
        String[] commands={"move relative 200 up","move relative 200 left"};
        RealTimeSignal signal= new RealTimeSignal(macAddress,samplingRate,channels,commands);
        signal.openConnection();
        signal.run();
        
    }
}
