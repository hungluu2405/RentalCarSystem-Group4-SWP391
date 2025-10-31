package service.account;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import model.GoogleUser;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import util.Constants;

public class LoginGoogleHandlerService {

    public String getAccessToken(String code) throws Exception {
        String responseStr = Request.Post(Constants.GOOGLE_LINK_GET_TOKEN)
                .bodyForm(Form.form()
                        .add("client_id", Constants.GOOGLE_CLIENT_ID)
                        .add("client_secret", Constants.GOOGLE_CLIENT_SECRET)
                        .add("redirect_uri", Constants.GOOGLE_REDIRECT_URI)
                        .add("code", code)
                        .add("grant_type", Constants.GOOGLE_GRANT_TYPE).build())
                .execute().returnContent().asString();

        JsonObject jobj = new Gson().fromJson(responseStr, JsonObject.class);
        return jobj.get("access_token").getAsString();
    }

    public GoogleUser getGoogleUserInfo(String accessToken) throws Exception {
        String responseStr = Request.Get(Constants.GOOGLE_LINK_GET_USER_INFO + accessToken)
                .execute().returnContent().asString();
        return new Gson().fromJson(responseStr, GoogleUser.class);
    }
}
