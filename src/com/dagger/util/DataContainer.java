package com.dagger.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.example.dagger.BindingClass;

public class DataContainer {

	private static DataContainer instance;
	private HashMap<String, BindingClass> bindclassMap = new LinkedHashMap<String, BindingClass>();

	public static DataContainer Instance() {
		if (instance == null) {
			instance = new DataContainer();
		}
		return instance;
	}

	public HashMap<String, BindingClass> getBindclassMap() {
		return bindclassMap;
	}

	public void setBindclassMap(HashMap<String, BindingClass> bindclassMap) {

		this.bindclassMap = bindclassMap;
	}

	public  BindingClass getBindingClass(Class bindclass) {
		String className = bindclass.getSimpleName();
		if (bindclassMap.containsKey(className)) {
			return bindclassMap.get(className);
		}
		return null;
	}

	public void writeDataToCache() {
		// for(Map.Entry<String, BindingClass> value:bindclassMap.entrySet()){
		// BindingClass bind=value.getValue();
		// bind.setIdMaps(null);
		// bind.setForignColumnBinding(null);
		// }

		File dir = new File("C://dbCache");
		if (!dir.exists()) {
			dir.mkdir();
		}
		File config = new File("C://dbCache", "config.txt");
		try {
			if (!config.exists()) {
				config.createNewFile();
			}
			ObjectOutputStream oo = new ObjectOutputStream(
					new FileOutputStream(config));
			oo.writeObject(bindclassMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
