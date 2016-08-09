package AML;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class BayesNet {
	

	private List<BayesNode> nodes = new ArrayList<BayesNode>();
    private Map<String, BayesNode> nodeMap = new HashMap<String, BayesNode>();

    private String name = "Car Failure Diagnosis";
    
    BayesNode addNode(String name)
    {
    	BayesNode newNode = new BayesNode(name);
    	newNode.setId(nodes.size());
    	nodes.add(newNode);
    	nodeMap.put(name, newNode);
    	return newNode;
    }
  
    public BayesNode getNode(String name) {    	
        return nodeMap.get(name);
    }

    public BayesNode getNode(int id) {
        return nodes.get(id);
    }
    
    public List<BayesNode> getNodes() {
        return Collections.unmodifiableList(nodes);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
