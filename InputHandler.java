/**
* @file InputHandler.java
* @brief Contiene la clase InputHandler 
*
* Esta clase recibe los argumentos con los que se ha ejecutado el programa,
* muestra la ayuda del programa si se especifica, lee los datos de entrada
* y genera una matriz de booleans basada en la matriz de adyacencia de entrada.
* Además genera errores si los argumentos no son válidos, si el fichero de
* entrada no existe o si ya existe el fichero de salida especificado.
*/

import java.util.Arrays;
import java.io.*;

/**
* @class InputHandler
* Lee datos de entrada e interpreta las opciones del programa. Verifica
* que todos los parámetros de entrada sean válidos.
*/
public class InputHandler {

	private int numColors_,numNodes_;
	//private int[] colors_; 
	private boolean[][] graph_;
	private boolean trace_;
	private boolean help_;
	private boolean valid_;
	private String inputFile_,outputFile_;
	
	public InputHandler(String[] args) {

		if (args.length>4){
			throw new IllegalArgumentException(
				"Demasiados argumentos (>4)!");
		}
		
		int nOptions=0;
		int nArguments=args.length;

		for (int i=0;i<args.length;i++){
			if (args[i].equals("-h")){
				help_=true;
				showHelp();
				System.exit(0);
			}
			if (args[i].equals("-t")){
				trace_=true;
				nOptions++;
			}
		}

		nArguments=args.length-nOptions;
		if (nArguments>2){
			throw new IllegalArgumentException(
				"Argumentos invalidos");
		}
		inputFile_=args[nOptions];
		outputFile_=null;
		if (nArguments==2){
			outputFile_=args[nOptions+1];
			testOutput();
		}

		testInput();
		readFile(inputFile_);
		testGraph();
	}		

        /**
	* Lee la lista de matrices del fichero de datos de entrada y genera
	* un vector de matrices a partir de ellos. Genera errores si el 
	* fichero de entrada no existe o no se puede leer.
	* @param inputFile Nombre del archivo de datos de entrada
        */
	private void readFile(String inputFile){
		BufferedReader br = null;

 		try {
 			String sCurrentLine;
 			br = new BufferedReader(new FileReader(inputFile));

			sCurrentLine = br.readLine();
			String[] lineVals = sCurrentLine.split(" ");
			numColors_=Integer.parseInt(lineVals[0]);

			sCurrentLine = br.readLine();
			lineVals = sCurrentLine.split(" ");
			numNodes_=Integer.parseInt(lineVals[0]);
			// Inicializa el grafo y lee la matriz de adyacencia
			graph_=new boolean[numNodes_][numNodes_];
			for (int i=0; i<numNodes_;i++){
				sCurrentLine = br.readLine();
				lineVals = sCurrentLine.split(" ");
				for (int j=0;j<numNodes_;j++){
				int inpVal = Integer.parseInt(lineVals[j]);	
				graph_[i][j] = (inpVal==1) ? true : false;
				}
			}
 		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null){
					br.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

        /**
	* Comprueba que el fichero de entrada exista y que no sea un directorio
        */
	public void testInput(){
		File f = new File(inputFile_);
			if(!f.exists() || f.isDirectory()){
				throw new 
				IllegalArgumentException(
				"El fichero de entrada no existe");
			}
	}

        /**
	* Comprueba que el fichero de salida no exista y que no sea un 
	* directorio
        */
	public void testOutput(){
		File f = new File(outputFile_);
			if(f.exists() && !f.isDirectory()){
				throw new 
				IllegalArgumentException(
				"El fichero de salida ya existe");
			}
	}

        /**
	* Comprueba que el grafo sea simetrico
        */
	public void testGraph(){
		for (int i=0;i<numNodes_;i++){
			for (int j=0;j<numNodes_;j++){
				if (graph_[i][j]!=graph_[j][i]){
					throw new IllegalArgumentException(
					"El grafo no es simétrico");
				}
			}
		}
	}

        /**
        * Devuelve la matriz de adyacencia del grafo
	* @return graph
        */
	public boolean[][] getGraph(){
		return graph_;
	}

        /**
        * Devuelve el numero maximo de colores permitido
	* @return numColors
        */
	public int getNumColors(){
		return numColors_;
	}

        /**
        * Devuelve el numero de vertices del grafo
	* @return numNodes
        */
	public int getNumNodes(){
		return numNodes_;
	}

        /**
        * Devuelve el estado de la opcion traza
	* @return trace
        */
	public boolean getTrace(){
		return trace_;
	}

        /**
        * Devuelve el estado de la opcion ayuda
	* @return help
        */
	public boolean getHelp(){
		return help_;
	}

        /**
        * Devuelve el nombre del fichero de salida
	* @return outputFile
        */
	public String getOutFile(){
		return outputFile_;
	}

        /**
        * Muestra la ayuda del programa
        */
	public void showHelp(){
		System.out.print("\nSINTAXIS:\n");
		System.out.print("coloreado [-t] [-h] [fichero_entrada] [fichero_salida]\n");
		System.out.print("-t\t\tTraza la asignación de colores\n");
		System.out.print("-h\t\tMuestra esta ayuda\n");
		System.out.print("fichero_entrada\tNombre del fichero de entrada\n");
		System.out.print("fichero_salida\tNombre del fichero de salida\n\n");
	}
}
