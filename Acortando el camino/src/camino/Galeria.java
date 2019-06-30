package camino;

public class Galeria implements Comparable<Galeria>{
	
	private int partida;
	private int llegada;
	private int costo;
	private int numero;
	private static int numeroGaleria = 1;
	
	public Galeria(int partida, int llegada) {
		this.partida = partida;
		this.llegada = llegada;
	}
	
	public Galeria(int partida, int llegada, int costo) {
		this.partida = partida;
		this.llegada = llegada;
		this.costo = costo;
		this.numero = numeroGaleria;
		numeroGaleria++;
	}
	
	public int compararPartida(Galeria galeria) {
		return this.partida - galeria.partida;
	}
	
	public int compararLlegada(Galeria galeria) {
		return this.llegada - galeria.llegada;
	}
	
	public int compararCosto(Galeria galeria) {
		return this.costo - galeria.costo;
	}
	
	public int getPartida() {
		return this.partida;
	}
	
	public int getLlegada() {
		return this.llegada;
	}
	
	public int getCosto() {
		return this.costo;
	}
	
	public int getNumero() {
		return this.numero;
	}

	@Override
	public int compareTo(Galeria galeria) {
		if((this.partida - galeria.partida) == 0 ) {
			if((this.llegada - galeria.llegada) == 0)
				return this.costo - galeria.costo;
			else
				return this.llegada - galeria.llegada;
		}
		return this.partida - galeria.partida;
	}
	
}
