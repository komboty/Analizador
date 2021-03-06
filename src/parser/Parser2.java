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
        
        //si es Inicio.
        if (String.valueOf(token.charAt(2)).equals(String.valueOf(inicio[0]))){
            if (sc.hasNextLine()) {
                token = sc.nextLine();
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

            //si es un identificador.
            if (token.charAt(0) == '3') {
                if (sc.hasNextLine()) {
                    token = sc.nextLine();
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
            
            //<fin_de_lista> → ε
            int fin[] = lexico.Archivo.VerificarTabla("Fin");
            if (String.valueOf(token.charAt(2)).equals(String.valueOf(fin[0]))){            
                return;
            }               
            
            //<fin_de_lista> → <enunciado> <fin_de_lista>
            enunciado();
            fin_de_lista();
        }
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
                
                //si es Identificador.
                if (token.charAt(0) == '3') {
                    if (sc.hasNextLine()){
                        token = sc.nextLine();
                        
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
                
                //si es dos puntos e igual.
                if (token.charAt(0) == '7') {
                    expresion();
                    
                    //si es dos puntos.
                    if (token.charAt(2) == ';') {
                        return;
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
        if (sc.hasNextLine()){
            token = sc.nextLine();
            factor_simple();
            op_rel();
            
            if (sc.hasNextLine()){
                token = sc.nextLine();
                factor_simple();
            }
        }
    }
    
    //<op rel> → > | < | = | <= | >=
    private void op_rel() {
        if (sc.hasNextLine()){
            token = sc.nextLine();
            if (token.charAt(0) == '6') {
                return;
            }
        }
        //si no es Leer ó Escribir.
        Error("<op_rel>", "> ó < ó = ó <= ó >=");
    }
    
    //<expresión> → <término><expresión_simple>
    private void expresion() {
        termino();
        expresion_simple();
    }
    
    //<expresión_simple> → + < término >< expresión_simple> | - <término >< expresión_simple> | ε
    private void expresion_simple() {
        //<expresión_simple> → ε
        if (token.charAt(2) == ')' || token.charAt(2) == ';') {
            return;
        }

        //<término simple> → + < término >< expresión_simple> | - < término >< expresión_simple>
        if (token.charAt(2) == '+' || token.charAt(2) == '-') {
            termino();
            expresion_simple();
            return;
        }
        
        //si no hay más lineas.
        Error("<expresion_simple>", "+ ó - ó ) ó ; ");         
    }
    
    //<término> → <factor><término_simple>
    private void termino() {
        factor();
        termino_simple();
    }
    
    //<término simple> → * <factor><término_simple> | / <factor><término_simple> | ε
    private void termino_simple() {
        if (sc.hasNextLine()){
            token = sc.nextLine();
            
            //<término simple> → ε
            if (token.charAt(2) == '+' || token.charAt(2) == '-' || token.charAt(2) == ')' || token.charAt(2) == ';') {
                return;
            }

            //<término simple> → / <factor><término_simple> | * <factor><término_simple>
            if (token.charAt(2) == '/' || token.charAt(2) == '*') {
                factor();
                termino_simple();
                return;
            }
        }
        
        //si no hay más lineas.
        Error("<termino_simple>", "* ó / + ó - ó ) ó ; ");        
    }
    
    //<factor> → ( <expresión> ) | <factor_simple>
    private void factor() {
        if (sc.hasNextLine()){
            token = sc.nextLine();
            
            //<factor> → <factor_simple>
            if (token.charAt(0) == '3' || token.charAt(0) == '1') {
                factor_simple();
                return;
            }
            
            //<factor> → ( <expresión> )
            if (token.charAt(2) == '(') {
                expresion();
                
                //si es )
                if (token.charAt(2) == ')') {
                    return;   
                }
                Error("<factor>", ")");
                return;
            }
        }
        //si no hay más lineas.
        Error("<factor>", "( ó Identificador ó Constante entera");
    }
    
    //<factor_simple> → id | entero
    private void factor_simple() {
        if (token.charAt(0) == '3' || token.charAt(0) == '1') {
            return;
        }
        
        //si no es Leer ó Escribir.
        Error("<factor_simple>", "Identificador o´ Constante entera");
    }
    
    //<enunciado_if> → Si <condición> Entonces <enunciado><fin_if>
    private void enunciado_if() {
        condicion();
        
        if (sc.hasNextLine()){                            
            token = sc.nextLine();
            
            //si es Entonces.
            int entonces[] = lexico.Archivo.VerificarTabla("Entonces");            
            if (String.valueOf(token.charAt(2)).equals(String.valueOf(entonces[0]))){
                if (sc.hasNextLine()){                            
                    token = sc.nextLine();
                    enunciado();
                    
                    if (sc.hasNextLine()){                            
                        token = sc.nextLine();

                        fin_if();
                        return;
                    }    
                }
                return;
            }            
        }
        //si no es Si.
        Error("<enunciado_if>", "Si");
    }
    
    //<fin_if> → Fin | Sino <enunciado> Fin
    private void fin_if() {
        //<fin_if> → Fin
        int fin[] = lexico.Archivo.VerificarTabla("Fin");
        if (String.valueOf(token.charAt(2)).equals(String.valueOf(fin[0]))) {
            return;
        }
        
        //<fin_if> → Sino <enunciado> Fin
        int sino[] = lexico.Archivo.VerificarTabla("Sino");
        if (String.valueOf(token.charAt(2)).equals(String.valueOf(sino[0]))) {
            if (sc.hasNextLine()){                            
                token = sc.nextLine();
                enunciado();

                if (sc.hasNextLine()){                            
                    token = sc.nextLine();

                    //si es Fin.
                    if (String.valueOf(token.charAt(2)).equals(String.valueOf(fin[0]))){
                        return;
                    }
                    Error("<fin_if>", "Fin");
                }
                return;
            }            
        }
        
        //si no es Fin ó Sino.
        Error("<fin_if>", "Fin ó Sino");
    }
    
    //<enunciado_while> → Mientras <condición> Hacer <enunciado> Fin
    private void enunciado_while() {
        condicion();
        
        if (sc.hasNextLine()){                            
            token = sc.nextLine();
            
            //si es Hacer.
            int hacer[] = lexico.Archivo.VerificarTabla("Hacer");            
            if (String.valueOf(token.charAt(2)).equals(String.valueOf(hacer[0]))){
                if (sc.hasNextLine()){                            
                    token = sc.nextLine();
                    enunciado();
                    
                    if (sc.hasNextLine()){                            
                        token = sc.nextLine();

                        //si es Hacer.
                        int fin[] = lexico.Archivo.VerificarTabla("Fin");            
                        if (String.valueOf(token.charAt(2)).equals(String.valueOf(fin[0]))){
                            return;
                        }
                        Error("<enunciado_while>", "Fin");
                    }    
                }
                //Error se esperaba Leer ó Escribir ó Identificador o´ Si ó Mientras
                return;
            }            
        }
        //si no es Hacer.
        Error("<enunciado_while>", "Hacer");
    }
    
    private void Error(String noTerminal, String error) {        
        System.out.println("Error en " + noTerminal + " se esperaba " + error);        
        pocoCodigo = true;
    }
}

