
public class EdgeWithWeight extends Edge {
	int weight;
	public EdgeWithWeight() {
		super();
		weight = 0;
	}
	public EdgeWithWeight(int v1, int v2) {
		super(v1, v2);
	}
	public EdgeWithWeight(int v1, int v2, int weight) {
		super(v1, v2);
		this.weight = weight;
	}
}
