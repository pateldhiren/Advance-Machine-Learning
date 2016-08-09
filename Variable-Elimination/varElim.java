package AML;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.Object;


public class varElim extends JFrame{
	BayesNet network = new BayesNet();
	BayesNode node = new BayesNode();
	FactorBuilder ctb = new FactorBuilder();
	VarElimAlgorithm cta = new VarElimAlgorithm();
	HashMap<String,node> node_list;
	node nl;
	JPanel secondPanel;
	JPanel timePanel;
	JList listbox;
	ArrayList<String> evidences = new ArrayList<String>();
	String time = "";
	
	 public varElim() {

	        initUI();
	    }
	 
	    public final void initUI() {

	    	JPanel basic = new JPanel();
	        basic.setLayout(new BoxLayout(basic, BoxLayout.Y_AXIS));
	        add(basic);
	        
	    	JPanel topPanel = new JPanel(new BorderLayout(0,0));    	     
	        JLabel topLabel = new JLabel("<html><span style='font-size:15px'>"+"Car Failure Diagnosis"+"</span></html>");
	       
	        topLabel.setBorder(BorderFactory.createEmptyBorder(0, 200, 5, 0));
	            	     
	        
	        ImageIcon icon = new ImageIcon("/home/dhiren/workspace/Car-Repair-icon.png");
	        Image image = icon.getImage();
	        JLabel label = new JLabel();
	        Image resizedImage = 
	        	    image.getScaledInstance(60, 60, Image.SCALE_DEFAULT);
	        label.setIcon(new ImageIcon(resizedImage));
	        label.setBorder(BorderFactory.createEmptyBorder(0, 30, 5, 0));
	        //label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
	        topPanel.add(label,BorderLayout.WEST);
	        
	        topPanel.add(topLabel,BorderLayout.CENTER);
	        JSeparator separator = new JSeparator();
	        separator.setForeground(Color.gray);
	        topPanel.add(separator, BorderLayout.SOUTH);
	        basic.add(topPanel);        	       
	        basic.add(Box.createRigidArea(new Dimension(0, 15)));
	        
	        
	       secondPanel = new JPanel(new BorderLayout());    	    
	        String	listData[] =
	    		{
	    			"Alternator",
	    			"Charging System",
	    			"Battery Age",
	    			"Battery Voltage",
	    			"HeadLights",
	    			"Main Fuse",
	    			"Distributor",
	    			"Voltage At Plug",
	    			"Starter Motor",
	    			"Spark Plugs",
	    			"Spark Timing",
	    			"Starter System",
	    			"Fuel System",
	    			"Spark Quality",
	    			"Car Cranks",
	    			"Air Filter",
	    			"Air system",
	    			"Car Starts"
	    		};
	        listbox = new JList( listData ); 
	        listbox.setFixedCellHeight(25);
	        listbox.setBorder(new EmptyBorder(20,10, 10, 10));
	        listbox.setSelectionBackground(Color.gray);
	        listbox.setVisibleRowCount(18); 
	        listbox.setName("List");
	        secondPanel.add(listbox, BorderLayout.WEST);     	       
	        basic.add(secondPanel);
	        basic.add(Box.createRigidArea(new Dimension(0, 15)));
	        
	        JLabel timeLabel = new JLabel(time); 
	        timePanel = new JPanel(new BorderLayout());  
	        timePanel.add(timeLabel);
	        basic.add(timePanel);
	        
	        JPanel bottom = new JPanel();
	        bottom.setAlignmentX(1f);
	        bottom.setLayout(new BoxLayout(bottom, BoxLayout.X_AXIS));
	        JButton select = new JButton("Select");
	        JButton count = new JButton("Count");
	        JButton reset = new JButton("Reset");
	        bottom.add(select);
	        bottom.add(Box.createRigidArea(new Dimension(5, 0)));
	        bottom.add(count);
	        bottom.add(Box.createRigidArea(new Dimension(5, 0)));
	        bottom.add(reset);
	        bottom.add(Box.createRigidArea(new Dimension(15, 0)));
	        basic.add(bottom);    	       
	        basic.add(Box.createRigidArea(new Dimension(0, 10)));
	            	   
	        
	        select.addActionListener(new ActionListener() {
	        	@Override
	            public void actionPerformed(ActionEvent e) {	        		        	
	        		rearrangeGUI();
	            }
	         });
	        
	        reset.addActionListener(new ActionListener() {
	        	@Override
	            public void actionPerformed(ActionEvent e) {	        		        	
	        		reset();
	        		rearrangeGUI();
	            }
	         });
	        
	        count.addActionListener(new ActionListener() {
	        	@Override
	            public void actionPerformed(ActionEvent e) {	        
	        		
	        		Component[] component = secondPanel.getComponents();
	        		 for(int i=0; i<component.length; i++)
	        		 {	        			 
	        		     if (component[i] instanceof JPanel)
	        		     {
	        		    	 JPanel panel = (JPanel) component[i];
	        		    	 Component[] componentDynamic = panel.getComponents();
	        		    	 for(int j=0; j<componentDynamic.length; j++)
	    	        		 {
	        		    		 JPanel panel1 = (JPanel) componentDynamic[j];
	        		    		 Component[] component1 = panel1.getComponents();
	        		    		 for(int k=0; k<component1.length; k++)
		    	        		 {
	        		    			 if (component1[k] instanceof JTable)
	        	        		     {
	        		    			 JTable table = (JTable)component1[k];
	        		    			 String nodeName = (String) table.getColumnModel().getColumn(0).getHeaderValue();
	        		    			// System.out.println(nodeName);	
	        		    			 nl = node_list.get(nodeName);	        		    			 
	    	        		    	// System.out.println(table.getModel().getValueAt(0, 1));
	    	        		    	// System.out.println(table.getModel().getValueAt(1, 1));
	    	        		    	 
	    	        		    	 for(int q=1;q<=nl.pd.length-1;q++)
	    	        		    	 {
	    	        		    		 nl.pd[q][1] = (String) table.getModel().getValueAt(q-1, 1);
	    	        		    	 }
	        	        		     }	        		    			
		    	        		 }	        		    	 
	    	        		 }
	        		     }	        		        
	        		 }
	        		 
	        		String str[][];
	        		long start = System.currentTimeMillis();                                                      
	        		str = cta.Get_One_Pd2("Car Starts", "Car Cranks", ctb, network, evidences);	     			
	     			str = cta.Get_One_Pd2("Car Starts", "Spark Quality", ctb, network, evidences);
	     			str = cta.Get_One_Pd2("Car Starts", "Fuel System", ctb, network, evidences);
	     			str = cta.Get_One_Pd2("Car Starts", "Air system", ctb, network, evidences);
	     			str = cta.Get_One_Pd2("Car Starts", "Spark Timing", ctb, network, evidences);	     			
	     			cta.saveAll(ctb);
	     			str = cta.Get_One_Pd2("Air system","Air Filter", ctb, network, evidences);
	     			cta.saveAll(ctb);
	     			str = cta.Get_One_Pd2("Spark Quality","Spark Plugs", ctb, network, evidences);
	     			str = cta.Get_One_Pd2("Spark Quality","Voltage At Plug", ctb, network, evidences);
	     			cta.saveAll(ctb);
	     			str = cta.Get_One_Pd2("Spark Timing","Distributor", ctb, network, evidences);
	     			cta.saveAll(ctb);
	     			str = cta.Get_One_Pd2("Car Cranks","Starter System", ctb, network, evidences);
	     			cta.saveAll(ctb);
	     			str = cta.Get_One_Pd2("Starter System","Starter Motor", ctb, network, evidences);
	     			str = cta.Get_One_Pd2("Starter System","Main Fuse", ctb, network, evidences);
	     			str = cta.Get_One_Pd2("Starter System","Battery Voltage", ctb, network, evidences);
	     			cta.saveAll(ctb);
	     			
	     	    	str = cta.Get_One_Pd2("HeadLights", "Battery Voltage", ctb, network, evidences);
	     	    	cta.saveAll(ctb);
	     		
	     			str = cta.Get_One_Pd2("Voltage At Plug", "Distributor", ctb, network, evidences);	     		     	
	     			str = cta.Get_One_Pd2("Voltage At Plug", "Main Fuse", ctb, network, evidences);	 
	     			str = cta.Get_One_Pd2("Voltage At Plug", "Battery Voltage", ctb, network, evidences);	
	     			cta.saveAll(ctb);
	     			
	     			str = cta.Get_One_Pd2("Battery Voltage", "Battery Age", ctb, network, evidences);
	     			str = cta.Get_One_Pd2("Battery Voltage", "Charging System", ctb, network, evidences);
	     			cta.saveAll(ctb);
	     			str = cta.Get_One_Pd2("Charging System", "Alternator", ctb, network, evidences);	     			     		
	     			cta.saveAll(ctb);	     			
     				     			
	     			
	     		//start	
	     			str = cta.Get_One_Pd2("Charging System", "Charging System", ctb, network, evidences);	     			     		
	     			cta.saveAll(ctb);	   
	     			str = cta.Get_One_Pd2("Battery Voltage", "Battery Voltage", ctb, network, evidences);
	     			cta.saveAll(ctb);
	     			str = cta.Get_One_Pd2("HeadLights", "HeadLights", ctb, network, evidences);
	     	    	cta.saveAll(ctb);
	     	    	str = cta.Get_One_Pd2("Voltage At Plug", "Voltage At Plug", ctb, network, evidences);	
	     			cta.saveAll(ctb);
	     			str = cta.Get_One_Pd2("Starter System","Starter System", ctb, network, evidences);
	     			cta.saveAll(ctb);
	     			str = cta.Get_One_Pd2("Spark Timing","Spark Timing", ctb, network, evidences);
	     			cta.saveAll(ctb);
	     			str = cta.Get_One_Pd2("Spark Quality","Spark Quality", ctb, network, evidences);
	     			cta.saveAll(ctb);
	     			str = cta.Get_One_Pd2("Car Cranks","Car Cranks", ctb, network, evidences);
	     			cta.saveAll(ctb);
	     			str = cta.Get_One_Pd2("Air system","Air system", ctb, network, evidences);
	     			cta.saveAll(ctb);
	     			str = cta.Get_One_Pd2("Car Starts", "Car Starts", ctb, network, evidences);	  
	     			cta.saveAll(ctb);	     					
	     			long end = System.currentTimeMillis();
	     			System.out.println(start + "  " + end + "  " + (end-start));
	     			time = "Time Spent :  " + String.valueOf((end-start) + "  milliseconds");
	     			
	        		rearrangeGUI();
	        		evidences.clear();
	            }
	         });
	        	      
	        
	        setTitle("Car Failure Diagnosis - Variable Elimination Method");
	        setSize(new Dimension(900, 650));
	        setResizable(false);
	        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	        setLocationRelativeTo(null);
	    }
	    
