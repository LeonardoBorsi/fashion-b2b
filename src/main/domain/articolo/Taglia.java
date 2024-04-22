package main.domain.articolo;

public class Taglia {
	
	private int id;
	private String nome;

	public Taglia(int id, String nome) {
		this.id = id;
		this.nome = nome;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public String toString() {
		return "Taglia [id=" + id + ", nome=" + nome + "]";
	}
	
}
