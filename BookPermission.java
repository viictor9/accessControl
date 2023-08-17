import java.io.*;
import java.security.*;
import java.util.*;

class BookPermission extends BasicPermission implements Serializable {
    String bookid = null;
    BitSet pages = new BitSet();

    public BookPermission(String perm) {
        super(perm);
        String[] fields = perm.split(":");
        bookid = fields[0];

        String[] ranges = fields[1].split(",");
        for (int i = 0; i < ranges.length; i++) {
            String[] range = ranges[i].split("-");
            int start = Integer.parseInt(range[0]);
            int end = start;

            if (range.length > 1) {
                end = Integer.parseInt(range[1]);
            }
            pages.set(start, end + 1);
        }

    }

    public boolean implies(Permission permission) {
        BookPermission bp = (BookPermission) permission;

        if (!bookid.equals(bp.bookid)) {
            System.out.println("Book with Book id: " + bookid + " not found. ");
            return false;
        }
        BitSet pgs = (BitSet) this.pages.clone();
        pgs.or(bp.pages);
        System.out.println("Following Pages are granted Permission. ");
        System.out.println(pgs);
        return true;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof BookPermission)) {
            return false;
        }

        BookPermission bp = (BookPermission) obj;
        return pages.equals(bp.pages) && bookid.equals(bp.bookid);

    }

    public static void main(String args[]) {
        Permission p1 = new BookPermission("123:1-3,5,7,10");

        System.out.println("Bookid:Pages(Book stored)--123:1-3,5,7,10\n");

        Permission p2 = new BookPermission("123:2");
        System.out.println("Bookid:Pages(Checking)--123:2");
        boolean b = p1.implies(p2);

        System.out.println("Access Granted: " + b + "\n");

        p2 = new BookPermission("123:3");
        System.out.println("Bookid:Pages(Checking)--123:3");
        b = p1.implies(p2);
        System.out.println("Access Granted " + b + "\n");
        p2 = new BookPermission("123:3,8-9,20");

        System.out.println("Bookid:Pages(Checking)--123:3,8-9,20");
        b = p1.implies(p2);
        System.out.println("Access Granted: " + b + "\n");

        p2 = new BookPermission("123:3-5");
        System.out.println("Bookid:Pages(Checking)--123:3-5");

        b = p1.implies(p2);
        System.out.println("Access Granted: " + b + "\n");

        p2 = new BookPermission("123:1-3");
        System.out.println("Bookid:Pages(Checking)--123:1-3");

        b = p1.implies(p2);
        System.out.println("Access Granted: " + b + "\n");
    }
}
