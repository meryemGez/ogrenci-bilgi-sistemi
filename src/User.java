import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
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

}

class Admin extends User {
    public Admin(String kullaniciID, String sifre, String ad) {
        super(kullaniciID, sifre, ad, "yonetici");
    }
}

class Ogrenci extends User {
    private String sinif;
    private String bolum;
    private double not;
    private boolean devamsizlik;

    public Ogrenci(String userID, String password, String name, String sinif, String bolum) {
        super(userID, password, name, "ogrenci");
        this.sinif = sinif;
        this.bolum = bolum;
        this.not = 0.0; // Başlangıçta not sıfır
        this.devamsizlik = false; // Başlangıçta devamsızlık yok
    }

    public String getSinif() {
        return sinif;
    }

    public String getBolum() {
        return bolum;
    }

    public double getNot() {
        return not;
    }

    public void setNot(double not) {
        this.not = not;
    }

    public boolean isDevamsizlik() {
        return devamsizlik;
    }

    public void setDevamsizlik(boolean devamsizlik) {
        this.devamsizlik = devamsizlik;
    }
}

class Ogretmen extends User {
    public Ogretmen(String kullaniciID, String sifre, String ad) {
        super(kullaniciID, sifre, ad, "ogretmen");
    }

    public void dersSaatiDuyurusu() {
        System.out.print("Ders saati duyurusu yapın: ");
        String duyuru = new Scanner(System.in).nextLine();
        Duyuru dersSaatiDuyurusu = new Duyuru(duyuru, this.getAdSoyad());
        StudentManagementSystem.duyurular.add(dersSaatiDuyurusu);
        System.out.println("Ders saati duyurusu yapıldı!");
    }
}

class Duyuru {
    private String mesaj;
    private String gonderen;

    public Duyuru(String mesaj, String gonderen) {
        this.mesaj = mesaj;
        this.gonderen = gonderen;
    }

    @Override
    public String toString() {
        return "Duyuru: " + mesaj + " (Gönderen: " + gonderen + ")";
    }
}

