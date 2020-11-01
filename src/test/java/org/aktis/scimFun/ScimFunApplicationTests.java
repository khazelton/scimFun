package org.aktis.scimFun;

import com.unboundid.scim2.client.ScimService;
import com.unboundid.scim2.common.types.Email;
import com.unboundid.scim2.common.types.Name;
import com.unboundid.scim2.common.types.UserResource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.util.Collections;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class ScimFunApplicationTests {

	@Test
	void contextLoads() throws Exception {
		assertThat(true);

		// code for tests goes here
		ClientBuilder clientBuilder = ClientBuilder.newBuilder();
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target("https://example.com/scim/v2");
		ScimService scimService = new ScimService(target);

		// Create a user
		UserResource user = new UserResource();
		user.setUserName("babs");
		user.setPassword("secret");
		Name name = new Name()
				.setGivenName("Barbara")
				.setFamilyName("Jensen");
		user.setName(name);
		Email email = new Email()
				.setType("home")
				.setPrimary(true)
				.setValue("babs@example.com");
		user.setEmails(Collections.singletonList(email));

	}

}

