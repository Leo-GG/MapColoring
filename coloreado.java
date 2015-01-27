/**
* @mainpage Coloreado de Grafos
*
* <b>Función del programa</b>: <p>Lee un grafo que representa un mapa en el
* plano y lo colorea de manera que no haya dos nodos conectados que tengan
* el mismo color utilizando para ello el menor número de colores posible y
* como máximo un número dado de colores</p>
*
* <b>Datos de entrada</b>: <p>El fichero de datos de entrada consta de una
* linea que indica el número máximo de colores que se pueden usar, una
* segunda linea con el número de nodos del grafo y a continuación una matriz
* de adyacencia que describe el grafo problema</p>
*
* <b>Datos de salida</b>: <p>El programa imprime una lista de nodos con el
* color que se ha usado para cada uno de ellos</p>
*
* <b>Uso</b>: <p>$java coloreado [-t][-h] [fichero_entrada] [fichero_salida]
* </p>
*              
* @author Leonardo Garma
* @version 0.2.0 09/01/2015
*/

/**
* @file coloreado.java
* @brief Contiene la clase coloreado y el programa principal
*
* Las funciones para colorear el grafo y generar los datos de salida están
* implimentadas en este archivo. 
*/

import java.util.Arrays;
import java.io.*;

/**
* @class coloreado
* Clase que contiene los métodos para colorear el grafo y generar los datos
* de salida
*/
public class coloreado {
	
	private int numColors_,numNodes_;
	private int[] colors_; 
	private boolean[][] graph_;
	private boolean trace_;
	private boolean showHelp_;
	private	boolean solved;
	private boolean writeToFile_;
	private File outputFile_;
	private PrintWriter writer_;
	private String[] colorNames_;

        /**
        * Constructor
        * @param graph La matriz de adyacencia del grafo
	* @param numColors El numero maximo de colores que se pueden usar
	* @param trace El estado de la opcion traza
	* @param outputFile El nombre del archivo de salida
        */
	public coloreado(boolean[][] graph, int numColors,
			 boolean trace, String outputFile){
		// Inicializa los valores de numero de nodos y colores y 
		// el grafo usando los valores de entrada
		int maxColors=numColors;
		graph_=graph;
		numNodes_=graph_.length;

		// Abre el fichero de salida si es necesario
		if (outputFile!=null){
		        outputFile_ = new File(outputFile);
			writeToFile_=true;
		        try {
				writer_ = new PrintWriter(outputFile_,"UTF-8"); 
			}catch ( IOException e ) {
          			 e.printStackTrace();
			}
		}
		else{
			 writeToFile_=false;
		}

		// Lee la opción traza 
		trace_=trace;
		printTraceStart();

		// Inicializa el vector solucion
		colors_=new int[numNodes_];
		
		// Define los nombres de 4 colores y el valor "ninguno" 
		// para los nodos que no se han coloreado aun
		String[] colorNames={"ninguno","amarillo","verde","rojo","azul"};
		colorNames_=colorNames;	

		// Trata de resolver el problema con el menor número posible
		// de colores.
		for (numColors_=1;numColors_<=maxColors;numColors_++){
			// Colorea el grafo
			colorNode(0,numColors_);

			// Informa del resultado
			if (solved){
				printTraceEnd();
				printColors();
				if (writeToFile_){
					writer_.close();
				}
				break;
			} 
		}
		if (!solved){
			printTraceEnd();
			if (writeToFile_){
				writer_.print("No hay solucion para "
					+numColors_+" colores\n");
				writer_.close();
			}
			else{
				System.out.print("No hay solucion para "
					+numColors_+" colores\n");
			}
		}
	}

        /**
        * Verifica si es posible colorear el nodo indicado con el primer color
	* de la lista. Si no es posible pasa al siguiente. Al encontrar una
	* opción válida hace una llamada recursiva con el siguiente nodo. Si no
	* es posible colorear i+1, se le asigna el siguiente color de la lista
	* a i. El bucle acaba cuando o bien se han coloreado todos los nodos o
	* se han agotado las posibles combinaciones de colores (sin haber 
	* alcanzado el nodo final).
	* @param node Nodo a colorear
	*/
	public void colorNode (int node, int maxColors) {
		if (node==numNodes_){
			solved=true;
			return;
		}
		for (int color=1;color<=maxColors;color++){
			//Comprueba que se pueda asignar el color
			if (checkValid(node,color)){
				colors_[node]=color;
				if (trace_) printColors();
				//Colorea el siguiente nodo
				colorNode(node+1,maxColors);
				if (solved) return;
				colors_[node]=0;
			}
		}
	}

        /**
        * Comprueba si un nodo dado puede colorearse con un color específico o
	* si por el contrario el nodo esta conectado a otro que ya tiene 
	* asignado ese color.
	* @param node Nodo a comprobar
	* @param color Color a comprobar
	*/
	public boolean checkValid(int node, int color){
		for (int i =0; i< numNodes_; i++){
			if (graph_[node][i] && color==colors_[i]){
				return false;
			}
		}
		return true;
	}
	
        /**
        * Imprime la lista de nodos y el color que cada uno tiene asignado.
	*/
	public void printColors(){
		if (writeToFile_){
			for (int i=0;i<numNodes_;i++){
				writer_.print((i+1)+" "+
				colorNames_[colors_[i]]+"\n");
			}
			writer_.print("\n");
		}
		else{
			for (int i=0;i<numNodes_;i++){
				System.out.print((i+1)+" "+
				colorNames_[colors_[i]]+"\n");
			}
			System.out.print("\n");
		}
	}

        /**
        * Imprime el grafo problema.
	*/
	public void printGraph(){
		for (int i=0;i<numNodes_;i++){
			for (int j=0;j<numNodes_;j++){
				String s = graph_[i][j] ? "1" : "0";
				System.out.print(s+" ");
			}
			System.out.print("\n");
		}
	}

        /**
        * Imprime el comienzo de la traza
	*/
	private void printTraceStart(){
		if (trace_){						
			if (writeToFile_){
				writer_.print("Traza:\n");
			}
			else{
				System.out.print("Traza:\n");
			}
		}
	}

        /**
        * Imprime el final de la traza
	*/
	private void printTraceEnd(){
		if (trace_){						
			if (writeToFile_){
				writer_.print("Fin de traza\n\n");
			}
			else{
				System.out.print("Fin de traza\n\n");
			}
		}
	}

        /**
        * Programa principal. Crea una instancia del lector de datos de 
	* entrada, con los argumentos del programa y recibe de este el grafo
	* problema, el numero de colores y el valor de la opción traza.
	* Crea un objeto coloreado con los datos de entrada que resuelve el
	* problema con los parámetros dados.
	* @param args Argumentos con los que se ha ejecutado el programa
	*/
	public static void main(String[] args) {
		InputHandler IHandler = new InputHandler(args);
		boolean[][] inputGraph=IHandler.getGraph();
		int N=IHandler.getNumColors();
		boolean trace=IHandler.getTrace();
		String outputFile=IHandler.getOutFile();
		coloreado colorGraph = new coloreado(inputGraph,N,trace,outputFile);		

	}
}
