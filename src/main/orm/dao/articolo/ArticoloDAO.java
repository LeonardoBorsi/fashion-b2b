package main.orm.dao.articolo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import main.domain.articolo.Articolo;
import main.domain.articolo.Capo;
import main.domain.articolo.Taglia;
import main.domain.articolo.Variante;
import main.orm.Database;

public class ArticoloDAO {
	public static Articolo insertArticolo(Capo capo, Variante variante, Taglia taglia) throws ClassNotFoundException, SQLException {
		int id = Database.insert(Database.Table.articoli, Map.of(
			"id_capo", capo.getId(),
			"id_variante", variante.getId(),
			"id_taglia", taglia.getId()

		));
		return getArticolo(id);
		 
	}
	
	public static void deleteArticolo(int id) throws ClassNotFoundException, SQLException {
		Database.deleteById(Database.Table.articoli, id);
	}
	
	public static Articolo getArticolo(int id) throws ClassNotFoundException, SQLException {
		Map<String, Object> res = Database.selectById(Database.Table.articoli, id);
		Capo capo = CapoDAO.getCapo((int) res.get("id_capo"));
		Variante variante = VarianteDAO.getVariante((int) res.get("id_variante"));
		Taglia taglia = TagliaDAO.getTaglia((int) res.get("id_taglia"));
		return new Articolo((int) res.get("id"), capo, variante, taglia);
	}
	
	public static List<Articolo> getAllArticoli() throws ClassNotFoundException, SQLException {
        List<Articolo> articoli = new ArrayList<>();
		List<Map<String, Object>> res = Database.selectAll(Database.Table.articoli);	
        for (Map<String, Object> row : res) {
        	Capo capo = CapoDAO.getCapo((int) row.get("id_capo"));
    		Variante variante = VarianteDAO.getVariante((int) row.get("id_variante"));
    		Taglia taglia = TagliaDAO.getTaglia((int) row.get("id_taglia"));
    		articoli.add(new Articolo((int) row.get("id"), capo, variante, taglia));
        }
		return articoli;
	}
}
