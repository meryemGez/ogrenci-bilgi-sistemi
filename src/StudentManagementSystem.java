import java.util.*;

class User {
    protected String kullaniciKimligi;
    protected String sifre;
    protected String adSoyad;
    protected String rutbe;

    public User(String kullaniciID, String sifre, String ad, String rutbe) {
        this.kullaniciKimligi = kullaniciID;
        this.sifre = sifre;
        this.adSoyad = ad;
        this.rutbe = rutbe;
    }

    public String getKullaniciKimligi() {
        return kullaniciKimligi;
    }

    public String getSifre() {
        return sifre;
    }

    public String getAdSoyad() {
        return adSoyad;
    }

    public String getRutbe() {
        return rutbe;
    }

    @Override
    public String toString() {
        return adSoyad + " (" + rutbe + ")";
    }
}

class Duyuru {
    private String mesaj;
    private String gonderen;

    public Duyuru(String mesaj, String gonderen) {
        this.mesaj = mesaj;
        this.gonderen = gonderen;
    }

    public String getMesaj() {
        return mesaj;
    }

    public String getGonderen() {
        return gonderen;
    }

    @Override
    public String toString() {
        return "Duyuru: " + mesaj + " (Gönderen: " + gonderen + ")";
    }
}

class Ogrenci extends User {
    private String sinif;
    private String bolum;

    public Ogrenci(String userID, String password, String name, String sinif, String bolum) {
        super(userID, password, name, "ogrenci");
        this.sinif = sinif;
        this.bolum = bolum;
    }

    public String getSinif() {
        return sinif;
    }

    public String getBolum() {
        return bolum;
    }

    public void setSinif(String sinif) {
        this.sinif = sinif;
    }

    public void setBolum(String bolum) {
        this.bolum = bolum;
    }
}

class Ogretmen extends User {
    public Ogretmen(String kullaniciID, String sifre, String ad) {
        super(kullaniciID, sifre, ad, "ogretmen");
    }

    public void duyuruYap(String mesaj, List<Duyuru> duyurular) {
        duyurular.add(new Duyuru(mesaj, this.getAdSoyad()));
    }

    public void notGir(Ogrenci ogrenci, String dersID, double notDegeri) {
        System.out.println("Öğrenci ID: " + ogrenci.getKullaniciKimligi() + ", Ders ID: " + dersID + ", Not: " + notDegeri);
    }
}

class Admin extends User {
    private List<User> users;

    public Admin(String kullaniciID, String sifre, String ad) {
        super(kullaniciID, sifre, ad, "yonetici");
        users = new ArrayList<>();
    }

    public void kullaniciEkle(User kullanici) {
        users.add(kullanici);
    }

    public void duyuruYap(String mesaj, List<Duyuru> duyurular) {
        duyurular.add(new Duyuru(mesaj, this.getAdSoyad()));
    }
}

