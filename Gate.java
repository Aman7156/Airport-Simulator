    public class Gate {
    private final String id;
    private boolean occupied ;//false

    public Gate(String id) 
    { this.id = id; 
    }
    public boolean isAvailable() {
         return !occupied; 
        }
    public void occupy() {
         occupied = true; 
        }
    public void release() {
         occupied = false; 
        }
    public String getId() {
         return id; 
        }
}

