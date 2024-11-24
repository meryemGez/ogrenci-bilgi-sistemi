public class kullaniciGirisi {
    public static void main(String[] args) {
        private String kullaniciID;
        private String sifre;
        private String isim;
        private String role;

    public User(String kullaniciID, String password, String name, String role) {
            this.kullaniciID = kullaniciID;
            this.sifre = sifre;
            this.isim = isim;
            this.role = role;
        }

        public String getkulllaniciID () {
            return kullaniciID;
        }

        public String getsifre () {
            return sifre;
        }

        public String getisim () {
            return isim;
        }

        public String getRole () {
            return role;
        }

        @Override
        public String toString () {
            return isim + " (" + role + ")";
        }
    }
}
