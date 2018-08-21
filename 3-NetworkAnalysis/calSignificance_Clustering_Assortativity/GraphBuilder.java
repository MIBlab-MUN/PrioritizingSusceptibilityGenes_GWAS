/*
 * File:           GraphBuilder.java
 * Author:         Ting Hu and Somayeh Kafaie
 * Date:           February, 2018
 * Purpose:        creating the graph and measuring some network parameters
 */

package calSignificance_linkedList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Random;

public class GraphBuilder {
	static float float_compare = (float) 0.00000001;
	static double INFINITY = 1000000.0;
	public static final double DEFAULT_DAMPING_FACTOR = 0.85;
	public static final double DEFAULT_PRECISION = 1.0e-5;
	
	public static class Vertex implements Cloneable
	{
		public int snpIndex;
		public String snpName;
		public double entroInfo;	
		public boolean mark;
		public int[] funClass;
		public int distance;
		public double wDistance;
		public double sigma;
		public Vertex parent;	
		ArrayList<Integer> downNeighbors;
		HashSet<Integer> preds;
		public double delta; 
		public double rank;
		public boolean isInGraph;
		
		public Vertex()
		{			
		}	
		public Vertex(String esnpName)
		{		
			snpName=esnpName;			
		}	
		public Vertex(int esnpIndex, String esnpName, float eentroInfo)
		{
			snpIndex=esnpIndex;
			snpName=esnpName;
			entroInfo=eentroInfo;
			mark=false;
			distance=Short.MAX_VALUE;
			wDistance = INFINITY;
			delta = 0;
			isInGraph = false;
		}		
		public Vertex(int esnpIndex, String esnpName, float eentroInfo, int[] fClass) //**** if vertex characteristic is considered
		{
			snpIndex=esnpIndex;
			snpName=esnpName;
			entroInfo=eentroInfo;
			mark=false;
			funClass=fClass;
			distance=Short.MAX_VALUE;
			wDistance = INFINITY;
			delta = 0;
			isInGraph = false;
		}		
		public Object clone ()
		{
			Vertex copy = new Vertex();	
			copy.distance=this.distance;
			copy.snpIndex=this.snpIndex;
			copy.snpName=this.snpName;
			copy.entroInfo=this.entroInfo;
			copy.mark=this.mark;
//			copy.funClass=this.funClass;
			copy.funClass = new int[this.funClass.length];
			for (int kk=0; kk<this.funClass.length; kk++)
				copy.funClass[kk] = this.funClass[kk];
			copy.wDistance = this.wDistance;
			copy.delta = this.delta;
			copy.isInGraph=this.isInGraph;
			
			return copy;
		}	
		public boolean equals(Object o)
		{
			Vertex v=(Vertex)o;
			return(this.snpName.equals((Object)v.snpName));
		}
		public int hashCode()
		{
			return this.snpName.hashCode();
		}
		public String toString()
		{
//			return "\t"+snpIndex + ":\t"+ entroInfo;
			return "\t" + snpIndex + "\t"+snpName + ":\t"+ entroInfo;
		}			
	}// ******** end class Vertex
	
	
	public static class Edge implements Cloneable
	{
		public int u;
		public int v;
		public String uName;
		public String vName;
		public double entroInterac;
		public int int_weight;
		public short significance;		
		public boolean isEdge;				
		
		public Edge()
		{			
		}
		
		public Edge (int eu, int ev)
		{
			u=eu;
			v=ev;
		}
		public Edge(int eu, int ev, String euName, String evName)
		{
			u=eu;
			v=ev;
			uName=euName;
			vName=evName;
			isEdge=false;			
		}		
		public Edge(int eu, int ev, String euName, String evName, double eentroInterac)
		{
			u=eu;
			v=ev;
			uName=euName;
			vName=evName;
			entroInterac=eentroInterac;
			isEdge=false;			
		}		
		public Edge(int eu, int ev, String euName, String evName, int e_int_weight)
		{
			u=eu;
			v=ev;
			uName=euName;
			vName=evName;
			int_weight = e_int_weight;
			isEdge=false;			
		}		
		public Edge(int eu, int ev, String euName, String evName, double eentroInterac,short esignificance)
		{
			u=eu;
			v=ev;
			uName=euName;
			vName=evName;
			entroInterac=eentroInterac;
			significance=esignificance;
			isEdge=false;			
		}		
		public Edge(int eu, int ev, double eentroInterac, short esignificance)
		{
			u=eu;
			v=ev;
			entroInterac=eentroInterac;
			significance=esignificance;
			isEdge=false;			
		}		
		public Edge(int eu, int ev, double eentroInterac)
		{
			u=eu;
			v=ev;
			entroInterac=eentroInterac;
			isEdge=false;			
		}
	
		public Object clone()
		{
			Edge copy = new Edge();	
			copy.u=this.u;
			copy.v=this.v;
			copy.uName=this.uName;
			copy.vName=this.vName;
			copy.entroInterac=this.entroInterac;
			copy.significance=this.significance;
			copy.isEdge=this.isEdge;
			return copy;
		}		
		public String toString()
		{
//			return "\t<"+ u + ",\t"+ v + ">\t\t"+ entroInterac + "\t\t"+ significance;
//			return "\t<"+ uName + ",\t"+ vName + ">\t\t"+ entroInterac + "\t\t"+ significance;
//			return "\t<"+ uName + ",\t"+ vName + ">\t\t"+ entroInterac;
			return "\t<"+ u + ",\t"+ v + ">\t\t"+ int_weight;
		}			
	}// ******** end class Edge
	
	
	public static class Graph
	{
		public int nVertex;
		public int notIsolated_Vertex;//the number of nodes which are not isolated
		public boolean[][] adjMatrix;
		
		public Graph(int nNode)
		{
			this.nVertex=nNode;
			this.adjMatrix = new boolean[nVertex][nVertex];	
			for (int i=0;i<this.nVertex;i++)
				for (int j=0; j<this.nVertex;j++)
					this.adjMatrix[i][j]=false;
		}	
		public Object clone()
		{
			Graph copy = new Graph(this.nVertex);
			copy.nVertex=this.nVertex;
			for(int i=0;i<nVertex;i++)
				for (int j=0;j<nVertex;j++)
					copy.adjMatrix[i][j]=this.adjMatrix[i][j];
			return copy;
		}
		
		public void setIsolatedVertices(ArrayList<Vertex> vertex_list)
		{
	    	//find isolated SNPs
	    	notIsolated_Vertex=0;
	    	for (int i=0; i<nVertex;i++)
	    	{
	    		boolean not_isolated = false;
	    		for (int j=0;j<nVertex;j++)
	    			if ((adjMatrix[i][j])&&(i!=j))
	    			{
	    				not_isolated = true;
	    				break;
	    			}
	    		if (not_isolated)
	    		{
	    			notIsolated_Vertex++;
	    			vertex_list.get(i).isInGraph=true;
	    		}
	    		else
	    			vertex_list.get(i).isInGraph=false;
	    	}
		}
		
		public void addEdge(Edge eedge)
		{
			adjMatrix[eedge.u][eedge.v]=true;
			adjMatrix[eedge.v][eedge.u]=true;
		}	
		
		public int getEdgeNumber()
		{
			int count=0;		
			for (int i=0; i<nVertex; i++)
				for (int j=0; j<nVertex; j++)
					if (adjMatrix[i][j])
						count++;
			return count/2;		
		}	
		
/*		public double getWeightedEdgeSum()
		{
			double sum=0;		
			for (int i=0; i<nVertex; i++)
				for (int j=0; j<nVertex; j++)
					if (adjMatrix[i][j])
					{
						sum = sum + e
					}

			return sum/2;		
		}	*/	
		
