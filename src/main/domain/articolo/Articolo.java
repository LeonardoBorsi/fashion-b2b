package main.domain.articolo;

public class Articolo {
	
	private int id;
	private Capo capo;
	private Variante variante;
	private Taglia taglia;
	
	public Articolo(int id, Capo capo, Variante variante, Taglia taglia) {
		this.id = id;
		this.capo = capo;
		this.variante = variante;
		this.taglia = taglia;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Capo getCapo() {
		return capo;
	}

	public void setCapo(Capo capo) {
		this.capo = capo;
	}

	public Variante getVariante() {
		return variante;
	}

	public void setVariante(Variante variante) {
		this.variante = variante;
	}

	public Taglia getTaglia() {
		return taglia;
	}

	public void setTaglia(Taglia taglia) {
		this.taglia = taglia;
	}

	@Override
	public String toString() {
		return "Articolo [id=" + id + ", capo=" + capo + ", variante=" + variante + ", taglia=" + taglia + "]";
	}
	
}
