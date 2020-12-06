package com.filecloud.authserver.security.oauth2;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.filecloud.authserver.security.dto.AuthUserDetail;
import com.filecloud.authserver.security.dto.UserSession;
import com.filecloud.authserver.util.ConstUtil;


@RestController
public class AuthCheckTokenEndpoint {

	private ResourceServerTokenServices resourceServerTokenServices;

	private AccessTokenConverter accessTokenConverter = new DefaultAccessTokenConverter();

	protected final Log logger = LogFactory.getLog(getClass());

	private WebResponseExceptionTranslator<OAuth2Exception> exceptionTranslator = new DefaultWebResponseExceptionTranslator();

	@Autowired
	public AuthCheckTokenEndpoint(ResourceServerTokenServices resourceServerTokenServices) {
		this.resourceServerTokenServices = resourceServerTokenServices;
	}

	/**
	 * @param exceptionTranslator
	 *        the exception translator to set
	 */
	public void setExceptionTranslator(WebResponseExceptionTranslator<OAuth2Exception> exceptionTranslator) {
		this.exceptionTranslator = exceptionTranslator;
	}

	/**
	 * @param accessTokenConverter
	 *        the accessTokenConverter to set
	 */
	public void setAccessTokenConverter(AccessTokenConverter accessTokenConverter) {
		this.accessTokenConverter = accessTokenConverter;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/oauth/check_token")
	@ResponseBody
	public Map<String, ?> checkToken(@RequestParam("token") String value) {

		OAuth2AccessToken token = resourceServerTokenServices.readAccessToken(value);
		if (token == null) {
			throw new InvalidTokenException("Token was not recognised");
		}

		if (token.isExpired()) {
			throw new InvalidTokenException("Token has expired");
		}

		OAuth2Authentication authentication = resourceServerTokenServices.loadAuthentication(token.getValue());

		Map<String, Object> response = (Map<String, Object>) accessTokenConverter.convertAccessToken(token, authentication);

		// Put user session to avoid extra call from resources servers (to get user id and full name)
		AuthUserDetail authUserDetail = (AuthUserDetail) authentication.getPrincipal();
		UserSession session = new UserSession(authUserDetail.getUserId(), authUserDetail.getFullName(), authUserDetail.getUsername());
		response.put(ConstUtil.KEY_SESSION, session);

		// gh-1070
		response.put("active", true); // Always true if token exists and not expired

		return response;
	}

	@ExceptionHandler(InvalidTokenException.class)
	public ResponseEntity<OAuth2Exception> handleException(Exception e) throws Exception {
		logger.info("Handling error: " + e.getClass().getSimpleName() + ", " + e.getMessage());
		// This isn't an oauth resource, so we don't want to send an
		// unauthorized code here. The client has already authenticated
		// successfully with basic auth and should just
		// get back the invalid token error.
		@SuppressWarnings("serial")
		InvalidTokenException e400 = new InvalidTokenException(e.getMessage()) {
			@Override
			public int getHttpErrorCode() {
				return 400;
			}
		};
		return exceptionTranslator.translate(e400);
	}
}
