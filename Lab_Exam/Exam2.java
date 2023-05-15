//Md. Emon Khan
//Roll: 30
interface ListAddition {
  void addElement(int element);
  void removeElement(int element);
}
class MyList {
  int[] elements;
  int head;
  MyList() {
      elements = new int[100];
      head = -1;
  }
  public void display() {
      for (int i = 0; i <= head; i++) {
          System.out.print(elements[i] + " ");
      }
      System.out.println();
  }
  int search(int element) {
      for (int i = 0; i <= head; i++) {
          if (elements[i] == element) {
              return i;
          }
      }
      return -1;
  }
}
class UnsortedList extends MyList implements ListAddition {
  UnsortedList() {
      super();
  }
  public void addElement(int element) {
      if (head < elements.length - 1) {
          elements[++head] = element;
      }
  }
  public void removeElement(int element) {
      int index = search(element);
      if (index != -1) {
          elements[index] = elements[head];
          head--;
      }
  }
}
class SortedList extends MyList implements ListAddition {
  SortedList() {
      super();
  }
  @Override
  int search(int element) {
    int lo = 0, hi = head;
    while(lo <= hi) {
      int mid = lo + (hi - lo)/2;
      if(elements[mid] == element)return mid;
      else if(elements[mid] < element) lo = mid + 1;
      else hi = mid - 1;
    }
    return -1;
   }
  public void addElement(int element) {
    int indx = -1;
    for(int i = 0; i <= head; i++) {
      if(elements[i] > element ) {
        indx = i;
        break;
      }
    }
    if(indx == -1) {
      elements[++head] = element;
      return;
    }
    int temp = elements[indx];
    elements[indx] = element;
    for(int i = indx+1; i <= head+1; i++) {
      int now = -1;
      if(i <= head)now = elements[i];
      elements[i] = temp;
      temp = now;
    }
    head++;
  }
  public void removeElement(int element) {
    int indx = search(element);
    if(indx != -1) {
      for(int i = indx+1; i <= head; i++) {
        elements[i - 1] = elements[i];
      }
      head--;
    }
  }
}
public class Main {
    public static void main(String[] args) {
      UnsortedList list1 = new UnsortedList();
      list1.addElement(1);
      list1.addElement(2);
      list1.addElement(3);
      list1.addElement(4);
      list1.addElement(5);
      list1.addElement(6);
      list1.removeElement(6);
      list1.display();
      System.out.println(list1.search(6));

      SortedList list2 = new SortedList();
      list2.addElement(1);
      list2.addElement(5);
      list2.addElement(2);
      list2.addElement(3);
      list2.addElement(6);
      list2.addElement(4);
      list2.display();
      System.out.println(list2.search(3));
      list2.removeElement(3);;
      list2.display();
    }
}
