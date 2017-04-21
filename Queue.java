

/**
 * @author denisavataksi
 * @param <T>
 * Generic Queue: First in First Out
 */
public interface Queue<T> {
	void enqueue(T data);
	T dequeue();
}
