package it.polito.tdp.formulaone.model;

public class Arco {

	Driver source;
	Driver target;
	double peso;
	
	public Driver getSource() {
		return source;
	}
	public void setSource(Driver source) {
		this.source = source;
	}
	public Driver getTarget() {
		return target;
	}
	public void setTarget(Driver target) {
		this.target = target;
	}
	public double getPeso() {
		return peso;
	}
	public void setPeso(double peso) {
		this.peso = peso;
	}
	
	public Arco(Driver source, Driver target, double peso) {
		super();
		this.source = source;
		this.target = target;
		this.peso = peso;
	}
}