	    void rearrangeGUI()
	    {		
		    	secondPanel.removeAll();
	    		secondPanel.add(listbox, BorderLayout.WEST);    
	    		JPanel dynamic = new JPanel(new FlowLayout(15));
	    		   		   
	    		List<String> SelectedValues = listbox.getSelectedValuesList();
	    		Iterator<String> it = SelectedValues.iterator();
	    		
	    		while(it.hasNext())
	    		{
	    			String temp = it.next();
	    			//System.out.println(temp);
	    			node tempNode = ctb.getNode(temp);
	    			//BayesNode tempNode = network.getNode(temp);
	    			String temp1[][] = tempNode.getProbabilities();
	    			Object columnNames[] = temp1[0];
	    			Object rowData[][] = new String[temp1.length-1][];
	    			for(int i = 1; i<temp1.length;i++)
	    				rowData[i-1] = temp1[i];
	    			
	    			DefaultTableModel model = new DefaultTableModel(rowData, columnNames);	        		 
	    			JTable table = new JTable(model);
	    			table.setRowHeight(20);
	    			TableColumnModel columnModel = table.getColumnModel();
	    			columnModel.getColumn(0).setPreferredWidth(100);
	    			
	    			model.addTableModelListener(new TableModelListener() {
	
	    			      public void tableChanged(TableModelEvent e) {
	    			         TableModel model = (TableModel)e.getSource();    			         
	    			         JTable temp = new JTable(model);
	    			         String nodeName = (String) temp.getColumnModel().getColumn(0).getHeaderValue();
	    			         System.out.println(nodeName);
	    			         if(!evidences.contains(nodeName))
	    			        	 evidences.add(nodeName);
	    			      }
	    			    });
	    			     JPanel tempPanel = new JPanel(new BorderLayout()); 
		        		table.getTableHeader().setBackground(Color.lightGray);	        		
		        		table.getTableHeader().setFont(new Font("Courier New", Font.BOLD, 12));
		        		tempPanel.add(table.getTableHeader(), BorderLayout.NORTH);
		        		tempPanel.add(table, BorderLayout.CENTER);    
		        		dynamic.add(tempPanel);		        		
	    		}
	    			    		
	    		secondPanel.add(dynamic);	    		
	    		secondPanel.revalidate();
	    		secondPanel.repaint();
	    		
	    		timePanel.removeAll();
	    		timePanel.revalidate();
	    	     timePanel.repaint();
	    	     
	    		JLabel timeLabel = new JLabel(time);   
		    timePanel.add(timeLabel,BorderLayout.EAST);
		    timePanel.revalidate();
    		    timePanel.repaint();
	       }
	    
