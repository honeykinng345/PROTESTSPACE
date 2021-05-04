package protestspacec.om.Model;

public class Posts {

    String name,postDescription,postImage,postid,posttTitle,timeStamp,uid;

    public Posts() {


    }

    public Posts(String name, String postDescription, String postImage, String postid, String posttTitle, String timeStamp, String uid) {
        this.name = name;
        this.postDescription = postDescription;
        this.postImage = postImage;
        this.postid = postid;
        this.posttTitle = posttTitle;
        this.timeStamp = timeStamp;
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPostDescription() {
        return postDescription;
    }

    public void setPostDescription(String postDescription) {
        this.postDescription = postDescription;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getPosttTitle() {
        return posttTitle;
    }

    public void setPosttTitle(String posttTitle) {
        this.posttTitle = posttTitle;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