		public int getVertexNumber()
		{
			int count=0;
			ArrayList<Short> vertices_in_graph = new ArrayList<Short>();
			
			for (short i=0; i<nVertex; i++)
				for (short j=0; j<nVertex; j++)
					if (adjMatrix[i][j])
					{
						if(!vertices_in_graph.contains(i))
							vertices_in_graph.add(i);
						if(!vertices_in_graph.contains(j))
							vertices_in_graph.add(j);
					}
			count=vertices_in_graph.size();					
			return count;			
		}
		
		public int getVertexDegree(int nodeIndex)
		{
			int count = 0;
			for (int i=0;i<nVertex;i++)
				if ((nodeIndex!=i)&&(adjMatrix[nodeIndex][i]))
						count++;
			return count;
		}
		
		public int getGCCorder(Vertex[] allNodes) throws IOException
		{
			ArrayList<Vertex> unmarkedNodes = new ArrayList<Vertex>();
			for (int i=0;i<allNodes.length;i++)
			{
				allNodes[i].mark=false;
				allNodes[i].distance=Short.MAX_VALUE;
				unmarkedNodes.add(allNodes[i]);			
			}
					
			int GCCorder=0;
			
			while(!unmarkedNodes.isEmpty())
			{				
				Vertex root=unmarkedNodes.get(0);			
				unmarkedNodes.remove(root);				
				int size=componentOrderBFS(root,unmarkedNodes);
				if (size>GCCorder)				
					GCCorder=size;								
			}			
			return GCCorder;
		}		
			
		public ArrayList<ArrayList<Integer>> getAllCCs (Vertex[] allNodes)
		{
			ArrayList<ArrayList<Integer>> allCCs = new ArrayList<ArrayList<Integer>>();
			ArrayList<Vertex> unmarkedNodes=new ArrayList<Vertex>();
			
			for (int i=0;i<allNodes.length;i++)
			{
				allNodes[i].mark=false;
				allNodes[i].distance=Short.MAX_VALUE;
				unmarkedNodes.add(allNodes[i]);			
			}
			
			while(!unmarkedNodes.isEmpty())
			{
				Vertex root=unmarkedNodes.get(0);			
				unmarkedNodes.remove(root);		
				ArrayList<Integer> oneComponent = new ArrayList<Integer>();
				oneComponent = componentBFS(root,unmarkedNodes);
				if (oneComponent.size()>1)				  
					allCCs.add(oneComponent);								
			}			
			return allCCs;
		}
			
		public ArrayList<Integer> getCCorders(Vertex[] allNodes)
		{
			ArrayList<Vertex> unmarkedNodes=new ArrayList<Vertex>();
			ArrayList<Integer> CCorders= new ArrayList<Integer>();
			
			for (int i=0;i<allNodes.length;i++)
			{
				allNodes[i].mark=false;
				allNodes[i].distance=Short.MAX_VALUE;
				unmarkedNodes.add(allNodes[i]);			
			}
			while(!unmarkedNodes.isEmpty())
			{
				Vertex root=unmarkedNodes.get(0);			
				unmarkedNodes.remove(root);		
				int o=componentOrderBFS(root,unmarkedNodes);
				if (o>1)				  
					CCorders.add(o);								
			}	
			return CCorders;
		}	
	
		public ArrayList<Integer> componentBFS(Vertex root, ArrayList<Vertex> unmarkedNodes)
		{
			ArrayList<Integer> componentNodeIds = new ArrayList<Integer>();

			root.mark=true;
			root.distance=0;
						
			LinkedList<Vertex> queue = new LinkedList<Vertex>();
			queue.addLast(root);	
			while(!queue.isEmpty())	
			{
				Vertex u=queue.removeFirst();
				
				for (int i=0;i<unmarkedNodes.size();i++)
					if(adjMatrix[u.snpIndex][unmarkedNodes.get(i).snpIndex])
					{
						if (!unmarkedNodes.get(i).mark)
						{
							unmarkedNodes.get(i).mark=true;
							unmarkedNodes.get(i).parent=u;
							unmarkedNodes.get(i).distance=unmarkedNodes.get(i).parent.distance+1;
							queue.addLast(unmarkedNodes.get(i));						
						}
					}
				
				u.mark=true;
				componentNodeIds.add(u.snpIndex);
				unmarkedNodes.remove(u);
			}						
			return componentNodeIds;
		}
		

				
		public int componentOrderBFS(Vertex root, ArrayList<Vertex> unmarkedNodes)
		{
			root.mark=true;
			root.distance=0;
			int size =0;
			
//			StringBuilder sb = new StringBuilder();
			
			LinkedList<Vertex> queue = new LinkedList<Vertex>();
			queue.addLast(root);	
			while(!queue.isEmpty())	
			{
				Vertex u=queue.removeFirst();
				
//				sb.append(u.snpIndex+" ");
				
				for (int i=0;i<unmarkedNodes.size();i++)
					if(adjMatrix[u.snpIndex][unmarkedNodes.get(i).snpIndex])
					{
						if (!unmarkedNodes.get(i).mark)
						{
							unmarkedNodes.get(i).mark=true;
							unmarkedNodes.get(i).parent=u;
							unmarkedNodes.get(i).distance=unmarkedNodes.get(i).parent.distance+1;
							queue.addLast(unmarkedNodes.get(i));						
						}
					}
				u.mark=true;
				size++;
				unmarkedNodes.remove(u);
			}			
	//		if(size>1)			
	//			System.out.println(sb);	
			return size;			
		}	
		
		public ArrayList<Integer> getGCCVertexDegrees(Vertex[] allNodes, Edge[][] edgeArray)
		{
			ArrayList<Vertex> subGraphNodes=new ArrayList<Vertex>();
			ArrayList<Vertex> unmarkedNodes = new ArrayList<Vertex>();
			for (int i=0;i<allNodes.length;i++)
			{
				allNodes[i].mark=false;
				allNodes[i].distance=Short.MAX_VALUE;
				unmarkedNodes.add(allNodes[i]);			
			}
			
			int GCCorder=0;
            Vertex rootInSubGraph=unmarkedNodes.get(0);
            
			while(!unmarkedNodes.isEmpty())
			{
				Vertex root=unmarkedNodes.get(0);			
				unmarkedNodes.remove(root);				
				int size=componentOrderBFS(root,unmarkedNodes);
				if (size>GCCorder)	
				{
					GCCorder=size;
					rootInSubGraph=root;
				}													
			}
			
			ArrayList<Vertex> unmarkedSubGraphNodes = new ArrayList<Vertex>();
			for (int i=0;i<allNodes.length;i++)
			{
				allNodes[i].mark=false;
				allNodes[i].distance=Short.MAX_VALUE;
				unmarkedSubGraphNodes.add(allNodes[i]);			
			}
			
			subGraphNodes.add(rootInSubGraph);
			rootInSubGraph.mark=true;
			rootInSubGraph.distance=0;
			
			LinkedList<Vertex> queue = new LinkedList<Vertex>();
			queue.addLast(rootInSubGraph);	
			while(!queue.isEmpty())	
			{
				Vertex u=queue.removeFirst();				
				
				for (int i=0;i<unmarkedSubGraphNodes.size();i++)
					if(adjMatrix[u.snpIndex][unmarkedSubGraphNodes.get(i).snpIndex])
					{
						if (!unmarkedSubGraphNodes.get(i).mark)
						{
							unmarkedSubGraphNodes.get(i).mark=true;
							unmarkedSubGraphNodes.get(i).parent=u;
							unmarkedSubGraphNodes.get(i).distance=unmarkedSubGraphNodes.get(i).parent.distance+1;
							queue.addLast(unmarkedSubGraphNodes.get(i));								
							subGraphNodes.add(unmarkedSubGraphNodes.get(i));
//							System.out.println(subGraphNodes.toString());
						}
					}
				u.mark=true;
				unmarkedSubGraphNodes.remove(u);
			}
			
//			System.out.println(subGraphNodes.size());
			Graph subGraph=new Graph(subGraphNodes.size());
			for(int j=0;j<subGraphNodes.size();j++)		
			{
				for(int k=j+1;k<subGraphNodes.size();k++)				
					if(adjMatrix[subGraphNodes.get(j).snpIndex][subGraphNodes.get(k).snpIndex])		
					{
						int t=subGraphNodes.get(j).snpIndex;
						int y=subGraphNodes.get(k).snpIndex;
						if(t<y)													
							subGraph.addEdge(edgeArray[t][y]);
						else
							subGraph.addEdge(edgeArray[y][t]);
//						System.out.println(j+"\t"+k+"\t"+subGraph.getEdgeNumber());
					}
			}
//			System.out.println(subGraph.getEdgeNumber());			
			return subGraph.getNonZeroVertexDegrees();
		}
		
