/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emgProcessing;

import BITalino.Frame;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author LolaA
 */
public class SignalAnalyzer {

    private int restingPotential;
    private int channel;
    
    //The threshold determines the number of times the energy of the EMG needs to be higher than the 
    //resting potential energy
    private int threshold;
    
    public SignalAnalyzer(Frame[] frame, int channel, int threshold){
        this.threshold=threshold;
        this.channel=channel;
        
        //We order the samples obtained in an ascending order
        frame= sortFrame(frame, 0, frame.length - 1);
        // We discard the 10% of the highest and the lowest values to get a more accurate resting potential
        // This means that the length of the shortenedFrame is 0.8 the length of the initial Frame
        Frame[] shortenedFrame= new Frame[(int)(0.8*frame.length)];
        //We copy from the position 0.1*frame.length (so we start discarding the lowest 10% of the values
        System.arraycopy(frame, (int)(0.1*frame.length), shortenedFrame, 0, shortenedFrame.length);
        
        restingPotential = energyCalculation(shortenedFrame);
    }

    public boolean contraction(Frame[] frame) {
        int signalEnergy = energyCalculation(frame);

        /*If the energy of the sample is more that five times the restingPotential, it means that
        the muscle is contracted*/
        return (signalEnergy >= threshold * restingPotential);
    }

    private int energyCalculation(Frame[] frame) {
        int energy = 0;
        for (int i = 0; i < frame.length; i++) {
            //we substract 512 to get rid of the offset
            energy = energy + (int) Math.pow(frame[i].analog[channel] - 512, 2);
        }
        return energy;
    }

    private Frame[] sortFrame(Frame[] frame, int start, int end) {
        int pivotIndex;
        if (start < end) {
            pivotIndex = partition(frame, start, end);
            sortFrame(frame, start, pivotIndex - 1);
            sortFrame(frame, pivotIndex + 1, end);
        }
        return frame;
    }

    private int partition(Frame[] frame, int start, int end) {
        int pivot, left, right, temp;
        pivot = frame[start].analog[channel];
        left = start;
        right = end;

        while (true) {
            while (left < end && frame[left].analog[channel] <= pivot) {
                left++;
            }
            while (right > start && frame[right].analog[channel] > pivot) {
                right--;
            }
            if (left >= right) {
                break;
            }
            temp = frame[left].analog[channel];
            frame[left].analog[channel] = frame[right].analog[channel];
            frame[right].analog[channel] = temp;
        }
        temp = frame[start].analog[channel];
        frame[start].analog[channel] = frame[right].analog[channel];
        frame[right].analog[channel] = temp;
        return right;
    }

}
