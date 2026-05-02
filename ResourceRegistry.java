import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ResourceRegistry<T> {

    private final List<T> items = new ArrayList<>();

    public void add(T item) {
        items.add(item);
    }

    public void addAll(List<T> list) {
        items.addAll(list);
    }

    public List<T> getAll() {
        return Collections.unmodifiableList(items);
    }

    public int size() {
        return items.size();
    }

    public T get(int index) {
        return items.get(index);
    }
}
