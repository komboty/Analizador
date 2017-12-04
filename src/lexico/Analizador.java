package lexico;

import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;

public class Analizador {
    
    public static void generar(){
        //tecla, es el caracter a leer.
        char tecla = '-';
        //estado, es el nombre del estado en el AFD
	char estado = 'I';
        //clase, es la clase del del lenguaje definido
	int clase = 0;
        //linea, es la linea en la que se encuentra el caracter leido en el archivo.
        int linea = 1;
        //Se crea la tabla de símbolos y se guarda la posición del último elemnto en la tabla.
        int posicionTabla = Archivo.TablaSimbolos();
        //verificaTabla, es para recibir los valores de la función "VerificarTabla".
        int[] verificaTabla;
        //valor, es para guardar los caracteres leídos en el archivo.
	String valor = "";
        //letra, guarda la expresión regular para definir una letra en nuestro lenguaje.
        String letra = "[A-Za-z]";
        //digito, guarda la expresión regular para definir una dígito en nuestro lenguaje.
        String digito = "[0-9]";
        //codigo, guarda el nombre del archivo a leer, que contiene el código.
        String codigo = "codigo.txt";
        
        //Se declara un objeto FileReader para poder leer el archivo caracter por caracter.
        FileReader fr;
        
        try {
            //Se crea el objeto FileReader y se le pasa el archivo con el código.
            fr = new FileReader(codigo);
            
            //Se leera el archivo hasta que el caracter leído sea -1, que significa que ya se acabo el archivo.
            while(tecla != (char)-1){
                
                //Se descartan los comentarios.
                if(tecla == '{'){
                    do{                        
                        tecla = (char)fr.read();
                    }while(tecla != '}' && tecla != (char)-1);
		}
                
                //Se lee un caracter en el archivo y se guarada en tecla.
                tecla = (char)fr.read();                
                                
                if(tecla == ' ' || tecla == '\n'){
                    
                    //Se van contando los saltos de linea.
                    if(tecla == '\n'){
                            linea++;
                    }
                    
                    //Se guarda un token con la clase 1, que son las constantes enteras.
                    if(estado == 'A' && valor.length() > 0){
                        clase = 1;                        
                        Archivo.GuardarToken(clase, valor);
                    }
                    
                    //Se guarda un token con la clase 3, que son los identificadores.
                    if(estado == 'B' && valor.length() > 0){
                        //Verificamos si existe el identificador en la tabla de símbolos.
                        verificaTabla = Archivo.VerificarTabla(valor);
                        
                        //Si se encuentra el identificador en la tabla de símbolos, se guardara el token.
                        if(verificaTabla[0] != 0){
                            valor = String.valueOf(verificaTabla[0]);
                            Archivo.GuardarToken(verificaTabla[1], valor);
                        }
                        
                        //Si no se encuentra el identificador en la tabla de símbolos,
                        //se guardara en la tabla de símbolos y en los tokens.
                        else{
                            clase = 3;
                            posicionTabla = Archivo.GuardarIdentificador(posicionTabla, valor, clase);
                            valor = String.valueOf(posicionTabla - 1);
                            Archivo.GuardarToken(clase, valor);
                        }
                    }
                    
                    if (estado == 'D'){
                        clase = 6;	
                        Archivo.GuardarToken(clase, valor); 
                    }
                                        
                    estado = 'I';
                    valor = "";
                }
                
                //Este switch es el AFD.
                switch(estado){

                    case 'I':
                        //Si el caracter leído es un digito nos vamos al caso A.
                        if(Pattern.matches(digito, tecla + "")){
                                estado = 'A';
                                valor += tecla;
                                break;
                        }
                        
                        //Si el caracter leído es una letra nos vamos al caso B.
                        if(Pattern.matches(letra, tecla + "")){
                                estado = 'B';
                                valor += tecla;
                                break;
                        }
                        
                        switch(tecla){
                                //Si el caracter leído es: (, ), ,, ;,
                                //guardamos el token con su clase correspondientes que es 4.
                                case '(':
                                case ')':
                                case ',':
                                case ';':
                                        clase = 4;
                                        valor = String.valueOf(tecla);
                                        Archivo.GuardarToken(clase, valor);
                                break;
                                
                                //Si el caracter leído es: +, -, *, /,
                                //guardamos el token con su clase correspondientes que es 5.
                                case '+':
                                case '-':
                                case '*':
                                case '/':
                                        clase = 5;
                                        valor = String.valueOf(tecla);
                                        Archivo.GuardarToken(clase, valor);
                                break;
                                
                                //Si el caracter leído es un = guardamos el token con 
                                //su clase correspondientes que es 6.
                                case '=':
                                        clase = 6;
                                        valor = String.valueOf(tecla);
                                        Archivo.GuardarToken(clase, valor);
                                break;
                                
                                //Si el caracter leído es: >, <,
                                //nos vamos al caso D.
                                case '>':
                                case '<':
                                        estado = 'D';
                                        valor += tecla;
                                break;
                                
                                //Si el caracter leído es : nos vamos al caso E.
                                case ':':
                                        estado = 'E';
                                        valor += tecla;
                                break;
                        }
                    break;

                    case 'A':
                        valor += tecla;
                        
                        //Si el caracter leído no es un dígito o el dígito almacenado pasa el valor de 65535
                        //se imprime un error en consola
                        if (!Pattern.matches(digito, tecla + "") || Integer.parseInt(valor) > 65535){
                            System.out.println("ERROR en entero: " + valor + " en la linea: " +  linea);
                            valor = "";
                        }
                    break;

                    case 'B':
                        valor += tecla;
                        
                        //Si el caracter leído no es una letra o un dígito o _
                        //se imprime un error en consola
                        if (!(Pattern.matches(letra, tecla + "") || Pattern.matches(digito, tecla + "") || tecla == '_') || valor.length() > 10){
                            System.out.println("ERROR en identificador: " + valor + " en la linea: " +  linea);
                            valor = "";
                        }
                    break;

                    case 'D':
                        //System.out.println(valor+"lexico.Analizador.generar()" + tecla);
                        //Si el caracter leído anteriormente es: >, <, y el caracter leído actualmente es =
                        //se guarada el token y se regresa al caso I.
                        if (tecla == '='){
                            clase = 6;												
                            estado = 'I';
                            valor += tecla;
                            Archivo.GuardarToken(clase, valor); 
                            valor = "";
                                                       
                        }
                        //Si el caracter leído anteriormente es: >, <, y el caracter leído actualmente no es =
                        //se verifica que caracter es para asi guardarlo o irse a otro caso.
//                        else {
//                            clase = 6;					
//                            Archivo.GuardarToken(clase, valor); 
//                            valor = "";
//
//                            if(Pattern.matches(digito, tecla + "")){
//                                estado = 'A';
//                                valor += tecla;
//                                break;
//                            }
//
//                            if(Pattern.matches(letra, tecla + "")){
//                                    estado = 'B';
//                                    valor += tecla;
//                                    break;
//                            }
//
//                            switch(tecla){
//                                case '(':
//                                case ')':
//                                case ',':
//                                case ';':
//                                        clase = 4;
//                                        valor = String.valueOf(tecla);
//                                        Archivo.GuardarToken(clase, valor);
//                                break;
//
//                                case '+':
//                                case '-':
//                                case '*':
//                                case '/':
//                                        clase = 5;
//                                        valor = String.valueOf(tecla);
//                                        Archivo.GuardarToken(clase, valor);
//                                break;
//
//                                case '=':
//                                        clase = 6;
//                                        valor = String.valueOf(tecla);
//                                        Archivo.GuardarToken(clase, valor);
//                                break;
//
//                                case '>':
//                                case '<':
//                                        estado = 'D';
//                                        valor += tecla;
//                                break;
//
//                                case ':':
//                                        estado = 'E';
//                                        valor += tecla;
//                                break;
//                            }
//                        }
                    break;	

                    case 'E':
                        valor += tecla;
                        
                        //Si el caracter leído anteriormente es : y el caracter leído actualmente es =
                        //se guarada el token y se regresa al caso I.
                        if (tecla == '='){
                            clase = 7;					
                            estado = 'I';
                            Archivo.GuardarToken(clase, valor); 								
                        }
                        //Si el caracter leído anteriormente es : y el caracter leído actualmente no es =
                        //se imprime un error en consola.
                        else{
                            System.out.println("ERROR en operador de asignacion: " + valor + " en la linea: " +  linea);
                        }

                        valor = "";
                    break;
                }
            }            
            fr.close();
        }
        catch(IOException | NumberFormatException e){
            System.out.println("analizadorlexico.Analizador.main()" + e);
        }
    }
}
