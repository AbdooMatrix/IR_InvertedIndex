package invertedIndex;

public class printPostingList {

    public void printPostingList(Posting p) {
        System.out.print("[");
        while (p != null) {
            System.out.print("" + p.docId);
            if (p.next != null) {
                System.out.print(",");
            }
            p = p.next;
        }
        System.out.println("]");
    }
}