		//Somayeh:START
		public String printGCCVertexDegreeDistributionNonZero(Vertex[] allNodes, Edge[][] edgeArray)
		{
			ArrayList<Vertex> subGraphNodes=new ArrayList<Vertex>();
			ArrayList<Vertex> unmarkedNodes = new ArrayList<Vertex>();
			for (int i=0;i<allNodes.length;i++)
			{
				allNodes[i].mark=false;
				allNodes[i].distance=Short.MAX_VALUE;
				unmarkedNodes.add(allNodes[i]);			
			}
			
			int GCCorder=0;
            Vertex rootInSubGraph=unmarkedNodes.get(0);
            
			while(!unmarkedNodes.isEmpty())
			{
				Vertex root=unmarkedNodes.get(0);			
				unmarkedNodes.remove(root);				
				int size=componentOrderBFS(root,unmarkedNodes);
				if (size>GCCorder)	
				{
					GCCorder=size;
					rootInSubGraph=root;
				}													
			}
			
			ArrayList<Vertex> unmarkedSubGraphNodes = new ArrayList<Vertex>();
			for (int i=0;i<allNodes.length;i++)
			{
				allNodes[i].mark=false;
				allNodes[i].distance=Short.MAX_VALUE;
				unmarkedSubGraphNodes.add(allNodes[i]);			
			}
			
			subGraphNodes.add(rootInSubGraph);
			rootInSubGraph.mark=true;
			rootInSubGraph.distance=0;
			
			LinkedList<Vertex> queue = new LinkedList<Vertex>();
			queue.addLast(rootInSubGraph);	
			while(!queue.isEmpty())	
			{
				Vertex u=queue.removeFirst();				
				
				for (int i=0;i<unmarkedSubGraphNodes.size();i++)
					if(adjMatrix[u.snpIndex][unmarkedSubGraphNodes.get(i).snpIndex])
					{
						if (!unmarkedSubGraphNodes.get(i).mark)
						{
							unmarkedSubGraphNodes.get(i).mark=true;
							unmarkedSubGraphNodes.get(i).parent=u;
							unmarkedSubGraphNodes.get(i).distance=unmarkedSubGraphNodes.get(i).parent.distance+1;
							queue.addLast(unmarkedSubGraphNodes.get(i));								
							subGraphNodes.add(unmarkedSubGraphNodes.get(i));
//							System.out.println(subGraphNodes.toString());
						}
					}
				u.mark=true;
				unmarkedSubGraphNodes.remove(u);
			}
			
//			System.out.println(subGraphNodes.size());
			Graph subGraph=new Graph(subGraphNodes.size());
			for(int j=0;j<subGraphNodes.size();j++)		
			{
				for(int k=j+1;k<subGraphNodes.size();k++)				
					if(adjMatrix[subGraphNodes.get(j).snpIndex][subGraphNodes.get(k).snpIndex])		
					{
						int t=subGraphNodes.get(j).snpIndex;
						int y=subGraphNodes.get(k).snpIndex;
						if(t<y)													
							subGraph.addEdge(edgeArray[t][y]);
						else
							subGraph.addEdge(edgeArray[y][t]);
//						System.out.println(j+"\t"+k+"\t"+subGraph.getEdgeNumber());
					}
			}
//			System.out.println(subGraph.getEdgeNumber());			
			return subGraph.printVertexDegreeDistributionNonZero();
		}
		//Somayeh:END
						
		public ArrayList<Integer> getNonZeroVertexDegrees()
		{
			ArrayList<Integer> vertexDegrees = new ArrayList<Integer>();
			
			for (int i=0;i<nVertex;i++)
			{
				int count=0;
				for (int j=0;j<nVertex;j++)
				{
					if(adjMatrix[i][j])
						count++;
				}	
				if (count!=0)				
					vertexDegrees.add(count);
			}			
			return vertexDegrees;			
		}
		
		public ArrayList<Integer> getVertexDegrees()
		{
			ArrayList<Integer> vertexDegrees = new ArrayList<Integer>();
			
			for (int i=0;i<nVertex;i++)
			{
				int count=0;
				for (int j=0;j<nVertex;j++)
				{
					if(adjMatrix[i][j])
						count++;
				}									
				vertexDegrees.add(count);
			}			
			return vertexDegrees;			
		}
				
		public String printVertexDegreeDistribution()
		{
			StringBuilder sb = new StringBuilder();
			sb.append("");
			
			ArrayList<Integer> degrees = this.getNonZeroVertexDegrees();
			
			int degreeMin=10000;
			int degreeMax=0;
			
			for (int i=0; i<degrees.size();i++)
			{
				if (degrees.get(i)<degreeMin)
					degreeMin=degrees.get(i);
				if (degrees.get(i)>degreeMax)
					degreeMax=degrees.get(i);
			}
			
			for(int degreeCut=degreeMin;degreeCut<=degreeMax;degreeCut++)
			{
				int count=0;
				for(int i=0;i<degrees.size();i++)
					if(degrees.get(i)==degreeCut)
						count++;
				sb.append(degreeCut+"\t");
				sb.append((double)count/degrees.size()+"\n");								
			}
			
			return sb.toString();
		}
		
		public String printVertexDegreeDistributionNonZero()
		{
			StringBuilder sb = new StringBuilder();
			sb.append("");
			
			ArrayList<Integer> degrees = this.getNonZeroVertexDegrees();
			
			int degreeMin=10000;
			int degreeMax=0;
			
			for (int i=0; i<degrees.size();i++)
			{
				if (degrees.get(i)<degreeMin)
					degreeMin=degrees.get(i);
				if (degrees.get(i)>degreeMax)
					degreeMax=degrees.get(i);
			}
			
			for(int degreeCut=degreeMin;degreeCut<=degreeMax;degreeCut++)
			{
				int count=0;
				for(int i=0;i<degrees.size();i++)
					if(degrees.get(i)==degreeCut)
						count++;
				if(count!=0)
				{
					sb.append(degreeCut+"\t");
					sb.append((double)count/degrees.size()+"\n");	
				}
											
			}
			
			return sb.toString();
		}
				
