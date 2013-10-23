package com.hiputto.common4android.util;

import java.util.HashMap;

import com.hiputto.common4android.util.HP_AsyncTaskUtils.AsyncTaskSteps;

import android.os.AsyncTask;

public class HP_NetWorkAsyncTask extends
		AsyncTask<String, Integer, HashMap<String, Object>> {
	private AsyncTaskSteps asyncTaskSteps;

	public HP_NetWorkAsyncTask(AsyncTaskSteps asyncTaskSteps) {
		this.asyncTaskSteps = asyncTaskSteps;
	}

	@Override
	protected void onPreExecute() {
		asyncTaskSteps.onPreExecute();
	}

	@Override
	protected HashMap<String, Object> doInBackground(String... params) {
		return asyncTaskSteps.doInBackground(params);
	}

	@Override
	protected void onPostExecute(HashMap<String, Object> result) {
		asyncTaskSteps.onPostExecute(result);
	}

	@Override
	protected void onCancelled() {
		asyncTaskSteps.onCancelled();
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		asyncTaskSteps.onProgressUpdate(values);
	}

}
