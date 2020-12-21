package com.filecloud.adminservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.filecloud.adminservice.client.AuthServerClient;
import com.filecloud.adminservice.client.DocumentServiceClient;
import com.filecloud.adminservice.model.dto.SingleIdRequestDto;
import com.filecloud.adminservice.response.Response.Status;
import com.filecloud.adminservice.response.Result;
import com.filecloud.adminservice.security.util.AuthUtil;


@Service
public class AdminService extends BaseService {

	private final AuthServerClient authClient;

	private final DocumentServiceClient documentClient;

	@Autowired
	public AdminService(AuthServerClient authClient, DocumentServiceClient documentClient) {
		this.authClient = authClient;
		this.documentClient = documentClient;
	}

	public Result<?> listUsers() {
		return authClient.listUsers(AuthUtil.getBearerToken());
	}

	public Result<?> activeUsers() {
		return authClient.activeUsers(AuthUtil.getBearerToken());
	}

	public Result<?> inactiveusers() {
		return authClient.inactiveusers(AuthUtil.getBearerToken());
	}

	public Result<?> getUser(long userId) {
		return authClient.getUser(AuthUtil.getBearerToken(), userId);
	}

	public Result<?> enableUser(SingleIdRequestDto dto) {
		return authClient.enableUser(AuthUtil.getBearerToken(), dto);
	}

	public Result<?> disableUser(SingleIdRequestDto dto) {
		return authClient.disableUser(AuthUtil.getBearerToken(), dto);
	}

	public Result<?> deleteUser(SingleIdRequestDto dto) {
		Result<?> result = authClient.deleteUser(AuthUtil.getBearerToken(), dto);

		if (result.isSuccess() && result.getStatusCode() == Status.ALL_OK.getStatusCode())
			documentClient.deleteUserDocuments(AuthUtil.getBearerToken(), dto);
		else
			error(result.getMessage());

		return result;
	}

	public Result<?> getActiveUserCount() {
		return authClient.activeUserCount(AuthUtil.getBearerToken());
	}

	public Result<?> getInActiveUserCount() {
		return authClient.inActiveUserCount(AuthUtil.getBearerToken());
	}

	public Result<?> getAllUsersCount() {
		return authClient.allUsersCount(AuthUtil.getBearerToken());
	}

	public Result<?> listRoleAdmin() {
		return authClient.listRoleAdmin(AuthUtil.getBearerToken());
	}

	public Result<?> listRoleUser() {
		return authClient.listRoleUser(AuthUtil.getBearerToken());
	}

}
