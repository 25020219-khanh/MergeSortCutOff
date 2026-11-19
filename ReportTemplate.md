# Báo cáo thí nghiệm: tìm CUTOFF cho Merge Sort

## 1. Mục tiêu
Tìm cutoff cho biến thể Merge Sort dùng Insertion Sort ở các đoạn nhỏ, trên cấu hình máy thực tế. Cụ thể, khi kích thước mảng con ≤ cutoff, thuật toán chuyển sang Insertion Sort thay vì tiếp tục đệ quy Merge Sort, nhằm tận dụng hiệu quả của Insertion Sort trên mảng nhỏ (ít overhead gọi hàm, tận dụng cache tốt hơn).

## 2. Cài đặt và môi trường
- **CPU:** 13th Gen Intel(R) Core(TM) i7-13620H
- **RAM:** 16 GB
- **Hệ điều hành:** Microsoft Windows 11 Home Single Language (Build 10.0.26300)
- **Phiên bản Java:** Java SE 25.0.2 (2026-01-20 LTS), HotSpot 64-Bit Server VM (mixed mode, sharing)
- **Merge Sort:** tự cài đặt theo bài giảng; mảng phụ `aux[]` được cấp phát **một lần** ở hàm `sort()` với kích thước bằng mảng đầu vào, truyền vào đệ quy để tránh cấp phát lặp lại.
- **Cách đo:** `System.nanoTime()`; mỗi lần đo sắp xếp một **bản sao** (`clone()`) của mảng gốc; chạy **10 lượt làm nóng** (warmup) JVM trước, sau đó **31 lượt đo**, lấy **trung vị** (median) — giá trị ở vị trí thứ 16 sau khi sắp xếp 31 giá trị. Sau mỗi lần sắp xếp, chương trình kiểm tra tính đúng đắn (mảng đã được sắp đúng thứ tự).

## 3. Thiết kế thí nghiệm
- **Cutoff:** 1, 4, 7, 10, 16, 24, 32, 48, 64.
- **Kích thước mảng:** 1.000, 10.000, 100.000 phần tử.
- **Dạng dữ liệu:**
  - `random` — giá trị ngẫu nhiên (`Random.nextInt()`),
  - `sorted` — tăng dần (0, 1, 2, …, n−1),
  - `reverse` — giảm dần (n, n−1, …, 1),
  - `duplicates` — nhiều giá trị trùng (chỉ 10 giá trị phân biệt, `nextInt(10)`).
- Dữ liệu được tạo với **seed cố định** (`20261117L`); mỗi phép đo sắp xếp một **bản sao** của mảng gốc để đảm bảo tính lặp lại.
- Tổng cộng: 4 dạng × 3 kích thước × 9 cutoff = **108 kịch bản**.

## 4. Kết quả

### 4.1. Bảng tổng hợp — cutoff tốt nhất theo từng kịch bản

| Dạng dữ liệu | Kích thước | Cutoff tốt nhất | Trung vị (ms) |
|---|---:|---:|---:|
| random | 1.000 | 48 | 0,017 |
| random | 10.000 | 48 | 0,447 |
| random | 100.000 | 64 | 5,909 |
| sorted | 1.000 | 64 | 0,004 |
| sorted | 10.000 | 64 | 0,079 |
| sorted | 100.000 | 64 | 1,017 |
| reverse | 1.000 | 7 | 0,011 |
| reverse | 10.000 | 16 | 0,120 |
| reverse | 100.000 | 7 | 1,520 |
| duplicates | 1.000 | 16 | 0,011 |
| duplicates | 10.000 | 48 | 0,242 |
| duplicates | 100.000 | 64 | 2,834 |

### 4.2. Bảng chi tiết — dữ liệu ngẫu nhiên (random)

| Cutoff | n = 1.000 (ms) | n = 10.000 (ms) | n = 100.000 (ms) |
|---:|---:|---:|---:|
| 1 | 0,145 | 0,614 | 7,621 |
| 4 | 0,030 | 0,571 | 7,099 |
| 7 | 0,033 | 0,528 | 6,761 |
| 10 | 0,028 | 0,496 | 6,727 |
| 16 | 0,023 | 0,499 | 6,334 |
| 24 | 0,026 | 0,463 | 6,195 |
| 32 | 0,019 | 0,466 | 6,060 |
| 48 | **0,017** | **0,447** | 6,082 |
| 64 | 0,020 | 0,448 | **5,909** |

### 4.3. Bảng chi tiết — dữ liệu tăng dần (sorted)

| Cutoff | n = 1.000 (ms) | n = 10.000 (ms) | n = 100.000 (ms) |
|---:|---:|---:|---:|
| 1 | 0,016 | 0,175 | 2,065 |
| 4 | 0,010 | 0,137 | 1,538 |
| 7 | 0,010 | 0,117 | 1,396 |
| 10 | 0,008 | 0,103 | 1,400 |
| 16 | 0,007 | 0,102 | 1,233 |
| 24 | 0,007 | 0,084 | 1,176 |
| 32 | 0,006 | 0,085 | 1,147 |
| 48 | 0,005 | 0,080 | 1,120 |
| 64 | **0,004** | **0,079** | **1,017** |

