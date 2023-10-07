package com.bhuvnesh.tinyURL.Model;

//UrlDto is the url information we get from user.
//expirationDate is an optional field to be given by the user.
public class UrlDto
{
    private String url;
    private String expirationDate;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    @Override
    public String toString() {
        return "UrlDto{" +
                "url='" + url + '\'' +
                ", expirationDate='" + expirationDate + '\'' +
                '}';
    }
}
