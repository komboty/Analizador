package parser;
        
import lexico.Analizador;
import java.io.File;
import java.util.Scanner;


public class Parser {
    //el archivo donde se encuentran los tokens.
    private File archivo;
    //leera el archivo de tokens.
    private Scanner sc;
    //el token leído.
    private String token;
    //bandera
    private boolean pocoCodigo;
        
    public Parser() {
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
        
        //token = sc.nextLine();
        //System.out.println("leido generar: " + token);
        programa();
        
        //si hay más lineas.
        if (sc.hasNextLine() || pocoCodigo) {
            sc.close();        
            System.out.println("Código no válido");
            return;
        }
                
        System.out.println("Código válido");
        sc.close();
        
        //si ya no hay algo por leer es válido el código.
//        if (!sc.hasNextLine() && !noFin) {            
//            System.out.println("Válido");
//            sc.close();
//            return;
//        }
//        
//        //si falta código,
//        if (noFin) {
//            sc.close();        
//            Error("<programa>", "tener más código");
//            return;
//        }
//        
//        //si hay más lineas por leer.
//        sc.close();        
//        System.out.println("Código no válido");
    }
    
    //<programa> → Inicio <declaración><lista_enunciados> Fin
    private void programa() {
        if (sc.hasNextLine()) {
            token = sc.nextLine();
            //se verifica la posición de Inicio.
            int inicio[] = lexico.Archivo.VerificarTabla("Inicio");
            System.out.println("leido <programa>: " + token);

            //si es Inicio.
            if (String.valueOf(token.charAt(2)).equals(String.valueOf(inicio[0]))){
            //if (token.charAt(2) == '5') {
                declaracion();
                listas_enuncidados();

                if (sc.hasNextLine()) {
                    token = sc.nextLine();
                    System.out.println("leido <programa>: " + token);

                    //si el token leído es Fin.                    
                    int fin[] = lexico.Archivo.VerificarTabla("Fin");
                    if (String.valueOf(token.charAt(2)).equals(String.valueOf(fin[0]))){
                    //if (token.charAt(2) == '3') {
                        return;
                    }
                }                
                //si no hay más lineas por leer.
                Error("<programa>", "Fin");
                return;
            }            
        }        
        //si no hay más lineas por leer.
        Error("<programa>", "Inicio");
    }
    
    //<declaración> → id<lista_ids>
    private void declaracion() {
        if (sc.hasNextLine()) {
            token = sc.nextLine();
            System.out.println("leido <declaración>: " + token);

            //si es un identificador.
            if (token.charAt(0) == '3') {
                listas_id();
                return;
            }
        }        
        //si no hay más lineas por leer.
        Error("<declaración>", "Identificador");
    }
    
    //<lista_ids> → , id <lista_ids> | ;
    private void listas_id() {
        if (sc.hasNextLine()) {
            token = sc.nextLine();
            System.out.println("leido <lista_ids>: " + token);
            
            //<lista_ids> → , id <lista_ids>
            if (token.charAt(2) == ',') {
                token = sc.nextLine();
                System.out.println("leido <lista_ids>2: " + token);
            
                //si es un identificador.
                if (token.charAt(0) == '3') {
                    listas_id();
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
        }        
        //si no hay más lineas por leer.
        Error("<lista_ids>", ", ó ;");
    }
    
    //<lista_enunciados> → <enunciado> <fin_de_lista>
    private void listas_enuncidados() {
        enunciado();
        fin_de_lista();
    }
    
    private void fin_de_lista() {
        
    }
    
    //<enunciado> → id := <expresión> ; | Leer id ; | Escribir id ; | <enunciado_if> | <enunciado_while>
    private void enunciado() {
        if (sc.hasNextLine()){
            token = sc.nextLine();
            System.out.println("leido <enunciado>: " + token);
            
            //<enunciado> → Leer id ; | Escribir id ;
            int leer[] = lexico.Archivo.VerificarTabla("Leer");
            int escribir[] = lexico.Archivo.VerificarTabla("Escribir");
            if (String.valueOf(token.charAt(2)).equals(String.valueOf(leer[0])) ||
                    String.valueOf(token.charAt(2)).equals(String.valueOf(escribir[0]))){
            //if (token.charAt(2) == '2' || token.charAt(2) == '6') {
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
                    if (token.charAt(2) == ':' && token.charAt(3) == '=') {
                        expresion();
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
            //if (token.charAt(2) == '8') {
                enunciado_if();
                return;
            }
            
            //<enunciado> → <enunciado_while>
            int mientras[] = lexico.Archivo.VerificarTabla("Mientras");
            if (String.valueOf(token.charAt(2)).equals(String.valueOf(mientras[0]))){
            //if (token.charAt(2) == '7') {
                enunciado_while();
                return;
            }
        }
        //si no es Leer ó Escribir.
        Error("<enunciado>", "Leer ó Escribir ó Identificador o´ Si ó Mientras");
    }
    
    private void condicion() {
        
    }
    
    private void op_rel() {
        
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
    
    private void factor_simple() {
        
    }
    
    private void enunciado_if() {
        
    }
    
    private void fin_if() {
        
    }
    
    private void enunciado_while() {
        
    }
    
    private void Error(String noTerminal, String error) {        
        System.out.println("Error en " + noTerminal + " se esperaba " + error);        
        pocoCodigo = true;
//        System.out.println("Código no válido");
//        sc.close();
//        System.exit(0);
    }
}
