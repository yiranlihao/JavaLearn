package yiranlihao.learn.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

/**
 * 实现List的Bean转换
 * @author Lihao
 *
 * @param <S> 源List
 * @param <T> 目标List
 */
public class CopyList<S,T>{
	
	//目标List的实体类型
	private Class<T> targetType;

	/**
	 * 构造方法
	 * @param targetType
	 */
	CopyList(Class<T> targetType) {
		this.targetType = targetType;
	}

	/**
	 * copy方法实现
	 * @param src
	 * @return
	 */
	List<T> copy(List<S> src) {
		List<T> target = new ArrayList<T>();
		for (S s : src) {
			T t = BeanUtils.instantiateClass(targetType);
			BeanUtils.copyProperties(s, t);
			target.add(t);
		}
		return target;
	}
}