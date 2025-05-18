import java.util.*;

class Book {
    private String title;
    private String author;
    private String isbn;
    private boolean isIssued;

    public Book(String title, String author, String isbn) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.isIssued = false;
    }

    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getIsbn() { return isbn; }
    public boolean isIssued() { return isIssued; }

    public void issue() { isIssued = true; }
    public void returnBook() { isIssued = false; }

    public String toString() {
        return title + " by " + author + " (ISBN: " + isbn + ") - " + (isIssued ? "Issued" : "Available");
    }
}

abstract class User {
    protected String name;
    protected String userId;

    public User(String name, String userId) {
        this.name = name;
        this.userId = userId;
    }

    public abstract String getRole();
}

class Member extends User {
    public Member(String name, String userId) {
        super(name, userId);
    }

    @Override
    public String getRole() {
        return "Member";
    }

    public String toString() {
        return name + " (" + userId + ")";
    }
}

class Library {
    private List<Book> books = new ArrayList<>();
    private Map<String, Member> members = new HashMap<>();
    private Map<String, List<Book>> issuedBooks = new HashMap<>();

    public void addBook(Book book) {
        books.add(book);
    }
    public boolean isIsbnExists(String isbn) {
    for (Book book : books) {
        if (book.getIsbn().equalsIgnoreCase(isbn)) {
            return true;  // ISBN already exists
        }
    }
    return false;
}
    public void registerMember(Member member) {
        members.put(member.userId, member);
    }

    public void issueBook(String isbn, String memberId) {
        Optional<Book> bookOpt = books.stream()
            .filter(b -> b.getIsbn().equals(isbn) && !b.isIssued())
            .findFirst();

        if (bookOpt.isPresent() && members.containsKey(memberId)) {
            Book book = bookOpt.get();
            book.issue();
            issuedBooks.putIfAbsent(memberId, new ArrayList<>());
            issuedBooks.get(memberId).add(book);
            System.out.println("Book issued to " + memberId);
        } else {
            System.out.println("Cannot issue book. Either unavailable or member not found.");
        }
    }

    public void returnBook(String isbn, String memberId) {
        if (issuedBooks.containsKey(memberId)) {
            List<Book> booksList = issuedBooks.get(memberId);
            for (Book book : booksList) {
                if (book.getIsbn().equals(isbn)) {
                    book.returnBook();
                    booksList.remove(book);
                    System.out.println("Book returned.");
                    return;
                }
            }
        }
        System.out.println("Book not found for this member.");
    }

    public void listAvailableBooks() {
        System.out.println("\nAvailable Books:");
        for (Book book : books) {
            if (!book.isIssued()) {
                System.out.println(book);
            }
        }
    }

    public void listIssuedBooks() {
        System.out.println("\nIssued Books:");
        for (Map.Entry<String, List<Book>> entry : issuedBooks.entrySet()) {
            System.out.println("Member ID: " + entry.getKey());
            for (Book book : entry.getValue()) {
                System.out.println("  - " + book);
            }
        }
    }

    public void listMembers() {
        System.out.println("\nRegistered Members:");
        for (Member member : members.values()) {
            System.out.println(member);
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Library library = new Library();
        Scanner sc = new Scanner(System.in);

        // Add some books and members
        library.addBook(new Book("Clean Code", "Robert C. Martin", "ISBN001"));
        library.addBook(new Book("Effective Java", "Joshua Bloch", "ISBN002"));
        library.addBook(new Book("Design Patterns", "Erich Gamma", "ISBN003"));

        library.registerMember(new Member("Alice", "MEM001"));
        library.registerMember(new Member("Bob", "MEM002"));
        
        System.out.println("\n--- Library Menu ---");
        System.out.println("1. List Available Books");
        System.out.println("2. List Issued Books");
        System.out.println("3. Register New Member");
        System.out.println("4. Issue a Book");
        System.out.println("5. Return a Book");
        System.out.println("6. List Members");
        System.out.println("0. Exit");
        int choice;
        do {
           
            System.out.print("Enter choice: ");
            choice = sc.nextInt();
            sc.nextLine();  // consume newline

            switch (choice) {
                case 1:
                    library.listAvailableBooks();
                    break;
                case 2:
                    library.listIssuedBooks();
                    break;
                case 3:
                    System.out.print("Enter name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter user ID: ");
                    String id = sc.nextLine();
                    library.registerMember(new Member(name, id));
                    break;
                case 4:
                    System.out.print("Enter ISBN: ");
                    String issueIsbn = sc.nextLine();
                    System.out.print("Enter Member ID: ");
                    String issueId = sc.nextLine();
                    library.issueBook(issueIsbn, issueId);
                    break;
                case 5:
                    System.out.print("Enter ISBN: ");
                    String returnIsbn = sc.nextLine();
                    System.out.print("Enter Member ID: ");
                    String returnId = sc.nextLine();
                    library.returnBook(returnIsbn, returnId);
                    break;
                case 6:
                    library.listMembers();
                    break;
                case 7:
                 System.out.print("Enter Book Title: ");
                 String title = sc.nextLine();
                 System.out.print("Enter Book Author: ");
                 String author = sc.nextLine();
                 System.out.print("Enter Book ISBN: ");
                 String isbn = sc.nextLine();

                 if (library.isIsbnExists(isbn)) {
                  System.out.println("A book with this ISBN already exists. Try a different one.");
                   }
                 else {
                   library.addBook(new Book(title, author, isbn));
                  System.out.println("Book added successfully.");
                  }
                 break;

                case 0:
                    System.out.println("Thank you for using the Library System!");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        } while (choice != 0);

        sc.close();
    }
}
