package main.orm.dao.articolo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import main.domain.Fornitore;
import main.domain.articolo.Capo;
import main.orm.Database;
import main.orm.dao.FornitoreDAO;

public class CapoDAO {
	public static Capo insertCapo(String nome, Fornitore fornitore) throws ClassNotFoundException, SQLException {
		int id = Database.insert(Database.Table.capi, Map.of(
			"nome", nome,
			"id_fornitore", fornitore.getId()
		));
		return getCapo(id);
	}
	
	public static void deleteCapo(int id) throws ClassNotFoundException, SQLException {
		Database.deleteById(Database.Table.capi, id);
	}
	
	public static Capo getCapo(int id) throws ClassNotFoundException, SQLException {
		Map<String, Object> res = Database.selectById(Database.Table.capi, id);
		Fornitore fornitore = FornitoreDAO.getFornitore((int) res.get("id_fornitore"));
		return new Capo((int) res.get("id"), (String) res.get("nome"), fornitore);
	}
	
	public static List<Capo> getAllCapi() throws ClassNotFoundException, SQLException {
        List<Capo> capi = new ArrayList<>();
		List<Map<String, Object>> res = Database.selectAll(Database.Table.capi);	
        for (Map<String, Object> row : res) {
        	Fornitore fornitore = FornitoreDAO.getFornitore((int) row.get("id_fornitore"));
        	capi.add(new Capo((int) row.get("id"), (String) row.get("nome"), fornitore));
        }
		return capi;
	}
}
