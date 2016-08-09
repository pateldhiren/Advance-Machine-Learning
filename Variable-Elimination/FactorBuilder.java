package AML;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

class node
{
	String name;
	String pd[][];
	List<Integer> assoc_factors = new ArrayList<Integer>();
	
	String getName()
	{
		return name;
	}
	String[][] getProbabilities()
	{
		return pd;
	}
}

class factor
{
List<String> invol_nodes = new ArrayList<String>();	
String self;
String probabilities[][];
}

public class FactorBuilder {

	private HashMap<String,node> nodes = new HashMap<String,node>();
	private List<factor> factors = new ArrayList<factor>();
	
	void BuildFactors(BayesNet net)
	{
		List<BayesNode> nodes_list = net.getNodes();
		BayesNode bn;
		node n;
		for(int i=0;i<nodes_list.size();i++)
		{
			n = new node();
			bn = nodes_list.get(i);
			n.name = bn.getName();
			n.pd = new String[bn.getOutcomesNumber()+1][2];
			n.pd[0][0] = bn.getName(); n.pd[0][1] = "Probabilities";
			for(int j=1;j<=bn.getOutcomesNumber();j++)
			{
				n.pd[j][0] = bn.getOutcomes().get(j-1);
				n.pd[j][1] = "1.0"; 
			}			
			if(bn.getParents().isEmpty())
				n.pd = bn.getProbabilities();				
			nodes.put(n.name, n);
		
			List<BayesNode> temp;
			factor f = new factor();
			if(!bn.getParents().isEmpty())
			{
			temp = bn.getParents();
			Iterator<BayesNode> it = temp.iterator();
			BayesNode bn_temp;
				while(it.hasNext())
				{				
				bn_temp = it.next();
				f.invol_nodes.add(bn_temp.getName());				
				}
				f.invol_nodes.add(bn.getName());
				f.self = bn.getName();
			f.probabilities = bn.getProbabilities();					
			factors.add(f);
			}
		}
		associate_factors();
	}
	
	void associate_factors()
	{
		Iterator it = factors.iterator();
		String str;
		int index = 0;
		while(it.hasNext())
		{			
			factor f = (factor) it.next();
			for(int i=0;i<f.invol_nodes.size();i++)
			{
				str = f.invol_nodes.get(i);
				node n = nodes.get(str);
				n.assoc_factors.add(index);
				nodes.put(str, n);
			}
			index++;
		}
	}
	
	
	node getNode(String name)
	{
		return nodes.get(name);
	}
	
	HashMap<String,node> getNodes()
	{
		return nodes;
	}
	List<factor> getFactors()
	{
		return factors;
	}
}