public class StudentManagementSystem {
    private static Map<String, User> userMap = new HashMap<>();
    private static List<Duyuru> duyurular = new ArrayList<>();
    private static List<String> ogrenciListesi = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);
    static int haksayisi = 3;

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("yoneticiymis gibi isim belirleyin");
        String isim = input.nextLine();
        System.out.println("yoneticiymis gibi sifre belirleyin");
        String sifre = input.nextLine();

        boolean aktiflik = true;

        if (aktiflik) {
            if (haksayisi > 0) {
                while (aktiflik) {
                    boolean sonuc = loginControl(isim, sifre);
                    if (sonuc) {
                        System.out.println("Basarili bir sekilde giris yaptiniz.");
                        break;
                    } else {
                        if (haksayisi == 0) {
                            System.out.println("Cok fazla basarisiz deneme lutfen daha sonra tekrar deneyiniz!!!");
                            break;
                        }
                        System.out.println("Yanlis kullanici adi veya sifre. " + haksayisi + " hakkiniz kalmistir!!!");
                        continue;
                    }
                }
            }
        }

        // Initialize system with predefined admin
        Admin admin = new Admin("admin1", "admin123", "Admin User");
        userMap.put(admin.getKullaniciKimligi(), admin);

        boolean exit = false;
        while (!exit) {
            System.out.println("1. Sisteme Giriş Yap");
            System.out.println("2. Çıkış");
            System.out.print("Lütfen işleminizi seçin: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    loginUser();
                    break;
                case 2:
                    exit = true;
                    break;
                default:
                    System.out.println("Hata! Lütfen tekrar deneyin.");
            }
        }
    }

    public static boolean loginControl(String username, String password) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Kullanici adiniz: ");
        String kullanicidanAlinanIsim = scanner.nextLine();
        System.out.print("Sifreniz: ");
        String kullanicidanAlinanSifre = scanner.nextLine();
        if (username.equals(kullanicidanAlinanIsim.trim()) && password.equals(kullanicidanAlinanSifre.trim())) {
            return true;
        } else {
            haksayisi--;
            return false;
        }
    }

    private static void loginUser() {
        System.out.print("Kullanıcı kimliği girin: ");
        String userID = scanner.nextLine();
        System.out.print("Şifre girin: ");
        String password = scanner.nextLine();

        User user = userMap.get(userID);
        if (user != null && user.getSifre().equals(password)) {
            System.out.println("HOŞGELDİN!, " + user.getAdSoyad());
            switch (user.getRutbe()) {
                case "yonetici":
                    adminMenu((Admin) user);
                    break;
                case "ogretmen":
                    teacherMenu((Ogretmen) user);
                    break;
                case "ogrenci":
                    studentMenu((Ogrenci) user);
                    break;
                default:
                    System.out.println("Geçersiz yetki seviyesi.");
            }
        } else {
            System.out.println("Geçersiz, lütfen tekrar deneyin.");
        }
    }

    private static void adminMenu(Admin yonetici) {
        boolean exit = false;
        while (!exit) {
            System.out.println("1. Kullanıcı ekle");
            System.out.println("2. Duyuru yayınla");
            System.out.println("3. Çıkış");
            System.out.print("İşleminizi seçiniz: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    addUser(yonetici);
                    break;
                case 2:
                    makeAnnouncement(yonetici);
                    break;
                case 3:
                    exit = true;
                    break;
                default:
                    System.out.println("Hata! Tekrar deneyin.");
            }
        }
    }

    private static void addUser(Admin yonetici) {
        System.out.print("Kullanıcı Türü (ogrenci/ogretmen/yonetici): ");
        String userType = scanner.nextLine();
        System.out.print("Kullanıcı Kimliği: ");
        String userID = scanner.nextLine();
        System.out.print("Şifre: ");
        String password = scanner.nextLine();
        System.out.print("Ad Soyad: ");
        String name = scanner.nextLine();

        User newUser;
        switch (userType) {
            case "ogrenci":
                System.out.print("Sınıf: ");
                String sinif = scanner.nextLine();
                System.out.print("Bölüm: ");
                String bolum = scanner.nextLine();
                newUser = new Ogrenci(userID, password, name, sinif, bolum);
                break;
            case "ogretmen":
                newUser = new Ogretmen(userID, password, name);
                break;
            case "yonetici":
                newUser = new Admin(userID, password, name);
                break;
            default:
                System.out.println("Geçersiz kullanıcı türü.");
                return;
        }

        yonetici.kullaniciEkle(newUser);
        userMap.put(userID, newUser);
        System.out.println("Kullanıcı başarıyla eklendi!");
    }

    private static void makeAnnouncement(Admin yonetici) {
        System.out.print("Duyuru mesajını girin: ");
        String message = scanner.nextLine();
        yonetici.duyuruYap(message, duyurular);
        System.out.println("Duyuru başarıyla yapıldı!");
    }

    private static void teacherMenu(Ogretmen ogretmen) {
        boolean exit = false;
        while (!exit) {
            System.out.println("1. Duyuru Yap");
            System.out.println("2. Öğrencileri Görüntüle");
            System.out.println("3. Not Gir");
            System.out.println("4. Çıkış");
            System.out.print("İşleminizi seçiniz: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    makeAnnouncement(ogretmen);
                    break;
                case 2:
                    viewStudents();
                    break;
                case 3:
                    notGir(ogretmen);
                    break;
                case 4:
                    exit = true;
                    break;
                default:
                    System.out.println("Hata! Tekrar deneyin.");
            }
        }
    }

    private static void makeAnnouncement(Ogretmen ogretmen) {
        System.out.print("Duyuru mesajını girin: ");
        String message = scanner.nextLine();
        ogretmen.duyuruYap(message, duyurular);
        System.out.println("Duyuru başarıyla yapıldı!");
    }

    private static void viewStudents() {
        for (User user : userMap.values()) {
            if (user instanceof Ogrenci) {
                Ogrenci ogrenci = (Ogrenci) user;
                System.out.println(ogrenci.getAdSoyad() + " - " + ogrenci.getSinif() + " (" + ogrenci.getBolum() + ")");
            }
        }
    }

    private static void notGir(Ogretmen ogretmen) {
        System.out.print("Öğrenci ID: ");
        String studentID = scanner.nextLine();
        Ogrenci ogrenci = (Ogrenci) userMap.get(studentID);
        if (ogrenci != null) {
            System.out.print("Ders ID: ");
            String dersID = scanner.nextLine();
            System.out.print("Not: ");
            double notDegeri = Double.parseDouble(scanner.nextLine());
            ogretmen.notGir(ogrenci, dersID, notDegeri);
            System.out.println("Not başarıyla girildi!");
        } else {
            System.out.println("Öğrenci bulunamadı.");
        }
    }

    private static void studentMenu(Ogrenci ogrenci) {
        System.out.println("Sınıfınız: " + ogrenci.getSinif() + ", Bölümünüz: " + ogrenci.getBolum());
        boolean exit = false;
        while (!exit) {
            System.out.println("1. Duyuruları Görüntüle");
            System.out.println("2. Çıkış");
            System.out.print("İşleminizi seçiniz: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    viewAnnouncements();
                    break;
                case 2:
                    exit = true;
                    break;
                default:
                    System.out.println("Hata! Tekrar deneyin.");
            }
        }
    }

    private static void viewAnnouncements() {
        for (Duyuru duyuru : duyurular) {
            System.out.println(duyuru);
        }
    }
}


    import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.*;
        import java.lang.reflect.Type;
