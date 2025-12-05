import java.time.LocalDate;
import java.util.regex.Pattern;

public class Member {
    private String memberId;
    private String name;
    private String email;
    private String phoneNumber;
    private int registrationYear;
    private String membershipType;


    private int transactionCount = 0;

    private static int totalMembers = 0;

    public Member(String name, String email, String phoneNumber, int registrationYear, String membershipType) {
        totalMembers++;
        this.memberId = String.format("MBR%03d", totalMembers);


        setName(name);
        setEmail(email);
        setPhoneNumber(phoneNumber);
        setRegistrationYear(registrationYear);
        setMembershipType(membershipType);
    }



    public void incrementTransaction() {
        this.transactionCount++;
    }

    public int getTransactionCount() {
        return transactionCount;
    }

    public void upgradeMembership(String newType) {
        if (!isValidMembershipType(newType)) {
            System.out.println("✗ Error: Membership type tidak valid");
            return;
        }


        if (this.membershipType.equals("Silver") && newType.equals("Gold")) {
            this.membershipType = "Gold";
            System.out.println("✓ " + name + " berhasil di-upgrade dari Silver ke Gold!");
            System.out.println("  Batas Pinjam Baru: " + getMaxBorrowLimit() + " buku | Diskon Denda Baru: " + (int)(getMembershipDiscount()*100) + "%");
        }
    }

    public void displayInfo() {
        System.out.println("[" + memberId + "] " + name);
        System.out.println("Email         : " + email);
        System.out.println("Phone         : " + phoneNumber);
        System.out.println("Membership    : " + membershipType + getStarRating());
        System.out.println("Tahun Daftar  : " + registrationYear);
        System.out.println("Durasi Member : " + getMembershipDuration() + " tahun");
        System.out.println("Batas Pinjam  : " + getMaxBorrowLimit() + " buku");
        System.out.println("Diskon Denda  : " + (int)(getMembershipDiscount() * 100) + "%");
        System.out.println("--------------------------------------------");
    }

    private String getStarRating() {
        switch (membershipType) {
            case "Platinum": return " ⭐⭐⭐";
            case "Gold": return " ⭐⭐";
            case "Silver": return " ⭐";
            default: return "";
        }
    }

    public int getMaxBorrowLimit() {
        switch (membershipType) {
            case "Platinum": return 10;
            case "Gold": return 7;
            case "Silver": return 5;
            default: return 5;
        }
    }

    public int getMembershipDuration() {
        return 2025 - registrationYear;
    }

    public double getMembershipDiscount() {
        switch (membershipType) {
            case "Platinum": return 0.50;
            case "Gold": return 0.30;
            case "Silver": return 0.10;
            default: return 0.0;
        }
    }

    public static int getTotalMembers() { return totalMembers; }


    public String getMemberId() { return memberId; }
    public String getName() { return name; }
    public String getMembershipType() { return membershipType; }

    public void setName(String name) { this.name = name; }

    public void setEmail(String email) {
        if (email == null || !email.contains("@") || !email.contains(".")) {
            throw new IllegalArgumentException("Error: Email tidak valid (harus mengandung @ dan .)");
        }
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || !Pattern.matches("\\d{10,13}", phoneNumber)) {
            throw new IllegalArgumentException("Error: Nomor telepon harus 10-13 digit");
        }
        this.phoneNumber = phoneNumber;
    }

    public void setRegistrationYear(int registrationYear) {
        this.registrationYear = registrationYear;
    }

    public void setMembershipType(String membershipType) {
        if (!isValidMembershipType(membershipType)) throw new IllegalArgumentException("Error: Membership type harus Silver/Gold/Platinum");
        this.membershipType = membershipType;
    }

    private boolean isValidMembershipType(String type) {
        return type != null && (type.equals("Silver") || type.equals("Gold") || type.equals("Platinum"));
    }
}