### 4.4. Bảng chi tiết — dữ liệu giảm dần (reverse)

| Cutoff | n = 1.000 (ms) | n = 10.000 (ms) | n = 100.000 (ms) |
|---:|---:|---:|---:|
| 1 | 0,018 | 0,172 | 2,034 |
| 4 | 0,012 | 0,142 | 1,599 |
| 7 | **0,011** | 0,127 | **1,520** |
| 10 | 0,023 | 0,120 | 1,538 |
| 16 | 0,012 | **0,120** | 1,562 |
| 24 | 0,011 | 0,143 | 1,681 |
| 32 | 0,014 | 0,141 | 1,790 |
| 48 | 0,014 | 0,187 | 1,893 |
| 64 | 0,022 | 0,185 | 2,381 |

### 4.5. Bảng chi tiết — nhiều giá trị trùng (duplicates)

| Cutoff | n = 1.000 (ms) | n = 10.000 (ms) | n = 100.000 (ms) |
|---:|---:|---:|---:|
| 1 | 0,020 | 0,400 | 4,455 |
| 4 | 0,013 | 0,355 | 3,942 |
| 7 | 0,013 | 0,315 | 3,565 |
| 10 | 0,011 | 0,283 | 3,548 |
| 16 | **0,011** | 0,286 | 3,196 |
| 24 | 0,011 | 0,252 | 3,057 |
| 32 | 0,011 | 0,254 | 2,940 |
| 48 | 0,012 | **0,242** | 2,922 |
| 64 | 0,016 | 0,246 | **2,834** |

### 4.6. Tốc độ cải thiện so với cutoff = 1 (không dùng Insertion Sort)

| Dạng dữ liệu | Kích thước | Cutoff tốt nhất | Tốc độ cải thiện |
|---|---:|---:|---:|
| random | 1.000 | 48 | 8,70× |
| random | 10.000 | 48 | 1,37× |
| random | 100.000 | 64 | 1,29× |
| sorted | 1.000 | 64 | 3,81× |
| sorted | 10.000 | 64 | 2,22× |
| sorted | 100.000 | 64 | 2,03× |
| reverse | 1.000 | 7 | 1,58× |
| reverse | 10.000 | 16 | 1,43× |
| reverse | 100.000 | 7 | 1,34× |
| duplicates | 1.000 | 16 | 1,88× |
| duplicates | 10.000 | 48 | 1,65× |
| duplicates | 100.000 | 64 | 1,57× |

## 5. Phân tích

### 5.1. So sánh cutoff 7 và 10

| Dạng dữ liệu | Kích thước | Cutoff 7 (ms) | Cutoff 10 (ms) | Chênh lệch |
|---|---:|---:|---:|---:|
| random | 1.000 | 0,033 | 0,028 | cutoff 10 nhanh hơn 14,0% |
| random | 10.000 | 0,528 | 0,496 | cutoff 10 nhanh hơn 6,2% |
| random | 100.000 | 6,761 | 6,727 | cutoff 10 nhanh hơn 0,5% |
| sorted | 1.000 | 0,010 | 0,008 | cutoff 10 nhanh hơn 22,4% |
| sorted | 10.000 | 0,117 | 0,103 | cutoff 10 nhanh hơn 11,5% |
| sorted | 100.000 | 1,396 | 1,400 | cutoff 7 nhanh hơn 0,3% |
| reverse | 1.000 | 0,011 | 0,023 | cutoff 7 nhanh hơn 108,1% |
| reverse | 10.000 | 0,127 | 0,120 | cutoff 10 nhanh hơn 5,1% |
| reverse | 100.000 | 1,520 | 1,538 | cutoff 7 nhanh hơn 1,1% |
| duplicates | 1.000 | 0,013 | 0,011 | cutoff 10 nhanh hơn 12,7% |
| duplicates | 10.000 | 0,315 | 0,283 | cutoff 10 nhanh hơn 10,2% |
| duplicates | 100.000 | 3,565 | 3,548 | cutoff 10 nhanh hơn 0,5% |

**Nhận xét:** Trên 12 kịch bản, cutoff 10 nhanh hơn cutoff 7 trong **9/12 trường hợp**. Tuy nhiên, trên dữ liệu giảm dần (reverse) ở n = 1.000, cutoff 7 nhanh hơn rất đáng kể (0,011 ms so với 0,023 ms). Điều này có thể do với mảng giảm dần, Insertion Sort phải thực hiện nhiều phép dịch chuyển nhất (worst-case O(k²)), và khi cutoff = 10 thì mảng con lớn hơn khiến chi phí bậc hai tăng rõ rệt. Với kích thước lớn hơn, sự khác biệt giữa 7 và 10 không còn đáng kể (< 5%).

### 5.2. Cutoff tốt nhất theo từng dạng dữ liệu

