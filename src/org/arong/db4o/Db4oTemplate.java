package org.arong.db4o;

import java.util.ArrayList;
import java.util.List;

import org.arong.egdownloader.model.Picture;
import org.arong.egdownloader.model.Task;
import org.arong.egdownloader.ui.ComponentConst;

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
	
	public static void main(String[] args) {
		//打开db4o级联
		EmbeddedConfiguration conf=Db4oEmbedded.newConfiguration();
		conf.common().objectClass("org.arong.egdownloader.model.Task").cascadeOnUpdate(true);
		conf.common().objectClass("org.arong.egdownloader.model.Task").cascadeOnDelete(true);
		List<Task> tasks = query(Task.class, ComponentConst.TASK_DATA_PATH);
		for (Task task : tasks) {
			System.out.println("id:" + task.getId());
			System.out.println("name:" + task.getName());
			System.out.println("saveDir:" + task.getSaveDir());
			System.out.println("total:" + task.getTotal());
			System.out.println("current:" + task.getCurrent());
			System.out.println("url:" + task.getUrl());
			System.out.println("status:" + task.getStatus().getStatus());
			System.out.println("---pictures:---");
			List<Picture> pics = task.getPictures();
			for (Picture pic : pics) {
				System.out.println("id:" + pic.getId());
				System.out.println("name:" + pic.getName());
				System.out.println("num:" + pic.getNum());
				System.out.println("url:" + pic.getUrl());
				System.out.println("realUrl:" + pic.getRealUrl());
				System.out.println("isCompleted:" + pic.isCompleted());
				System.out.println("");
			}
			System.out.println("");
		}
	}
	
	private static void cascadeClasses(EmbeddedConfiguration configuration,
			List<Class<?>> classList) {
		for (Class<?> c : classList) {
			configuration.common().objectClass(c).cascadeOnUpdate(true);
			configuration.common().objectClass(c).cascadeOnDelete(true);
		}
	}

	public static <T> List<T> queryByExample(T t, String db4o_file_path) {
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

	public static <T> List<T> query(Class<T> clazz, String db4o_file_path) {
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

	public static <T> List<T> query(Predicate<T> predicate, String db4o_file_path) {
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

	public static <T> boolean exists(Predicate<T> predicate, String db4o_file_path) {
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

	public static <T> void store(T t, String db4o_file_path) {
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
	public static <T> void cascadeStore(T t, List<Class<?>> cascadeClassList, String db4o_file_path) {
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

	public static <T> void delete(Class<T> clazz, String db4o_file_path) {
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

	public static <T> void delete(T t, String db4o_file_path) {
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

	public static <T> void delete(Predicate<T> predicate, String db4o_file_path) {
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

	public static <T> void cascadeDelete(T t, List<Class<?>> cascadeClassList, String db4o_file_path) {
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
			List<Class<?>> cascadeClassList, String db4o_file_path) {
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

	public static <T> void update(T example, T target, String db4o_file_path) {
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

	public static <T> void update(Predicate<T> predicate, T target, String db4o_file_path) {
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
			List<Class<?>> cascadeClassList, String db4o_file_path) {
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
			List<Class<?>> cascadeClassList, String db4o_file_path) {
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
