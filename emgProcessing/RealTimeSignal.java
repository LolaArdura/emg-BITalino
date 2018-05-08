/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emgProcessing;

import javax.bluetooth.RemoteDevice;
import BITalino.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author LolaA
 */
public class RealTimeSignal extends Thread {

    String macAddress;
    int samplingRate;
    BITalino bitalino = null;
    volatile boolean stop = false;
    int[] acquisitionChannels;
    String[] commands; //The first command will be assigned to the first channel

    //we need method for the connection with 
    //the other application + channel from which we are acquiring data
    //The connection is made in the processor
    public RealTimeSignal(String macAddress, int samplingRate, int[] acquisitionChannels, String[] commands) {
        this.macAddress = macAddress;
        this.samplingRate = samplingRate;
        bitalino = new BITalino();
        this.acquisitionChannels = acquisitionChannels;
        this.commands = commands;
    }

    @Override
    public void run() {
        try {
            //If the method openConnection has not been called yet, an error would occur
            startAcquisition();

            bitalino.close();

        } catch (BITalinoException ex) {
            ex.printStackTrace();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } catch (Throwable ex) {
            ex.printStackTrace();
        } finally {
            try {
                //close bluetooth connection
                bitalino.close();
            } catch (BITalinoException ex) {
                ex.printStackTrace();
            }
        }
    }

    public boolean openConnection() {
        try {
            // find devices
            Vector<RemoteDevice> devices = bitalino.findDevices();
            System.out.println(devices);

            // connect to BITalino device
            bitalino.open(macAddress, samplingRate);
            return true;
        } catch (BITalinoException ex) {
            ex.printStackTrace();
            return false;
        } catch (InterruptedException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public void startAcquisition() throws BITalinoException, Throwable {
        Frame[] samples;
        Frame[] samples20;
        List<SignalAnalizer> analizers = new ArrayList<SignalAnalizer>();

        Socket socket = new Socket("localhost", 9000);
        PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        System.out.println(reader.readLine());
        System.out.println(reader.readLine());
        bitalino.start(acquisitionChannels); //starts acquiring from channels

        //To set the resting potential we need to read 1 seconds.
        samples = bitalino.read(samplingRate); //1s of acquisition to set the resting potential
        System.out.println(acquisitionChannels.length);
        for (int channel = 0; channel < acquisitionChannels.length; channel++) {
            System.out.println(acquisitionChannels[channel]);
            analizers.add(new SignalAnalizer(samples, acquisitionChannels[channel]));
        }
        System.out.println("Resting potential set");
        while (!stop) {
            int samples20ms = (int) (0.02 * samplingRate);
            samples20 = bitalino.read(samples20ms); //we will read 20 ms
            for (int channel = 0; channel < acquisitionChannels.length; channel++) {
                boolean verification = analizers.get(channel).contraction(samples20);
                if (verification) {
                    writer.println(commands[channel]);
                    writer.flush();
                }
            }

        }

        releaseResources(writer, reader, socket);
    }

    public void stopAcquisition() {
        stop = true;
    }

    private void releaseResources(PrintWriter writer, BufferedReader reader, Socket socket) {
        if (writer != null) {
            writer.close();
        }
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        try {
            socket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
