package main.orm.dao.articolo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import main.domain.articolo.Capo;
import main.domain.articolo.Variante;
import main.orm.Database;

public class VarianteDAO {
	public static Variante insertVariante(String nome, String colore, Capo capo) throws ClassNotFoundException, SQLException {
		int id = Database.insert(Database.Table.varianti, Map.of(
			"nome", nome,
			"colore", colore,
			"id_capo", capo.getId()
		));
		return getVariante(id);
		 
	}
	
	public static void deleteVariante(int id) throws ClassNotFoundException, SQLException {
		Database.deleteById(Database.Table.varianti, id);
	}
	
	public static Variante getVariante(int id) throws ClassNotFoundException, SQLException {
		Map<String, Object> res = Database.selectById(Database.Table.varianti, id);
		Capo capo = CapoDAO.getCapo((int) res.get("id_capo"));
		return new Variante((int) res.get("id"), (String) res.get("nome"), (String) res.get("colore"), capo);
	}
	
	public static List<Variante> getAllVarianti() throws ClassNotFoundException, SQLException {
        List<Variante> varianti = new ArrayList<>();
		List<Map<String, Object>> res = Database.selectAll(Database.Table.varianti);	
        for (Map<String, Object> row : res) {
        	Capo capo = CapoDAO.getCapo((int) row.get("id_capo"));
        	varianti.add(new Variante((int) row.get("id"), (String) row.get("nome"), (String) row.get("colore"), capo));
        }
		return varianti;
	}
}
