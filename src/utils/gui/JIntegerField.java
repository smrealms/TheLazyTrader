package utils.swing;

import java.awt.Toolkit;

import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

public class JIntegerField extends JTextField
{

	long maxValue = Long.MAX_VALUE;
	long minValue = 0;
	int maxLength = String.valueOf(maxValue).length();

	/**
	 * Default constructor for JIntegerField.
	 */
	public JIntegerField()
	{
		super();
	}

	public JIntegerField(int startValue)
	{
		super();
		this.setText(Integer.toString(startValue));
	}

	protected Document createDefaultModel()
	{
		return new IntegerDocument();
	}

	public void setMinValue(long value)
	{
		minValue = value;
	}

	public long getMinValue()
	{
		return minValue;
	}

	public void setMaxValue(long value)
	{
		maxValue = value;
	}

	public long getMaxValue()
	{
		return maxValue;
	}

	public void setMaxLength(int value)
	{
		maxLength = value;
	}

	public long getMaxLength()
	{
		return maxLength;
	}

	public long getValue()
	{
		return Long.parseLong(this.getText());
	}

	public boolean hasValue()
	{
		return this.getText().length()>0;
	}

	public void setValue(int value)
	{
		this.setText(Integer.toString(value));
	}

	class IntegerDocument extends PlainDocument
	{
		public void insertString(int offs, String str, AttributeSet a) throws BadLocationException
		{
			long typedValue = -1;

			StringBuffer textBuffer = new StringBuffer(JIntegerField.this.getText().trim());
			// The offset argument must be greater than or equal to 0, and less
			// than or equal to the length of this string buffer
			if ((offs >= 0) && (offs <= textBuffer.length()))
			{
				textBuffer.insert(offs, str);
				String textValue = textBuffer.toString();
				if (textBuffer.length() > maxLength)
				{
					JOptionPane.showMessageDialog(JIntegerField.this, "The number of characters must be less than or equal to " + getMaxLength(), "Error Message", JOptionPane.ERROR_MESSAGE);
					return;
				}

				if ((textValue == null) || (textValue.equals("")))
				{
					remove(0, getLength());
					super.insertString(0, "", null);
					return;
				}

				if (textValue.equals("-") && minValue < 0)
				{
					super.insertString(offs, new String(str), a);
					return;
				}

				try
				{
					typedValue = Long.parseLong(textValue);
					if ((typedValue > maxValue) || (typedValue < minValue))
					{
						JOptionPane.showMessageDialog(JIntegerField.this, "The value can only be from " + getMinValue() + " to " + getMaxValue(), "Error Message", JOptionPane.ERROR_MESSAGE);
					}
					else
					{
						super.insertString(offs, new String(str), a);
					}
				}
				catch (final NumberFormatException ex)
				{
					Toolkit.getDefaultToolkit().beep();
					JOptionPane.showMessageDialog(JIntegerField.this, "Only numeric values allowed.", "Error Message", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}
}