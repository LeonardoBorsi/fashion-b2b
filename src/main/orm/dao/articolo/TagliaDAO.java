package main.orm.dao.articolo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import main.domain.articolo.Taglia;
import main.orm.Database;

public class TagliaDAO {
	public static Taglia insertTaglia(String nome) throws ClassNotFoundException, SQLException {
		int id = Database.insert(Database.Table.taglie, Map.of("nome", nome));
		return getTaglia(id);
	}
	
	public static void deleteTaglia(int id) throws ClassNotFoundException, SQLException {
		Database.deleteById(Database.Table.taglie, id);
	}
	
	public static Taglia getTaglia(int id) throws ClassNotFoundException, SQLException {
		Map<String, Object> res = Database.selectById(Database.Table.taglie, id);				
		return new Taglia((int) res.get("id"), (String) res.get("nome"));
	}
	
	public static List<Taglia> getAllTaglie() throws ClassNotFoundException, SQLException {
        List<Taglia> taglie = new ArrayList<>();
		List<Map<String, Object>> res = Database.selectAll(Database.Table.taglie);	
        for (Map<String, Object> row : res) {
        	taglie.add(new Taglia((int) row.get("id"), (String) row.get("nome")));
        }
		return taglie;
	}
}
