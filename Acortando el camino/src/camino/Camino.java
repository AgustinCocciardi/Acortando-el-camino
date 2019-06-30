package camino;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Camino {

	private int bifurcaciones;
	private int abiertas;
	private int obstruidas;
	private Integer bifurcacionInicial;
	private Integer bifurcacionFinal;
	private PriorityQueue<Galeria> galeriasObstruidas = new PriorityQueue<Galeria>();
	private ArrayList<Galeria> galeriasUsadas = new ArrayList<Galeria>();
	private ArrayList<Integer> galeriasPorLimpiar = new ArrayList<Integer>();
	private int[][] matrizAdyacencia1;
	private int[][] matrizAdyacencia2;
	private static final int SIN_GALERIAS = 1;
	private static final int CON_GALERIAS = 4;
	private static final int UNA_GALERIA = 2;
	private static final int DOS_GALERIAS = 3;
	private ArrayList<Integer> noSolucion = new ArrayList<Integer>();
	private int[] precedencias;

	public Camino(Scanner entrada) {
		int nodo1, nodo2, costo;
		this.bifurcaciones = entrada.nextInt();
		this.abiertas = entrada.nextInt();
		this.obstruidas = entrada.nextInt();
		this.bifurcacionInicial = 0;
		this.bifurcacionFinal = this.bifurcaciones - 1;
		this.matrizAdyacencia1 = new int[this.bifurcaciones][this.bifurcaciones];
		this.matrizAdyacencia2 = new int[this.bifurcaciones][this.bifurcaciones];
		this.precedencias = new int[this.bifurcaciones];
		for(int[] rows : matrizAdyacencia1)
			Arrays.fill(rows, Integer.MAX_VALUE);
		for(int[] rows : matrizAdyacencia2)
			Arrays.fill(rows, Integer.MAX_VALUE);
		for(int i=0; i < this.abiertas; i++) {
			nodo1 = entrada.nextInt() - 1;
			nodo2 = entrada.nextInt() - 1;
			costo = entrada.nextInt();
			if(this.matrizAdyacencia1[nodo1][nodo2] > costo)
				this.matrizAdyacencia1[nodo1][nodo2] = costo;
			if(this.matrizAdyacencia2[nodo1][nodo2] > costo)
				this.matrizAdyacencia2[nodo1][nodo2] = costo;
		}
		for(int i=0; i < this.obstruidas; i++) {
			nodo1 = entrada.nextInt() - 1;
			nodo2 = entrada.nextInt() - 1;
			costo = entrada.nextInt();
			this.galeriasObstruidas.offer(new Galeria(nodo1,nodo2,costo));
			if(this.matrizAdyacencia2[nodo1][nodo2] > costo)
				this.matrizAdyacencia2[nodo1][nodo2] = costo;
		}
	}

	private void prepararLista() {
		for (int i = 0; i < this.bifurcaciones ; i++)
			this.noSolucion.add(i);
	}

	private void primerDijkstra() {
		this.prepararLista();
		Integer nodoW;
		this.noSolucion.remove(bifurcacionInicial);
		while(this.noSolucion.isEmpty() == false) {
			nodoW = this.noSolucion.get(0);
			for(int i=1; i<this.noSolucion.size(); i++) {
				if(this.matrizAdyacencia1[bifurcacionInicial][nodoW] > 
				   this.matrizAdyacencia1[bifurcacionInicial][this.noSolucion.get(i)]) {
					nodoW = this.noSolucion.get(i);
				}
			}
			this.noSolucion.remove(nodoW);
			for(int i=0; i<this.noSolucion.size(); i++) {
				if(matrizAdyacencia1[nodoW][this.noSolucion.get(i)] != Integer.MAX_VALUE) {
					if(matrizAdyacencia1[bifurcacionInicial][this.noSolucion.get(i)] > 
					   (matrizAdyacencia1[bifurcacionInicial][nodoW] + matrizAdyacencia1[nodoW][this.noSolucion.get(i)])) {
						matrizAdyacencia1[bifurcacionInicial][this.noSolucion.get(i)] = (matrizAdyacencia1[bifurcacionInicial][nodoW] + matrizAdyacencia1[nodoW][this.noSolucion.get(i)]);
					}
				}
			}
		}
	}

	private int segundoDijkstraYDecision() {
		this.prepararLista();
		Arrays.fill(precedencias, bifurcacionInicial);
		Integer nodoW;
		this.noSolucion.remove(bifurcacionInicial);
		while(this.noSolucion.isEmpty() == false) {
			nodoW = this.noSolucion.get(0);
			for(int i=1; i<this.noSolucion.size(); i++) {
				if(this.matrizAdyacencia2[bifurcacionInicial][nodoW] > 
				   this.matrizAdyacencia2[bifurcacionInicial][this.noSolucion.get(i)]) {
					nodoW = this.noSolucion.get(i);
				}
			}
			this.noSolucion.remove(nodoW);
			for(int i=0; i<this.noSolucion.size(); i++) {
				if(matrizAdyacencia2[nodoW][this.noSolucion.get(i)] != Integer.MAX_VALUE) {
					if(matrizAdyacencia2[bifurcacionInicial][this.noSolucion.get(i)] > 
					   (matrizAdyacencia2[bifurcacionInicial][nodoW] + matrizAdyacencia2[nodoW][this.noSolucion.get(i)])) {
						matrizAdyacencia2[bifurcacionInicial][this.noSolucion.get(i)] = (matrizAdyacencia2[bifurcacionInicial][nodoW] + matrizAdyacencia2[nodoW][this.noSolucion.get(i)]);
						this.precedencias[this.noSolucion.get(i)] = nodoW;
					}
				}
			}
		}
		if(matrizAdyacencia1[bifurcacionInicial][bifurcacionFinal] <= matrizAdyacencia2[bifurcacionInicial][bifurcacionFinal])
			return SIN_GALERIAS;
		int bifurcacion1 = this.precedencias[bifurcacionFinal], bifurcacion2 = bifurcacionFinal;
		boolean encontrada;
		while(bifurcacion1 != bifurcacion2) {
			this.galeriasUsadas.add(new Galeria(bifurcacion1,bifurcacion2));
			bifurcacion2 = bifurcacion1;
			bifurcacion1 = this.precedencias[bifurcacion2];
		}
		Collections.reverse(galeriasUsadas);
		Galeria galCola = this.galeriasObstruidas.poll(), galLista = this.galeriasUsadas.remove(0);
		while(galCola != null && galLista != null) {
			if(galCola.compararPartida(galLista) == 0 ) {
				if(galCola.compararLlegada(galLista) == 0) {
					this.galeriasPorLimpiar.add(galCola.getNumero());
					if(this.galeriasObstruidas.isEmpty() == false)
						galCola = this.galeriasObstruidas.poll();
					else
						galCola = null;
					if(this.galeriasUsadas.isEmpty() == false)
						galLista = this.galeriasUsadas.remove(0);
					else
						galLista = null;
				}
				else if(galCola.compararLlegada(galLista) < 0) {
					if(this.galeriasObstruidas.isEmpty() == false)
						galCola = this.galeriasObstruidas.poll();
					else
						galCola = null;
				}
				else {
					if(this.galeriasUsadas.isEmpty() == false)
						galLista = this.galeriasUsadas.remove(0);
					else
						galLista = null;
				}
			}
			else if(galCola.compararPartida(galLista) < 0 ){
				if(this.galeriasObstruidas.isEmpty() == false)
					galCola = this.galeriasObstruidas.poll();
				else
					galCola = null;
			}
			else {
				if(this.galeriasUsadas.isEmpty() == false)
					galLista = this.galeriasUsadas.remove(0);
				else
					galLista = null;
			}
		}
		if(this.galeriasPorLimpiar.size() == 1)
			return UNA_GALERIA;
		if(this.galeriasPorLimpiar.size() == 2)
			return DOS_GALERIAS;
		return SIN_GALERIAS;
	}

	private void resolver(PrintWriter salida) {
		int decision;
		this.primerDijkstra();
		decision = this.segundoDijkstraYDecision(); 
		if(decision == SIN_GALERIAS) {
			salida.println(SIN_GALERIAS + " " + this.matrizAdyacencia1[bifurcacionInicial][bifurcacionFinal]);
		}
		if(decision == UNA_GALERIA) {
			salida.println(UNA_GALERIA + " " + this.galeriasPorLimpiar.get(0) + " " + this.matrizAdyacencia2[bifurcacionInicial][bifurcacionFinal]);
		}
		if(decision == DOS_GALERIAS) {
			salida.println(DOS_GALERIAS + " " + this.galeriasPorLimpiar.get(0) + " " + this.galeriasPorLimpiar.get(1) + " " + this.matrizAdyacencia2[bifurcacionInicial][bifurcacionFinal]);
		}
	}

	public static void main(String[] args) throws IOException {
		Scanner entrada = new Scanner(new FileReader("ciudad2.in"));
		Camino camino = new Camino(entrada);
		entrada.close();
		PrintWriter salida = new PrintWriter(new FileWriter("ciudad2.out"));
		camino.resolver(salida);
		salida.close();
	}

}
