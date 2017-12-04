package parser;
        
import lexico.Analizador;
import java.io.File;
import java.util.Scanner;


public class Parser2 {
    //el archivo donde se encuentran los tokens.
    private File archivo;
    //leera el archivo de tokens.
    private Scanner sc;
    //el token leído.
    private String token;
    //bandera
    private boolean pocoCodigo;
        
    public Parser2() {
        try{
            archivo = new File("token.txt");            
            sc = new Scanner(archivo);
            pocoCodigo = false;
        } catch(Exception e) {
            System.out.println("analizador.Parser.main() " + e);
        }
    }
    
    public void generar() {
        //se generan los tokens.
        Analizador.generar();
        
        
        //si no hay código.
        if (!sc.hasNextLine()) {
            System.out.println("No hay código");
            sc.close();
            return;
        }
        
        //se empieza analizar.
        token = sc.nextLine();
        System.out.println("leido generar: " + token);
        programa();
        
        
        //si hay más lineas.
        if (sc.hasNextLine() || pocoCodigo) {
            sc.close();        
            System.out.println("Código no válido");
            return;
        }
        
        //si es válido.
        System.out.println("Código válido");
        sc.close();
    }
    
    //<programa> → Inicio <declaración><lista_enunciados> Fin
    private void programa() {        
        //se verifica la posición de Inicio.
        int inicio[] = lexico.Archivo.VerificarTabla("Inicio");
        //System.out.println("leido <programa>: " + token);

        //si es Inicio.
        if (String.valueOf(token.charAt(2)).equals(String.valueOf(inicio[0]))){
            if (sc.hasNextLine()) {
                token = sc.nextLine();
                System.out.println("leido <programa>: " + token);
                declaracion();
                listas_enuncidados();
                
                //si el token leído es Fin.                    
                int fin[] = lexico.Archivo.VerificarTabla("Fin");
                if (String.valueOf(token.charAt(2)).equals(String.valueOf(fin[0]))){                    
                    return;
                }
                
                //si no hay más lineas por leer.
                Error("<programa>", "Fin");
                return;
            }
            Error("<declaración>", "Identificador");
            return;
        }            
        //si no hay más lineas por leer.
        Error("<programa>", "Inicio");
    }
    
    //<declaración> → id<lista_ids>
    private void declaracion() {
        if (token.charAt(0) == '3') {
            if (sc.hasNextLine()) {
                token = sc.nextLine();
                System.out.println("leido <declaracion>: " + token);
                listas_id();
                return;
            }
        }
        Error("<declaración>", "Identificador");
    }
    
    //<lista_ids> → , id <lista_ids> | ;
    private void listas_id() {
        //<lista_ids> → , id <lista_ids>
        if (token.charAt(2) == ',') {
            token = sc.nextLine();
            System.out.println("leido <lista_ids>2: " + token);

            //si es un identificador.
            if (token.charAt(0) == '3') {
                if (sc.hasNextLine()) {
                    token = sc.nextLine();
                    System.out.println("leido <declaracion>: " + token);
                    listas_id();
                    return;
                }
                //si no hay más lineas por leer.
                Error("<lista_ids>", ", ó ;");
                return;
            }                
            //si no es Identificador.
            Error("<lista_ids>", "Identificador");
            return;
        }
        //<lista_ids> → ;
        if (token.charAt(2) == ';') {
            return;
        }        
        //si no hay más lineas por leer.
        Error("<lista_ids>", ", ó ;");
    }
    
    //<lista_enunciados> → <enunciado> <fin_de_lista>
    private void listas_enuncidados() {
        if (sc.hasNextLine()){
            token = sc.nextLine();
            System.out.println("leido <listas_enuncidados>: " + token);            
            enunciado();
            fin_de_lista();
            return;
        }
        //si no es Leer ó Escribir ó Identificador o´ Si ó Mientras.
        Error("<enunciado>", "Leer ó Escribir ó Identificador o´ Si ó Mientras");
        
    }
    
    //<fin_de_lista> → ε | <enunciado> <fin_de_lista>
    private void fin_de_lista() {
        if (sc.hasNextLine()){
            token = sc.nextLine();
            System.out.println("leido <fin_de_lista>: " + token);
            
            //<fin_de_lista> → ε
            int fin[] = lexico.Archivo.VerificarTabla("Fin");
            if (String.valueOf(token.charAt(2)).equals(String.valueOf(fin[0]))){            
                return;
            }               
            
            //<fin_de_lista> → <enunciado> <fin_de_lista>
            enunciado();
            fin_de_lista();
//            return;
        }
        //si no es Leer ó Escribir ó Identificador o´ Si ó Mientras.
        //Error("<enunciado>", "Leer ó Escribir ó Identificador o´ Si ó Mientras");
    }
    
