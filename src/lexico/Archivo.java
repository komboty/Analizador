package lexico;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

public class Archivo {
    /** 
     * Crea un archivo llamado "tablaSimbolos" y lo llena con una tabla de símbolos 
     * que contiene las palabras reservadas.
     * La tabla de símbolos tiene como campos: posición, nombre y clase, en ese orden.
     * @return La posición del último elemento que esta en el archivo.
     * @exception Regresa -1.
    */
    static int TablaSimbolos(){
        int posicion;
        String[] nombre = {"Entonces", "Escribir", "Fin", "Hacer", "Inicio", "Leer", "Mientras", "Si", "Sino"};
        int clase = 2; 
           
        try{
            String archivo;
            FileWriter fr;
            BufferedWriter br;
            
            archivo = "token.txt";
            fr = new FileWriter(archivo);
            archivo = "tablaSimbolos.txt";
            fr = new FileWriter(archivo);
            br = new BufferedWriter(fr);

            for (posicion = 1; posicion <= nombre.length; posicion++){
                    br.write(posicion + " " + nombre[posicion - 1] + " " + clase);
                    br.newLine();
            }

            br.close();

            return posicion;
        }
        catch(Exception e) {
            e.printStackTrace();
            return -1;
        }   
    }

    /** 
     * Guarda un token en el archivo llamado "token" de la siguiente manera:
     * clase y valor, en ese orden.
     * @param clase 
     *      Tipo entero, es la clase del token a guardar.
     * @param valor 
     *      Tipo String, es el valor del token a guardar.
    */
    static void GuardarToken(int clase, String valor){
        try{
            String archivo;
            FileWriter fr;
            BufferedWriter br;

            archivo = "token.txt";
            fr = new FileWriter(archivo, true);
            br = new BufferedWriter(fr);

            br.write(clase + " " + valor);
            br.newLine();

            br.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    /** 
     * Guarda un identificador en el archivo llamado "tablaSimbolos" de la siguiente manera:
     * posición, nombre y clase, en ese orden.
     * @param posicion 
     *      Tipo entero, es la posición del identificador a guardar.
     * @param nombre 
     *      Tipo String, es el nombre del identificador a guardar.
     * @param clase 
     *      Tipo entero, es la clase del identificador a guardar.
     * @return la posición más uno, del último elemento que esta en el archivo.
     * @exception Regresa -1.
    */
    static int GuardarIdentificador(int posicion, String nombre, int clase){	
        try{
            String archivo;
            FileWriter fr;
            BufferedWriter br;

            archivo = "tablaSimbolos.txt";
            fr = new FileWriter(archivo, true);
            br = new BufferedWriter(fr);

            br.write(posicion + " " + nombre + " " + clase);
            br.newLine();
            posicion++;

            br.close();
            return posicion;
        }
        catch(Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /** 
     * Verifica en el archivo "tablaSimbolos" si una cadena se encuentra en él.     
     * @param cadena 
     *      Tipo String, es la cadena a encontrar en el archivo.     
     * @return Un arreglo de tipo entero con dos elementos.
     *      Si es encontrado, el primer elemento es la posición y 
     *      el segundo elemento es la clase.
     *      Si no es encontrado, el primer y segundo elemento son cero.
     * @exception Regresa un arreglo de tipo entero con dos elementos
     *      que contienen -1.
    */
    public static int[] VerificarTabla(String cadena){
	int[] posicion_Clase = new int[2];
	String nombre;
        try{
            String ruta;
            Scanner sc;
            File archivo;

            ruta = "tablaSimbolos.txt";
            archivo = new File(ruta);
            sc = new Scanner(archivo);
            
            while (sc.hasNextLine()){
                String linea = sc.nextLine();
                
                Scanner scLinea = new Scanner(linea);
                scLinea.useDelimiter(" ");
                
                int posicion = Integer.parseInt(scLinea.next());
                nombre = scLinea.next();
                int clase = Integer.parseInt(scLinea.next());

                if(cadena.equals(nombre)){
                    sc.close();
                    posicion_Clase[0] = posicion;
                    posicion_Clase[1] = clase;
                    return posicion_Clase;
                }
            }
            
            sc.close();
            posicion_Clase[0] = 0;
            posicion_Clase[1] = 0;
            return posicion_Clase;
        }
        catch(Exception e) {
            e.printStackTrace();
            posicion_Clase[0] = -1;
            posicion_Clase[1] = -1;
            return posicion_Clase;
        }
    }
}
