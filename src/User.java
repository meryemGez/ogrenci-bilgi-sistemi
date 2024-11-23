public class User {
    public static void main(String[] args) {
        private String userID;
        private String password;
        private String name;
        private String role;

    public User(String userID, String password, String name, String role) {
            this.userID = userID;
            this.password = password;
            this.name = name;
            this.role = role;
        }

        public String getUserID () {
            return userID;
        }

        public String getPassword () {
            return password;
        }

        public String getName () {
            return name;
        }

        public String getRole () {
            return role;
        }

        @Override
        public String toString () {
            return name + " (" + role + ")";
        }
    }
}
