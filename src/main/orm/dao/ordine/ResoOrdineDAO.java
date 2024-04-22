package main.orm.dao.ordine;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import main.domain.ordine.OrdineCliente;
import main.domain.ordine.ResoOrdine;
import main.domain.ordine.StatoResoOrdine;
import main.orm.Database;

public class ResoOrdineDAO {

	public static ResoOrdine insertResoOrdine(StatoResoOrdine stato, OrdineCliente ordine, String motivazione) throws ClassNotFoundException, SQLException {
		int idResoOrdine = Database.insert(Database.Table.resi_ordini, Map.of(
			"id_stato_reso_ordine", stato.getId(),
			"id_ordine_cliente", ordine.getId(),
			"motivazione", motivazione
		));
		return getResoOrdine(idResoOrdine);
	}
	
	public static ResoOrdine getResoOrdine(int idResoOrdine) throws ClassNotFoundException, SQLException {
		Map<String, Object> res = Database.selectById(Database.Table.resi_ordini, idResoOrdine);	
		StatoResoOrdine stato = StatoResoOrdineDAO.getStatoResoOrdine((int) res.get("id_stato_reso_ordine"));
		OrdineCliente ordine = OrdineClienteDAO.getOrdineCliente((int) res.get("id_ordine_cliente"));
		return new ResoOrdine((int) res.get("id"), stato, ordine, (String) res.get("motivazione"));
	}
	
	public static ResoOrdine getResoOrdineByOrdineClienteId(int idOrdineCliente) throws ClassNotFoundException, SQLException {
		StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM ")
			.append(Database.Table.resi_ordini)
			.append(" WHERE id_ordine_cliente = ")
			.append(idOrdineCliente);
		
		ResultSet rs = Database.rawQuery(sqlBuilder.toString());
		if (rs.next()) {
			StatoResoOrdine stato = StatoResoOrdineDAO.getStatoResoOrdine(rs.getInt("id_stato_reso_ordine"));
			OrdineCliente ordine = OrdineClienteDAO.getOrdineCliente(rs.getInt("id_ordine_cliente"));
			
			return new ResoOrdine(rs.getInt("id"), stato, ordine, rs.getString("motivazione"));
	    }
		return null;
	}
	
	public static void updateResoOrdine(ResoOrdine reso) throws ClassNotFoundException, SQLException {
		StringBuilder sqlBuilder = new StringBuilder("UPDATE ")
			.append(Database.Table.resi_ordini)
			.append(" SET id_stato_reso_ordine = ")
			.append(reso.getStato().getId())
			.append(", id_ordine_cliente = ")
			.append(reso.getOrdine().getId())
			.append(", motivazione = '")
			.append(reso.getMotivazione())
			.append("' WHERE id = ")
			.append(reso.getId());
		Database.rawUpdate(sqlBuilder.toString());
	}
}
