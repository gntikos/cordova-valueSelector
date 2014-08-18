package gntikos.plugin.valueselector;

import me.init.demo.R;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class ValueSelector extends CordovaPlugin  {

	// ---------- Variables ----------
	public String dialogTitle;
	private final String DEFAULT_TITLE = "Select Value";
	
	// ---------- CordovaPlugin overrides ----------
	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException  {
		try {
			dialogTitle = args.getString(0);
		} catch (JSONException e) {
			dialogTitle = DEFAULT_TITLE;
		}

		if (action.equals("openRange")) {
			this.selectFromRange(args.getInt(1), args.getJSONObject(2), callbackContext);
			return true;
		} 
		else if (action.equals("openList")) {
			JSONArray list = args.getJSONArray(1);
			CharSequence[] stringList = new CharSequence[list.length()];
			
			for (int i=0; i<list.length(); i++)
				stringList[i] = list.getString(i);
			
			this.selectFromList(stringList, args.getInt(2), callbackContext);
			return true;
		}

		return false;
	}
	

	// * ================================ [ Private Methods ] ================================ *
	
	private synchronized void selectFromRange(int initVal, final JSONObject params, final CallbackContext callbackContext) throws JSONException {
		final CordovaInterface cordova  = this.cordova;
		
		final View view = this.cordova.getActivity().getLayoutInflater().inflate(R.layout.selector,  null);
		final SeekBar bar = (SeekBar) view.findViewById(R.id.theSeekBar);
		final TextView valueText = (TextView) view.findViewById(R.id.valueText);

		final int step = params.getInt("step");
		
		final int minValue = params.getInt("min");
		final int maxValue = (params.getInt("max") - minValue);
		final int init = (initVal*step) / step;
		
		bar.setProgress((init - minValue)/step);
		bar.setMax((maxValue - minValue)/step + 1);
		
		valueText.setText("Value: " + Integer.toString( (init*step) / step ));			

		OnSeekBarChangeListener barListener = new OnSeekBarChangeListener() {
			
			@Override
			public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
				valueText.setText("Value: " + Integer.toString(arg1*step + minValue));
			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {}
			@Override
			public void onStopTrackingTouch(SeekBar arg0) {}
		};
		
		bar.setOnSeekBarChangeListener(barListener);
		
		Runnable runnable = new Runnable() {
			
			@Override
			public void run() {
				new AlertDialog.Builder(cordova.getActivity())
					.setTitle(dialogTitle)
					.setView(view)
					.setPositiveButton("Apply", new AlertDialog.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							callbackContext.success(minValue + bar.getProgress()*step);
						}
					})
					.setNegativeButton("Cancel", new AlertDialog.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							callbackContext.error("No value selected");
						}
					})
					.create()
					.show();
			}
		};
		
		this.cordova.getActivity().runOnUiThread(runnable);
	}
	
	private synchronized void selectFromList(final CharSequence[] items, final int currentChoice, final CallbackContext callbackContext) {

		final CordovaInterface cordova = this.cordova;
		
		Runnable runnable = new Runnable() {

			public void run() {
				AlertDialog.Builder dlg = new AlertDialog.Builder(cordova.getActivity());
				dlg.setTitle(dialogTitle);
				dlg.setSingleChoiceItems(items, currentChoice, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						callbackContext.success(which);
					}
				});
				
				dlg.setNegativeButton("Cancel",  new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						callbackContext.error("No value selected");
					}
				});
					
				dlg.create().show();
			}	
		};
		
		this.cordova.getActivity().runOnUiThread(runnable);
	}
}
