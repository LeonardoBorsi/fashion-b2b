package main.domain.articolo;

public class Variante {
	
	private int id;
	private String nome;
	private String colore;
	private Capo capo;
	
	public Variante(int id, String nome, String colore, Capo capo) {
		this.id = id;
		this.nome = nome;
		this.colore = colore;
		this.capo = capo;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getColore() {
		return colore;
	}

	public void setColore(String colore) {
		this.colore = colore;
	}

	public Capo getCapo() {
		return capo;
	}

	public void setCapo(Capo capo) {
		this.capo = capo;
	}

	@Override
	public String toString() {
		return "Variante [id=" + id + ", nome=" + nome + ", colore=" + colore + ", capo=" + capo + "]";
	}

}
