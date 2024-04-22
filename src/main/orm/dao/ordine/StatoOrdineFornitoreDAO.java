package main.orm.dao.ordine;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import main.domain.ordine.StatoOrdineFornitore;
import main.orm.Database;

public class StatoOrdineFornitoreDAO {
	
	public static StatoOrdineFornitore insertStatoOrdineFornitore(String nome) throws ClassNotFoundException, SQLException {
		int id = Database.insert(Database.Table.stati_ordine_fornitore, Map.of(
			"nome", nome
		));
		return getStatoOrdineFornitore(id);
	}
	public static StatoOrdineFornitore getStatoOrdineFornitore(int id) throws ClassNotFoundException, SQLException {
		Map<String, Object> res = Database.selectById(Database.Table.stati_ordine_fornitore, id);				
		return new StatoOrdineFornitore((int) res.get("id"), (String) res.get("nome"));
	}
	
	public static List<StatoOrdineFornitore> getAllStatiOrdineFornitore() throws ClassNotFoundException, SQLException {
        List<StatoOrdineFornitore> stati = new ArrayList<>();
		List<Map<String, Object>> res = Database.selectAll(Database.Table.stati_ordine_fornitore);	
        for (Map<String, Object> row : res) {
        	stati.add(new StatoOrdineFornitore((int) row.get("id"), (String) row.get("nome")));
        }
		return stati;
	}
	
}
