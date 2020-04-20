import java.util.Arrays;
import storage.FactorArray;

public class Program {

	public static void main(String[] args) {
		Program pr = new Program();
		pr.run();
	}
	
	public void run() {
		int[][] matrix = new int[][]{
			{-1,  7, -1,  5, -1, -1, -1},
			{ 7, -1,  8,  9,  7, -1, -1},
			{-1,  8, -1, -1,  5, -1, -1},
			{ 5,  9, -1, -1, 15,  6, -1},
			{-1,  7,  5, 15, -1,  8,  9},
			{-1, -1, -1,  6,  8, -1, 11},
			{-1, -1, -1, -1,  9, 11, -1}
		};
		Edge[] mst = findKruskal(matrix);
		printEList(mst);
		
		mst = findPrim(matrix);
		printEList(mst);
	}
	
	Edge[] findKruskal(int[][] matrix) {
		int n = matrix.length;
		
		FactorArray<EdgeWithWeight> faWEdges = new FactorArray<EdgeWithWeight>();
		for(int y = 0; y < n; y++) {
			for(int x = y + 1; x < n; x++) {
				if(matrix[y][x] >= 0) {
					EdgeWithWeight we = new EdgeWithWeight();
					we.v1 = y;
					we.v2 = x;
					we.weight = matrix[y][x];
					faWEdges.add(we);
				}
			}
		}
		
		//преобразовать в массив
		EdgeWithWeight[] wedges = faWEdges.toArray(new EdgeWithWeight[faWEdges.size()]);
		//printWEList(wedges);
		//отсортировать по возрастанию весов ребёр
		sortEdges(wedges);
		//printWEList(wedges);
		
		FactorArray<Edge> faRes = new FactorArray<Edge>();
		int cntLinkedV = 0;//счетчик связанных вершин
		boolean[] linked = new boolean[n];
		Arrays.fill(linked, false);
		int root[] = new int[n];
		for(int i = 0; i < n; i++)
			root[i] = i;
		
		for(int j = 0; j < wedges.length && cntLinkedV < n; j++) {//идем по списку отсортированных ребёр, пока есть "одинокие" узлы
			EdgeWithWeight curE = wedges[j];
			if(root[curE.v1] != root[curE.v2]) {//добавляем текущее ребро к лесу F, если его добавление не создаст цикла
				for(int i = 0; i < n; i++) 
					if(root[i] == root[curE.v2]) root[i] = root[curE.v1];
				
				
				Edge e = new Edge();
				e.v1 = curE.v1;
				e.v2 = curE.v2;
				faRes.add(e);
				
				if(!linked[e.v1]) {
					linked[e.v1] = true;
					cntLinkedV++;
				}
				if(!linked[e.v2]) {
					linked[e.v2] = true;
					cntLinkedV++;
				}
			}
			
		}
		
		
		return faRes.toArray(new Edge[faRes.size()]);
	}
	
	void sortEdges(EdgeWithWeight[] array) {
		EdgeWithWeight[] copy = new EdgeWithWeight[array.length];
		System.arraycopy(array, 0, copy, 0, array.length);
		splitMerge(copy, 0, array.length, array);
	}
	
	void splitMerge(EdgeWithWeight[] copy, int begin, int end, EdgeWithWeight[] array) {
		if (end - begin < 2)
			return;
		int middle = (begin + end) / 2;
		splitMerge(array, begin, middle, copy);
		splitMerge(array, middle, end, copy);
		merge(copy, begin, middle, end, array);
	}
	
	void merge(EdgeWithWeight[] src, int begin, int middle, int end, EdgeWithWeight[] dest) {
		int fst = begin;
		int snd = middle;
		for(int k = begin; k < end; k++) {
			if( fst < middle && (snd >= end || src[fst].weight <= src[snd].weight) ) {
				dest[k] = src[fst];
				fst++;
			}
			else {
				dest[k] = src[snd];
				snd++;
			}
			
		}
	}
	
	void printWEList(EdgeWithWeight[] wedges){
		for(int i = 0; i < wedges.length;i++) {
			EdgeWithWeight we = wedges[i];
			System.out.println(we.v1 + " " + we.v2 + " : " + we.weight);
		}
		System.out.println("");
	}
	
	void printEList(Edge[] edges){
		for(int i = 0; i < edges.length;i++) {
			Edge e = edges[i];
			System.out.println(e.v1 + " " + e.v2 );
		}
		System.out.println("");
	}
	
	Edge[] findPrim(int[][] matrix) {
		int n = matrix.length;
		boolean[] isLinked = new boolean[n];
		Arrays.fill(isLinked, false);
		FactorArray<Integer> linkedV = new FactorArray<Integer>();
		FactorArray<Edge> faRes = new FactorArray<Edge>();
		//начинаем строить минимальное остовное дерево с вершины 0
		linkedV.add(0);
		isLinked[0] = true;
		while(linkedV.size() < n) {
			//проходим по всем элементам списка linkedV, из всех ребёр выбираем минимальное
			int minEW = Integer.MAX_VALUE;
			int minEV1 = -1;
			int minEV2 = -1;
			for(int i = 0; i < linkedV.size(); i++) {//идем по списку уже связанных вершин
				int v1 = linkedV.get(i);
				//перебираем рёбра, ассоциированные с v1, но лишь те, которые ведут в еще не связанные вершины
				for(int v2 = 0; v2 < n; v2++) {
					if(isLinked[v2])continue;
					int w = matrix[v1][v2];
					if(w == -1)continue;
					if(w < minEW) {
						minEW = w;
						minEV1 = v1;
						minEV2 = v2;
					}
				}
			}
			
			if(minEW == Integer.MAX_VALUE)
				break;
			
			isLinked[minEV2] = true;
			linkedV.add(minEV2);
			faRes.add(new Edge(minEV1, minEV2));
		}
		
		return faRes.toArray(new Edge[0]);
	}
	
}
