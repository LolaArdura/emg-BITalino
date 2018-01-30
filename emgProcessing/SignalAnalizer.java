/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emgProcessing;
import BITalino.Frame;

/**
 *
 * @author LolaA
 */
public class SignalAnalizer {
    private int restingPotential;
    
    public SignalAnalizer(Frame[] frame){
        restingPotential=energyCalculation(frame);
    }
    
    public int contraction (Frame[] frame){
        int signalEnergy=energyCalculation(frame);
       
        /*If the energy of the sample is more that five times the restingPotential, it means that
        the muscle is contracted*/
        if (signalEnergy>=5*restingPotential) return 1;
        else return 0;
    }
    private int energyCalculation (Frame[] frame){
        int energy=0;
         for (int i=0; i<frame.length;i++){
            energy=energy+ (int) Math.pow(frame[i].analog[0]-512, 2);
        }
         return energy;
    }
    
}