		public ArrayList<Double> getVertexWeightedDegreeCentrality(Edge[][] edgeArray) throws IOException
		{
			ArrayList<Double> vertexWeightedDegreeCentrality=new ArrayList<Double>();
			
			for (int i=0;i<nVertex;i++)
			{
				double cent=0;
				for (int j=0;j<nVertex;j++)
				{
					if(adjMatrix[i][j])
					{
						if (i<j)						
							cent = cent+edgeArray[i][j].entroInterac;
						else if (i>j)
							cent = cent+edgeArray[j][i].entroInterac;
						else
							FileProcessing.log("ERROR: ha", true);					
					}					
				}	
				vertexWeightedDegreeCentrality.add(cent);
			}				
			return vertexWeightedDegreeCentrality;
		}	
		
		//The formula has been presented in Eq 4 of degreeAssortativity-Eq4_Newman.pdf
		public float getDegreeAssortativity() //******** for undirected simple graph
		{
			float product = 0;
			float sum = 0;
			float sqSum=0;
			int nEdges =0;
			ArrayList<Integer> degree = this.getVertexDegrees();
			
			for (int i=0;i<nVertex;i++)
				for(int j=i;j<nVertex;j++)
				{
					if(adjMatrix[i][j])   //*****for each edge
					{
						product = product + degree.get(i)*degree.get(j);
						sum = sum + degree.get(i)+degree.get(j);
						sqSum = sqSum + degree.get(i)*degree.get(i) + degree.get(j)*degree.get(j);
						nEdges++;
					}
				}	
			
			if(nEdges==0)
				return -1000;
			else
			{
				float num1= product/(float) nEdges;
				float den1 = sqSum/(float) (2*nEdges);
				float num2 = (sum/(float)(2*nEdges))*(sum/(float)(2*nEdges));
				if ((den1-num2<float_compare)&&(num1-num2<float_compare))
					return 1;
				else if(den1-num2<float_compare)
					return -1000;
				else				
					return (num1-num2)/(den1-num2);
			}		
		}
			
		/*public double getAssortativity(ArrayList<Double> nodeProp) //******** for undirected simple graph
		{
			double product = 0;
			double sum = 0;
			double sqSum=0;
			int nEdges =0;

			if(nodeProp.size()!=nVertex)
			{
				System.out.println("wrong node property array!!");
				return -10000;
			}		
			else
			{
				for (int i=0;i<nVertex;i++)
					for(int j=i;j<nVertex;j++)
					{
						if(adjMatrix[i][j])   //*****for each edge
						{
							product = product + nodeProp.get(i)*nodeProp.get(j);
							sum = sum + nodeProp.get(i)+nodeProp.get(j);
							sqSum = sqSum + nodeProp.get(i)*nodeProp.get(i) + nodeProp.get(j)*nodeProp.get(j);
							nEdges++;
						}
					}	
				
				if(nEdges==0)
					return -1000;
				else
				{
					double num1= product/(float) nEdges;
					double den1 = sqSum/(float) (2*nEdges);
					double num2 = (sum/(float)(2*nEdges))*(sum/(float)(2*nEdges));
					if ((den1-num2<SEN.float_compare)&&(num1-num2<SEN.float_compare))
						return 1;
					else if(den1-num2<SEN.float_compare)
						return -1000;
					else				
						return (num1-num2)/(den1-num2);
				}						
			}		
		}*/
			
		public Graph getEdgeSwapped(int numberOfSwaps) throws IOException
		{
			Graph swappedGraph = (Graph) this.clone();
			Random rand = new Random();
			
			for (int k=0;k<numberOfSwaps;k++)
			{
				int e1_i=0;
				int e1_j=0;
				int e2_i=0;
				int e2_j=0;
				
				while(e1_i==e2_i || e1_j==e2_j || e2_i==e1_j || e1_i==e2_j   
					|| !swappedGraph.adjMatrix[e1_i][e1_j] || !swappedGraph.adjMatrix[e2_i][e2_j] 
					|| swappedGraph.adjMatrix[e2_i][e1_j] || swappedGraph.adjMatrix[e1_i][e2_j])				
				{
					e1_i = rand.nextInt(nVertex);
					e1_j = e1_i + rand.nextInt(nVertex - e1_i);
					e2_i = rand.nextInt(nVertex);
					e2_j = e2_i + rand.nextInt(nVertex - e2_i);						
				}				
				swappedGraph.adjMatrix[e1_i][e1_j] = false;
				swappedGraph.adjMatrix[e2_i][e2_j] = false;
				swappedGraph.adjMatrix[e2_i][e1_j] = true;
				swappedGraph.adjMatrix[e1_i][e2_j] = true;
				
				swappedGraph.adjMatrix[e1_j][e1_i] = false;
				swappedGraph.adjMatrix[e2_j][e2_i] = false;
				swappedGraph.adjMatrix[e2_j][e1_i] = true;
				swappedGraph.adjMatrix[e1_j][e2_i] = true;
				
				FileProcessing.log("Done: Swap#"+k, true);
			}			
			return swappedGraph;
		}
		
		public Graph getEdgeSwapped_withEdgeList(int numberOfSwaps, ArrayList<Integer>[] temp) throws IOException
		{
			Graph swappedGraph = (Graph) this.clone();
			
			@SuppressWarnings("unchecked")
			ArrayList<Integer>[] edge_list =new ArrayList[temp.length];
			for (int i=0;i<temp.length;i++)
			{
				edge_list[i]=new ArrayList<Integer>();
				for (int j=0;j<temp[i].size();j++)
					edge_list[i].add(temp[i].get(j));
			}
			
			Random rand = new Random();
			
			for (int k=0;k<numberOfSwaps;k++)
			{
				int e1_i=0;
				int e1_j=0;
				int e2_i=0;
				int e2_j=0;
				int e1_j_index = 0;
				int e2_j_index = 0;
				while(e1_i==e2_i || e1_j==e2_j || e2_i==e1_j || e1_i==e2_j   
					|| !swappedGraph.adjMatrix[e1_i][e1_j] || !swappedGraph.adjMatrix[e2_i][e2_j] 
					|| swappedGraph.adjMatrix[e2_i][e1_j] || swappedGraph.adjMatrix[e1_i][e2_j])				
				{
					do {
					e1_i = rand.nextInt(nVertex);
					}while(edge_list[e1_i].size() == 0);
					e1_j_index = rand.nextInt(edge_list[e1_i].size());
					e1_j = edge_list[e1_i].get(e1_j_index);
					
					do{
						e2_i = rand.nextInt(nVertex);
					}while(edge_list[e2_i].size()==0);
					e2_j_index = rand.nextInt(edge_list[e2_i].size());
					e2_j = edge_list[e2_i].get(e2_j_index);
				}				
				swappedGraph.adjMatrix[e1_i][e1_j] = false;
				swappedGraph.adjMatrix[e2_i][e2_j] = false;
				swappedGraph.adjMatrix[e2_i][e1_j] = true;
				swappedGraph.adjMatrix[e1_i][e2_j] = true;
				edge_list[e1_i].set(e1_j_index,e2_j);
				edge_list[e2_i].set(e2_j_index, e1_j);
				
				swappedGraph.adjMatrix[e1_j][e1_i] = false;
				swappedGraph.adjMatrix[e2_j][e2_i] = false;
				swappedGraph.adjMatrix[e2_j][e1_i] = true;
				swappedGraph.adjMatrix[e1_j][e2_i] = true;
				edge_list[e1_j].set(edge_list[e1_j].indexOf(e1_i), e2_i);
				edge_list[e2_j].set(edge_list[e2_j].indexOf(e2_i), e1_i);
				
				//FileProcessing.log("Done: Swap#"+k, true);
			}			
			return swappedGraph;
		}
		
