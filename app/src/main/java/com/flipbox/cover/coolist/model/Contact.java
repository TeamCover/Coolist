package com.flipbox.cover.coolist.model;

/**
 * Created by Agus on 16/06/2015.
 * mistiawanagus@gmail.com
 * twitter @mistiawanagus
 */
public class Contact {
    private String firstName,lastName,phone,twitter,facebook,linkedin, thumbnailUrl, email;

    public Contact(){
    }

    public Contact(String firstName, String lastName, String phone, String twitter, String facebook, String linkedin, String thumbnailUrl, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.twitter = twitter;
        this.facebook = facebook;
        this.linkedin = linkedin;
        this.thumbnailUrl = thumbnailUrl;
        this.email = email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public void setLinkedin(String linkedin) {
        this.linkedin = linkedin;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    public String getTwitter() {
        return twitter;
    }

    public String getFacebook() {
        return facebook;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

}
