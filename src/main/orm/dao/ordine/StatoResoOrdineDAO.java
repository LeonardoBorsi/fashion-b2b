package main.orm.dao.ordine;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import main.domain.ordine.StatoResoOrdine;
import main.orm.Database;

public class StatoResoOrdineDAO {

	public static StatoResoOrdine insertStatoResoOrdine(String nome) throws ClassNotFoundException, SQLException {
		int id = Database.insert(Database.Table.stati_reso_ordine, Map.of(
			"nome", nome
		));
		return getStatoResoOrdine(id);
	}
	public static StatoResoOrdine getStatoResoOrdine(int id) throws ClassNotFoundException, SQLException {
		Map<String, Object> res = Database.selectById(Database.Table.stati_reso_ordine, id);				
		return new StatoResoOrdine((int) res.get("id"), (String) res.get("nome"));
	}
	
	public static List<StatoResoOrdine> getAllStatiResoOrdine() throws ClassNotFoundException, SQLException {
        List<StatoResoOrdine> stati = new ArrayList<>();
		List<Map<String, Object>> res = Database.selectAll(Database.Table.stati_reso_ordine);	
        for (Map<String, Object> row : res) {
        	stati.add(new StatoResoOrdine((int) row.get("id"), (String) row.get("nome")));
        }
		return stati;
	}
}