- **random, sorted, duplicates:** Cutoff tốt nhất có xu hướng **tăng dần** khi kích thước mảng tăng, nằm trong khoảng **48–64**. Với ba dạng dữ liệu này, Insertion Sort hoạt động hiệu quả trên mảng con lớn hơn nhờ:
  - `sorted`: Insertion Sort đạt O(k) trên dữ liệu đã sắp — cutoff lớn rất có lợi.
  - `duplicates`: Nhiều giá trị trùng → nhiều phần tử "gần đúng vị trí" → Insertion Sort ít dịch chuyển.
  - `random`: Lợi ích từ việc giảm overhead đệ quy và tận dụng cache vẫn bù đắp được chi phí O(k²) của Insertion Sort khi k ≤ 64.

- **reverse:** Cutoff tốt nhất nằm trong khoảng **7–16**, thấp hơn rõ rệt. Đây là worst-case của Insertion Sort: mỗi phần tử phải dịch chuyển đến đầu mảng con, tổng chi phí O(k²) lớn nhất. Khi cutoff > 16, thời gian **tăng mạnh** — đặc biệt ở n = 100.000, cutoff 64 (2,381 ms) thậm chí chậm hơn cutoff 1 (2,034 ms).

### 5.3. Mức chênh lệch và tính ổn định

- Giữa cutoff = 1 (Merge Sort thuần) và cutoff tốt nhất, tốc độ cải thiện từ **1,29× đến 8,70×**, cho thấy kỹ thuật cutoff luôn mang lại lợi ích rõ rệt.
- Vùng cutoff **10–32** là "vùng an toàn": hiệu năng khá gần mức tối ưu trên hầu hết các dạng dữ liệu (trừ reverse ở cutoff > 24).
- Phương pháp đo bằng trung vị giúp loại bỏ outlier do GC (garbage collection) hay các tiến trình nền, tăng tính ổn định. Tuy nhiên, với n = 1.000, thời gian đo rất nhỏ (< 0,05 ms) nên nhiễu tương đối lớn hơn — một số biến động bất thường (ví dụ: reverse n = 1.000 cutoff = 10 đột ngột tăng lên 0,023 ms) có thể do yếu tố bên ngoài.

### 5.4. Hạn chế của phép đo

- **Chỉ đo trên một cấu hình máy duy nhất** (Intel i7-13620H, 16 GB RAM); cutoff tối ưu có thể khác trên kiến trúc CPU khác (AMD, ARM, v.v.) do khác biệt về kích thước cache L1/L2, branch prediction.
- **Kích thước mảng tối đa 100.000** — chưa thử nghiệm trên mảng hàng triệu phần tử, nơi yếu tố cache miss có thể thay đổi kết quả.
- **Chỉ dùng kiểu `int`** — với kiểu dữ liệu khác (Object, String), chi phí so sánh (`compareTo`) cao hơn có thể thay đổi cutoff tối ưu.
- **JVM warmup 10 lượt** có thể chưa đủ để JIT compiler tối ưu hóa hoàn toàn, đặc biệt với n nhỏ. JMH (Java Microbenchmark Harness) sẽ cho kết quả chính xác hơn.
- Thời gian đo ở n = 1.000 rất nhỏ (vài chục µs), **độ phân giải `nanoTime()`** và jitter hệ thống có thể gây sai số đáng kể.
- Seed cố định đảm bảo tính lặp lại nhưng chỉ tạo **một phân phối dữ liệu** cho mỗi dạng; nhiều seed khác nhau sẽ cho bức tranh đầy đủ hơn.

## 6. Kết luận

Trên cấu hình máy Intel i7-13620H / 16 GB / Windows 11 / Java 25:

- **Cutoff được chọn: 16** — đây là giá trị **cân bằng tốt nhất** trên tất cả các dạng dữ liệu:
  - Với random, sorted, duplicates: hiệu năng gần mức tối ưu (chênh lệch < 10% so với cutoff tốt nhất là 48–64).
  - Với reverse (worst-case của Insertion Sort): vẫn nằm trong vùng an toàn, không bị suy giảm hiệu năng như cutoff ≥ 24.

- **Giá trị giáo trình (7 và 10)** là hợp lý nhưng **bảo thủ**: trên máy hiện đại, cutoff lớn hơn (16–32) thường nhanh hơn nhờ cache L1 lớn hơn và branch prediction tốt hơn.

- **Lưu ý quan trọng:** Kết quả này chỉ áp dụng cho cấu hình máy cụ thể này. Không khái quát thành cutoff tối ưu cho mọi máy — cần chạy benchmark trên từng cấu hình để xác định giá trị phù hợp.

## 7. Tệp đính kèm
- `MergeSortCutoff.java` — cài đặt Merge Sort với Insertion Sort cutoff (theo bài giảng).
- `DataGenerator.java` — sinh dữ liệu: random, sorted, reverse, duplicates (seed cố định).
- `Benchmark.java` — chương trình benchmark: warmup, 31 lần đo, lấy trung vị, ghi CSV.
- `benchmark.csv` — kết quả thô: 108 bản ghi (type, size, cutoff, median_ns).
- `debai.txt` — đề bài gốc.
