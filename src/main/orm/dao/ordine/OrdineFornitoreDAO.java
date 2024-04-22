package main.orm.dao.ordine;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import main.domain.Fornitore;
import main.domain.articolo.ArticoloOrdineFornitore;
import main.domain.ordine.OrdineCliente;
import main.domain.ordine.OrdineFornitore;
import main.domain.ordine.StatoOrdineFornitore;
import main.orm.Database;
import main.orm.dao.FornitoreDAO;
import main.orm.dao.articolo.ArticoloOrdineFornitoreDAO;

public class OrdineFornitoreDAO {

	public static OrdineFornitore insertOrdineFornitore(
		StatoOrdineFornitore stato,
		Fornitore fornitore,
		OrdineCliente ordineCliente,
		List<ArticoloOrdineFornitore> articoli
	) throws ClassNotFoundException, SQLException {
		int idOrdineFornitore = Database.insert(Database.Table.ordini_fornitori, Map.of(
			"id_stato_ordine_fornitore", stato.getId(),
			"id_fornitore", fornitore.getId(),
			"id_ordine_cliente", ordineCliente.getId()
		));
		ArticoloOrdineFornitoreDAO.insertOrdineFornitoreArticoli(idOrdineFornitore, articoli);
		return getOrdineFornitore(idOrdineFornitore);
	}
	
	public static OrdineFornitore getOrdineFornitore(int idOrdineFornitore) throws ClassNotFoundException, SQLException {
		Map<String, Object> row = Database.selectById(Database.Table.ordini_fornitori, idOrdineFornitore);	
		StatoOrdineFornitore stato = StatoOrdineFornitoreDAO.getStatoOrdineFornitore((int) row.get("id_stato_ordine_fornitore"));
		Fornitore fornitore = FornitoreDAO.getFornitore((int) row.get("id_fornitore"));
		List<ArticoloOrdineFornitore> articoli = ArticoloOrdineFornitoreDAO.getArticoliOrdineFornitoreByOrdineFornitoreId((int) row.get("id"));
		return new OrdineFornitore((int) row.get("id"), stato, fornitore, articoli);
	}
	
	public static List<OrdineFornitore> getOrdiniFornitoriByOrdineClienteId(int idOrdineCliente) throws ClassNotFoundException, SQLException {	
		List<OrdineFornitore> ordiniFornitori = new ArrayList<>();
		StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM ")
			.append(Database.Table.ordini_fornitori)
			.append(" WHERE id_ordine_cliente = ")
			.append(idOrdineCliente);
		ResultSet rs = Database.rawQuery(sqlBuilder.toString());
		while (rs.next()) {
			StatoOrdineFornitore stato = StatoOrdineFornitoreDAO.getStatoOrdineFornitore(rs.getInt("id_stato_ordine_fornitore"));
			Fornitore fornitore = FornitoreDAO.getFornitore(rs.getInt("id_fornitore"));
			List<ArticoloOrdineFornitore> articoliordineFornitore = ArticoloOrdineFornitoreDAO.getArticoliOrdineFornitoreByOrdineFornitoreId(rs.getInt("id"));
			
			ordiniFornitori.add(new OrdineFornitore(
				rs.getInt("id"),
				stato,
				fornitore,
				articoliordineFornitore
			));
	    }
		return ordiniFornitori;
	}
	
	public static void updateOrdineFornitore(OrdineFornitore ordine) throws ClassNotFoundException, SQLException {
		StringBuilder sqlBuilder = new StringBuilder("UPDATE ")
			.append(Database.Table.ordini_fornitori)
			.append(" SET id_stato_ordine_fornitore = ")
			.append(ordine.getStato().getId())
			.append(", id_fornitore = ")
			.append(ordine.getFornitore().getId())
			.append(" WHERE id = ")
			.append(ordine.getId());
		Database.rawUpdate(sqlBuilder.toString());
	}
	
}
