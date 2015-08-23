package sk.kasper.nottoday.models;

/**
 * Created by Valter on 01.08.2015.
 */
public class Item {
	public String name = "";
	public Long id = 0L;
	public String description = "";

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Item item = (Item) o;

		return id.equals(item.id);

	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public String toString() {
		return "Item{" +
				"name='" + name + '\'' +
				", id=" + id +
				", description='" + description + '\'' +
				'}';
	}
}
