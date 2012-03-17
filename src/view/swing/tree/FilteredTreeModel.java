package view.swing.tree;

import java.util.Enumeration;

import javax.swing.tree.DefaultTreeModel;

public class FilteredTreeModel extends DefaultTreeModel
{

	public FilteredTreeModel(FilterableTreeNode _root)
	{
		super(_root);
	}

	public Object getChild(Object parent, int index)
	{

		FilterableTreeNode node = (FilterableTreeNode) parent;
		int pos = 0;
		for (int i = 0, cnt = 0; i < node.getChildCount(); i++)
		{
			if (((FilterableTreeNode) node.getChildAt(i)).filterAllows())
			{
				if (cnt++ == index)
				{
					pos = i;
					break;
				}
			}
		}
		return node.getChildAt(pos);
	}

	public int getChildCount(Object parent)
	{

		FilterableTreeNode node = (FilterableTreeNode) parent;
		int childCount = 0;
		Enumeration children = node.children();
		while (children.hasMoreElements())
		{
			if (((FilterableTreeNode) children.nextElement()).filterAllows())
			{
				childCount++;
			}

		}
		return childCount;

	}
}
