package fca;

public class ConceptEdge {

	private String id;
	private Double similarity;
	
	public ConceptEdge(String id){
		this.id = id;	
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Double getSimilarity() {
		return similarity;
	}
	public void setSimilarity(Double similarity) {
		this.similarity = similarity;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.id;
	}
	
}
