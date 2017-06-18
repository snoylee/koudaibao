package com.kdkj.koudailicai.util.config;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import android.util.Log;
import com.kdkj.koudailicai.util.ExceptionHelper;

/*
 * 配置文件管理类
 * @author railszhu
 */
public class ConfigManager {

	public static final int STATUS_INIT = 0;
	public static final int STATUS_READY = 1;
	public static final int STATUS_COMPLETE = 2;
	public static final int STATUS_FAILED = 3;
	
	public static final String LOG_TAG = "ConfigManager";
	
	private Map<Integer, Object> val_map;

	private int  status;
	private long version;
	private long lastCheckTime;

	private OnConfigStatusChangeListener listener = null;

	public ConfigManager() {
		this.val_map = new HashMap<Integer, Object>();
		this.status = STATUS_INIT;
		this.version = 0;
		this.lastCheckTime = 0;
	}

	public String getString(Integer key) {
		return (String) this.val_map.get(key);
	}

	public int getInt(Integer key) {
		if (this.val_map.get(key) instanceof Integer) {
			return (Integer) this.val_map.get(key);
		} else {
			try {
				return Integer.parseInt((String) this.val_map.get(key));
			} catch (NumberFormatException e) {
				Log.e(LOG_TAG, "String " + (String) this.val_map.get(key)
						+ " can not be convert to integer.");
				return 0;
			}
		}
	}

	public long getLong(Integer key) {
		if (this.val_map.get(key) instanceof Long) {
			return (Long) this.val_map.get(key);
		} else {
			try {
				return Long.parseLong((String) this.val_map.get(key));
			} catch (NumberFormatException e) {
				Log.e(LOG_TAG, "String " + this.val_map.get(key)
						+ " can not be convert to long.");
				return 0;
			}
		}
	}

	public double getDouble(Integer key) {
		if (this.val_map.get(key) instanceof Double) {
			return (Double) this.val_map.get(key);
		} else {
			try {
				return Double.parseDouble((String) this.val_map.get(key));
			} catch (NumberFormatException e) {
				Log.e(LOG_TAG, "String " + this.val_map.get(key)
						+ " can not be convert to double.");
				return 0;
			}
		}
	}

	public Boolean getBoolean(Integer key) {
		Object value = this.val_map.get(key);
		if (value instanceof Boolean) {
			return (Boolean) value;
		} else if (value instanceof Integer) {
			return (Integer) value != 0;
		} else {
			try {
				return Boolean.parseBoolean((String) value);
			} catch (Exception e) {
				return false;
			}
		}
	}

	public Object getObject(Integer key) {
		return this.val_map.get(key);
	}

	public int getStatus() {
		return this.status;
	}

	public synchronized void setStatus(int status) {
		int oldStatus = this.status;
		this.status = status;
		if (this.listener != null) {
			this.listener.onChange(this, oldStatus, status);
		}
	}

	public boolean isReady() {
		return this.status == STATUS_READY || this.isComplete();
	}

	public boolean isComplete() {
		return this.status == STATUS_COMPLETE;
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

	public long getLastCheckTime() {
		return lastCheckTime;
	}

	public void setLastCheckTime(long lastCheckTime) {
		this.lastCheckTime = lastCheckTime;
	}

	public void setOnConfigStatusChangeListener(OnConfigStatusChangeListener listener) {
		this.listener = listener;
	}

	public void putValue(Integer key, Object value) {
		this.val_map.put(key, value);
	}

	//保存配置文件
	public boolean save(String filename) {
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		try {
			fos = new FileOutputStream(filename);
			oos = new ObjectOutputStream(fos);
			oos.writeLong(this.version);
			oos.writeObject(this.val_map);
			return true;
		} catch (Exception e) {
			Log.e(LOG_TAG, ExceptionHelper.stackTraceToString(e));
			return false;
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (Exception e) {
					Log.e(LOG_TAG, ExceptionHelper.stackTraceToString(e));
				}
			}

			if (oos != null) {
				try {
					oos.close();
				} catch (Exception e) {
					Log.e(LOG_TAG, ExceptionHelper.stackTraceToString(e));
				}
			}
		}
	}
	
	//读取配置文件
	@SuppressWarnings("unchecked")
	public boolean restore(String filename) {
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		try {
			fis = new FileInputStream(filename);
			ois = new ObjectInputStream(fis);
			this.version = (long) ois.readLong();
			this.val_map = (Map<Integer, Object>) ois.readObject();
			return true;
		} catch (Exception e) {
			Log.e(LOG_TAG, ExceptionHelper.stackTraceToString(e));
			return false;
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (Exception e) {
					Log.e(LOG_TAG, ExceptionHelper.stackTraceToString(e));
				}
			}

			if (ois != null) {
				try {
					ois.close();
				} catch (Exception e) {
					Log.e(LOG_TAG, ExceptionHelper.stackTraceToString(e));
				}
			}
		}
	}
}
