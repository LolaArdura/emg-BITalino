/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emgProcessing.recordedEMG;


import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author LolaA
 */
public class mainRecordedEMG {
    public static void main (String[] args){
        try {
            RecordedSignalProcessing procesador=
                    new RecordedSignalProcessing("C:/Java/BitalinoEMG/emg.txt");
            
            procesador.processFile();
        } catch (FileNotFoundException ex) {
            System.out.println("No se encontro el archivo");
        } catch (IOException ex) {
            System.out.println("Error en la lectura");
        }
    }
}
