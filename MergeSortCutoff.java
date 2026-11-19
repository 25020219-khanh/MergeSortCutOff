import java.util.Arrays;

public class MergeSortCutoff {

    private static void insertionSort(int[] a, int left, int right) {
        for (int i = left + 1; i <= right; i++) {
            int key = a[i];
            int j = i - 1;

            while (j >= left && a[j] > key) {
                a[j + 1] = a[j];
                j--;
            }
            a[j + 1] = key;
        }
    }

    private static void merge(int[] a, int[] aux, int left, int mid, int right) {
        for (int i = left; i <= right; i++) {
            aux[i] = a[i];
        }

        int i = left;
        int j = mid + 1;

        for (int k = left; k <= right; k++) {
            if (i > mid) {
                a[k] = aux[j++];
            } else if (j > right) {
                a[k] = aux[i++];
            } else if (aux[i] <= aux[j]) {
                a[k] = aux[i++];
            } else {
                a[k] = aux[j++];
            }
        }
    }

    private static void mergeSort(
            int[] a, int[] aux, int left, int right, int cutoff) {

        if (right <= left) {
            return;
        }

        int length = right - left + 1;

        if (length <= cutoff) {
            insertionSort(a, left, right);
            return;
        }

        int mid = left + (right - left) / 2;

        mergeSort(a, aux, left, mid, cutoff);
        mergeSort(a, aux, mid + 1, right, cutoff);
        merge(a, aux, left, mid, right);
    }

    public static void sort(int[] a, int cutoff) {
        if (a == null || a.length < 2) {
            return;
        }
        if (cutoff < 1) {
            throw new IllegalArgumentException("cutoff phải >= 1");
        }

        int[] aux = new int[a.length];
        mergeSort(a, aux, 0, a.length - 1, cutoff);
    }

    public static void main(String[] args) {
        int[] values = { 8, 3, 5, 1, 9, 2, 7, 4, 6 };
        int cutoff = 4;

        sort(values, cutoff);

        System.out.println(Arrays.toString(values));
    }
}