		public Graph getEdgeRandom()
		{
			Graph edgeRandomGraph = new Graph(this.nVertex);
			Random rand = new Random();
			int nEdge = this.getEdgeNumber();
			
			while(edgeRandomGraph.getEdgeNumber()<nEdge)
			{
				int v_i = rand.nextInt(nVertex);
				int v_j = rand.nextInt(nVertex);
				
				if (v_i < v_j && !edgeRandomGraph.adjMatrix[v_i][v_j])
				{
					Edge e = new Edge(v_i,v_j);
					edgeRandomGraph.addEdge(e);
				}
//				System.out.println(edgeRandomGraph.getEdgeNumber()+" == "+count +"?");
			}			
			return edgeRandomGraph;
		}
		
		/*public int[][] getAllPairShortestPath()
		{
			int[][] distance = new int[this.nVertex][this.nVertex];
			
			int[][] weightMatrix = new int[this.nVertex][this.nVertex];
			for(int i=0;i<this.nVertex;i++)
				for(int j=0;j<this.nVertex;j++)
				{
					if(this.adjMatrix[i][j])
						weightMatrix[i][j]=1;
					else
						weightMatrix[i][j]=Integer.MAX_VALUE;
				}
			FloydWarshall fw = new FloydWarshall();	
			fw.calcShortestPaths(weightMatrix,this.nVertex);
			
			for(int i=0;i<this.nVertex;i++)
				for(int j=0;j<this.nVertex;j++)
				{
					distance[i][j]=fw.getShortestDistance(i, j);
				}
			
			return distance;
		}*/
		
		public Graph getSubGraph(int referenceVertexIndex) //****** generate a subgraph that has only the direct neighbors of the reference vertex
		{
			Graph subGraph = new Graph(this.nVertex);
			ArrayList<Integer> directNeighbors = new ArrayList<Integer>();
			for(int i=0;i<this.nVertex;i++)
			{
				if(this.adjMatrix[referenceVertexIndex][i])
				{
					directNeighbors.add(i);
					subGraph.adjMatrix[referenceVertexIndex][i]=subGraph.adjMatrix[i][referenceVertexIndex]=true;
				}
					
			}
			for(int i=0; i<directNeighbors.size(); i++)
			{
				for(int j=0; j<this.nVertex;j++)
				{
					if(this.adjMatrix[directNeighbors.get(i)][j] && directNeighbors.contains(j))
					{
						subGraph.adjMatrix[directNeighbors.get(i)][j]=subGraph.adjMatrix[j][directNeighbors.get(i)] = true;
					}
				}				
			}
			return subGraph;
		}
		
		//Somayeh
		
		public ArrayList<Integer> componentBFS_closeness(Vertex root, ArrayList<Vertex> allNodes, double[] dist, Edge[][] edgeArray, boolean weighted)
		{
			ArrayList<Vertex> unmarkedNodes=new ArrayList<Vertex>(); 
			for (int i=0;i<allNodes.size();i++)
				unmarkedNodes.add(allNodes.get(i));			
			
			ArrayList<Integer> componentNodeIds = new ArrayList<Integer>();

			root.mark=true;
			root.distance=0;
						
			LinkedList<Vertex> queue = new LinkedList<Vertex>();
			queue.addLast(root);	
			while(!queue.isEmpty())	
			{
				Vertex u=queue.removeFirst();
				
				for (int i=0;i<unmarkedNodes.size();i++)
					if(adjMatrix[u.snpIndex][unmarkedNodes.get(i).snpIndex])
					{
						if (!unmarkedNodes.get(i).mark)
						{
							unmarkedNodes.get(i).mark=true;
							if (!weighted)
								dist[unmarkedNodes.get(i).snpIndex]=dist[u.snpIndex]+1;
							else
								dist[unmarkedNodes.get(i).snpIndex]=dist[u.snpIndex]+1/(edgeArray[u.snpIndex][unmarkedNodes.get(i).snpIndex].entroInterac);
							queue.addLast(unmarkedNodes.get(i));						
						}
					}
				
				u.mark=true;
				componentNodeIds.add(u.snpIndex);
				unmarkedNodes.remove(u);
			}						
			return componentNodeIds;
		}
		
		public void componentBFS_betweenness(Vertex root, ArrayList<Vertex> allNodes)
		{
		ArrayList<Vertex> unmarkedNodes=new ArrayList<Vertex>(); 
		for (int i=0;i<allNodes.size();i++)
			unmarkedNodes.add(allNodes.get(i));			
		
		root.mark=true;
		root.distance=0;
		allNodes.get(root.snpIndex).distance=0;
		allNodes.get(root.snpIndex).sigma=1;
					
		LinkedList<Vertex> queue = new LinkedList<Vertex>();
		queue.addLast(root);	
		while(!queue.isEmpty())	
		{
			Vertex u=queue.removeFirst();
			
			for (int i=0;i<unmarkedNodes.size();i++)
				if(adjMatrix[u.snpIndex][unmarkedNodes.get(i).snpIndex])
				{
					if ((!unmarkedNodes.get(i).mark)&&(allNodes.get(unmarkedNodes.get(i).snpIndex).distance==-1))
					{
						unmarkedNodes.get(i).mark=true;
						allNodes.get(unmarkedNodes.get(i).snpIndex).distance=allNodes.get(u.snpIndex).distance+1;
						allNodes.get(unmarkedNodes.get(i).snpIndex).sigma =allNodes.get(u.snpIndex).sigma;
						allNodes.get(u.snpIndex).downNeighbors.add(unmarkedNodes.get(i).snpIndex);
						queue.addLast(unmarkedNodes.get(i));						
					}
					else if (allNodes.get(unmarkedNodes.get(i).snpIndex).distance==allNodes.get(u.snpIndex).distance+1)
					{
						allNodes.get(unmarkedNodes.get(i).snpIndex).sigma += allNodes.get(u.snpIndex).sigma;
						allNodes.get(u.snpIndex).downNeighbors.add(unmarkedNodes.get(i).snpIndex);
					}
				}
			
			u.mark=true;
			unmarkedNodes.remove(u);
		}						
	}
		
		public double[][] allPairDistances(Vertex[] allNodes, Edge[][] edgeArray, boolean weighted)
		{
			// 0: init with infinite distance
			double[][] m = new double[nVertex][nVertex];
			for (int i=0; i<this.nVertex; i++)
				for (int j=0; j<this.nVertex; j++)
						m[j][i] = -1;
			
			// 1: BFS from every s in graph
			ArrayList<Vertex> unmarkedNodes=new ArrayList<Vertex>();			
			for (int i=0;i<allNodes.length;i++)
			{
				allNodes[i].mark=false;
				allNodes[i].distance=-1;
				unmarkedNodes.add(allNodes[i]);			
			}
			
			for (int s=0; s<this.nVertex; s++)
			{
				for (int i=0;i<allNodes.length;i++)
				{
					unmarkedNodes.get(i).mark=false;
					unmarkedNodes.get(i).distance=-1;
				}
				Vertex root = unmarkedNodes.get(s);
				double[] distance = new double[nVertex];
				ArrayList<Integer> component = this.componentBFS_closeness(root, unmarkedNodes,distance, edgeArray, weighted);
				if (component.size()>1)
					for (int k=0; k<component.size(); k++)
					{
						int to = component.get(k);
						m[to][s] = distance[to];
					}
			}
			return m;
		}
		
