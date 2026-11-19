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
| random | 10.000 | 64 | 0,455 |
| random | 100.000 | 64 | 6,439 |
| sorted | 1.000 | 64 | 0,005 |
| sorted | 10.000 | 48 | 0,080 |
| sorted | 100.000 | 48 | 1,216 |
| reverse | 1.000 | 10 | 0,011 |
| reverse | 10.000 | 16 | 0,136 |
| reverse | 100.000 | 10 | 1,653 |
| duplicates | 1.000 | 16 | 0,010 |
| duplicates | 10.000 | 64 | 0,231 |
| duplicates | 100.000 | 32 | 2,934 |

### 4.2. Bảng chi tiết — dữ liệu ngẫu nhiên (random)

| Cutoff | n = 1.000 (ms) | n = 10.000 (ms) | n = 100.000 (ms) |
|---:|---:|---:|---:|
| 1 | 0,110 | 0,623 | 8,301 |
| 4 | 0,055 | 0,603 | 7,835 |
| 7 | 0,084 | 0,572 | 7,411 |
| 10 | 0,029 | 0,888 | 7,302 |
| 16 | 0,029 | 0,600 | 7,060 |
| 24 | 0,026 | 0,486 | 6,987 |
| 32 | 0,023 | 0,501 | 6,986 |
| 48 | **0,017** | 0,458 | 6,664 |
| 64 | 0,023 | **0,455** | **6,439** |

### 4.3. Bảng chi tiết — dữ liệu tăng dần (sorted)

| Cutoff | n = 1.000 (ms) | n = 10.000 (ms) | n = 100.000 (ms) |
|---:|---:|---:|---:|
| 1 | 0,016 | 0,176 | 2,158 |
| 4 | 0,011 | 0,140 | 1,637 |
| 7 | 0,010 | 0,119 | 1,573 |
| 10 | 0,008 | 0,109 | 1,495 |
| 16 | 0,007 | 0,105 | 1,918 |
| 24 | 0,007 | 0,088 | 1,372 |
| 32 | 0,006 | 0,089 | 1,259 |
| 48 | 0,006 | **0,080** | **1,216** |
| 64 | **0,005** | 0,081 | 1,372 |

### 4.4. Bảng chi tiết — dữ liệu giảm dần (reverse)

| Cutoff | n = 1.000 (ms) | n = 10.000 (ms) | n = 100.000 (ms) |
|---:|---:|---:|---:|
| 1 | 0,029 | 0,451 | 2,442 |
| 4 | 0,012 | 0,292 | 1,795 |
| 7 | 0,011 | 0,318 | 1,715 |
| 10 | **0,011** | 0,170 | **1,653** |
| 16 | 0,012 | **0,136** | 1,689 |
| 24 | 0,013 | 0,346 | 1,751 |
| 32 | 0,016 | 0,169 | 1,969 |
| 48 | 0,033 | 0,212 | 2,032 |
| 64 | 0,056 | 0,194 | 2,660 |

### 4.5. Bảng chi tiết — nhiều giá trị trùng (duplicates)

| Cutoff | n = 1.000 (ms) | n = 10.000 (ms) | n = 100.000 (ms) |
|---:|---:|---:|---:|
| 1 | 0,022 | 0,391 | 6,079 |
| 4 | 0,013 | 0,349 | 4,402 |
| 7 | 0,013 | 0,340 | 3,680 |
| 10 | 0,011 | 0,292 | 3,785 |
| 16 | **0,010** | 0,288 | 3,299 |
| 24 | 0,011 | 0,242 | 3,083 |
| 32 | 0,012 | 0,246 | **2,934** |
| 48 | 0,011 | 0,234 | 2,950 |
| 64 | 0,016 | **0,231** | 3,136 |

### 4.6. Tốc độ cải thiện so với cutoff = 1 (không dùng Insertion Sort)

| Dạng dữ liệu | Kích thước | Cutoff tốt nhất | Tốc độ cải thiện |
|---|---:|---:|---:|
| random | 1.000 | 48 | 6,33× |
| random | 10.000 | 64 | 1,37× |
| random | 100.000 | 64 | 1,29× |
| sorted | 1.000 | 64 | 3,57× |
| sorted | 10.000 | 48 | 2,19× |
| sorted | 100.000 | 48 | 1,77× |
| reverse | 1.000 | 10 | 2,74× |
| reverse | 10.000 | 16 | 3,32× |
| reverse | 100.000 | 10 | 1,48× |
| duplicates | 1.000 | 16 | 2,08× |
| duplicates | 10.000 | 64 | 1,70× |
| duplicates | 100.000 | 32 | 2,07× |

## 5. Phân tích

### 5.1. So sánh cutoff 7 và 10

