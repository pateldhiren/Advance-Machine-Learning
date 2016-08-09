package AML;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VarElimAlgorithm {
	ArrayList<node> toBeSaved = new ArrayList<node>();
	
	String [][] Product_Marginalize(String[][] str1, String[][] str2,BayesNet network)
	{
		int column=0,last_column, new_column_size=0, new_row_size = 1,another_columns[] = new int[str1[0].length-2],index = 0,t;
		float temp;
		BayesNode bn;
		List<String> list = new ArrayList<String>();
		List<Integer> skip_rows = new ArrayList<Integer>();
		for(int i=0;i<str1[0].length;i++)
		{
			//System.out.println(str1[0][i] + "  " + str2[0][0]);
			if(str1[0][i].equals(str2[0][0]))
			{
				column = i;
				
			}
			else if(!str1[0][i].equals("Probabilities"))
			{
				another_columns[index++] = i;
				bn = network.getNode(str1[0][i]);
				new_row_size *= bn.getOutcomesNumber();
				new_column_size++;
				list.add(str1[0][i]);
			}
			else
			{
				new_column_size++;
				list.add(str1[0][i]);
			}
		}
		
		new_row_size++;
		//System.out.println(new_row_size + "  " + new_column_size);
		String str[][] = new String[new_row_size][new_column_size];
		for(int i=0;i<new_column_size;i++)
		{
			str[0][i] = list.get(i);
		}
				
		//System.out.println(column);
		String value;
		last_column = str1[0].length-1;
		//System.out.println(last_column);
		for(int i=1;i<str2.length;i++)
		{
			value = str2[i][0];
			for(int j=1;j<str1.length;j++)
			{
				if(value.equals(str1[j][column]))
				{				
				   temp = Float.parseFloat(str1[j][last_column]) * Float.parseFloat(str2[i][1]);
				   str1[j][last_column] = String.valueOf(temp);
				}
			}
		}
		
		/*System.out.println();
		for(int j=0;j<str1.length;j++)
		{
			for(int k=0;k<str1[j].length;k++)
			{
				System.out.print(str1[j][k] + "  ");
			}
			System.out.println();
		}
		System.out.println();
	*/

		index = 1;
		int index1=1;
		while(index<new_row_size)
		{
			int j;	
			while(skip_rows.contains(index1))
				index1++;
			for(j=0;j<another_columns.length;j++)
			{
				str[index][j] = str1[index1][another_columns[j]];
			}
			
			temp = 0;
			for(int i=1;i<str1.length;i++)
			{
				//System.out.println("1" + "  " + str1[i][last_column]);
				t=0;
				for(j=0;j<another_columns.length;j++)
				{
					//System.out.println("2" + "  " + str[index][j] + "  " + str1[i][another_columns[j]]);
					if(str[index][j].equals(str1[i][another_columns[j]]))
						t++;
				}
				if(t==j)
				{
					skip_rows.add(i);
					//System.out.println("3" + "  " + str1[i][last_column]);
					temp+= Float.parseFloat(str1[i][last_column]);					
				}
			}
			str[index][new_column_size-1] = String.valueOf(temp);
			index++;
		}
		
		return str;
	}

	String [][] Get_One_Pd(String self_factor, String left_one, FactorBuilder ctb, BayesNet network,ArrayList<String> evidences)
	{
		if(!evidences.contains(left_one))
		{
		 factor spe_factor = null;		 
		for(int i=0;i<ctb.getFactors().size();i++)
		{
			if(self_factor.equals(ctb.getFactors().get(i).self))
			{
				spe_factor = ctb.getFactors().get(i);
				break;
			}
		}
		
		String str[][] = new String[spe_factor.probabilities.length][spe_factor.probabilities[0].length] ;
		 for (int i = 0; i < spe_factor.probabilities.length; i++) {
		        System.arraycopy(spe_factor.probabilities[i], 0, str[i], 0, spe_factor.probabilities[i].length);
		    }
		for(int i=0;i<spe_factor.invol_nodes.size();i++)
		{
			if(!spe_factor.invol_nodes.get(i).equals(left_one))
			{
				node n = ctb.getNodes().get(spe_factor.invol_nodes.get(i));
				str = Product_Marginalize(str ,n.pd , network);
			}
		}
		
		
		HashMap<String,node> hm = ctb.getNodes();
		node n = new node();
		n.name	= hm.get(left_one).getName();
	//	n.pd = hm.get(left_one).pd;
		n.assoc_factors = hm.get(left_one).assoc_factors;
					
		n.pd = str;
		float temp,sum = 0;;
		for(int i=1;i<n.pd.length;i++)
		{
			temp = Float.parseFloat(n.pd[i][1]);
			//temp = Float.parseFloat(n.pd[i][1])*Float.parseFloat(str[i][1]);
			//n.pd[i][1] = String.valueOf(temp);
			sum+=temp;
		}
		
		for(int i=1;i<n.pd.length;i++)
		{
			temp = Float.parseFloat(n.pd[i][1]);
			temp /= sum;
			n.pd[i][1] = String.valueOf(temp);			
		}
		//hm.put(left_one,n);
		str = n.pd;
		toBeSaved.add(n);
		return str;
		}
		else
			return null;
	}
	
	void saveAll(FactorBuilder ctb)
	{
		HashMap<String,node> hm = ctb.getNodes();
		node n;
		for(int i=0;i<toBeSaved.size();i++)
		{
			n =toBeSaved.get(i); 
			hm.put(n.getName(),n);
		}		
		toBeSaved.clear();
		System.out.println("free " + toBeSaved.size());
	}
	
	String [][] Get_One_Pd2(String self_factor, String left_one, FactorBuilder ctb, BayesNet network,ArrayList<String> evidences)
	{
		if(!evidences.contains(left_one))
		{
		 factor spe_factor = null;		 
		for(int i=0;i<ctb.getFactors().size();i++)
		{
			if(self_factor.equals(ctb.getFactors().get(i).self))
			{
				spe_factor = ctb.getFactors().get(i);
				break;
			}
		}
		
		String str[][] = new String[spe_factor.probabilities.length][spe_factor.probabilities[0].length] ;
		 for (int i = 0; i < spe_factor.probabilities.length; i++) {
		        System.arraycopy(spe_factor.probabilities[i], 0, str[i], 0, spe_factor.probabilities[i].length);
		    }
		for(int i=0;i<spe_factor.invol_nodes.size();i++)
		{
			if(!spe_factor.invol_nodes.get(i).equals(left_one))
			{
				node n = ctb.getNodes().get(spe_factor.invol_nodes.get(i));
				str = Product_Marginalize(str ,n.pd , network);
			}
		}
		
		
		HashMap<String,node> hm = ctb.getNodes();
		//node n = hm.get(left_one);
		
		node n = new node();                        //changes
		n.name	= hm.get(left_one).getName();		
	//	n.pd = hm.get(left_one).pd;
		n.pd = new String[hm.get(left_one).pd.length][hm.get(left_one).pd[0].length] ;
		 for (int i = 0; i < hm.get(left_one).pd.length; i++) {
		        System.arraycopy(hm.get(left_one).pd[i], 0, n.pd[i], 0, hm.get(left_one).pd[i].length);
		    }
		n.assoc_factors = hm.get(left_one).assoc_factors;
		
		//n.pd = str;
		float temp,sum = 0;;
		for(int i=1;i<n.pd.length;i++)
		{
			//temp = Float.parseFloat(n.pd[i][1]);
			temp = Float.parseFloat(n.pd[i][1])*Float.parseFloat(str[i][1]);
			n.pd[i][1] = String.valueOf(temp);
			sum+=temp;
		}
		
		for(int i=1;i<n.pd.length;i++)
		{
			temp = Float.parseFloat(n.pd[i][1]);
			temp /= sum;
			n.pd[i][1] = String.valueOf(temp);			
		}
		//hm.put(left_one,n);  //change
		str = n.pd;
		toBeSaved.add(n);
		return str;
		}
		else
			return null;
	}
}
