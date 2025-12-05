public class Book {
    private String bookId;
    private String title;
    private String author;
    private String category;
    private int publicationYear;
    private boolean isAvailable;
    private int totalCopies;
    private int availableCopies;

    private static int totalBooks = 0;

    public Book(String title, String author, String category, int publicationYear, int totalCopies) {
        totalBooks++;
        this.bookId = String.format("BK%03d", totalBooks);


        if(publicationYear < 1900 || publicationYear > 2025) throw new IllegalArgumentException("Error: Tahun terbit tidak valid (1900-2025)");

        this.title = title;
        this.author = author;
        this.category = category;
        this.publicationYear = publicationYear;
        this.totalCopies = totalCopies;
        this.availableCopies = totalCopies;
        this.isAvailable = totalCopies > 0;
    }

    public void displayBookInfo() {
        System.out.println("[" + bookId + "] " + title);
        System.out.println("Penulis       : " + author);
        System.out.println("Kategori      : " + category);
        System.out.println("Tahun Terbit  : " + publicationYear);
        System.out.println("Umur Buku     : " + getBookAge() + " tahun");
        System.out.println("Total Copy    : " + totalCopies + " eksemplar");

        String newReleaseTag = isNewRelease() ? " [NEW RELEASE]" : "";
        System.out.println("Tersedia      : " + availableCopies + " eksemplar | Status: " + getAvailabilityStatus() + newReleaseTag);
        System.out.println("--------------------------------------------");
    }

    public boolean borrowBook() {
        if (availableCopies > 0) {
            availableCopies--;
            isAvailable = availableCopies > 0;
            return true;
        }
        return false;
    }

    public void returnBook() {
        if (availableCopies < totalCopies) {
            availableCopies++;
            isAvailable = true;
        }
    }

    public int getBookAge() {
        return 2025 - publicationYear;
    }

    public boolean isNewRelease() {

        return getBookAge() <= 7;
    }

    public String getAvailabilityStatus() {
        if (availableCopies == 0) return "Tidak Tersedia";
        if (availableCopies <= 5) return "Terbatas ⚠️";
        return "Banyak Tersedia ✓";
    }

    public static int getTotalBooks() { return totalBooks; }


    public String getBookId() { return bookId; }
    public String getTitle() { return title; }
    public String getCategory() { return category; }
}