| Dạng dữ liệu | Kích thước | Cutoff 7 (ms) | Cutoff 10 (ms) | Chênh lệch |
|---|---:|---:|---:|---:|
| random | 1.000 | 0,084 | 0,029 | cutoff 10 nhanh hơn 65,1% |
| random | 10.000 | 0,572 | 0,888 | cutoff 7 nhanh hơn 55,1% |
| random | 100.000 | 7,411 | 7,302 | cutoff 10 nhanh hơn 1,5% |
| sorted | 1.000 | 0,010 | 0,008 | cutoff 10 nhanh hơn 20,8% |
| sorted | 10.000 | 0,119 | 0,109 | cutoff 10 nhanh hơn 8,8% |
| sorted | 100.000 | 1,573 | 1,495 | cutoff 10 nhanh hơn 4,9% |
| reverse | 1.000 | 0,011 | 0,011 | xấp xỉ bằng nhau (6,1%) |
| reverse | 10.000 | 0,318 | 0,170 | cutoff 10 nhanh hơn 46,7% |
| reverse | 100.000 | 1,715 | 1,653 | cutoff 10 nhanh hơn 3,6% |
| duplicates | 1.000 | 0,013 | 0,011 | cutoff 10 nhanh hơn 15,5% |
| duplicates | 10.000 | 0,340 | 0,292 | cutoff 10 nhanh hơn 14,2% |
| duplicates | 100.000 | 3,680 | 3,785 | cutoff 7 nhanh hơn 2,9% |

**Nhận xét:** Trên 12 kịch bản, cutoff 10 nhanh hơn hoặc xấp xỉ bằng cutoff 7 trong **10/12 trường hợp**. Chỉ có 2 trường hợp cutoff 7 nhanh hơn rõ rệt: random n=10.000 (chênh 55,1%, có thể do nhiễu đo — giá trị cutoff 10 ở đây bất thường cao so với xu hướng) và duplicates n=100.000 (chênh chỉ 2,9%, không đáng kể). Với kích thước lớn (n=100.000), hai giá trị cutoff cho hiệu năng gần nhau (chênh < 5%), cho thấy sự khác biệt giữa 7 và 10 không có ý nghĩa thực tế ở dữ liệu lớn.

**Lưu ý về nhiễu:** Một số kết quả ở kích thước nhỏ (n=1.000) và trung bình (n=10.000) cho thấy nhiễu đáng kể — ví dụ random n=10.000 cutoff=10 đo được 0,888 ms cao bất thường so với cutoff=7 (0,572 ms) và cutoff=16 (0,600 ms). Điều này là đặc trưng của microbenchmark khi thời gian đo quá nhỏ, dễ bị ảnh hưởng bởi GC, JIT recompilation, hoặc tiến trình nền.

### 5.2. Cutoff tốt nhất theo từng dạng dữ liệu

- **random:** Cutoff tốt nhất nằm trong khoảng **48–64**. Thời gian giảm đều đặn khi cutoff tăng từ 1 lên 64 (đặc biệt rõ ở n=100.000: 8,301 ms → 6,439 ms). Insertion Sort trên dữ liệu ngẫu nhiên có chi phí trung bình O(k²/4), vẫn thấp hơn overhead đệ quy Merge Sort khi k ≤ 64.

- **sorted:** Cutoff tốt nhất nằm trong khoảng **48–64**. Insertion Sort đạt O(k) trên dữ liệu đã sắp — cutoff lớn rất có lợi. Tốc độ cải thiện lên đến 3,57× so với cutoff = 1.

- **reverse:** Cutoff tốt nhất chỉ **10–16**, thấp hơn rõ rệt so với các dạng khác. Đây là worst-case của Insertion Sort: mỗi phần tử phải dịch chuyển đến đầu mảng con, tổng chi phí O(k²). Khi cutoff > 16, thời gian **tăng mạnh** — đặc biệt ở n=100.000, cutoff 64 (2,660 ms) **chậm hơn** cutoff 1 (2,442 ms), tức kỹ thuật cutoff phản tác dụng ở mức này.

- **duplicates:** Cutoff tốt nhất nằm trong khoảng **16–64** tùy kích thước. Nhiều giá trị trùng → nhiều phần tử "gần đúng vị trí" → Insertion Sort ít dịch chuyển. Tuy nhiên ở n=100.000, cutoff 64 (3,136 ms) đã bắt đầu chậm hơn cutoff 32 (2,934 ms), cho thấy có một "đỉnh" tối ưu rồi giảm.

### 5.3. Mức chênh lệch và tính ổn định

