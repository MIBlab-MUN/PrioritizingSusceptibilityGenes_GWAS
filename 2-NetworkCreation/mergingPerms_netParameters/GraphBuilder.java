/*
 * File:           GraphBuilder.java
 * Author:         Ting Hu and Somayeh Kafaie
 * Date:           November, 2017
 * Purpose:        creating the graph and measuring some network parameters
 */

//package graphGeneration_IGs_pCount;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

//import RunSEN.SEN;

public class GraphBuilder {

	public static class Vertex implements Cloneable
	{
		public int snpIndex;
		public String snpName;
		public double entroInfo;	
		public boolean mark;
		public int[] funClass;
		public int distance;
		public Vertex parent;		
		
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
		}		
		public Vertex(int esnpIndex, String esnpName, float eentroInfo, int[] fClass) //**** if vertex characteristic is considered
		{
			snpIndex=esnpIndex;
			snpName=esnpName;
			entroInfo=eentroInfo;
			mark=false;
			funClass=fClass;
			distance=Short.MAX_VALUE;
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
		
        //Somayeh: I guess it finds the size of the largest connected component
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
						if(subGraphNodes.get(j).snpIndex<subGraphNodes.get(k).snpIndex)													
							subGraph.addEdge(edgeArray[subGraphNodes.get(j).snpIndex][subGraphNodes.get(k).snpIndex]);
						else
							subGraph.addEdge(edgeArray[subGraphNodes.get(k).snpIndex][subGraphNodes.get(j).snpIndex]);
//						System.out.println(j+"\t"+k+"\t"+subGraph.getEdgeNumber());
					}
			}
//			System.out.println(subGraph.getEdgeNumber());			
			return subGraph.getNonZeroVertexDegrees();
		}
		
		//Somayeh:START
		public String getGCCVertexDegreeDistribution(Vertex[] allNodes, Edge[][] edgeArray)
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
						if(subGraphNodes.get(j).snpIndex<subGraphNodes.get(k).snpIndex)													
							subGraph.addEdge(edgeArray[subGraphNodes.get(j).snpIndex][subGraphNodes.get(k).snpIndex]);
						else
							subGraph.addEdge(edgeArray[subGraphNodes.get(k).snpIndex][subGraphNodes.get(j).snpIndex]);
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
				
		public ArrayList<Double> getVertexWeightedDegreeCentrality(Edge[][] edgeArray)
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
							System.out.println("ha");
						
					}					
				}	
				vertexWeightedDegreeCentrality.add(cent);
			}				
			return vertexWeightedDegreeCentrality;
		}	
		
		
		/*public float getDegreeAssortativity() //******** for undirected simple graph
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
				if ((den1-num2<SEN.float_compare)&&(num1-num2<SEN.float_compare))
					return 1;
				else if(den1-num2<SEN.float_compare)
					return -1000;
				else				
					return (num1-num2)/(den1-num2);
			}		
		}*/
			
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
			
		public Graph getEdgeSwapped(int numberOfSwaps)
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
