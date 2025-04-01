package com.example.blog.model.vo.blog;

public class blogAddVO {
    private String title;
    private String createTime;
    private String category;
    private String headImage;
    private String miniDesc;
    private String mdUrl;

    public blogAddVO() {}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getMiniDesc() {
        return miniDesc;
    }

    public void setMiniDesc(String miniDesc) {
        this.miniDesc = miniDesc;
    }

    public String getMdUrl() {
        return mdUrl;
    }

    public void setMdUrl(String mdUrl) {
        this.mdUrl = mdUrl;
    }
}
