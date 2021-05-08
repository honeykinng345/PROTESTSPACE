package protestspacec.om.Model;

public class Comment {
    String cId, comment, uId, uName, uImg;

    public Comment(){}

    public Comment(String cId, String comment, String uId, String uName, String uImg) {
        this.cId = cId;
        this.comment = comment;
        this.uId = uId;
        this.uName = uName;
        this.uImg = uImg;
    }

    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getuImg() {
        return uImg;
    }

    public void setuImg(String uImg) {
        this.uImg = uImg;
    }
}
