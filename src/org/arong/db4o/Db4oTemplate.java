package org.arong.db4o;

import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.query.Predicate;
/**
 * db4o操作类
 * @author 阿荣
 * @since 2014-05-23
 */
public final class Db4oTemplate {
	private static String db4o_file_path = getClassPath() + "db/task.data";
	
	public static void setDb4o_file_path(String db4oFilePath) {
		db4o_file_path = getClassPath() + db4oFilePath;
	}
	private static String getClassPath() {
		URL url = Db4oTemplate.class.getResource("/");
		String path = null;
		try {
			path = URLDecoder.decode(url.getPath(), "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}

		return path;
	}

	private static void cascadeClasses(EmbeddedConfiguration configuration,
			List<Class<?>> classList) {
		for (Class<?> c : classList) {
			configuration.common().objectClass(c).cascadeOnUpdate(true);
			configuration.common().objectClass(c).cascadeOnDelete(true);
		}
	}

	public static <T> List<T> queryByExample(T t) {
		List<T> result = new ArrayList<T>();
		ObjectContainer db = null;
		try {
			db = Db4oEmbedded.openFile(db4o_file_path);
			ObjectSet<T> oSet = db.queryByExample(t);
			while (oSet.hasNext()) {
				result.add(oSet.next());
			}
			return result;
		} finally {
			if (db != null)
				db.close();
		}
	}

	public static <T> List<T> query(Class<T> clazz) {
		List<T> result = new ArrayList<T>();
		ObjectContainer db = null;
		try {
			db = Db4oEmbedded.openFile(db4o_file_path);
			ObjectSet<T> oSet = db.query(clazz);
			while (oSet.hasNext()) {
				result.add(oSet.next());
			}
			return result;
		} finally {
			if (db != null)
				db.close();
		}
	}

	public static <T> List<T> query(Predicate<T> predicate) {
		List<T> result = new ArrayList<T>();
		ObjectContainer db = null;
		try {
			db = Db4oEmbedded.openFile(db4o_file_path);
			ObjectSet<T> oSet = db.query(predicate);
			while (oSet.hasNext()) {
				result.add(oSet.next());
			}
			return result;
		} finally {
			if (db != null)
				db.close();
		}
	}

	public static <T> boolean exists(Predicate<T> predicate) {
		ObjectContainer db = null;
		try {
			db = Db4oEmbedded.openFile(db4o_file_path);
			ObjectSet<T> oSet = db.query(predicate);
			if (oSet.hasNext()) {
				return true;
			}
		} finally {
			if (db != null)
				db.close();
		}

		return false;
	}

	public static <T> void store(T t) {
		ObjectContainer db = null;
		try {
			db = Db4oEmbedded.openFile(db4o_file_path);
			db.store(t);
		} finally {
			if (db != null)
				db.close();
		}
	}

	/**
	 * @deprecated the same with store() above
	 */
	public static <T> void cascadeStore(T t, List<Class<?>> cascadeClassList) {
		EmbeddedConfiguration configuration = Db4oEmbedded.newConfiguration();
		cascadeClasses(configuration, cascadeClassList);

		ObjectContainer db = null;
		try {
			db = Db4oEmbedded.openFile(configuration, db4o_file_path);
			db.store(t);
		} finally {
			if (db != null)
				db.close();
		}
	}

	public static <T> void delete(Class<T> clazz) {
		ObjectContainer db = null;
		try {
			db = Db4oEmbedded.openFile(db4o_file_path);
			ObjectSet<T> oSet = db.query(clazz);
			while (oSet.hasNext()) {
				db.delete(oSet.next());
			}
		} finally {
			if (db != null)
				db.close();
		}
	}

	public static <T> void delete(T t) {
		ObjectContainer db = null;
		try {
			db = Db4oEmbedded.openFile(db4o_file_path);
			ObjectSet<T> targetSet = db.queryByExample(t);
			while (targetSet.hasNext()) {
				db.delete(targetSet.next());
			}
		} finally {
			if (db != null)
				db.close();
		}
	}

	public static <T> void delete(Predicate<T> predicate) {
		ObjectContainer db = null;
		try {
			db = Db4oEmbedded.openFile(db4o_file_path);
			ObjectSet<T> targetSet = db.query(predicate);
			while (targetSet.hasNext()) {
				db.delete(targetSet.next());
			}
			db.commit();
		} finally {
			if (db != null)
				db.close();
		}
	}

	public static <T> void cascadeDelete(T t, List<Class<?>> cascadeClassList) {
		EmbeddedConfiguration configuration = Db4oEmbedded.newConfiguration();
		cascadeClasses(configuration, cascadeClassList);

		ObjectContainer db = null;
		try {
			db = Db4oEmbedded.openFile(configuration, db4o_file_path);
			ObjectSet<T> targetSet = db.queryByExample(t);
			while (targetSet.hasNext()) {
				db.delete(targetSet.next());
			}
			
		} finally {
			if (db != null)
				db.close();
		}
	}

	public static <T> void cascadeDelete(Predicate<T> predicate,
			List<Class<?>> cascadeClassList) {
		EmbeddedConfiguration configuration = Db4oEmbedded.newConfiguration();
		cascadeClasses(configuration, cascadeClassList);

		ObjectContainer db = null;
		try {
			db = Db4oEmbedded.openFile(configuration, db4o_file_path);
			ObjectSet<T> targetSet = db.query(predicate);
			while (targetSet.hasNext()) {
				db.delete(targetSet.next());
			}
		} finally {
			if (db != null)
				db.close();
		}
	}

	public static <T> void update(T example, T target) {
		ObjectContainer db = null;
		try {
			db = Db4oEmbedded.openFile(db4o_file_path);
			ObjectSet<T> targetSet = db.queryByExample(example);
			while (targetSet.hasNext()) {
				db.delete(targetSet.next());
				db.store(target);
			}
		} finally {
			if (db != null)
				db.close();
		}
	}

	public static <T> void update(Predicate<T> predicate, T target) {
		ObjectContainer db = null;
		try {
			db = Db4oEmbedded.openFile(db4o_file_path);
			ObjectSet<T> targetSet = db.query(predicate);
			while (targetSet.hasNext()) {
				db.delete(targetSet.next());
				db.store(target);
			}
		} finally {
			if (db != null)
				db.close();
		}
	}

	public static <T> void cascadeUpdate(T example, T target,
			List<Class<?>> cascadeClassList) {
		EmbeddedConfiguration configuration = Db4oEmbedded.newConfiguration();
		cascadeClasses(configuration, cascadeClassList);

		ObjectContainer db = null;
		try {
			db = Db4oEmbedded.openFile(configuration, db4o_file_path);
			ObjectSet<T> targetSet = db.queryByExample(example);
			while (targetSet.hasNext()) {
				db.delete(targetSet.next());
				db.store(target);
			}
		} finally {
			if (db != null)
				db.close();
		}
	}

	public static <T> void cascadeUpdate(Predicate<T> predicate, T target,
			List<Class<?>> cascadeClassList) {
		EmbeddedConfiguration configuration = Db4oEmbedded.newConfiguration();
		cascadeClasses(configuration, cascadeClassList);

		ObjectContainer db = null;
		try {
			db = Db4oEmbedded.openFile(configuration, db4o_file_path);
			ObjectSet<T> targetSet = db.query(predicate);
			while (targetSet.hasNext()) {
				db.delete(targetSet.next());
				db.store(target);
			}
		} finally {
			if (db != null)
				db.close();
		}
	}
}
