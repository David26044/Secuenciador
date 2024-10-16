/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mundo;

/**
 *
 * @author ACER
 */
public class Instruccion {

    String contenido;
    int linea;
    int salto;
    Tipo tipo;

    public Instruccion(String comando, int linea, Tipo tipo) {
        this.contenido = comando;
        this.linea = linea;
        salto = 0;
        this.tipo = tipo;
    }

    //Constructor usado para el JUMP de while ya que esta es la unica instruccion de la que se sabe su salto al momento de su creacion
    public Instruccion(String contenido, int linea, int salto, Tipo tipo) {
        this.contenido = contenido;
        this.linea = linea;
        this.salto = salto;
        this.tipo = tipo;
    }
    
    

    public enum Tipo {
        IF, FOR, WHILE, ELSE, ASIGNACION
    }

    public void nose(int lineaJ) {
        switch (tipo) {
            case IF:
                caseIF(lineaJ);
                break;
            case FOR:
                caseFOR(lineaJ);
                break;
            case WHILE:
                caseWHILE(lineaJ);
                break;
            case ELSE:
                caseELSE(lineaJ);
                break;
        }
    }

    public void caseIF(int lineaJ) {
        salto = lineaJ;
    }
    
    public void caseFOR(int lineaJ) {
        salto = lineaJ;
    }
    
    public void caseWHILE(int lineaJ) {
        salto = lineaJ;
    }

    public void setSalto(int salto) {
        this.salto = salto;
    }
    public void caseELSE(int lineaJ) {
        salto = lineaJ;
    }

    public Tipo getTipo(){
        return tipo;
    }
    
    public int getSalto(){
        return salto;
    }
    
    @Override
    public String toString() {
        return linea + "    " + contenido + "     " + salto;
    }

}
