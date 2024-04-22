package main.orm.dao.articolo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import main.domain.articolo.Articolo;
import main.domain.articolo.ArticoloMagazzino;
import main.orm.Database;

public class ArticoloMagazzinoDAO {
	
	public static ArticoloMagazzino insertArticoloMagazzino(Articolo articolo, int qnt) throws ClassNotFoundException, SQLException {		
		Database.insert(Database.Table.articoli_magazzino, Map.of(
				"id_articolo", articolo.getId(),
				"qnt_disponibile", qnt
			));
		return getArticoloMagazzinoByArticoloID(articolo.getId());
	}
	
	public static ArticoloMagazzino updateArticoloMagazzino(ArticoloMagazzino articolo) throws ClassNotFoundException, SQLException {		
		StringBuilder sqlBuilder = new StringBuilder("UPDATE ")
				.append(Database.Table.articoli_magazzino)
				.append(" SET qnt_disponibile = ")
				.append(articolo.getQntDisponibile())
				.append(" WHERE id_articolo = ")
				.append(articolo.getId());
		Database.rawUpdate(sqlBuilder.toString());
		return getArticoloMagazzinoByArticoloID(articolo.getId());
	}
	
	public static ArticoloMagazzino getArticoloMagazzinoByArticoloID(int idArticolo) throws ClassNotFoundException, SQLException {		
		StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM ")
			.append(Database.Table.articoli_magazzino)
			.append(" WHERE id_articolo = ")
			.append(idArticolo);
		
		ResultSet rs = Database.rawQuery(sqlBuilder.toString());
		if (rs.next()) {
			Articolo articolo = ArticoloDAO.getArticolo(idArticolo);
			return new ArticoloMagazzino(
				articolo,
				rs.getInt("qnt_disponibile")
			);
	    }
		
		return null;
	}
}
