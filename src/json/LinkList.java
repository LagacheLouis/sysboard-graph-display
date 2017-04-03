package json;

public class LinkList<T> {

    private T stuff;
    private LinkList<T> next;
    
        public LinkList(T stuff, LinkList<T> next) {
            super();
            this.stuff = stuff;
            this.next = next;
        }
        
        public T getStuff() {
            return stuff;
        }
        public void setStuff(T stuff) {
            this.stuff = stuff;
        }
        public LinkList<T> getNext() {
            return next;
        }
        public void setNext(LinkList<T> next) {
            this.next = next;
        }
        
        public String toString() {
                if (this.getNext() == null)
                        return this.getStuff().toString();
                else
                        return this.getStuff().toString() + ", \n" + this.getNext().toString();
        }
        
        public int size()
        {
                if(getNext() == null){
                        return 1;
                }
                    return 1 + this.getNext().size();
        }
        
        public boolean contains(T item)
        {
                if(this.getStuff().equals(item))return true;
                else if(this.getNext() == null)return false;        
                else return this.getNext().contains(item);
        }
        
        public void insert(T t,int i)
        {
                if(i == 0){
                        this.setNext(new LinkList<T>(this.getStuff(),this.getNext()));
                        this.setStuff(t);
                }
                else
                        this.getNext().insert(t, i-1);
        }
        
        public LinkList() {
                
                // TODO Auto-generated constructor stub
        }

}