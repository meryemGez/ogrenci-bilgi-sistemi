import java.util.Scanner;

public class LoginUygulamasi {


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        //kullanici adi ve sifre girecek. 3.hatasinda sistem bloke olacak.
        //kullanici adi Mert sifre 789trm olsun

        String sifre = "789trm"; //ben suanlik  sifre  ve kullanici adi belirlenmis sekilde kullandim bunu yoneticilerin koymasini da saglayabiliriz cok sorun degil.
        String kullaniciAdi = "Mert";
        boolean aktiflik = true;

        if (aktiflik) {                            //kullanicinin 3 hakki oldugu icin aktiflik ksiminin bir yerde bitmesi ve hesabin bloke olmasi lazim
                                                    //aktifligi burda ister arttirin istterseniz de sey yapabiliriz kullanicinin hesabini tamamen bloke etme ve yoneticiyle iletisime gecip
                                                     // aktiflestirmesini saglayabiliriz. Ya da sure sayaci gibi bir sey varsa eger belli bir sure bekletip ondan sonra girmesini saglamaliyiz
            for (int i = 3; i > 0; i--) {

                    System.out.print("Lutfen kullanici adinizi giriniz : ");
                String kullanicidanAlinanIsim = scanner.nextLine();                 // burda kullanicidan sifre fln aliyoruz
                    System.out.print("Lutfen sifrenizi giriniz : ");
                String kullanicidanAlinanSifre = scanner.nextLine();


                if (kullaniciAdi.equals(kullanicidanAlinanIsim.trim()) && sifre.equals(kullanicidanAlinanSifre.trim())) {  //girdigi deger bizim belirledigimiz sifre ve kullanici adiyla eslesiyorsa
                                                                                                                            //if blogu calisir
                    System.out.println("Sisteme basarili bir sekilde giris yaptiniz.");
                    break;                                                                                                   //sisteme girdikten sonra bir daha girmenin mantigi yok o yzden break
                } else {
                    if ((i-1)==0){
                        aktiflik = false;                                                                                               // eger kullanicinin hakki 0 olmussa gecici ya da kalici olarak bloke edilmesi lazim. ona karar verelim
                        System.out.println("Cok fazla hatali giris yaptiniz. Lutfen daha sonra tekrar deneyiniz.");
                        break;                                                                                                          //burdada eger hakki tukendiyse asagidakilari dondurmesin diye break firlattim bacilar
                    }
                    System.out.println("Kullanici adi veya sifreyi yanlis girdiniz! " + (i - 1) + " hakkiniz kalmistir.");

                }
            }


        }
//    public static boolean veriler(String sifre, String kullaniciAdi){      // bunu methodlarla alakali yazmaya caliscam daha temiz ve anlsilir duruyor


    }
}

