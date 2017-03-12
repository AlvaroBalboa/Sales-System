package com.theironyard.charlotte;

/**
 * Created by souporman on 3/9/17.
 */
public class PageSettings {
    int id;
    int userId;
    boolean inCart=false;
    boolean inStock=true;
    boolean canReview=false;
    boolean wrongPass=false;
    boolean userTaken=false;
    boolean createUser=false;


    public PageSettings(int id,int userId) {
        this.id=id;
        this.userId=userId;
    }

    public boolean isCreateUser() {
        return createUser;
    }

    public void setCreateUser(boolean createUser) {
        this.createUser = createUser;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean isInCart() {
        return inCart;
    }

    public void setInCart(boolean inCart) {
        this.inCart = inCart;
    }

    public boolean isInStock() {
        return inStock;
    }

    public void setInStock(boolean inStock) {
        this.inStock = inStock;
    }

    public boolean isCanReview() {
        return canReview;
    }

    public void setCanReview(boolean canReview) {
        this.canReview = canReview;
    }

    public boolean isWrongPass() {
        return wrongPass;
    }

    public void setWrongPass(boolean wrongPass) {
        this.wrongPass = wrongPass;
    }

    public boolean isUserTaken() {
        return userTaken;
    }

    public void setUserTaken(boolean userTaken) {
        this.userTaken = userTaken;
    }
}
