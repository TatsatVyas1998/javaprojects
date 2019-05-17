package apps;

import structures.*;
import java.util.ArrayList;

import apps.PartialTree.Arc;

public class MST {

	/**
	 * Initializes the algorithm by building single-vertex partial trees
	 *
	 * @param graph Graph for which the MST is to be found
	 * @return The initial partial tree list
	 */
	public static PartialTreeList initialize(Graph graph) {
		PartialTreeList list = new PartialTreeList();
		PartialTree par = null ;

		MinHeap<Arc> H = new MinHeap();
		for (int i =0 ; i<graph.vertices.length; i++){
			par = new PartialTree(graph.vertices[i]);

			Vertex.Neighbor ptr = graph.vertices[i].neighbors;
			H = par.getArcs();
			while(ptr!=null){

				PartialTree.Arc arc = new PartialTree.Arc(graph.vertices[i],ptr.vertex , ptr.weight );
				H.insert(arc);
				ptr=ptr.next;


			}



			list.append(par);



		}

		/* COMPLETE THIS METHOD */

		return list;
	}

	/**
	 * Executes the algorithm on a graph, starting with the initial partial tree list
	 *
	 * @param ptlist Initial partial tree list
	 * @return Array list of all arcs that are in the MST - sequence of arcs is irrelevant
	 */



	public static ArrayList<PartialTree.Arc> execute(PartialTreeList ptlist) {
		ArrayList<PartialTree.Arc> arcarr = new ArrayList<PartialTree.Arc>(ptlist.size()) ;
		PartialTree ptx = null;
		PartialTree pty = null;
		MinHeap<Arc> pqx= null;
		MinHeap<Arc> pqy =null;
		Arc mstarc = null;
		Vertex v2 = null;

		while ( ptlist.size()!= 1){
			ptx = ptlist.remove();
			pqx= ptx.getArcs();
			mstarc = pqx.getMin();
			pqx.deleteMin();
			v2 = mstarc.v2;
			Vertex v1= ptx.getRoot();
			Vertex ptr = v2;
			while ( !(ptr.parent.equals(ptr)) || v1.name.equals(ptr.name)){
				if( v1.equals(ptr) ){
					mstarc=pqx.getMin();
					pqx.deleteMin();
					v2= mstarc.v2;
					ptr = v2;
				}else{
					ptr =ptr.parent;
				}

			}


			pty = ptlist.removeTreeContaining(v2);

			pqy= pty.getArcs();

			ptx.merge(pty);

			ptlist.append(ptx);
			arcarr.add(mstarc);


		}


		return arcarr;
	}
}
