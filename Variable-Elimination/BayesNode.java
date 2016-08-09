package AML;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BayesNode {

	private int Id;
	private String Name;
	private List<BayesNode> childList = new ArrayList<BayesNode>();
	private List<BayesNode> parentList = new ArrayList<BayesNode>();
	private List<String> outcomes = new ArrayList<String>();
	//private List<Double> probabilities = new ArrayList<Double>();
	private String probabilities[][];
	BayesNode()
	{
		
	}
	BayesNode(String name)
	{
		Name = name;
	}
	
	void setId(int id)
	{
		Id = id;
	}
	
	int getId()
	{
		return Id;
	}
	
	String getName()
	{
		return Name;
	}
	
	void setParents(BayesNet net,String... parents)
	{
		for(int i=0;i<parents.length;i++)
		{
			//BayesNet b = new BayesNet();
			BayesNode node = net.getNode(parents[i]);
			if(node == null)
			{
				try {
					throw new Exception();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("Node doesn't exist");
					return;
				}
				//System.out.println("Node doesn't exist");
			}
			if(!this.parentList.contains(node))
			{
				this.parentList.add(node);
				node.childList.add(this);
			}
		}
	}
	
	void setOutcomes(String... outcomes)
	{
		for(int i=0;i<outcomes.length;i++)
		{
			if(!this.outcomes.contains(outcomes[i]))
			{
				this.outcomes.add(outcomes[i]);
			}
		}
	}
	List<String> getOutcomes()
	{
		return this.outcomes;
	}
	int  getOutcomesNumber()
	{
		return this.outcomes.size();
	}	
	
	void setProbabilities(double...p)
	{
	/*	for(int i=0;i<p.length;i++)
		{
			this.probabilities.add(p[i]);
		}
		*/
		int temp=1,temp_copy,index;
		Iterator it = this.parentList.iterator();
		BayesNode bn;
		while(it.hasNext())
		{
			 bn = (BayesNode) it.next();
			 temp*= bn.getOutcomesNumber();
		}
		temp*= this.getOutcomesNumber();
		this.probabilities = new String[temp+1][this.parentList.size() + 2];
		int rows,columns=0;
		for(int i=0;i<this.parentList.size();i++)
		{
			this.probabilities[0][columns++] = this.parentList.get(i).getName();
		}
		this.probabilities[0][columns++] = this.getName();
		this.probabilities[0][columns++] = "Probabilities";
		columns = 0;
		String str;
		
		temp_copy = temp;
		it = this.parentList.iterator();
		while(it.hasNext())
		{
			rows = 1;
			 bn = (BayesNode) it.next();
			 temp_copy /= bn.getOutcomesNumber();
			 index=0;
			 while(rows<=temp)
			 {
				 str = bn.getOutcomes().get(index % bn.getOutcomesNumber());
			 for(int j=0;j<temp_copy;j++)
			 {
				 this.probabilities[rows++][columns] = str;					 
			 }
			 index++;
			 }
			 columns++;
		}
		rows = 1;
		 while(rows<=temp)
		 {
			 this.probabilities[rows][columns] = this.getOutcomes().get((rows-1) % this.getOutcomesNumber());
			 rows++;
		 }
		columns++;
		rows = 1;
		for(int i=0;i<p.length;i++)
		{
			this.probabilities[rows++][columns] = String.valueOf(p[i]);
		}
	}
	
	String[][] getProbabilities()
	{
		return probabilities;
	}
	
	List<BayesNode> getParents()
	{
		return this.parentList;
	}
	List<BayesNode> getChildren()
	{
		return this.childList;
	}
}