	void reset()
	{
				
	     evidences.clear();
		
		BayesNet networkTemp = new BayesNet();		
		FactorBuilder ctbTemp = new FactorBuilder();
		
		node = networkTemp.addNode("Alternator");
		node.setOutcomes("okay","faulty");
		node.setProbabilities(0.997,0.003);
		
		node = networkTemp.addNode("Battery Age");
		node.setOutcomes("new","old","very_old");
		node.setProbabilities(0.4,0.4,0.2);
		
		node = networkTemp.addNode("Charging System");		
		node.setParents(networkTemp,"Alternator");
		node.setOutcomes("okay","faulty");
		node.setProbabilities(0.5,0.5,0,1);
				
		node = networkTemp.addNode("Battery Voltage");		
		node.setParents(networkTemp,"Charging System","Battery Age");
		node.setOutcomes("strong","weak","dead");
		node.setProbabilities(0.95,0.04,0.01,0.8,0.15,0.05,0.6,0.3,0.1,0.008,0.3,0.692,0.004,0.2,0.796,0.002,0.1,0.898);
		
		node = networkTemp.addNode("HeadLights");		
		node.setParents(networkTemp,"Battery Voltage");
		node.setOutcomes("bright","deam","off");
		node.setProbabilities(0.94,0.01,0.05,0,0.95,0.05,0,0,1);
		
		node = networkTemp.addNode("Distributor");		
		//node.setParents(network,"BV");
		node.setOutcomes("okay","faulty");
		node.setProbabilities(0.99,0.0099);
		
		node = networkTemp.addNode("Main Fuse");		
		//node.setParents(network,"BV");
		node.setOutcomes("okay","blown");
		node.setProbabilities(0.99,0.01);
		
		node = networkTemp.addNode("Voltage At Plug");		
		node.setParents(networkTemp,"Main Fuse","Distributor","Battery Voltage");
		node.setOutcomes("strong","weak","none");
		node.setProbabilities(0.9,0.05,0.05, 0,0.9,0.1, 0,0,1, 0.1,0.1,0.8, 0,0.1,0.9, 0,0,1, 0,0,1, 0,0,1, 0,0,1,
								0,0,1, 0,0,1, 0,0,1);
						
		node = networkTemp.addNode("Spark Plugs");
		node.setOutcomes("okay","too_wide","fouled");
		node.setProbabilities(0.7,0.1,0.2);
		
		node = networkTemp.addNode("Starter Motor");
		node.setOutcomes("okay","faulty");
		node.setProbabilities(0.995,0.00499);
		
		node = networkTemp.addNode("Spark Timing");
		node.setParents(networkTemp,"Distributor");
		node.setOutcomes("good","bad","very_bad");
		node.setProbabilities(0.9,0.09,0.01,0.2,0.3,0.5);
		
		node = networkTemp.addNode("Spark Quality");
		node.setParents(networkTemp,"Spark Plugs","Voltage At Plug");
		node.setOutcomes("good","bad","very_bad");
		node.setProbabilities(1,0,0,0,1,0,0,0,1,0,1,0,0,0,1,0,0,1,0,1,0,0,0,1,0,0,1);
		
		node = networkTemp.addNode("Starter System");
		node.setParents(networkTemp,"Main Fuse","Starter Motor","Battery Voltage");
		node.setOutcomes("okay","faulty");
		node.setProbabilities(0.98,0.02, 0.9,0.1, 0.1,0.9, 0.02,0.98, 0.01,0.99, 0.005,0.995,
							0,1, 0,1, 0,1,0,1, 0,1,0,1);
		
		node = networkTemp.addNode("Fuel System");
		node.setOutcomes("okay","faulty");
		node.setProbabilities(0.9,0.1);
		
		node = networkTemp.addNode("Car Cranks");
		node.setParents(networkTemp,"Starter System");
		node.setOutcomes("true","false");
		node.setProbabilities(0.8,0.2, 0.05,0.95);
		
		node = networkTemp.addNode("Air Filter");
		node.setOutcomes("clean","dirty");
		node.setProbabilities(0.9,0.1);
		
		node = networkTemp.addNode("Air system");
		node.setParents(networkTemp,"Air Filter");
		node.setOutcomes("okay","faulty");
		node.setProbabilities(0.9,0.1, 0.3,0.7);
		
		node = networkTemp.addNode("Car Starts");
		node.setParents(networkTemp,"Car Cranks","Fuel System","Spark Quality","Air system","Spark Timing");
		node.setOutcomes("true","false");		
		node.setProbabilities(0.99,0.0099, 0.98,0.02, 0.7,0.3, 0.8,0.2, 0.75,0.25, 0.6,0.4, 
				0.7,0.3, 0.65,0.35, 0.5,0.5, 0.6,0.4, 0.5,0.5, 0.4,0.6, 0,1, 0,1, 0,1, 0,1, 0,1, 0,1, 
				0.1, 0.9, 0.05,0.95, 0.02,0.98, 0.05,0.95, 0.02,0.98, 0.01,0.99, 0.05,0.95,0.02,0.98, 
				0.01,0.99, 0.02,0.98, 0.01,0.99, 0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1, 
				0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1, 0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,  
				0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1);
		
		ctbTemp.BuildFactors(networkTemp);
		
		network = networkTemp;
		ctb = ctbTemp;
		node_list.clear();
		node_list = ctb.getNodes();
	}
	void network()
	{
		node = network.addNode("Alternator");
		node.setOutcomes("okay","faulty");
		node.setProbabilities(0.997,0.003);
		
		node = network.addNode("Battery Age");
		node.setOutcomes("new","old","very_old");
		node.setProbabilities(0.4,0.4,0.2);
		
		node = network.addNode("Charging System");		
		node.setParents(network,"Alternator");
		node.setOutcomes("okay","faulty");
		node.setProbabilities(0.5,0.5,0,1);
				
		node = network.addNode("Battery Voltage");		
		node.setParents(network,"Charging System","Battery Age");
		node.setOutcomes("strong","weak","dead");
		node.setProbabilities(0.95,0.04,0.01,0.8,0.15,0.05,0.6,0.3,0.1,0.008,0.3,0.692,0.004,0.2,0.796,0.002,0.1,0.898);
		
		node = network.addNode("HeadLights");		
		node.setParents(network,"Battery Voltage");
		node.setOutcomes("bright","deam","off");
		node.setProbabilities(0.94,0.01,0.05,0,0.95,0.05,0,0,1);
		
		node = network.addNode("Distributor");		
		//node.setParents(network,"BV");
		node.setOutcomes("okay","faulty");
		node.setProbabilities(0.99,0.01);
		
		node = network.addNode("Main Fuse");		
		//node.setParents(network,"BV");
		node.setOutcomes("okay","blown");
		node.setProbabilities(0.99,0.01);
		
		node = network.addNode("Voltage At Plug");		
		node.setParents(network,"Main Fuse","Distributor","Battery Voltage");
		node.setOutcomes("strong","weak","none");
		node.setProbabilities(0.9,0.05,0.05, 0,0.9,0.1, 0,0,1, 0.1,0.1,0.8, 0,0.1,0.9, 0,0,1, 0,0,1, 0,0,1, 0,0,1,
								0,0,1, 0,0,1, 0,0,1);
						
		node = network.addNode("Spark Plugs");
		node.setOutcomes("okay","too_wide","fouled");
		node.setProbabilities(0.7,0.1,0.2);
		
		node = network.addNode("Starter Motor");
		node.setOutcomes("okay","faulty");
		node.setProbabilities(0.995,0.00499);
		
		node = network.addNode("Spark Timing");
		node.setParents(network,"Distributor");
		node.setOutcomes("good","bad","very_bad");
		node.setProbabilities(0.9,0.09,0.01,0.2,0.3,0.5);
		
		node = network.addNode("Spark Quality");
		node.setParents(network,"Spark Plugs","Voltage At Plug");
		node.setOutcomes("good","bad","very_bad");
		node.setProbabilities(1,0,0,0,1,0,0,0,1,0,1,0,0,0,1,0,0,1,0,1,0,0,0,1,0,0,1);
		
		node = network.addNode("Starter System");
		node.setParents(network,"Main Fuse","Starter Motor","Battery Voltage");
		node.setOutcomes("okay","faulty");
		node.setProbabilities(0.98,0.02, 0.9,0.1, 0.1,0.9, 0.02,0.98, 0.01,0.99, 0.005,0.995,
							0,1, 0,1, 0,1,0,1, 0,1,0,1);
		
		node = network.addNode("Fuel System");
		node.setOutcomes("okay","faulty");
		node.setProbabilities(0.9,0.1);
		
		node = network.addNode("Car Cranks");
		node.setParents(network,"Starter System");
		node.setOutcomes("true","false");
		node.setProbabilities(0.8,0.2, 0.05,0.95);
		
		node = network.addNode("Air Filter");
		node.setOutcomes("clean","dirty");
		node.setProbabilities(0.9,0.1);
		
		node = network.addNode("Air system");
		node.setParents(network,"Air Filter");
		node.setOutcomes("okay","faulty");
		node.setProbabilities(0.9,0.1, 0.3,0.7);
		
		node = network.addNode("Car Starts");
		node.setParents(network,"Car Cranks","Fuel System","Spark Quality","Air system","Spark Timing");
		node.setOutcomes("true","false");		
		node.setProbabilities(0.99, 0.01,
	        	0.98, 0.2,
	        	0.7,  0.3,
	        	0.8,  0.2,
	        	0.75, 0.25,
	        	0.6,  0.4,
	        	0.7,  0.3,
	        	0.65, 0.35,
	        	0.5,  0.5,
	        	0.6,  0.4,
	        	0.5,  0.5,
	        	0.4,  0.6,
	        	0,    1,
	        	0,    1,
	        	0,    1,
	        	0,    1,
	        	0,    1,
	        	0,    1,
	        	0.1,  0.9,
	        	0.05, 0.95,
	        	0.02, 0.98,
	        	0.05, 0.95,
	        	0.01, 0.99,
	        	0.05, 0.95,
	        	0.02, 0.98,
	        	0.01, 0.99,
	        	0.05, 0.95,
	        	0.02, 0.98,
	        	0.01, 0.99,
	        	0.02, 0.98,
	        	0.01, 0.99,
	        	0,    1,
	        	0,    1,
	        	0,    1,
	        	0,    1,
	        	0,    1,
	        	0,    1,
	        	0,    1,
	        	0,    1,
	        	0,    1,
	        	0,    1,
	        	0,    1,
	        	0,    1,
	        	0,    1,
	        	0,    1,
	        	0,    1,
	        	0,    1,
	        	0,    1,
	        	0,    1,
	        	0,    1,
	        	0,    1,
	        	0,    1,
	        	0,    1,
	        	0,    1,
	        	0,    1,
	        	0,    1,
	        	0,    1,
	        	0,    1,
	        	0,    1,
	        	0,    1,
	        	0,    1,
	        	0,    1,
	        	0,    1,
	        	0,    1,
	        	0,    1,
	        	0,    1,
	        	0,    1,
	        	0,    1,
	        	0,    1,
	        	0,    1,
	        	0,    1,
	        	0,    1
	        );
		
		ctb.BuildFactors(network);		
		node_list = ctb.getNodes();		
		
		String str[][];
		
		/*nl = node_list.get("HeadLights");
		nl.pd[1][1]="0"; nl.pd[2][1]="0"; nl.pd[3][1]="1";
		node_list.put("HeadLights", nl);
		nl= node_list.get("Voltage At Plug");
		nl.pd[1][1]="0"; nl.pd[2][1]="0"; nl.pd[3][1]="1";
		node_list.put("Voltage At Plug", nl);
		
		nl = node_list.get("Main Fuse");
		nl.pd[1][1]="0.1"; nl.pd[2][1]="0.9";
		node_list.put("Main Fuse", nl);
		
		
		str = cta.Get_One_Pd("HeadLights", "Battery Voltage", ctb, network);
		str = cta.Get_One_Pd("Battery Voltage", "Battery Age", ctb, network);
		str = cta.Get_One_Pd("Battery Voltage", "Charging System", ctb, network);
		str = cta.Get_One_Pd("Charging System", "Alternator", ctb, network);
		str = cta.Get_One_Pd("Voltage At Plug", "Distributor", ctb, network);
		
		
		
		str = cta.Get_One_Pd("Voltage At Plug", "Battery Voltage", ctb, network);	
		str = cta.Get_One_Pd2("Voltage At Plug", "Distributor", ctb, network);
		str = cta.Get_One_Pd2("Battery Voltage", "Battery Age", ctb, network);
		str = cta.Get_One_Pd2("Battery Voltage", "Charging System", ctb, network);
		str = cta.Get_One_Pd2("Charging System", "Alternator", ctb, network);
		
		
		str = cta.Get_One_Pd2("HeadLights", "Battery Voltage", ctb, network);	
		*/
		//str = cta.Get_One_Pd("HL", "HL", ctb, network);
		//str = cta.Get_One_Pd("VP", "DS", ctb, network);
		/*System.out.println();
		for(int j=0;j<str.length;j++)
		{
			for(int k=0;k<str[j].length;k++)
			{
				System.out.print(str[j][k] + "  ");
			}
			System.out.println();
		}
		
		System.out.println();
		System.out.println();
		
		/*CliqueTreeAlgorithm cta = new CliqueTreeAlgorithm();
		String str1[][],str2[][] = new String[3][2];
		node = network.getNode("CS");
		//str2 = node.getProbabilities();
		str2[0][0] = "CS"; str2[0][1] = "Probabilities";
		str2[1][0] = "okay"; str2[1][1] = "0";
		str2[2][0] = "faulty"; str2[2][1] = "1";
		//str2[3][0] = "dead"; str2[3][1] = "0.2";
		node = network.getNode("BV");
		str1 = node.getProbabilities();
		for(int j=0;j<str2.length;j++)
		{
			for(int k=0;k<str2[j].length;k++)
			{
				System.out.print(str2[j][k] + "  ");
			}
			System.out.println();
		}
		System.out.println();
		for(int j=0;j<str1.length;j++)
		{
			for(int k=0;k<str1[j].length;k++)
			{
				System.out.print(str1[j][k] + "  ");
			}
			System.out.println();
		}
		str1 = cta.Product_Marginalize(str1, str2,network);
		
		System.out.println();
		for(int j=0;j<str1.length;j++)
		{
			for(int k=0;k<str1[j].length;k++)
			{
				System.out.print(str1[j][k] + "  ");
			}
			System.out.println();
		}*/
		
		HashMap<String,node> l = ctb.getNodes();
		
		Set s = l.entrySet();
		Iterator it = s.iterator();
		
		while(it.hasNext())
		{
			Map.Entry<String, node> m = (Map.Entry) it.next();
			node n = m.getValue();	
			System.out.print(n.name + "  ");
			System.out.println();
			
			System.out.print("Associated Factors : ");
			List<Integer> itr = n.assoc_factors;
			for(int i=0;i<itr.size();i++)
				System.out.print(itr.get(i) + "  ");
			System.out.println();
			str = n.pd;
			for(int j=0;j<str.length;j++)
			{
				for(int k=0;k<str[j].length;k++)
				{
					System.out.print(str[j][k] + "  ");
				}
				System.out.println();
			}
			System.out.println();
		}
		System.out.println();
		System.out.println();
		List<factor> lf = ctb.getFactors();
		it = lf.iterator();
		while(it.hasNext())
		{
			factor f = (factor) it.next();		
			List<String> lstr = f.invol_nodes;
			System.out.print("Factor : ");
			for(int i=0;i<lstr.size();i++)
				System.out.print(lstr.get(i)+ "  ");	
			System.out.println();
			System.out.print("Self : " + f.self);
			System.out.println();
			str = f.probabilities;
			for(int j=0;j<str.length;j++)
			{
				for(int k=0;k<str[j].length;k++)
				{
					System.out.print(str[j][k] + "  ");
				}
				System.out.println();
			}
			System.out.println();
		}
		
		/*node = network.getNode("BA");
		str = node.getProbabilities();
		for(int i=0;i<str.length;i++)
		{
			for(int j=0;j<str[i].length;j++)
			{
				System.out.print(str[i][j] + "  ");
			}
			System.out.println();
		}
		
		/*node = network.getNode("BV");
		List<BayesNode> l = node.getParents();
		BayesNode bn;
		for(int i=0;i<l.size();i++)
		{
			bn = l.get(i);
			System.out.print(bn.getName() + "  ");
		}
		*/	
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//initialize_BN();
		//BayesNet network = new BayesNet();
		final varElim ex = new varElim();
		
		ex.network();
		 SwingUtilities.invokeLater(new Runnable() {

             public void run() {
            	 ex.setVisible(true);
             }
         });   
		 
	}
}
