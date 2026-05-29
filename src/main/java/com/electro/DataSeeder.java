package com.electro;
import com.electro.model.*;
import com.electro.repository.ElectroRepository;
import com.electro.repository.UserRepository;
import com.electro.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner initData(ElectroRepository electroRepo,
                               UserRepository userRepo,
                               UserService userService,
                               JdbcTemplate jdbcTemplate,
                               org.springframework.security.crypto.password.PasswordEncoder passwordEncoder) {
        return args -> {
            try {
                jdbcTemplate.execute("ALTER TABLE users MODIFY COLUMN notified BOOLEAN DEFAULT FALSE;");
                jdbcTemplate.execute("ALTER TABLE orders MODIFY COLUMN notified BOOLEAN DEFAULT FALSE;");
                jdbcTemplate.execute("UPDATE users SET provider = 'LOCAL' WHERE provider IS NULL");
            } catch (Exception e) {
                // Ignore if it fails
            }

            boolean needsReseed = electroRepo.count() < 19 
                || electroRepo.findAll().stream().anyMatch(e -> e.getImageUrl() == null || e.getImageUrl().isEmpty());
            if (needsReseed) {
                electroRepo.deleteAll();

                // HP 
                Hp samsungS23 = new Hp("Samsung S23 Ultra", 14999000, 10, "Samsung");
                samsungS23.setDeskripsi("Samsung Galaxy S23 Ultra adalah smartphone flagship yang dilengkapi dengan S Pen bawaan, ditenagai chipset khusus Snapdragon 8 Gen 2, dan kamera utama 200MP dengan zoom hingga 100x. Ponsel ini memiliki layar 6.8 inci QHD+ Dynamic AMOLED 2X 120Hz dan baterai 5000 mAh.");
                samsungS23.setSpesifikasi("Processor: Snapdragon 8 Gen 2 for Galaxy | Layar: 6.8\" QHD+ (3088x1440) 120Hz | Kamera Belakang: 200MP Utama, 12MP UW, 10MP Tele, 10MP Periskop | Kamera Depan: 12MP Dual Pixel AF | Baterai: 5000 mAh (45W Fast Charging) | Ketahanan: IP68, Armor Aluminum, Victus 2");
                samsungS23.setImageUrl("https://images.unsplash.com/photo-1610945415295-d9bbf067e59c?q=80&w=400&auto=format&fit=crop");
                electroRepo.save(samsungS23);

                Hp samsungJ2 = new Hp("Samsung J2 Prime", 1499000, 10, "Samsung");
                samsungJ2.setDeskripsi("Samsung Galaxy J2 Prime adalah smartphone entry-level yang cocok untuk pengguna yang baru pertama kali menggunakan smartphone. Hadir dengan desain compact dan baterai tahan lama untuk kebutuhan sehari-hari.");
                samsungJ2.setSpesifikasi("Processor: MediaTek MT6737T Quad-core 1.4GHz | Layar: 5.0\" WVGA (480x800) | Kamera Belakang: 8MP | Kamera Depan: 5MP | Baterai: 2600 mAh | RAM: 1.5GB | Storage: 8GB");
                samsungJ2.setImageUrl("https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?q=80&w=400&auto=format&fit=crop");
                electroRepo.save(samsungJ2);

                Hp xiaomi13 = new Hp("Xiaomi 13", 8999000, 10, "Xiaomi");
                xiaomi13.setDeskripsi("Xiaomi 13 hadir sebagai flagship Xiaomi dengan kamera berkolaborasi bersama Leica, performa tinggi bertenaga Snapdragon 8 Gen 2, dan desain premium kompak. Cocok untuk pengguna yang menginginkan kualitas foto profesional di genggaman.");
                xiaomi13.setSpesifikasi("Processor: Snapdragon 8 Gen 2 | Layar: 6.36\" AMOLED (1080x2400) 120Hz | Kamera Belakang: 54MP Leica Utama, 12MP UW, 10MP Tele | Kamera Depan: 32MP | Baterai: 4500 mAh (67W Fast Charging) | RAM: 8/12GB | Storage: 128/256GB");
                xiaomi13.setImageUrl("https://images.unsplash.com/photo-1592899677977-9c10ca588bbd?q=80&w=400&auto=format&fit=crop");
                electroRepo.save(xiaomi13);

                Hp iphone14 = new Hp("iPhone 14", 13999000, 10, "Apple");
                iphone14.setDeskripsi("iPhone 14 hadir dengan chip A15 Bionic yang powerful, sistem kamera yang ditingkatkan dengan mode aksi, dan fitur keselamatan Emergency SOS via satelit. Desain elegan dengan layar Super Retina XDR yang jernih.");
                iphone14.setSpesifikasi("Processor: Apple A15 Bionic | Layar: 6.1\" Super Retina XDR (1170x2532) 60Hz | Kamera Belakang: 12MP Utama, 12MP UW | Kamera Depan: 12MP TrueDepth | Baterai: 3279 mAh (20W Fast Charging) | RAM: 6GB | Storage: 128/256/512GB");
                iphone14.setImageUrl("https://images.unsplash.com/photo-1616348436168-de43ad0db179?q=80&w=400&auto=format&fit=crop");
                electroRepo.save(iphone14);

                // LAPTOP 
                Laptop asusRog = new Laptop("Asus ROG", 25999000, 10, "Asus");
                asusRog.setDeskripsi("Asus ROG adalah laptop gaming kelas atas yang dirancang untuk performa maksimal. Dibekali GPU dedicated terkini dan sistem pendingin canggih, laptop ini mampu menjalankan game AAA and aplikasi berat dengan lancar.");
                asusRog.setSpesifikasi("Processor: Intel Core i9-13900H | Layar: 15.6\" FHD (1920x1080) 165Hz | GPU: NVIDIA RTX 4070 8GB | RAM: 16GB DDR5 | Storage: 1TB NVMe SSD | Baterai: 90Wh | OS: Windows 11");
                asusRog.setImageUrl("https://images.unsplash.com/photo-1603302576837-37561b2e2302?q=80&w=400&auto=format&fit=crop");
                electroRepo.save(asusRog);

                Laptop acerPredator = new Laptop("Acer Predator", 22999000, 10, "Acer");
                acerPredator.setDeskripsi("Acer Predator Helios hadir sebagai laptop gaming bertenaga tinggi dengan desain agresif khas Predator. Dilengkapi teknologi pendingin AeroBlade 3D Fan untuk menjaga performa tetap optimal saat gaming marathon.");
                acerPredator.setSpesifikasi("Processor: Intel Core i7-13700HX | Layar: 15.6\" QHD (2560x1440) 165Hz | GPU: NVIDIA RTX 4060 8GB | RAM: 16GB DDR5 | Storage: 512GB NVMe SSD | Baterai: 76Wh | OS: Windows 11");
                acerPredator.setImageUrl("https://images.unsplash.com/photo-1593642632823-8f785ba67e45?q=80&w=400&auto=format&fit=crop");
                electroRepo.save(acerPredator);

                Laptop lenovoLegion = new Laptop("Lenovo Legion", 19999000, 10, "Lenovo");
                lenovoLegion.setDeskripsi("Lenovo Legion 5 adalah laptop gaming mid-range yang menawarkan keseimbangan sempurna antara performa dan harga. Sistem pendingin Coldfront 5.0 memastikan suhu tetap terjaga bahkan saat sesi gaming panjang.");
                lenovoLegion.setSpesifikasi("Processor: AMD Ryzen 7 7745HX | Layar: 15.6\" FHD (1920x1080) 144Hz | GPU: NVIDIA RTX 4060 8GB | RAM: 16GB DDR5 | Storage: 512GB NVMe SSD | Baterai: 80Wh | OS: Windows 11");
                lenovoLegion.setImageUrl("https://images.unsplash.com/photo-1588872657578-7efd1f1555ed?q=80&w=400&auto=format&fit=crop");
                electroRepo.save(lenovoLegion);

                // TABLET 
                Tablet ipadPro = new Tablet("iPad Pro", 16999000, 10, "Apple");
                ipadPro.setDeskripsi("iPad Pro adalah tablet paling canggih dari Apple yang ditenagai chip M2. Dengan layar Liquid Retina XDR ProMotion 120Hz dan konektivitas Thunderbolt, iPad Pro mampu menggantikan laptop untuk berbagai pekerjaan kreatif profesional.");
                ipadPro.setSpesifikasi("Processor: Apple M2 | Layar: 11\" Liquid Retina XDR (2388x1668) 120Hz ProMotion | Kamera Belakang: 12MP Wide, 10MP UW | Kamera Depan: 12MP Ultra Wide | Storage: 128GB/256GB/512GB/1TB/2TB | Konektivitas: Thunderbolt/USB4, Wi-Fi 6E, 5G");
                ipadPro.setImageUrl("https://images.unsplash.com/photo-1544244015-0df4b3ffc6b0?q=80&w=400&auto=format&fit=crop");
                electroRepo.save(ipadPro);

                Tablet samsungTab = new Tablet("Samsung Galaxy Tab", 9999000, 10, "Samsung");
                samsungTab.setDeskripsi("Samsung Galaxy Tab S8 hadir sebagai tablet Android premium dengan layar AMOLED yang memukau dan dukungan S Pen. Performa tinggi bertenaga Snapdragon 8 Gen 1 cocok untuk produktivitas, hiburan, dan kreativitas.");
                samsungTab.setSpesifikasi("Processor: Snapdragon 8 Gen 1 | Layar: 11\" LTPS TFT (1600x2560) 120Hz | Kamera Belakang: 13MP Wide, 6MP UW | Kamera Depan: 12MP UW | RAM: 8GB | Storage: 128/256GB | Baterai: 8000 mAh (45W Fast Charging)");
                samsungTab.setImageUrl("https://images.unsplash.com/photo-1633185360980-dfc79db14d23?q=80&w=400&auto=format&fit=crop");
                electroRepo.save(samsungTab);

                // TV 
                Tv samsungQled = new Tv("Samsung QLED 55\"", 12999000, 5, "Samsung");
                samsungQled.setDeskripsi("Samsung QLED 55\" menghadirkan kualitas gambar luar biasa dengan teknologi Quantum Dot yang menghasilkan warna lebih akurat dan cerah. Didukung prosesor Neural Quantum untuk tampilan yang selalu optimal di berbagai kondisi pencahayaan.");
                samsungQled.setSpesifikasi("Ukuran: 55 inci | Resolusi: 4K UHD (3840x2160) | Panel: QLED | Refresh Rate: 120Hz | HDR: Quantum HDR | Smart TV: Tizen OS | Konektivitas: 4x HDMI, 2x USB, Wi-Fi, Bluetooth | Suara: 20W Dolby Atmos");
                samsungQled.setImageUrl("https://images.unsplash.com/photo-1593359677879-a4bb92f829d1?q=80&w=400&auto=format&fit=crop");
                electroRepo.save(samsungQled);

                Tv lgOled = new Tv("LG OLED 65\"", 22999000, 3, "LG");
                lgOled.setDeskripsi("LG OLED 65\" adalah puncak teknologi televisi dengan panel OLED self-lit yang menghasilkan hitam sempurna dan kontras tak terbatas. Prosesor α9 Gen6 AI memastikan kualitas gambar dan suara terbaik dengan pemrosesan cerdas berbasis AI.");
                lgOled.setSpesifikasi("Ukuran: 65 inci | Resolusi: 4K UHD (3840x2160) | Panel: OLED evo | Refresh Rate: 120Hz | HDR: Dolby Vision, HDR10, HLG | Smart TV: webOS 23 | Konektivitas: 4x HDMI 2.1, 3x USB, Wi-Fi 6, Bluetooth 5.0 | Suara: 60W Dolby Atmos");
                lgOled.setImageUrl("https://images.unsplash.com/photo-1593789198777-f29bc259780e?q=80&w=400&auto=format&fit=crop");
                electroRepo.save(lgOled);

                // AC 
                Ac daikin = new Ac("Daikin Inverter 1PK", 4999000, 8, "Daikin");
                daikin.setDeskripsi("Daikin Inverter 1PK hadir sebagai AC hemat energi dengan teknologi inverter terdepan. Mampu mendinginkan ruangan lebih cepat dengan konsumsi listrik lebih efisien, dilengkapi filter pembersih udara untuk kenyamanan maksimal.");
                daikin.setSpesifikasi("Kapasitas: 1 PK (9000 BTU) | Tipe: Split Inverter | Daya Listrik: 750 Watt | Refrigerant: R-32 | Mode: Cool, Fan, Dry, Auto | Filter: Anti-Bakteri, Anti-Jamur | Rating Bintang: 5 Star | Garansi Kompresor: 5 Tahun");
                daikin.setImageUrl("https://images.unsplash.com/photo-1621905251189-08b45d6a269e?q=80&w=400&auto=format&fit=crop");
                electroRepo.save(daikin);

                Ac panasonic = new Ac("Panasonic Inverter 1.5PK", 5499000, 6, "Panasonic");
                panasonic.setDeskripsi("Panasonic Inverter 1.5PK dilengkapi dengan teknologi nanoe-X yang mampu menghambat virus dan bakteri di udara. Cocok untuk ruangan berukuran sedang dengan performa pendinginan optimal dan konsumsi daya yang efisien.");
                panasonic.setSpesifikasi("Kapasitas: 1.5 PK (12000 BTU) | Tipe: Split Inverter | Daya Listrik: 1100 Watt | Refrigerant: R-32 | Mode: Cool, Fan, Dry, Auto, Nanoe-X | Filter: nanoe-X, PM2.5 | Rating Bintang: 4 Star | Garansi Kompresor: 5 Tahun");
                panasonic.setImageUrl("https://images.unsplash.com/photo-1581092921461-eab62e97a780?q=80&w=400&auto=format&fit=crop");
                electroRepo.save(panasonic);

                // BLENDER 
                Blender philips = new Blender("Philips Pro Blender", 1299000, 15, "Philips");
                philips.setDeskripsi("Philips Pro Blender hadir dengan motor bertenaga tinggi yang mampu menghancurkan es batu dan biji-bijian keras dengan mudah. Pisau ProBlend 6 3D memastikan hasil blender yang halus dan merata untuk smoothie, jus, dan masakan.");
                philips.setSpesifikasi("Daya Motor: 1400 Watt | Kecepatan: 3 Level + Pulse | Kapasitas Jar: 2 Liter (Kaca) | Pisau: ProBlend 6 3D Stainless Steel | Fungsi: Crush Ice, Smoothie, Soup | Keamanan: Anti-Slip, Auto Cut-Off | Garansi: 2 Tahun");
                philips.setImageUrl("https://images.unsplash.com/photo-1585515320310-259814833e62?q=80&w=400&auto=format&fit=crop");
                electroRepo.save(philips);

                Blender miyako = new Blender("Miyako Turbo Blender", 499000, 20, "Miyako");
                miyako.setDeskripsi("Miyako Turbo Blender adalah pilihan ekonomis yang andal untuk kebutuhan dapur sehari-hari. Dilengkapi tiga kecepatan dan fungsi pulse untuk hasil blender yang sesuai kebutuhan, cocok untuk membuat jus buah, smoothie, dan bumbu masak.");
                miyako.setSpesifikasi("Daya Motor: 350 Watt | Kecepatan: 2 Level + Pulse | Kapasitas Jar: 1.5 Liter (Plastik BPA Free) | Pisau: Stainless Steel 4 Mata | Fungsi: Blend, Chop, Grind | Keamanan: Anti-Slip | Garansi: 1 Tahun");
                miyako.setImageUrl("https://images.unsplash.com/photo-1578652414167-2856422f1839?q=80&w=400&auto=format&fit=crop");
                electroRepo.save(miyako);

                // KULKAS 
                Kulkas samsungSbs = new Kulkas("Samsung Side by Side 600L", 11999000, 4, "Samsung");
                samsungSbs.setDeskripsi("Samsung Side by Side 600L menghadirkan kemewahan dan fungsionalitas dalam satu lemari es berkapasitas besar. Dilengkapi fitur Twin Cooling Plus yang menjaga kesegaran makanan lebih lama dengan mengontrol kelembaban secara terpisah untuk freezer dan kulkas.");
                samsungSbs.setSpesifikasi("Kapasitas Total: 600 Liter | Tipe: Side by Side | Konfigurasi: Kulkas 357L + Freezer 243L | Teknologi: Twin Cooling Plus, Digital Inverter | Fitur: SpaceMax, All-Around Cooling, Ice Maker | Konsumsi Listrik: 420 kWh/tahun | Rating Bintang: 4 Star");
                samsungSbs.setImageUrl("https://images.unsplash.com/photo-1584568694244-14fbdf83bd30?q=80&w=400&auto=format&fit=crop");
                electroRepo.save(samsungSbs);

                Kulkas lgFrench = new Kulkas("LG French Door 540L", 9999000, 5, "LG");
                lgFrench.setDeskripsi("LG French Door 540L hadir dengan desain elegan dan kapasitas besar yang cocok untuk keluarga. Teknologi Door Cooling+ memastikan penyebaran udara dingin yang merata ke seluruh penjuru kulkas, menjaga kesegaran bahan makanan lebih optimal.");
                lgFrench.setSpesifikasi("Kapasitas Total: 540 Liter | Tipe: French Door | Konfigurasi: Kulkas 384L + Freezer 156L | Teknologi: Door Cooling+, Linear Cooling, Inverter Linear Compressor | Fitur: UVnano, Fresh Balancer, Slim Ice Maker | Konsumsi Listrik: 395 kWh/tahun | Rating Bintang: 4 Star");
                lgFrench.setImageUrl("https://images.unsplash.com/photo-1600585154340-be6161a56a0c?q=80&w=400&auto=format&fit=crop");
                electroRepo.save(lgFrench);

                // HEADPHONE
                Headphone sonyWh = new Headphone("Sony WH-1000XM5", 4999000, 10, "Sony");
                sonyWh.setDeskripsi("Sony WH-1000XM5 adalah headphone over-ear terbaik di kelasnya dengan teknologi noise cancelling paling canggih dari Sony. Ditenagai chip prosesor QN1 dan HD Noise Cancelling, headphone ini mampu memblokir suara luar secara efektif untuk pengalaman audio yang imersif.");
                sonyWh.setSpesifikasi("Tipe: Over-Ear Wireless | Driver: 30mm | Frekuensi: 4Hz - 40kHz | Noise Cancelling: 8 Mikrofon, QN1 Processor | Konektivitas: Bluetooth 5.2, NFC, 3.5mm Jack | Baterai: 30 jam (ANC On) | Fitur: Speak-to-Chat, LDAC, Multipoint | Berat: 250g");
                sonyWh.setImageUrl("https://images.unsplash.com/photo-1618366712010-f4ae9c647dcb?q=80&w=400&auto=format&fit=crop");
                electroRepo.save(sonyWh);

                Headphone boseQc = new Headphone("Bose QuietComfort 45", 5499000, 8, "Bose");
                boseQc.setDeskripsi("Bose QuietComfort 45 menghadirkan kenyamanan premium dengan teknologi noise cancelling ikonik dari Bose. Bantalan telinga yang lembut dan headband yang ringan membuatnya nyaman dipakai berjam-jam, cocok untuk perjalanan jauh dan bekerja dari rumah.");
                boseQc.setSpesifikasi("Tipe: Over-Ear Wireless | Driver: TriPort Acoustic | Frekuensi: 20Hz - 20kHz | Noise Cancelling: Quiet Mode & Aware Mode | Konektivitas: Bluetooth 5.1, 3.5mm Jack | Baterai: 24 jam | Fitur: SimpleSync, Voice Assistant, Multipoint | Berat: 238g");
                boseQc.setImageUrl("https://images.unsplash.com/photo-1505740420928-5e560c06d30e?q=80&w=400&auto=format&fit=crop");
                electroRepo.save(boseQc);
            }

            if (userRepo.count() == 0) {
                userService.register("admin", "admin@electro.com", "admin123");
                var admin = userRepo.findByUsername("admin").get();
                admin.setRole("ADMIN");
                userRepo.save(admin);
            }
        };
    }
}