package com.kdkj.koudailicai.util.resource;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.kdkj.koudailicai.util.ExceptionHelper;

import android.util.Log;

public class ResourceDownloader {

	private DownloadThread download_thread;
	
	private OnDownloadFinishedListener finished_listener = null;

	public ResourceDownloader() {
		this.download_thread = new DownloadThread();
		this.download_thread.start();
	}

	public void addTask(RemoteResource file) {
		this.download_thread.download_queue.offer(file);
	}

	public void exit() {
		this.download_thread.exit_on_empty = true;
	}

	public void waitComplete() {
		this.download_thread.exit_on_empty = true;
		try {
			this.download_thread.join();
		} catch (InterruptedException e) {

		}
		return;
	}

	public void setOnDownloadFinishedListener(OnDownloadFinishedListener finished_listener) {
		this.finished_listener = finished_listener;
	}

	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		this.exit();
		super.finalize();
	}

	private class DownloadThread extends Thread {
		
		private static final int CONNECT_TIME_OUT = 1000;
		private static final int READ_TIME_OUT = 3000;

		private ConcurrentLinkedQueue<RemoteResource> download_queue;
		private boolean exit_on_empty;
		private String LOG_TAG = "DownLoadThread";

		public DownloadThread() {
			this.download_queue = new ConcurrentLinkedQueue<RemoteResource>();
			this.exit_on_empty = false;
		}

		@Override
		public void run() {
			Log.d(LOG_TAG, "DownloadThread run");
			// TODO Auto-generated method stub
			while (!this.exit_on_empty || !this.download_queue.isEmpty()) {
				RemoteResource file = this.download_queue.poll();
				if (file != null) {
					if (!this.download(file)) {
						Log.w(LOG_TAG,"Failed to download file " + file.getPath()+ " with url " + file.getUrl());
					}
				} else {
					try {
						sleep(100);
					} catch (InterruptedException e) {
					}
				}
				Log.d(LOG_TAG, "DownloadThread Loop");
			}
			
			if(finished_listener != null) {
				Log.d(LOG_TAG, "Emit on finished listener.");
				finished_listener.onFinished();
			} else {
				Log.d(LOG_TAG, "Empty finished listener.");
			}
		}
		
		public boolean download(RemoteResource resource) {
			try {
				URL res = new URL(resource.getUrl());
				URLConnection connection = res.openConnection();
				connection.setConnectTimeout(CONNECT_TIME_OUT);
				connection.setReadTimeout(READ_TIME_OUT);
				InputStream is = connection.getInputStream();
				OutputStream os = new FileOutputStream(resource.getPath());//context.openFileOutput(file.getPath(), Context.MODE_PRIVATE);
				byte[] buff = new byte[1024];
				int hasRead = 0;
				while ((hasRead = is.read(buff)) > 0) {
					os.write(buff, 0, hasRead);
				}
				is.close();
				os.close();
				resource.setStatus(RemoteResource.STATUS_READY);
				Log.d(LOG_TAG, "File " + resource.getPath() + " download complete.");
				return true;
			} catch (Exception e) {
				resource.setStatus(RemoteResource.STATUS_FAILED);
				Log.w(LOG_TAG, "Failed to download file " + resource.getPath() + " with url " + resource.getUrl() + ", error: " + e.getMessage());
				Log.w(LOG_TAG , ExceptionHelper.stackTraceToString(e));
				return false;
			}
		}
	}
}