		public double[] closenessCentrality(Vertex[] allNodes, Edge[][] edgeArray, boolean weighted)
		{
			
			double[][] distMatrix;
			distMatrix = allPairDistances(allNodes, edgeArray, weighted);
			double[] cc = new double[nVertex];
			for (int j=0; j<nVertex; j++)
			{
				double c = 0;
				for (int i=0; i<nVertex; i++)
				{
					// +: reachable
					// 0: self
					// -1: unreachable
					if (distMatrix[i][j] > 0)
						c += 1.0/distMatrix[i][j];
				}
				cc[j] = c/(nVertex-1);
			}
			return cc;
		}
	
		public double[] betweennessCentrality(Vertex[] allNodes)
		{
			@SuppressWarnings("unchecked")
			ArrayList<Integer>[] downNeighbors = (ArrayList<Integer>[])new ArrayList[nVertex];
			ArrayList<Vertex> unmarkedNodes=new ArrayList<Vertex>();			
			for (int i=0;i<allNodes.length;i++)
			{
				allNodes[i].mark=false;
				allNodes[i].distance=-1;
				unmarkedNodes.add(allNodes[i]);	
			}
		
			double[] bc = new double[nVertex];
			for (int i=0; i<nVertex; i++)
				bc[i] = 0;		
			// run modified BFS from every source s
			for (int s=0; s<nVertex; s++)
			{
				// reset vectors
				Vertex root = unmarkedNodes.get(s);
				for (int i=0;i<allNodes.length;i++)
				{
					unmarkedNodes.get(i).mark=false;
					unmarkedNodes.get(i).distance=-1;
					unmarkedNodes.get(i).sigma=0;
					unmarkedNodes.get(i).downNeighbors=new ArrayList<Integer>();
				}
				componentBFS_betweenness(root, unmarkedNodes);
				double[] x = calculateXFromVertex(unmarkedNodes);
				for (int i=0; i<nVertex; i++)
					bc[i] += x[i];
			}
			return bc;
		}
		
		private double[] calculateXFromVertex(ArrayList<Vertex> allNodes)
		{
			double[] x = new double[nVertex];
			for (int i=0; i<nVertex; i++)
				x[i] = 0;
			// 1: find maximum distance from s
			int d_max = -1;
			for (int i=0; i<nVertex; i++)
				if (allNodes.get(i).distance > d_max) 
					d_max = allNodes.get(i).distance;
			// 2: calculate x values for layer d, starting from d_max, the bottom layer
			for (int d=d_max; d>=0; d--)
			{
				for (int i=0; i<nVertex; i++)
					if (allNodes.get(i).distance == d)
						if ((d-d_max)<float_compare)	// bottom layer
							x[i] = 1;
						else			// general case
						{
							x[i] = 1;
							for (int j=0; j<allNodes.get(i).downNeighbors.size(); j++)
							{
								int down = allNodes.get(i).downNeighbors.get(j);
								x[i] += x[down]*allNodes.get(i).sigma/allNodes.get(down).sigma;
							}
						}
			}
			return x;
		}
		
		public double[] betweennessCentrality(Vertex[] allNodes, Edge[][] edgeArray, boolean weighted)
		{
			float n = nVertex;
			double[] bc = new double[nVertex];
			for (int i=0; i<nVertex; i++)
				bc[i] = 0;
			for (int i=0;i<n;i++) 
			{			
				PriorityQueue<Vertex> S = null;
				Vertex s = allNodes[i];
				S = dijkstraExplore(s, allNodes, edgeArray, weighted);

				// The really new things in the Brandes algorithm are here:
				// Accumulation phase:

				while (!S.isEmpty()) {
					Vertex w = S.poll();
					
					/*for (int v : allNodes[w.snpIndex].preds) {
						double c = ((allNodes[v].sigma / allNodes[w.snpIndex].sigma) * (1.0 + allNodes[w.snpIndex].delta));
						allNodes[v].delta=allNodes[v].delta + c;
					}
					if (w != s) 
						bc[w.snpIndex]=bc[w.snpIndex]+allNodes[w.snpIndex].delta;*/
					//if (w != s) 
					allNodes[w.snpIndex].delta++;
					for (int v : allNodes[w.snpIndex].preds) {
						double c = ((allNodes[v].sigma / allNodes[w.snpIndex].sigma) * (allNodes[w.snpIndex].delta));
						allNodes[v].delta=allNodes[v].delta + c;
					}

				}
				for (int j=0;j<nVertex;j++)
					bc[j]+=allNodes[j].delta;
			}
			
			return bc;
		}
		
		protected PriorityQueue<Vertex> dijkstraExplore(Vertex root, Vertex[] allNodes, Edge[][] edgeArray, boolean weighted) 
		{
			PriorityQueue<Vertex> S = new PriorityQueue<Vertex>(nVertex,new BrandesNodeComparatorLargerFirst());
			PriorityQueue<Vertex> Q = new PriorityQueue<Vertex>(nVertex,new BrandesNodeComparatorSmallerFirst());

			for (int i=0;i<allNodes.length;i++)
			{
				allNodes[i].sigma=0;
				allNodes[i].wDistance=INFINITY;
				allNodes[i].preds=new HashSet<Integer>();
				allNodes[i].delta = 0;
			}
			root.wDistance=0;
			root.sigma=1;
			Q.add(root);

			while (!Q.isEmpty()) 
			{
				Vertex v = Q.poll();
				S.add(v);
				for (int i=0;i<nVertex;i++)
					if((adjMatrix[v.snpIndex][i])&&(v.snpIndex!=i))
					{
						double alt;
						if (weighted)
							alt = allNodes[v.snpIndex].wDistance +(1.0/edgeArray[v.snpIndex][i].entroInterac);
						else
							alt = allNodes[v.snpIndex].wDistance +1;
						double dw = allNodes[i].wDistance;
						
						if (alt < dw) {
							allNodes[i].wDistance = alt;
							updatePriority(S, allNodes[i]);
							updatePriority(Q, allNodes[i]);
							if (dw == INFINITY) {
								Q.add(allNodes[i]);
							}
							allNodes[i].sigma=0;
							allNodes[i].preds=new HashSet<Integer>();
						}
						if (allNodes[i].wDistance == alt) {
							allNodes[i].sigma = allNodes[i].sigma + allNodes[v.snpIndex].sigma;
							allNodes[i].preds.add(v.snpIndex);
						}
					}
			}

			return S;
		}
		
		/**
		 * Increasing comparator used for priority queues.
		 */
		protected class BrandesNodeComparatorLargerFirst implements
				Comparator<Vertex> {
			public int compare(Vertex x, Vertex y) {
				// return (int) ( (distance(y)*1000.0) - (distance(x)*1000.0) );
				if (x.wDistance > y.wDistance)     return -1;
				else if (x.wDistance < y.wDistance)   return 1;
				return 0;
			}
		}
		
		/**
		 * Decreasing comparator used for priority queues.
		 */
		protected class BrandesNodeComparatorSmallerFirst implements
				Comparator<Vertex> {
			public int compare(Vertex x, Vertex y) {
				// return (int) ( (distance(x)*1000.0) - (distance(y)*1000.0) );
				if (x.wDistance > y.wDistance)   return 1;
				else if (x.wDistance < y.wDistance)  return -1;
				return 0;
			}
		}
		/**
		 * Update the given priority queue if the given node changed its priority
		 * (here distance) and if the node is already part of the queue.
		 * 
		 * @param Q
		 *            The queue.
		 * @param node
		 *            The node.
		 */
		protected void updatePriority(PriorityQueue<Vertex> Q, Vertex node) {
			if (Q.contains(node)) {
				Q.remove(node);
				Q.add(node);
			}
		}
		
