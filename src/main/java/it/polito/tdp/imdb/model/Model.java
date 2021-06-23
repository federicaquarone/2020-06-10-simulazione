package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {
	
	ImdbDAO dao;
	Graph <Actor, DefaultWeightedEdge> grafo;
	Map <Integer,Actor> idMap;
	public Model() {
		this.dao= new ImdbDAO();
		idMap= new HashMap<>();
	}
	
	public List<String> getGeneri(){
		return dao.getGeneri();
	}

	
	public void creaGrafo(String genere) {
		grafo= new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		//vertici
		Graphs.addAllVertices(grafo, dao.getVertici(genere));
		for(Actor a: dao.getVertici(genere)) {
			if(!idMap.containsKey(a)) {
				idMap.put(a.getId(), a);
			}
		}
		
		//aggiungo archi
		for(Arco a: dao.getArchi(genere, idMap)) {
			Graphs.addEdge(grafo, a.getA1(), a.getA2(), a.getPeso());
		}
		
		
	}

	public int getNVertici() {
		// TODO Auto-generated method stub
		return grafo.vertexSet().size();
	}

	public int getNArchi() {
		// TODO Auto-generated method stub
		return grafo.edgeSet().size();
	}
	
	public List<Actor> getAttoriGrafo(){
		List<Actor> result= new LinkedList<Actor>(grafo.vertexSet());
		Collections.sort(result);
		return result;
	}
	
	public List<Actor> getVicini(Actor attore){
		List<Actor> result= Graphs.neighborListOf(grafo, attore);
		Collections.sort(result);
		return result;
	}
	
	public List<Actor> getConnectedActor(Actor a){
		ConnectivityInspector<Actor,DefaultWeightedEdge> ci= new ConnectivityInspector<Actor,DefaultWeightedEdge>(grafo);
	    List<Actor> actors= new ArrayList<>(ci.connectedSetOf(a));
		actors.remove(a);
		Collections.sort(actors);
		return actors;
	}
}
