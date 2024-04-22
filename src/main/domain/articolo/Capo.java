package main.domain.articolo;

import main.domain.Fornitore;

public class Capo {
	
	private int id;
	private String nome;
	private Fornitore fornitore;
	
	public Capo(int id, String nome, Fornitore fornitore) {
		this.id = id;
		this.nome = nome;
		this.fornitore = fornitore;
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

	public Fornitore getFornitore() {
		return fornitore;
	}

	public void setFornitore(Fornitore fornitore) {
		this.fornitore = fornitore;
	}

	@Override
	public String toString() {
		return "Capo [id=" + id + ", nome=" + nome + ", fornitore=" + fornitore + "]";
	}
	
}
