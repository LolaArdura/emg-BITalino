/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emgProcessing.recordedEMG;
import java.io.*;
import BITalino.Frame;
import emgProcessing.SignalAnalizer;

/**
 *
 * @author LolaA
 */
public class RecordedSignalProcessing {
    private File file;
    private FileInputStream fileInput;
    private InputStreamReader inputStream;
    private BufferedReader bufferedReader;
    
    public RecordedSignalProcessing(String fileRoute) throws FileNotFoundException{
        file=new File(fileRoute);
        fileInput= new FileInputStream(file);
        inputStream= new InputStreamReader(fileInput);
        bufferedReader= new BufferedReader(inputStream);
    }
    
    public void processFile() throws IOException{
        String reading;
        Frame[] samples= new Frame[100];
        reading=bufferedReader.readLine();
        for (int i=0; i<100&&(reading!=null);i++){
            int data=Integer.parseInt(reading);
            samples[i]=new Frame();
            samples[i].analog[0]= data;
            reading= bufferedReader.readLine();
        }
        //We miss one datum because the first loop stops when i=100, and in the while loop we read another
        //line
        SignalAnalizer signalAnalizer=new SignalAnalizer(samples);
        while((reading=bufferedReader.readLine())!=null){
         for (int i=0; i<100&&(reading!=null);i++){
            samples[i].analog[0]= Integer.parseInt(reading);
            reading= bufferedReader.readLine();
        }
        int contraction=signalAnalizer.contraction(samples);
        if(contraction==1) System.out.println("Contracted");
        else System.out.println("Not contracted");
         
        }
        
        
        
    
    }
    
}
