import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Transaction {
    private String transactionId;
    private Member member;
    private Book book;
    private String borrowDate;
    private String dueDate;
    private String returnDate;
    private int daysLate;
    private double lateFee;

    private static int totalTransactions = 0;
    public static final double LATE_FEE_PER_DAY = 2000.0;
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd-MM-yyyy");


    public static String simulationDate = "01-12-2025";

    public Transaction(Member member, Book book, String borrowDate, int borrowDurationDays) {
        if(!book.borrowBook()) {
            throw new IllegalStateException("Error: Buku tidak tersedia untuk dipinjam");
        }

        totalTransactions++;
        this.transactionId = String.format("TRX%03d", totalTransactions);
        this.member = member;
        this.book = book;
        this.borrowDate = borrowDate;

        LocalDate start = LocalDate.parse(borrowDate, FMT);
        this.dueDate = start.plusDays(borrowDurationDays).format(FMT);

        member.incrementTransaction();
    }

    public void processReturn(String returnDate) {
        this.returnDate = returnDate;
        LocalDate due = LocalDate.parse(dueDate, FMT);
        LocalDate ret = LocalDate.parse(returnDate, FMT);

        long days = ChronoUnit.DAYS.between(due, ret);
        this.daysLate = (days > 0) ? (int) days : 0;

        calculateLateFee();
        book.returnBook();
    }

    private void calculateLateFee() {
        if (daysLate > 0) {
            double grossFee = daysLate * LATE_FEE_PER_DAY;
            double discount = grossFee * member.getMembershipDiscount();
            this.lateFee = grossFee - discount;
        } else {
            this.lateFee = 0;
        }
    }

    public void displayTransaction() {
        System.out.println("[" + transactionId + "] " + getStatusHeader());
        System.out.println("Peminjam      : " + member.getName() + " (" + member.getMemberId() + ") - " + member.getMembershipType());
        System.out.println("Buku          : " + book.getTitle() + " (" + book.getBookId() + ")");
        System.out.println("Tgl Pinjam    : " + borrowDate);
        System.out.println("Tgl Tempo     : " + dueDate);

        if (returnDate != null) {
            // Finished Transaction
            System.out.println("Tgl Kembali   : " + returnDate);
            System.out.println("Terlambat     : " + daysLate + " hari");
            if (daysLate > 0) {
                System.out.println("Denda         : Rp " + (int)lateFee + " (Rp " + (int)(daysLate*LATE_FEE_PER_DAY) + " - diskon " + (int)(member.getMembershipDiscount()*100) + "%)");
            } else {
                System.out.println("Denda         : Rp 0");
            }
        } else {

            LocalDate due = LocalDate.parse(dueDate, FMT);

            LocalDate ref = LocalDate.parse(simulationDate, FMT);
            long daysLeft = ChronoUnit.DAYS.between(ref, due);

            System.out.println("Status        : Masih Dipinjam (" + daysLeft + " hari lagi)");
        }
        System.out.println("--------------------------------------------");
    }

    public String getStatusHeader() {
        if (returnDate != null) {
            return daysLate > 0 ? "SELESAI - TERLAMBAT ⚠️" : "SELESAI - TEPAT WAKTU ✓";
        }
        return "AKTIF";
    }


    public Member getMember() { return member; }
    public Book getBook() { return book; }
    public double getLateFee() { return lateFee; }
    public boolean isReturned() { return returnDate != null; }
    public boolean isLate() { return daysLate > 0; }

    public static int getTotalTransactions() { return totalTransactions; }
}