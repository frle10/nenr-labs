package hr.fer.zemris.nenr.neuralnetwork;

import java.util.List;

public class Group {

	public List<Matrix> groups;
	
	public List<Matrix> groupLabels;

	public Group(List<Matrix> groups, List<Matrix> groupLabels) {
		this.groups = groups;
		this.groupLabels = groupLabels;
	}
	
}
