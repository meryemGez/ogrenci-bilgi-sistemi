import java.util.Scanner;

public class LoginUygulamasiRe{
    static int haksayisi = 3;             //haksayisina her metotta erisebilmek icin class icinde tanimliyoruz.

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
            System.out.println("yoneticiymis gibi  isim belirleyin");
        String isim = input.nextLine();
            System.out.println("yoneticiymis gibi sifre belirleyin");
        String sifre = input.nextLine();

        boolean aktiflik = true;                    //ilk basta kullanicinin hakklari halihazirda var old icin true

        if(aktiflik){                          //aktiflik dogruysa calistirio
                                                     //biizm iki temel asamamiz var kullanicinin hakki var mi yok mu eger varsa girdigi sonuclar dogru mu yanlis  mi. bu kdr
            if(haksayisi>0) {
                while (aktiflik) {                                                         // yanlis girerse diye degeri tekrar almak icin  while
                    boolean sonuc = LoginUygulamasi(isim, sifre);
                    if (sonuc) {
                        System.out.println("Basarili bir sekilde giris yaptiniz.");
                        break;
                    } else {
                        if(haksayisi==0) {
                                System.out.println("Cok fazla basarisiz deneme lutfen daha sonra tekrar deneyiniz!!!");
                                break;
                        }
                        System.out.println("Yanlis kullanici adi veya sifre. " + haksayisi + "  hakkiniz kalmistir!!!");
                        continue;
                    }
                }
            }

        }
    }

    public static boolean LoginUygulamasi(String username, String password){

        Scanner scanner = new Scanner(System.in);
            System.out.print("Kullanici adiniz : ");
        String kullanicidanAlinanIsim = scanner.nextLine();
            System.out.print("Sifreniz : ");
        String kullanicidanAlinanSifre = scanner.nextLine();      //BU METHOD ALLAHIN BELASI BIR KISIM. SIRF YONTICIDEN ISIM VE SIFRE ALMAK ICIN SCANNERI BURAYA ATTIM
        if(username.equals(kullanicidanAlinanIsim.trim()) && password.equals(kullanicidanAlinanSifre.trim())){ //iste esitse dogru fonuyor
            return true;
        }else {
            haksayisi--;                                                               //yanlissa yanlis donuyor ve hak sayisi 1 eksiliyor
            return false;
        }
    }

}