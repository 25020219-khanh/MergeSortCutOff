import java.util.Random;

public final class DataGenerator {
    private DataGenerator() {
    }

    public static int[] create(String type, int size, long seed) {
        int[] values = new int[size];
        Random random = new Random(seed);

        switch (type) {
            case "random":
                for (int i = 0; i < size; i++)
                    values[i] = random.nextInt();
                break;
            case "sorted":
                for (int i = 0; i < size; i++)
                    values[i] = i;
                break;
            case "reverse":
                for (int i = 0; i < size; i++)
                    values[i] = size - i;
                break;
            case "duplicates":
                for (int i = 0; i < size; i++)
                    values[i] = random.nextInt(10);
                break;
            default:
                throw new IllegalArgumentException("Loại dữ liệu không hỗ trợ: " + type);
        }
        return values;
    }
}
