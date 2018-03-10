/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emgProcessing;
import javax.bluetooth.RemoteDevice;
import BITalino.*;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author LolaA
 */
public class RealTimeSignal {
    public static void main (String[] args){
        Frame[] samples; // We will take 100 samples per second
        Frame[] samples20;
        BITalino bitalino=null;
        try {
            bitalino = new BITalino();
            // find devices
            Vector<RemoteDevice> devices = bitalino.findDevices();
            System.out.println(devices);

            // connect to BITalino device
            String macAddress = "20:16:02:14:75:66";
            int samplingRate = 100;
            bitalino.open(macAddress, samplingRate);
            
            //We acquire from channel A1 (emg)
            int[] channels={0};
            bitalino.start(channels); //starts acquiring from channels
            
            //To set the resting potential we need to read 5 seconds.
            samples=bitalino.read(100); //100 samples = 1s if the sampling rate is 100
            SignalAnalizer signalAnalizer= new SignalAnalizer(samples);
            
            while(true){
                samples20=bitalino.read(20); //we will be reading 200ms to 200ms
                int contraction= signalAnalizer.contraction(samples20);
                System.out.println(contraction); //1 means contracted, 0 means at rest
            }
          
            
        } catch (BITalinoException ex) {
           ex.printStackTrace();
        } catch (InterruptedException ex) {
           ex.printStackTrace();
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        finally {
            try {
                //close bluetooth connection
                bitalino.close();
            } catch (BITalinoException ex) {
                ex.printStackTrace();
            }
        }
    }
}
