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

    public SignalAnalizer(Frame[] frame) {
        //We order the samples obtained in an ascending order
        frame= sortFrame(frame, 0, frame.length - 1);
        // We discard the 10% of the highest and the lowest values to get a more accurate resting potential
        // Since there are 100 samples we will discard the first and the last ten
        Frame[] shortenedFrame= new Frame[80];
        System.arraycopy(frame, 10, shortenedFrame, 0, shortenedFrame.length);
        
        restingPotential = energyCalculation(shortenedFrame);
    }

    public int contraction(Frame[] frame) {
        int signalEnergy = energyCalculation(frame);

        /*If the energy of the sample is more that five times the restingPotential, it means that
        the muscle is contracted*/
        if (signalEnergy >= 5 * restingPotential) {
            return 1;
        } else {
            return 0;
        }
    }

    private int energyCalculation(Frame[] frame) {
        int energy = 0;
        for (int i = 0; i < frame.length; i++) {
            //we substract 512 to get rid of the offset
            energy = energy + (int) Math.pow(frame[i].analog[0] - 512, 2);
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
        pivot = frame[start].analog[0];
        left = start;
        right = end;

        while (true) {
            while (left < end && frame[left].analog[0] <= pivot) {
                left++;
            }
            while (right > start && frame[right].analog[0] > pivot) {
                right--;
            }
            if (left >= right) {
                break;
            }
            temp = frame[left].analog[0];
            frame[left].analog[0] = frame[right].analog[0];
            frame[right].analog[0] = temp;
        }
        temp = frame[start].analog[0];
        frame[start].analog[0] = frame[right].analog[0];
        frame[right].analog[0] = temp;
        return right;
    }

}
