package view.swing.tree;

import java.util.Map;
import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;
import model.HasRace;

public class FilterableTreeNode extends DefaultMutableTreeNode
{
	protected Vector<FilterableTreeNode> children;
	protected Map<Integer, Boolean> races;

	public FilterableTreeNode(Object userObj)
	{
		super(userObj);
	}

	public FilterableTreeNode(Object userObj, boolean _allowsChildren)
	{
		super(userObj, _allowsChildren);
	}

	public void setRaceFilter(Map<Integer, Boolean> _races)
	{
		this.races = _races;
		if (this.allowsChildren)
			setChildRaceFilters();
	}

	private void setChildRaceFilters()
	{
		for (FilterableTreeNode f : this.children) {
			f.setRaceFilter(this.races);
		}
	}

	public boolean filterAllows()
	{
		if (this.userObject instanceof HasRace)
		{
			HasRace hr = (HasRace) this.userObject;
			if (this.races != null)
				return this.races.get(hr.getRace());
		}
		return true;
	}

}
