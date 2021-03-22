package com.roymark.queue.util;

import java.util.List;
import java.util.Map;

/**
 * 判断是否为空统一工具类
 * @author benjamin
 *
 */
public class RegulationUtil {
	public static boolean isEmpty(String s)
	  {
	    if ((s == null) || ("".equals(s.trim()))) {
	      return true;
	    }
	    return false;
	  }
	  
	  public static boolean isEmpty(Object o)
	  {
	    if ((o instanceof String)) {
	      return isEmpty((String)o);
	    }
	    return o == null;
	  }
	  
	  public static <E> boolean isEmpty(List<E> l)
	  {
	    if ((l == null) || (l.size() <= 0)) {
	      return true;
	    }
	    return false;
	  }
	  
	  public static <E, V, K> boolean isEmpty(Map<K, V> l)
	  {
	    if ((l == null) || (l.size() <= 0)) {
	      return true;
	    }
	    return false;
	  }
	  public static <E> boolean isEmpty(byte[] l)
	  {
		  if((l == null) || (l.length <= 0)) {
			  return true;
		  }
		  return false;
	  }
}
