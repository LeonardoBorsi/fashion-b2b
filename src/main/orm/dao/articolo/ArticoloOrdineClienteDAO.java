package main.orm.dao.articolo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import main.domain.articolo.Articolo;
import main.domain.articolo.ArticoloOrdineCliente;
import main.orm.Database;


public class ArticoloOrdineClienteDAO {
	
	public static void insertOrdineClienteArticoli(int idOrdineCliente, List<ArticoloOrdineCliente> articoli) throws ClassNotFoundException, SQLException {
		for(ArticoloOrdineCliente articolo: articoli) {
			Database.insert(Database.Table.articoli_ordine_cliente, Map.of(
				"id_ordine_cliente", idOrdineCliente,
				"id_articolo", articolo.getId(),
				"qnt_ordinata_dal_cliente", articolo.getQntOrdinataDalCliente(),
				"qnt_magazzino", articolo.getQntMagazzino(),
				"qnt_ordinata_al_fornitore", articolo.getQntOrdinataAlFornitore(),
				"qnt_arrivata_dal_fornitore", articolo.getQntArrivataDalFornitore()
			));
		}
	}
	
	public static void updateArticoloOrdineCliente(int idOrdineCliente, ArticoloOrdineCliente articolo) throws ClassNotFoundException, SQLException {
		StringBuilder sqlBuilder = new StringBuilder("UPDATE ")
				.append(Database.Table.articoli_ordine_cliente)
				.append(" SET qnt_ordinata_dal_cliente = ")
				.append(articolo.getQntOrdinataDalCliente())
				.append(", qnt_magazzino = ")
				.append(articolo.getQntMagazzino())
				.append(", qnt_ordinata_al_fornitore = ")
				.append(articolo.getQntOrdinataAlFornitore())
				.append(", qnt_arrivata_dal_fornitore = ")
				.append(articolo.getQntArrivataDalFornitore())
				.append(" WHERE id_ordine_cliente = ")
				.append(idOrdineCliente)
				.append(" AND id_articolo = ")
				.append(articolo.getId());
		Database.rawUpdate(sqlBuilder.toString());
	}

	
	public static List<ArticoloOrdineCliente> getArticoliOrdineClinteByOrdineClienteId(int idOrdineCliente) throws ClassNotFoundException, SQLException {
		List<ArticoloOrdineCliente> articoliOrdineCliente = new ArrayList<>();
		StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM ")
			.append(Database.Table.articoli_ordine_cliente)
			.append(" WHERE id_ordine_cliente = ")
			.append(idOrdineCliente);
		ResultSet rs = Database.rawQuery(sqlBuilder.toString());
		while (rs.next()) {
			Articolo articolo = ArticoloDAO.getArticolo(rs.getInt("id_articolo"));
			articoliOrdineCliente.add(new ArticoloOrdineCliente(
				articolo,
				rs.getInt("qnt_ordinata_dal_cliente"),
				rs.getInt("qnt_magazzino"),
				rs.getInt("qnt_ordinata_al_fornitore"),
				rs.getInt("qnt_arrivata_dal_fornitore")
			));
	    }
		return articoliOrdineCliente;
	}
	
	public static ArticoloOrdineCliente getArticoloOrdineClinteByOrdineClienteIdAndArticoloID(int idOrdineCliente, int idArticolo) throws ClassNotFoundException, SQLException {
		StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM ")
			.append(Database.Table.articoli_ordine_cliente)
			.append(" WHERE id_ordine_cliente = ")
			.append(idOrdineCliente)
			.append(" AND id_articolo = ")
			.append(idArticolo);
		ResultSet rs = Database.rawQuery(sqlBuilder.toString());
		if (rs.next()) {
			Articolo articolo = ArticoloDAO.getArticolo(rs.getInt("id_articolo"));
			return new ArticoloOrdineCliente(
				articolo,
				rs.getInt("qnt_ordinata_dal_cliente"),
				rs.getInt("qnt_magazzino"),
				rs.getInt("qnt_ordinata_al_fornitore"),
				rs.getInt("qnt_arrivata_dal_fornitore")
			);
	    }
		return null;
	}
		
	public static void deleteAllArticoliOrdineClinteByOrdineClienteId(int idOrdineCliente) throws ClassNotFoundException, SQLException {
		StringBuilder sqlBuilder = new StringBuilder("DELETE FROM ")
			.append(Database.Table.articoli_ordine_cliente)
			.append(" WHERE id_ordine_cliente = ")
			.append(idOrdineCliente);
		Database.rawUpdate(sqlBuilder.toString());
	}
	
	
}