		public double calculateClusteringCoefficient()
		{
			int triangles = 0;
			int PathWithLengthTwo = 0;
			for (int i=0; i<nVertex; i++)
				for (int j=0;j<nVertex;j++)
				{
					if ((adjMatrix[i][j])&&i!=j)
					{
						for (int k=0; k<nVertex;k++)
							if ((adjMatrix[j][k])&&(j!=k)&&(k!=i)) 
							//found a path with length two from i to k
							{
								PathWithLengthTwo++;
								if (adjMatrix[k][i])
									triangles++;
							}
					}
				}
			
			return (double)triangles/PathWithLengthTwo;
		}
		
		public ArrayList<Integer> getVertexDegreeForAdjMatrix(boolean[][] ourMatrix, boolean[] removed) throws IOException
		{
			ArrayList<Integer> vertexDegrees = new ArrayList<Integer>();
			if (ourMatrix.length!=ourMatrix[0].length)
			{
				FileProcessing.log("ERROR: Wrong adjacency matrix!", true);
				return vertexDegrees;
			}
			for (int i=0;i<ourMatrix.length;i++)
			{
				if (!removed[i])
				{
					int count=0;
					for (int j=0;j<ourMatrix[0].length;j++)
					{
						if((ourMatrix[i][j])&&(i!=j))
							count++;
					}
					vertexDegrees.add(count);
				}
			}			
			return vertexDegrees;			
		}
		/*A K-core is a maximal subset of vertices such that each is connected 
		 * to at least k other vertices in the subset.
		 * This function returns all nodes that are connected to at least k other 
		 * nodes. They can be all in one set or in separated sets.
		 */
		public ArrayList<Integer> findKCoreNodes(int K, Vertex[] allNodes, boolean[][] adMatrix) throws IOException
		{
			//boolean[][] adMatrix=new boolean[nVertex][nVertex];			
			boolean[] removed =new boolean[nVertex];
			for (int i=0;i<nVertex;i++)
			{
				removed[i] = false;
				for (int j=0;j<nVertex;j++)
					adMatrix[i][j]=adjMatrix[i][j];
			}

			ArrayList<Integer> remainedNodes = new ArrayList<Integer>();
			for (int i=0;i<allNodes.length;i++)
				remainedNodes.add(allNodes[i].snpIndex);
			if (K<1)
				return remainedNodes; // all nodes are in one subset
			
			if (K==1)
			{
				ArrayList<ArrayList<Integer>> ccs = getAllCCs(allNodes); 
				ArrayList<Integer> nodes = new ArrayList<Integer>();
				for (int i=0;i<ccs.size();i++)
					for (int j=0;j<ccs.get(i).size();j++)
						nodes.add(ccs.get(i).get(j));
				return nodes; //all nodes which are in a component - are not isolated
			}
			
			boolean anyDegreeLessThanK = true;
			while (anyDegreeLessThanK)
			{
				anyDegreeLessThanK = false;
				ArrayList<Integer> degrees = getVertexDegreeForAdjMatrix(adMatrix, removed);
				if (degrees.size()!=remainedNodes.size())
				{
					FileProcessing.log("ERROR: degrees.size()!=remainedNodes.size()", true);
					return remainedNodes;
				}
				for (int i=remainedNodes.size()-1;i>=0;i--)
					if (degrees.get(i) < K)
					{
						anyDegreeLessThanK = true;
						//remove all its edges
						for (int j=0;j<adMatrix.length;j++)
						{
							adMatrix[remainedNodes.get(i)][j]=false;
							adMatrix[j][remainedNodes.get(i)]=false;
						}
						//removeNode
						removed[remainedNodes.get(i)]=true;
						remainedNodes.remove(i);
					}				
			}
			
			return remainedNodes;
		}

		public ArrayList<ArrayList<Integer>> findKCoreComponents(int K, Vertex[] allNodes) throws IOException
		{		
			boolean[][] adMatrix=new boolean[nVertex][nVertex];
			ArrayList<Integer> Nodes = findKCoreNodes(K, allNodes, adMatrix);
			
			ArrayList<ArrayList<Integer>> allComponents = new ArrayList<ArrayList<Integer>>();
			boolean[] marked=new boolean[nVertex];
			for (int i=0; i<nVertex;i++)
				marked[i]=false;
			
			LinkedList<Integer> queue = new LinkedList<Integer>();
			for (int i=0; i<Nodes.size();i++)
				if (!marked[Nodes.get(i)])
				{
					ArrayList<Integer> oneComponent = new ArrayList<Integer>();
					queue.addLast(Nodes.get(i));
					while(!queue.isEmpty())	
					{
						int u=queue.removeFirst();
						for (int j=0; j<Nodes.size();j++)
						{
							if ((adMatrix[u][Nodes.get(j)])&&(u!=Nodes.get(j))&&(!marked[Nodes.get(j)]))
							{
								queue.addLast(Nodes.get(j));
								marked[Nodes.get(j)]=true;
							}
						}
						marked[u]=true;
						oneComponent.add(u);
					}
					allComponents.add(oneComponent);
				}
			
			return allComponents;
		}
		
//		public double[] calculatePageRank(Vertex[] allNodes, boolean verbose)
//		{
//			double[] pr = new double[nVertex];
//			
//			double initialRank = 1.0 / nVertex;
//			for (int i=0;i<allNodes.length;i++)
//			{
//				allNodes[i].rank=initialRank;
//			}
//			ArrayList<Double> newRanks = new ArrayList<Double>(nVertex);
//			int iterationCount = 0;
//			double normDiff=0;
//			double dampingFactor = DEFAULT_DAMPING_FACTOR;
//			double precision = DEFAULT_PRECISION;
//			do {
//				normDiff = iteration(allNodes, dampingFactor, newRanks);
//				iterationCount++;
//				if (verbose)
//					System.err.printf("%6d%16.8f%n", iterationCount, normDiff);
//			} while (normDiff > precision);
//			
//			for (int i=0; i<nVertex; i++)
//				pr[i] = allNodes[i].rank;
//			return pr;
//		}
//		public double iteration(Vertex[] allNodes, double dampingFactor, ArrayList<Double> newRanks )  
//		{
//			double dampingTerm = (1 - dampingFactor) / allNodes.length;
//			newRanks.clear();
//			double danglingRank = 0;
//			for (int i = 0; i < allNodes.length; i++) 
//			{
//				double sum = 0;
//				for (int j=0;j<allNodes.length;j++)
//					if ((i!=j)&&(adjMatrix[i][j]))
//						sum+=allNodes[j].rank/getVertexDegree(j);
//
//				newRanks.add(dampingTerm + dampingFactor * sum);
//				if ((allNodes[i].isInGraph)&&(getVertexDegree(i) == 0))
//					danglingRank += allNodes[i].rank;
//			}
//			danglingRank *= dampingFactor / allNodes.length;
//
//			double normDiff = 0;
//			for (int i = 0; i < allNodes.length; i++) 
//			{
//				double currentRank = allNodes[i].rank;
//				double newRank = newRanks.get(i) + danglingRank;
//				normDiff += Math.abs(newRank - currentRank);
//				allNodes[i].rank=newRank;
//			}
//			return normDiff;
//		}

