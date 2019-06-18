package it.polito.tdp.formulaone.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import it.polito.tdp.formulaone.model.Arco;
import it.polito.tdp.formulaone.model.Circuit;
import it.polito.tdp.formulaone.model.Constructor;
import it.polito.tdp.formulaone.model.Driver;
import it.polito.tdp.formulaone.model.Season;


public class FormulaOneDAO {

	public List<Integer> getAllYearsOfRace() {
		
		String sql = "SELECT year FROM races ORDER BY year" ;
		
		try {
			Connection conn = ConnectDB.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet rs = st.executeQuery() ;
			
			List<Integer> list = new ArrayList<>() ;
			while(rs.next()) {
				list.add(rs.getInt("year"));
			}
			
			conn.close();
			return list ;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Query Error");
		}
	}
	
	public List<Season> getAllSeasons() {
		
		String sql = "SELECT year, url FROM seasons ORDER BY year" ;
		
		try {
			Connection conn = ConnectDB.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet rs = st.executeQuery() ;
			
			List<Season> list = new ArrayList<>() ;
			while(rs.next()) {
				list.add(new Season(Year.of(rs.getInt("year")), rs.getString("url"))) ;
			}
			
			conn.close();
			return list ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Circuit> getAllCircuits() {

		String sql = "SELECT circuitId, name FROM circuits ORDER BY name";

		try {
			Connection conn = ConnectDB.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			List<Circuit> list = new ArrayList<>();
			while (rs.next()) {
				list.add(new Circuit(rs.getInt("circuitId"), rs.getString("name")));
			}

			conn.close();
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Query Error");
		}
	}
	
	public List<Constructor> getAllConstructors() {

		String sql = "SELECT constructorId, name FROM constructors ORDER BY name";

		try {
			Connection conn = ConnectDB.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			List<Constructor> constructors = new ArrayList<>();
			while (rs.next()) {
				constructors.add(new Constructor(rs.getInt("constructorId"), rs.getString("name")));
			}

			conn.close();
			return constructors;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Query Error");
		}
	}

	public void getDrivers(HashMap<Integer, Driver> idMap) {
		// TODO Auto-generated method stub
		String sql = "SELECT driverId as id,d.forename as n,d.surname as s FROM drivers d ";

		try {
			Connection conn = ConnectDB.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				idMap.put(rs.getInt("id"),new Driver(rs.getInt("id"), rs.getString("n"), rs.getString("s")));
			}

			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Query Error");
		}
	}

	public List<Driver> getVertex(Season anno,HashMap<Integer, Driver> idMap) {
		// TODO Auto-generated method stub
		String sql = "SELECT distinct driverId as id FROM results r,races r2 WHERE statusId = 1 AND r2.YEAR = ? AND r.raceId = r2.raceId";
		List<Driver> result = new ArrayList<Driver>();

		try {
			Connection conn = ConnectDB.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno.getYear().getValue());

			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				result.add(idMap.get(rs.getInt("id")));
			}

			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Query Error");
		}
	}

	public List<Arco> getArco(HashMap<Integer,Driver> idMap, Year year) {
		String sql = "SELECT COUNT(*) as tot,r.driverId AS prima,r2.driverId AS dopo FROM results r,results r2,races r3 WHERE r.positionOrder < r2.positionOrder AND r.statusId = 1 AND r3.YEAR = ? AND r3.raceId = r.raceId AND r2.statusId = 1 AND r.raceId = r2.raceId GROUP BY prima,dopo";
		List<Arco> edges = new ArrayList<Arco>();
		
		try {
			Connection conn = ConnectDB.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, year.getValue());
		
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
	
				edges.add(new Arco(idMap.get(rs.getInt("prima")), idMap.get(rs.getInt("dopo")), rs.getDouble("tot")));
			}

			conn.close();
			return edges;
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Query Error");
		}
	}

	public double getPeso(Driver source, Driver target) {
		String sql = "SELECT COUNT(*) as tot,r.driverId AS prima,r2.driverId AS dopo FROM results r,results r2 WHERE r.positionOrder < r2.positionOrder AND (r.driverId = ? AND r2.driverId = ?) AND r.raceId = r2.raceId";
		double val = 0.0;
		
		try {
			Connection conn = ConnectDB.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1,source.getDriverId());
			st.setInt(2, target.getDriverId());

			ResultSet rs = st.executeQuery();

			if (rs.next()) {
				val = rs.getDouble("tot");
			}

			conn.close();
			return val;
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Query Error");
		}
	}
	
	
	
}
