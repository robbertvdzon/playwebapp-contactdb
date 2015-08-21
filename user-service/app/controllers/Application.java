package controllers;

import models.User;
import models.Users;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;
import java.util.UUID;

import static play.libs.Json.toJson;

public class Application extends Controller {

    public Result addUser() {
        User user = Form.form(User.class).bindFromRequest().get();
        user.setUuid(UUID.randomUUID().toString());
        user.save();
        return ok(toJson(user));
    }

    public Result getUsers() {
        List<User> users = User.find.all();
        return ok(toJson(new Users(users)));
    }

    public Result getUser(String uuid) {
        List<User> users = User.find.where().eq("uuid", uuid).findList();
        if (!users.isEmpty()) {
            return ok(toJson(users.get(0)));
        }
        return notFound();
    }

    public Result deleteUser(String uuid) {
        List<User> users = User.find.where().eq("uuid", uuid).findList();
        if (!users.isEmpty()) {
            users.get(0).delete();
            return ok();
        }
        return notFound();
    }

}
