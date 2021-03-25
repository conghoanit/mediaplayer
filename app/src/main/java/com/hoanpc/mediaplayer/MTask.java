package com.hoanpc.mediaplayer;

import android.os.AsyncTask;

public final class MTask extends AsyncTask<Object, Object, Object> {
    private final String key;
    private final OnMTaskCallBack callBack;

    public MTask(String key, OnMTaskCallBack callBack) {
        this.key = key;
        this.callBack = callBack;
    }

    @Override
    protected Object doInBackground(Object... data) {
        return callBack.executeTask(this, key, data[0]);
    }

    public final void requestUI(Object data) {
        publishProgress(data);
    }

    @Override
    protected void onProgressUpdate(Object... values) {
        callBack.updateUI(this, key, values[0]);
    }

    @Override
    protected void onPostExecute(Object value) {
        callBack.completeTask(this, key, value);
    }

    public final void start(Object data) {
        execute(data);
    }

    public final void startAsync(Object data) {
        executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, data);
    }

    public final void stop() {
        cancel(true);
    }

    public interface OnMTaskCallBack {
        Object executeTask(MTask task, String key, Object data);

        default void updateUI(MTask task, String key, Object data) {
            //do nothing
        }

        default void completeTask(MTask task, String key, Object data) {
            //do nothing
        }
    }
}