    //<enunciado> → id := <expresión> ; | Leer id ; | Escribir id ; | <enunciado_if> | <enunciado_while>
    private void enunciado() {        
        //<enunciado> → Leer id ; | Escribir id ;
        int leer[] = lexico.Archivo.VerificarTabla("Leer");
        int escribir[] = lexico.Archivo.VerificarTabla("Escribir");
        if (String.valueOf(token.charAt(2)).equals(String.valueOf(leer[0])) ||
                String.valueOf(token.charAt(2)).equals(String.valueOf(escribir[0]))){
        
            if (sc.hasNextLine()){
                token = sc.nextLine();
                System.out.println("leido <enunciado>2: " + token);
                //si es Identificador.
                if (token.charAt(0) == '3') {
                    if (sc.hasNextLine()){
                        token = sc.nextLine();
                        System.out.println("leido <enunciado>3: " + token);
                        //si es punto y coma.
                        if (token.charAt(2) == ';') {
                            return;
                        }
                    }
                    //si no hay más lineas por leer.
                    Error("<enunciado>", ";");
                    return;                    
                }
            }
            //si no hay más lineas por leer.
            Error("<enunciado>", "Identificador");
            return;
        }
        
        //<enunciado> → id := <expresión> ; 
        if (token.charAt(0) == '3') {
            if (sc.hasNextLine()){
                token = sc.nextLine();
                System.out.println("leido <enunciado>4: " + token);
                //si es dos puntos e igual.
                if (token.charAt(0) == '7') {
//                    expresion();
                    if (sc.hasNextLine()){
                        token = sc.nextLine();
                        System.out.println("leido <enunciado>5: " + token);
                        //si es dos puntos e igual.
                        if (token.charAt(2) == ';') {
                            return;
                        }
                    }
                    //si no hay más lineas por leer.
                    Error("<enunciado>", ";");
                    return;
                }
            }
            //si no hay más lineas por leer.
            Error("<enunciado>", ":=");
            return;
        }

        //<enunciado> → <enunciado_if>
        int si[] = lexico.Archivo.VerificarTabla("Si");
        if (String.valueOf(token.charAt(2)).equals(String.valueOf(si[0]))){
            enunciado_if();
            return;
        }

        //<enunciado> → <enunciado_while>
        int mientras[] = lexico.Archivo.VerificarTabla("Mientras");
        if (String.valueOf(token.charAt(2)).equals(String.valueOf(mientras[0]))){
            enunciado_while();
            return;
        }    
        //si no es Leer ó Escribir.
        Error("<enunciado>", "Leer ó Escribir ó Identificador o´ Si ó Mientras");
    }
    
    //<condición> → <factor_simple> <op_rel> <factor_simple>
    private void condicion() {
        factor_simple();
        op_rel();
        factor_simple();
    }
    
    //<op rel> → > | < | = | <= | >=
    private void op_rel() {
        if (sc.hasNextLine()){
            token = sc.nextLine();
            System.out.println("leido <op_rel>: " + token);
            if (token.charAt(0) == '6') {
                return;
            }
        }
        //si no es Leer ó Escribir.
        //no lee <
        Error("<op_rel>", "> ó < ó = ó <= ó >=");
    }
    
    private void expresion() {
        
    }
    
    private void expresion_simple() {
        
    }
    
    private void término() {
        
    }
    
    private void término_simple() {
        
    }
    
    private void factor() {
        
    }
    
    //<factor_simple> → id | entero
    private void factor_simple() {
//        if (sc.hasNextLine()){
//            token = sc.nextLine();
//            System.out.println("leido <factor_simple>: " + token);        
            if (token.charAt(0) == '3' || token.charAt(0) == '1') {
                return;
            }
//        }
        //si no es Leer ó Escribir.
        Error("<factor_simple>", "Identificador o´ Constante entera");
    }
    
    private void enunciado_if() {
        
    }
    
    private void fin_if() {
        
    }
    
    //<enunciado_while> → Mientras <condición> Hacer <enunciado> Fin
    private void enunciado_while() {
//        if (sc.hasNextLine()){
//            token = sc.nextLine();
//            System.out.println("leido <enunciado_while>: " + token);
            condicion();            
//            return;
//        }
//        //si no es Leer ó Escribir.
//        Error("<enunciado_while>", "Identificador o´ Constante entera");
    }
    
    private void Error(String noTerminal, String error) {        
        System.out.println("Error en " + noTerminal + " se esperaba " + error);        
        pocoCodigo = true;
//        System.out.println("Código no válido");
//        sc.close();
//        System.exit(0);
    }
}

