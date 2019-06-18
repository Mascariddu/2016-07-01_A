package it.polito.tdp.formulaone.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import com.mysql.jdbc.ReplicationDriver;

import it.polito.tdp.formulaone.FormulaOneController;
import it.polito.tdp.formulaone.db.FormulaOneDAO;

public class Model {
	
	FormulaOneDAO dao;
	SimpleDirectedWeightedGraph<Driver, DefaultWeightedEdge> grafo;
	HashMap<Integer, Driver> idMap;
	List<Driver> vertex;
	List<Arco> archi;
	
	List<Driver> best;
	private int L;
	private int tassoMin;
	
	public Model() {
		
		dao = new FormulaOneDAO();
		idMap = new HashMap<Integer, Driver>();
		dao.getDrivers(idMap);
		
	}

	public List<Season> getSeasons() {
		// TODO Auto-generated method stub
		return dao.getAllSeasons();
	}

	public void creaGrafo(Season anno) {
		
		grafo = new SimpleDirectedWeightedGraph<Driver, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		vertex = dao.getVertex(anno,idMap);
		Graphs.addAllVertices(grafo, vertex);
		archi = dao.getArco(idMap,anno.getYear());
		
		for(Arco a : archi) {
			
			Graphs.addEdge(grafo, a.getSource(), a.getTarget(), a.getPeso());
			System.out.println("Aggiunta");
		
		}
		
		System.out.println("#vertici: "+grafo.vertexSet().size());
		System.out.println("#archi: "+grafo.edgeSet().size());
	}

	public Driver pilotaMigliore() {
		// TODO Auto-generated method stub
		Driver best = new Driver(0, "", "");
		int bestVal = -1000000;
		
		for(Driver driver : grafo.vertexSet()) {
			
			int vittorie = 0;
			int sconfitte = 0;
			
			for(DefaultWeightedEdge edge : grafo.outgoingEdgesOf(driver))
				vittorie += grafo.getEdgeWeight(edge);
			
			for(DefaultWeightedEdge edge : grafo.incomingEdgesOf(driver))
				sconfitte += grafo.getEdgeWeight(edge);
			
			if((vittorie - sconfitte) > bestVal) {
				best = driver;
				bestVal = (vittorie - sconfitte);
			}
			
		}
		
		return best;
	}

	public List<Driver> trovaDreamTeam(int k) {
		// TODO Auto-generated method stub
		best = new ArrayList<Driver>();
		List<Driver> parziale = new ArrayList<Driver>();
		L = 0;
		tassoMin = 100000;
		
		cerca(parziale,k,L);
		
		return best;
	}

	private void cerca(List<Driver> parziale, int k,int L) {
		// TODO Auto-generated method stub
		System.out.println("Entrato!");
		
		System.out.println("migliore: "+tassoMin+" parziale: "+getTasso(parziale));

		if(L == k) {
			if( tassoMin > getTasso(parziale)) {
				best = new ArrayList<Driver>(parziale);
				tassoMin = getTasso(parziale);
				System.out.println("New best!");
			}
			System.out.println("Esco!");
			return;
		}
		
		
		for(Driver driver : grafo.vertexSet()) {
			if(!parziale.contains(driver)) {
				
				System.out.println("Aggiungo!");
				parziale.add(driver);
				cerca(parziale,k,L+1);
				System.out.println("Rimuovo!");
				parziale.remove(parziale.size()-1);
				
			}
		}
	}

	public int getTasso(List<Driver> best2) {
		// TODO Auto-generated method stub
		int totale = 0;
		
		for(Driver driver : best2) {
			
			for(DefaultWeightedEdge edge : grafo.incomingEdgesOf(driver)) {
				
				if(!best2.contains(grafo.getEdgeSource(edge)))
					totale += grafo.getEdgeWeight(edge);
				
			}
		}
		
		return totale;
	}
	
	public int getTasso(Driver d,List<Driver> best) {
		
		int totale = 0;
		
		for(DefaultWeightedEdge edge : grafo.incomingEdgesOf(d)) {
			
			if(!best.contains(grafo.getEdgeSource(edge)))
				totale += grafo.getEdgeWeight(edge);
			
		}
		
		return totale;
	}
}
