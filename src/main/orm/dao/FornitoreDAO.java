package main.orm.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import main.domain.Fornitore;
import main.orm.Database;

public class FornitoreDAO {
	public static Fornitore insertFornitore(String ragioneSociale, String indirizzo) throws ClassNotFoundException, SQLException {
		int id = Database.insert(Database.Table.fornitori, Map.of(
			"ragione_sociale", ragioneSociale,
			"indirizzo", indirizzo
		));
		return getFornitore(id);
	}
	
	public static void deleteFornitore(int id) throws ClassNotFoundException, SQLException {
		Database.deleteById(Database.Table.fornitori, id);
	}
	
	public static Fornitore getFornitore(int id) throws ClassNotFoundException, SQLException {
		Map<String, Object> res = Database.selectById(Database.Table.fornitori, id);				
		return new Fornitore((int) res.get("id"), (String) res.get("ragione_sociale"), (String) res.get("indirizzo"));
	}
	
	public static List<Fornitore> getAllFornitori() throws ClassNotFoundException, SQLException {
        List<Fornitore> fornitori = new ArrayList<>();
		List<Map<String, Object>> res = Database.selectAll(Database.Table.fornitori);	
        for (Map<String, Object> row : res) {
        	fornitori.add(new Fornitore((int) row.get("id"), (String) row.get("ragione_sociale"), (String) row.get("indirizzo")));
        }
		return fornitori;
	}
}
