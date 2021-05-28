package protestspacec.om.Model;

public class Qoutation {

    String id, uid, pid, pname, pimage, pdescription, uname, uimage, price, description;
    boolean isAccepted;

    public Qoutation(){}

    public Qoutation(String id, String uid, String pid, String pname, String pimage, String pdescription, String uname, String uimage, String price, String description, boolean isAccepted) {
        this.id = id;
        this.uid = uid;
        this.pid = pid;
        this.pname = pname;
        this.pimage = pimage;
        this.pdescription = pdescription;
        this.uname = uname;
        this.uimage = uimage;
        this.price = price;
        this.description = description;
        this.isAccepted = isAccepted;
    }

    public boolean getIsAccepted() {
        return isAccepted;
    }

    public void setIsAccepted(boolean accepted) {
        isAccepted = accepted;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPimage() {
        return pimage;
    }

    public void setPimage(String pimage) {
        this.pimage = pimage;
    }

    public String getPdescription() {
        return pdescription;
    }

    public void setPdescription(String pdescription) {
        this.pdescription = pdescription;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUimage() {
        return uimage;
    }

    public void setUimage(String uimage) {
        this.uimage = uimage;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