public class StudentManagementSystem {
    public static HashSet<Duyuru> duyurular;
    private static Map<String, User> userMap = new HashMap<>();
    private static List<Duyuru> Duyurular = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);
    private static final String DATA_FILE = "users.json";

    public static void main(String[] args) {
        loadUsers(); // JSON'dan kullanıcıları yükle

        boolean exit = false;
        while (!exit) {
            System.out.println("1. Kullanıcı Girişi");
            System.out.println("2. Çıkış");
            System.out.print("Seçiminizi yapın: ");
            int choice = -1; // İlk başta geçersiz bir değer atıyoruz
            while (choice == -1) {
                try {
                    System.out.print("İşleminizi seçin: ");
                    choice = Integer.parseInt(scanner.nextLine()); // Burada sayı parse etmeye çalışıyoruz
                } catch (NumberFormatException e) {
                    System.out.println("Geçersiz seçim! Lütfen geçerli bir sayısal değer girin.");
                }
            }

            switch (choice) {
                case 1:
                    loginUser();
                    break;
                case 2:
                    saveUsers(); // gsona kullanıcıları kaydeder
                    exit = true;
                    break;
                default:
                    System.out.println("Hatalı seçim! Tekrar deneyin.");
            }
        }
    }

    private static void loginUser() {
        System.out.print("Kullanıcı kimliği girin: ");
        String userID = scanner.nextLine();
        System.out.print("Şifre girin: ");
        String password = scanner.nextLine();

        User user = userMap.get(userID);
        if (user != null && user.getSifre().equals(password)) {
            System.out.println("Hoşgeldiniz, " + user.getAdSoyad());
            if (user instanceof Admin) {
                adminMenu((Admin) user);
            } else if (user instanceof Ogretmen) {
                teacherMenu((Ogretmen) user);
            } else if (user instanceof Ogrenci) {
                studentMenu((Ogrenci) user);
            }
        } else {
            System.out.println("Geçersiz kimlik veya şifre!");
        }
    }

    private static void adminMenu(Admin admin) {
        boolean exit = false;
        while (!exit) {
            System.out.println("1. Kullanıcı Ekle");
            System.out.println("2. Duyuru Yap");
            System.out.println("3. Çıkış");
            System.out.print("Seçiminizi yapın: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    addUser(admin);
                    break;
                case 2:
                    makeAnnouncement(admin);
                    break;
                case 3:
                    exit = true;
                    break;
                default:
                    System.out.println("Hatalı seçim! Tekrar deneyin.");
            }
        }
    }

    private static void addUser(Admin admin) {
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
                System.out.println("Geçersiz kullanıcı türü!");
                return;
        }

        userMap.put(userID, newUser);
        System.out.println("Kullanıcı başarıyla eklendi!");
    }

    private static void makeAnnouncement(Admin admin) {
        System.out.print("Duyuru mesajını girin: ");
        String message = scanner.nextLine();
        Duyuru duyuru = new Duyuru(message, admin.getAdSoyad());
        duyurular.add(duyuru);
        System.out.println("Duyuru başarıyla yapıldı!");
    }

    private static void teacherMenu(Ogretmen teacher) {
        boolean exit = false;
        while (!exit) {
            System.out.println("1. Not Girişi Yap");
            System.out.println("2. Devamsızlık Girişi Yap");
            System.out.println("3. Ders Saati Duyurusu Yap");
            System.out.println("4. Çıkış");
            System.out.print("Seçiminizi yapın: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    enterGrades(teacher);
                    break;
                case 2:
                    enterAttendance(teacher);
                    break;
                case 3:
                    teacher.dersSaatiDuyurusu();
                    break;
                case 4:
                    exit = true;
                    break;
                default:
                    System.out.println("Hatalı seçim! Tekrar deneyin.");
            }
        }
    }

    private static void enterGrades(Ogretmen teacher) {
        System.out.print("Not girişi yapacak öğrencinin kimliğini girin: ");
        String studentID = scanner.nextLine();
        Ogrenci student = (Ogrenci) userMap.get(studentID);
        if (student != null) {
            System.out.print("Notu girin: ");
            double grade = Double.parseDouble(scanner.nextLine());
            student.setNot(grade);
            System.out.println("Not başarıyla kaydedildi!");
        } else {
            System.out.println("Öğrenci bulunamadı!");
        }
    }

    private static void enterAttendance(Ogretmen teacher) {
        System.out.print("Devamsızlık girişi yapacak öğrencinin kimliğini girin: ");
        String studentID = scanner.nextLine();
        Ogrenci student = (Ogrenci) userMap.get(studentID);
        if (student != null) {
            System.out.print("Devamsızlık durumu (true/false): ");
            boolean attendance = Boolean.parseBoolean(scanner.nextLine());
            student.setDevamsizlik(attendance);
            System.out.println("Devamsızlık durumu başarıyla kaydedildi!");
        } else {
            System.out.println("Öğrenci bulunamadı!");
        }
    }

    private static void studentMenu(Ogrenci student) {
        System.out.println("Öğrenci menüsü henüz eklenmedi.");
    }

    private static void saveUsers() {
        try (Writer writer = new FileWriter(DATA_FILE)) {
            Gson gson = new Gson();
            gson.toJson(userMap, writer);
            System.out.println("Kullanıcılar başarıyla kaydedildi!");
        } catch (IOException e) {
            System.err.println("Kullanıcılar kaydedilemedi: " + e.getMessage());
        }
    }

    private static void loadUsers() {
        try (Reader reader = new FileReader(DATA_FILE)) {
            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, User>>() {}.getType();
            userMap = gson.fromJson(reader, type);
            if (userMap == null) userMap = new HashMap<>();
            System.out.println("Kullanıcılar başarıyla yüklendi!");
        } catch (FileNotFoundException e) {
            System.out.println("Veri dosyası bulunamadı. Yeni bir sistem başlatılıyor.");
            createInitialAdmin(); // İlk admin kullanıcıyı burada oluşturuyoruz
        } catch (IOException e) {
            System.err.println("Kullanıcılar yüklenemedi: " + e.getMessage());
        }
    }

    private static void createInitialAdmin() {
        // İlk admin kullanıcıyı oluşturuyoruz
        Admin admin = new Admin("admin1", "admin123", "Admin User");
        userMap.put(admin.getKullaniciKimligi(), admin);
        System.out.println("İlk admin kullanıcısı oluşturuldu: " + admin.getAdSoyad());
    }
}
