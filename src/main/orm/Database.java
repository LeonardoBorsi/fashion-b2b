package main.orm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database {
    
    private Database(){}

	public static ResultSet rawQuery(String sql) throws ClassNotFoundException, SQLException {
		Connection c = ConnectionManager.getConnection();
        return c.prepareStatement(sql).executeQuery();
	}
	
	public static void rawUpdate(String sql) throws ClassNotFoundException, SQLException {
		Connection c = ConnectionManager.getConnection();
        c.prepareStatement(sql).executeUpdate();
	}
	
	public static List<Map<String, Object>> selectAll(Table tableName) throws SQLException, ClassNotFoundException {
		String sql = "SELECT * FROM " + tableName;
        List<Map<String, Object>> results = new ArrayList<>();
        ResultSet rs = Database.rawQuery(sql);
	    int columns = rs.getMetaData().getColumnCount();
	    
	    while (rs.next()) {
	        Map<String, Object> result = new HashMap<>();
	        for (int i = 1; i <= columns; i++) {
	            String columnName = rs.getMetaData().getColumnName(i);
	            Object value = rs.getObject(i);
	            result.put(columnName, value);
	        }
	        results.add(result);
	    }
        
        return results;
    }
	
	public static Map<String, Object> selectById(Table tableName, int id) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM " + tableName + " WHERE id = ?";
        ResultSet rs =  executePreparedStatementQuery(sql, id);
        
        if (rs.next()) {
            Map<String, Object> result = new HashMap<>();
            int columns = rs.getMetaData().getColumnCount();
            for (int i = 1; i <= columns; i++) {
                String columnName = rs.getMetaData().getColumnName(i);
                Object value = rs.getObject(i);
                result.put(columnName, value);
            }
            return result;
        } else {
            throw new SQLException("No results for ID: "+id);
        }
    }
	
	public static void deleteById(Table tableName, int id) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM " + tableName + " WHERE id = ?";
        executePreparedStatementUpdate(sql, id);
    }
	
	public static void updateById(Table tableName, int id, Map<String, Object> columnValueMap) throws SQLException, ClassNotFoundException {
        StringBuilder sqlBuilder = new StringBuilder("UPDATE ");
        sqlBuilder.append(tableName).append(" SET ");
        
        columnValueMap.forEach((columnName, value) -> {
            sqlBuilder.append(columnName).append(" = ?, ");
        });
        
        sqlBuilder.setLength(sqlBuilder.length() - 2);
        sqlBuilder.append(" WHERE id = ?");
        String sql = sqlBuilder.toString();
        
        Object[] params = new Object[columnValueMap.size() + 1];
        int index = 0;
        for (Object value : columnValueMap.values()) {
            params[index++] = value;
        }
        params[index] = id;
        
        executePreparedStatementQuery(sql, params);
    }
	
	public static int insert(Table tableName, Map<String, Object> columnValueMap) throws SQLException, ClassNotFoundException {
		StringBuilder sqlBuilder = new StringBuilder("INSERT INTO ");
        sqlBuilder.append(tableName).append(" (");
        StringBuilder placeholders = new StringBuilder();
        
        columnValueMap.forEach((columnName, value) -> {
            sqlBuilder.append(columnName).append(", ");
            placeholders.append("?, ");
        });
        
        sqlBuilder.setLength(sqlBuilder.length() - 2);
        placeholders.setLength(placeholders.length() - 2);
        sqlBuilder.append(") VALUES (").append(placeholders).append(") RETURNING id");
        String sql = sqlBuilder.toString();
        Object[] params = columnValueMap.values().toArray();
        
        ResultSet rs = executePreparedStatementQuery(sql, params);
        if (rs.next()) {
            return rs.getInt("id");
        } else {
            throw new SQLException("L'inserimento non ha restituito alcun ID generato automaticamente.");
        }
    }
	
	public static ResultSet executePreparedStatementQuery(String sql, Object... params) throws SQLException, ClassNotFoundException {
		Connection c = ConnectionManager.getConnection();
		PreparedStatement ps = c.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            Object param = params[i];
            if (param instanceof String) {
                ps.setString(i + 1, (String) param);
            } else if (param instanceof Integer) {
                ps.setInt(i + 1, (Integer) param);
            } else {
                throw new IllegalArgumentException("Tipo di parametro non supportato: " + param.getClass());
            }
        }
        return ps.executeQuery();
    }
	
	public static void executePreparedStatementUpdate(String sql, Object... params) throws SQLException, ClassNotFoundException {
		Connection c = ConnectionManager.getConnection();
		PreparedStatement ps = c.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            Object param = params[i];
            if (param instanceof String) {
                ps.setString(i + 1, (String) param);
            } else if (param instanceof Integer) {
                ps.setInt(i + 1, (Integer) param);
            } else {
                throw new IllegalArgumentException("Tipo di parametro non supportato: " + param.getClass());
            }
        }
        ps.executeUpdate();
    }
	
	public enum Table {
		clienti,
		fornitori,
		taglie,
		capi,
		varianti,
		articoli,
		stati_ordine_cliente,
		ordini_clienti,
		articoli_ordine_cliente,
		stati_ordine_fornitore,
		ordini_fornitori,
		articoli_ordine_fornitore,
		stati_reso_ordine,
		resi_ordini,
		articoli_magazzino,
	};

	
    public static void deleteSchema() throws ClassNotFoundException, SQLException {
        Database.rawUpdate("DROP TABLE IF EXISTS resi_ordini;");		
        Database.rawUpdate("DROP TABLE IF EXISTS articoli_ordine_fornitore;");
        Database.rawUpdate("DROP TABLE IF EXISTS ordini_fornitori;");
        Database.rawUpdate("DROP TABLE IF EXISTS articoli_ordine_cliente;");
        Database.rawUpdate("DROP TABLE IF EXISTS ordini_clienti;");
        Database.rawUpdate("DROP TABLE IF EXISTS articoli_magazzino;");

        Database.rawUpdate("DROP TABLE IF EXISTS stati_ordine_cliente;");
        Database.rawUpdate("DROP TABLE IF EXISTS stati_ordine_fornitore;");
        Database.rawUpdate("DROP TABLE IF EXISTS stati_reso_ordine;");
        
        Database.rawUpdate("DROP TABLE IF EXISTS articoli;");
        Database.rawUpdate("DROP TABLE IF EXISTS varianti;");
        Database.rawUpdate("DROP TABLE IF EXISTS capi;");
        Database.rawUpdate("DROP TABLE IF EXISTS taglie;");

        Database.rawUpdate("DROP TABLE IF EXISTS clienti;");
        Database.rawUpdate("DROP TABLE IF EXISTS fornitori;");
	}
    
	public static void createSchema() throws ClassNotFoundException, SQLException {
		Database.deleteSchema();
		
		Database.rawUpdate("CREATE TABLE clienti (\n"
				+ "    id SERIAL PRIMARY KEY,\n"
				+ "    nome VARCHAR(50),\n"
				+ "    cognome VARCHAR(50)\n"
				+ ");");
		
		Database.rawUpdate("CREATE TABLE fornitori (\n"
				+ "    id SERIAL PRIMARY KEY,\n"
				+ "    ragione_sociale VARCHAR(50),\n"
				+ "    indirizzo VARCHAR(50)\n"
				+ ");");
		
		Database.rawUpdate("CREATE TABLE taglie (\n"
				+ "    id SERIAL PRIMARY KEY,\n"
				+ "    nome VARCHAR(50)\n"
				+ ");");
		
		Database.rawUpdate("CREATE TABLE capi (\n"
				+ "    id SERIAL PRIMARY KEY,\n"
				+ "    nome VARCHAR(50),\n"
				+ "    id_fornitore SERIAL,\n"
				+ "    FOREIGN KEY (id_fornitore) REFERENCES fornitori(id)\n"
				+ ");");
		
		Database.rawUpdate("CREATE TABLE varianti (\n"
				+ "    id SERIAL PRIMARY KEY,\n"
				+ "    nome VARCHAR(50),\n"
				+ "    colore VARCHAR(50),\n"
				+ "    id_capo SERIAL,\n"
				+ "    FOREIGN KEY (id_capo) REFERENCES capi(id)\n"
				+ ");");
		
		Database.rawUpdate("CREATE TABLE articoli (\n"
				+ "    id SERIAL PRIMARY KEY,\n"
				+ "    id_capo SERIAL,\n"
				+ "    FOREIGN KEY (id_capo) REFERENCES capi(id),\n"
				+ "    id_variante SERIAL,\n"
				+ "    FOREIGN KEY (id_variante) REFERENCES varianti(id),\n"
				+ "    id_taglia SERIAL,\n"
				+ "    FOREIGN KEY (id_taglia) REFERENCES taglie(id)\n"
				+ ");");
		
		Database.rawUpdate("CREATE TABLE stati_ordine_cliente (\n"
				+ "    id SERIAL PRIMARY KEY,\n"
				+ "    nome VARCHAR(50)\n"
				+ ");");
		
		Database.rawUpdate("CREATE TABLE ordini_clienti (\n"
				+ "    id SERIAL PRIMARY KEY,\n"
				+ "    id_stato_ordine_cliente SERIAL,\n"
				+ "    FOREIGN KEY (id_stato_ordine_cliente) REFERENCES stati_ordine_cliente(id),\n"
				+ "    id_cliente SERIAL,\n"
				+ "    FOREIGN KEY (id_cliente) REFERENCES clienti(id)\n"
				+ ");");
		
		Database.rawUpdate("CREATE TABLE articoli_ordine_cliente (\n"
				+ "    id SERIAL PRIMARY KEY,\n"
				+ "    id_ordine_cliente SERIAL,\n"
				+ "    FOREIGN KEY (id_ordine_cliente) REFERENCES ordini_clienti(id),\n"
				+ "    id_articolo SERIAL,\n"
				+ "    FOREIGN KEY (id_articolo) REFERENCES articoli(id),\n"
				+ "    qnt_ordinata_dal_cliente INT,\n"
				+ "    qnt_magazzino INT,\n"
				+ "    qnt_ordinata_al_fornitore INT,\n"
				+ "    qnt_arrivata_dal_fornitore INT\n"
				+ ");");
		
		Database.rawUpdate("CREATE TABLE stati_ordine_fornitore (\n"
				+ "    id SERIAL PRIMARY KEY,\n"
				+ "    nome VARCHAR(50)\n"
				+ ");");
		
		Database.rawUpdate("CREATE TABLE ordini_fornitori (\n"
				+ "    id SERIAL PRIMARY KEY,\n"
				+ "    id_stato_ordine_fornitore SERIAL,\n"
				+ "    FOREIGN KEY (id_stato_ordine_fornitore) REFERENCES stati_ordine_fornitore(id),\n"
				+ "    id_fornitore SERIAL,\n"
				+ "    FOREIGN KEY (id_fornitore) REFERENCES fornitori(id),\n"
				+ "    id_ordine_cliente SERIAL,\n"
				+ "    FOREIGN KEY (id_ordine_cliente) REFERENCES ordini_clienti(id)\n"
				+ ");");
		
		Database.rawUpdate("CREATE TABLE articoli_ordine_fornitore (\n"
				+ "    id SERIAL PRIMARY KEY,\n"
				+ "    id_ordine_fornitore SERIAL,\n"
				+ "    FOREIGN KEY (id_ordine_fornitore) REFERENCES ordini_fornitori(id),\n"
				+ "    id_articolo SERIAL,\n"
				+ "    FOREIGN KEY (id_articolo) REFERENCES articoli(id),\n"
				+ "    qnt_ordinata INT,\n"
				+ "    qnt_prodotta INT,\n"
				+ "    qnt_spedita INT\n"
				+ ");");
		
		Database.rawUpdate("CREATE TABLE stati_reso_ordine (\n"
				+ "    id SERIAL PRIMARY KEY,\n"
				+ "    nome VARCHAR(50)\n"
				+ ");");
		
		Database.rawUpdate("CREATE TABLE resi_ordini (\n"
				+ "    id SERIAL PRIMARY KEY,\n"
				+ "    id_stato_reso_ordine SERIAL,\n"
				+ "    FOREIGN KEY (id_stato_reso_ordine) REFERENCES stati_reso_ordine(id),\n"
				+ "    id_ordine_cliente SERIAL,\n"
				+ "    FOREIGN KEY (id_ordine_cliente) REFERENCES ordini_clienti(id),\n"
				+ "    motivazione VARCHAR(50)\n"
				+ ");");
		
		Database.rawUpdate("CREATE TABLE articoli_magazzino (\n"
				+ "    id SERIAL PRIMARY KEY,\n"
				+ "    id_articolo SERIAL,\n"
				+ "    FOREIGN KEY (id_articolo) REFERENCES articoli(id),\n"
				+ "    qnt_disponibile INT\n"
				+ ");");
	
	}
	
}