		/**
		 * To calculate page rank for the graph 
		 * weighted: if true we calculate weighted page rank
		 * original: if true we use the following eq to calculate values:
		 * PR(A) = (1-d) + d (PR(T1)/C(T1) + ... + PR(Tn)/C(Tn))
		 * where d is the dampingFactor
		 * original: if false we use the following eq:
		 * PR(A) = (1-d)/numberofnodes + d (PR(T1)/C(T1) + ... + PR(Tn)/C(Tn))
		 * @return page rank value for all vertices.
		 */
		public double[] calculatePageRank(Vertex[] allNodes, boolean verbose,Edge[][] edgeArray, boolean weighted, boolean original) throws IOException
		{
			double[] pr = new double[nVertex];
			
			double initialRank = 1.0 / notIsolated_Vertex;
			for (int i=0;i<allNodes.length;i++)
			{
				if (allNodes[i].isInGraph)
					allNodes[i].rank=initialRank;
				else
					allNodes[i].rank=0;
			}
			ArrayList<Double> newRanks = new ArrayList<Double>(nVertex);
			int iterationCount = 0;
			double normDiff=0;
			double dampingFactor = DEFAULT_DAMPING_FACTOR;
			double precision = DEFAULT_PRECISION;
			do {
				if (weighted)
				{
					//calculate the weight of each vertex as the summation of the weight of its edges
					double[] weights = calculate_vertex_weight(allNodes, edgeArray);
					normDiff = iteration_weighted(allNodes, dampingFactor, newRanks, weights, edgeArray);
				}
				else if (original)
					normDiff = iteration_original(allNodes, dampingFactor, newRanks);
				else
					normDiff = iteration(allNodes, dampingFactor, newRanks);
				iterationCount++;
				if (verbose)
					System.err.printf("%6d%16.8f%n", iterationCount, normDiff);
			} while (normDiff > precision);
			
			for (int i=0; i<nVertex; i++)
				pr[i] = allNodes[i].rank;
			return pr;
		}
		
		public double[] calculate_vertex_weight(Vertex[] allNodes, Edge[][] edgeArray)	
		{
			double[] weights = new double[allNodes.length];
			for (int i=0;i<allNodes.length;i++)
			{
				weights[i] = 0;
				if (allNodes[i].isInGraph)
				{
					for (int j=0;j<allNodes.length;j++)
						if ((i!=j)&&(adjMatrix[i][j]))
							weights[i]+= edgeArray[i][j].entroInterac;		
				}
			}
			return weights;
		}
		
		public double iteration(Vertex[] allNodes, double dampingFactor, ArrayList<Double> newRanks ) throws IOException 
		{
			int not_isolated = 0;
			for (int i=0;i<allNodes.length;i++)
				if (allNodes[i].isInGraph)
					not_isolated++;
			if (not_isolated != notIsolated_Vertex)
				FileProcessing.log("ERROR: not_isolated:"+not_isolated+" <> notIsolated_Vertex:"+notIsolated_Vertex, false);
			
			double dampingTerm = (1 - dampingFactor) / not_isolated;
			newRanks.clear();
			double danglingRank = 0;
			for (int i = 0; i < allNodes.length; i++) 
			{
				if (allNodes[i].isInGraph)
				{
					double sum = 0;
					for (int j=0;j<allNodes.length;j++)
						if ((i!=j)&&(adjMatrix[i][j]))
							sum+=allNodes[j].rank/getVertexDegree(j);

					newRanks.add(dampingTerm + dampingFactor * sum);
					if (getVertexDegree(i) == 0)
						danglingRank += allNodes[i].rank;
				}
				else newRanks.add(0.0);
			}
			danglingRank *= dampingFactor / allNodes.length;

			double normDiff = 0;
			for (int i = 0; i < allNodes.length; i++) 
			{
				if (allNodes[i].isInGraph)
				{
					double currentRank = allNodes[i].rank;
					double newRank = newRanks.get(i) + danglingRank;
					normDiff += Math.abs(newRank - currentRank);
					allNodes[i].rank=newRank;
				}
			}
			return normDiff;
		}
		
		public double iteration_original(Vertex[] allNodes, double dampingFactor, ArrayList<Double> newRanks ) throws IOException 
		{
			int not_isolated = 0;
			for (int i=0;i<allNodes.length;i++)
				if (allNodes[i].isInGraph)
					not_isolated++;
			if (not_isolated != notIsolated_Vertex)
				FileProcessing.log("ERROR: not_isolated:"+not_isolated+" <> notIsolated_Vertex:"+notIsolated_Vertex, false);
			
			newRanks.clear();
			double danglingRank = 0;
			for (int i = 0; i < allNodes.length; i++) 
			{
				if (allNodes[i].isInGraph)
				{
					double sum = 0;
					for (int j=0;j<allNodes.length;j++)
						if ((i!=j)&&(adjMatrix[i][j]))
							sum+=allNodes[j].rank/getVertexDegree(j);

					newRanks.add((1 - dampingFactor) + dampingFactor * sum);
					if (getVertexDegree(i) == 0)
						danglingRank += allNodes[i].rank;
				}
				else newRanks.add(0.0);
			}
			danglingRank *= dampingFactor / not_isolated;

			double normDiff = 0;
			for (int i = 0; i < allNodes.length; i++) 
			{
				if (allNodes[i].isInGraph)
				{
					double currentRank = allNodes[i].rank;
					double newRank = newRanks.get(i) + danglingRank;
					normDiff += Math.abs(newRank - currentRank);
					allNodes[i].rank=newRank;
				}
			}
			return normDiff;
		}
		
		public double iteration_weighted(Vertex[] allNodes, double dampingFactor, 
				ArrayList<Double> newRanks, double[] weights, Edge[][] edgeArray) throws IOException 
		{
			int not_isolated = 0;
			for (int i=0;i<allNodes.length;i++)
				if (allNodes[i].isInGraph)
					not_isolated++;
			if (not_isolated != notIsolated_Vertex)
				FileProcessing.log("ERROR: not_isolated:"+not_isolated+" <> notIsolated_Vertex:"+notIsolated_Vertex, false);
			
			newRanks.clear();
			double danglingRank = 0;
			for (int i = 0; i < allNodes.length; i++) 
			{
				if (allNodes[i].isInGraph)
				{
					double sum = 0;
					for (int j=0;j<allNodes.length;j++)
						if ((i!=j)&&(adjMatrix[i][j]))
							sum+=(allNodes[j].rank * (edgeArray[i][j].entroInterac/weights[j]));

					newRanks.add((1 - dampingFactor) + dampingFactor * sum);
					if (getVertexDegree(i) == 0)
						danglingRank += allNodes[i].rank;
				}
				else newRanks.add(0.0);
			}
			danglingRank *= dampingFactor / not_isolated;

			double normDiff = 0;
			for (int i = 0; i < allNodes.length; i++) 
			{
				if (allNodes[i].isInGraph)
				{
					double currentRank = allNodes[i].rank;
					double newRank = newRanks.get(i) + danglingRank;
					normDiff += Math.abs(newRank - currentRank);
					allNodes[i].rank=newRank;
				}
			}
			return normDiff;
		}
		
		/**
		 * To convert graph to a JUNG graph for computing betweenness centrality.
		 * 2012.07.26
		 * @return instance of UndirectedSparseGraph converted from "this".
		 */
/*		public edu.uci.ics.jung.graph.UndirectedSparseGraph<Vertex, Number> getJungGraph()
		{
			UndirectedSparseGraph<Vertex, Number> g = new UndirectedSparseGraph<Vertex, Number>();
			// 1. Add all vertices of "this" to g
			// 1.1 Do the following for every vertex
			Vertex v;
			g.addVertex(v);
			
			// 2. Add all edges of "this" to g
			// 2.1 Do the following for every edge
			Number e;
			g.addEdge(e, v1, v2);
			
			// 3. Done
			System.out.println(g);	// verify if you are getting the right g.
			return g;
		}*/
		
	}//******** end class Graph
	
}
