package yiranlihao.learn.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.springframework.beans.BeanUtils;

public class CopyVector<S, T> {
	private Class<T> targetType;

	CopyVector(Class<T> targetType) {
		this.targetType = targetType;
	}

	Vector<T> copy(Vector<S> src) {
		Vector<T> target = new Vector<T>();
		for (S s : src) {
			T t = BeanUtils.instantiateClass(targetType);
			BeanUtils.copyProperties(s, t);
			target.add(t);
		}
		return target;
	}
}