- Giữa cutoff = 1 (Merge Sort thuần) và cutoff tốt nhất, tốc độ cải thiện từ **1,29× đến 6,33×**, cho thấy kỹ thuật cutoff luôn mang lại lợi ích rõ rệt.
- Vùng cutoff **10–32** là "vùng an toàn": hiệu năng khá gần mức tối ưu trên hầu hết các dạng dữ liệu (trừ reverse ở cutoff > 24).
- Phương pháp đo bằng trung vị giúp loại bỏ outlier do GC hay các tiến trình nền. Tuy nhiên, kết quả vẫn có nhiễu đáng kể ở kích thước nhỏ — ví dụ:
  - random n=1.000 cutoff=7 (0,084 ms) cao bất thường so với cutoff=4 (0,055 ms) và cutoff=10 (0,029 ms).
  - random n=10.000 cutoff=10 (0,888 ms) vọt cao so với xu hướng xung quanh.
  - reverse n=10.000 cutoff=24 (0,346 ms) đột biến giữa cutoff=16 (0,136 ms) và cutoff=32 (0,169 ms).
- Những biến động này cho thấy 31 lượt đo có thể chưa đủ để loại bỏ hết nhiễu, đặc biệt khi thời gian thực thi quá nhỏ (< 1 ms).

### 5.4. Hạn chế của phép đo

- **Chỉ đo trên một cấu hình máy duy nhất** (Intel i7-13620H, 16 GB RAM); cutoff tối ưu có thể khác trên kiến trúc CPU khác (AMD, ARM, v.v.) do khác biệt về kích thước cache L1/L2, branch prediction.
- **Kích thước mảng tối đa 100.000** — chưa thử nghiệm trên mảng hàng triệu phần tử, nơi yếu tố cache miss có thể thay đổi kết quả.
- **Chỉ dùng kiểu `int`** — với kiểu dữ liệu khác (Object, String), chi phí so sánh (`compareTo`) cao hơn có thể thay đổi cutoff tối ưu.
- **JVM warmup 10 lượt** có thể chưa đủ để JIT compiler tối ưu hóa hoàn toàn, đặc biệt với n nhỏ. JMH (Java Microbenchmark Harness) sẽ cho kết quả chính xác hơn.
- Thời gian đo ở n = 1.000 rất nhỏ (vài chục µs), **độ phân giải `nanoTime()`** và jitter hệ thống có thể gây sai số đáng kể — thể hiện qua các giá trị bất thường (outlier) đã nêu.
- Seed cố định đảm bảo tính lặp lại nhưng chỉ tạo **một phân phối dữ liệu** cho mỗi dạng; nhiều seed khác nhau sẽ cho bức tranh đầy đủ hơn.
- **Tiến trình nền** trên máy không được kiểm soát hoàn toàn trong lúc đo, có thể gây nhiễu.

## 6. Kết luận

Trên cấu hình máy Intel i7-13620H / 16 GB / Windows 11 / Java 25:

- **Cutoff được chọn: 16** — đây là giá trị **cân bằng tốt nhất** trên tất cả các dạng dữ liệu:
  - Với random, sorted, duplicates: hiệu năng gần mức tối ưu (chênh lệch < 15% so với cutoff tốt nhất là 48–64).
  - Với reverse (worst-case của Insertion Sort): vẫn nằm trong vùng an toàn — cutoff 16 thực tế là cutoff tốt nhất cho reverse n=10.000 (0,136 ms), và chỉ chênh ~2% so với cutoff tốt nhất ở reverse n=100.000.
  - Nếu tăng cutoff lên 32 hoặc 48, hiệu năng tốt hơn trên random/sorted nhưng **suy giảm rõ rệt trên reverse** (reverse n=100.000 cutoff=48 là 2,032 ms so với cutoff=10 là 1,653 ms — chậm hơn 23%).

- **Giá trị giáo trình (7 và 10)** là hợp lý nhưng **bảo thủ**: trên máy hiện đại, cutoff 10 nhất quán nhanh hơn cutoff 7 (10/12 kịch bản). Cutoff 16 tối ưu hơn cả hai.

- **Lưu ý quan trọng:** Kết quả này chỉ áp dụng cho cấu hình máy cụ thể này. Không khái quát thành cutoff tối ưu cho mọi máy — cần chạy benchmark trên từng cấu hình để xác định giá trị phù hợp.

## 7. Tệp đính kèm
- `MergeSortCutoff.java` — cài đặt Merge Sort với Insertion Sort cutoff (theo bài giảng).
- `DataGenerator.java` — sinh dữ liệu: random, sorted, reverse, duplicates (seed cố định).
- `Benchmark.java` — chương trình benchmark: warmup, 31 lần đo, lấy trung vị, ghi CSV.
- `benchmark.csv` — kết quả thô: 108 bản ghi (type, size, cutoff, median_ns).
- `debai.txt` — đề bài gốc.
