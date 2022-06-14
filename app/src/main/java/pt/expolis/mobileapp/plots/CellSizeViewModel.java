package pt.expolis.mobileapp.plots;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import pt.expolis.mobileapp.R;

/**
 * Interface used by all view model that have a cell radius or size property.
 * Units are either meters or radians.
 * This property is used when drawing a rectangular grid on top of a map.
 * It controls the size of each cell in the grid.
 */
public interface CellSizeViewModel
{
	/**
	 * Gets the size of cell.
	 * Units are <em>meters</em>.
	 * @return the size of cell.
	 */
	float getCellRadius ();

	/**
	 * Sets the size of the cell.
	 * Units are <em>meters</em>.
	 * @param value the new size of cell.
	 */
	void setCellRadius (float value);

	class Setup
	{
		public static void setupCellSize (View view, CellSizeViewModel model)
		{
			EditText editText = view.findViewById (R.id.cellSizeEditText);
			editText.setText (String.valueOf (model.getCellRadius ()));
			editText.addTextChangedListener (new TextWatcher ()
			{
				@Override
				public void beforeTextChanged (CharSequence charSequence, int i, int i1, int i2)
				{

				}

				@Override
				public void onTextChanged (CharSequence charSequence, int i, int i1, int i2)
				{

				}

				@Override
				public void afterTextChanged (Editable editable)
				{
					String text = editText.getText ().toString ();
					if (!text.isEmpty ()) {
						try {
							model.setCellRadius (Float.parseFloat (text));
						}
						catch (NumberFormatException ignored) {
						}
					}
				}
			});
		}
	}
}