import java.util.*;

class User {
    // Mevcut özellikler ve metodlar buraya gelecek
}

class Duyuru {
    // Mevcut özellikler ve metodlar buraya gelecek
}

class Ogrenci extends User {
    // Mevcut özellikler ve metodlar buraya gelecek
}

class Ogretmen extends User {
    // Mevcut özellikler ve metodlar buraya gelecek
}

class Admin extends User {
    // Mevcut özellikler ve metodlar buraya gelecek
}

public class StudentManagementSystem {
    private static Map<String, User> userMap = new HashMap<>();
    private static List<Duyuru> duyurular = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);
    private static final String FILE_NAME = "users.json";
    private static final String ANNOUNCEMENTS_FILE = "announcements.json";
    static int haksayisi = 3;

    public static void main(String[] args) {
        loadData();
        // Mevcut kodlar burada yer alacak

        // Verileri kayıt etmek için bu kısmı kullanın:
        saveData();
    }

    private static void saveData() {
        Gson gson = new Gson();
        try (Writer writer = new FileWriter(FILE_NAME)) {
            gson.toJson(userMap, writer);
            System.out.println("Kullanıcı verileri başarıyla kaydedildi.");
        } catch (IOException e) {
            System.out.println("Kullanıcı verileri kaydedilirken hata oluştu: " + e.getMessage());
        }
        try (Writer writer = new FileWriter(ANNOUNCEMENTS_FILE)) {
            gson.toJson(duyurular, writer);
            System.out.println("Duyuru verileri başarıyla kaydedildi.");
        } catch (IOException e) {
            System.out.println("Duyuru verileri kaydedilirken hata oluştu: " + e.getMessage());
        }
    }

    private static void loadData() {
        Gson gson = new Gson();
        try (Reader reader = new FileReader(FILE_NAME)) {
            Type userType = new TypeToken<Map<String, User>>() {}.getType();
            userMap = gson.fromJson(reader, userType);
            System.out.println("Kullanıcı verileri başarıyla yüklendi.");
        } catch (FileNotFoundException e) {
            System.out.println("Kullanıcı verileri dosyası bulunamadı, yeni bir dosya oluşturulacak.");
        } catch (IOException e) {
            System.out.println("Kullanıcı verileri yüklenirken hata oluştu: " + e.getMessage());
        }
        try (Reader reader = new FileReader(ANNOUNCEMENTS_FILE)) {
            Type announcementType = new TypeToken<List<Duyuru>>() {}.getType();
            duyurular = gson.fromJson(reader, announcementType);
            System.out.println("Duyuru verileri başarıyla yüklendi.");
        } catch (FileNotFoundException e) {
            System.out.println("Duyuru verileri dosyası bulunamadı, yeni bir dosya oluşturulacak.");
        } catch (IOException e) {
            System.out.println("Duyuru verileri yüklenirken hata oluştu: " + e.getMessage());
        }
    }

    // Mevcut metodlar burada yer alacak

    private static void adminMenu(Admin yonetici) {
        boolean exit = false;
        while (!exit) {
            System.out.println("1. Kullanıcı ekle");
            System.out.println("2. Duyuru yayınla");
            System.out.println("3. Çıkış");
            System.out.print("İşleminizi seçiniz: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    addUser(yonetici);
                    break;
                case 2:
                    makeAnnouncement(yonetici);
                    break;
                case 3:
                    saveData();
                    exit = true;
                    break;
                default:
                    System.out.println("Hata! Tekrar deneyin.");
            }
        }
    }

    // Diğer metodlar burada yer alacak ve saveData() çağrıları eklenecek
}

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.*;
        import java.lang.reflect.Type;
