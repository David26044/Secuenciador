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
            for (i = 0; linea != null; i++) {

                if (linea.contains("if")) {  //agrega una nueva instruccion con tipo IF, la agrega a la pila, actualiza linea y repite el ciclo
                    linea = linea.substring(0);
                    instrucciones[i] = new Instruccion(linea, i, IF);  // Usa la variable en cada iteración
                    stack.push(i);
                    linea = br.readLine();
                    continue;
                }
                if (linea.contains("while")) {
                    linea = linea.substring(0);
                    instrucciones[i] = new Instruccion(linea, i, WHILE);
                    stack.push(i);
                    linea = br.readLine();
                    continue;
                }

                if (linea.contains("else")) {
                    linea = linea.substring(0);
                    instrucciones[i] = new Instruccion("JUMP", i, ELSE);  // Usa la variable en cada iteración
                    stack.push(i);
                    linea = br.readLine();
                    continue;
                }
                //Si contiene un cierre y la pila no está vacia:
                //actualiza la linea, procesa la instruccion en la pila
                if (linea.contains("}") && !stack.isEmpty()) {
                    linea = br.readLine();
                    i = procesarInstruccion(i, linea);
                    System.out.println("Hola");
                    continue;
                }
                if (linea.contains("{")) {
                    linea = br.readLine();
                    i--;
                    continue;
                }

                instrucciones[i] = new Instruccion(linea, i, ASIGNACION);  // Usa la variable en cada iteración
                linea = br.readLine();  // Actualiza la variable al final del ciclo
            }
            arreglarIF(i);
            for (i = 0; i < 11; i++) {
                System.out.println(instrucciones[i]);
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    //Cuando se encuentra un "}" se llama a este metodo que a la linea en la pila le llama al metodo nose, que va a 
    //cambiar la linea de salto 
    public int procesarInstruccion(int lineaActual, String nextLinea) {
        int lineaEnPila = (int) stack.pop();
        int i = 0;
        switch (instrucciones[lineaEnPila].getTipo()) {
            case IF:
                System.out.println("Hola");
                i = caseIF(lineaActual, lineaEnPila, nextLinea);
                break;
            case FOR:
                i = caseFOR(lineaActual);
                break;
            case WHILE:
                i = caseWHILE(lineaActual, lineaEnPila);
                break;
            case ELSE:
                i = caseELSE(lineaActual, lineaEnPila);
                break;
        }
        return i;
    }

    /*Soluciona un problema de que al haber if adentro de if y luego else ejm:, 
    if(exp-L1-1)
    {
        if(exp-L2-2)
        {
        A;
        }
    }
    else
    {
    B;
    }
    C;
    
    El primer if se soluciona con la condicion del metodo caseIF() así que salta hacia C;
    pero, el segundo if está saltando hacia el else. Lo cual es incorrecto, debe saltar hacia C;
    y este es el mismo salto del else
     */
    public void arreglarIF(int i) {//i es la cantidad de lineas
        //los if que estan saltando a un else saltan a la siguiente linea
        for (int j = 0; j < i; j++) {
            if (instrucciones[j].tipo == IF) { // si la linea es un IF
                int salto = instrucciones[j].getSalto(); //la linea de su salto
                if (instrucciones[salto].getTipo() == ELSE) {// si esta saltando a un else
                    instrucciones[j].setSalto(instrucciones[salto].getSalto()); //Salta hacia donde está saltando ese else
                }
            }
        }
    }

    //retorna la lineaActual-1 para que se ignore el } de este
    public int caseIF(int lineaActual, int lineaEnPila, String nextLinea) {
        if (nextLinea.contains("else")) {
            //Porque si la siguiente linea del que cierra el if es un else entonces el jump debe ser a la siguiente
            instrucciones[lineaEnPila].setSalto(lineaActual + 1);
        } else {//sino, se hace normal
            instrucciones[lineaEnPila].setSalto(lineaActual);
        }
        return lineaActual - 1;
    }

    public int caseFOR(int lineaJ) {
        salto = lineaJ;
    }

    //se retorna la linea actual porque el } se cambia por JUMP asi que no se ignora, se cambia 
    public int caseWHILE(int lineaActual, int lineaEnPila) {
        instrucciones[lineaEnPila].setSalto(lineaActual);
        instrucciones[lineaActual] = new Instruccion("JUMP", lineaActual, lineaEnPila, ASIGNACION);
        return lineaActual;
    }

    public int caseELSE(int lineaActual, int lineaEnPila) {
        instrucciones[lineaEnPila].setSalto(lineaActual);
        return lineaActual - 1;
    }

    public static void main(String[] args) {
        Secuenciador secuenciador = new Secuenciador();
    }

}
