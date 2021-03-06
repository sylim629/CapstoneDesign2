package manager;

import java.util.List;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.exception.FacebookOAuthException;
import com.restfb.types.User;

public class FriendsList {

	private List<User> friends = null;

	public FriendsList() {
		DefaultFacebookClient fbClient = new DefaultFacebookClient(LoginManager.sharedInstance().getaccessTok());
		try {
			friends = fbClient.fetchConnection("/me/friends", User.class, Parameter.with("fields", "id, name"))
					.getData();
		} catch (FacebookOAuthException e) {
			e.printStackTrace();
		}
	}

	public List<User> getFriendsList() {
		return friends;
	}

	public void printfriend() {
		for (int i = 0; i < friends.size(); i++) {
			User user = friends.get(i);
			System.out.printf("%s %s\n", user.getId(), user.getName());
		}
	}

	public String getFriendID(String name) {
		for (int i = 0; i < friends.size(); i++) {
			User user = friends.get(i);
			if (user.getName().equals(name)) {
				return user.getId();
			}
		}
		
		return "fail";
	}

	public String getFriendName(String id) {
		for (int i = 0; i < friends.size(); i++) {
			User user = friends.get(i);
			if (user.getId().equals(id))
				return user.getName();
		}
		
	     String accessToken = LoginManager.sharedInstance().getaccessTok();
	      FacebookClient fbClient = new DefaultFacebookClient(accessToken);
	      User me = fbClient.fetchObject("me", User.class, Parameter.with("fields", "first_name,last_name"));
	      String name = me.getLastName()+" "+me.getFirstName();   // facebook user id

		
		return name;
	}
}