<div align="center">

# <img src="https://api.iconify.design/lucide:zap.svg?color=%23f8fafc" width="30" height="30" style="vertical-align: middle; margin-bottom: 4px;"/> ELECTRO

**Platform e-commerce elektronik berbasis web dengan fitur manajemen produk, pemesanan, dan komplain pelanggan.**

<br/>

[![Java](https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.java.com/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com/)
[![Thymeleaf](https://img.shields.io/badge/Thymeleaf-3.1-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white)](https://www.thymeleaf.org/)
[![HTML5](https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=html5&logoColor=white)](https://developer.mozilla.org/en-US/docs/Web/HTML)
[![CSS3](https://img.shields.io/badge/CSS3-1572B6?style=for-the-badge&logo=css3&logoColor=white)](https://developer.mozilla.org/en-US/docs/Web/CSS)
[![JavaScript](https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black)](https://developer.mozilla.org/en-US/docs/Web/JavaScript)
[![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)](https://maven.apache.org/)
[![License: MIT](https://img.shields.io/badge/License-MIT-f59e0b?style=for-the-badge&logo=opensourceinitiative&logoColor=white)](LICENSE)

</div>

---

## <img src="https://api.iconify.design/lucide:book-open.svg?color=%233b82f6" width="22" height="22" style="vertical-align: middle; margin-bottom: 4px;"/> Tentang Proyek

**Electro Store** adalah aplikasi web toko elektronik yang dibangun menggunakan **Spring Boot** (Java). Aplikasi ini mendukung dua peran pengguna — **Admin** dan **Customer** — dengan fitur lengkap mulai dari manajemen produk, keranjang belanja, checkout, hingga sistem komplain.

Proyek ini dikembangkan oleh **Kelompok 2 — Alarm Dimatikan** sebagai proyek akhir mata kuliah.

---

## <img src="https://api.iconify.design/lucide:gantt-chart-square.svg?color=%2310b981" width="22" height="22" style="vertical-align: middle; margin-bottom: 4px;"/> Tech Stack

| Layer | Teknologi |
|---|---|
| **Backend** | Java 17, Spring Boot 3.5, Spring Security, Spring Data JPA |
| **Frontend** | Thymeleaf, HTML5, CSS3, JavaScript (Vanilla) |
| **Database** | MySQL 8 |
| **Build Tool** | Apache Maven |
| **Auth** | Spring Security (Form Login) + OAuth2 (Google Login) |
| **ORM** | Hibernate / JPA |

---

## <img src="https://api.iconify.design/lucide:sparkles.svg?color=%23eab308" width="22" height="22" style="vertical-align: middle; margin-bottom: 4px;"/> Fitur Utama

### <img src="https://api.iconify.design/lucide:user.svg?color=%236366f1" width="18" height="18" style="vertical-align: middle; margin-bottom: 3px;"/> Customer
- <img src="https://api.iconify.design/lucide:lock.svg?color=%2364748b" width="14" height="14" style="vertical-align: middle;"/> Register & Login (email/password atau akun Google)
- <img src="https://api.iconify.design/lucide:shopping-bag.svg?color=%2364748b" width="14" height="14" style="vertical-align: middle;"/> Browse produk elektronik berdasarkan kategori
- <img src="https://api.iconify.design/lucide:search.svg?color=%2364748b" width="14" height="14" style="vertical-align: middle;"/> **Linear Search** — pencarian produk berdasarkan nama, merk, atau kategori
- <img src="https://api.iconify.design/lucide:arrow-up-down.svg?color=%2364748b" width="14" height="14" style="vertical-align: middle;"/> **Bubble Sort** — urutkan produk berdasarkan harga (ascending / descending)
- <img src="https://api.iconify.design/lucide:shopping-cart.svg?color=%2364748b" width="14" height="14" style="vertical-align: middle;"/> Keranjang belanja & checkout
- <img src="https://api.iconify.design/lucide:package.svg?color=%2364748b" width="14" height="14" style="vertical-align: middle;"/> Riwayat pesanan
- <img src="https://api.iconify.design/lucide:file-text.svg?color=%2364748b" width="14" height="14" style="vertical-align: middle;"/> Ajukan komplain produk
- <img src="https://api.iconify.design/lucide:settings.svg?color=%2364748b" width="14" height="14" style="vertical-align: middle;"/> Pengaturan profil akun

### <img src="https://api.iconify.design/lucide:shield-check.svg?color=%23ef4444" width="18" height="18" style="vertical-align: middle; margin-bottom: 3px;"/> Admin
- <img src="https://api.iconify.design/lucide:layout-dashboard.svg?color=%2364748b" width="14" height="14" style="vertical-align: middle;"/> Dashboard statistik (total penjualan, order masuk, produk terjual, stok tersisa)
- <img src="https://api.iconify.design/lucide:plus-circle.svg?color=%2364748b" width="14" height="14" style="vertical-align: middle;"/> Tambah, edit, hapus produk elektronik
- <img src="https://api.iconify.design/lucide:users.svg?color=%2364748b" width="14" height="14" style="vertical-align: middle;"/> Manajemen user
- <img src="https://api.iconify.design/lucide:truck.svg?color=%2364748b" width="14" height="14" style="vertical-align: middle;"/> Manajemen order (ubah status pesanan)
- <img src="https://api.iconify.design/lucide:messages-square.svg?color=%2364748b" width="14" height="14" style="vertical-align: middle;"/> Manajemen komplain pelanggan
- <img src="https://api.iconify.design/lucide:trending-up.svg?color=%2364748b" width="14" height="14" style="vertical-align: middle;"/> Laporan dan grafik penjualan

### <img src="https://api.iconify.design/lucide:tags.svg?color=%23a855f7" width="18" height="18" style="vertical-align: middle; margin-bottom: 3px;"/> Kategori Produk
`HP` · `Laptop` · `Tablet` · `TV` · `AC` · `Kulkas` · `Blender` · `Headphone`

---

## <img src="https://api.iconify.design/lucide:folder-tree.svg?color=%2306b6d4" width="22" height="22" style="vertical-align: middle; margin-bottom: 4px;"/> Struktur Proyek
```
Electro/
├── src/
│   ├── main/
│   │   ├── java/com/electro/
│   │   │   ├── config/          # Konfigurasi Spring Security
│   │   │   ├── controller/      # MVC & REST Controllers
│   │   │   ├── model/           # Entity JPA (Electronic, Order, User, Complaint, dll.)
│   │   │   ├── repository/      # Spring Data JPA Repositories
│   │   │   ├── security/        # Custom UserDetails & OAuth2 Service
│   │   │   ├── service/         # Business Logic (Search, Sort, CRUD)
│   │   │   ├── DataSeeder.java  # Seed data awal
│   │   │   └── ElectroApplication.java
│   │   └── resources/
│   │       ├── static/
│   │       │   ├── css/         # Stylesheet per halaman
│   │       │   └── js/          # Script per halaman
│   │       ├── templates/       # Thymeleaf HTML templates
│   │       └── application.properties
│   └── test/
├── data/                        # H2/local DB files
├── LICENSE                      # File lisensi proyek (MIT License)
├── README.md                    # Dokumentasi proyek
├── pom.xml
└── mvnw

```

---

## <img src="https://api.iconify.design/lucide:rocket.svg?color=%23f97316" width="22" height="22" style="vertical-align: middle; margin-bottom: 4px;"/> Cara Menjalankan

### Prasyarat

- **Java 17** atau lebih tinggi
- **MySQL 8** (lokal atau cloud)
- **Maven 3.x** (atau gunakan `./mvnw` yang sudah disertakan)

### Langkah Instalasi

**1. Clone repositori**
```bash
git clone [https://github.com/Kelompok-2-Alarm-dimatikan/Electro.git](https://github.com/Kelompok-2-Alarm-dimatikan/Electro.git)
cd Electro
```

---

### Langkah Instalasi

**1. Clone repositori**
```bash
git clone https://github.com/Kelompok-2-Alarm-dimatikan/Electro.git
cd Electro
```

**2. Konfigurasi database**

Buka `src/main/resources/application.properties` dan sesuaikan:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/electrodb
spring.datasource.username=root
spring.datasource.password=your_password
```

> ⚠️ **Penting:** Jangan commit file `application.properties` yang berisi kredensial asli ke repositori publik. Gunakan environment variable atau `.env` file.

**3. Konfigurasi Google OAuth2 (opsional)**

Jika ingin menggunakan fitur login via Google, isi di `application.properties`:
```properties
spring.security.oauth2.client.registration.google.client-id=YOUR_CLIENT_ID
spring.security.oauth2.client.registration.google.client-secret=YOUR_CLIENT_SECRET
```

**4. Jalankan aplikasi**
```bash
./mvnw spring-boot:run
```
Atau di Windows:
```cmd
mvnw.cmd spring-boot:run
```

**5. Buka di browser**
```
http://localhost:8080
```

---

## <img src="https://api.iconify.design/lucide:key-round.svg?color=%2314b8a6" width="22" height="22" style="vertical-align: middle; margin-bottom: 4px;"/> Akses Default

| Role | URL | Keterangan |
|---|---|---|
| Landing Page | `/` | Halaman publik |
| Login | `/login` | Form login |
| Register | `/register` | Daftar akun baru |
| Customer | `/electronic` | Halaman utama setelah login |
| Admin | `/admin` | Hanya untuk role ADMIN |

> Akun admin dapat dibuat melalui seeder (`DataSeeder.java`) atau langsung di database dengan role `ADMIN`.

---

## <img src="https://api.iconify.design/lucide:cpu.svg?color=%23ec4899" width="22" height="22" style="vertical-align: middle; margin-bottom: 4px;"/> Algoritma yang Diimplementasikan

| Algoritma | Fungsi | Lokasi |
|---|---|---|
| **Linear Search** | Pencarian produk berdasarkan nama, merk, atau kategori (mendukung multi-kata / AND) | `ElectroService.java` |
| **Bubble Sort** | Pengurutan produk berdasarkan harga (ascending & descending) | `ElectroService.java` |

---

## <img src="https://api.iconify.design/lucide:users-2.svg?color=%238b5cf6" width="22" height="22" style="vertical-align: middle; margin-bottom: 4px;"/> Tim Pengembang

| Nama | GitHub |
|---|---|
| Syafiq (Syaaxi) | [@ThisSyaa](https://github.com/ThisSyaa) |
| Irfan | [@irfanfebym-sketch](https://github.com/irfanfebym-sketch) |
| Dewangga | [@DewanggaJunior](https://github.com/DewanggaJunior) |
| Fadil | [@fadilsss-ai](https://github.com/fadilsss-ai) |
| Revaliza | [@revalizaaura](https://github.com/revalizaaura) |
| Nazila | [@nazilaqieeta-svg](https://github.com/nazilaqieeta-svg) |
| Shelomita | [@shelomitaaa](https://github.com/shelomitaaa) |

---

## <img src="https://api.iconify.design/lucide:scale.svg?color=%23f59e0b" width="22" height="22" style="vertical-align: middle; margin-bottom: 4px;"/> Lisensi

Proyek ini dilisensikan di bawah **MIT License** — lihat file [LICENSE](LICENSE) untuk detail lengkap.

---

<div align="center">

Dibuat oleh **Kelompok 2 - Alarm Dimatikan**

</div>