import java.util.*;

class User {
    // Mevcut özellikler ve metodlar buraya gelecek
}

class Duyuru {
    // Mevcut özellikler ve metodlar buraya gelecek
}

class Ogrenci extends User {
    // Mevcut özellikler ve metodlar buraya gelecek
}

class Ogretmen extends User {
    // Mevcut özellikler ve metodlar buraya gelecek
}

class Admin extends User {
    // Mevcut özellikler ve metodlar buraya gelecek
}

public class StudentManagementSystem {
    private static Map<String, User> userMap = new HashMap<>();
    private static List<Duyuru> duyurular = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);
    private static final String FILE_NAME = "users.json";
    private static final String ANNOUNCEMENTS_FILE = "announcements.json";
    static int haksayisi = 3;

    public static void main(String[] args) {
        loadData();
        // Mevcut kodlar burada yer alacak

        // Verileri kayıt etmek için bu kısmı kullanın:
        saveData();
    }

    private static void saveData() {
        Gson gson = new Gson();
        try (Writer writer = new FileWriter(FILE_NAME)) {
            gson.toJson(userMap, writer);
            System.out.println("Kullanıcı verileri başarıyla kaydedildi.");
        } catch (IOException e) {
            System.out.println("Kullanıcı verileri kaydedilirken hata oluştu: " + e.getMessage());
        }
        try (Writer writer = new FileWriter(ANNOUNCEMENTS_FILE)) {
            gson.toJson(duyurular, writer);
            System.out.println("Duyuru verileri başarıyla kaydedildi.");
        } catch (IOException e) {
            System.out.println("Duyuru verileri kaydedilirken hata oluştu: " + e.getMessage());
        }
    }

    private static void loadData() {
        Gson gson = new Gson();
        try (Reader reader = new FileReader(FILE_NAME)) {
            Type userType = new TypeToken<Map<String, User>>() {}.getType();
            userMap = gson.fromJson(reader, userType);
            System.out.println("Kullanıcı verileri başarıyla yüklendi.");
        } catch (FileNotFoundException e) {
            System.out.println("Kullanıcı verileri dosyası bulunamadı, yeni bir dosya oluşturulacak.");
        } catch (IOException e) {
            System.out.println("Kullanıcı verileri yüklenirken hata oluştu: " + e.getMessage());
        }
        try (Reader reader = new FileReader(ANNOUNCEMENTS_FILE)) {
            Type announcementType = new TypeToken<List<Duyuru>>() {}.getType();
            duyurular = gson.fromJson(reader, announcementType);
            System.out.println("Duyuru verileri başarıyla yüklendi.");
        } catch (FileNotFoundException e) {
            System.out.println("Duyuru verileri dosyası bulunamadı, yeni bir dosya oluşturulacak.");
        } catch (IOException e) {
            System.out.println("Duyuru verileri yüklenirken hata oluştu: " + e.getMessage());
        }
    }

    // Mevcut metodlar burada yer alacak

    private static void adminMenu(Admin yonetici) {
        boolean exit = false;
        while (!exit) {
            System.out.println("1. Kullanıcı ekle");
            System.out.println("2. Duyuru yayınla");
            System.out.println("3. Çıkış");
            System.out.print("İşleminizi seçiniz: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    addUser(yonetici);
                    break;
                case 2:
                    makeAnnouncement(yonetici);
                    break;
                case 3:
                    saveData();
                    exit = true;
                    break;
                default:
                    System.out.println("Hata! Tekrar deneyin.");
            }
        }
    }

    // Diğer metodlar burada yer alacak ve saveData() çağrıları eklenecek
}
