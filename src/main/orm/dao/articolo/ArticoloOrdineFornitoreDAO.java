package main.orm.dao.articolo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import main.domain.articolo.Articolo;
import main.domain.articolo.ArticoloOrdineFornitore;
import main.orm.Database;

public class ArticoloOrdineFornitoreDAO {

	public static void insertOrdineFornitoreArticoli(int idOrdineFornitore, List<ArticoloOrdineFornitore> articoli) throws ClassNotFoundException, SQLException {
		for(ArticoloOrdineFornitore articolo: articoli) {
			Database.insert(Database.Table.articoli_ordine_fornitore, Map.of(
				"id_ordine_fornitore", idOrdineFornitore,
				"id_articolo", articolo.getId(),
				"qnt_ordinata", articolo.getQntOrdinata(),
				"qnt_prodotta", articolo.getQntProdotta(),
				"qnt_spedita", articolo.getQntProdotta()
			));
		}
	}
	
	public static List<ArticoloOrdineFornitore> getArticoliOrdineFornitoreByOrdineFornitoreId(int idOrdineFornitore) throws ClassNotFoundException, SQLException {
		List<ArticoloOrdineFornitore> articoliOrdineFornitore = new ArrayList<>();
		StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM ")
			.append(Database.Table.articoli_ordine_fornitore)
			.append(" WHERE id_ordine_fornitore = ")
			.append(idOrdineFornitore);
		ResultSet rs = Database.rawQuery(sqlBuilder.toString());
		while (rs.next()) {
			Articolo articolo = ArticoloDAO.getArticolo(rs.getInt("id_articolo"));
			articoliOrdineFornitore.add(new ArticoloOrdineFornitore(
				articolo,
				rs.getInt("qnt_ordinata"),
				rs.getInt("qnt_prodotta"),
				rs.getInt("qnt_spedita")
			));
	    }
		return articoliOrdineFornitore;
	}
	
	public static void updateArticoloOrdineFornitore(int idOrdineFornitore, ArticoloOrdineFornitore articolo) throws ClassNotFoundException, SQLException {
		StringBuilder sqlBuilder = new StringBuilder("UPDATE ")
				.append(Database.Table.articoli_ordine_fornitore)
				.append(" SET qnt_ordinata = ")
				.append(articolo.getQntOrdinata())
				.append(", qnt_prodotta = ")
				.append(articolo.getQntProdotta())
				.append(", qnt_spedita = ")
				.append(articolo.getQntSpedita())
				.append(" WHERE id_ordine_fornitore = ")
				.append(idOrdineFornitore)
				.append(" AND id_articolo = ")
				.append(articolo.getId());
		Database.rawUpdate(sqlBuilder.toString());
	}
	
}
