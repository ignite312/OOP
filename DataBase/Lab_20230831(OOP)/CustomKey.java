// Md. Emon Khan
// Roll : 30
public class CustomKey {
    private final Integer first;
    private final String second;

    public CustomKey(Integer first, String second) {
        this.first = first;
        this.second = second;
    }
    public int getFirst() {
        return first;
    }

    public String getSecond() {
        return second;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomKey customKey = (CustomKey) o;
        return first.equals(customKey.first) &&
                second.equals(customKey.second);
    }

    @Override
    public int hashCode() {
        int result = first.hashCode();
        result = 31 * result + second.hashCode();
        return result;
    }
}
