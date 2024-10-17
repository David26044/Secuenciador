/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mundo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import static mundo.Instruccion.Tipo.*;

/**
 *
 * @author ACER
 */
public class Secuenciador {

    Stack stack;
    Instruccion instrucciones[];

    public Secuenciador() {
        stack = new Stack<Integer>(100);
        instrucciones = new Instruccion[100];
        leerArchivo();
    }

    public void leerArchivo() {
        try {
            String filePath = "data/ejercicio1.in";
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String linea = br.readLine();  // Inicializa antes del bucle
            int i;
            String temp = "";
            for (i = 1; linea != null; i++) {

                if (linea.contains("if")) {  //agrega una nueva instruccion con tipo IF, la agrega a la pila, actualiza linea y repite el ciclo
                    linea = manipularIf(linea);
                    instrucciones[i] = new Instruccion(linea, i, IF);  // Usa la variable en cada iteración
                    stack.push(i);
                    linea = br.readLine();

                    continue;
                }
                if (linea.contains("while") && !linea.contains(";")) {
                    linea = manipularWhile(linea);
                    instrucciones[i] = new Instruccion(linea, i, WHILE);
                    stack.push(i);
                    linea = br.readLine();
                    continue;
                }

                if (linea.contains("do")) {
                    instrucciones[i] = new Instruccion(linea, i, DO);
                    System.out.println(linea);
                    stack.push(i);
                    linea = br.readLine();
                    continue;
                }

                if (linea.contains("else")) {
                    instrucciones[i] = new Instruccion("JUMP", i, ELSE);  // Usa la variable en cada iteración
                    stack.push(i);
                    linea = br.readLine();
                    continue;
                }

                if (linea.contains("for")) {
                    String txtFor[] = manipularFor(linea);
                    instrucciones[i] = new Instruccion(txtFor[0], i, ASIGNACION);
                    i++;
                    instrucciones[i] = new Instruccion(txtFor[1], i, FOR);

                    temp = txtFor[2].substring(0, txtFor[2].length() - 1) + ";";
                    System.out.println(temp);
                    linea = br.readLine();
                    stack.push(i);
                    continue;
                }
                //Si contiene un cierre y la pila no está vacia:
                //actualiza la linea, procesa la instruccion en la pila
                if (linea.contains("}") && !stack.isEmpty()) {
                    int lineaEnPila = (int) stack.pop();
                    linea = br.readLine();
                    if (instrucciones[lineaEnPila].getTipo() == FOR) {
                        i = procesarInstruccion(lineaEnPila, i, temp);
                    } else {
                        i = procesarInstruccion(lineaEnPila, i, linea);
                    }
                    continue;
                }
                if (linea.contains("{") || (linea.contains("while") && linea.contains(";"))) {
                    linea = br.readLine();
                    i--;
                    continue;
                }

                instrucciones[i] = new Instruccion(linea, i, ASIGNACION);  // Usa la variable en cada iteración
                linea = br.readLine();  // Actualiza la variable al final del ciclo
            }
            // arreglarIF(i);
            for (i = 0; i < 50; i++) {
                System.out.println(instrucciones[i]);
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    //Cuando se encuentra un "}" se llama a este metodo que a la linea en la pila le llama al metodo nose, que va a 
    //cambiar la linea de salto 
    public int procesarInstruccion(int lineaEnPila, int lineaActual, String nextLinea) {
        int i = 0;
        switch (instrucciones[lineaEnPila].getTipo()) {
            case IF:
                i = caseIF(lineaActual, lineaEnPila, nextLinea);
                break;
            case FOR:
                i = caseFOR(lineaActual, lineaEnPila, nextLinea);
                break;
            case WHILE:
                i = caseWHILE(lineaActual, lineaEnPila);
                break;
            case ELSE:
                i = caseELSE(lineaActual, lineaEnPila);
                break;
            case DO:
                i = CaseDOWHILE(lineaActual, lineaEnPila, nextLinea);
                break;
        }
        return i;
    }

    //retorna la lineaActual-1 para que se ignore el } de este
    public int caseIF(int lineaActual, int lineaEnPila, String nextLinea) {
        if (nextLinea != null) {
            if (nextLinea.contains("else")) {
                //Porque si la siguiente linea del que cierra el if es un else entonces el jump debe ser a la siguiente
                instrucciones[lineaEnPila].setSalto(lineaActual + 1);
                return lineaActual-1;
            } 
        }
        instrucciones[lineaEnPila].setSalto(lineaActual);
        return lineaActual - 1;
    }

    public int caseFOR(int lineaActual, int lineaEnPila, String lineaIncremento) {
        instrucciones[lineaEnPila].setSalto(lineaActual+2);
        System.out.println("Linea incremento" + lineaIncremento);
        instrucciones[lineaActual] = new Instruccion(lineaIncremento, lineaActual, ASIGNACION);
        instrucciones[lineaActual + 1] = new Instruccion("JUMP", lineaActual + 1, lineaEnPila, ASIGNACION);
        return lineaActual + 1;
    }

    //se retorna la linea actual porque el } se cambia por JUMP asi que no se ignora, se cambia 
    public int caseWHILE(int lineaActual, int lineaEnPila) {
        instrucciones[lineaEnPila].setSalto(lineaActual + 1);
        instrucciones[lineaActual] = new Instruccion("JUMP", lineaActual, lineaEnPila, ASIGNACION);
        return lineaActual;
    }

    public int caseELSE(int lineaActual, int lineaEnPila) {
        instrucciones[lineaEnPila].setSalto(lineaActual);
        return lineaActual - 1;
    }

    public int CaseDOWHILE(int lineaActual, int lineaEnPila, String lineaCondicion) {
        instrucciones[lineaActual] = new Instruccion(manipularWhile(lineaCondicion), lineaActual, lineaEnPila, WHILE);
        System.out.println("Se agregó: " + manipularWhile(lineaCondicion));
        return lineaActual;
    }

    public String manipularIf(String linea) {
        String temp;
        String[] partes;
        partes = linea.split("\\(");
        temp = "";
        for (int i = 1; i < partes.length; i++) {
            temp += partes[i];
        }
        temp = temp.substring(0, temp.length() - 1);
        return temp;
    }

    public String manipularWhile(String linea) {
        String temp = manipularIf(linea);
        return temp.substring(0, temp.length() - 1);
    }

    public String[] manipularFor(String linea) {
        String temp = "";
        String partes1[];
        String partes2[];
        partes1 = linea.split("\\(");
        partes2 = partes1[1].split(";");

        return partes2;
    }

    public static void main(String[] args) {
        Secuenciador secuenciador = new Secuenciador();
    }

}
